/**
 * 
 */
package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 学科代码表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "const_discipline")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConstDiscipline implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4860417643306757659L;
  /**
   * 
   */
  private Long id;
  /**
   * 
   */
  private Long superId;

  private String zhName;

  private String enName;

  // private Integer seqNo;

  // private ConstDiscipline constDisciplineSuper;

  // private List<ConstDiscipline> constDisciplineSub;

  private String des3Id;

  private String subZhName;

  private String subEnName;
  // 学科代码
  private String discCode;
  // 编码
  private String code;

  // 1=有子学科，0=没有子学科
  private String isSubItem;

  private int discLevel;// 学科代码级别(1-一级学科；2-二级学科；3-三级学科；4-四级学科)_MJG_SCM-4750.

  public ConstDiscipline() {
    super();
  }

  public ConstDiscipline(Long id, Long superId) {
    super();
    this.id = id;
    this.superId = superId;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "SUPER_ID")
  public Long getSuperId() {
    return superId;
  }

  public void setSuperId(Long superId) {
    this.superId = superId;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  // @Transient
  // public ConstDiscipline getConstDisciplineSuper() {
  // return constDisciplineSuper;
  // }
  //
  // public void setConstDisciplineSuper(ConstDiscipline constDisciplineSuper)
  // {
  // this.constDisciplineSuper = constDisciplineSuper;
  // }

  // @Column(name = "SEQ_NO")
  // public Integer getSeqNo() {
  // return seqNo;
  // }
  //
  // public void setSeqNo(Integer seqNo) {
  // this.seqNo = seqNo;
  // }

  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  // @Transient
  // public List<ConstDiscipline> getConstDisciplineSub() {
  // return constDisciplineSub;
  // }
  //
  // public void setConstDisciplineSub(List<ConstDiscipline>
  // constDisciplineSub) {
  // this.constDisciplineSub = constDisciplineSub;
  // }

  @Transient
  public String getDes3Id() {
    if (this.id != null && des3Id == null)
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    return des3Id;
  }

  @Column(name = "IS_SUB_ITEM")
  public String getIsSubItem() {
    return isSubItem;
  }

  public void setIsSubItem(String isSubItem) {
    this.isSubItem = isSubItem;
  }

  @Column(name = "DISC_CODE")
  public String getDiscCode() {
    return discCode;
  }

  public void setDiscCode(String discCode) {
    this.discCode = discCode;
  }

  @Transient
  public String getSubZhName() {
    if (this.zhName != null) {
      zhName = discCode + "-" + zhName;
      if (this.zhName.length() > 18)
        subZhName = this.zhName.substring(0, 18) + "...";
      else
        subZhName = this.zhName;
    }
    return subZhName;
  }

  @Transient
  public String getSubEnName() {
    if (this.enName != null) {
      enName = discCode + "-" + enName;
      if (this.enName.length() > 27)
        subEnName = this.enName.substring(0, 27) + "...";
      else
        subEnName = this.enName;
    }
    return subEnName;
  }

  @Transient
  public int getDiscLevel() {
    return discLevel;
  }

  public void setDiscLevel(int discLevel) {
    this.discLevel = discLevel;
  }

}
