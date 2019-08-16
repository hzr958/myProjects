package com.smate.web.fund.agency.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 机构赞记录表
 * 
 * @author lhd
 *
 */
@Entity
@Table(name = "V_INS_AWARD_PSN")
public class PsnAwardIns implements Serializable {

	private static final long serialVersionUID = 3696941512668278103L;
	@Id
	@Column(name = "RECORD_ID")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_AWARD_INS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	private Long recordId;// 主键
	@Column(name = "INS_ID")
	private Long insId;// 机构id
	@Column(name = "AWARDER_PSN_ID")
	private Long awardPsnId;// 赞/取消赞人员Id
	@Column(name = "AWARD_DATE")
	private Date awardDate;// 赞时间
	@Column(name = "STATUS")
	private Integer status;// 赞还是取消赞(默认0) 1:赞 0:取消赞

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Long getAwardPsnId() {
		return awardPsnId;
	}

	public void setAwardPsnId(Long awardPsnId) {
		this.awardPsnId = awardPsnId;
	}

	public Date getAwardDate() {
		return awardDate;
	}

	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getInsId() {
		return insId;
	}

	public void setInsId(Long insId) {
		this.insId = insId;
	}

	public PsnAwardIns() {
	}

	public PsnAwardIns(Long recordId, Long insId, Long awardPsnId, Date awardDate, Integer status) {
		super();
		this.recordId = recordId;
		this.insId = insId;
		this.awardPsnId = awardPsnId;
		this.awardDate = awardDate;
		this.status = status;
	}

	public PsnAwardIns(Long recordId, Long insId, Integer status) {
		super();
		this.recordId = recordId;
		this.insId = insId;
		this.status = status;
	}

}
