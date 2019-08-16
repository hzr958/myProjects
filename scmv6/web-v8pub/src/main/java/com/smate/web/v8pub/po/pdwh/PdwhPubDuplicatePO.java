package com.smate.web.v8pub.po.pdwh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果查重记录表
 * 
 * @author YJ 2018年5月31号
 */

@Entity
@Table(name = "V_PDWH_DUPLICATE")
public class PdwhPubDuplicatePO implements Serializable {

  private static final long serialVersionUID = -2292144189774997480L;

  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库成果id，主键

  @Column(name = "HASH_TITLE")
  private String hashTitle; // title生成的hash值

  @Column(name = "HASH_TP")
  private String hashTP; // (Title + pubType) 生成的hash值

  @Column(name = "HASH_TPP")
  private String hashTPP; // (Title + pubYear + pubType) 生成的hash值, 数据表中建立唯一索引

  @Column(name = "HASH_DOI")
  private String hashDoi; // doi的hash

  @Column(name = "HASH_CLEAN_DOI")
  private String hashCleanDoi; // doi去除标点符号的hash

  @Column(name = "HASH_CNKI_DOI")
  private String hashCnkiDoi; // cnki的doi的hash

  @Column(name = "HASH_CLEAN_CNKI_DOI")
  private String hashCleanCnkiDoi; // cnki的doi去除标点符号的hash

  @Column(name = "HASH_ISI_SOURCE_ID")
  private String hashIsiSourceId; // isi的source_id的hash

  @Column(name = "HASH_EI_SOURCE_ID")
  private String hashEiSourceId; // ei的source_id的hash

  @Column(name = "HASH_APPLICATION_NO")
  private String hashApplicationNo; // 专利申请号hash

  @Column(name = "HASH_PUBLICATION_OPEN_NO")
  private String hashPublicationOpenNo; // 专利公开（公告）号hash

  @Column(name = "HASH_STANDARD_NO")
  private String hashStandardNo; // 标准标准号hash

  @Column(name = "HASH_REGISTER_NO")
  private String hashRegisterNo; // 软件著作权的登记号hash

  public PdwhPubDuplicatePO() {
    super();
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getHashTP() {
    return hashTP;
  }

  public void setHashTP(String hashTP) {
    this.hashTP = hashTP;
  }

  public String getHashTitle() {
    return hashTitle;
  }

  public void setHashTitle(String hashTitle) {
    this.hashTitle = hashTitle;
  }

  public String getHashTPP() {
    return hashTPP;
  }

  public void setHashTPP(String hashTPP) {
    this.hashTPP = hashTPP;
  }

  public String getHashDoi() {
    return hashDoi;
  }

  public void setHashDoi(String hashDoi) {
    this.hashDoi = hashDoi;
  }

  public String getHashCnkiDoi() {
    return hashCnkiDoi;
  }

  public void setHashCnkiDoi(String hashCnkiDoi) {
    this.hashCnkiDoi = hashCnkiDoi;
  }

  public String getHashIsiSourceId() {
    return hashIsiSourceId;
  }

  public void setHashIsiSourceId(String hashIsiSourceId) {
    this.hashIsiSourceId = hashIsiSourceId;
  }

  public String getHashEiSourceId() {
    return hashEiSourceId;
  }

  public void setHashEiSourceId(String hashEiSourceId) {
    this.hashEiSourceId = hashEiSourceId;
  }

  public String getHashApplicationNo() {
    return hashApplicationNo;
  }

  public void setHashApplicationNo(String hashApplicationNo) {
    this.hashApplicationNo = hashApplicationNo;
  }

  public String getHashPublicationOpenNo() {
    return hashPublicationOpenNo;
  }

  public void setHashPublicationOpenNo(String hashPublicationOpenNo) {
    this.hashPublicationOpenNo = hashPublicationOpenNo;
  }

  public String getHashCleanDoi() {
    return hashCleanDoi;
  }

  public void setHashCleanDoi(String hashCleanDoi) {
    this.hashCleanDoi = hashCleanDoi;
  }

  public String getHashCleanCnkiDoi() {
    return hashCleanCnkiDoi;
  }

  public void setHashCleanCnkiDoi(String hashCleanCnkiDoi) {
    this.hashCleanCnkiDoi = hashCleanCnkiDoi;
  }

  public String getHashStandardNo() {
    return hashStandardNo;
  }

  public void setHashStandardNo(String hashStandardNo) {
    this.hashStandardNo = hashStandardNo;
  }

  public String getHashRegisterNo() {
    return hashRegisterNo;
  }

  public void setHashRegisterNo(String hashRegisterNo) {
    this.hashRegisterNo = hashRegisterNo;
  }

  @Override
  public String toString() {
    return "PubDuplicatePO{" + "pdwhPubId='" + pdwhPubId + '\'' + ", hashTP='" + hashTP + '\'' + ", hashTTP='" + hashTPP
        + '\'' + ", hashDoi='" + hashDoi + '\'' + ", hashCnkiDoi='" + hashCnkiDoi + '\'' + ", hashIsiSourceId='"
        + hashIsiSourceId + '\'' + ", hashEiSourceId='" + hashEiSourceId + '\'' + ", hashApplicationNo='"
        + hashApplicationNo + '\'' + ", hashPublicationOpenNo='" + hashPublicationOpenNo + '\'' + '}';
  }
}
