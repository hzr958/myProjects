package com.smate.web.prj.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 项目文件导入的原始数据
 */
@Entity
@Table(name = "v_prj_import_original_data")
public class PrjImportOriginalData implements Serializable {



  private static final long serialVersionUID = -2506171690348809163L;

  @Id
  @Column(name = "PRJ_ID")
  private Long prjId;

  @Column(name = "CREATE_PSN_ID")
  private Long createPsnId;

  @Column(name = "PRJ_DATA")
  private String prjData ;  // 项目数据

  @Column(name = "TYPE")
  private Integer type = 1 ;  //类型,扩展字段    1=资助机构模板

  @Column(name = "GMT_GREATE")
  private Date gmtGreate ;

  @Column(name = "GMT_UPDATE")
  private Date gmtUpdate ;


  public PrjImportOriginalData() {

  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public String getPrjData() {
    return prjData;
  }

  public void setPrjData(String prjData) {
    this.prjData = prjData;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Date getGmtGreate() {
    return gmtGreate;
  }

  public void setGmtGreate(Date gmtGreate) {
    this.gmtGreate = gmtGreate;
  }

  public Date getGmtUpdate() {
    return gmtUpdate;
  }

  public void setGmtUpdate(Date gmtUpdate) {
    this.gmtUpdate = gmtUpdate;
  }
}
