package com.smate.center.batch.model.pdwh.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果匹配中文姓名.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_ZH_NAME")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class PsnPmZhName implements Serializable {

  private static final long serialVersionUID = -5858804153089857996L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String zhName;// 人员中文名.
  private Integer type;// 人名状态(1-原始姓名；2-补充姓名，有可能是用户曾用名.)

  public PsnPmZhName() {
    super();
  }

  public PsnPmZhName(Long id, Long psnId, String zhName, Integer type) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.zhName = zhName;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_ZH_NAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "TYPE")
  public Integer getType() {
    return type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
