package de.phigroup.websocket.monitor.dto;

import lombok.Data;

@Data
public class SigarStaticCpuInfo {
	
	long cpuCacheSize, cpuCoresPerSocket, cpuMhz, cpuTotalCores;
	String cpuModel, cpuVendor;
}