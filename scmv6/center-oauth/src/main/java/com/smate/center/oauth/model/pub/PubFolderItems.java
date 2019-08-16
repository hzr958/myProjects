package com.smate.center.oauth.model.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * 文件夹-成果关系表.
 * 
 * @author tj
 * 
 */
@Entity
@Table(name = "PUB_FOLDER_ITEMS")
public class PubFolderItems implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5325701026723795168L;

  private Long id;
  private Long folderId;
  private Long pubId;
  private Date createDate;
  private PubFolder pubFolder;
  private Publication publication;

  public PubFolderItems() {
    super();
  }

  public PubFolderItems(Long folderId, Long pubId, Date createDate) {
    super();
    this.folderId = folderId;
    this.pubId = pubId;
    this.createDate = createDate;
  }

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_FOLDER_ITEMS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "FOLDER_ID")
  public Long getFolderId() {
    return folderId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setFolderId(Long folderId) {
    this.folderId = folderId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @JsonIgnore
  // 多对一定义，cascade操作避免定义CascadeType.REMOVE
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  // 关联定义
  @JoinColumn(name = "FOLDER_ID", referencedColumnName = "FOLDER_ID", insertable = false, updatable = false)
  @OrderBy("id")
  // 集合中对象id的缓存.
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public PubFolder getPubFolder() {
    return pubFolder;
  }

  @JsonIgnore
  // 多对一定义，cascade操作避免定义CascadeType.REMOVE
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  // 关联定义
  @JoinColumn(name = "PUB_ID", referencedColumnName = "PUB_ID", insertable = false, updatable = false)
  @OrderBy("id")
  // 集合中对象id的缓存. insert="false" update="false"
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  public Publication getPublication() {
    return publication;
  }

  public void setPubFolder(PubFolder pubFolder) {
    this.pubFolder = pubFolder;
  }

  public void setPublication(Publication publication) {
    this.publication = publication;
  }

}
