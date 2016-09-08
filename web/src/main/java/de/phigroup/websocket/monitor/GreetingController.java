package de.phigroup.websocket.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

	static final String LINE_SEP = System.getProperty("line.separator");
	
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
//        Thread.sleep(1000); // simulated delay
        
//        Map<String, String> map =  System.getenv();
//        for(String key : map.keySet()) {
//        	
//        	System.out.println(key + ": " + map.get(key));
//        	
//        }
        String out = executeCommand();
        
        return new Greeting("Hello, " + message.getName() + "!" + LINE_SEP + out);
    }

	private String executeCommand() {

		Lines session = new Lines();
		session.setListLines(new ArrayList<>());

		session.setListLines(new ArrayList<>());

		try {
			String command = "netstat";

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);
			Process process = pb.start();

			// pb=new ProcessBuilder("dir");
			// pb.redirectErrorStream(true);
			// process=pb.start();

			BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = null;
			do {

				// do something with commandline output.
				line = inStreamReader.readLine();

				if (line != null) {
					session.getListLines().add(line);
				}

			} while (line != null);

			System.out.println(session.getListLines());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String lines = "";
		for (String line : session.listLines) {
			lines += line + LINE_SEP;
			
		}
		

		return lines;

	}
    
}
