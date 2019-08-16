package com.smate.web.group.action.grp.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.model.group.GrpPubs;
import com.smate.web.group.model.grp.grpbase.GrpControl;

/**
 * 群组讨论表单
 * 
 * @author AiJiangBin
 *
 */
public class GrpDiscussForm {
  private String grpDesc; // 群组简介
  private List<GrpDiscussShowMemberInfo> memberInfoList; // 群组成员
  private List<GrpPubShowInfo> grpPubShowInfoList; // 群组成果 显示信息
  private List<GrpPubs> grpPubsList;// 群组成果
  private Long grpId;
  private String des3GrpId;
  private String des3PsnId; // 加密的人员id
  private Long psnId; // 人员id
  private Integer role;// 群组角色权限[1=创建人,2=管理员, 3=组员]
  private Integer grpCategory; // 群组类型
  private String openType;// O开放,H半开放,P隐私
  private String flag;// 首页是否显示,0不显示,1显示,2群组成员不判断
  private GrpControl grpControl;// 当前群组开放模块
  private PrjInfo prjInfo;

  public String getGrpDesc() {
    return grpDesc;
  }

  public void setGrpDesc(String grpDesc) {
    this.grpDesc = grpDesc;
  }

  public List<GrpDiscussShowMemberInfo> getMemberInfoList() {
    return memberInfoList;
  }

  public void setMemberInfoList(List<GrpDiscussShowMemberInfo> memberInfoList) {
    this.memberInfoList = memberInfoList;
  }

  public List<GrpPubs> getGrpPubsList() {
    return grpPubsList;
  }

  public void setGrpPubsList(List<GrpPubs> grpPubsList) {
    this.grpPubsList = grpPubsList;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getGrpId() {
    if ((grpId == null || grpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
      Long.getLong("");
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public List<GrpPubShowInfo> getGrpPubShowInfoList() {
    return grpPubShowInfoList;
  }

  public void setGrpPubShowInfoList(List<GrpPubShowInfo> grpPubShowInfoList) {
    this.grpPubShowInfoList = grpPubShowInfoList;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public GrpControl getGrpControl() {
    return grpControl;
  }

  public void setGrpControl(GrpControl grpControl) {
    this.grpControl = grpControl;
  }

  public PrjInfo getPrjInfo() {
    return prjInfo;
  }

  public void setPrjInfo(PrjInfo prjInfo) {
    this.prjInfo = prjInfo;
  }
}
