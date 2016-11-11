package com.gtmetrix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public interface DealDAO extends Database {

	default void save(Deal deal) {
		System.out.println("Updating Deals");
		try (Connection connection = getConnection()){
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO Deals(dealId,startDate,endDate,dealUrl,productType) VALUES(?,?,?,?,?)");

			stmt.setString(1, deal.getId());
			stmt.setDate(2, toSqlDate(deal.getStartDate()));
			stmt.setDate(3, toSqlDate(deal.getEndDate()));
			stmt.setString(4, deal.getUrl());
			stmt.setString(5, deal.getProductType());

			stmt.executeUpdate();
			connection.close();
		}
		catch (SQLException sq){
			System.out.println("Error with :" + deal.getId());

			System.out.println("SQLException: " + sq.getMessage());
			System.out.println("SQLState: " + sq.getSQLState());
			System.out.println("VendorError: " + sq.getErrorCode());
		}
	}

	void saveAll(List<Deal> deals);
}
