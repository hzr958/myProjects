package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果xml处理表
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "PDWH_PUB_XML_TOHANDLE")
public class PdwhPubXmlToHandle implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5987027089929964925L;
  private Long tmpId;
  private String tmpXml;
  private Long insId;
  private Long psnId;// 当前操作人：2为默认系统批量导入；
  private Integer status;
  private String errorMsg;

  public PdwhPubXmlToHandle() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Id
  @Column(name = "TMP_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_XML_TOHANDLE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getTmpId() {
    return tmpId;
  }

  public void setTmpId(Long tmpId) {
    this.tmpId = tmpId;
  }

  @Column(name = "TMP_XML")
  public String getTmpXml() {
    return tmpXml;
  }

  public void setTmpXml(String tmpXml) {
    this.tmpXml = tmpXml;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
