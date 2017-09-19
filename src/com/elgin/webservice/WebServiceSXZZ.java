

package com.elgin.webservice;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import SOA.YxProGlob.Unit.TLogError;

public class WebServiceSXZZ  extends SXZZWS{  
     
	/**
	 * 查询病人转诊信息
	 */
	public String GetReferralInfo(String message) {
		//操作日志记录类
		Object RtnObj = null;
		Document inDom = null;
		Element inEle = null;
		try{
			//解析传入的类
			inDom = DocumentHelper.parseText(message);
			if(inDom == null){
	    		throw new Exception("传入了非法字符,数据解析错误，请确认传入的XML格式是否正确！");
			}
			String OrgCode = inDom.getRootElement().element("ASK").element("OrgCode") == null ? "" : inDom.getRootElement().element("ASK").element("OrgCode").getText();
			String OrgName = inDom.getRootElement().element("ASK").element("OrgName") == null ? "" : inDom.getRootElement().element("ASK").element("OrgName").getText();
			if("".equals(OrgCode) || "".equals(OrgName)){
	    		throw new Exception("调用机构编码和调用机构名称不能为空，请按标准传入正确参数！");
			}
			//获取连接
			/* Connection conn = null;
            conn = JdbcUtils_JNDI.getConnection();
			System.out.println(conn);*/
			inEle = inDom.getRootElement();
			inEle.addAttribute("Version","1.2");
	    	//调用总线处理
			RtnObj = GetReferralInfo(inEle);
		}catch (Exception e) { //异常消息
			e.printStackTrace();
		}
		return (String) RtnObj;
	}
    
	
	/**
	 * 双向转诊转出操作
	 */
	public String ReferralOut(String message) {
		//操作日志记录类
	//	System.out.println(message);
		String RtnObj = null;
		Document inDom = null;
		Element inEle = null;
		try{
			//解析传入的类
			inDom = DocumentHelper.parseText(message);
			if(inDom == null){
	    		throw new Exception("传入了非法字符,数据解析错误，请确认传入的XML格式是否正确！");
			}
			String OrgCode = inDom.getRootElement().element("ASK").element("OrgCode") == null ? "" : inDom.getRootElement().element("ASK").element("OrgCode").getText();
			String OrgName = inDom.getRootElement().element("ASK").element("OrgName") == null ? "" : inDom.getRootElement().element("ASK").element("OrgName").getText();
			if("".equals(OrgCode) || "".equals(OrgName)){
	    		throw new Exception("调用机构编码和调用机构名称不能为空，请按标准传入正确参数！");
			}
			inEle = inDom.getRootElement();
				inEle.addAttribute("Version","1.2");
	    	//调用总线处理
			/*RtnObj = TEsbBus.doEsb(inEle , inOpr);*/
			
			RtnObj = (String) ReferralOut(inEle);
		}catch (Exception e) { //异常消息
			e.printStackTrace();
		}
		return RtnObj;
	} 
	/**
	 * 双向转诊回转操作
	 */
	public String ReferralBack(String message) {
	//	System.out.println(message);
		
	//	TLogError.WriteLogInfo(message, "sxzz");
		
		String RtnObj = null;
		Document inDom = null;
		Element inEle = null;
		try{
			//解析传入的类
			inDom = DocumentHelper.parseText(message);
			if(inDom == null){
	    		throw new Exception("传入了非法字符,数据解析错误，请确认传入的XML格式是否正确！");
			}
			String OrgCode = inDom.getRootElement().element("ASK").element("OrgCode") == null ? "" : inDom.getRootElement().element("ASK").element("OrgCode").getText();
			String OrgName = inDom.getRootElement().element("ASK").element("OrgName") == null ? "" : inDom.getRootElement().element("ASK").element("OrgName").getText();
			if("".equals(OrgCode) || "".equals(OrgName)){
	    		throw new Exception("调用机构编码和调用机构名称不能为空，请按标准传入正确参数！");
			}
			
			inEle = inDom.getRootElement();
				inEle.addAttribute("Version","1.2");
			/*Element Msh = inEle.addElement("MSH");
				Msh.addElement("MSH.1").setText("Y310068");
				Msh.addElement("MSH.2").setText("ReferralBack");*/
				RtnObj = (String) ReferralBack(inEle);
		}catch (Exception e) { //异常消息
			e.printStackTrace();
		}
		return RtnObj;
	} 
	/**
	 * 修改处理状态
	 * @param message
	 * @return
	 */
	public String ModifyZT(String message) {
		String RtnObj = null;
		Document inDom = null;
		Element inEle = null;
		try{
			//解析传入的类
			inDom = DocumentHelper.parseText(message);
			if(inDom == null){
	    		throw new Exception("传入了非法字符,数据解析错误，请确认传入的XML格式是否正确！");
			}
			String OrgCode = inDom.getRootElement().element("ASK").element("OrgCode") == null ? "" : inDom.getRootElement().element("ASK").element("OrgCode").getText();
			String OrgName = inDom.getRootElement().element("ASK").element("OrgName") == null ? "" : inDom.getRootElement().element("ASK").element("OrgName").getText();
			if("".equals(OrgCode) || "".equals(OrgName)){
	    		throw new Exception("调用机构编码和调用机构名称不能为空，请按标准传入正确参数！");
			}
			
			inEle = inDom.getRootElement();
				inEle.addAttribute("Version","1.2");
				RtnObj = (String) ModifyZT(inEle);
		}catch (Exception e) { //异常消息
			e.printStackTrace();
		}
		return RtnObj;
	} 
}  