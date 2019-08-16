package com.smate.web.v8pub.service.fileimport.extract;


import java.io.Serializable;
import java.util.List;

import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubSCAcquisitionTypeEnum;
import com.smate.core.base.pub.enums.PubSCScopeTypeEnum;
import com.smate.core.base.pub.enums.PubStandardTypeEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;

/**
 * 单个成果的信息
 * 
 * @author aijiangbin
 * @date 2018年7月30日
 */
public class PubFileInfo implements Serializable {
  private static final long serialVersionUID = -6788077819715308847L;

  public Integer seqNo; // 系列号 从1开始

  public String title;// 成果标题

  public List<SitationInfo> sitationJson;

  public String sourceDbCode = ""; // srcDbId dbId
  public String sourceId = ""; //

  public String organization = "";// 单位(该属性保存的是通信作者与各作者单位的详细信息)

  public String authorNames;// 作者名称列表

  private String authorNamesAbbr;// 作者简称

  public String cabstract; // briefDesc

  public String pubyear; // publishDate 发表日期
  public String acceptYear; // 接受日期

  public String volumeNo; // 卷号 最大20字符

  public String issue; // 最大20字符

  public Integer pubType;// 成果类型

  public String original;// 期刊名称

  public String keywords;// 关键字

  public String keywordPlus;// 额外的关键词

  public String issn; // 国际标准期刊号

  public String doi;

  public Integer citations;// 引用数

  public String srcFulltextUrl;

  public String regNo; // TODO

  public String city;

  public String fundInfo;

  public String remarks;

  public String pageNumber; // 起止页码 或者 文章号

  // 奖励
  public String awardCategory;// 奖励类别
  public String awardGrade;// 奖励等级
  public String issueInsName;// 颁奖机构
  public String certificateNo;// 证书编号
  public String awardDate;// 授奖日期


  /**
   * 书著title book pubyear 出版日期
   */
  public String publisher; // --Pub aI //出版社

  public String language; // -- Lan aG

  public PubBookTypeEnum categoryValue; // 书著类型 ,专利类别
  public String publicationStatus; // 出版状态
  public Integer totalPages; //
  public Integer totalWords; //
  // Book Section
  // publisher 取值 --Pub aI
  public String bookTitle;// bookTitle ;书籍/著作名
  public String seriesName; // 丛书名
  public String editors; // 编辑
  public String chapterNo; // 章节号
  public String ISBN;// 国际标准图书编号

  /**
   * Patent category_value==专利类别 issue_org== 发证单位 pubyear == 专利申请日期 applier == patent_applier 专利权人
   */
  public String patentNo; // 专利号
  public String startDate;// patent_issue_date; 专利生效起始日期 effectiveStartDate
  public String endDate;// 截止生效日期 effectiveEndDate
  public String applicationNo; // patent_reg_no ; 申请号
  public String issuingAuthority; // patent_issue_org ; 专利授权组织，签发机关
  public String categoryNo;// patent_category ; 主分类号
  public String patentStatus;// 专利状态
  public String patentOpenNo;// 公开(公告)号
  public String applicationDate;// 申请(公开)日期
  public PubPatentTransitionStatusEnum patentChangeStatus;// 转化状态
  public String patentPrice;// 交易金额
  public PubPatentAreaEnum patentArea;// 专利国家
  public String patenType;// 专利类型
  public String ipc;// 专利ipc
  public String cpc;// 专利cpc

  /**
   * 学位论文 Thesis thesisProgramme == degree ==学位 thesis_dept_name == department thesisInsName ==
   * issuingAuthority 签发机关，颁发单位 pubyear ==== 答辩日期
   */
  public PubThesisDegreeEnum degree; // 学位
  public String department; // thesis_dept_name ; 部门
  public String defenseDate;// 答辩日期

  public String applier; // patent_applier ; 申请人\专利权人

  /**
   * pub_conf_paper 会议 conf_venue == 城市 proceedingTitle ==会议名称
   */
  public String confName; // 会议名称
  public PubConferencePaperTypeEnum paperTypeValue; // 论文类别
  public String organizer; // 会议组织者
  public String papersName;// 会议集名

  // other
  public String country; // 国家或地区

  /**
   * 标准
   */
  public PubStandardTypeEnum type; // 标准类型
  public String standardNo; // 标准号
  public String publishAgency; // 公布机构
  public String technicalCommittees; // 归口单位

  /**
   * 软件著作权
   */
  public String registerNo; // 登记号
  public PubSCAcquisitionTypeEnum acquisitionType; // 权利获得方式
  public PubSCScopeTypeEnum scopeType; // 权利范围

  private String email;// 通信邮箱地址

  private String unitInfo;// 作者单位信息(仅保存单位信息,区分于organization)


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSourceDbCode() {
    return sourceDbCode;
  }

  public void setSourceDbCode(String sourceDbCode) {
    this.sourceDbCode = sourceDbCode;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getCabstract() {
    return cabstract;
  }

  public void setCabstract(String cabstract) {
    this.cabstract = cabstract;
  }

  public String getPubyear() {
    return pubyear;
  }

  public void setPubyear(String pubyear) {
    this.pubyear = pubyear;
  }

  public String getVolumeNo() {
    return volumeNo;
  }

  public void setVolumeNo(String volumeNo) {
    this.volumeNo = volumeNo;
  }

  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getISBN() {
    return ISBN;
  }

  public void setISBN(String iSBN) {
    ISBN = iSBN;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }

  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getFundInfo() {
    return fundInfo;
  }

  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getSeriesName() {
    return seriesName;
  }

  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
  }

  public String getEditors() {
    return editors;
  }

  public void setEditors(String editors) {
    this.editors = editors;
  }

  public String getChapterNo() {
    return chapterNo;
  }

  public void setChapterNo(String chapterNo) {
    this.chapterNo = chapterNo;
  }

  public String getPatentNo() {
    return patentNo;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getApplicationNo() {
    return applicationNo;
  }

  public void setApplicationNo(String applicationNo) {
    this.applicationNo = applicationNo;
  }

  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }

  public String getCategoryNo() {
    return categoryNo;
  }

  public void setCategoryNo(String categoryNo) {
    this.categoryNo = categoryNo;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getApplier() {
    return applier;
  }

  public void setApplier(String applier) {
    this.applier = applier;
  }

  public String getRegNo() {
    return regNo;
  }

  public void setRegNo(String regNo) {
    this.regNo = regNo;
  }

  public String getConfName() {
    return confName;
  }

  public void setConfName(String confName) {
    this.confName = confName;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getAwardCategory() {
    return awardCategory;
  }

  public void setAwardCategory(String awardCategory) {
    this.awardCategory = awardCategory;
  }

  public String getAwardGrade() {
    return awardGrade;
  }

  public void setAwardGrade(String awardGrade) {
    this.awardGrade = awardGrade;
  }

  public String getIssueInsName() {
    return issueInsName;
  }

  public void setIssueInsName(String issueInsName) {
    this.issueInsName = issueInsName;
  }

  public PubBookTypeEnum getCategoryValue() {
    return categoryValue;
  }

  public void setCategoryValue(PubBookTypeEnum categoryValue) {
    this.categoryValue = categoryValue;
  }

  public String getPublicationStatus() {
    return publicationStatus;
  }

  public void setPublicationStatus(String publicationStatus) {
    this.publicationStatus = publicationStatus;
  }

  public Integer getTotalWords() {
    return totalWords;
  }

  public void setTotalWords(Integer totalWords) {
    this.totalWords = totalWords;
  }

  public PubConferencePaperTypeEnum getPaperTypeValue() {
    return paperTypeValue;
  }

  public void setPaperTypeValue(PubConferencePaperTypeEnum paperTypeValue) {
    this.paperTypeValue = paperTypeValue;
  }

  public String getOrganizer() {
    return organizer;
  }

  public void setOrganizer(String organizer) {
    this.organizer = organizer;
  }

  public String getPatentStatus() {
    return patentStatus;
  }

  public void setPatentStatus(String patentStatus) {
    this.patentStatus = patentStatus;
  }

  public String getPatentOpenNo() {
    return patentOpenNo;
  }

  public void setPatentOpenNo(String patentOpenNo) {
    this.patentOpenNo = patentOpenNo;
  }

  public PubThesisDegreeEnum getDegree() {
    return degree;
  }

  public void setDegree(PubThesisDegreeEnum degree) {
    this.degree = degree;
  }

  public List<SitationInfo> getSitationJson() {
    return sitationJson;
  }

  public void setSitationJson(List<SitationInfo> sitationJson) {
    this.sitationJson = sitationJson;
  }

  public String getAcceptYear() {
    return acceptYear;
  }

  public void setAcceptYear(String acceptYear) {
    this.acceptYear = acceptYear;
  }

  public String getKeywordPlus() {
    return keywordPlus;
  }

  public void setKeywordPlus(String keywordPlus) {
    this.keywordPlus = keywordPlus;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Integer getCitations() {
    return citations;
  }

  public void setCitations(Integer citations) {
    this.citations = citations;
  }

  public String getCertificateNo() {
    return certificateNo;
  }

  public void setCertificateNo(String certificateNo) {
    this.certificateNo = certificateNo;
  }

  public String getAwardDate() {
    return awardDate;
  }

  public void setAwardDate(String awardDate) {
    this.awardDate = awardDate;
  }

  public String getPapersName() {
    return papersName;
  }

  public void setPapersName(String papersName) {
    this.papersName = papersName;
  }

  public PubPatentAreaEnum getPatentArea() {
    return patentArea;
  }

  public void setPatentArea(PubPatentAreaEnum patentArea) {
    this.patentArea = patentArea;
  }

  public String getApplicationDate() {
    return applicationDate;
  }

  public void setApplicationDate(String applicationDate) {
    this.applicationDate = applicationDate;
  }

  public PubPatentTransitionStatusEnum getPatentChangeStatus() {
    return patentChangeStatus;
  }

  public void setPatentChangeStatus(PubPatentTransitionStatusEnum patentChangeStatus) {
    this.patentChangeStatus = patentChangeStatus;
  }

  public String getPatentPrice() {
    return patentPrice;
  }

  public void setPatentPrice(String patentPrice) {
    this.patentPrice = patentPrice;
  }

  public String getDefenseDate() {
    return defenseDate;
  }

  public void setDefenseDate(String defenseDate) {
    this.defenseDate = defenseDate;
  }

  public String getIpc() {
    return ipc;
  }

  public void setIpc(String ipc) {
    this.ipc = ipc;
  }

  public String getCpc() {
    return cpc;
  }

  public void setCpc(String cpc) {
    this.cpc = cpc;
  }

  public String getPatenType() {
    return patenType;
  }

  public void setPatenType(String patenType) {
    this.patenType = patenType;
  }

  public String getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
  }

  public String getAuthorNamesAbbr() {
    return authorNamesAbbr;
  }

  public void setAuthorNamesAbbr(String authorNamesAbbr) {
    this.authorNamesAbbr = authorNamesAbbr;
  }

  public String getUnitInfo() {
    return unitInfo;
  }

  public void setUnitInfo(String unitInfo) {
    this.unitInfo = unitInfo;
  }

  public PubStandardTypeEnum getType() {
    return type;
  }

  public void setType(PubStandardTypeEnum type) {
    this.type = type;
  }

  public String getStandardNo() {
    return standardNo;
  }

  public void setStandardNo(String standardNo) {
    this.standardNo = standardNo;
  }

  public String getPublishAgency() {
    return publishAgency;
  }

  public void setPublishAgency(String publishAgency) {
    this.publishAgency = publishAgency;
  }

  public String getTechnicalCommittees() {
    return technicalCommittees;
  }

  public void setTechnicalCommittees(String technicalCommittees) {
    this.technicalCommittees = technicalCommittees;
  }

  public String getRegisterNo() {
    return registerNo;
  }

  public void setRegisterNo(String registerNo) {
    this.registerNo = registerNo;
  }

  public PubSCAcquisitionTypeEnum getAcquisitionType() {
    return acquisitionType;
  }

  public void setAcquisitionType(PubSCAcquisitionTypeEnum acquisitionType) {
    this.acquisitionType = acquisitionType;
  }

  public PubSCScopeTypeEnum getScopeType() {
    return scopeType;
  }

  public void setScopeType(PubSCScopeTypeEnum scopeType) {
    this.scopeType = scopeType;
  }
}
