package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 基准库成果作者与sns人员关联信息表
 * 
 * @author LJ
 *
 *         2017年9月6日
 */
@Table(name = "PDWH_SNS_PUBAUTHOR_RELATION")
@Entity
public class PdwhSnsPubAuthorRelation implements Serializable {
  private static final long serialVersionUID = -781973339420039092L;
  private Long id;
  private Long pubId;// 基准库成果Id
  private Long psnId;// sns人员Id
  private String name;// 匹配上的基准库成果作者名
  private String relationemail;// 基准库成果作者邮件
  private String organization;// 匹配上的基准库成果作者单位

  public PdwhSnsPubAuthorRelation(Long pubId, Long psnId, String organization) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.organization = organization;
  }

  public PdwhSnsPubAuthorRelation(Long pubId, Long psnId, String name, String organization) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.relationemail = name;
    this.organization = organization;
  }

  public PdwhSnsPubAuthorRelation(Long id, Long pubId, Long psnId, String name, String relationemail,
      String organization) {
    super();
    this.id = id;
    this.pubId = pubId;
    this.psnId = psnId;
    this.name = name;
    this.relationemail = relationemail;
    this.organization = organization;
  }

  public PdwhSnsPubAuthorRelation() {
    super();
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_SNS_PA_RELATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "RELATION_EMAIL")
  public String getRelationemail() {
    return relationemail;
  }

  public void setRelationemail(String relationemail) {
    this.relationemail = relationemail;
  }

  @Column(name = "ORGANIZATION")
  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
