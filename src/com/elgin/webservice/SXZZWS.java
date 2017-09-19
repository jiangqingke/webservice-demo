package com.elgin.webservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cdyx.test.TDBConnPool;
import com.cdyx.util.GetXTCS;
import com.cdyx.util.JdbcUtils_JNDI;

import SOA.YxProGlob.Unit.TLogError;

public class SXZZWS  {

	
	/**
	 * 获取转诊信息   查询操作  GetReferralInfo
	 * @param ele
	 * @param inOpr
	 * @return
	 * @throws Exception
	 */
	
	public Object GetReferralInfo( Element ele) throws Exception { 
		List<String> list = new ArrayList<String>();
		String ResXML = "";
		ele = ele.element("ASK");
		//System.out.println(ele.asXML());
		if(ele == null){
			return this.CreateErrorInfo("J00001","请按照标准传入正确参数格式");
		}
		String YLJGID = ele.element("YLJGID")==null?"":ele.element("YLJGID").getText();
		if("".equals(YLJGID)){
			return this.CreateErrorInfo("J00001","医疗机构不能为空，请重新传入");
		}
		/*String TBSXZZZCJLB = TTbDefList.getHMap().get("77363").GetTbName("", "");
		String TBSXZZHZJLB = TTbDefList.getHMap().get("77364").GetTbName("", "");*/
		String TBSXZZZCJLB = "ahisdb..TBSXZZZCJLB";
		String TBSXZZHZJLB = "ahisdb..TBSXZZHZJLB";
		String CSQL="";
		ResultSet rs = null;
		Document document = DocumentHelper.createDocument();
		Element MSG = document.addElement("MSG");
		Element RES = MSG.addElement("RES");
		RES.addElement("ActionTime").setText(getdate());
		RES.addElement("ResultMsg").setText("1");
		Element DAT = RES.addElement("DAT");
		CSQL = " SELECT CBRID,CBH,CHZXM,CHZXB,DZCSJ,CZCYLJGID,CZCYLJGMC,CZRYLJGID,CZRYLJGMC,'2' IZT,'' CZDJG,'' CZYBAH,'' CZYJCJG,'' CHZZLJG,'' CHZYSXM,'' CHZFLXDH,'' DHZSJ,"
				+ "CHZNL,CCBYX,CXBS,CJWS,CZLJG,CLXDH  FROM "+TBSXZZZCJLB+" WITH(NOLOCK) where  isnull(ICLBZ,'1')<>'3' and (CZCYLJGID=? or CZRYLJGID=?) " ;
		
		CSQL = CSQL +" UNION ALL  SELECT CBRID,CBH,CHZXM,CHZXB,'' DZCSJ,CZCYLJGID,CZCYLJGMC,CZRYLJGID,CZRYLJGMC,'4' IZT,CZDJG,'' CZYBAH,CZYJCJG,CHZZLJG,CHZYSXM,CHZFLXDH,DHZSJ,"
				+ "'' CHZNL,'' CCBYX,'' CXBS,'' CJWS,'' CZLJG,'' CLXDH  FROM "+TBSXZZHZJLB+" WITH(NOLOCK) where isnull(BCL,'0')<>'1' and (CZCYLJGID=? or CZRYLJGID=? )";
		list.add(YLJGID);
		list.add(YLJGID);
		list.add(YLJGID);
		list.add(YLJGID);
		Connection conn = JdbcUtils_JNDI.getConnection();
		try {
			rs = TDBConnPool.ExecQry(conn,CSQL, list);
			while(rs.next()){  //CBRID,CBRXM,CBRXB,CONVERT(varchar(16),DCSNY,121) DCSNY,CJTZZ,CLXDH 
				Element ReferralInfo = DAT.addElement("ROW");
				ReferralInfo.addElement("ID").setText(rs.getString("CBRID")==null?"":rs.getString("CBRID"));
				ReferralInfo.addElement("Key").setText(rs.getString("CBH")==null?"":rs.getString("CBH"));
				ReferralInfo.addElement("Name").setText(rs.getString("CHZXM")==null?"":rs.getString("CHZXM"));
				ReferralInfo.addElement("Sex").setText(rs.getString("CHZXB")==null?"":rs.getString("CHZXB"));
				ReferralInfo.addElement("OutTime").setText(rs.getString("DZCSJ")==null?"":rs.getString("DZCSJ"));
				ReferralInfo.addElement("OutMedicalID").setText(rs.getString("CZCYLJGID")==null?"":rs.getString("CZCYLJGID"));
				ReferralInfo.addElement("OutMedicalName").setText(rs.getString("CZCYLJGMC")==null?"":rs.getString("CZCYLJGMC"));
				ReferralInfo.addElement("InMedicalID").setText(rs.getString("CZRYLJGID")==null?"":rs.getString("CZRYLJGID"));
				ReferralInfo.addElement("InMedicalName").setText(rs.getString("CZRYLJGMC")==null?"":rs.getString("CZRYLJGMC"));
				if("4".equals(rs.getString("IZT"))){//回转
					  ReferralInfo.addElement("DiagnosisResult").setText(rs.getString("CZDJG")==null?"":rs.getString("CZDJG"));	
					  ReferralInfo.addElement("HospitalizationID").setText(rs.getString("CZYBAH")==null?"":rs.getString("CZYBAH")); 
					  ReferralInfo.addElement("MainResult").setText(rs.getString("CZYJCJG")==null?"":rs.getString("CZYJCJG")); 
					  ReferralInfo.addElement("TreatmentCourseNext").setText(rs.getString("CHZZLJG")==null?"":rs.getString("CHZZLJG"));//治疗经过，下一步治疗方案及康复意见
					  ReferralInfo.addElement("BackDoctor").setText(rs.getString("CHZYSXM")==null?"":rs.getString("CHZYSXM")); 
					  ReferralInfo.addElement("BackTelephone").setText(rs.getString("CHZFLXDH")==null?"":rs.getString("CHZFLXDH")); 
					  ReferralInfo.addElement("BackTime").setText(rs.getString("DHZSJ")==null?"":rs.getString("DHZSJ"));
					  ReferralInfo.addElement("Mark").setText("4");
				  }else{//转出
					  ReferralInfo.addElement("PatientAge").setText(rs.getString("CHZNL")==null?"":rs.getString("CHZNL"));	
					  ReferralInfo.addElement("FristImage").setText(rs.getString("CCBYX")==null?"":rs.getString("CCBYX")); //初步映像
					  ReferralInfo.addElement("NowMedicalHistory").setText(rs.getString("CXBS")==null?"":rs.getString("CXBS"));  //现病史
					  ReferralInfo.addElement("BeforeMedicalHistory").setText(rs.getString("CJWS")==null?"":rs.getString("CJWS"));//既往史
					  ReferralInfo.addElement("TreatmentCourse").setText(rs.getString("CZLJG")==null?"":rs.getString("CZLJG")); //治疗经过
					  ReferralInfo.addElement("Telephone").setText(rs.getString("CLXDH")==null?"":rs.getString("CLXDH")); 
					  ReferralInfo.addElement("Mark").setText("2");
				}
			}
			ResXML = document.asXML();
		} catch (Exception e) {
			ResXML = this.CreateErrorInfo("S00001",e.getMessage());
		} finally {
			conn.close();
		}
		return ResXML;
	}
			
	/**
	 * 双向转诊  转出操作 IZT=2   ReferralOut
	 * @param ele
	 * @param inOpr
	 * @return
	 * @throws Exception
	 */
	
		public Object ReferralOut(Element ele) throws Exception { 	
				List<String> list = new ArrayList<String>();
				List<String> list1 = new ArrayList<String>();
				ele=ele.element("ASK");
				if(ele == null){
					return this.CreateErrorInfo("J00001","请按照标准传入正确参数格式");
				}
				String ResXML = "";
				//根据身份证,卡号 判断his有没有这个人,有的话取主要信息，没有的话要生成一条信息
				String CSFZH = ele.element("SFZH")==null?"":ele.element("SFZH").getText();
				if("".equals(CSFZH)||CSFZH.length()!=18){
					return this.CreateErrorInfo("S00001","身份证号格式输入错误，请按标准传入正确参数。");
				}
				String where="";
				String CardType = ele.element("CardType").getText()==null?"":ele.element("CardType").getText();
				String CardNumber = ele.element("CardNumber").getText()==null?"":ele.element("CardNumber").getText();
				if("".equals(CardType) || "".equals(CardNumber)){
					return this.CreateErrorInfo("K00001","卡类型和卡号不能为空，请按标准传入正确参数。");
				}
				int a = Integer.parseInt(CardType);
				switch (a) {
				case 5: //农合号
					where += " CDABH=? ";
					break;
				case 06: //医疗卡号 居民健康卡
					where += " CJKKH=? ";
					break;
				case 22: //身份证号码
					where += " CZJHM=? ";
					break;	
				default:
					break;
				}
				Connection conn = JdbcUtils_JNDI.getConnection();
				try {
					String CSQL1="";
					String TBSXZZZCJLB = "AHISDB..TBSXZZZCJLB";
					String TBHZJBXX = "AHISDB..TBHZJBXX";
					String CBM = GetXTCS.GetSysParameter("89116", 1);
					String  CZCYLJGMC=ele.element("OutMedicalName").getText()==null?"":ele.element("OutMedicalName").getText();
					String  CZCYLJGID=ele.element("OutMedicalID").getText()==null?"":ele.element("OutMedicalID").getText();
					String  ZCYSXM=ele.element("Doctor").getText()==null?"":ele.element("Doctor").getText();
					String  JLRGH=ele.element("DoctorID").getText()==null?"":ele.element("DoctorID").getText();
					String  CZRYLJGMC=ele.element("InMedicalName").getText()==null?"":ele.element("InMedicalName").getText();
					String  CZRYLJGID=ele.element("InMedicalID").getText()==null?"":ele.element("InMedicalID").getText();
					String  DZCSJ=ele.element("OutTime").getText()==null?"":ele.element("OutTime").getText();
					String  Name=ele.element("Name").getText()==null?"":ele.element("Name").getText();
					String  Sex=ele.element("Sex").getText()==null?"":ele.element("Sex").getText();
					
					String  CYZNL=ele.element("PatientAge").getText()==null?"":ele.element("PatientAge").getText();
					String  CCBYX=ele.element("FristImage").getText()==null?"":ele.element("FristImage").getText();
					String  CXBS=ele.element("NowMedicalHistory").getText()==null?"":ele.element("NowMedicalHistory").getText();
					String  CJWS=ele.element("BeforeMedicalHistory").getText()==null?"":ele.element("BeforeMedicalHistory").getText();
					String  CZLJG=ele.element("TreatmentCourse").getText()==null?"":ele.element("TreatmentCourse").getText();
					String  CLXDH=ele.element("Telephone").getText()==null?"":ele.element("Telephone").getText();
					  
					
					String[] Arr={"1",CSFZH,CZCYLJGMC,CZCYLJGID,ZCYSXM,JLRGH,CZRYLJGMC,CZRYLJGID,DZCSJ,Name,Sex,getdate(),getdate(),CYZNL,CCBYX,CXBS,CJWS,CZLJG,CLXDH};
					String[] Arr2={"处理标志","身份证号","转出医疗机构名称","转出医疗机构ID","转出医生姓名","转出医生工号","转入医疗机构名称","转入医疗机构ID",
							"转出时间","病人姓名","病人性别","最后修改时间","记录时间","患者年龄","初步印象","现病史","既往史","治疗经过","联系电话"};
					for(int i=0;i<Arr.length;i++){
						if("".equals(Arr[i])){
							return this.CreateErrorInfo("K00001",Arr2[i]+"输入错误，请按标准传入正确参数。");
						}
						list.add(Arr[i]);
					}
					String CBRID=CheckData("1",where,CardNumber);
					 if("".equals(CBRID)){//插入一条数据TBHZJBXX//HIS基本信息没有数据
						  CBRID = this.GetCBRID();
						  CSQL1=" INSERT INTO  "+TBHZJBXX+" (IFLAG,CBRID,CZJHM,CZJBM,CZJMC,CBXLBBM,CBXLBMC,CBRXM,DCSNY,CBRXBBM,CBRXBMC,CHYBM,CHYMC,"
						 		+ "CGJBM,CGJMC,CMZBM,CMZMC,CRYLBBM,CRYLBMC,DJDRQ,CJDJGID,CJDJGMC,CJDRGH,CJDRMC,CJGID,DLASTM)"
						 		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						  String[] ArrJBXX={"0",CBRID,CSFZH,"01","居民身份证","0","00",Name,CSFZH.substring(6, 14),Sex.indexOf("男")>-1?"1":"2",Sex,"-",
								     "-","-","-","-","-","-","-",getdate(),CZRYLJGID,CZRYLJGMC,"-","-","-",getdate()};
						  for(int i=0;i<ArrJBXX.length;i++){
								if("".equals(ArrJBXX[i])){
									return this.CreateErrorInfo("K00001",Arr2[i]+"输入错误，请按标准传入正确参数。");
								}
								list1.add(ArrJBXX[i]);
							}
					 }
					list.add(CBM); 
					list.add(CBRID);
					 for (int i=0;i<list1.size();i++){
						 list.add(list1.get(i));
					 }
					Document document = DocumentHelper.createDocument();
					Element MSG = document.addElement("MSG");
					Element RES = MSG.addElement("RES");
					String TSQL= "INSERT INTO "+TBSXZZZCJLB+"(ICLBZ,CSFZH,CZCYLJGMC,CZCYLJGID,CZCYSXM,CJLRGH,CZRYLJGMC,CZRYLJGID," +
							" DZCSJ,CHZXM,CHZXB,DLASTM,DJLSJ,CHZNL,CCBYX,CXBS,CJWS,CZLJG,CLXDH,CBH,CBRID) " +
							" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
										boolean bool =TDBConnPool.Execute(conn, TSQL+CSQL1, list);
						if (bool) {
							conn.commit();
							RES.addElement("ActionTime").setText(getdate());
							RES.addElement("ResultMsg").setText("1");
						}else{
							conn.rollback();
							RES.addElement("ActionTime").setText(getdate());
							RES.addElement("ResultMsg").setText("0");
						} 
						ResXML = document.asXML();
				} catch (Exception e) {
					conn.rollback();
					ResXML = this.CreateErrorInfo("S00001",e.getMessage());
				} finally {
					conn.close();
				}
				return 	ResXML;
			}
		private String CheckData(String XH,String where,String ID) throws Exception {
			/*String TBSXZZZCJLB = TTbDefList.getHMap().get("77363").GetTbName("", "");
			String TBHZJBXX = TTbDefList.getHMap().get("77365").GetTbName("", "");*/
			
			String TBSXZZZCJLB = "AHISDB..TBSXZZZCJLB";
			String TBHZJBXX = "AHISDB..TBHZJBXX";
			Connection conn = JdbcUtils_JNDI.getConnection();
			List<String> list = new ArrayList<String>();
			ResultSet rs = null;
			String CSQL = "";
			if("1".equals(XH)){
				CSQL = " SELECT   CBRID,CBRXM,CONVERT(varchar(16),DCSNY,121) DCSNY FROM "+TBHZJBXX+"  WITH(NOLOCK) WHERE "+where;
				list.add(ID);
			}else if("2".equals(XH)){
				CSQL = " SELECT  CBRID,CBH,CHZXM,CHZXB,DZCSJ,CZCYLJGID,CZCYLJGMC,CZRYLJGID  FROM  "+TBSXZZZCJLB+" WITH(NOLOCK) where  CBH=? ";
				list.add(ID);
			}
			rs = TDBConnPool.ExecQry(conn,CSQL, list);
			if(rs.next()){//取数据返回
				return 	rs.getString("CBRID");
			}else{//插入一条数据
				return 	"";
			}
		}
		
		/**
		 * //双向转诊  回转操作  IZT=4   ReferralBack 
		 * @param ele
		 * @param inOpr
		 * @return
		 * @throws Exception
		 */
		public Object ReferralBack( Element ele) throws Exception {
			String ResXML = "";
			ele=ele.element("ASK");
			if(ele == null){
				return this.CreateErrorInfo("J00001","请按照标准传入正确参数格式");
			}
			Document document = DocumentHelper.createDocument();
			Element MSG = document.addElement("MSG");
			Element RES = MSG.addElement("RES");
			//String TBSXZZHZJLB = TTbDefList.getHMap().get("77364").GetTbName("", "");//回转表
			String TBSXZZHZJLB = "ahisdb..TBSXZZHZJLB";
			String TBSXZZZCJLB = "AHISDB..TBSXZZZCJLB";
			List<String> list = new ArrayList<String>();
			if(ele.element("Key") == null)
				return this.CreateErrorInfo("K00006","Key不存在，请检查节点格式。");
			String  CBH=ele.element("Key").getText()==null?"":ele.element("Key").getText();
			String CBRID=CheckData("2","",CBH);
			if("".equals(CBRID)){
				return this.CreateErrorInfo("K00001","转出单不存在，请检查重新传入ID号。");
			}
			String  DHZSJ=ele.element("BackTime")==null || ele.element("BackTime").getText()==null?"":ele.element("BackTime").getText();
			String  CHZYSXM=ele.element("Doctor") == null || ele.element("Doctor").getText()==null?"":ele.element("Doctor").getText();
			String  CHZYSGH=ele.element("DoctorID")==null || ele.element("DoctorID").getText()==null?"":ele.element("DoctorID").getText();
			String  CZDJG=ele.element("DiagnosisResult")==null || ele.element("DiagnosisResult").getText()==null?"":ele.element("DiagnosisResult").getText();
			String  CZYJCJG=ele.element("MainResult")==null || ele.element("MainResult").getText()==null?"":ele.element("MainResult").getText();
			String  CZYBAH=ele.element("HospitalizationID")==null || ele.element("HospitalizationID").getText()==null?"":ele.element("HospitalizationID").getText();
			String  CHZFLXDH=ele.element("Telephone")==null || ele.element("Telephone").getText()==null?"":ele.element("Telephone").getText();
			String  CKFYJ=ele.element("Advise")==null || ele.element("Advise").getText()==null?"":ele.element("Advise").getText();
			String  CZRYLJGID=ele.element("CZRYLJGID")==null || ele.element("CZRYLJGID").getText()==null?"":ele.element("CZRYLJGID").getText();
			String  CZRYLJGMC=ele.element("CZRYLJGMC")==null || ele.element("CZRYLJGMC").getText()==null?"":ele.element("CZRYLJGMC").getText();
			String  CZCYLJGID=ele.element("CZCYLJGID")==null || ele.element("CZCYLJGID").getText()==null?"":ele.element("CZCYLJGID").getText();
			String  CZCYLJGMC=ele.element("CZCYLJGMC")==null || ele.element("CZCYLJGMC").getText()==null?"":ele.element("CZCYLJGMC").getText();
			String  CHZXM=ele.element("Name") == null || ele.element("Name").getText()==null?"":ele.element("Name").getText();
			//String  CHZZLJG=ele.element("TreatmentCourseNext").getText()==null?"":ele.element("TreatmentCourseNext").getText();
			
			String[] Arr={"0",DHZSJ,CHZYSXM,CHZYSGH,CZDJG,CZYJCJG,CZYBAH,CHZFLXDH,CKFYJ,CBH,CZRYLJGID,CZRYLJGMC,CZCYLJGID,CZCYLJGMC,CHZXM,CBRID};
			String[] Arr2={"处理状态","回转时间","回转医生","回转医生工号","诊断结果","主要检查结果","住院病案号","回转方联系电话","康复意见","病人转诊编号","转入医疗机构id","转入医疗机构名称","转出医疗机构ID","转出医疗机构名称","患者姓名","患者区域编号"};
			for(int i=0;i<Arr.length;i++){
				if("".equals(Arr[i])){
					return this.CreateErrorInfo("K00001",Arr2[i]+"不能为空，请重新传入");
				}
				list.add(Arr[i]);
			}
			Connection conn = JdbcUtils_JNDI.getConnection();
			try {
				String TSQL= "INSERT INTO "+TBSXZZHZJLB+" (BCL,DHZSJ,CHZYSXM,CHZYSGH,CZDJG,CZYJCJG,CZYBAH,CHZFLXDH,CKFYJ,CBH,CZRYLJGID,CZRYLJGMC,CZCYLJGID,CZCYLJGMC,CHZXM,CBRID,DLASTM)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate())";
				//TSQL=TSQL+" update "+TBSXZZZCJLB+" set ICLBZ='3'  where cbh=?";	
				boolean bool =TDBConnPool.Execute(conn, TSQL, list);
					if (bool) {
						conn.commit();
						RES.addElement("ActionTime").setText(this.getdate());
						RES.addElement("ResultMsg").setText("1");
					}else{
						conn.rollback();
						RES.addElement("ActionTime").setText(this.getdate());
						RES.addElement("ResultMsg").setText("0");
					} 
					ResXML = document.asXML();
			} catch (Exception e) {
				TLogError.WriteLogInfo(e.getMessage(), "sxzz");
				ResXML = this.CreateErrorInfo("S00001",e.getMessage());
			} finally {
				conn.close();
			}
			return 	ResXML;
		}
        
		public Object ModifyZT( Element ele) throws Exception {
			String ResXML = "";
			ele=ele.element("ASK");
			if(ele == null){
				return this.CreateErrorInfo("J00001","请按照标准传入正确参数格式");
			}
			Document document = DocumentHelper.createDocument();
			Element MSG = document.addElement("MSG");
			Element RES = MSG.addElement("RES");
			String TBSXZZHZJLB = "ahisdb..TBSXZZHZJLB";
			String TBSXZZZCJLB = "AHISDB..TBSXZZZCJLB";
			List<String> list = new ArrayList<String>();
			String  CBH=ele.element("Key").getText()==null?"":ele.element("Key").getText();
			String CBRID=CheckData("2","",CBH);
			String TSQL="";
			if("".equals(CBRID)){
				return this.CreateErrorInfo("K00001","转诊记录单不存在，请检查重新传入ID号。");
			}
			String  Mark=ele.element("Mark").getText()==null?"":ele.element("Mark").getText();
			String  ICLZT=ele.element("ICLZT").getText()==null?"":ele.element("ICLZT").getText();
			
			String[] Arr={Mark,ICLZT};
			String[] Arr2={"转诊单类型","处理状态"};
			for(int i=0;i<Arr.length;i++){
				if("".equals(Arr[i])){
					return this.CreateErrorInfo("K00001",Arr2[i]+"不能为空，请重新传入");
				}
			}
			if("2".equals(Mark)){//修改转出单状态
				 TSQL= " update "+TBSXZZZCJLB+" set ICLBZ=?  where cbh=?";
				list.add(ICLZT);
				list.add(CBH);
			}else{//修改回转单状态
				 TSQL= " update "+TBSXZZHZJLB+" set BCL='1'  where cbh=?";
				list.add(CBH);
			}
			Connection conn = JdbcUtils_JNDI.getConnection();
			try {
				boolean bool =TDBConnPool.Execute(conn, TSQL, list);
					if (bool) {
						conn.commit();
						RES.addElement("ActionTime").setText(this.getdate());
						RES.addElement("ResultMsg").setText("1");
					}else{
						conn.rollback();
						RES.addElement("ActionTime").setText(this.getdate());
						RES.addElement("ResultMsg").setText("0");
					} 
					ResXML = document.asXML();
			} catch (Exception e) {
				ResXML = this.CreateErrorInfo("S00001",e.getMessage());
			} finally {
				conn.close();
			}
			return 	ResXML;
		}
		
		public String CreateErrorInfo(String ErrBM,String ErrInfo){
			Document document = DocumentHelper.createDocument();
			Element MSG = document.addElement("MSG");
			Element RES = MSG.addElement("RES");
			RES.addElement("ActionTime").setText(this.getdate());
			RES.addElement("ResultMsg").setText("0");
			RES.addElement("ErrCode").setText(ErrBM);
			RES.addElement("ERR").setText(ErrInfo);
			return document.asXML();
		}
		// 生成病人ID
		protected String GetCBRID() throws Exception{
			return GetXTCS.GetDefParamter("AHISIDPART","")+GetXTCS.GetSysParameter("0001",1);
		}
		public String getdate() {
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time=format.format(date);
			return time;
		}
}