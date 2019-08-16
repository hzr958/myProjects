package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 个人好友系统推荐.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_SYS")
public class FriendTempSys implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5820819319533142086L;
  // pk
  private Long id;
  // 个人psnId
  private Long psnId;
  // 推荐好友psnId
  private Long tempPsnId;
  // 推荐好友的姓名
  private String tempPsnName;
  // 推荐好友头像url
  private String tempPsnHeadUrl;
  // 推荐好友头衔
  private String tempPsnTitel;

  private String des3TempPsnId;

  private String tempPsnEname;

  private String tempPsnFirstName;

  private String tempPsnLastName;

  private String score;// 相识度

  private int isSendInvitation;
  // is成果合作
  private boolean iscpt;
  // 合作类型：4成果合作，5项目合作
  private String cptTypes;

  private Integer hindex;

  // 是否已经关注
  private Long attId;

  public FriendTempSys() {
    super();
  }

  public FriendTempSys(Long psnId, Long tempPsnId) {
    super();
    this.psnId = psnId;
    this.tempPsnId = tempPsnId;
  }

  public FriendTempSys(Long tempPsnId, String tempPsnName, String tempPsnHeadUrl, String tempPsnTitel) {
    super();
    this.tempPsnId = tempPsnId;
    this.tempPsnName = tempPsnName;
    this.tempPsnHeadUrl = tempPsnHeadUrl;
    this.tempPsnTitel = tempPsnTitel;
  }

  public FriendTempSys(Long tempPsnId, String tempPsnName, String tempPsnEname, String tempPsnHeadUrl,
      String tempPsnTitel) {
    super();
    this.tempPsnId = tempPsnId;
    this.tempPsnName = tempPsnName;
    this.tempPsnEname = tempPsnEname;
    this.tempPsnHeadUrl = tempPsnHeadUrl;
    this.tempPsnTitel = tempPsnTitel;
  }

  public FriendTempSys(Long tempPsnId, String tempPsnName, String tempPsnHeadUrl, String tempPsnTitel,
      String tempPsnFirstName, String tempPsnLastName, String cptTypes) {
    super();
    this.tempPsnId = tempPsnId;
    this.tempPsnName = tempPsnName;
    this.tempPsnHeadUrl = tempPsnHeadUrl;
    this.tempPsnTitel = tempPsnTitel;
    this.tempPsnFirstName = tempPsnFirstName;
    this.tempPsnLastName = tempPsnLastName;
    this.cptTypes = cptTypes;
  }

  public FriendTempSys(Long tempPsnId, String tempPsnName, String tempPsnHeadUrl, String tempPsnTitel,
      String tempPsnFirstName, String tempPsnLastName) {
    super();
    this.tempPsnId = tempPsnId;
    this.tempPsnName = tempPsnName;
    this.tempPsnHeadUrl = tempPsnHeadUrl;
    this.tempPsnTitel = tempPsnTitel;
    this.tempPsnFirstName = tempPsnFirstName;
    this.tempPsnLastName = tempPsnLastName;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_SYSTEM", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "TEMP_PSN_ID")
  public Long getTempPsnId() {
    return tempPsnId;
  }

  public void setTempPsnId(Long tempPsnId) {
    this.tempPsnId = tempPsnId;
  }

  @Transient
  public String getTempPsnName() {
    if (tempPsnName != null && "null".equals(tempPsnName))
      tempPsnName = tempPsnName.replaceAll("null", "");
    return tempPsnName;
  }

  public void setTempPsnName(String tempPsnName) {
    this.tempPsnName = tempPsnName;
  }

  @Transient
  public String getTempPsnHeadUrl() {
    return tempPsnHeadUrl;
  }

  public void setTempPsnHeadUrl(String tempPsnHeadUrl) {
    this.tempPsnHeadUrl = tempPsnHeadUrl;
  }

  @Transient
  public String getTempPsnTitel() {
    if (tempPsnTitel != null && "null".equals(tempPsnTitel))
      tempPsnTitel = tempPsnTitel.replaceAll("null", "");
    return tempPsnTitel;
  }

  public void setTempPsnTitel(String tempPsnTitel) {
    this.tempPsnTitel = tempPsnTitel;
  }

  @Transient
  public String getDes3TempPsnId() {
    if (this.tempPsnId != null && des3TempPsnId == null) {
      des3TempPsnId = ServiceUtil.encodeToDes3(this.tempPsnId.toString());
    }
    return des3TempPsnId;
  }

  public void setDes3TempPsnId(String des3TempPsnId) {
    this.des3TempPsnId = des3TempPsnId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((tempPsnId == null) ? 0 : tempPsnId.hashCode());
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
    FriendTempSys other = (FriendTempSys) obj;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (tempPsnId == null) {
      if (other.tempPsnId != null)
        return false;
    } else if (!tempPsnId.equals(other.tempPsnId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public String getTempPsnEname() {
    if (tempPsnEname != null && "null".equals(tempPsnEname))
      tempPsnEname = tempPsnEname.replaceAll("null", "");
    return tempPsnEname;
  }

  public void setTempPsnEname(String tempPsnEname) {
    this.tempPsnEname = tempPsnEname;
  }

  @Transient
  public String getTempPsnFirstName() {
    return tempPsnFirstName;
  }

  public void setTempPsnFirstName(String tempPsnFirstName) {
    this.tempPsnFirstName = tempPsnFirstName;
  }

  @Transient
  public String getTempPsnLastName() {
    return tempPsnLastName;
  }

  public void setTempPsnLastName(String tempPsnLastName) {
    this.tempPsnLastName = tempPsnLastName;
  }

  @Transient
  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  @Transient
  public int getIsSendInvitation() {
    return isSendInvitation;
  }

  public void setIsSendInvitation(int isSendInvitation) {
    this.isSendInvitation = isSendInvitation;
  }

  @Transient
  public boolean isIscpt() {
    // SCM-5046 系统各处去除合字显示
    iscpt = false;
    return iscpt;
  }

  public void setIscpt(boolean iscpt) {
    this.iscpt = iscpt;
  }

  @Transient
  public String getCptTypes() {
    return cptTypes;
  }

  public void setCptTypes(String cptTypes) {
    this.cptTypes = cptTypes;
  }

  @Transient
  public Integer getHindex() {
    return hindex;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex;
  }

  @Transient
  public Long getAttId() {
    return attId;
  }

  public void setAttId(Long attId) {
    this.attId = attId;
  }

}
