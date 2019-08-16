package com.smate.web.group.action.grp.form;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.model.group.psn.PsnInfo;

/**
 * 群组成员form
 * 
 * @author zzx
 *
 */
public class GrpMemberForm {

  private Long psnId;// 当前人psnId
  private String des3PsnId;
  private Long targetPsnId;// 目标人Id（被操作的对象）
  private String des3TargetPsnId;
  private String emails;// 被邀请的人员邮件集合，以逗号隔开
  private String targetPsnIds;// 目标人Id集合（被操作的对象）
  private Long grpId;// 群组ID
  private String des3GrpId;
  private Integer role;// 群组角色权限[1=创建人,2=管理员, 3=组员]
  private Integer targetRole;// 被操作人的角色
  private String updateRole;// 修改的权限 修改为：[1=变更群组拥有者,2=设置为管理员, 3=设置为普通组员]
  private Integer delType;// 移除人员的操作[99=删除（被移除出群组）,98=删除（自动退出群组）]
  private Integer disposeType;// 处理人员申请操作[1=同意,2=忽略]
  private Integer firstResult;
  private Integer maxResults;
  private Integer isApplyJoinGrp;// 是否是申请加入群组 1=申请加入群组，0=取消加群组
  private Integer showType;// 1=第一次查显示单条申请人记录，2=非第一次查显示单条申请人记录，3=显示申请人列表
  private Integer psnCount;// 人员数量 （目前使用到的地方：申请人数量、成员列表总人数）
  private Integer isReferrer = 0;// 1=推荐库，0=普通邀请
  private Page<PsnInfo> page = new Page<PsnInfo>();
  private String searchKey;// 检索条件
  private String ispending;// 是否是从群组列表点击未处理事项过来的 1=是 0=否
  private Integer type;// 加入群组的方式1=申请 2=被邀请
  private String targetdes3GrpId;
  private List<Long> grpIds;
  private String msgType; // 发送消息类型
  private String reveiverIds = ""; // 发送邮件的接收Id，逗号隔离
  private String token;
  private String targetUrl;
  private Map<String, Object> resultMap;
  private File emailExcelFile;
  private List<String> emailList;
  private Integer isProjectPub; // 项目成果1 项目文献0
  private Long fiterEmailCount = 0L;// 已过滤的邮件数量
  private Boolean addSuccess = false;// 加入成功
  /**
   * 要显示的人员信息列表
   */
  private List<PsnInfo> psnInfoList;

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGrpId() {
    if ((grpId == null || grpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Long getTargetPsnId() {
    if ((targetPsnId == null || targetPsnId == 0L) && StringUtils.isNotBlank(des3TargetPsnId)) {
      targetPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3TargetPsnId));
    }
    return targetPsnId;
  }

  public void setTargetPsnId(Long targetPsnId) {
    this.targetPsnId = targetPsnId;
  }

  public String getUpdateRole() {
    return updateRole;
  }

  public void setUpdateRole(String updateRole) {
    this.updateRole = updateRole;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public String getTargetPsnIds() {
    return targetPsnIds;
  }

  public void setTargetPsnIds(String targetPsnIds) {
    this.targetPsnIds = targetPsnIds;
  }

  public Integer getDelType() {
    return delType;
  }

  public void setDelType(Integer delType) {
    this.delType = delType;
  }

  public Integer getDisposeType() {
    return disposeType;
  }

  public void setDisposeType(Integer disposeType) {
    this.disposeType = disposeType;
  }

  public Integer getFirstResult() {
    return firstResult;
  }

  public void setFirstResult(Integer firstResult) {
    this.firstResult = firstResult;
  }

  public Integer getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {
    this.maxResults = maxResults;
  }

  public Integer getShowType() {
    return showType;
  }

  public void setShowType(Integer showType) {
    this.showType = showType;
  }

  public Integer getPsnCount() {
    return psnCount;
  }

  public void setPsnCount(Integer psnCount) {
    this.psnCount = psnCount;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getDes3TargetPsnId() {
    return des3TargetPsnId;
  }

  public void setDes3TargetPsnId(String des3TargetPsnId) {
    this.des3TargetPsnId = des3TargetPsnId;
  }

  public Integer getIsReferrer() {
    return isReferrer;
  }

  public void setIsReferrer(Integer isReferrer) {
    this.isReferrer = isReferrer;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getEmails() {
    return emails;
  }

  public void setEmails(String emails) {
    this.emails = emails;
  }

  public String getIspending() {
    return ispending;
  }

  public void setIspending(String ispending) {
    this.ispending = ispending;
  }

  public Integer getTargetRole() {
    return targetRole;
  }

  public void setTargetRole(Integer targetRole) {
    this.targetRole = targetRole;
  }

  public Integer getIsApplyJoinGrp() {
    return isApplyJoinGrp;
  }

  public void setIsApplyJoinGrp(Integer isApplyJoinGrp) {
    this.isApplyJoinGrp = isApplyJoinGrp;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getTargetdes3GrpId() {
    return targetdes3GrpId;
  }

  public void setTargetdes3GrpId(String targetdes3GrpId) {
    this.targetdes3GrpId = targetdes3GrpId;
  }

  public List<Long> getGrpIds() {
    return grpIds;
  }

  public void setGrpIds(List<Long> grpIds) {
    this.grpIds = grpIds;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getReveiverIds() {
    return reveiverIds;
  }

  public void setReveiverIds(String reveiverIds) {
    this.reveiverIds = reveiverIds;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public File getEmailExcelFile() {
    return emailExcelFile;
  }

  public void setEmailExcelFile(File emailExcelFile) {
    this.emailExcelFile = emailExcelFile;
  }

  public List<String> getEmailList() {
    return emailList;
  }

  public void setEmailList(List<String> emailList) {
    this.emailList = emailList;
  }

  public Integer getIsProjectPub() {
    return isProjectPub;
  }

  public void setIsProjectPub(Integer isProjectPub) {
    this.isProjectPub = isProjectPub;
  }

  public Long getFiterEmailCount() {
    return fiterEmailCount;
  }

  public void setFiterEmailCount(Long fiterEmailCount) {
    this.fiterEmailCount = fiterEmailCount;
  }

  public Boolean getAddSuccess() {
    return addSuccess;
  }

  public void setAddSuccess(Boolean addSuccess) {
    this.addSuccess = addSuccess;
  }


}
