package com.lhsystems.monitoring.dto;

import lombok.Data;

@Data
public class SigarStaticCpuInfo {
	
	long cpuCacheSize, cpuCoresPerSocket, cpuMhz, cpuTotalCores;
	String cpuModel, cpuVendor;
}