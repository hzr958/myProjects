package com.smate.core.base.psn.model.psncnf;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：模块、排序
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG_MOUDLE")
public class PsnConfigMoudle implements PsnCnfBase, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5649736930402842714L;

  // 配置主键
  private Long cnfId;

  // 模块开放权限(位运算任意组合使用)
  private Long anyMod;

  // 排序：大于0参与排序的模块，等于0无序模块
  private String seqNos;

  private Map<Integer, PsnCnfEnum> seqMap;

  private String[] seqChanges = new String[2];

  // 创建日期
  private Date createDate = new Date();
  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigMoudle() {

  }

  public PsnConfigMoudle(Long cnfId) {
    this.cnfId = cnfId;
  }

  @JsonIgnore
  @Override
  @Id
  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "ANY_MOD")
  public Long getAnyMod() {
    return anyMod;
  }

  @JsonIgnore
  @Column(name = "SEQ_NOS")
  public String getSeqNos() {
    return seqNos;
  }

  @JsonIgnore
  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @JsonIgnore
  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setAnyMod(Long anyMod) {
    this.anyMod = anyMod;
  }

  public void setSeqNos(String seqNos) {
    this.seqNos = seqNos;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  /**
   * @return seqMap
   */
  @Transient
  @JsonIgnore
  public Map<Integer, PsnCnfEnum> getSeqMap() {
    return seqMap;
  }

  /**
   * @param seqMap 要设置的 seqMap
   */
  public void setSeqMap(Map<Integer, PsnCnfEnum> seqMap) {
    this.seqMap = seqMap;
  }

  /**
   * @return seqChanges
   */

  @Transient
  @JsonIgnore
  public String[] getSeqChanges() {
    return seqChanges;
  }

  /**
   * @param seqChanges 要设置的 seqChanges
   */
  public void setSeqChanges(String[] seqChanges) {
    this.seqChanges = seqChanges;
  }

}
