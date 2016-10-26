package com.gtmetrix;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("WeakerAccess")
public class PageSpeedTest extends HttpUtils {

	public List<TestResult> testAllDeals(List<Message> queue) {
		List<TestResult> testResults = new ArrayList<>();
		Gson gson = new Gson();
		Database database = new Database();
		for (Message message : queue) {

			CloseableHttpResponse response = sendHttpRequest(getHttpPost(), message.url);
			String postJsonData = readHttpResponse(response);

			PostData postData = gson.fromJson(postJsonData, PostData.class);

			try {
				TestResult addResult = getTestResult(postData);
				addResult.setDealId(message.dealId);
				addResult.setProductType(message.productType);

				database.updateDatabase(addResult, message);
				testResults.add(addResult);

				Query query = new Query();
				query.markDealTested();

			} catch (NullPointerException e) {
				System.out.println("Unable to get test result for " + message.url);
			}
		}
		return testResults;
	}

	private void pause(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException intex) {
			System.out.println(intex.getMessage());
		}
	}

	private TestResult getTestResult(PostData postData) throws NullPointerException {
		Gson gson = new Gson();
		if (postData.test_id != null) {
			HttpGet httpget = httpGetBuilder(postData.test_id);
			CloseableHttpResponse response = sendHttpRequest(httpget);
			String getJsonData = readHttpResponse(response);

			TestResult testResult = gson.fromJson(getJsonData, TestResult.class);
			if (!Objects.equals(testResult.state, "completed") && !Objects.equals(testResult.error, "Test not found")) {
				pause(1);
				testResult = getTestResult(postData);
			}
			testResult.setTestId(postData.test_id);
			return testResult;
		}
		throw new NullPointerException();
	}
}
