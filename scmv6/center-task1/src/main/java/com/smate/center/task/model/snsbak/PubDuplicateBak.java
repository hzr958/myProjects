package com.smate.center.task.model.snsbak;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人库成果查重备份表
 * 
 * @author YJ
 *
 *         2019年3月28日
 */
@Entity
@Table(name = "V_PUB_DUPLICATE_BAK")
public class PubDuplicateBak implements Serializable {

  private static final long serialVersionUID = -6678982356051330615L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果id，主键

  @Column(name = "HASH_TP")
  private String hashTP; // (中文|英文) + 类别生成的hash值

  @Column(name = "HASH_TITLE")
  private String hashTitle; // title生成的hash值

  @Column(name = "HASH_TPP")
  private String hashTPP; // (中文|英文) + 年份 + 类别生成的hash值

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

  @Column(name = "DETAILS_HASH")
  private String detailsHash; // 成果json数据hash，根据所有数据计算hash值，可以验证成果是否被编辑过

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PubDuplicateBak() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getHashTPP() {
    return hashTPP;
  }

  public void setHashTPP(String hashTPP) {
    this.hashTPP = hashTPP;
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

  public String getDetailsHash() {
    return detailsHash;
  }

  public void setDetailsHash(String detailsHash) {
    this.detailsHash = detailsHash;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  @Override
  public String toString() {
    return "PubDuplicateBak{" + "pubId='" + pubId + '\'' + ", hashTP='" + hashTP + '\'' + ", hashTTP='" + hashTPP + '\''
        + ", hashDoi='" + hashDoi + '\'' + ", hashCnkiDoi='" + hashCnkiDoi + '\'' + ", hashIsiSourceId='"
        + hashIsiSourceId + '\'' + ", hashEiSourceId='" + hashEiSourceId + '\'' + ", hashApplicationNo='"
        + hashApplicationNo + '\'' + ", hashPublicationOpenNo='" + hashPublicationOpenNo + '\'' + '}';
  }

}
