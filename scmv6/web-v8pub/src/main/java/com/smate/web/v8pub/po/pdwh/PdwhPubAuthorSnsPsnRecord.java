package com.smate.web.v8pub.po.pdwh;

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
 * 基准库成果作者和sns人员对应关系表
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Entity
@Table(name = "PDWH_AUTHOR_SNS_PSN_RECORD")
public class PdwhPubAuthorSnsPsnRecord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -388262774464033650L;
  private Long id;// 主键id
  private Long pubId;// 成果id
  private Long psnId;// 人员id
  private String pubMemberName;// 成果作者
  private Long pubMemberId;// 成果作者memberId
  private String psnName;// sns人名
  private Long insId;// 单位id
  private String insName;// 单位名
  private Integer status;
  private Date updateTime;
  private Integer nameType;// 匹配上的姓名类型,对应person_pm_name表

  /**
   * 状态 0 成果匹配不到sns人员，<br>
   * 状态1.表示可信度低(仅人名匹配上)<br>
   * 2.表示可信度中（人名和未确认的地址），<br>
   * 3表示可信度最高（由用户成果认领确认或人名和确认的地址）。<br>
   * （地址状态信息PDWH_PUB_ADDR_INS_RELATIVE表状态描述）
   **/

  public PdwhPubAuthorSnsPsnRecord() {
    super();
  }

  public PdwhPubAuthorSnsPsnRecord(Long pubId, Long psnId, String psnName, Long insId, String insName, Integer status,
      Date updateTime) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.psnName = psnName;
    this.insId = insId;
    this.insName = insName;
    this.status = status;
    this.updateTime = updateTime;
  }

  public PdwhPubAuthorSnsPsnRecord(Long pubId, Long psnId, String psnName, Long insId, String insName, Integer status,
      Date updateTime, Integer nameType) {
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

  public PdwhPubAuthorSnsPsnRecord(Long pubId, Long psnId, Integer nameType, String psnName) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.nameType = nameType;
    this.psnName = psnName;
  }

  @Column(name = "NAME_TYPE")
  public Integer getNameType() {
    return nameType;
  }

  public void setNameType(Integer nameType) {
    this.nameType = nameType;
  }

  @Id
  @Column(name = "Id")
  @SequenceGenerator(name = "SEQ_STORE", allocationSize = 1, sequenceName = "SEQ_PDWH_AUTH_SNS_PSN_RECORD")
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
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

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PUB_MEMBER_NAME")
  public String getPubMemberName() {
    return pubMemberName;
  }

  public void setPubMemberName(String pubMemberName) {
    this.pubMemberName = pubMemberName;
  }

  @Column(name = "PUB_MEMBER_ID")
  public Long getPubMemberId() {
    return pubMemberId;
  }

  public void setPubMemberId(Long pubMemberId) {
    this.pubMemberId = pubMemberId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "PSN_NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "UPDATE_TIME")
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
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PdwhPubAuthorSnsPsnRecord other = (PdwhPubAuthorSnsPsnRecord) obj;
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

    if (this == obj)
      return true;

    return true;
  }

  @Override
  public String toString() {
    return "PdwhPubAuthorSnsPsnRecord{" + "id=" + id + ", pubId=" + pubId + ", psnId=" + psnId + ", psnName='" + psnName
        + '\'' + ", insId=" + insId + ", insName='" + insName + '\'' + ", status=" + status + ", updateTime="
        + updateTime + ", nameType=" + nameType + '}';
  }
}
