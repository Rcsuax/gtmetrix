package com.gtmetrix.Services;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("WeakerAccess")
public abstract class HttpUtilService {

	public abstract HttpPost getHttpPost();

    public abstract HttpGet getHttpGet(String testId);

	private CloseableHttpClient client() {
		Properties config = new Properties();

		try {
			InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
			config.load(in);
			String user = config.getProperty("api_user");
			String api_key = config.getProperty("api_key");

			CredentialsProvider credentials = new BasicCredentialsProvider();
			credentials.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials(user, api_key));
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
			in.close();
			return httpclient;
		} catch (IOException io) {
			System.out.println(io.getMessage());
			throw new NullPointerException();
		}
	}

    public CloseableHttpResponse sendHttpRequest(HttpGet httpGet) {
        try {
            return client().execute(httpGet);
        }
        catch (IOException ioe){
            System.out.print(ioe.getMessage());
			throw new NullPointerException();
        }
    }

    public CloseableHttpResponse sendHttpRequest(HttpPost httpPost, String url) {
        try {
            List<NameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair("url",url));
            formData.add(new BasicNameValuePair("x-metrix-adblock","0"));

            httpPost.setEntity(new UrlEncodedFormEntity(formData, Consts.UTF_8));
            return client().execute(httpPost);
        }
        catch (IOException ioe){
            System.out.print(ioe.getMessage());
            throw new NullPointerException();
        }
    }

    public String readHttpResponse(CloseableHttpResponse response) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            while (true) {
                String line = br.readLine();
                if (br.readLine() == null) {
                    System.out.println(line);
                    return line;
                }
            }
        }
        catch (IOException ioe){
            System.out.print(ioe.getMessage());
			throw new NullPointerException();
        }
    }
}