package com.cdyx.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TDBConnPool {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test();

	}

	public static void test() {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=AHISCENTER";
		String userName = "sa"; //
		String userPwd = "123456"; //
		Connection dbConn;

		try {
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			System.out.println("Connection Successful! ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Connection getConn() {
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://localhost:1433; DatabaseName=AHISCENTER";
		String username = "sa";
		String password = "123456";
		Connection conn = null;
		try {
			Class.forName(driver); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	@SuppressWarnings("unused")
	static Connection InitCommit() throws Exception {
		Connection conn = null;
		try {
			conn = TDBConnPool.getConn();
			conn.setAutoCommit(false);
		} catch (Exception e) {
			throw new Exception("建立链接失败");
		}
		return conn;
	}

	public static ResultSet ExecQry(Connection conn, String CSQL, List<?> Cvalue) throws Exception {
		ResultSet rs = null;
		try {
			PreparedStatement ps = conn.prepareStatement(CSQL);
			if (Cvalue != null) {
				for (int i = 1; i <= Cvalue.size(); i++) {
					ps.setObject(i, Cvalue.get(i - 1));
				}
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return rs;
	}

	public static boolean Execute(Connection conn, String CSQL, List<?> Cvalue) throws Exception {
		boolean bool = false;
		try {
			PreparedStatement ps = conn.prepareStatement(CSQL);
			if (Cvalue != null) {
				for (int i = 1; i <= Cvalue.size(); i++) {
					ps.setObject(i, Cvalue.get(i - 1));
				}
			}
			ps.executeUpdate();
			bool = true;
		} catch (Exception e) {
			bool = false;
			throw new Exception(e.getMessage());
		} finally {
			// conn.close();
		}
		return bool;
	}

}
