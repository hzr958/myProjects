package com.smate.center.task.v8pub.sns.po;

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
 * 个人库成果引用次数记录实体
 * 
 * @author YJ
 *
 *         2018年7月12日
 */
@Entity
@Table(name = "V_PUB_CITATIONS")
public class PubCitationsPO implements Serializable {

  private static final long serialVersionUID = 1674560242945573596L;

  /**
   * 逻辑主键ID
   */
  @Id
  @SequenceGenerator(name = "SEQ_PUB_CITATIONS_ID", sequenceName = "SEQ_PUB_CITATIONS_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_CITATIONS_ID")
  @Column(name = "ID")
  private Long id;

  /**
   * 个人库成果id
   */
  @Column(name = "PUB_ID")
  private Long pubId;

  /**
   * 引用次数
   */
  @Column(name = "CITATIONS")
  private Integer citations;

  /**
   * 引用更新时间
   */
  @Column(name = "GMT_MODIFIED")
  private Date gmtModified;

  /**
   * 引用更新类型 手动更新1，后台更新0
   */
  @Column(name = "TYPE")
  private Integer citedType;

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

  public int getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public int getCitedType() {
    return citedType;
  }

  public void setCitedType(Integer citedType) {
    this.citedType = citedType;
  }

  @Override
  public String toString() {
    return "PubCitationsPO{" + "id='" + id + '\'' + ", pubId='" + pubId + '\'' + ", citations='" + citations + '\''
        + ", gmtModified='" + gmtModified + '\'' + ", citedType='" + citedType + '\'' + '}';
  }
}
