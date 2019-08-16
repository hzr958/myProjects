package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 个人好友分组.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PSN_FRIEND_GROUP")
public class FriendGroup implements Serializable {

  private static final long serialVersionUID = -917725504459148322L;
  // pk
  private Long groupId;
  // 个人psnId
  private Long psnId;
  // 小组中文名
  private String nameZh;
  // 小组英文名
  private String nameEn;
  // 小组总计好友人数
  private Long firendCount;
  // 是否可以编辑
  private Long status;
  // 页面显示的名称
  private String name;
  // 页面显示的名称，不截取长度
  private String fullName;
  // 是否在当前用户的好友分组中
  private int isUserFGroup;

  private String desName;// 加密的name

  private String sendEncoderURL;// 待发送的、编码过的URL

  public FriendGroup() {
    super();
    this.status = 0L;
    this.firendCount = 0L;
    this.isUserFGroup = 0;
  }

  @Id
  @Column(name = "FRIEND_GROUP_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_FRIEND_GROUP", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "NAME_ZH")
  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  @Column(name = "NAME_EN")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  @Transient
  public Long getFirendCount() {
    return firendCount;
  }

  public void setFirendCount(Long firendCount) {
    this.firendCount = firendCount;
  }

  @Transient
  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (StringUtils.isNotBlank(name)) {
      if (isChinese(name) && name.length() > 6)
        name = name.substring(0, 6) + "..";
      else if (name.length() > 20)
        name = name.substring(0, 20) + "..";
    }
    this.name = name;
  }

  @Transient
  public int getIsUserFGroup() {
    return isUserFGroup;
  }

  public void setIsUserFGroup(int isUserFGroup) {
    this.isUserFGroup = isUserFGroup;
  }

  public static boolean isChinese(String str) {
    String regex = "^[\u4E00-\u9FFF]+$";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    boolean flag = false;
    for (int i = 0; i < str.length(); i++) {
      if (pattern.matcher(String.valueOf(str.charAt(i))).matches()) {
        flag = true;
        break;
      }
    }
    return flag;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public String getDesName() {
    return desName;
  }

  public void setDesName(String desName) {
    this.desName = desName;
  }

  @Transient
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Transient
  public String getSendEncoderURL() {
    return sendEncoderURL;
  }

  public void setSendEncoderURL(String sendEncoderURL) {
    this.sendEncoderURL = sendEncoderURL;
  }

}
