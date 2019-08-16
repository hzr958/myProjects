package com.smate.web.group.model.grp.keywords;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "psn_kw_rmc_ref_hotkw")
public class PsnKwRmcRefHotkw implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -768873030183273588L;
  /**
   * 
   */

  private Long id;
  // 人员ID
  private Long psnId;
  // 热词关键词ID
  private Long kwHkid;
  // 序号
  private Integer seqNo;
  // 相关关键词ID
  private Long rkid;

  public PsnKwRmcRefHotkw() {
    super();
  }

  public PsnKwRmcRefHotkw(Long psnId, Long kwHkid, Integer seqNo, Long rkid) {
    super();
    this.psnId = psnId;
    this.kwHkid = kwHkid;
    this.seqNo = seqNo;
    this.rkid = rkid;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_KW_RMC_REF_HOTKW", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KW_HKID")
  public Long getKwHkid() {
    return kwHkid;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "RKID")
  public Long getRkid() {
    return rkid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKwHkid(Long kwHkid) {
    this.kwHkid = kwHkid;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public void setRkid(Long rkid) {
    this.rkid = rkid;
  }
}
