package de.phigroup.websocket.monitor.dto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class SigarStaticSystemInfo {

	String fqdn;
	
	ArrayList<SigarStaticFileSystemInfo> staticFileSystemInfos;
}