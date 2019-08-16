package com.smate.web.v8pub.vo.sns.newresume;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 我的简历与科研成果关系表.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PERSONAL_RESUME_PUB")
public class PersonalResumePub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2585013911530093622L;
  // 主键
  private Long resumePubId;
  // 成果pub_id
  private Long pubId;
  // 我的简历主键，关联psn_resume.resume_id
  private Long resumeId;
  // 成果排序
  private Integer seqNo;

  @Id
  @Column(name = "RESUME_PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PERSONAL_RESUME_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getResumePubId() {
    return resumePubId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "RESUME_ID")
  public Long getResumeId() {
    return resumeId;
  }

  public void setResumePubId(Long resumePubId) {
    this.resumePubId = resumePubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setResumeId(Long resumeId) {
    this.resumeId = resumeId;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

}
