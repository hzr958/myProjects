package com.smate.center.task.model.grp;

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
 * 群组成果初始化
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_GRP_PUB_INIT")
public class GrpPubInit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2998444744300814245L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_PUB_INIT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键id
  @Column(name = "GRP_ID")
  private Long grpId; // 群组id
  @Column(name = "PUB_ID")
  private Long pubId; // 推荐成果id
  @Column(name = "PUB_YEAR")
  private Integer pubYear;// 推荐成果年份
  @Column(name = "CITATIONS")
  private Integer citations;// 推荐成果引用次数
  @Column(name = "HAS_FULLTEXT")
  private Integer hasFulltext;// 推荐成果是否有全文
  @Column(name = "STATUS")
  private Integer status = 0;// 是否已加入群组
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 更新时间

  public GrpPubInit() {
    super();
  }

  public GrpPubInit(Long grpId, Long pubId, Integer pubYear, Integer citations, Integer hasFulltext, Date createDate) {
    super();
    this.grpId = grpId;
    this.pubId = pubId;
    this.pubYear = pubYear;
    this.citations = citations;
    this.hasFulltext = hasFulltext;
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public Integer getHasFulltext() {
    return hasFulltext;
  }

  public void setHasFulltext(Integer hasFulltext) {
    this.hasFulltext = hasFulltext;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }


}
