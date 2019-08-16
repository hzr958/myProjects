package com.smate.web.management.model.mail.bpo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IRISSZ_MAIL_ADDR")
public class IrisEmailAddr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7910110494174059760L;
	@Id
	@Column(name = "ID")
	private Long id;
	@Column(name = "EMAIL")
	private String email;
	//1正常，0停用
	@Column(name = "STATUS")
	private Integer status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	
	
	
	
}
