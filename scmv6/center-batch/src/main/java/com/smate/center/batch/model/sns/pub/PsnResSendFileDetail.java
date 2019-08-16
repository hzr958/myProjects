package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 资源详细信息.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "PSN_RES_SEND_FILE_DETAIL")
public class PsnResSendFileDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3987828195601012978L;
  private Long id;
  private PsnResSend resSend;
  private Long resSendId;
  private Long psnId;
  private String resName;
  private String resType;
  private String resDesc;
  private String resPath;
  private Integer size;
  private Integer resNodeId;
  private Date sendTime;
  private StationFile stationFile;
  private String des3Id;
  private Date resUploadTime;
  private int status;
  private String subResDesc;
  // 转义文件描述
  private String escapeFileDesc;
  private Long resArchiveId;

  private String resViewName;
  private String resViewType;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_RES_SEND_FILE_Detail", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "RES_SEND_ID", insertable = false, updatable = false)
  @JsonIgnore
  public PsnResSend getResSend() {
    return resSend;
  }

  public void setResSend(PsnResSend resSend) {
    this.resSend = resSend;
  }

  @Column(name = "RES_SEND_ID")
  public Long getResSendId() {
    return resSendId;
  }

  public void setResSendId(Long resSendId) {
    this.resSendId = resSendId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "RES_NAME")
  public String getResName() {
    return resName;
  }

  public void setResName(String resName) {
    this.resName = resName;
  }

  @Column(name = "RES_TYPE")
  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  @Column(name = "RES_DESC")
  public String getResDesc() {
    return resDesc;
  }

  public void setResDesc(String resDesc) {
    this.resDesc = resDesc;
  }

  @Column(name = "RES_PATH")
  public String getResPath() {
    return resPath;
  }

  public void setResPath(String resPath) {
    this.resPath = resPath;
  }

  @Column(name = "RES_NODE_ID")
  public Integer getResNodeId() {
    return resNodeId;
  }

  public void setResNodeId(Integer resNodeId) {
    this.resNodeId = resNodeId;
  }

  @Column(name = "RES_COMMEND_TIME")
  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  @Transient
  public StationFile getStationFile() {
    return stationFile;
  }

  public void setStationFile(StationFile stationFile) {
    this.stationFile = stationFile;
  }

  @Column(name = "RES_SIZE")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  @Transient
  public String getDes3Id() {
    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  @Column(name = "RES_UPLOAD_TIME")
  public Date getResUploadTime() {
    return resUploadTime;
  }

  public void setResUploadTime(Date resUploadTime) {
    this.resUploadTime = resUploadTime;
  }

  @Transient
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Transient
  public String getSubResDesc() {
    if (this.resDesc != null) {
      if (this.resDesc.length() > 32) {
        subResDesc = this.resDesc.substring(0, 32) + "...";
      } else {
        subResDesc = this.resDesc;
      }
    }
    return HtmlUtils.toHtml(subResDesc);
  }

  @Transient
  public String getEscapeFileDesc() {
    if (this.resDesc != null) {
      escapeFileDesc = HtmlUtils.toHtml(this.resDesc);
    }
    return escapeFileDesc;
  }

  @Column(name = "RES_ACHIVE_ID")
  public Long getResArchiveId() {
    return resArchiveId;
  }

  public void setResArchiveId(Long resArchiveId) {
    this.resArchiveId = resArchiveId;
  }

  @Transient
  public String getResViewName() {
    if (StringUtils.isNotBlank(resName)) {
      resViewName = resName.substring(0, resName.lastIndexOf("."));
    }
    return resViewName;
  }

  public void setResViewName(String resViewName) {
    this.resViewName = resViewName;
  }

  @Transient
  public String getResViewType() {
    if (StringUtils.isNotBlank(resName)) {
      resViewType = resName.substring(resName.lastIndexOf(".") + 1).toLowerCase();
    }
    return resViewType;
  }

  public void setResViewType(String resViewType) {
    this.resViewType = resViewType;
  }
}
