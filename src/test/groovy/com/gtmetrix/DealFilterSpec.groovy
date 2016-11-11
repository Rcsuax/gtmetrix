package com.gtmetrix

import com.gtmetrix.DealBuilderService.DealFilter
import spock.lang.Specification

import java.text.SimpleDateFormat


class DealFilterSpec extends Specification {

	def mockDealOne

	def mockDealTwo

	def mockDealThree

	void setup() {
		mockDealOne = Mock(Deal)
		mockDealTwo = Mock(Deal)
		mockDealThree = Mock(Deal)
	}

	def "add deal to list if it will start within 24 hours"() {
		given:
			Date now = new Date().parse("yyyy-MM-dd hh:mm",new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()))

			//Pass since Upcoming
			mockDealOne.startDate >> now + 1
			mockDealOne.id >> "One"
			println(mockDealOne.startDate)

			//Fail since startDate is in the past
			mockDealTwo.startDate >> now - 3
			mockDealTwo.id >> "Two"
			println(mockDealTwo.startDate)

			//Fail since startDate is current
			mockDealThree.startDate >> now
			mockDealThree.id >> "Three"
			println(mockDealThree.startDate)

			def dealList= [mockDealOne,mockDealTwo,mockDealThree]
		when:
			def filter = new DealFilter().filterUpcomingDeals(dealList)
		then:
			filter.contains(mockDealOne)
		and:
			!filter.contains(mockDealTwo)
			!filter.contains(mockDealThree)

	}
}
