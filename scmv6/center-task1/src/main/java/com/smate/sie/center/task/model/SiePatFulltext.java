package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目全文.
 * 
 * @author 叶星源
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PAT_FULLTEXT")
public class SiePatFulltext implements Serializable {

  private Long patId;
  /** 全文附件Id，对应ArchiveFiles的fileId. */
  private Long fulltextFileId;
  /** 全文附件后缀. */
  private String fulltextFileExt;
  /** 全文的哪个页码被转换成了图片. */
  private Integer fulltextImagePageIndex;
  /** 全文图片所在的路径. */
  private String fulltextImagePath;

  public SiePatFulltext() {}

  public SiePatFulltext(Long patId) {
    this.patId = patId;
  }

  public SiePatFulltext(Long patId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt,
      Integer fulltextImagePageIndex, String fulltextImagePath) {
    this.patId = patId;
    this.fulltextFileId = fulltextFileId;
    this.fulltextFileExt = fulltextFileExt;
    this.fulltextImagePageIndex = fulltextImagePageIndex;
    this.fulltextImagePath = fulltextImagePath;
  }

  @Id
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  @Column(name = "FULLTEXT_FILE_ID")
  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
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

}
