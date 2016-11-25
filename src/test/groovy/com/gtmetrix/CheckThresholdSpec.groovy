package com.gtmetrix

import com.gtmetrix.Models.TestResult
import spock.lang.Shared
import spock.lang.Specification

class CheckThresholdSpec extends Specification {

	@Shared
	def checkThreshold

	@Shared
	TestResult mockTestResultOne

	@Shared
	TestResult mockTestResultTwo

	@Shared
	TestResult mockTestResultThree

	def setupSpec() {
		checkThreshold = new CheckThreshold()

		mockTestResultOne = Stub {
			getHtmlDownloadTime() >> 200
			getPageLoadTime() >> 6000
		}

		mockTestResultTwo = Stub {
			getHtmlDownloadTime() >> 500
			getPageLoadTime() >> 12000
		}
		mockTestResultThree = Stub {
			getHtmlDownloadTime() >> 500
			getPageLoadTime() >> 6000
		}
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