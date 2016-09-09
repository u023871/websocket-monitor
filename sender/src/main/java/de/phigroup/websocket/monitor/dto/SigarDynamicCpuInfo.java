package de.phigroup.websocket.monitor.dto;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;

import lombok.Data;

@Data
public class SigarDynamicCpuInfo {
	
	long idleTime, irqTime, niceTime, softIrqTime, stolenTime, sysTime, totalTime, userTime, waitTime;
	double idlePerc, irqPerc, nicePerc, softIrqPerc, stolenPerc, sysPerc, combinedPerc, userPerc, waitPerc;
	int cpuNumber;
	
	public void fill(Cpu cpu, CpuPerc cpuPerc, int cpuNumber) {
		
		this.cpuNumber = cpuNumber;
		
		idleTime = cpu.getIdle(); // Get the Total system cpu idle time.
		irqTime = cpu.getIrq(); // Get the Total system cpu time servicing interrupts.
		niceTime = cpu.getNice(); // Get the Total system cpu nice time.
		softIrqTime = cpu.getSoftIrq(); // Get the Total system cpu time servicing softirqs.
		stolenTime = cpu.getStolen(); // Get the Total system cpu involuntary wait time.
		sysTime = cpu.getSys(); // Get the Total system cpu kernel time.
		totalTime = cpu.getTotal(); // Get the Total system cpu time.
		userTime = cpu.getUser(); // Get the Total system cpu user time.
		waitTime = cpu.getWait(); // Get the Total system cpu io wait time.
		
		idlePerc = cpuPerc.getIdle();
		irqPerc = cpuPerc.getIrq();
		nicePerc = cpuPerc.getNice();
		softIrqPerc = cpuPerc.getSoftIrq();
		stolenPerc = cpuPerc.getStolen();
		sysPerc = cpuPerc.getSys();
		combinedPerc = cpuPerc.getCombined();
		userPerc = cpuPerc.getUser();
		waitPerc = cpuPerc.getWait();
	}
}