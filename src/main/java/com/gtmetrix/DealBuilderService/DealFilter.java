package com.gtmetrix.DealBuilderService;

import com.gtmetrix.Database;
import com.gtmetrix.Deal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DealFilter implements Database {

    public List<Deal> filterUpcomingDeals(List<Deal> dealList){
		List<Deal> filteredList = new ArrayList<>();
    	for (Deal deal: dealList){
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				Date now = dateFormat.parse(dateFormat.format(new Date()));
				Date before = new Date(now.getTime() + TimeUnit.HOURS.toMillis(25));

				if(deal.getStartDate().after(now) && deal.getStartDate().before(before)){
					filteredList.add(deal);
				}
				else System.out.println("Deal " + deal.getId() + " does not pass rules " + deal.getStartDate());
			}
			catch (ParseException e){
				System.out.println(e.getMessage());
			}
		}
		return filteredList;
	}

    public void saveAll(List<Deal> deals) {
        for (Deal deal : deals){
			System.out.println("saving deal " + deal.getId());
			save(deal);
		}
    }
}
