package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果、文献修改实体.
 * 
 * @author lcw
 * 
 */
@Entity
@Table(name = "PUBLICATION")
public class JnlPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1546979583539553815L;
  // 成果编号
  private Long id;
  // 所有人ID
  private Long psnId;
  // 成果类型 const_pub_type
  private Integer typeId;
  // 影响因子排序字段
  private Double impactFactorsSort;
  // 成果所属 期刊id
  private Long jid;
  // 成果为1,文献为2
  private Integer articleType;

  public JnlPub() {
    super();
  }

  @Id
  @Column(name = "PUB_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "PUB_TYPE")
  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  @Column(name = "IMPACT_FACTORS_SORT")
  public Double getImpactFactorsSort() {
    return impactFactorsSort;
  }

  public void setImpactFactorsSort(Double impactFactorsSort) {
    this.impactFactorsSort = impactFactorsSort;
  }

  @Column(name = "JID")
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  @Column(name = "ARTICLE_TYPE")
  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

}
