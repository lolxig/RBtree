package com.xig.bean;

public class OldPhoneBean implements Comparable<OldPhoneBean> {

	private String phone;

	private String cityCode;

	private String address;

	private String ispName;

	private String postCode;

	private String areaCode;

	private String ispCode;

	private String update;

	public OldPhoneBean (String phone, String cityCode, String address, String ispName,
	                     String postCode, String areaCode, String ispCode, String update) {
		this.phone = phone;
		this.cityCode = cityCode;
		this.address = address;
		this.ispName = ispName;
		this.postCode = postCode;
		this.areaCode = areaCode;
		this.ispCode = ispCode;
		this.update = update;
	}


	@Override
	public int compareTo(OldPhoneBean oldPhoneBean) {
		return this.phone.compareTo(oldPhoneBean.phone);
	}

	@Override
	public String toString() {
		return phone + "\t" +
				cityCode + "\t" +
				address + "\t" +
				ispName + "\t" +
				postCode + "\t" +
				areaCode + "\t" +
				ispCode + "\t" +
				update;
	}

}
