package com.smate.web.psn.model.resume;



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
 * 科研简历修改日志.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "PSN_RESUME_LOG")
public class PsnResumeLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7165910118835628892L;
  private Long id;
  private Long psnId;
  private Long tempId;
  private Date logTime;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RESUME_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "TEMP_ID")
  public Long getTempId() {
    return tempId;
  }

  public void setTempId(Long tempId) {
    this.tempId = tempId;
  }

  @Column(name = "LOG_TIME")
  public Date getLogTime() {
    return logTime;
  }

  public void setLogTime(Date logTime) {
    this.logTime = logTime;
  }

}
