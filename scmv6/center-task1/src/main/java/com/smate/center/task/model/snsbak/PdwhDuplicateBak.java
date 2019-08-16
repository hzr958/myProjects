package com.smate.center.task.model.snsbak;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果查重记录备份表
 * 
 * @author YJ
 *
 *         2019年3月28日
 */

@Entity
@Table(name = "V_PDWH_DUPLICATE_BAK")
public class PdwhDuplicateBak implements Serializable {

  private static final long serialVersionUID = -5841951897542830958L;

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

  @Column(name = "HASH_CNKI_DOI")
  private String hashCnkiDoi; // cnki的doi的hash

  @Column(name = "HASH_ISI_SOURCE_ID")
  private String hashIsiSourceId; // isi的source_id的hash

  @Column(name = "HASH_EI_SOURCE_ID")
  private String hashEiSourceId; // ei的source_id的hash

  @Column(name = "HASH_APPLICATION_NO")
  private String hashApplicationNo; // 专利申请号hash

  @Column(name = "HASH_PUBLICATION_OPEN_NO")
  private String hashPublicationOpenNo; // 专利公开（公告）号hash

  public PdwhDuplicateBak() {
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

  @Override
  public String toString() {
    return "PdwhDuplicateBak{" + "pdwhPubId='" + pdwhPubId + '\'' + ", hashTP='" + hashTP + '\'' + ", hashTTP='"
        + hashTPP + '\'' + ", hashDoi='" + hashDoi + '\'' + ", hashCnkiDoi='" + hashCnkiDoi + '\''
        + ", hashIsiSourceId='" + hashIsiSourceId + '\'' + ", hashEiSourceId='" + hashEiSourceId + '\''
        + ", hashApplicationNo='" + hashApplicationNo + '\'' + ", hashPublicationOpenNo='" + hashPublicationOpenNo
        + '\'' + '}';
  }

}
