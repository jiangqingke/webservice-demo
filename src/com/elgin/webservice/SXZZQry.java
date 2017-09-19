package com.elgin.webservice;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.cdyx.base.Base;

public class SXZZQry extends Base implements Serializable{
	public Object GetReferralInfo(String str) {
		List<String> paramList = new ArrayList<String>();
		String ResXML = "";
		if (str == null || "".equals(str)) {
			return this.CreateErrorInfo("J00001", "请按照标准传入正确参数格式");
		}
		String[] params = str.split("\\|");
		String CSFZH = params[0];
		if ("".equals(CSFZH)) {
			return this.CreateErrorInfo("J00001", "身份证号不能为空，请重新传入");
		}
		//默认取一月时间内
		String DAYS = "30";
		if(params.length>1) {
			DAYS = params[1];
		}
		String ZCSQL = "", HZSQL = "";
		ResultSet rs1 = null, rs2 = null;
		Connection conn = this.getConnection();
		Document document = DocumentHelper.createDocument();
		Element MSG = document.addElement("MSG");
		Element RES = MSG.addElement("RES");
		RES.addElement("ActionTime").setText(getdate());
		RES.addElement("ResultMsg").setText("1");
		Element DAT = RES.addElement("DAT");

		String DZCSJ = "";
		ZCSQL = "SELECT top 1 * FROM AHISDB..TBSXZZZCJLB with(nolock) "
				+ "WHERE DZCSJ>getdate()-"+DAYS+" AND CSFZH=? ORDER BY DZCSJ DESC ";
		paramList.add(CSFZH);
		try {
			rs1 = this.ExecQry(conn, ZCSQL, paramList);
			while (rs1.next()) {
				DZCSJ = rs1.getString("DZCSJ") == null ? "" : rs1.getString("DZCSJ");
				Element ReferralInfo = DAT.addElement("ROW");
				ReferralInfo.addElement("ID").setText(rs1.getString("CBRID") == null ? "" : rs1.getString("CBRID"));
				ReferralInfo.addElement("Key").setText(rs1.getString("CBH") == null ? "" : rs1.getString("CBH"));
				ReferralInfo.addElement("Name").setText(rs1.getString("CHZXM") == null ? "" : rs1.getString("CHZXM"));
				ReferralInfo.addElement("Sex").setText(rs1.getString("CHZXB") == null ? "" : rs1.getString("CHZXB"));
				ReferralInfo.addElement("OutTime")
						.setText(rs1.getString("DZCSJ") == null ? "" : rs1.getString("DZCSJ"));
				ReferralInfo.addElement("OutMedicalID")
						.setText(rs1.getString("CZCYLJGID") == null ? "" : rs1.getString("CZCYLJGID"));
				ReferralInfo.addElement("OutMedicalName")
						.setText(rs1.getString("CZCYLJGMC") == null ? "" : rs1.getString("CZCYLJGMC"));
				ReferralInfo.addElement("InMedicalID")
						.setText(rs1.getString("CZRYLJGID") == null ? "" : rs1.getString("CZRYLJGID"));
				ReferralInfo.addElement("InMedicalName")
						.setText(rs1.getString("CZRYLJGMC") == null ? "" : rs1.getString("CZRYLJGMC"));
				ReferralInfo.addElement("PatientAge")
						.setText(rs1.getString("CHZNL") == null ? "" : rs1.getString("CHZNL"));
				ReferralInfo.addElement("FristImage")
						.setText(rs1.getString("CCBYX") == null ? "" : rs1.getString("CCBYX")); // 初步映像
				ReferralInfo.addElement("NowMedicalHistory")
						.setText(rs1.getString("CXBS") == null ? "" : rs1.getString("CXBS")); // 现病史
				ReferralInfo.addElement("BeforeMedicalHistory")
						.setText(rs1.getString("CJWS") == null ? "" : rs1.getString("CJWS"));// 既往史
				ReferralInfo.addElement("TreatmentCourse")
						.setText(rs1.getString("CZLJG") == null ? "" : rs1.getString("CZLJG")); // 治疗经过
				ReferralInfo.addElement("Telephone")
						.setText(rs1.getString("CLXDH") == null ? "" : rs1.getString("CLXDH"));
				ReferralInfo.addElement("Mark").setText("2");
				ReferralInfo.addElement("Tag").setText("转出信息");
				ReferralInfo.addElement("ICLBZ")
						.setText("1".equals(rs1.getString("ICLBZ")) ? "未接收"
								: "2".equals(rs1.getString("ICLBZ")) ? "已接收未处理"
										: "3".equals(rs1.getString("ICLBZ")) ? "已处理" : "无结果信息");

			}
		} catch (Exception e) {
			ResXML = this.CreateErrorInfo("S00001", e.getMessage());
		} finally {
			this.close(conn, rs1, null);
		}
		try {
			HZSQL = "SELECT HZ.*,ZC.DZCSJ FROM AHISDB..TBSXZZHZJLB HZ, AHISDB..TBSXZZZCJLB ZC " + "WHERE HZ.CSFZH=ZC.CSFZH AND HZ.DHZSJ>ZC.DZCSJ "
					+ "AND HZ.CSFZH=? AND ZC.DZCSJ=?";
			paramList.add(DZCSJ);
			conn = this.getConnection();
			rs2 = this.ExecQry(conn, HZSQL, paramList);
			while (rs2.next()) {
				Element ReferralInfo = DAT.addElement("ROW");
				ReferralInfo.addElement("ID").setText(rs2.getString("CBRID") == null ? "" : rs2.getString("CBRID"));
				ReferralInfo.addElement("Key").setText(rs2.getString("CBH") == null ? "" : rs2.getString("CBH"));
				ReferralInfo.addElement("Name").setText(rs2.getString("CHZXM") == null ? "" : rs2.getString("CHZXM"));
				ReferralInfo.addElement("Sex").setText(rs2.getString("CHZXB") == null ? "" : rs2.getString("CHZXB"));
				ReferralInfo.addElement("OutTime")
						.setText(rs2.getString("DZCSJ") == null ? "" : rs2.getString("DZCSJ"));
				ReferralInfo.addElement("OutMedicalID")
						.setText(rs2.getString("CZCYLJGID") == null ? "" : rs2.getString("CZCYLJGID"));
				ReferralInfo.addElement("OutMedicalName")
						.setText(rs2.getString("CZCYLJGMC") == null ? "" : rs2.getString("CZCYLJGMC"));
				ReferralInfo.addElement("InMedicalID")
						.setText(rs2.getString("CZRYLJGID") == null ? "" : rs2.getString("CZRYLJGID"));
				ReferralInfo.addElement("InMedicalName")
						.setText(rs2.getString("CZRYLJGMC") == null ? "" : rs2.getString("CZRYLJGMC"));
				ReferralInfo.addElement("DiagnosisResult")
						.setText(rs2.getString("CZDJG") == null ? "" : rs2.getString("CZDJG"));
				ReferralInfo.addElement("HospitalizationID")
						.setText(rs2.getString("CZYBAH") == null ? "" : rs2.getString("CZYBAH"));
				ReferralInfo.addElement("MainResult")
						.setText(rs2.getString("CZYJCJG") == null ? "" : rs2.getString("CZYJCJG"));
				ReferralInfo.addElement("TreatmentCourseNext")
						.setText(rs2.getString("CHZZLJG") == null ? "" : rs2.getString("CHZZLJG"));// 治疗经过，下一步治疗方案及康复意见
				ReferralInfo.addElement("BackDoctor")
						.setText(rs2.getString("CHZYSXM") == null ? "" : rs2.getString("CHZYSXM"));
				ReferralInfo.addElement("BackTelephone")
						.setText(rs2.getString("CHZFLXDH") == null ? "" : rs2.getString("CHZFLXDH"));
				ReferralInfo.addElement("BackTime")
						.setText(rs2.getString("DHZSJ") == null ? "" : rs2.getString("DHZSJ"));
				ReferralInfo.addElement("Mark").setText("4");
				ReferralInfo.addElement("Tag").setText("回转信息");
				ReferralInfo.addElement("BCL").setText(
						"1".equals(rs2.getString("BCL")) ? "已处理" : "0".equals(rs2.getString("BCL")) ? "未处理" : "");
			}
			ResXML = document.asXML();
		} catch (Exception e) {
			ResXML = this.CreateErrorInfo("S00001", e.getMessage());
		} finally {
			this.close(conn, rs2, null);
		}
		return ResXML;
	}
}
