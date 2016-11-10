package com.gtmetrix;

import com.gtmetrix.DealBuilderService.DealFilter;
import com.gtmetrix.DealBuilderService.XmlDealBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
	public static void main(String[] args) {

		List<Deal> dealList = XmlDealBuilder.getListOfDeals();

		DealFilter dealFilter = new DealFilter();
		List<Deal> persistDeals = dealFilter.filterUpcomingDeals(dealList);
		dealFilter.saveAll(persistDeals);


		Query query = new Query();
		List<Message> testQueue = query.getAllDeals();

		PageSpeedTest pageSpeed = new PageSpeedTest();
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