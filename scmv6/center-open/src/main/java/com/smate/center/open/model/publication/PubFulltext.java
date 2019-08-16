package com.smate.center.open.model.publication;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果全文.
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "PUB_FULLTEXT")
public class PubFulltext implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4032820531351996940L;

  private Long pubId;
  /** 全文附件Id，对应ArchiveFile的fileId. */
  private Long fulltextFileId;
  /** 全文所在节点. */
  private Integer fulltextNode;
  /** 全文附件后缀. */
  private String fulltextFileExt;
  /** 全文的哪个页码被转换成了图片. */
  private Integer fulltextImagePageIndex;
  /** 全文图片所在的路径. */
  private String fulltextImagePath;
  /** 全文下载权限. */
  private int permission;
  // 对应的成果分组id 分组为了更好的得到相关成果
  private Long groupId;

  public PubFulltext() {}

  public PubFulltext(Long pubId) {
    this.pubId = pubId;
  }

  public PubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt,
      Integer fulltextImagePageIndex, String fulltextImagePath) {
    this.pubId = pubId;
    this.fulltextFileId = fulltextFileId;
    this.fulltextNode = fulltextNode;
    this.fulltextFileExt = fulltextFileExt;
    this.fulltextImagePageIndex = fulltextImagePageIndex;
    this.fulltextImagePath = fulltextImagePath;
  }

  public PubFulltext(Long pubId, String fulltextImagePath) {
    this.pubId = pubId;
    this.fulltextImagePath = fulltextImagePath;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "FULLTEXT_FILE_ID")
  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  @Column(name = "FULLTEXT_NODE")
  public Integer getFulltextNode() {
    return fulltextNode;
  }

  public void setFulltextNode(Integer fulltextNode) {
    this.fulltextNode = fulltextNode;
  }

  @Column(name = "FULLTEXT_FILE_EXT")
  public String getFulltextFileExt() {
    return fulltextFileExt;
  }

  public void setFulltextFileExt(String fulltextFileExt) {
    this.fulltextFileExt = fulltextFileExt;
  }

  @Column(name = "FULLTEXT_IMAGE_PAGE_INDEX")
  public Integer getFulltextImagePageIndex() {
    return fulltextImagePageIndex;
  }

  public void setFulltextImagePageIndex(Integer fulltextImagePageIndex) {
    this.fulltextImagePageIndex = fulltextImagePageIndex;
  }

  @Column(name = "FULLTEXT_IMAGE_PATH")
  public String getFulltextImagePath() {
    return fulltextImagePath;
  }

  public void setFulltextImagePath(String fulltextImagePath) {
    this.fulltextImagePath = fulltextImagePath;
  }

  @Column(name = "PERMISSION")
  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

}
