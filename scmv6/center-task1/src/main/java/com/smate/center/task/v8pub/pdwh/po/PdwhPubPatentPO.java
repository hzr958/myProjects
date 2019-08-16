package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果专利数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */

@Entity
@Table(name = "V_PUB_PDWH_PATENT")
public class PdwhPubPatentPO implements Serializable {

  private static final long serialVersionUID = -70393454031013078L;

  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;// 基准库成果id，主键

  @Column(name = "APPLICATION_NO")
  private String applicationNo; // 申请号

  @Column(name = "PUBLICATION_OPEN_NO")
  private String publicationOpenNo; // 专利公开（公告）号

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getApplicationNo() {
    return applicationNo;
  }

  public void setApplicationNo(String applicationNo) {
    this.applicationNo = applicationNo;
  }

  public String getPublicationOpenNo() {
    return publicationOpenNo;
  }

  public void setPublicationOpenNo(String publicationOpenNo) {
    this.publicationOpenNo = publicationOpenNo;
  }

}
