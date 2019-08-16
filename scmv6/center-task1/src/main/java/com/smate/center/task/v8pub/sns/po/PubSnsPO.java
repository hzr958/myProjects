package com.smate.center.task.v8pub.sns.po;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.smate.center.task.v8pub.enums.PubSnsStatusEnum;
import com.smate.center.task.v8pub.enums.PubSnsStatusEnum.PubSnsStatusEnumConverter;
import com.smate.center.task.v8pub.po.PubPO;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum.PubSnsRecordFromEnumConverter;

/**
 * 个人成果基础信息
 * 
 * @author houchuanjie
 * @date 2018/06/01 15:55
 */
@Entity
@Table(name = "V_PUB_SNS")
public class PubSnsPO extends PubPO {

  private static final long serialVersionUID = -1491582107838535689L;

  /**
   * 成果id
   */
  @Id
  // @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_SNS_ID", allocationSize = 1)
  // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "PUB_ID")
  private Long pubId;
  /**
   * 记录来源
   */
  @Column(name = "RECORD_FROM")
  @Convert(converter = PubSnsRecordFromEnumConverter.class)
  private PubSnsRecordFromEnum recordFrom;

  /**
   * 成果状态
   */
  @Column(name = "STATUS")
  @Convert(converter = PubSnsStatusEnumConverter.class)
  private PubSnsStatusEnum status;

  /**
   * 最新修改版本号
   */
  @Column(name = "VERSION")
  private Long version;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }



  public PubSnsStatusEnum getStatus() {
    return status;
  }

  public void setStatus(PubSnsStatusEnum status) {
    this.status = status;
  }

  public PubSnsRecordFromEnum getRecordFrom() {
    return recordFrom;
  }

  public void setRecordFrom(PubSnsRecordFromEnum recordFrom) {
    this.recordFrom = recordFrom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PubSnsPO)) {
      return false;
    }
    PubSnsPO that = (PubSnsPO) o;
    return pubId.equals(that.pubId);
  }

  @Override
  public int hashCode() {
    return 29 * 1 + Objects.hashCode(pubId);
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  // @Override
  // public String toString() {
  // return "PubSnsPO{" + "pubId=" + pubId + ", publishDate='" + publishDate + '\'' + ",citations=" +
  // citations
  // + ", countryId=" + countryId + ", recordFrom=" + recordFrom
  // + ", title='" + title + '\'' + ", authorNames='" + authorNames + '\''
  // + ", briefDesc='" + briefDesc + '\'' + ", createPsnId=" + createPsnId + ", gmtCreate=" +
  // gmtCreate
  // + ", gmtModified=" + gmtModified + ", status=" + status + '}';
  // }

}
