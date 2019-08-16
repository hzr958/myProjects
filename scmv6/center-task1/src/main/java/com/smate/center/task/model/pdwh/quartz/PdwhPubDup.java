package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果查重表
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_PUB_DUP")
public class PdwhPubDup implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8990849692385040029L;
  private Long pubId;
  private Integer pubType;
  private Integer pubYear;
  private Long unionHashKey;// (title与pubyear,pubType生成的hash值, 数据表中建立唯一索引)
  private String doi;
  private Long doiHash;
  private String cnkiDoi;
  private Long cnkiDoiHash;
  private String isiSourceId;
  private Long isiSourceIdHash;
  private String eiSourceId;
  private Long eiSourceIdHash;
  private Long zhTitleHash;
  private Long enTitleHash;
  private Long titleHash;// (zh_title | en_title 组合hash)
  private Long patentNoHash;
  private Long patentOpenNoHash;

  public PdwhPubDup() {
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

  @Column(name = "PUB_TYPE")
  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  @Column(name = "UNION_HASH_KEY")
  public Long getUnionHashKey() {
    return unionHashKey;
  }

  public void setUnionHashKey(Long unionHashKey) {
    this.unionHashKey = unionHashKey;
  }

  @Column(name = "DOI")
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Column(name = "DOI_HASH")
  public Long getDoiHash() {
    return doiHash;
  }

  public void setDoiHash(Long doiHash) {
    this.doiHash = doiHash;
  }

  @Column(name = "CNKI_DOI")
  public String getCnkiDoi() {
    return cnkiDoi;
  }

  public void setCnkiDoi(String cnkiDoi) {
    this.cnkiDoi = cnkiDoi;
  }

  @Column(name = "CNKI_DOI_HASH")
  public Long getCnkiDoiHash() {
    return cnkiDoiHash;
  }

  public void setCnkiDoiHash(Long cnkiDoiHash) {
    this.cnkiDoiHash = cnkiDoiHash;
  }

  @Column(name = "ISI_SORUCE_ID")
  public String getIsiSourceId() {
    return isiSourceId;
  }

  public void setIsiSourceId(String isiSourceId) {
    this.isiSourceId = isiSourceId;
  }

  @Column(name = "ISI_SORUCE_ID_HASH")
  public Long getIsiSourceIdHash() {
    return isiSourceIdHash;
  }

  public void setIsiSourceIdHash(Long isiSourceIdHash) {
    this.isiSourceIdHash = isiSourceIdHash;
  }

  @Column(name = "EI_SOURCE_ID")
  public String getEiSourceId() {
    return eiSourceId;
  }

  public void setEiSourceId(String eiSourceId) {
    this.eiSourceId = eiSourceId;
  }

  @Column(name = "EI_SOURCE_ID_HASH")
  public Long getEiSourceIdHash() {
    return eiSourceIdHash;
  }

  public void setEiSourceIdHash(Long eiSourceIdHash) {
    this.eiSourceIdHash = eiSourceIdHash;
  }

  @Column(name = "ZH_TITLE_HASH")
  public Long getZhTitleHash() {
    return zhTitleHash;
  }

  public void setZhTitleHash(Long zhTitleHash) {
    this.zhTitleHash = zhTitleHash;
  }

  @Column(name = "EN_TITLE_HASH")
  public Long getEnTitleHash() {
    return enTitleHash;
  }

  public void setEnTitleHash(Long enTitleHash) {
    this.enTitleHash = enTitleHash;
  }

  @Column(name = "TITLE_HASH")
  public Long getTitleHash() {
    return titleHash;
  }

  public void setTitleHash(Long titleHash) {
    this.titleHash = titleHash;
  }

  @Column(name = "PATENT_NO_HASH")
  public Long getPatentNoHash() {
    return patentNoHash;
  }

  public void setPatentNoHash(Long patentNoHash) {
    this.patentNoHash = patentNoHash;
  }

  @Column(name = "PATENT_OPEN_NO_HASH")
  public Long getPatentOpenNoHash() {
    return patentOpenNoHash;
  }

  public void setPatentOpenNoHash(Long patentOpenNoHash) {
    this.patentOpenNoHash = patentOpenNoHash;
  }

  @Column(name = "PUB_YEAR")
  public Integer getPubYear() {
    return pubYear;
  }

  public void setPubYear(Integer pubYear) {
    this.pubYear = pubYear;
  }

}
