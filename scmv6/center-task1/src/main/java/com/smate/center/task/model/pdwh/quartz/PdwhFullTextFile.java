package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 全文文件
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "PDWH_FULLTEXT_FILE")
public class PdwhFullTextFile implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -7892838654837255371L;
  private Long pubId;// 成果id
  private Long fileId;// 文件id
  private Date createDate;// 创建时间

  public PdwhFullTextFile() {
    super();
    // TODO Auto-generated constructor stub
  }


  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Id
  @Column(name = "FILE_ID")
  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
