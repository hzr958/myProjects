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
 * 用户合作者英文姓名表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_CO_EN_NAME")
public class PsnPmCoEnName implements Serializable {

  private static final long serialVersionUID = 8267755052216203035L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String coName;// 合作者英文名.
  private Integer pubCo = 0;// 是否成果合作者.
  private Integer prpCo = 0;// 是否申请书合作者.
  private Integer prjCo = 0;// 是否项目合作者.
  private Integer type = 0;// 英文名类别(1:init_name,2:full_name,3other).

  public PsnPmCoEnName() {
    super();
  }

  public PsnPmCoEnName(Long id, Long psnId, String coName, Integer pubCo, Integer prpCo, Integer prjCo, Integer type) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.coName = coName;
    this.pubCo = pubCo;
    this.prpCo = prpCo;
    this.prjCo = prjCo;
    this.type = type;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_CO_EN_NAME", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CO_NAME")
  public String getCoName() {
    return coName;
  }

  @Column(name = "PUB_CO")
  public Integer getPubCo() {
    return pubCo;
  }

  @Column(name = "PRP_CO")
  public Integer getPrpCo() {
    return prpCo;
  }

  @Column(name = "PRJ_CO")
  public Integer getPrjCo() {
    return prjCo;
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

  public void setCoName(String coName) {
    this.coName = coName;
  }

  public void setPubCo(Integer pubCo) {
    this.pubCo = pubCo;
  }

  public void setPrpCo(Integer prpCo) {
    this.prpCo = prpCo;
  }

  public void setPrjCo(Integer prjCo) {
    this.prjCo = prjCo;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
