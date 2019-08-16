package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * cwli基础期刊出版商.
 */
@Entity
@Table(name = "BASE_JOURNAL_PUBLISHER")
public class BaseJournalPublisher implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -5047957224985281380L;

  private Long publisherId;

  private Long jnlId;

  private Long dbId;
  // 出版商
  private String publisherName;
  // 出版商地址
  private String publisherAddress;
  // 出版商url
  private String publisherUrl;

  public BaseJournalPublisher() {
    super();
  }

  public BaseJournalPublisher(Long jnlId, Long dbId) {
    super();
    this.jnlId = jnlId;
    this.dbId = dbId;
  }

  @Id
  @Column(name = "PUBLISHER_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_BASE_JOU_PUBLISHER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Long publisherId) {
    this.publisherId = publisherId;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "DBID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "PUBLISHER_NAME")
  public String getPublisherName() {
    return publisherName;
  }

  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }

  @Column(name = "PUBLISHER_ADDRESS")
  public String getPublisherAddress() {
    return publisherAddress;
  }

  public void setPublisherAddress(String publisherAddress) {
    this.publisherAddress = publisherAddress;
  }

  @Column(name = "PUBLISHER_URL")
  public String getPublisherUrl() {
    return publisherUrl;
  }

  public void setPublisherUrl(String publisherUrl) {
    this.publisherUrl = publisherUrl;
  }

}
