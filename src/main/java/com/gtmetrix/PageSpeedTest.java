package com.gtmetrix;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("WeakerAccess")
public class PageSpeedTest extends HttpUtils implements Database {

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

		PostData postData = gson.fromJson(postJsonData, PostData.class);

		try {
			TestResult result = getTestResult(postData);
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

	public void saveAll(List<TestResult> testResultList) {
		for (TestResult tr : testResultList){
			save(tr);
		}
	}

	private void pause(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException intex) {
			System.out.println(intex.getMessage());
		}
	}

	private TestResult getTestResult(PostData postData) throws NullPointerException,InvalidTestStateException {
		Gson gson = new Gson();
		if (postData.test_id != null) {
			HttpGet httpget = httpGetBuilder(postData.test_id);
			CloseableHttpResponse response = sendHttpRequest(httpget);
			String getJsonData = readHttpResponse(response);

			TestResult testResult = gson.fromJson(getJsonData, TestResult.class);
			if(!testResult.error.isEmpty()){
				throw new InvalidTestStateException(testResult.error);
			}

			if (!Objects.equals(testResult.state, "completed")) {
				pause(1);
				testResult = getTestResult(postData);
			}
			testResult.setTestId(postData.test_id);
			return testResult;
		}
		throw new NullPointerException();
	}
}
