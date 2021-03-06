package com.javafever.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConector {

	private static java.sql.Connection conn;
	private static final String URL = "jdbc:mysql://localhost:8889/movietheatre?serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PWD = "Jcrf151681";

	private DbConector() {
	}

	public static Connection getConnection() throws SQLException {

		if (conn == null || conn.isClosed()) {
			try {

				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(URL, USER, PWD);
				if (conn != null) {

					// System.out.println("Database connected succesfully.");

				}

			} catch (Exception e) {
				System.out.println("Database connection failed : " + e.getMessage());
			}
			return conn;
		} else {// If the connection exist and is valid return same connection
			return conn;
		}
	}

}
