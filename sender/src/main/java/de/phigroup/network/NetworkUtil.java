package de.phigroup.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import de.phigroup.http.client.SpringHttpClient;
import lombok.extern.slf4j.Slf4j;

/**
 * util for network related checks
 * 
 * @author u023871
 *
 */
@Slf4j
public class NetworkUtil {

	public static final String WINDOWS_CODE_PAGE = "CP437";

	/**
	 * @param internetProtocolAddress
	 *            The internet protocol address to ping
	 * @return True if the address is responsive, false otherwise
	 * @throws IOException
	 */
	public static boolean isReachable(String internetProtocolAddress) throws IOException {

		boolean pingable = true;

		List<String> command = new ArrayList<>();
		command.add("ping");

		if (SystemUtils.IS_OS_WINDOWS) {
			command.add("-n");
		} else if (SystemUtils.IS_OS_UNIX) {
			command.add("-c");
		} else {
			throw new UnsupportedOperationException("Unsupported operating system");
		}

		command.add("1");
		command.add(internetProtocolAddress);

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Process process = processBuilder.start();

		Charset charset = Charset.defaultCharset();

		// for some reason on windows we need to use this code page
		if (SystemUtils.IS_OS_WINDOWS && Charset.isSupported(WINDOWS_CODE_PAGE)) {

			charset = Charset.forName(WINDOWS_CODE_PAGE);
		}

		BufferedReader standardOutput = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));

		String outputLine;

		while ((outputLine = standardOutput.readLine()) != null) {

			log.debug(outputLine);

			// Picks up Windows and Unix unreachable hosts
			if (outputLine.toLowerCase().contains("destination host unreachable")) {
				pingable = false;
			}
			// ping to localhost could be blocked
			else if (outputLine.toLowerCase().contains("allgemeiner fehler")) {
				pingable = false;
			}
		}

		return pingable;
	}

	/**
	 * check whether some web address is available
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static boolean isWebServiceOnline(String url) throws Exception {

		boolean online = false;

		SpringHttpClient client = new SpringHttpClient("http://localhost:9090");

		try {
			@SuppressWarnings("unused")
			String response = client.get(url);
//			log.debug("response  : " + response);

			HttpStatus httpStatus = client.getStatus();

			if(httpStatus != null) {
				
				if(httpStatus.is2xxSuccessful()) {
					
					online = true;
				}
				
				log.debug("httpStatus: " + httpStatus.value());
			}
		} catch (RestClientException e) {

			online = false;
			log.debug(e.getMessage(), e);
		} catch (Exception e) {

			online = false;
			throw e;
		}
		

		return online;
	}

}