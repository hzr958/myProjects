package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果全文转换图片刷新实体.
 * 
 * @author pwl
 * 
 */
@Entity
@Table(name = "PUB_FULLTEXT_IMAGE_REFRESH")
public class PubFulltextImageRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3877665317053467292L;

  private Long pubId;
  private Long fulltextFileId;
  private Integer fulltextNode;
  private String fulltextFileExt;
  private Integer status = 0;
  private String errorMsg;

  public PubFulltextImageRefresh() {}

  public PubFulltextImageRefresh(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt) {
    this.pubId = pubId;
    this.fulltextFileId = fulltextFileId;
    this.fulltextNode = fulltextNode;
    this.fulltextFileExt = fulltextFileExt;
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

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

}
