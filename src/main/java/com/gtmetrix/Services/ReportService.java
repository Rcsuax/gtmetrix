package com.gtmetrix.Services;

import com.gtmetrix.Models.TestResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ReportService {

	private static Map<String,String> getApiProperties() throws IOException{
		Properties config = new Properties();
		InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
		config.load(in);
		final String user = config.getProperty("api_user");
		final String key = config.getProperty("api_key");

		Map<String,String>  map = new HashMap<>();
		map.put("user",user);
		map.put("key",key);
		return map;
	}

	public static String generateHtmlReport(List<TestResult> results) throws IOException {
		if (results.isEmpty()){
			throw new IllegalArgumentException("No test results available");
		}

		Map<String,String> map =  getApiProperties();
		String user = map.get("user");
		String key = map.get("key");


		StringBuilder output = new StringBuilder("<h1>Sales that break the threshold: </h1>");
		for (TestResult tr : results){
			String url = stripHttps(tr.resources.report_pdf_full);
			output.append(String.format("%n<p>Deal Id: <b>%s</b><p>",tr.message.dealId));
			output.append(String.format("<p>Html download time of %d ms</p>",tr.getHtmlDownloadTime()));
			output.append(String.format("%n<p>Html size: %s bytes</p>",tr.results.html_bytes));
			output.append(String.format("%n<p>Page load time : %d ms</p>",tr.getPageLoadTime()));
			output.append(String.format("%n<p>Product Type : %s </p>",tr.message.productType));
			output.append(String.format("Report Url: %s",tr.results.report_url));
			output.append(String.format("%n<p>Download Full Report here: <a href='%s?username=%s&password=%s'>Sale Report</a> </p><hr>%n",url,user,key));
		}
		return output.toString();
	}

	public static String generateReport(List<TestResult> results) throws IOException {
		if (results.isEmpty()){
			throw new IllegalArgumentException("No test results available");
		}

		Map<String,String> map =  getApiProperties();
		String user = map.get("user") + ":";
		String key = map.get("key") + "@";

		String output = "Sales that break the threshold: \n";
		for (TestResult tr : results){
			String url = stripHttps(tr.resources.report_pdf_full) + "```";
			output += "\n\nDeal Id: " + tr.message.dealId;
			output += "\nHtml download time of " + tr.getHtmlDownloadTime() + " ms";
			output += "\nHtml size: " + tr.results.html_bytes + " bytes";
			output += "\nPage load time : "+ tr.getPageLoadTime() + " ms";
			output += "\nProduct Type : " + tr.message.productType;
			output += "\nReport Url: " + tr.results.report_url;
			output += "\nDownload Full Report here: ```" + user + key + url;
		}
		return output;
	}

	private static String stripHttps(String s){
		return s.replaceFirst("https://","");
	}
}