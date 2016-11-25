package com.gtmetrix.Services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SlackService {

	private HttpPost getHttpPost(){
		Properties config = new Properties();
		HttpPost httpPost = null;
		try {
			InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
			config.load(in);
			String slackUrl = config.getProperty("slack_url");
			httpPost = new HttpPost(slackUrl);

		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		return httpPost;
	}

	public CloseableHttpResponse sendSlackMessage(String message) throws IOException {
		CloseableHttpResponse response = null;
		HttpPost httpPost = getHttpPost();
		CloseableHttpClient httpClient = HttpClients.createDefault();

		httpPost.setEntity(new StringEntity("{'text':'" + message + "'}",ContentType.create("application/json")));
		try {
			response = httpClient.execute(httpPost);
		}
		catch (IOException ioe){
			System.out.println(ioe.getMessage());
		}
		return response;
	}
}
