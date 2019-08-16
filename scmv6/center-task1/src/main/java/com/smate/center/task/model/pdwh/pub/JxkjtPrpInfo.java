package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JXKJT_PRP_INFO
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "JXKJT_PRP_INFO")
public class JxkjtPrpInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PRP_CODE")
  private Long prpCode;//
  @Column(name = "POS_CODE")
  private Long posCode;//
  @Column(name = "CONTENT")
  private String content;//
  @Column(name = "PRP_XML")
  private String prpXml;//

  public Long getPrpCode() {
    return prpCode;
  }

  public void setPrpCode(Long prpCode) {
    this.prpCode = prpCode;
  }

  public Long getPosCode() {
    return posCode;
  }

  public void setPosCode(Long posCode) {
    this.posCode = posCode;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPrpXml() {
    return prpXml;
  }

  public void setPrpXml(String prpXml) {
    this.prpXml = prpXml;
  }


}
