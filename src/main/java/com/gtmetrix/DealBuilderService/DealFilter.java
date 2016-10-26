package com.gtmetrix.DealBuilderService;

import com.gtmetrix.Database;
import com.gtmetrix.Deal;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

public class DealFilter extends Database {

    public void filterUpcomingDeals(List<Deal> dealList){
		for (Deal deal: dealList){
			DateTime st = new DateTime(deal.getStartDate().getTime()),
					day = new DateTime(DateTimeZone.UTC);

			st = st.withTimeAtStartOfDay();
			day = day.plusHours(24).withTimeAtStartOfDay();
			//Run test if sale starts within 24? hours

			if(st.isAfterNow() && st.isBefore(day)) {
				System.out.println("saving deal " + deal.getId());
				save(deal);
			}

			else System.out.println("Deal " + deal.getId() + " does not pass rules");
		}
	}

    private void save(Deal deal){
        updateDatabase(deal);
    }
}
