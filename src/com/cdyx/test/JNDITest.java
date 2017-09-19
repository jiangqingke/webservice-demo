package com.cdyx.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cdyx.util.JdbcUtils_JNDI;

public class JNDITest extends HttpServlet {

    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            //获取连接
            conn = JdbcUtils_JNDI.getConnection();
            String sql = "insert into account(name,money) values(?,?)";
            st = conn.prepareStatement(sql);
            st.setString(1, "F");
            st.setFloat(2, 2000);
            st.executeUpdate();
            response.getWriter().print("插入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JdbcUtils_JNDI.release(conn, st, rs);
        }
    
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}