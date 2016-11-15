package com.gtmetrix;

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

		try {
			String report = ReportBuilder.generateReport(failedThresholdCheck);
			EmailSender sender = new EmailSender();
			sender.send(report);
		}
		catch (IllegalArgumentException | IOException e){
			System.out.println(e.getMessage());
		}
	}
}