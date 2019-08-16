package com.smate.web.fund.agency.model;

import java.io.Serializable;

/**
 * 机构检索左边地区菜单
 * 
 * @author wsn
 *
 */
public class InsSearchRegion implements Serializable {

	private static final long serialVersionUID = -824441065051016579L;
	private String zhName; // 中文名称
	private String enName; // 英文名称
	private String showName; // 页面显示的名称
	private Long count;// 统计数
	private Long regionId; // 地区ID

	public InsSearchRegion(String zhName, String enName, String showName, Long count, Long regionId) {
		super();
		this.zhName = zhName;
		this.enName = enName;
		this.showName = showName;
		this.count = count;
		this.regionId = regionId;
	}

	public InsSearchRegion() {
		super();
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
