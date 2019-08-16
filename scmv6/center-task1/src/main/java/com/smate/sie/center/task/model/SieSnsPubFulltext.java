package com.smate.sie.center.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yxy 成果全文链接或全文附件.
 */
@Entity
@Table(name = "SNS_PUB_FULLTEXT")
public class SieSnsPubFulltext implements java.io.Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4455045589698428059L;

  private Long pubId;
  /** 全文附件Id，对应ArchiveFile的fileId. */
  private Long fulltextFileId;
  /** 全文图片所在的路径. */
  private String fulltextImagePath;

  public SieSnsPubFulltext() {
    super();
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

  @Column(name = "FULLTEXT_IMAGE_PATH")
  public String getFulltextImagePath() {
    return fulltextImagePath;
  }

  public void setFulltextImagePath(String fulltextImagePath) {
    this.fulltextImagePath = fulltextImagePath;
  }

}
