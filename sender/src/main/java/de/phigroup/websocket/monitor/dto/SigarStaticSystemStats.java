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

	double cpuPercCombined, memPercUsed, uptime;

	@JsonProperty
	SigarStaticCpuInfo staticCpuInfo;
	@JsonProperty
	SigarStaticSystemInfo staticSystemInfo;

	@JsonIgnore
	public static SigarStaticSystemStats getSigarSystemStatistics() throws SigarException {

		SigarStaticSystemStats stats = new SigarStaticSystemStats();

		Sigar sigar = new Sigar();
	
		fillStaticCpuInfo(sigar, stats);

		fillStaticSystemInfo(sigar, stats);

		return stats;
	}

	/**
	 * fill static CPU info
	 * 
	 * @throws SigarException
	 */
	private static void fillStaticCpuInfo(Sigar sigar, SigarStaticSystemStats stats) throws SigarException {

		SigarStaticCpuInfo sigarCpuInfo = new SigarStaticCpuInfo();
		CpuInfo cpuInfo = sigar.getCpuInfoList()[0];
		sigarCpuInfo.setCpuCacheSize(cpuInfo.getCacheSize());
		sigarCpuInfo.setCpuCoresPerSocket(cpuInfo.getCoresPerSocket());
		sigarCpuInfo.setCpuMhz(cpuInfo.getMhz());
		sigarCpuInfo.setCpuTotalCores(cpuInfo.getTotalCores());
		sigarCpuInfo.setCpuModel(cpuInfo.getModel());
		sigarCpuInfo.setCpuVendor(cpuInfo.getVendor());

		stats.setStaticCpuInfo(sigarCpuInfo);
	}

	/**
	 * fill static system info
	 * 
	 * @throws SigarException
	 */
	private static void fillStaticSystemInfo(Sigar sigar, SigarStaticSystemStats stats) throws SigarException {

		SigarStaticSystemInfo sigarStaticSystemInfo = new SigarStaticSystemInfo();

		sigarStaticSystemInfo.setFqdn(sigar.getFQDN());

		if (sigarStaticSystemInfo.getStaticFileSystemInfos() == null) {
			sigarStaticSystemInfo.setStaticFileSystemInfos(new ArrayList<>());
		}
		
		// returns a collection of file system mounts
		FileSystem[] fss = sigar.getFileSystemList();
		for (FileSystem fs : fss) {

			SigarStaticFileSystemInfo fsi = new SigarStaticFileSystemInfo();
			
			fsi.setDevName(fs.getDevName()); // e.g. \\phigroup-nas\Download
			fsi.setDirName(fs.getDirName()); // e.g. Z:\
			fsi.setFlags(fs.getFlags()); // e.g. 0
			fsi.setOptions(fs.getOptions()); // e.g. rw
			fsi.setSysTypeName(fs.getSysTypeName()); // e.g. NTFS or cdrom
			fsi.setTypeName(fs.getTypeName()); // e.g. remote or cdrom
			fsi.setType(fs.getType()); // e.g. 3

			sigarStaticSystemInfo.getStaticFileSystemInfos().add(fsi);
		}

		stats.setStaticSystemInfo(sigarStaticSystemInfo);
		
		System.out.println(stats.getStaticSystemInfo());
	}
}
