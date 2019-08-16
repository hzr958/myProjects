package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 基准库成果作者和sns人员对应关系表MongoDB实体
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Document(collection = "PDWH_AUTHOR_SNS_PSN_RECORD")
@CompoundIndexes({
    @CompoundIndex(name = "UK_PDWH_AUTHOR_SNS_PSN_RE", def = "{'insId': 1, 'psnId': 1,'nameType': 1, 'pubId': 1}",
        unique = true),
    @CompoundIndex(name = "IDX_PDWH_PUB_AU_SNS_RE_PUB", def = "{'pubId': 1}"),
    @CompoundIndex(name = "IDX_PDWH_PUB_AU_SNS_RE_PSN", def = "{'psnId': 1}"),
    @CompoundIndex(name = "IDX_PDWH_PUB_AU_SNS_RE_INS", def = "{'insId': 1}"),
    @CompoundIndex(name = "IDX_PDWH_PUB_AU_SNS_RE_NT", def = "{'status': 1,'nameType': 1}")}

)
public class MongoPdwhPubAuthorSnsPsnRecord implements Serializable {

  private static final long serialVersionUID = 2684799670196538903L;
  /**
   * 
   */
  @Id
  @Indexed
  @Field("id")
  private Long id;// 主键id
  @Field("pubId")
  private Long pubId;// 成果id
  @Field("psnId")
  private Long psnId;// 人员id
  @Field("psnName")
  private String psnName;// sns人名
  @Field("insId")
  private Long insId;// 单位id
  @Field("insName")
  private String insName;// 单位名
  @Field("status")
  private Integer status;
  @Field("updateTime")
  private Date updateTime;
  @Field("nameType")
  private Integer nameType;// 匹配上的姓名类型,对应person_pm_name表

  /**
   * 状态 0 成果匹配不到sns人员，<br>
   * 状态1.表示可信度低(仅人名匹配上)<br>
   * 2.表示可信度中（人名和未确认的地址），<br>
   * 3表示可信度最高（由用户成果认领确认或人名和确认的地址）。<br>
   * （地址状态信息PDWH_PUB_ADDR_INS_RECORD表状态描述）
   **/

  public MongoPdwhPubAuthorSnsPsnRecord() {
    super();
  }

  public MongoPdwhPubAuthorSnsPsnRecord(Long pubId, Long psnId, String psnName, Long insId, String insName,
      Integer status, Date updateTime) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.psnName = psnName;
    this.insId = insId;
    this.insName = insName;
    this.status = status;
    this.updateTime = updateTime;
  }

  public MongoPdwhPubAuthorSnsPsnRecord(Long pubId, Long psnId, String psnName, Long insId, String insName,
      Integer status, Date updateTime, Integer nameType) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.psnName = psnName;
    this.insId = insId;
    this.insName = insName;
    this.status = status;
    this.updateTime = updateTime;
    this.nameType = nameType;
  }

  public MongoPdwhPubAuthorSnsPsnRecord(Long id, Long pubId, Long psnId, String psnName, Long insId, String insName,
      Integer status, Date updateTime, Integer nameType) {
    this.id = id;
    this.pubId = pubId;
    this.psnId = psnId;
    this.psnName = psnName;
    this.insId = insId;
    this.insName = insName;
    this.status = status;
    this.updateTime = updateTime;
    this.nameType = nameType;
  }

  public MongoPdwhPubAuthorSnsPsnRecord(Long pubId, Long psnId, Integer nameType, String psnName) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.nameType = nameType;
    this.psnName = psnName;
  }

  public Integer getNameType() {
    return nameType;
  }

  public void setNameType(Integer nameType) {
    this.nameType = nameType;
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

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    result = prime * result + ((nameType == null) ? 0 : nameType.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((psnName == null) ? 0 : psnName.hashCode());
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MongoPdwhPubAuthorSnsPsnRecord other = (MongoPdwhPubAuthorSnsPsnRecord) obj;

    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    if (nameType == null) {
      if (other.nameType != null)
        return false;
    } else if (!nameType.equals(other.nameType))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (psnName == null) {
      if (other.psnName != null)
        return false;
    } else if (!psnName.equals(other.psnName))
      return false;
    if (pubId == null) {
      if (other.pubId != null)
        return false;
    } else if (!pubId.equals(other.pubId))
      return false;
    return true;
  }

}
