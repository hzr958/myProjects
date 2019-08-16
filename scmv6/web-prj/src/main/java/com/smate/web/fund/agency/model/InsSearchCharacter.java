package com.smate.web.fund.agency.model;

import java.io.Serializable;

/**
 * 机构检索左边机构类别菜单
 * 
 * @author wsn
 *
 */
public class InsSearchCharacter implements Serializable {

	private static final long serialVersionUID = -6878884066558828206L;

	private String zhName; // 中文名称
	private String enName; // 英文名称
	private String showName; // 页面显示名称
	private Long count; // 统计数
	private Long characterId; // 类别ID, 来自于Institution表的nature字段

	public InsSearchCharacter() {
		super();
	}

	public InsSearchCharacter(String zhName, String enName, String showName, Long count, Long characterId) {
		super();
		this.zhName = zhName;
		this.enName = enName;
		this.showName = showName;
		this.count = count;
		this.characterId = characterId;
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

	public Long getCharacterId() {
		return characterId;
	}

	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

}
