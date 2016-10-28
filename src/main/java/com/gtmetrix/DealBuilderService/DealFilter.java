package com.gtmetrix.DealBuilderService;

import com.gtmetrix.Database;
import com.gtmetrix.Deal;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

public class DealFilter extends Database {

    public List<Deal> filterUpcomingDeals(List<Deal> dealList){
		List<Deal> filteredList = new ArrayList<>();
    	for (Deal deal: dealList){

			DateTime st = new DateTime(deal.getStartDate().getTime()).withTimeAtStartOfDay();
			DateTime day = new DateTime(DateTimeZone.UTC).plusHours(24).withTimeAtStartOfDay();

			if(st.isAfterNow() && st.isBefore(day)) {
				filteredList.add(deal);
			}

			else System.out.println("Deal " + deal.getId() + " does not pass rules");
		}
		return filteredList;
	}

    public void save(List<Deal> deals) {
        for (Deal deal : deals){
			System.out.println("saving deal " + deal.getId());
			updateDatabase(deal);
		}
    }
}
