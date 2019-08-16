package com.smate.center.open.model.folder;



import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.string.ServiceUtil;



/**
 * 成果、收藏夹关系表.
 * 
 * @author liqinghua
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
  private String escapeName;

  private Long psnId;

  private Date createDate;
  private String des3Id;
  private Set<PubFolderItems> pubFolderItems;
  private Integer count;

  @Id
  @Column(name = "FOLDER_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_FOLDER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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

  @Transient
  public String getDes3Id() {

    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
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
  public String getEscapeName() {
    if (this.name != null)
      escapeName = HtmlUtils.toHtml(this.name);
    return escapeName;
  }

  public void setEscapeName(String escapeName) {
    this.escapeName = escapeName;
  }

  @Transient
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
