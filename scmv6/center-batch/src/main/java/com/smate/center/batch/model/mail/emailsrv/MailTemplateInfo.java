package com.smate.center.batch.model.mail.emailsrv;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模板信息表
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "MAIL_TEMPLATE_INFO")
public class MailTemplateInfo implements Serializable {

  private static final long serialVersionUID = 7682332993396711826L;

  private Integer tempCode;

  private String name;

  private String subject;

  private String folderName;

  private Integer status;

  private Date createDate;

  private Integer priorCode;

  private String remarks;

  public MailTemplateInfo() {

  }

  public MailTemplateInfo(Integer tempCode, String name, String subject) {

    this.tempCode = tempCode;
    this.name = name;
    this.subject = subject;
  }

  @Id
  @Column(name = "TEMP_CODE")
  public Integer getTempCode() {
    return tempCode;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "SUBJECT")
  public String getSubject() {
    return subject;
  }

  @Column(name = "FOLDER_NAME")
  public String getFolderName() {
    return folderName;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "PRIOR_CODE")
  public Integer getPriorCode() {
    return priorCode;
  }

  @Column(name = "REMARKS")
  public String getRemarks() {
    return remarks;
  }

  public void setTempCode(Integer tempCode) {
    this.tempCode = tempCode;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setPriorCode(Integer priorCode) {
    this.priorCode = priorCode;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

}
