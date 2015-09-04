package com.wmsapi.dto;

public class DTOSendExpressTrace {
	
	private String bizUserId;
	private String bizUserPw;
	private String callId;
	private String encType;
	private String brandNo;
	private String expressNo;
	private String dvState;
	private String noDvReason;
	private String regDatetime;
	public String getBrandNo() {
		return brandNo;
	}
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getDvState() {
		return dvState;
	}
	public void setDvState(String dvState) {
		this.dvState = dvState;
	}
	public String getNoDvReason() {
		return noDvReason;
	}
	public void setNoDvReason(String noRsnCd) {
		this.noDvReason = noRsnCd;
	}
	public String getRegDatetime() {
		return regDatetime;
	}
	public void setRegDatetime(String regDatetime) {
		this.regDatetime = regDatetime;
	}
	public String getBizUserId() {
		return bizUserId;
	}
	public void setBizUserId(String bizUserId) {
		this.bizUserId = bizUserId;
	}
	public String getBizUserPw() {
		return bizUserPw;
	}
	public void setBizUserPw(String bizUserPw) {
		this.bizUserPw = bizUserPw;
	}
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public String getEncType() {
		return encType;
	}
	public void setEncType(String encType) {
		this.encType = encType;
	}
}
