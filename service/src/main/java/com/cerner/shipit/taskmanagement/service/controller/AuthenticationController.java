package com.cerner.shipit.taskmanagement.service.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	/**
	 * This will authenticate the user and send the response.
	 *
	 * @param userName
	 * @param password
	 */
	@GetMapping(value = "/authUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public void authenticateUser(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);

		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet("https://jira2.cerner.com");
			String auth = new String(Base64.encodeBase64((userName + ":" + password).getBytes()));
			httpGet.addHeader("Authorization", "BASIC " + auth);
			CloseableHttpResponse response = client.execute(httpGet);
			System.out.println(response);
			client.close();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
	}

	public static void main(String[] args) {
		URL jiraURL;
		try {
			jiraURL = new URL("https://jira2.cerner.com");
			HttpURLConnection connection = (HttpURLConnection) jiraURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			String userCredentials = "HK063220:***********";
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			connection.setRequestProperty("Authorization", basicAuth);
			connection.connect();
			Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			for (int c; (c = in.read()) >= 0; System.out.print((char) c)) {
				;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
