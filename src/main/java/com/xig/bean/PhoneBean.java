package com.xig.bean;

public class PhoneBean implements Comparable<PhoneBean> {

	private String prefix;

	private String phone;

	private String province;

	private String city;

	private String isp;

	private String post_code;

	private String city_code;

	private String area_code;

	public PhoneBean(String prefix,
	                       String phone,
	                       String province,
	                       String city,
	                       String isp,
	                       String post_code,
	                       String city_code,
	                       String area_code) {
		this.prefix = prefix;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.isp = isp;
		this.post_code = post_code;
		this.city_code = city_code;
		this.area_code = area_code;
	}

	@Override
	public int compareTo(PhoneBean anotherBean) {
		return phone.compareTo(anotherBean.phone);
	}

	@Override
	public String toString() {
		return prefix + "\t" +
				phone + "\t" +
				province + "\t" +
				city + "\t" +
				isp + "\t" +
				post_code + "\t" +
				city_code + "\t" +
				area_code;
	}

}
