package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Administrator 成果合作情况
 */
@Entity
@Table(name = "KNOWLEDGEGRAPH_PUB_COOPERATION")
public class KGPubCooperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5717585943872217189L;

	private Long pdwhPubId;
	private Integer international = 0;
	private Integer national = 0;
	private Integer institute = 0;
	private Integer industry = 0;
	private Integer self = 0;

	public KGPubCooperation() {
		super();
	}

	public KGPubCooperation(Long pdwhPubId) {
		super();
		this.pdwhPubId = pdwhPubId;
	}

	@Id
	@Column(name = "PDWH_PUB_ID")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_KEYWORD_SUBSET", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	public Long getPdwhPubId() {
		return pdwhPubId;
	}

	public void setPdwhPubId(Long pdwhPubId) {
		this.pdwhPubId = pdwhPubId;
	}

	@Column(name = "INTERNATIONAL")
	public Integer getInternational() {
		return international;
	}

	public void setInternational(Integer international) {
		this.international = international;
	}

	@Column(name = "NATIONAL")
	public Integer getNational() {
		return national;
	}

	public void setNational(Integer national) {
		this.national = national;
	}

	@Column(name = "INSTITUTE")
	public Integer getInstitute() {
		return institute;
	}

	public void setInstitute(Integer institute) {
		this.institute = institute;
	}

	@Column(name = "INDUSTRY")
	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	@Column(name = "SELF")
	public Integer getSelf() {
		return self;
	}

	public void setSelf(Integer self) {
		this.self = self;
	}

}
