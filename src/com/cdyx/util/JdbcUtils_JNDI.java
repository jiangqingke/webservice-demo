package com.cdyx.util;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcUtils_JNDI {
    private static DataSource ds = null;
    //在静态代码块中创建数据库连接池
    static{
        try {
            //初始化JNDI
            Context initCtx = new InitialContext();
            //得到JNDI容器
             Context envCtx = (Context) initCtx.lookup("java:comp/env");
            //从JNDI容器中检索name为jdbc/datasource的数据源
             ds = (DataSource)envCtx.lookup("jdbc/datasource");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /*
     * 从数据源中获取连接
     */
    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }
    
    /*
     * 释放资源
     */
    public static void release(Connection conn,Statement st,ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(st!=null){
                try {
                    st.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(conn!=null){
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
