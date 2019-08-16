package com.smate.web.psn.form.resume;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.newresume.PsnResume;
import com.smate.web.psn.model.resume.CVEduInfo;
import com.smate.web.psn.model.resume.CVPrj;
import com.smate.web.psn.model.resume.CVPsnInfo;
import com.smate.web.psn.model.resume.CVPubInfo;
import com.smate.web.psn.model.resume.CVWorkInfo;

/**
 * 简历form
 * 
 * @author lhd
 *
 */
public class PersonalResumeForm {
  private Long psnId;// 人员id
  private String des3PsnId;// 加密的人员id
  private String serviceType; // 服务类型
  private Long resumeId; // 简历ID
  private String des3ResumeId; // 加密简历ID
  private List<PsnResume> resumeList;// 简历列表
  private Integer moduleId; // 模块ID
  private Integer moduleSeq; // 模块顺序
  private String moduleTitle; // 模块标题
  private String moduleInfo; // 模块信息，json格式
  private String psnBaseInfo;// 个人的基本信息
  private List<Map<String, String>> eduMapList;// 教育经历列表
  private List<Map<String, String>> workMapList;// 工作经历列表
  private String resumeName;// 简历名称
  private List<CVPrj> CVPrjList;// 项目模块
  private List<ProjectInfo> prjInfo;// 显示项目模块信息
  private List<Project> prjList;// 显示已导入项目信息
  private String searchKey; // 检索条件
  private Page page = new Page(); // 封装的page对象，分页查询用
  private Integer isRepresentPubModule = 0; // 是代表性著作模块
  private Integer isOtherPubModule = 0; // 是其他成果模块
  private List<CVPubInfo> pubInfoList; // 简历成果信息List
  private List<PubInfo> pubInfos; // 成果信息
  private String addedPrjIds;// 已导入项目ids
  private List<PubInfo> representPubList; // 代表性成果信息List
  private List<PubInfo> otherPubList; // 其他成果信息List
  private CVWorkInfo cvWorkInfo;// 工作经历
  private CVEduInfo cvEduInfo;// 教育经历
  private String seqEdu;// 第几条教育经历
  private String seqWork;// 第几条工作经历
  private String des3CnfId; // 加密的人员权限表cnfId
  private CVPsnInfo cvPsnInfo;// 个人信息
  private String currentPsnName; // 当前人员姓名
  private String resultMsg;// 返回的信息
  private String isShowMoule = "0";// 是否显示该模块，0显示，1不显示
  private Integer wordSeqStart = 20; // word模板中链接id起始值
  private String cvFileName; // 导出的简历文件名称
  private Integer baseInfoModuleStatus = 0; // 基本信息模块状态，0：正常，1：不显示出来
  private Integer workModuleStatus = 0; // 工作经历模块状态，0：正常，1：不显示出来
  private Integer eduModuleStatus = 0; // 教育经历模块状态，0：正常，1：不显示出来
  private Integer prjModuleStatus = 0; // 项目模块状态，0：正常，1：不显示出来
  private Integer pubModuleStatus = 0; // 10篇以内代表性项目模块状态，0：正常，1：不显示出来
  private Integer otherPubModuleStatus = 0; // 其他代表性论著模块状态，0：正常，1：不显示出来

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    }
    if (psnId == null || psnId == 0l) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public Long getResumeId() {
    if (resumeId == null && StringUtils.isNotBlank(des3ResumeId)) {
      resumeId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ResumeId));
    }
    return resumeId;
  }

  public void setResumeId(Long resumeId) {
    this.resumeId = resumeId;
  }

  public String getDes3ResumeId() {
    return des3ResumeId;
  }

  public void setDes3ResumeId(String des3ResumeId) {
    this.des3ResumeId = des3ResumeId;
  }

  public String getPsnBaseInfo() {
    return psnBaseInfo;
  }

  public void setPsnBaseInfo(String psnBaseInfo) {
    this.psnBaseInfo = psnBaseInfo;
  }

  public List<Map<String, String>> getEduMapList() {
    return eduMapList;
  }

  public void setEduMapList(List<Map<String, String>> eduMapList) {
    this.eduMapList = eduMapList;
  }

  public List<Map<String, String>> getWorkMapList() {
    return workMapList;
  }

  public void setWorkMapList(List<Map<String, String>> workMapList) {
    this.workMapList = workMapList;
  }

  public List<PsnResume> getResumeList() {
    return resumeList;
  }

  public void setResumeList(List<PsnResume> resumeList) {
    this.resumeList = resumeList;
  }

  public Integer getModuleId() {
    return moduleId;
  }

  public void setModuleId(Integer moduleId) {
    this.moduleId = moduleId;
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

  public String getModuleInfo() {
    return moduleInfo;
  }

  public void setModuleInfo(String moduleInfo) {
    this.moduleInfo = moduleInfo;
  }

  public String getResumeName() {
    return resumeName;
  }

  public void setResumeName(String resumeName) {
    this.resumeName = resumeName;
  }

  public List<CVPrj> getCVPrjList() {
    return CVPrjList;
  }

  public void setCVPrjList(List<CVPrj> cVPrjList) {
    CVPrjList = cVPrjList;
  }

  public List<ProjectInfo> getPrjInfo() {
    return prjInfo;
  }

  public void setPrjInfo(List<ProjectInfo> prjInfo) {
    this.prjInfo = prjInfo;
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

  public List<Project> getPrjList() {
    return prjList;
  }

  public void setPrjList(List<Project> prjList) {
    this.prjList = prjList;
  }

  public Integer getIsRepresentPubModule() {
    return isRepresentPubModule;
  }

  public void setIsRepresentPubModule(Integer isRepresentPubModule) {
    this.isRepresentPubModule = isRepresentPubModule;
  }

  public Integer getIsOtherPubModule() {
    return isOtherPubModule;
  }

  public void setIsOtherPubModule(Integer isOtherPubModule) {
    this.isOtherPubModule = isOtherPubModule;
  }

  public List<CVPubInfo> getPubInfoList() {
    return pubInfoList;
  }

  public void setPubInfoList(List<CVPubInfo> pubInfoList) {
    this.pubInfoList = pubInfoList;
  }

  public List<PubInfo> getPubInfos() {
    return pubInfos;
  }

  public void setPubInfos(List<PubInfo> pubInfos) {
    this.pubInfos = pubInfos;
  }

  public String getAddedPrjIds() {
    return addedPrjIds;
  }

  public void setAddedPrjIds(String addedPrjIds) {
    this.addedPrjIds = addedPrjIds;
  }

  public List<PubInfo> getRepresentPubList() {
    return representPubList;
  }

  public void setRepresentPubList(List<PubInfo> representPubList) {
    this.representPubList = representPubList;
  }

  public List<PubInfo> getOtherPubList() {
    return otherPubList;
  }

  public void setOtherPubList(List<PubInfo> otherPubList) {
    this.otherPubList = otherPubList;
  }

  public CVWorkInfo getCvWorkInfo() {
    return cvWorkInfo;
  }

  public void setCvWorkInfo(CVWorkInfo cvWorkInfo) {
    this.cvWorkInfo = cvWorkInfo;
  }

  public CVEduInfo getCvEduInfo() {
    return cvEduInfo;
  }

  public void setCvEduInfo(CVEduInfo cvEduInfo) {
    this.cvEduInfo = cvEduInfo;
  }

  public String getSeqEdu() {
    return seqEdu;
  }

  public void setSeqEdu(String seqEdu) {
    this.seqEdu = seqEdu;
  }

  public String getSeqWork() {
    return seqWork;
  }

  public void setSeqWork(String seqWork) {
    this.seqWork = seqWork;
  }

  public String getDes3CnfId() {
    return des3CnfId;
  }

  public void setDes3CnfId(String des3CnfId) {
    this.des3CnfId = des3CnfId;
  }

  public CVPsnInfo getCvPsnInfo() {
    return cvPsnInfo;
  }

  public void setCvPsnInfo(CVPsnInfo cvPsnInfo) {
    this.cvPsnInfo = cvPsnInfo;
  }

  public String getCurrentPsnName() {
    return currentPsnName;
  }

  public void setCurrentPsnName(String currentPsnName) {
    this.currentPsnName = currentPsnName;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public String getIsShowMoule() {
    return isShowMoule;
  }

  public void setIsShowMoule(String isShowMoule) {
    this.isShowMoule = isShowMoule;
  }

  public Integer getWordSeqStart() {
    return wordSeqStart;
  }

  public void setWordSeqStart(Integer wordSeqStart) {
    this.wordSeqStart = wordSeqStart;
  }

  public String getCvFileName() {
    return cvFileName;
  }

  public void setCvFileName(String cvFileName) {
    this.cvFileName = cvFileName;
  }

  public Integer getBaseInfoModuleStatus() {
    return baseInfoModuleStatus;
  }

  public void setBaseInfoModuleStatus(Integer baseInfoModuleStatus) {
    this.baseInfoModuleStatus = baseInfoModuleStatus;
  }

  public Integer getWorkModuleStatus() {
    return workModuleStatus;
  }

  public void setWorkModuleStatus(Integer workModuleStatus) {
    this.workModuleStatus = workModuleStatus;
  }

  public Integer getEduModuleStatus() {
    return eduModuleStatus;
  }

  public void setEduModuleStatus(Integer eduModuleStatus) {
    this.eduModuleStatus = eduModuleStatus;
  }

  public Integer getPrjModuleStatus() {
    return prjModuleStatus;
  }

  public void setPrjModuleStatus(Integer prjModuleStatus) {
    this.prjModuleStatus = prjModuleStatus;
  }

  public Integer getPubModuleStatus() {
    return pubModuleStatus;
  }

  public void setPubModuleStatus(Integer pubModuleStatus) {
    this.pubModuleStatus = pubModuleStatus;
  }

  public Integer getOtherPubModuleStatus() {
    return otherPubModuleStatus;
  }

  public void setOtherPubModuleStatus(Integer otherPubModuleStatus) {
    this.otherPubModuleStatus = otherPubModuleStatus;
  }

  @Override
  public String toString() {
    return "PersonalResumeForm [psnId=" + psnId + ", des3PsnId=" + des3PsnId + ", serviceType=" + serviceType
        + ", resumeId=" + resumeId + ", des3ResumeId=" + des3ResumeId + ", resumeList=" + resumeList + ", moduleId="
        + moduleId + ", moduleSeq=" + moduleSeq + ", moduleTitle=" + moduleTitle + ", moduleInfo=" + moduleInfo
        + ", psnBaseInfo=" + psnBaseInfo + ", eduMapList=" + eduMapList + ", workMapList=" + workMapList
        + ", resumeName=" + resumeName + ", CVPrjList=" + CVPrjList + ", prjInfo=" + prjInfo + ", prjList=" + prjList
        + ", searchKey=" + searchKey + ", page=" + page + ", isRepresentPubModule=" + isRepresentPubModule
        + ", isOtherPubModule=" + isOtherPubModule + ", pubInfoList=" + pubInfoList + ", pubInfos=" + pubInfos
        + ", addedPrjIds=" + addedPrjIds + ", representPubList=" + representPubList + ", otherPubList=" + otherPubList
        + ", cvWorkInfo=" + cvWorkInfo + ", cvEduInfo=" + cvEduInfo + ", seqEdu=" + seqEdu + ", seqWork=" + seqWork
        + ", des3CnfId=" + des3CnfId + ", cvPsnInfo=" + cvPsnInfo + ", currentPsnName=" + currentPsnName
        + ", resultMsg=" + resultMsg + "]";
  }

}
