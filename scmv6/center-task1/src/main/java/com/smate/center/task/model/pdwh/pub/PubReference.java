package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果参考文献
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "PUB_REFERENCE")
public class PubReference implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5203340296847661205L;
  private Long id;
  private Long citedPdwhPubId;
  private String key;
  private String doi;
  private String issue;
  private String firstPage;
  private String volume;
  private String edition;
  private String component;
  private String author;
  private String year;
  private String unstructured;
  private String journalTitle;
  private String articleTitle;
  private String seriesTitle;
  private String volumeTitle;
  private String issn;
  private String isbn;
  private String standardDesignator;
  private String standardsBody;
  private String doiHash;
  private String journalTitleHash;
  private String articleTitleHash;
  private String seriesTitleHash;
  private String volumeTitleHash;
  private Long pdwhPubId;

  public PubReference() {
    super();
  }

  public PubReference(Long citedPdwhPubId, String key) {
    super();
    this.citedPdwhPubId = citedPdwhPubId;
    this.key = key;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_REFERENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "CITED_PDWH_PUB_ID")
  public Long getCitedPdwhPubId() {
    return citedPdwhPubId;
  }

  public void setCitedPdwhPubId(Long citedPdwhPubId) {
    this.citedPdwhPubId = citedPdwhPubId;
  }

  @Column(name = "KEY")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Column(name = "ISSUE")
  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  @Column(name = "FIRST_PAGE")
  public String getFirstPage() {
    return firstPage;
  }

  public void setFirstPage(String firstPage) {
    this.firstPage = firstPage;
  }

  @Column(name = "VOLUME")
  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  @Column(name = "EDITION")
  public String getEdition() {
    return edition;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  @Column(name = "COMPONENT")
  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  @Column(name = "AUTHOR")
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Column(name = "YEAR")
  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  @Column(name = "UNSTRUCTURED")
  public String getUnstructured() {
    return unstructured;
  }

  public void setUnstructured(String unstructured) {
    this.unstructured = unstructured;
  }

  @Column(name = "JOURNAL_TITLE")
  public String getJournalTitle() {
    return journalTitle;
  }

  public void setJournalTitle(String journalTitle) {
    this.journalTitle = journalTitle;
  }

  @Column(name = "ARTICLE_TITLE")
  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }

  @Column(name = "SERIES_TITLE")
  public String getSeriesTitle() {
    return seriesTitle;
  }

  public void setSeriesTitle(String seriesTitle) {
    this.seriesTitle = seriesTitle;
  }

  @Column(name = "VOLUME_TITLE")
  public String getVolumeTitle() {
    return volumeTitle;
  }

  public void setVolumeTitle(String volumeTitle) {
    this.volumeTitle = volumeTitle;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  @Column(name = "ISBN")
  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  @Column(name = "STANDARD_DESIGNATOR")
  public String getStandardDesignator() {
    return standardDesignator;
  }

  public void setStandardDesignator(String standardDesignator) {
    this.standardDesignator = standardDesignator;
  }

  @Column(name = "STANDARDS_BODY")
  public String getStandardsBody() {
    return standardsBody;
  }

  public void setStandardsBody(String standardsBody) {
    this.standardsBody = standardsBody;
  }

  @Column(name = "DOI_HASH")
  public String getDoiHash() {
    return doiHash;
  }

  public void setDoiHash(String doiHash) {
    this.doiHash = doiHash;
  }

  @Column(name = "JOURNAL_TITLE_HASH")
  public String getJournalTitleHash() {
    return journalTitleHash;
  }

  public void setJournalTitleHash(String journalTitleHash) {
    this.journalTitleHash = journalTitleHash;
  }

  @Column(name = "ARTICLE_TITLE_HASH")
  public String getArticleTitleHash() {
    return articleTitleHash;
  }

  public void setArticleTitleHash(String articleTitleHash) {
    this.articleTitleHash = articleTitleHash;
  }

  @Column(name = "SERIES_TITLE_HASH")
  public String getSeriesTitleHash() {
    return seriesTitleHash;
  }

  public void setSeriesTitleHash(String seriesTitleHash) {
    this.seriesTitleHash = seriesTitleHash;
  }

  @Column(name = "VOLUME_TITLE_HASH")
  public String getVolumeTitleHash() {
    return volumeTitleHash;
  }

  public void setVolumeTitleHash(String volumeTitleHash) {
    this.volumeTitleHash = volumeTitleHash;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

}
