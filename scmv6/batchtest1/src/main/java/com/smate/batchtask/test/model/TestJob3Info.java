package com.smate.batchtask.test.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "V_BATCH_TEST_JOB3INFO")
public class TestJob3Info implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5288599612735275746L;

	@Id
	@Column(name = "INFO_ID")
	@SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_BATCH_TEST_JOB3INFO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
	private Long infoId;
	
	@Column(name = "MSG_ID")
	private Long msgId;
	
	@Column(name = "THREAD_INFO")
	private String threadInfo;
	
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	public TestJob3Info() {
		super();
	}

	public Long getInfoId() {
		return infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getThreadInfo() {
		return threadInfo;
	}

	public void setThreadInfo(String threadInfo) {
		this.threadInfo = threadInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}