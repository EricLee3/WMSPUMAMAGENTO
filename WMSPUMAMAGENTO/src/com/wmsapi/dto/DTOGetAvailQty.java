package com.wmsapi.dto;

public class DTOGetAvailQty {
	
	private String centerCd;
	private String brandCd;
	private String itemCd;
	private String itemState;
	private String currQty;
	private String availQty;
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

	public String getCurrQty() {
		return currQty;
	}

	public void setCurrQty(String currQty) {
		this.currQty = currQty;
	}

	public String getAvailQty() {
		return availQty;
	}

	public void setAvailQty(String availQty) {
		this.availQty = availQty;
	}
}
