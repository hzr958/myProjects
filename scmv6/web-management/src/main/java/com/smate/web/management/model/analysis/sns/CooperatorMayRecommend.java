package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基金、论文推荐合作者：可能合作者实体类.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "COOPERATOR_MAY_RECOMMEND")
public class CooperatorMayRecommend implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -3572891896143282602L;
  // 主键
  private Long id;
  // 用户psnId
  private Long psnId;
  // 合作者psnId
  private Long coPsnId;
  // 质量分数,hindex
  private Integer coQuality = 0;
  // 部门分数，相同为1
  private Integer coDept = 0;
  // 相同期刊数量
  private Integer coJnl = 0;
  // 相同所教课程数量
  private Integer coTaught = 0;
  // 合作度分数，好友（推荐论文合作者）=1
  private Integer coDegree = 0;
  // 总分(不包括关键词)
  private Double coTotal = 0.0;
  // 创建时间
  private Date createDate = new Date();
  // 合作者版本
  private Long coVersion = -1L;

  // 临时关键词分数（关键词属于可变的，所以定义为临时变量）
  private Integer tmpCoKw = 0;

  public CooperatorMayRecommend() {

  }

  public CooperatorMayRecommend(Long psnId, Long coPsnId, Integer coQuality, Integer coDept, Integer coJnl,
      Integer coTaught, Integer coDegree) {
    this.psnId = psnId;
    this.coPsnId = coPsnId;
    this.coQuality = coQuality;
    this.coDept = coDept;
    this.coJnl = coJnl;
    this.coTaught = coTaught;
    this.coDegree = coDegree;
  }

  @Id
  @Column(name = "CO_ID")
  @SequenceGenerator(sequenceName = "SEQ_COMAY_RECOMMEND", name = "SEQ_STORE", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CO_PSN_ID")
  public Long getCoPsnId() {
    return coPsnId;
  }

  @Column(name = "CO_QUALITY")
  public Integer getCoQuality() {
    return coQuality;
  }

  @Column(name = "CO_DEPT")
  public Integer getCoDept() {
    return coDept;
  }

  @Column(name = "CO_JNL")
  public Integer getCoJnl() {
    return coJnl;
  }

  @Column(name = "CO_TAUGHT")
  public Integer getCoTaught() {
    return coTaught;
  }

  @Column(name = "CO_DEGREE")
  public Integer getCoDegree() {
    return coDegree;
  }

  @Column(name = "CO_TOTAL")
  public Double getCoTotal() {
    return coTotal;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "CO_VERSION")
  public Long getCoVersion() {
    return coVersion;
  }

  @Column(name = "TMP_CO_KW")
  public Integer getTmpCoKw() {
    return tmpCoKw;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCoPsnId(Long coPsnId) {
    this.coPsnId = coPsnId;
  }

  public void setCoQuality(Integer coQuality) {
    this.coQuality = coQuality;
  }

  public void setCoDept(Integer coDept) {
    this.coDept = coDept;
  }

  public void setCoJnl(Integer coJnl) {
    this.coJnl = coJnl;
  }

  public void setCoTaught(Integer coTaught) {
    this.coTaught = coTaught;
  }

  public void setCoDegree(Integer coDegree) {
    this.coDegree = coDegree;
  }

  public void setCoTotal(Double coTotal) {
    this.coTotal = coTotal;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setCoVersion(Long coVersion) {
    this.coVersion = coVersion;
  }

  public void setTmpCoKw(Integer tmpCoKw) {
    this.tmpCoKw = tmpCoKw;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((coDegree == null) ? 0 : coDegree.hashCode());
    result = prime * result + ((coDept == null) ? 0 : coDept.hashCode());
    result = prime * result + ((coJnl == null) ? 0 : coJnl.hashCode());
    result = prime * result + ((coPsnId == null) ? 0 : coPsnId.hashCode());
    result = prime * result + ((coQuality == null) ? 0 : coQuality.hashCode());
    result = prime * result + ((coTaught == null) ? 0 : coTaught.hashCode());
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
    CooperatorMayRecommend other = (CooperatorMayRecommend) obj;
    if (coDegree == null) {
      if (other.coDegree != null)
        return false;
    } else if (!coDegree.equals(other.coDegree))
      return false;
    if (coDept == null) {
      if (other.coDept != null)
        return false;
    } else if (!coDept.equals(other.coDept))
      return false;
    if (coJnl == null) {
      if (other.coJnl != null)
        return false;
    } else if (!coJnl.equals(other.coJnl))
      return false;
    if (coPsnId == null) {
      if (other.coPsnId != null)
        return false;
    } else if (!coPsnId.equals(other.coPsnId))
      return false;
    if (coQuality == null) {
      if (other.coQuality != null)
        return false;
    } else if (!coQuality.equals(other.coQuality))
      return false;
    if (coTaught == null) {
      if (other.coTaught != null)
        return false;
    } else if (!coTaught.equals(other.coTaught))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }

}
