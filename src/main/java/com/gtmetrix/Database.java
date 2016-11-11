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

    default Date toSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}