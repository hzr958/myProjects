package com.smate.web.v8pub.po.sns;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果文件关联表
 *
 * @author aijiangbin
 * @create 2019-01-28 14:21
 **/

@Entity
@Table (name = "V_PUB_FILE_RELATION")
public class PubFileRelationPO {

  @Id
  @Column (name = "PUB_ID")
  private Long pubId; // 成果id，主键
  @Column (name = "FILE_ID")
  private Long fileId; // 文件id


  public PubFileRelationPO() {
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }
}
