package com.gtmetrix;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ReportBuilder {

	public static String generateReport(List<TestResult> results) throws IOException {
		if (results.isEmpty()){
			throw new IllegalArgumentException("No test results available");
		}

		Properties config = new Properties();
		InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
		config.load(in);
		final String user = config.getProperty("api_user");
		final String key = config.getProperty("api_key");


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

	public static String stripHttps(String s){
		return s.replaceFirst("https://","");
	}
}