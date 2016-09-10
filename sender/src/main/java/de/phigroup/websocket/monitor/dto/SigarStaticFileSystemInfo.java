package de.phigroup.websocket.monitor.dto;

import lombok.Data;

@Data
public class SigarStaticFileSystemInfo {

	String devName, // e.g. \\phigroup-nas\Download
	dirName, // e.g. Z:\
	options, // e.g. rw
	sysTypeName, // e.g. NTFS or cdrom
	typeName; // e.g. remote or cdrom
	long flags; // e.g. 0
	int type; // e.g. 3
}