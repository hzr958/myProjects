package com.smate.center.batch.model.sns.pub;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * 成果、收藏夹关系表.
 * 
 * @author tj
 * 
 */
@Entity
@Table(name = "PUB_FOLDER")
public class PubFolder implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5864875884757555263L;

  private Long id;
  private int articleType;
  private String name;
  private String floderDesc;
  private int enabled;

  private Long psnId;

  private Date createDate;
  private Set<PubFolderItems> pubFolderItems;
  private Integer count;

  public PubFolder() {
    super();
  }

  public PubFolder(Long id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

  @Id
  @Column(name = "FOLDER_ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "OWNER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "ARTICLE_TYPE")
  public int getArticleType() {
    return articleType;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  @Column(name = "FOLDER_DESC")
  public String getFloderDesc() {
    return floderDesc;
  }

  @Column(name = "ENABLED")
  public int getEnabled() {
    return enabled;
  }

  public void setArticleType(int articleType) {
    this.articleType = articleType;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFloderDesc(String floderDesc) {
    this.floderDesc = floderDesc;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }


  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "folderId")
  public Set<PubFolderItems> getPubFolderItems() {
    return pubFolderItems;
  }

  public void setPubFolderItems(Set<PubFolderItems> pubFolderItems) {
    this.pubFolderItems = pubFolderItems;
  }

  @Transient
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
