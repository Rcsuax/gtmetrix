package com.gtmetrix;

import com.gtmetrix.Models.Deal;
import com.gtmetrix.Models.Message;
import com.gtmetrix.Models.TestResult;
import com.gtmetrix.Services.*;

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


		PageSpeedService pageSpeed = new PageSpeedService();
		List<Message> testQueue = service.getAllDeals();
		List<TestResult> testResults = pageSpeed.testAllDeals(testQueue);
		pageSpeed.saveAll(testResults);

		TestResultService check = new TestResultService();
		List<TestResult> failedThresholdCheck = check.checkTestsForThresholdBreaches(testResults);

		SlackService slack = new SlackService();

		try {
			EmailService emailService = new EmailService();
			String htmlReport= ReportService.generateHtmlReport(failedThresholdCheck);
			emailService.send(htmlReport);

			String report = ReportService.generateReport(failedThresholdCheck);
			slack.sendSlackMessage(report);
		}
		catch (IllegalArgumentException | IOException e){
			System.out.println(e.getMessage());
		}
	}
}