package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果期刊数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Entity
@Table(name = "V_PUB_PDWH_JOURNAL")
public class PdwhPubJournalPO implements Serializable {

  private static final long serialVersionUID = 3273873676632437278L;

  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id，主键

  @Column(name = "JID")
  private Long jid; // 期刊id，也是BASE_JOURNAL的主键

  @Column(name = "NAME")
  private String name;// 期刊名称

  @Column(name = "ISSN")
  private String issn; // ISSN号

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

}
