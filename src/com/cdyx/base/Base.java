package com.cdyx.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.cdyx.druid.DBPoolConnection;

/**
 * 基础类
 * 
 * @author Administrator
 *
 */
public class Base {
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		DBPoolConnection dbp = DBPoolConnection.getInstance();
		DruidPooledConnection conn = null;
		try {
			conn = dbp.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭
	 * 
	 * @param conn
	 * @param rs
	 * @param ptmt
	 */
	public void close(Connection conn, ResultSet rs, PreparedStatement ptmt) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (ptmt != null) {
			try {
				ptmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行查询，返回几个ResultSet集合
	 * 
	 * @param conn
	 * @param CSQL
	 * @param Cvalue
	 * @return
	 * @throws Exception
	 */
	public ResultSet ExecQry(Connection conn, String CSQL, List<?> Cvalue) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(CSQL);
			if (Cvalue != null) {
				for (int i = 1; i <= Cvalue.size(); i++) {
					ps.setObject(i, Cvalue.get(i - 1));
				}
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
		}
		return rs;
	}

	/**
	 * 执行update,insert,delete操作
	 * 
	 * @param conn
	 * @param CSQL
	 * @param Cvalue
	 * @return
	 * @throws Exception
	 */
	public boolean Execute(Connection conn, String CSQL, List<?> Cvalue) throws Exception {
		boolean bool = false;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(CSQL);
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
		}
		return bool;
	}

	public String CreateErrorInfo(String ErrBM, String ErrInfo) {
		Document document = DocumentHelper.createDocument();
		Element MSG = document.addElement("MSG");
		Element RES = MSG.addElement("RES");
		RES.addElement("ActionTime").setText(this.getdate());
		RES.addElement("ResultMsg").setText("0");
		RES.addElement("ErrCode").setText(ErrBM);
		RES.addElement("ERR").setText(ErrInfo);
		return document.asXML();
	}

	public String getdate() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}
}
