package com.gtmetrix;

import java.io.*;
import java.sql.*;

import java.sql.Date;
import java.util.*;

public interface Database {

    default Connection getConnection() throws SQLException {
			Properties config = new Properties();

		try {
			InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
			config.load(in);
			String db_user = config.getProperty("db_user");
			String db_pass = config.getProperty("db_password");
			return DriverManager.getConnection("jdbc:mysql://localhost/gtmetrix?user=" + db_user + "&password=" + db_pass);
		}
		catch (IOException e){
			System.out.println(e.getMessage());
			throw new NullPointerException();
		}
    }

    default void updateDatabase(TestResult data){
		System.out.println("updating TestResults");
		try {
			PreparedStatement stmt = getConnection().prepareStatement(
					"INSERT INTO TestResults(test_id,deal_id,page_load_time,html_bytes,page_elements,report_url,html_load_time,page_bytes,pagespeed_score,yslow_score,product_type,timestamp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
			);

			stmt.setString(1, data.getTestId());
			stmt.setString(2, data.message.dealId);
			stmt.setInt(3,Integer.parseInt(data.results.page_load_time));
			stmt.setInt(4,Integer.parseInt(data.results.html_bytes));
			stmt.setInt(5,Integer.parseInt(data.results.page_elements));
			stmt.setString(6,data.results.report_url);
			stmt.setInt(7,Integer.parseInt(data.results.html_load_time));
			stmt.setInt(8,Integer.parseInt(data.results.page_bytes));
			stmt.setInt(9,Integer.parseInt(data.results.pagespeed_score));
			stmt.setInt(10,Integer.parseInt(data.results.yslow_score));
			stmt.setString(11,data.message.productType);
			stmt.setDate(12,toSqlDate(new java.util.Date()));

			stmt.executeUpdate();
		}
		catch (SQLException sq){
			System.out.println("Error with :" + data.message.dealId);

			System.out.println("SQLException: " + sq.getMessage());
			System.out.println("SQLState: " + sq.getSQLState());
			System.out.println("VendorError: " + sq.getErrorCode());
		}
    }

    default void updateDatabase(Deal deal) {
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

    static Date toSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}