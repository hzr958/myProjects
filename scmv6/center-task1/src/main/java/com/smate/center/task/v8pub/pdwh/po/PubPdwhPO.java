package com.smate.center.task.v8pub.pdwh.po;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.smate.center.task.v8pub.po.PubPO;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum.PubPdwhStatusEnumConverter;


/**
 * @author houchuanjie
 * @date 2018/06/01 16:47
 */
@Entity
@Table(name = "V_PUB_PDWH")
public class PubPdwhPO extends PubPO {

  private static final long serialVersionUID = -5253527469864008820L;

  public PubPdwhPO() {
    // TODO Auto-generated constructor stub
  }

  public PubPdwhPO(Long pubId, String title) {
    this.pubId = pubId;
    this.title = title;
  }

  public PubPdwhPO(Long pubId, Integer publishYear, Integer citations) {
    this.pubId = pubId;
    this.publishYear = publishYear;
    this.citations = citations;
  }

  /**
   * 成果id
   */
  @Id
  // @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_PDWH_ID", allocationSize = 1)
  // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "PUB_ID")
  protected Long pubId;

  /**
   * 基准库成果状态
   */
  @Convert(converter = PubPdwhStatusEnumConverter.class)
  @Column(name = "STATUS")
  private PubPdwhStatusEnum status;

  public PubPdwhStatusEnum getStatus() {
    return status;
  }


  public void setStatus(PubPdwhStatusEnum status) {
    this.status = status;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PubPdwhPO)) {
      return false;
    }
    PubPdwhPO that = (PubPdwhPO) o;
    return pubId.equals(that.pubId);
  }

  @Override
  public int hashCode() {
    return 17 * 1 + Objects.hashCode(pubId);
  }

  @Override
  public String toString() {
    return "PubPdwhPO{" + "pubId=" + pubId + ", publishYear=" + publishYear + ", countryId=" + countryId + ", title='"
        + title + '\'' + ", authorNames='" + authorNames + '\'' + ", briefDesc='" + briefDesc + '\'' + ", createPsnId="
        + createPsnId + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + '}';
  }
}
