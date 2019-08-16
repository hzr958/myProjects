package com.smate.web.v8pub.po.representpub;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * v_represent_pub的主键
 *
 * @author wsn
 * @createTime 2017年3月17日 下午3:12:52
 *
 */
public class RepresentPubPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -950496382613194333L;
  private Long psnId; // 人员ID
  private Long pubId; // 成果ID

  public RepresentPubPk() {
    super();
  }

  public RepresentPubPk(Long psnId, Long pubId) {
    super();
    this.psnId = psnId;
    this.pubId = pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

}
