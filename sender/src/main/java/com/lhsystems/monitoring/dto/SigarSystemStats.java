package com.lhsystems.monitoring.dto;

import java.util.ArrayList;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
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
 * google OperatingSystemMXBean for a simple alternative, but with very few values only
 * 
 * Test Sigar stuff with -Djava.library.path="D:\DEV\Workspaces\Spring\gs-messaging-stomp-websocket\stomp-websocket-client\src\main\resources\sigar\lib",
 * see http://stackoverflow.com/questions/11612711/sigar-unsatisfiedlinkerror!
 * 
 * BTW: Do not use inner classes, this is crap and deserializing them with ObjectMapper is hard if you browse the web...
 * 
 * @author u023871
 *
 */
@Data
public class SigarSystemStats {

	double cpuPercCombined, memPercUsed, uptime;
	@JsonProperty SigarStaticCpuInfo staticCpuInfo;
	@JsonProperty ArrayList<SigarDynamicCpuInfo> dynamicCpuInfos;
	
	@JsonIgnore
	public static SigarSystemStats getSigarSystemStatistics() throws SigarException {
		
		SigarSystemStats stats = new SigarSystemStats();
		
		Sigar sigar = new Sigar();		
	    Mem mem = null;
	    CpuPerc cpuperc = null;
	    FileSystemUsage filesystemusage = null;

	    mem = sigar.getMem();
        cpuperc = sigar.getCpuPerc();
        filesystemusage = sigar.getFileSystemUsage("C:");

        Uptime uptime = new Uptime();

	    System.out.print(mem.getUsedPercent()+"\t");
	    System.out.print((cpuperc.getCombined()*100)+"\t");
	    System.out.print(filesystemusage.getUsePercent()+"\n");
	    
	    stats.setCpuPercCombined((cpuperc.getCombined()*100));
	    stats.setMemPercUsed(mem.getUsedPercent());
	    stats.setUptime(uptime.getUptime());
	    
	    SigarStaticCpuInfo sigarCpuInfo = new SigarStaticCpuInfo();
	    CpuInfo cpuInfo = sigar.getCpuInfoList()[0];
	    sigarCpuInfo.setCpuCacheSize(cpuInfo.getCacheSize());
	    sigarCpuInfo.setCpuCoresPerSocket(cpuInfo.getCoresPerSocket());
	    sigarCpuInfo.setCpuMhz(cpuInfo.getMhz());
	    sigarCpuInfo.setCpuTotalCores(cpuInfo.getTotalCores());
	    sigarCpuInfo.setCpuModel(cpuInfo.getModel());
	    sigarCpuInfo.setCpuVendor(cpuInfo.getVendor());
	    stats.setStaticCpuInfo(sigarCpuInfo);
	    
	    
	    Cpu[] cpus = sigar.getCpuList();
	    CpuPerc[] cpuPercs = sigar.getCpuPercList();
	    
	    for (int i=0; i<cpus.length; i++) {
			
	    	Cpu cpu =  cpus[i];
	    	CpuPerc cpuPerc =  cpuPercs[i];
	    	
	    	if(stats.dynamicCpuInfos == null) {
	    		stats.dynamicCpuInfos = new ArrayList<>();
	    	}
	    	
	    	SigarDynamicCpuInfo dyn = new SigarDynamicCpuInfo();
	    	dyn.fill(cpu, cpuPerc);
	    	
	    	stats.dynamicCpuInfos.add(dyn);
		}
	 
	    return stats;
	}
	
}
