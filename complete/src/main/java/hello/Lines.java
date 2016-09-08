package hello;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Lines { 
	
	ArrayList<String> listLines;
	
	String current;
}