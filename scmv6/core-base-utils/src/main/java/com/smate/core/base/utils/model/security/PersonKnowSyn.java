package com.smate.core.base.utils.model.security;

import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 个人基本信息--人员推荐冗余表.
 * 
 * @author cwli
 * 
 */
@Entity
@Table (name = "PERSON_KNOW")
public class PersonKnowSyn implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5524060905747335066L;
	// 用户编码
	private Long personId;
	// 信息完整度
	private Integer complete;
	private String des3Id;
	// 标示是从导入用户或单位添加
	private Integer isAdd;
	// 如果该字段没有值则公开。如果有值则不公开
	private Integer isPrivate;
	// 是否已登录过
	private Integer isLogin;
	// 是否允许添加好友
	private Integer isAddFrd;
	// 0禁用，1正常，2删除
	private Integer enabled;

	/**
	 *
	 */
	public PersonKnowSyn() {

	}

	@Id
	@Column (name = "PSN_ID")
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Column (name = "IS_PRIVATE")
	public Integer getIsPrivate() {
		if (isPrivate == null)
			return 0;
		return isPrivate;
	}

	public void setIsPrivate(Integer isPrivate) {
		this.isPrivate = isPrivate;
	}

	@Column (name = "IS_LOGIN")
	public Integer getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Integer isLogin) {
		this.isLogin = isLogin;
	}

	@Column (name = "COMPLETE")
	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

	@Column (name = "IS_ADD")
	public Integer getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}

	@Column (name = "IS_ADDFRD")
	public Integer getIsAddFrd() {
		return isAddFrd;
	}

	public void setIsAddFrd(Integer isAddFrd) {
		this.isAddFrd = isAddFrd;
	}

	@Column (name = "ENABLED")
	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	@Transient
	public String getDes3Id() {
		if (this.personId != null && des3Id == null) {
			des3Id = ServiceUtil.encodeToDes3(this.personId.toString());
		}
		return des3Id;
	}

	public void setDes3Id(String des3Id) {
		this.des3Id = des3Id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
