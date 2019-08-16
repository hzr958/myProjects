package com.smate.core.base.psn.model.profile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.consts.model.ConstDiscipline;


/**
 * 个人熟悉的学科.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_DISCIPLINE")
public class PsnDiscipline implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7382801955846450672L;

  private Long id;
  private Long psnId;
  private Long disId;
  private ConstDiscipline disc;
  private String discName;
  private String strPdKeys;
  private Map<String, Object> endorseMap; // 该学科被认同人员信息 by zk
  private List<PsnDisciplineKey> pdKeys;
  private int flag;

  public PsnDiscipline() {
    super();
  }

  public PsnDiscipline(Long psnId, Long disId) {
    super();
    this.psnId = psnId;
    this.disId = disId;
  }

  public PsnDiscipline(Long id, Long psnId, Long disId, ConstDiscipline disc) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.disId = disId;
    this.disc = disc;
  }

  @Id
  @Column(name = "PSNDIS_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_DISCIPLINE", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "DIS_ID")
  public Long getDisId() {
    return disId;
  }

  public void setDisId(Long disId) {
    this.disId = disId;
  }

  @Column(name = "FLAG")
  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  @Transient
  public ConstDiscipline getDisc() {
    return disc;
  }

  public void setDisc(ConstDiscipline disc) {
    this.disc = disc;
  }

  @Transient
  public List<PsnDisciplineKey> getPdKeys() {
    return pdKeys;
  }

  public void setPdKeys(List<PsnDisciplineKey> pdKeys) {
    this.pdKeys = pdKeys;
  }

  @Transient
  public String getDiscName() {
    return discName;
  }

  @Transient
  public String getStrPdKeys() {
    return strPdKeys;
  }

  public void setDiscName(String discName) {
    this.discName = discName;
  }

  public void setStrPdKeys(String strPdKeys) {
    this.strPdKeys = strPdKeys;
  }

  @SuppressWarnings("rawtypes")
  @Transient
  public Map<String, Object> getEndorseMap() {
    return endorseMap;
  }

  public void setEndorseMap(Map<String, Object> endorseMap) {
    this.endorseMap = endorseMap;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((disId == null) ? 0 : disId.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PsnDiscipline other = (PsnDiscipline) obj;
    if (disId == null) {
      if (other.disId != null)
        return false;
    } else if (!disId.equals(other.disId))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }

}
