package com.cdyx.test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cdyx.druid.DBPoolConnection;

public class DruidTest {
	public static void main(String[] args) {
		DBPoolConnection dbp = DBPoolConnection.getInstance();
		DruidPooledConnection conn = null;
		PreparedStatement ptmt= null;
		try {
			//System.out.println(dbp);
			conn = dbp.getConnection();
			System.out.println(conn);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(ptmt != null) {
				try {
					ptmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
