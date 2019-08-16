package com.smate.web.dyn.form.dynamic.group;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 
 * @author tsz
 *
 */
public class GroupDynProduceForm {

  private Long groupId; // 群组id
  private String des3GroupId; // 加密的群组id
  private Long psnId; // 人员id
  private String des3Psnid; // 加密的人员id
  private String resType; // 资源类型 pub/file
  private Long resId; // 资源id
  private String des3ResId; // 资源id
  private String dynContent; // 动态文本
  private String tempType; // 动态模板 PUBLISHDYN 发布动态 /ADDRES 添加成果和文件 /SHARE
  // 只是添加内容
  private Long receiverGrpId; // 动态接收群组(产生动态的群组) 如果为空就取groupId
  private String des3ReceiverGrpId;
  private Integer databaseType = 1;// 区分个人库和基准库的分享 个人库 1 基准库2
  private Integer dbId;// 网站Id
  private String resInfoJson; // 资源json格式信息

  public Long getGroupId() {
    if (this.groupId != null && this.groupId != 0L) {
      return groupId;
    } else if (StringUtils.isNotBlank(this.des3GroupId)) {
      this.groupId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(this.des3GroupId));
    } else if (this.groupId == null || this.groupId == 0L) {
      this.groupId = this.receiverGrpId;
    }
    return groupId;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3Psnid() {
    return des3Psnid;
  }

  public void setDes3Psnid(String des3Psnid) {
    this.des3Psnid = des3Psnid;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public Long getResId() {
    if (this.resId == null || this.resId == 0L) {
      if (StringUtils.isNotBlank(this.getDes3ResId())) {
        this.resId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.getDes3ResId()));
      }
    }
    return resId;
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public String getDynContent() {
    return dynContent;
  }

  public void setDynContent(String dynContent) {
    this.dynContent = dynContent;
  }

  public String getTempType() {
    return tempType;
  }

  public void setTempType(String tempType) {
    this.tempType = tempType;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  /**
   * receiverGrpId:表示其他群组的群组id， 分享到其他群组的id ； groupId ： 当前群组的id
   * 
   * @return
   */
  public Long getReceiverGrpId() {
    if ((receiverGrpId == null || receiverGrpId == 0L) && StringUtils.isNotBlank(des3ReceiverGrpId)) {
      receiverGrpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ReceiverGrpId));
    } else if (receiverGrpId == null) {
      return groupId;
    }
    return receiverGrpId;
  }

  public void setReceiverGrpId(Long receiverGrpId) {
    this.receiverGrpId = receiverGrpId;
  }

  public String getDes3ReceiverGrpId() {
    if (StringUtils.isBlank(des3ReceiverGrpId) && receiverGrpId != null) {
      des3ReceiverGrpId = Des3Utils.encodeToDes3(receiverGrpId.toString());
    }
    return des3ReceiverGrpId;
  }

  public void setDes3ReceiverGrpId(String des3ReceiverGrpId) {
    this.des3ReceiverGrpId = des3ReceiverGrpId;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getResInfoJson() {
    return resInfoJson;
  }

  public void setResInfoJson(String resInfoJson) {
    this.resInfoJson = resInfoJson;
  }

}
