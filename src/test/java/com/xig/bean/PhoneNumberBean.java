package com.xig.bean;

public class PhoneNumberBean implements Comparable<PhoneNumberBean> {

	private String prefix;

	private String phone;

	private String province;

	private String city;

	private String isp;

	private String post_code;

	private String city_code;

	private String area_code;

	public PhoneNumberBean(String prefix,
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
	public int compareTo(PhoneNumberBean phoneNumberBean) {
		int c1 = this.phone.length();
		int c2 = phoneNumberBean.phone.length();

		if (c1 > c2) {
			return 1;
		} else if (c1 < c2) {
			return -1;
		} else {
			for (int i = 0; i < this.phone.length(); ++i) {
				c1 = this.phone.charAt(i);
				c2 = phoneNumberBean.phone.charAt(i);
				if (c1 == c2) {
					continue;
				} else if (c1 > c2) {
					return 1;
				} else {
					return -1;
				}
			}
			return 0;
		}
	}

	@Override
	public String toString() {
		return this.phone;
	}
}
