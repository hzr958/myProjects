package com.smate.web.psn.model.resume;

/**
 * 简历-项目模块
 * 
 * @author lhd
 *
 */
public class CVPrj {
  private Long prjId;// 项目id
  private Integer seq;// 顺序

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Integer getSeq() {
    return seq;
  }

  public void setSeq(Integer seq) {
    this.seq = seq;
  }

}
