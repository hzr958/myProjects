package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * cwli基础期刊出版商.
 */
@Entity
@Table(name = "BASE_JOURNAL_PUBLISHER")
public class BaseJournalPublisher implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 6326842952506667870L;

  private Long publisherId;

  private BaseJournal journal;

  private Long dbId;
  // 出版商
  private String publisherName;
  // 出版商地址
  private String publisherAddress;
  // 出版商url
  private String publisherUrl;
  private String dbCode;

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

  @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "JNL_ID", insertable = true, updatable = true)
  public BaseJournal getJournal() {
    return journal;
  }

  public void setJournal(BaseJournal journal) {
    this.journal = journal;
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

  @Transient
  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
