package com.gtmetrix;

import com.gtmetrix.DealBuilderService.DealFilter;
import com.gtmetrix.DealBuilderService.XmlDealBuilder;

import java.util.List;

public class Main {
	public static void main(String[] args) {

		List<Deal> dealList = XmlDealBuilder.getListOfDeals();

		DealFilter dealFilter = new DealFilter();
		List<Deal> persistDeals = dealFilter.filterUpcomingDeals(dealList);
		dealFilter.save(persistDeals);


		Query query = new Query();
		List<Message> testQueue = query.getAllDeals();

		PageSpeedTest pageSpeed = new PageSpeedTest();
		List<TestResult> testResults = pageSpeed.testAllDeals(testQueue);

		CheckThreshold check = new CheckThreshold();
		List<TestResult> failedThresholdCheck = check.checkTestsForThresholdBreaches(testResults);

		try {
			String report = ReportBuilder.generateReport(failedThresholdCheck);
			EmailSender sender = new EmailSender();
			sender.send(report);
		}
		catch (IllegalArgumentException e){
			System.out.println(e.getMessage());
		}
	}
}