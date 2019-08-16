package com.smate.center.task.model.pdwh.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PDWH_PUB_CLEAN_DOI")
// 基准库成果清理后的doihash信息表（去除不能作为文件名的字符 空格）（不会实时更新）
// 用于历史doi命名的全文匹配处理
public class PdwhPubCleanDOI {
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "CLEAN_DOI")
  private String cleanDoi;
  @Column(name = "CLEAN_DOI_HASH")
  private Long cleanDoiHash;

  public PdwhPubCleanDOI() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getCleanDoi() {
    return cleanDoi;
  }

  public void setCleanDoi(String cleanDoi) {
    this.cleanDoi = cleanDoi;
  }

  public Long getCleanDoiHash() {
    return cleanDoiHash;
  }

  public void setCleanDoiHash(Long cleanDoiHash) {
    this.cleanDoiHash = cleanDoiHash;
  }

}
