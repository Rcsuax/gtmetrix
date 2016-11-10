package com.gtmetrix;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Query implements Database {

	public List<Message> getAllDeals(){
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

	public void markDealTested() {
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

	public ResultSet executeQuery(String Query) throws SQLException {
		Statement statement = getConnection().createStatement();
		return statement.executeQuery(Query);
	}
}
