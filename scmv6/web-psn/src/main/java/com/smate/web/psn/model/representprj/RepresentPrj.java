package com.smate.web.psn.model.representprj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员代表性项目
 *
 * @author wsn
 * @createTime 2017年3月14日 下午8:17:08
 *
 */
@Entity
@Table(name = "V_REPRESENT_PRJ")
public class RepresentPrj implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 4553633331509517756L;

  private RepresentPrjPk representPrjPk; // 联合主键类

  private Integer seqNo; // 排序用

  private Integer status; // 状态 0：有效， 1：无效

  public RepresentPrj(Long prjId, Long psnId, Integer seqNo, Integer status) {
    super();
    new RepresentPrjPk().setPsnId(psnId);
    new RepresentPrjPk().setPrjId(prjId);
    this.seqNo = seqNo;
    this.status = status;
  }

  public RepresentPrj() {
    super();
    this.representPrjPk = new RepresentPrjPk();
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }


  public void setStatus(Integer status) {
    this.status = status;
  }

  @EmbeddedId
  public RepresentPrjPk getRepresentPrjPk() {
    return representPrjPk;
  }


  public void setRepresentPrjPk(RepresentPrjPk representPrjPk) {
    this.representPrjPk = representPrjPk;
  }


  public void setRepPrjId(Long prjId) {
    if (this.representPrjPk == null) {
      representPrjPk = new RepresentPrjPk();
    }
    this.representPrjPk.setPrjId(prjId);
  }

  @Transient
  public Long getRepPrjId() {
    if (this.representPrjPk == null) {
      representPrjPk = new RepresentPrjPk();
    }
    return this.representPrjPk.getPrjId();
  }


  public void setRepPsnId(Long psnId) {
    if (this.representPrjPk == null) {
      representPrjPk = new RepresentPrjPk();
    }
    this.representPrjPk.setPsnId(psnId);
  }

  @Transient
  public Long getRepPsnId() {
    if (this.representPrjPk == null) {
      representPrjPk = new RepresentPrjPk();
    }
    return this.representPrjPk.getPsnId();
  }

}
