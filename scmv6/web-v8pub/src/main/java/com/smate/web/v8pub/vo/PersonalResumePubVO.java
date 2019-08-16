package com.smate.web.v8pub.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 个人简历-----成果模块使用form
 * 
 * @author wsn
 *
 */
public class PersonalResumePubVO implements Serializable {

  private static final long serialVersionUID = 3644704101358052443L;
  // 简历ID
  private String resumeId;
  private String des3ResumeId;

  // 简历中包含的成果ID，以“,”号分隔
  private String pubIds;
  private Long cvId; // 未加密的简历ID
  private String des3CVId; // 加密的简历ID
  private List<PubInfo> pubInfoList; // 成果列表
  private Integer moduleId; // 简历模块ID
  private Long psnId; // 人员Id
  private Long cnfId; // 权限配置ID
  private Integer anyUser; // 查看权限
  private String searchKey; // 查询字符串
  private Page page; // 分页查询用page
  private List<PubInfo> openPubList; // 公开成果列表
  private String addToRepresentPubIds; // 已选中的代表性成果ID, 逗号拼接
  private String otherSelectedPubIds; // 已选中的其他成果ID, 逗号拼接
  private String des3CnfId; // 加密的人员权限配置ID
  private String serviceType; // 服务类型
  private String moduleInfo; // 模块信息，json格式
  private Integer moduleSeq; // 模块顺序，暂时用不到
  private String moduleTitle; // 模块标题

  private List<PubInfo> prpPubs = new ArrayList<PubInfo>();

  public String getResumeId() {
    return resumeId;
  }

  public void setResumeId(String resumeId) {
    this.resumeId = resumeId;
  }

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public List<PubInfo> getPrpPubs() {
    return prpPubs;
  }

  public void setPrpPubs(List<PubInfo> prpPubs) {
    this.prpPubs = prpPubs;
  }

  public Long getCvId() {
    if (cvId == null && StringUtils.isNotBlank(des3CVId)) {
      cvId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3CVId));
    }
    return cvId;
  }

  public void setCvId(Long cvId) {
    this.cvId = cvId;
  }

  public String getDes3CVId() {
    return des3CVId;
  }

  public void setDes3CVId(String des3cvId) {
    des3CVId = des3cvId;
  }

  public List<PubInfo> getPubInfoList() {
    return pubInfoList;
  }

  public void setPubInfoList(List<PubInfo> pubInfoList) {
    this.pubInfoList = pubInfoList;
  }

  public Integer getModuleId() {
    return moduleId;
  }

  public void setModuleId(Integer moduleId) {
    this.moduleId = moduleId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getCnfId() {
    if (cnfId == null && StringUtils.isNotBlank(des3CnfId)) {
      cnfId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3CnfId));
    }
    return cnfId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public Integer getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<PubInfo> getOpenPubList() {
    return openPubList;
  }

  public void setOpenPubList(List<PubInfo> openPubList) {
    this.openPubList = openPubList;
  }

  public String getAddToRepresentPubIds() {
    return addToRepresentPubIds;
  }

  public void setAddToRepresentPubIds(String addToRepresentPubIds) {
    this.addToRepresentPubIds = addToRepresentPubIds;
  }

  public String getOtherSelectedPubIds() {
    return otherSelectedPubIds;
  }

  public void setOtherSelectedPubIds(String otherSelectedPubIds) {
    this.otherSelectedPubIds = otherSelectedPubIds;
  }

  public String getDes3CnfId() {
    return des3CnfId;
  }

  public void setDes3CnfId(String des3CnfId) {
    this.des3CnfId = des3CnfId;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getModuleInfo() {
    return moduleInfo;
  }

  public void setModuleInfo(String moduleInfo) {
    this.moduleInfo = moduleInfo;
  }

  public Integer getModuleSeq() {
    return moduleSeq;
  }

  public void setModuleSeq(Integer moduleSeq) {
    this.moduleSeq = moduleSeq;
  }

  public String getModuleTitle() {
    return moduleTitle;
  }

  public void setModuleTitle(String moduleTitle) {
    this.moduleTitle = moduleTitle;
  }

  public String getDes3ResumeId() {
    return des3ResumeId;
  }

  public void setDes3ResumeId(String des3ResumeId) {
    this.des3ResumeId = des3ResumeId;
  }

}
