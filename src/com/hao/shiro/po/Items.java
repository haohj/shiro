package com.hao.shiro.po;

import java.util.Date;

public class Items {
	private Integer id;

	private String name;

	private String pic;

	private String test;

	private Float price;

	private String[] piclist;

	private Date createtime;

	private String detail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic == null ? null : pic.trim();
		// 按逗号分隔将图片拆分成数组
		if (pic != null) {
			this.setPiclist(pic.split(","));
		}
	}

	public String[] getPiclist() {
		return piclist;
	}

	public void setPiclist(String[] piclist) {
		this.piclist = piclist;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getDetail() {
		return detail;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void setDetail(String detail) {
		this.detail = detail == null ? null : detail.trim();
	}
}