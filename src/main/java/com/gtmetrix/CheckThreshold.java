package com.gtmetrix;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CheckThreshold {

    public List<TestResult> checkTestsForThresholdBreaches(List<TestResult> testResults) {
		List<TestResult> listResult = new ArrayList<>();
    	for (TestResult result : testResults){
			if(filterHtmlDownloadTime(result) && filterPageLoadTime(result)) {
				listResult.add(result);
			}
		}
        return listResult;
    }
    private boolean filterHtmlDownloadTime(TestResult result){
    	return result.getHtmlDownloadTime() > 300;
	}

	private boolean filterPageLoadTime(TestResult result){
		return result.getPageLoadTime() < 9000;
	}
}