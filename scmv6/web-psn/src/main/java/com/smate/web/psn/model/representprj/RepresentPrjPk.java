package com.smate.web.psn.model.representprj;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * v_represent_prj的主键
 *
 * @author wsn
 * @createTime 2017年3月17日 下午3:12:52
 *
 */
public class RepresentPrjPk implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -5147217851867885315L;
  private Long psnId; // 人员ID
  private Long prjId; // 成果ID

  public RepresentPrjPk() {
    super();
  }

  public RepresentPrjPk(Long psnId, Long prjId) {
    super();
    this.psnId = psnId;
    this.prjId = prjId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

}
