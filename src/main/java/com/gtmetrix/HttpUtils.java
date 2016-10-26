package com.gtmetrix;

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
public abstract class HttpUtils {

	public HttpPost getHttpPost(){
        return new HttpPost("https://gtmetrix.com/api/0.1/test");
    }

    public HttpGet httpGetBuilder(String test_id) {
        return new HttpGet("https://gtmetrix.com/api/0.1/test/"+ test_id);
    }

	private CloseableHttpClient client() {
		Properties config = new Properties();

		try {
			InputStream in = new FileInputStream("/home/reuben/work/java_unit/config.properties");
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

    public CloseableHttpResponse sendHttpRequest(HttpGet http_get) throws NullPointerException {
        try {
            return client().execute(http_get);
        }
        catch (IOException ioe){
            System.out.print(ioe.getMessage());
			throw new NullPointerException();
        }
    }

    public CloseableHttpResponse sendHttpRequest(HttpPost http_post, String url) throws NullPointerException {
        try {
            List<NameValuePair> formData = new ArrayList<>();
            formData.add(new BasicNameValuePair("url",url));
            formData.add(new BasicNameValuePair("x-metrix-adblock","0"));

            http_post.setEntity(new UrlEncodedFormEntity(formData, Consts.UTF_8));
            return client().execute(http_post);
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