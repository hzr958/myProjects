package com.smate.web.v8pub.po.representpub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员代表性成果
 *
 * @author wsn
 * @createTime 2017年3月14日 下午8:17:08
 *
 */
@Entity
@Table(name = "V_REPRESENT_PUB")
public class RepresentPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6425045102402109756L;

  private RepresentPubPk representPubPk; // 联合主键类

  private Integer seqNo; // 排序用

  private Integer status; // 状态 0：有效， 1：无效

  public RepresentPub(Long pubId, Long psnId, Integer seqNo, Integer status) {
    super();
    new RepresentPubPk().setPsnId(psnId);
    new RepresentPubPk().setPubId(pubId);
    this.seqNo = seqNo;
    this.status = status;
  }

  public RepresentPub() {
    super();
    this.representPubPk = new RepresentPubPk();
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
  public RepresentPubPk getRepresentPubPk() {
    return representPubPk;
  }


  public void setRepresentPubPk(RepresentPubPk representPubPk) {
    this.representPubPk = representPubPk;
  }


  public void setRepPubId(Long pubId) {
    if (this.representPubPk == null) {
      representPubPk = new RepresentPubPk();
    }
    this.representPubPk.setPubId(pubId);
  }

  @Transient
  public Long getRepPubId() {
    if (this.representPubPk == null) {
      representPubPk = new RepresentPubPk();
    }
    return this.representPubPk.getPubId();
  }


  public void setRepPsnId(Long psnId) {
    if (this.representPubPk == null) {
      representPubPk = new RepresentPubPk();
    }
    this.representPubPk.setPsnId(psnId);
  }

  @Transient
  public Long getRepPsnId() {
    if (this.representPubPk == null) {
      representPubPk = new RepresentPubPk();
    }
    return this.representPubPk.getPsnId();
  }

}
