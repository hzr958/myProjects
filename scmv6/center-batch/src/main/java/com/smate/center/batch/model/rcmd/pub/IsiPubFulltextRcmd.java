package com.smate.center.batch.model.rcmd.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ISI成果全文推荐表. rcmd
 * 
 * @author tsz
 * 
 */
@Entity
@Table(name = "ISI_PUB_FULLTEXT_RCMD")
public class IsiPubFulltextRcmd implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4335846730195937083L;

  private Long id;
  private Long pubId; // 基准库成果 publication_all id
  private Long fulltextFileId;
  /** 匹配推荐人员：0等待匹配，1已匹配. */
  private Integer status;

  public IsiPubFulltextRcmd() {}

  public IsiPubFulltextRcmd(Long pubId, Long fulltextFileId) {
    this.pubId = pubId;
    this.fulltextFileId = fulltextFileId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ISI_PUB_FULLTEXT_RCMD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "FULLTEXT_FILE_ID")
  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
