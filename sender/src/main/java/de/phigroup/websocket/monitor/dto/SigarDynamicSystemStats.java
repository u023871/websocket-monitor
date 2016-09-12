package de.phigroup.websocket.monitor.dto;

import java.util.ArrayList;
import java.util.Date;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Uptime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * google OperatingSystemMXBean for a simple alternative, but with very few
 * values only.
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
 * We cannot make this dynamic content a singleton.
 * 
 * @author u023871
 *
 */
@Data
@Slf4j
public class SigarDynamicSystemStats {

	double cpuPercCombined, memPercUsed, uptime;

	@JsonProperty
	ArrayList<SigarDynamicCpuInfo> dynamicCpuInfos;

	@JsonProperty
	ArrayList<SigarDynamicFileSystemInfo> dynamicFileSystemInfos;

	@JsonIgnore
	Sigar sigar;

	/**
	 * hide default constructor
	 */
	private SigarDynamicSystemStats() {
		
	}
	
	/**
	 * get filled new instance (we cannot make this dynamic content a singleton)
	 * @return
	 * @throws SigarException
	 */
	public static SigarDynamicSystemStats getNewFilledInstance() throws SigarException {
		
		SigarDynamicSystemStats stats = new SigarDynamicSystemStats();
		
		long timeBefore = new Date().getTime(), timeAfter = 0;
		System.out.println("timeBefore: " + timeBefore);
		stats.setSigar(new Sigar());
		timeAfter = new Date().getTime();
		System.out.println("timeAfter: " + timeAfter);
		System.out.println("Time to open Sigar instance: " + (timeBefore - timeAfter));
		
		stats.fill();
		
		return stats;
	}
	
	@JsonIgnore
	public void fill() throws SigarException {

		Mem mem = null;
		CpuPerc cpuperc = null;

		mem = getSigar().getMem();
		cpuperc = getSigar().getCpuPerc();

		Uptime uptime = new Uptime();

		this.setCpuPercCombined((cpuperc.getCombined() * 100));
		this.setMemPercUsed(mem.getUsedPercent());
		this.setUptime(uptime.getUptime());

		this.setDynamicCpuInfos(fillDynamicCpuInfo());

		this.setDynamicFileSystemInfos(fillDynamicFileSystemInfo());
	}

	/**
	 * fill dynamic CPU info
	 * 
	 * TODO: split into own feed
	 * 
	 * @throws SigarException
	 */
	public ArrayList<SigarDynamicCpuInfo> fillDynamicCpuInfo() throws SigarException {

		Sigar sigar = new Sigar();
		
		ArrayList<SigarDynamicCpuInfo> dynamicCpuInfos = new ArrayList<>();
		
		Cpu[] cpus = sigar.getCpuList();
		CpuPerc[] cpuPercs = sigar.getCpuPercList();

		for (int i = 0; i < cpus.length; i++) {

			Cpu cpu = cpus[i];
			CpuPerc cpuPerc = cpuPercs[i];

			SigarDynamicCpuInfo dyn = new SigarDynamicCpuInfo();
			dyn.fill(cpu, cpuPerc, i);

			dynamicCpuInfos.add(dyn);
		}
		
		return dynamicCpuInfos;
	}

	/**
	 * fill dynamic FS info
	 * 
	 * TODO: split into own feed
	 * 
	 * @throws SigarException
	 */
	public ArrayList<SigarDynamicFileSystemInfo> fillDynamicFileSystemInfo() throws SigarException {

		ArrayList<SigarDynamicFileSystemInfo> fileSystemInfos = new ArrayList<>();

		SigarStaticSystemStats g = SigarStaticSystemStats.getFilledInstance();
		ArrayList<SigarStaticFileSystemInfo> fsi = g.fillStaticFileSystemInfos();

		for (SigarStaticFileSystemInfo sigarStaticFileSystemInfo : fsi) {
			
			String devName = sigarStaticFileSystemInfo.getDevName();
			
			try {
				FileSystemUsage fileSystemUsage = getSigar().getFileSystemUsage(devName);
				SigarDynamicFileSystemInfo dynamicFileSystemInfo = new SigarDynamicFileSystemInfo();
				dynamicFileSystemInfo.fill(fileSystemUsage, devName);
				fileSystemInfos.add(dynamicFileSystemInfo);
				
			} catch (Exception e) {

				// catch because FS might not be accessible or down or whatever
				log.error(e.getMessage(), e);
			}
		}
		
		System.out.println("fileSystemInfos: " + fileSystemInfos);
		
		return fileSystemInfos;
		
	}
}
