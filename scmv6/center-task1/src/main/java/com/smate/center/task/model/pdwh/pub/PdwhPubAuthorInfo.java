package com.smate.center.task.model.pdwh.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PDWH_PUB_AUTHOR_INFO")
public class PdwhPubAuthorInfo {
  private Long Id;
  private Long pubId;
  private Long assignInsId;// 成果指派的机构InsId
  private String authorName;
  private String authorNameSpec;// 清理后的人名
  private String organization;
  private String organizationSpec;// 清理后的单位
  private String email;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_AUTHOR_INFO", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "AUTHOR_NAME")
  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  @Column(name = "AUTHOR_NAME_SPEC")
  public String getAuthorNameSpec() {
    return authorNameSpec;
  }

  public void setAuthorNameSpec(String authorNameSpec) {
    this.authorNameSpec = authorNameSpec;
  }

  @Column(name = "ORGANIZATION")
  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "ORGANIZATION_SPEC")
  public String getOrganizationSpec() {
    return organizationSpec;
  }

  public void setOrganizationSpec(String organizationSpec) {
    this.organizationSpec = organizationSpec;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "ASSIGN_INSID")
  public Long getAssignInsId() {
    return assignInsId;
  }

  public void setAssignInsId(Long assignInsId) {
    this.assignInsId = assignInsId;
  }

  public PdwhPubAuthorInfo(Long pubId, Long assignInsId, String authorName, String authorNameSpec, String organization,
      String organizationSpec) {
    super();
    this.pubId = pubId;
    this.assignInsId = assignInsId;
    this.authorName = authorName;
    this.authorNameSpec = authorNameSpec;
    this.organization = organization;
    this.organizationSpec = organizationSpec;
  }

  public PdwhPubAuthorInfo() {
    super();
  }

  public PdwhPubAuthorInfo(Long pubId, String authorName, String authorNameSpec, String organization, String email) {
    super();
    this.pubId = pubId;
    this.authorName = authorName;
    this.authorNameSpec = authorNameSpec;
    this.organization = organization;
    this.email = email;
  }

}
