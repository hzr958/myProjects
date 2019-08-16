package com.smate.sie.core.base.utils.model.validate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

@Entity
@Table(name = "KPI_VALIDATE_MAIN_UPLOAD")
public class KpiValidateMainUpload implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6705813265700959641L;

  private Long id;

  private String uuId;

  private Long psnId;

  private String psnName;

  private Long insId;

  private String insName;

  private Long fileId;

  private Integer isDel; // 0未删除 1已删除

  private String ip;

  private Date submitTime;


  private String submitTimeStr; // 提交时间，列表显示用
  private KpiValidateMain main;
  private String des3Id;

  public KpiValidateMainUpload() {
    super();
  }

  public KpiValidateMainUpload(Long id, Long psnId, Date submitTime, Long fileId, String uuId) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.submitTime = submitTime;
    this.fileId = fileId;
    this.uuId = uuId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_KPI_VALIDATE_MAIN_UPLOAD", allocationSize = 1)
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "UUID")
  public String getUuId() {
    return uuId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  @Column(name = "FILE_ID")
  public Long getFileId() {
    return fileId;
  }

  @Column(name = "IS_DEL")
  public Integer getIsDel() {
    return isDel;
  }

  @Column(name = "IP")
  public String getIp() {
    return ip;
  }

  @Column(name = "SUBMIT_TIME")
  public Date getSubmitTime() {
    return submitTime;
  }

  public void setSubmitTime(Date submitTime) {
    this.submitTime = submitTime;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUuId(String uuId) {
    this.uuId = uuId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  @Transient
  public KpiValidateMain getMain() {
    return main;
  }

  public void setMain(KpiValidateMain main) {
    this.main = main;
  }

  @Transient
  public String getSubmitTimeStr() {
    if (submitTime != null) {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try {
        submitTimeStr = format.format(submitTime);
      } catch (Exception e) {
        submitTimeStr = "";
      }
    }
    return submitTimeStr;
  }

  public void setSubmitTimeStr(String submitTimeStr) {
    this.submitTimeStr = submitTimeStr;
  }

  @Transient
  public String getDes3Id() {
    if (this.uuId != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

}
