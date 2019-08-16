package com.smate.web.mobile.group.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * @description 移动端群组文件VO
 * @author xiexing
 * @date 2019年5月10日
 */
public class MobileGroupFileVO {
  private Long grpId;// 群组id
  private String des3GrpId;// 群组加密id
  private String searchKey;// 查询关键字
  private Long memberId;// 群组成员id(查询群组成员文件)
  private String des3MemberId;// 群组成员加密id(查询群组成员文件)
  private Long grpLabelId;// 未加密标签id
  private String des3GrpLabelId;// 群组标签加密id
  private Integer grpCategory;// 群组分类 10:课程群组 ， 11项目群组,12,兴趣群组
  private Integer psnRole; // 4申请中,9群组外成员
  private Page page = new Page(10);// 分页对象
  private String des3GrpFileId; // 群组文件加密ID
  private String sharePlatform = "friend";// 分享平台
  private String des3RecieverIds;// 分享给联系人加密id
  private String receiverEmails;// 通过邮件将文件分享给联系人
  private String des3GrpIds;// 分享给群组加密id
  private List<PsnInfoShow> psnInfoShow;// 回显选中的人员
  private String resType;// 资源类型
  private String des3ResId;// 分享资源加密id
  private String leaveMsg;// 分享留言信息
  private String fileName;// 当前分享的文件名称
  private List<Integer> searchFileType = new ArrayList<Integer>(); // 查询的文件类型1:作业;2:教学课件
  private Integer workFileType; // 1: 作业
  private Integer courseFileType;// 2: 教学课件
  private Integer PageNo;
  private String labelIds;// 标签id多个用逗号分隔
  private Integer grpFileType = 0;// 1、作业，2、课件，0、文件

  public Long getGrpId() {
    if (NumberUtils.isNullOrZero(grpId) && StringUtils.isNotEmpty(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getMemberId() {
    if (NumberUtils.isNullOrZero(memberId) && StringUtils.isNotEmpty(des3MemberId)) {
      memberId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3MemberId));
    }
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getDes3MemberId() {
    return des3MemberId;
  }

  public void setDes3MemberId(String des3MemberId) {
    this.des3MemberId = des3MemberId;
  }

  public String getDes3GrpLabelId() {
    return des3GrpLabelId;
  }

  public void setDes3GrpLabelId(String des3GrpLabelId) {
    this.des3GrpLabelId = des3GrpLabelId;
  }


  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public Integer getPsnRole() {
    return psnRole;
  }

  public void setPsnRole(Integer psnRole) {
    this.psnRole = psnRole;
  }


  @Override
  public String toString() {
    return "MobileGroupFileVO [grpId=" + grpId + ", des3GrpId=" + des3GrpId + ", searchKey=" + searchKey + ", memberId="
        + memberId + ", des3MemberId=" + des3MemberId + ", grpLabelId=" + grpLabelId + ", des3GrpLabelId="
        + des3GrpLabelId + ", grpCategory=" + grpCategory + ", psnRole=" + psnRole + ", page=" + page
        + ", des3GrpFileId=" + des3GrpFileId + ", sharePlatform=" + sharePlatform + ", des3RecieverIds="
        + des3RecieverIds + ", receiverEmails=" + receiverEmails + ", des3GrpIds=" + des3GrpIds + ", psnInfoShow="
        + psnInfoShow + ", resType=" + resType + ", des3ResId=" + des3ResId + ", leaveMsg=" + leaveMsg + ", fileName="
        + fileName + ", searchFileType=" + searchFileType + ", workFileType=" + workFileType + ", courseFileType="
        + courseFileType + "]";
  }

  public String getResType() {
    return resType;
  }

  public void setResType(String resType) {
    this.resType = resType;
  }

  public String getDes3ResId() {
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {
    this.des3ResId = des3ResId;
  }

  public String getSharePlatform() {
    return sharePlatform;
  }

  public void setSharePlatform(String sharePlatform) {
    this.sharePlatform = sharePlatform;
  }

  public String getDes3GrpIds() {
    return des3GrpIds;
  }

  public void setDes3GrpIds(String des3GrpIds) {
    this.des3GrpIds = des3GrpIds;
  }

  public String getDes3RecieverIds() {
    return des3RecieverIds;
  }

  public void setDes3RecieverIds(String des3RecieverIds) {
    this.des3RecieverIds = des3RecieverIds;
  }

  public List<PsnInfoShow> getPsnInfoShow() {
    return psnInfoShow;
  }

  public void setPsnInfoShow(List<PsnInfoShow> psnInfoShow) {
    this.psnInfoShow = psnInfoShow;
  }

  public Long getGrpLabelId() {
    return grpLabelId;
  }

  public void setGrpLabelId(Long grpLabelId) {
    this.grpLabelId = grpLabelId;
  }

  public String getLeaveMsg() {
    return leaveMsg;
  }

  public void setLeaveMsg(String leaveMsg) {
    this.leaveMsg = leaveMsg;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getReceiverEmails() {
    return receiverEmails;
  }

  public void setReceiverEmails(String receiverEmails) {
    this.receiverEmails = receiverEmails;
  }

  public String getDes3GrpFileId() {
    return des3GrpFileId;
  }

  public void setDes3GrpFileId(String des3GrpFileId) {
    this.des3GrpFileId = des3GrpFileId;
  }

  public List<Integer> getSearchFileType() {
    if (searchFileType != null && searchFileType.size() > 0) {
      return searchFileType;
    }
    if (workFileType != null && courseFileType == null) {
      searchFileType.add(1);
    } else if (workFileType == null && courseFileType != null) {
      searchFileType.add(2);
    } else if (workFileType != null && courseFileType != null) {
      searchFileType.add(1);
      searchFileType.add(2);
    } else {
      searchFileType.add(0); // 默认查询文件
    }
    return searchFileType;
  }

  public void setSearchFileType(List<Integer> searchFileType) {
    this.searchFileType = searchFileType;
  }

  public Integer getWorkFileType() {
    return workFileType;
  }

  public void setWorkFileType(Integer workFileType) {
    this.workFileType = workFileType;
  }

  public Integer getCourseFileType() {
    return courseFileType;
  }

  public void setCourseFileType(Integer courseFileType) {
    this.courseFileType = courseFileType;
  }

  public Integer getPageNo() {
    return PageNo;
  }

  public void setPageNo(Integer pageNo) {
    PageNo = pageNo;
  }

  public String getLabelIds() {
    return labelIds;
  }

  public void setLabelIds(String labelIds) {
    this.labelIds = labelIds;
  }

  public Integer getGrpFileType() {
    return grpFileType;
  }

  public void setGrpFileType(Integer grpFileType) {
    this.grpFileType = grpFileType;
  }

}
