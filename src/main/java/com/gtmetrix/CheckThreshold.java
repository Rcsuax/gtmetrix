package com.gtmetrix;

import com.gtmetrix.Models.TestResult;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CheckThreshold {

    public List<TestResult> checkTestsForThresholdBreaches(List<TestResult> testResults) {
		List<TestResult> listResult = new ArrayList<>();
    	for (TestResult result : testResults){
			if(filterHtmlDownloadTime(result) || filterPageLoadTime(result)) {
				listResult.add(result);
			}
		}
        return listResult;
    }
    private boolean filterHtmlDownloadTime(TestResult result){
    	return result.getHtmlDownloadTime() > 250;
	}

	private boolean filterPageLoadTime(TestResult result){
		return result.getPageLoadTime() > 8000;
	}
}
