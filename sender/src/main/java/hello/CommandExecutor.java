package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;

import com.lhsystems.monitoring.dto.Lines;

@Controller
public class CommandExecutor {

	static final String LINE_SEP = System.getProperty("line.separator");

	public String executeCommand() {

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
		for (String line : session.getListLines()) {
			lines += line + LINE_SEP;
			
		}
		return lines;

	}
    
}
