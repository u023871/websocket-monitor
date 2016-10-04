package de.phigroup.websocket.monitor.dto;

import java.util.ArrayList;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * google OperatingSystemMXBean for a simple alternative, but with very few
 * values only
 * 
 * Test Sigar stuff with
 * -Djava.library.path="D:\DEV\Workspaces\Spring\gs-messaging-stomp-websocket\stomp-websocket-client\src\main\resources\sigar\lib",
 * see http://stackoverflow.com/questions/11612711/sigar-unsatisfiedlinkerror!
 *
 * Sources see sigar\bindings\java\src at
 * https://github.com/hyperic/sigar/tree/master/bindings/java/src.
 * 
 * BTW: Do not use inner classes, this is crap and deserializing them with
 * ObjectMapper is hard if you browse the web...
 * 
 * @author u023871
 *
 */
@Data
public class SigarStaticSystemStats {

	@JsonProperty
	SigarStaticCpuInfo staticCpuInfo;
	
	@JsonProperty
	SigarStaticSystemInfo staticSystemInfo;
	
	@JsonProperty
	ArrayList<SigarStaticFileSystemInfo> staticFileSystemInfos;
	
	@JsonIgnore
	Sigar sigar;
	
	@JsonIgnore
	static SigarStaticSystemStats instance;

	/**
	 * hide default constructor
	 */
	private SigarStaticSystemStats() {
		
	}
	
	/**
	 * get filled singleton
	 * @return
	 * @throws SigarException
	 */
	public static SigarStaticSystemStats getFilledInstance() throws SigarException {
	
		if(SigarStaticSystemStats.instance == null) {
			
			SigarStaticSystemStats.instance = new SigarStaticSystemStats();
			
			SigarStaticSystemStats.instance.setSigar(new Sigar());
			
			SigarStaticSystemStats.instance.fill();
		}
		
		return SigarStaticSystemStats.instance;
	}
	
	public void fill() throws SigarException {

		this.setStaticCpuInfo(fillStaticCpuInfo());

		this.setStaticSystemInfo(fillStaticSystemInfo());

		this.setStaticFileSystemInfos(fillStaticFileSystemInfos());
	}

	/**
	 * fill static CPU info
	 * 
	 * @throws SigarException
	 */
	public SigarStaticCpuInfo fillStaticCpuInfo() throws SigarException {

		SigarStaticCpuInfo sigarCpuInfo = new SigarStaticCpuInfo();
		CpuInfo cpuInfo = getSigar().getCpuInfoList()[0];
		sigarCpuInfo.setCpuCacheSize(cpuInfo.getCacheSize());
		sigarCpuInfo.setCpuCoresPerSocket(cpuInfo.getCoresPerSocket());
		sigarCpuInfo.setCpuMhz(cpuInfo.getMhz());
		sigarCpuInfo.setCpuTotalCores(cpuInfo.getTotalCores());
		sigarCpuInfo.setCpuModel(cpuInfo.getModel());
		sigarCpuInfo.setCpuVendor(cpuInfo.getVendor());

		return sigarCpuInfo;
	}

	/**
	 * fill static system info
	 * 
	 * @throws SigarException
	 */
	public SigarStaticSystemInfo fillStaticSystemInfo() throws SigarException {

		SigarStaticSystemInfo sigarStaticSystemInfo = new SigarStaticSystemInfo();

		sigarStaticSystemInfo.setFqdn(getSigar().getFQDN());

		return sigarStaticSystemInfo;
	}
	

	/**
	 * fill static file system infos
	 * 
	 * @throws SigarException
	 */
	public ArrayList<SigarStaticFileSystemInfo> fillStaticFileSystemInfos() throws SigarException {

		ArrayList<SigarStaticFileSystemInfo> fileSystemInfos = new ArrayList<>();
		
		// returns a collection of file system mounts
		FileSystem[] fss = getSigar().getFileSystemList();
		for (FileSystem fs : fss) {

			SigarStaticFileSystemInfo fsi = new SigarStaticFileSystemInfo();
			
			fsi.setDevName(fs.getDevName()); // e.g. \\phigroup-nas\Download
			fsi.setDirName(fs.getDirName()); // e.g. Z:\
			fsi.setOptions(fs.getOptions()); // e.g. rw
			fsi.setSysTypeName(fs.getSysTypeName()); // e.g. NTFS or cdrom
			fsi.setTypeName(fs.getTypeName()); // e.g. remote or cdrom

			fileSystemInfos.add(fsi);
		}
		
		return fileSystemInfos;
	}
}
