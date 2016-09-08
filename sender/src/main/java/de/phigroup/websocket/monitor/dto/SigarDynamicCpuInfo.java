package de.phigroup.websocket.monitor.dto;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;

import lombok.Data;

@Data
public class SigarDynamicCpuInfo {
	
	long idle, irq, nice, softIrq, stolen, sys, total, user, wait;
	double idlePerc, irqPerc, nicePerc, softIrqPerc, stolenPerc, sysPerc, combinedPerc, userPerc, waitPerc;
	
	public void fill(Cpu cpu, CpuPerc cpuPerc) {
		
		idle = cpu.getIdle(); // Get the Total system cpu idle time.
		irq = cpu.getIrq(); // Get the Total system cpu time servicing interrupts.
		nice = cpu.getNice(); // Get the Total system cpu nice time.
		softIrq = cpu.getSoftIrq(); // Get the Total system cpu time servicing softirqs.
		stolen = cpu.getStolen(); // Get the Total system cpu involuntary wait time.
		sys = cpu.getSys(); // Get the Total system cpu kernel time.
		total = cpu.getTotal(); // Get the Total system cpu time.
		user = cpu.getUser(); // Get the Total system cpu user time.
		wait = cpu.getWait(); // Get the Total system cpu io wait time.
		
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