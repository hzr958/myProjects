package com.smate.web.group.form;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组操作Form
 * 
 * @author zk
 *
 */
public class GroupOptForm {

  private String des3GroupId; // 加密群组id
  private Long groupId; // 群组id
  private String des3PsnId; // 加密人员id
  private Long psnId; // 人员id
  private String des3MemberId; // 加密群组成员id
  private Long memberId; // 群组成员id
  private Long stataionFileId;// 个人文件stationFileId
  private String des3StataionFileId;// 加密个人文件stationFileId
  private Long groupFileId;// 群组文件GroupFileId
  private String des3GroupFileId;// 加密群组文件GroupFileid
  private String fileDesc; // 文件描述
  private boolean flag; // 是否群组普通成员删除他人文件 true:是<则提示没权限删除> false:否
  private Integer maxResults;// 查询结果数


  public boolean isFlag() {
    return flag;
  }

  public void setFlag(boolean flag) {
    this.flag = flag;
  }

  public String getDes3MemberId() {
    return des3MemberId;
  }

  public void setDes3MemberId(String des3MemberId) {
    this.des3MemberId = des3MemberId;
  }

  public Long getMemberId() {
    if (memberId == null && StringUtils.isNotBlank(des3MemberId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3MemberId);
      if (des3Str == null) {
        return memberId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3PsnId);
      if (des3Str == null) {
        return psnId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3GroupId() {
    return des3GroupId;
  }

  public void setDes3GroupId(String des3GroupId) {
    this.des3GroupId = des3GroupId;
  }

  public Long getGroupId() {
    if (groupId == null && StringUtils.isNotBlank(des3GroupId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3GroupId);
      if (des3Str == null) {
        return groupId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getStataionFileId() {
    if (stataionFileId == null && StringUtils.isNotBlank(des3StataionFileId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3StataionFileId);
      if (des3Str == null) {
        return stataionFileId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return stataionFileId;
  }

  public void setStataionFileId(Long stataionFileId) {
    this.stataionFileId = stataionFileId;
  }

  public String getDes3StataionFileId() {
    return des3StataionFileId;
  }

  public void setDes3StataionFileId(String des3StataionFileId) {
    this.des3StataionFileId = des3StataionFileId;
  }

  public Long getGroupFileId() {
    if (groupFileId == null && StringUtils.isNotBlank(des3GroupFileId)) {
      String des3Str = Des3Utils.decodeFromDes3(des3GroupFileId);
      if (des3Str == null) {
        return groupFileId;
      } else {
        return Long.valueOf(des3Str);
      }
    }
    return groupFileId;
  }

  public void setGroupFileId(Long groupFileId) {
    this.groupFileId = groupFileId;
  }

  public String getDes3GroupFileId() {
    return des3GroupFileId;
  }

  public void setDes3GroupFileId(String des3GroupFileId) {
    this.des3GroupFileId = des3GroupFileId;
  }

  public String getFileDesc() {
    return fileDesc;
  }

  public void setFileDesc(String fileDesc) {
    this.fileDesc = fileDesc;
  }

  public Integer getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {
    this.maxResults = maxResults;
  }
}
