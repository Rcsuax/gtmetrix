package com.gtmetrix.Models.Interfaces;

import com.gtmetrix.Models.TestResult;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface TestResultDao extends Database {

	default void save(TestResult result){
		System.out.println("updating TestResults");
		try {
			PreparedStatement stmt = getConnection().prepareStatement(
					"INSERT INTO TestResults(test_id,deal_id,page_load_time,html_bytes,page_elements,report_url,html_load_time,page_bytes,pagespeed_score,yslow_score,product_type,timestamp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)"
			);

			stmt.setString(1, result.getTestId());
			stmt.setString(2, result.message.dealId);
			stmt.setInt(3,Integer.parseInt(result.results.page_load_time));
			stmt.setInt(4,Integer.parseInt(result.results.html_bytes));
			stmt.setInt(5,Integer.parseInt(result.results.page_elements));
			stmt.setString(6,result.results.report_url);
			stmt.setInt(7,Integer.parseInt(result.results.html_load_time));
			stmt.setInt(8,Integer.parseInt(result.results.page_bytes));
			stmt.setInt(9,Integer.parseInt(result.results.pagespeed_score));
			stmt.setInt(10,Integer.parseInt(result.results.yslow_score));
			stmt.setString(11,result.message.productType);
			stmt.setDate(12,toSqlDate(new java.util.Date()));

			stmt.executeUpdate();
		}
		catch (SQLException sq){
			System.out.println("Error with :" + result.message.dealId);

			System.out.println("SQLException: " + sq.getMessage());
			System.out.println("SQLState: " + sq.getSQLState());
			System.out.println("VendorError: " + sq.getErrorCode());
		}
	}

	void saveAll(List<TestResult> testResults);
}
