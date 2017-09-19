package com.cdyx.test;

import com.elgin.webservice.SXZZQry;

public class Main {
	public static void main(String[] args) {
		SXZZQry qry = new SXZZQry();
		String csfzh = "220122197310225544|360";
		String str = (String) qry.GetReferralInfo(csfzh);
		System.out.println(str);
	}
}
