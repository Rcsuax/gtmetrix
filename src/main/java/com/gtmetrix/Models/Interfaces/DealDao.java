package com.gtmetrix.Models.Interfaces;

import com.gtmetrix.Models.Deal;
import com.gtmetrix.Models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public interface DealDao extends Database {

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

	default List<Message> getAllDeals(){
		String query ="SELECT dealId,dealUrl,productType FROM Deals WHERE testCompletion IS FALSE";
		List<Message> queue = new ArrayList<>();
		try {
			ResultSet queryResult = executeQuery(query);
			while (queryResult.next()){
				String dealId = queryResult.getString(1);
				String url = queryResult.getString(2) + "?&code=1f2ef7ea039010cc&email=pingdomcheckse123@mailinator.com";
				String product = queryResult.getString(3);
				queue.add(Message.getInstance(dealId,url,product));
			}
			System.out.printf("There are: %d %n",queue.size());
			System.out.println("Query Completed, retrieved all deals");
		}
		catch (SQLException sql){
			System.out.println("SQLException: " + sql.getMessage());
			System.out.println("SQLState: " + sql.getSQLState());
			System.out.println("VendorError: " + sql.getErrorCode());
		}
		return queue;
	}

	default void markDealTested() {
		String query ="Update Deals SET testCompletion = TRUE WHERE testCompletion = 0 LIMIT 1";
		try {
			Statement statement = getConnection().createStatement();
			statement.executeUpdate(query);
			System.out.println("Deal marked as Completed");

		}
		catch (SQLException sql){
			System.out.println("SQLException: " + sql.getMessage());
			System.out.println("SQLState: " + sql.getSQLState());
			System.out.println("VendorError: " + sql.getErrorCode());
		}
	}

	default ResultSet executeQuery(String Query) throws SQLException {
		Statement statement = getConnection().createStatement();
		return statement.executeQuery(Query);
	}

	void saveAll(List<Deal> deals);
}
