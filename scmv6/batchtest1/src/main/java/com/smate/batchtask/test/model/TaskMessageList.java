package com.smate.batchtask.test.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_BATCH_TASKMSG_LIST")
public class TaskMessageList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8848463347410323001L;
	
	@Id
	@Column(name = "MSG_ID")
	private Long messageId;
	
	@Column(name = "MSG_DETAIL")
	private String messageDetails;

	@Column(name = "WEIGHT")
	private String weight;
	
	@Column(name = "STATUS")
	private Integer status; //0默认值，1已标记，正在处理，2处理成功，3处理出错，4忽略（2,4状态需要在此表中移除，存入统计表中）
	
	public TaskMessageList() {
		super();
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(String messageDetails) {
		this.messageDetails = messageDetails;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
		
}