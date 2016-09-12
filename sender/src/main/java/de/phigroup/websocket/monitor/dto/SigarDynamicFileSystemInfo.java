package de.phigroup.websocket.monitor.dto;

import org.hyperic.sigar.FileSystemUsage;

import lombok.Data;

@Data
public class SigarDynamicFileSystemInfo {
	
	String devName;
	long avail, files, free, total, used, freeFiles, diskReadBytes, diskReads, diskWriteBytes, diskWrites;
	double usePercent, diskQueue, diskServiceTime;
	
	public void fill(FileSystemUsage fileSystemUsage, String devName) {
	
		this.devName = devName;
		this.avail = fileSystemUsage.getAvail();
		this.files = fileSystemUsage.getFiles();
		this.free = fileSystemUsage.getFree();
		this.freeFiles = fileSystemUsage.getFreeFiles();
		this.diskQueue = fileSystemUsage.getDiskQueue();
		this.diskReadBytes = fileSystemUsage.getDiskReadBytes();
		this.diskReads = fileSystemUsage.getDiskReads();
		this.diskServiceTime = fileSystemUsage.getDiskServiceTime();
		this.diskWriteBytes = fileSystemUsage.getDiskWriteBytes();
		this.diskWrites = fileSystemUsage.getDiskWrites();
		this.total = fileSystemUsage.getTotal();
		this.used = fileSystemUsage.getUsed();
		this.usePercent = fileSystemUsage.getUsePercent();
	}
}