package com.smate.sie.core.base.utils.model.prj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @since 2018年3月1日
 * @descript 项目学科
 */
@Entity
@Table(name = "PRJ_DISCIPLINE")
public class SiePrjDiscipline {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_DISCIPLINE", allocationSize = 1)
  private Long id;
  @Column(name = "prj_id")
  private Long prjId;
  @Column(name = "DIS_CODE") // 学科代码
  private String disCode;
  @Column(name = "ZH_NAME") // 学科名称
  private String zhName;
  @Column(name = "en_name") // 学科名称
  private String EnName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getDisCode() {
    return disCode;
  }

  public void setDisCode(String disCode) {
    this.disCode = disCode;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return EnName;
  }

  public void setEnName(String enName) {
    EnName = enName;
  }

}
