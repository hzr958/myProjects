package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * rol成果xml成果id实体
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "ROL_PUB_XML_ABS_ID_ZK")
public class RolPubXmlPubId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8196265323621978740L;
  @Id
  @Column(name = "pub_id")
  private Long pubId;
  @Column(name = "status")
  private Integer status; // 0:未处理，1:已处理，并更新，２：已处理，无须更新
  @Column(name = "create_Date")
  private Date createDate;

  public Long getPubId() {
    return pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
