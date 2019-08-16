package com.smate.center.batch.model.psn;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 个人所教课程.
 * 
 * @author zym
 * 
 */
@Entity
@Table(name = "PERSON_TAUGHT")
public class PersonTaught implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8820728969361725505L;
  // 主键
  private Long psnId;
  // 创建时间
  private Date createDate;
  // 所教课程内容
  private String content;

  @Id
  @Column(name = "PSN_ID")
  @GenericGenerator(name = "SEQ_STORE", strategy = "assigned")
  @GeneratedValue(generator = "SEQ_STORE")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CONTENT")
  public String getContent() {
    return content;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

}
