package com.wmsapi.dto;

public class DTOSendInventory {
	
	private String bizUserId;
	private String bizUserPw;
	private String callId;
	private String encType;
	private String centerCd;
	private String brandCd;
	private String itemCd;
	private String itemState;
	private String availQty;
	private String currQty;
	public String getCenterCd() {
		return centerCd;
	}
	public void setCenterCd(String centerCd) {
		this.centerCd = centerCd;
	}
	public String getBrandCd() {
		return brandCd;
	}
	public void setBrandCd(String brandCd) {
		this.brandCd = brandCd;
	}
	public String getItemCd() {
		return itemCd;
	}
	public void setItemCd(String itemCd) {
		this.itemCd = itemCd;
	}
	public String getItemState() {
		return itemState;
	}
	public void setItemState(String itemState) {
		this.itemState = itemState;
	}
	public String getAvailQty() {
		return availQty;
	}
	public void setAvailQty(String availQty) {
		this.availQty = availQty;
	}
	public String getCurrQty() {
		return currQty;
	}
	public void setCurrQty(String currQty) {
		this.currQty = currQty;
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
