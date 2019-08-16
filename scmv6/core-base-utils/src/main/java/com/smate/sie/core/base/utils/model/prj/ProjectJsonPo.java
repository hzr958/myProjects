package com.smate.sie.core.base.utils.model.prj;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目详情表
 * 
 * @author lijianming
 * 
 * @date 2019年6月5日
 */
@Entity
@Table(name = "PRJ_JSON")
public class ProjectJsonPo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7519890264268680344L;

  private Long prjId;

  private String prjJson;

  public ProjectJsonPo() {
    super();
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  @Column(name = "PRJ_JSON")
  public String getPrjJson() {
    return prjJson;
  }

  public void setPrjJson(String prjJson) {
    this.prjJson = prjJson;
  }

}
