package com.cdyx.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import com.cdyx.test.TDBConnPool;

public class GetXTCS 
{
  protected static HashMap<String, String> PMap = new HashMap<String, String>();
  public static boolean InitXS = false;

  public static synchronized String GetSysParameter(String CBH, int Step) throws Exception
  {
    String param = null;
    int ILength = 0;
    String CSQL = "declare @DIFF INT ";
    CSQL = CSQL + " declare @PARAM VARCHAR(20)";
    CSQL = CSQL + " set @DIFF=" + Step;
    CSQL = CSQL + " set @PARAM='" + CBH + "'";
    CSQL = CSQL + " exec AHISTER..GETSYSNUMBER @DIFF,@PARAM OUT";
    try { Connection conn = JdbcUtils_JNDI.getConnection();
      try { if (!TDBConnPool.Execute(conn, CSQL, null)) {
          throw new Exception("存储过程执行错误！");
        }
	        ResultSet rs = TDBConnPool.ExecQry(conn, "SELECT CVALUE PARAM,ILength FROM AHISTER..TBSYSPARAM WHERE CBH='" + CBH + "'", null);
	        if (rs.next()) {
	          ILength = rs.getInt("ILength");
	          param = rs.getString("PARAM");
	        }
	        if (rs != null)
	          rs.close();
	        conn.commit();
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
        conn.close();
      }
    } catch (Exception e) {
    	throw new Exception(e.getMessage());
    }
    return getParamFill(param, ILength);
  }

  public static synchronized String getParamFill(String paramValue, int fillCount) throws Exception
  {
    int count = fillCount - paramValue.length();
    String resultStr = "";
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        resultStr = resultStr + "0";
      }
      return resultStr += paramValue;
    }
    return paramValue;
  }
  public boolean InitLoad() throws Exception {
    boolean bol = false;
	 Connection conn =JdbcUtils_JNDI.getConnection();
	 /*Connection conn = TDBConnPool.InitCommit();*/
    try {
      ResultSet rs = TDBConnPool.ExecQry(conn,"SELECT CCSMC,CVALUE FROM AHISTER..TBYXXTCSI WITH(NOLOCK)", null);
      while (rs.next()) {
        PMap.put(rs.getString("CCSMC"), rs.getString("CVALUE"));
      }
      if (rs != null)
        rs.close();
      bol = true;
    } catch (Exception e) {
    	throw new Exception(e.getMessage());
    } finally {
      conn.close();
    }
    return bol;
  }

  public static synchronized String GetDefParamter(String CNBMC, String MRVALUE) throws Exception
  {
    if ((!InitXS) && 
      (new GetXTCS().InitLoad())) {
      InitXS = true;
    }

    String Value = (String)PMap.get(CNBMC);
    String RTNVALUE = Value == null ? MRVALUE : Value;
    Value = null;
    return RTNVALUE;
  }

  public static synchronized boolean InitDefParam(String CNBMC) throws Exception
  {
    boolean bol = false;
	 /*Connection conn = TDBConnPool.InitCommit();*/
    Connection conn =JdbcUtils_JNDI.getConnection();
    try {
      ResultSet rs = TDBConnPool.ExecQry(conn,"SELECT CCSMC,CVALUE FROM AHISTER..TBYXXTCSI WITH(NOLOCK) WHERE CCSMC='" + CNBMC + "'", null);
      if (rs.next()) {
        PMap.put(CNBMC, rs.getString("CVALUE"));
      }
      if (rs != null)
        rs.close();
      bol = true;
    } catch (Exception e) {
    	throw new Exception(e.getMessage());
    } finally {
      conn.close();
    }
    return bol;
  }
}