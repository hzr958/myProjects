package com.smate.web.v8pub.po.sns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果修改记录
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Entity
@Table(name = "V_PUB_MODIFY")
public class PubModifyPO implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 6311336934850957008L;

  @Id
  @SequenceGenerator(name = "SEQ_PUB_MODIFY_ID", sequenceName = "SEQ_PUB_MODIFY_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_MODIFY_ID")
  @Column(name = "ID")

  private Long id; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "MODIFY_PSN_ID")
  private Long modifyPsnId; // 修改人员


  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建/修改时间

  @Column(name = "MODIFY_DESC")
  private String modifyDesc; // 修改描述 500 字符

  public PubModifyPO() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getModifyPsnId() {
    return modifyPsnId;
  }

  public void setModifyPsnId(Long modifyPsnId) {
    this.modifyPsnId = modifyPsnId;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public String getModifyDesc() {
    return modifyDesc;
  }

  public void setModifyDesc(String modifyDesc) {
    this.modifyDesc = modifyDesc;
  }



}
