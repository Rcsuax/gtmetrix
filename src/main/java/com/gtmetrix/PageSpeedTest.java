package com.gtmetrix;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("WeakerAccess")
public class PageSpeedTest extends HttpUtils implements TestResultDAO {

	@Override
	public HttpPost getHttpPost() {
		return new HttpPost("https://gtmetrix.com/api/0.1/test");
	}

	@Override
	public HttpGet getHttpGet(String testId) {
		return new HttpGet("https://gtmetrix.com/api/0.1/test/" + testId);
	}

	@Override
	public void saveAll(List<TestResult> testResultList) {
		for (TestResult tr : testResultList){
			save(tr);
		}
	}

	public List<TestResult> testAllDeals(List<Message> queue) {
		List<TestResult> testResults = new ArrayList<>();
		for (Message message : queue) {
			TestResult tr = testOneDeal(message);
			if (tr != null) testResults.add(tr);
		}
		return testResults;
	}

	public TestResult testOneDeal(Message message){
		Gson gson = new Gson();
		CloseableHttpResponse response = sendHttpRequest(getHttpPost(), message.url);
		String postJsonData = readHttpResponse(response);

		PostResponse postResponse = gson.fromJson(postJsonData, PostResponse.class);

		try {
			TestResult result = getTestResult(postResponse);
			result.message = message;

			Query query = new Query();
			query.markDealTested();
			return result;

		} catch (NullPointerException | InvalidTestStateException e) {
			System.out.println(e.getMessage());
			System.out.println("Unable to get test result for " + message.url);
			return null;
		}
	}

	private TestResult getTestResult(PostResponse postResponse) throws NullPointerException,InvalidTestStateException {
		Gson gson = new Gson();
		if (postResponse.test_id != null) {
			HttpGet httpget = getHttpGet(postResponse.test_id);
			CloseableHttpResponse response = sendHttpRequest(httpget);
			String getJsonData = readHttpResponse(response);

			TestResult testResult = gson.fromJson(getJsonData, TestResult.class);
			if(!testResult.error.isEmpty()){
				throw new InvalidTestStateException(testResult.error);
			}

			if (!Objects.equals(testResult.state, "completed")) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException intex) {
					System.out.println(intex.getMessage());
				}
				testResult = getTestResult(postResponse);
			}
			testResult.setTestId(postResponse.test_id);
			return testResult;
		}
		throw new NullPointerException();
	}
}
