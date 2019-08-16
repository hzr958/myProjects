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
@Table(name = "PRJ_FULLTEXT")
public class SiePrjFulltext implements Serializable {

  private Long prjId;
  /** 全文附件Id，对应ArchiveFiles的fileId. */
  private Long fulltextFileId;
  /** 全文附件后缀. */
  private String fulltextFileExt;
  /** 全文的哪个页码被转换成了图片. */
  private Integer fulltextImagePageIndex;
  /** 全文图片所在的路径. */
  private String fulltextImagePath;

  public SiePrjFulltext() {}

  public SiePrjFulltext(Long prjId) {
    this.prjId = prjId;
  }

  public SiePrjFulltext(Long prjId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt,
      Integer fulltextImagePageIndex, String fulltextImagePath) {
    this.prjId = prjId;
    this.fulltextFileId = fulltextFileId;
    this.fulltextFileExt = fulltextFileExt;
    this.fulltextImagePageIndex = fulltextImagePageIndex;
    this.fulltextImagePath = fulltextImagePath;
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
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
