package com.smate.web.group.action.grp.form;

import java.util.List;

import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.model.grp.grpbase.GrpKwDisc;
import com.smate.web.group.model.grp.grpbase.GrpStatistics;

/**
 * 群组主表显示信息
 * 
 * @author AiJiangBin
 */
public class GrpShowInfo {
  private Long grpId; // 群组id
  private String des3GrpId; // 群组id
  private Long grpNo; // 群主编码
  private String grpName; // 群组名字
  private String grpDescription;// 群组简介
  private Integer grpCategory; // 群组分类 10:兴趣群组 ， 11项目群组
  private String grpAuatars;// 群组头像地址
  private String status;// 群组状态 【01 = 正常 ， 99 = 删除】
  private Integer role;// 当前人与群组的权限关系 ， 1群组拥有者 ， 2群组管理成员 ， 3群组普通成员， 4，待批准
  private Integer isTop;// 是否置顶， 1=已经置顶 ，0=没有置顶
  private Integer isApplyGrp;// 是否是申请群组， 1=是，0=否
  private Integer isGrpUnion;// 是否关联 ， 0是 ，1否
  private Integer pendIngCount;// 未处理事项
  private Integer pendIngCountType;// 未处理事项 1=审批申请人 2=成果认领
  private GrpBaseinfo grpBaseInfo;
  private GrpKwDisc grpKwDisc;
  private GrpStatistics grpStatistic;
  private Long grpProjectPubSum; // 项目群组成果数量
  private Long grpProjectRefSum; // 项目群组文献数量
  private Long grpCourseFileSum; // 课程群组课件数量
  private Long grpWorkFileSum;// 课程群组作业数量
  private GrpControl grpControl;
  private List<String> grpKeywordList; // 群组关键词
  private String firstDisciplinetName;
  private String secondDisciplinetName;
  private String grpInviterName;// 群组邀请人
  private String grpInviterInsName;
  private String grpInviterAvatars;
  private String grpInviterDepartment;
  private String grpInviterUrl;
  private String grpInviterDes3psnId;
  private String grpIndexUrl;
  private Long grpPendingConfirmPubs; // 项目待确认成果数
  private Long groupUnreadCount; // 群组的未读数
  private Integer isNsfcPrj;
  private PrjInfo prjInfo;
  private String showDyn;// 是否显示群组动态
  private Integer grpListIndex; // 群组在查询出的列表中的rowNum

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getGrpNo() {
    return grpNo;
  }

  public void setGrpNo(Long grpNo) {
    this.grpNo = grpNo;
  }

  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getGrpDescription() {
    return grpDescription;
  }

  public void setGrpDescription(String grpDescription) {
    this.grpDescription = grpDescription;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public String getGrpAuatars() {
    return grpAuatars;
  }

  public void setGrpAuatars(String grpAuatars) {
    this.grpAuatars = grpAuatars;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getIsTop() {
    return isTop;
  }

  public void setIsTop(Integer isTop) {
    this.isTop = isTop;
  }

  public GrpBaseinfo getGrpBaseInfo() {
    return grpBaseInfo;
  }

  public void setGrpBaseInfo(GrpBaseinfo grpBaseInfo) {
    this.grpBaseInfo = grpBaseInfo;
  }

  public GrpKwDisc getGrpKwDisc() {
    return grpKwDisc;
  }

  public void setGrpKwDisc(GrpKwDisc grpKwDisc) {
    this.grpKwDisc = grpKwDisc;
  }

  public GrpStatistics getGrpStatistic() {
    return grpStatistic;
  }

  public void setGrpStatistic(GrpStatistics grpStatistic) {
    this.grpStatistic = grpStatistic;
  }

  public Integer getPendIngCount() {
    return pendIngCount;
  }

  public void setPendIngCount(Integer pendIngCount) {
    this.pendIngCount = pendIngCount;
  }

  public Integer getIsGrpUnion() {
    return isGrpUnion;
  }

  public void setIsGrpUnion(Integer isGrpUnion) {
    this.isGrpUnion = isGrpUnion;
  }

  public GrpControl getGrpControl() {
    return grpControl;
  }

  public void setGrpControl(GrpControl grpControl) {
    this.grpControl = grpControl;
  }

  public Integer getPendIngCountType() {
    return pendIngCountType;
  }

  public void setPendIngCountType(Integer pendIngCountType) {
    this.pendIngCountType = pendIngCountType;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public List<String> getGrpKeywordList() {
    return grpKeywordList;
  }

  public void setGrpKeywordList(List<String> grpKeywordList) {
    this.grpKeywordList = grpKeywordList;
  }

  public Integer getIsApplyGrp() {
    return isApplyGrp;
  }

  public void setIsApplyGrp(Integer isApplyGrp) {
    this.isApplyGrp = isApplyGrp;
  }

  public String getFirstDisciplinetName() {
    return firstDisciplinetName;
  }

  public void setFirstDisciplinetName(String firstDisciplinetName) {
    this.firstDisciplinetName = firstDisciplinetName;
  }

  public String getSecondDisciplinetName() {
    return secondDisciplinetName;
  }

  public void setSecondDisciplinetName(String secondDisciplinetName) {
    this.secondDisciplinetName = secondDisciplinetName;
  }

  public String getGrpInviterName() {
    return grpInviterName;
  }

  public void setGrpInviterName(String grpInviterName) {
    this.grpInviterName = grpInviterName;
  }

  public String getGrpIndexUrl() {
    return grpIndexUrl;
  }

  public void setGrpIndexUrl(String grpIndexUrl) {
    this.grpIndexUrl = grpIndexUrl;
  }

  public String getGrpInviterInsName() {
    return grpInviterInsName;
  }

  public void setGrpInviterInsName(String grpInviterInsName) {
    this.grpInviterInsName = grpInviterInsName;
  }

  public String getGrpInviterDepartment() {
    return grpInviterDepartment;
  }

  public void setGrpInviterDepartment(String grpInviterDepartment) {
    this.grpInviterDepartment = grpInviterDepartment;
  }

  public String getGrpInviterDes3psnId() {
    return grpInviterDes3psnId;
  }

  public void setGrpInviterDes3psnId(String grpInviterDes3psnId) {
    this.grpInviterDes3psnId = grpInviterDes3psnId;
  }

  public String getGrpInviterUrl() {
    return grpInviterUrl;
  }

  public void setGrpInviterUrl(String grpInviterUrl) {
    this.grpInviterUrl = grpInviterUrl;
  }

  public String getGrpInviterAvatars() {
    return grpInviterAvatars;
  }

  public void setGrpInviterAvatars(String grpInviterAvatars) {
    this.grpInviterAvatars = grpInviterAvatars;
  }

  public Long getGrpProjectPubSum() {
    return grpProjectPubSum;
  }

  public void setGrpProjectPubSum(Long grpProjectPubSum) {
    this.grpProjectPubSum = grpProjectPubSum;
  }

  public Long getGrpProjectRefSum() {
    return grpProjectRefSum;
  }

  public void setGrpProjectRefSum(Long grpProjectRefSum) {
    this.grpProjectRefSum = grpProjectRefSum;
  }

  public Long getGrpCourseFileSum() {
    return grpCourseFileSum;
  }

  public void setGrpCourseFileSum(Long grpCourseFileSum) {
    this.grpCourseFileSum = grpCourseFileSum;
  }

  public Long getGrpWorkFileSum() {
    return grpWorkFileSum;
  }

  public void setGrpWorkFileSum(Long grpWorkFileSum) {
    this.grpWorkFileSum = grpWorkFileSum;
  }

  public Long getGroupUnreadCount() {
    return groupUnreadCount;
  }

  public void setGroupUnreadCount(Long groupUnreadCount) {
    this.groupUnreadCount = groupUnreadCount;
  }

  public Long getGrpPendingConfirmPubs() {
    return grpPendingConfirmPubs;
  }

  public void setGrpPendingConfirmPubs(Long grpPendingConfirmPubs) {
    this.grpPendingConfirmPubs = grpPendingConfirmPubs;
  }

  public Integer getIsNsfcPrj() {
    return isNsfcPrj;
  }

  public void setIsNsfcPrj(Integer isNsfcPrj) {
    this.isNsfcPrj = isNsfcPrj;
  }

  public PrjInfo getPrjInfo() {
    return prjInfo;
  }

  public void setPrjInfo(PrjInfo prjInfo) {
    this.prjInfo = prjInfo;
  }

  public String getShowDyn() {
    return showDyn;
  }

  public void setShowDyn(String showDyn) {
    this.showDyn = showDyn;
  }

  public Integer getGrpListIndex() {
    return grpListIndex;
  }

  public void setGrpListIndex(Integer grpListIndex) {
    this.grpListIndex = grpListIndex;
  }


}
