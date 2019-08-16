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
 * 用户成果匹配英文名表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_EN_NAME")
public class PsnPmEnName implements Serializable {

  private static final long serialVersionUID = -3508839820405590413L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String enName;// 人员英文名.
  private Integer type;// 1:prefix_name,前缀名;2:init_name,简名;3:full_name,全名;4:addt_name补充名.

  public PsnPmEnName() {
    super();
  }

  public PsnPmEnName(Long id, Long psnId, String enName, Integer type) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.enName = enName;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_EN_NAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
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

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
