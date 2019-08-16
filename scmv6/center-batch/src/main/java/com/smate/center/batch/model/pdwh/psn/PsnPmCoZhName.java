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
 * 用户合作者中文名表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_CO_ZH_NAME")
public class PsnPmCoZhName implements Serializable {

  private static final long serialVersionUID = 1182554788505659142L;
  private Long id;// 主键.
  private Long psnId;// 人员ID.
  private String coName;// 合作者中文名.
  private String firstName;// 拼音(first name).
  private String lastName;// 拼音(last name).
  private Integer dyzFlag = 0;// 合作者中文是否包含多音字.
  private Integer pubCo = 0;// 是否成果合作者.
  private Integer prpCo = 0;// 是否申请书合作者.
  private Integer prjCo = 0;// 是否项目合作者.

  public PsnPmCoZhName() {
    super();
  }

  public PsnPmCoZhName(Long id, Long psnId, String coName, String firstName, String lastName, Integer dyzFlag,
      Integer pubCo, Integer prpCo, Integer prjCo) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.coName = coName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dyzFlag = dyzFlag;
    this.pubCo = pubCo;
    this.prpCo = prpCo;
    this.prjCo = prjCo;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_CO_ZH_NAME", allocationSize = 1)
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

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  @Column(name = "DYZ_FLAG")
  public Integer getDyzFlag() {
    return dyzFlag;
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCoName(String coName) {
    this.coName = coName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setDyzFlag(Integer dyzFlag) {
    this.dyzFlag = dyzFlag;
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

}
