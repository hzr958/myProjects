package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * pub-ins同步记录.
 * 
 */
@Entity
@Table(name = "PUB_INS_SYNC")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class PubInsSync implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5416855427546757559L;

  /*
   * INS_ID NUMBER(18) N SNS_PUB_ID NUMBER(18) N PSN_ID NUMBER(18) N CREATE_AT DATE N
   */

  private PubInsSyncKey id;
  private Long psnId;
  private Date createAt;

  /*
   * FROM_NODE_ID NUMBER(6) Y PUBLISH_YEAR NUMBER(4) Y PUB_TYPE NUMBER(4) Y TITLE VARCHAR2(500 CHAR) Y
   */
  private Integer fromNodeId;
  private Integer publishYear;
  private Integer pubTypeId;
  private String title;
  private Integer isSubmited;

  public PubInsSync() {
    super();
    this.createAt = new Date();
    this.isSubmited = 0;
  }

  public PubInsSync(Long insId, Long snsPubId, Long psnId) {
    super();
    this.id = new PubInsSyncKey(snsPubId, insId);
    this.psnId = psnId;
    this.createAt = new Date();
    this.isSubmited = 0;
  }

  @EmbeddedId
  public PubInsSyncKey getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "CREATE_AT")
  public Date getCreateAt() {
    return createAt;
  }

  public void setId(PubInsSyncKey id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  @Column(name = "FROM_NODE_ID")
  public Integer getFromNodeId() {
    return fromNodeId;
  }

  @Column(name = "PUBLISH_YEAR")
  public Integer getPublishYear() {
    return publishYear;
  }

  @Column(name = "PUB_TYPE")
  public Integer getPubTypeId() {
    return pubTypeId;
  }

  @Column(name = "TITLE")
  public String getTitle() {
    return title;
  }

  public void setFromNodeId(Integer fromNodeId) {
    this.fromNodeId = fromNodeId;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public void setPubTypeId(Integer pubTypeId) {
    this.pubTypeId = pubTypeId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "IS_SUBMITED")
  public Integer getIsSubmited() {
    return isSubmited;
  }

  public void setIsSubmited(Integer isSubmited) {
    this.isSubmited = isSubmited;
  }

}
