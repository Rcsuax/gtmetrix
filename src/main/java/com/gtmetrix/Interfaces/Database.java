package com.gtmetrix.Interfaces;

import java.io.*;
import java.sql.*;

import java.sql.Date;
import java.util.*;

public interface Database {

    default Connection getConnection() {
		Connection connection = null;
		Properties config = new Properties();
		InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
		try {
			config.load(in);
			String db_user = config.getProperty("db_user");
			String db_pass = config.getProperty("db_password");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/gtmetrix?user=" + db_user + "&password=" + db_pass);
		}
		catch (IOException | SQLException e){
			System.out.println(e.getMessage());
		}
		return connection;
    }

    default Date toSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}