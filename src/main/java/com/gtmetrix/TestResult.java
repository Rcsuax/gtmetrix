package com.gtmetrix;

@SuppressWarnings("WeakerAccess")
public class TestResult {

	public String error;

	public String state;

	public Resource resources;

	public Result results;

	private String dealId;

	private String testId;

	private String productType;

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductType() {
		return productType;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestId() {
		return testId;
	}

	public int getHtmlDownloadTime() {
		return new Integer(results.html_load_time);
	}

	public int getPageLoadTime(){
		return new Integer(results.page_load_time);
	}
}