package com.gtmetrix;

import com.gtmetrix.Models.Deal;
import com.gtmetrix.Models.Message;
import com.gtmetrix.Models.TestResult;
import com.gtmetrix.Services.DealService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
	public static void main(String[] args) {

		DealService service = new DealService();
		InputStream inputStream = service.getValidInputStream();
		List<Deal> dealList = service.getListOfDealsFromXML(inputStream);
		List<Deal> dealsToTest = service.filterUpcomingDeals(dealList);
		service.saveAll(dealsToTest);


		PageSpeedTest pageSpeed = new PageSpeedTest();
		List<Message> testQueue = service.getAllDeals();
		List<TestResult> testResults = pageSpeed.testAllDeals(testQueue);
		pageSpeed.saveAll(testResults);

		CheckThreshold check = new CheckThreshold();
		List<TestResult> failedThresholdCheck = check.checkTestsForThresholdBreaches(testResults);

		SlackService ss = new SlackService();

		try {
			String report = ReportBuilder.generateReport(failedThresholdCheck);
			EmailSender sender = new EmailSender();
			sender.send(report);
			ss.sendSlackMessage(report);
		}
		catch (IllegalArgumentException | IOException e){
			System.out.println(e.getMessage());
		}
	}
}