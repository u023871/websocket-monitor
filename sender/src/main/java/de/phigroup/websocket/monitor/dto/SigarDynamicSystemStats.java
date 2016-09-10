package de.phigroup.websocket.monitor.dto;

import java.util.ArrayList;

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
public class SigarDynamicSystemStats {

	double cpuPercCombined, memPercUsed, uptime;

	@JsonProperty
	ArrayList<SigarDynamicCpuInfo> dynamicCpuInfos;

	@JsonIgnore
	public static SigarDynamicSystemStats getSigarSystemStatistics() throws SigarException {

		SigarDynamicSystemStats stats = new SigarDynamicSystemStats();

		Sigar sigar = new Sigar();
		Mem mem = null;
		CpuPerc cpuperc = null;
		FileSystemUsage filesystemusage = null;

		mem = sigar.getMem();
		cpuperc = sigar.getCpuPerc();
		filesystemusage = sigar.getFileSystemUsage("C:");

		Uptime uptime = new Uptime();

		stats.setCpuPercCombined((cpuperc.getCombined() * 100));
		stats.setMemPercUsed(mem.getUsedPercent());
		stats.setUptime(uptime.getUptime());

		fillDynamicCpuInfo(sigar, stats);

		return stats;
	}

	/**
	 * fill dynamic CPU info
	 * 
	 * @throws SigarException
	 */
	private static void fillDynamicCpuInfo(Sigar sigar, SigarDynamicSystemStats stats) throws SigarException {

		Cpu[] cpus = sigar.getCpuList();
		CpuPerc[] cpuPercs = sigar.getCpuPercList();

		for (int i = 0; i < cpus.length; i++) {

			Cpu cpu = cpus[i];
			CpuPerc cpuPerc = cpuPercs[i];

			if (stats.getDynamicCpuInfos() == null) {
				stats.setDynamicCpuInfos(new ArrayList<>());
			}

			SigarDynamicCpuInfo dyn = new SigarDynamicCpuInfo();
			dyn.fill(cpu, cpuPerc, i);

			stats.getDynamicCpuInfos().add(dyn);
		}
	}

}
