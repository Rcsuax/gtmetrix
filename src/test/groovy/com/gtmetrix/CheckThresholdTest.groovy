package com.gtmetrix

import spock.lang.Specification

class CheckThresholdTest extends Specification {

	def checkThreshold
	def mockTestResultOne
	def mockTestResultTwo
	def mockTestResultThree

	def setup() {
		checkThreshold = new CheckThreshold()

		mockTestResultOne = Mock(TestResult)
		mockTestResultTwo = Mock(TestResult)
		mockTestResultThree = Mock(TestResult)
	}

	def "Takes a list of test results and returns a list of test results"() {
		given:
			List<TestResult> testResults = [mockTestResultOne, mockTestResultTwo, mockTestResultThree]

		when:
			def checkedResults = checkThreshold.checkTestsForThresholdBreaches(testResults)
		then:
			checkedResults instanceof List<TestResult>
	}

	def "Takes a list of deals and returns the deals which do not meet the criteria"() {
		given:
			//Pass and should not be returned
			mockTestResultOne.getHtmlDownloadTime() >> 200
			mockTestResultOne.getPageLoadTime() >> 6000

			//Fail and be returned within a list
			mockTestResultTwo.getHtmlDownloadTime() >> 500
			mockTestResultTwo.getPageLoadTime() >> 12000

			//Fail and be returned within a list
			mockTestResultThree.getHtmlDownloadTime() >> 500
			mockTestResultThree.getPageLoadTime() >> 6000

			def testResults = [mockTestResultOne, mockTestResultTwo, mockTestResultThree]

		when:
			def checkedResults = checkThreshold.checkTestsForThresholdBreaches(testResults)
		then:
			!checkedResults.contains(mockTestResultOne)
		and:
			checkedResults.contains(mockTestResultTwo)
			checkedResults.contains(mockTestResultThree)
	}
}