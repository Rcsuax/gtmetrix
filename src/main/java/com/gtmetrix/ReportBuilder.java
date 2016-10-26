package com.gtmetrix;

import java.util.List;

public class ReportBuilder {

	public static String generateReport(List<TestResult> results) {
		if (results.isEmpty()){
			throw new IllegalArgumentException("No test results available");
		}
		StringBuilder output = new StringBuilder("<h1>Sales that break the threshold: </h1>");
		for (TestResult tr : results){
			output.append(String.format("%n<p>Deal Id: <b>%s</b> with a Html download time of %d ms</p>",tr.getDealId(),tr.getHtmlDownloadTime()));
			output.append(String.format("%n<p>Html size: %s bytes</p>",tr.results.html_bytes));
			output.append(String.format("%n<p>Page load time : %d ms</p>",tr.getPageLoadTime()));
			output.append(String.format("%n<p>Product Type : %s </p>",tr.getProductType()));
			output.append(String.format("%n<p>Full Report view here: <a href='tech@secretescapes.com:dad9fac6b581bfe9af30452f3b7e0487@%s'>Sale Report</a> </p><hr>%n",tr.resources.report_pdf_full));
		}
		return output.toString();
	}
}