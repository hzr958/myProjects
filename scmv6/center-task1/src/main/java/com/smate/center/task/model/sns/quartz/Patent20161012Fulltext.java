package com.smate.center.task.model.sns.quartz;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * patent专利全文对照
 */

@Entity
@Table(name = "PATENT_TMP_20161012_FULLTEXT")
public class Patent20161012Fulltext implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 3532820766504456440L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "FULLTEXT_FILEID")
  private Long fullTextFileId;

  @Column(name = "FULLTEXT_URL")
  private String fullTextUrl;


  public Patent20161012Fulltext() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getFullTextFileId() {
    return fullTextFileId;
  }

  public void setFullTextFileId(Long fullTextFileId) {
    this.fullTextFileId = fullTextFileId;
  }

  public String getFullTextUrl() {
    return fullTextUrl;
  }

  public void setFullTextUrl(String fullTextUrl) {
    this.fullTextUrl = fullTextUrl;
  }

}
