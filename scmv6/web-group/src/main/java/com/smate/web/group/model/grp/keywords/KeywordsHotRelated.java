package com.smate.web.group.model.grp.keywords;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 关键词热词相关词.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "KEYWORDS_HOT_RELATED")
public class KeywordsHotRelated implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 317335404769614037L;

  private Long id;
  private Long cid;
  private Long rid;
  private Double relSim;
  // 序号
  private Integer seqNo;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "CID")
  public Long getCid() {
    return cid;
  }

  @Column(name = "RID")
  public Long getRid() {
    return rid;
  }

  @Column(name = "REL_SIM")
  public Double getRelSim() {
    return relSim;
  }

  @Transient
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCid(Long cid) {
    this.cid = cid;
  }

  public void setRid(Long rid) {
    this.rid = rid;
  }

  public void setRelSim(Double relSim) {
    this.relSim = relSim;
  }

}
