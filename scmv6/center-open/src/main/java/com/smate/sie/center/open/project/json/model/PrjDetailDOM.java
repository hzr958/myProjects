package com.smate.sie.center.open.project.json.model;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * 项目详情,json统一节点 注意：一个节点多个内容统一用英文分号分割，比如关键词、作者、领域等
 * 
 * @author lijianming
 *
 * @date 2019年2月23日
 */
public class PrjDetailDOM {

  @Id
  protected Long prjId; // 项目Id

  protected String title = new String(); // 标题

  protected Long insId; // 所属机构Id

  protected String summary = new String(); // 摘要，概要

  protected String keywords = new String(); // 关键字

  protected String authorNames = new String(); // 作者拼接字段

  protected String disciplineCode = new String(); // 领域

  protected String disciplineName = new String(); // 领域

  protected Integer isPublicCode; // 是否公开 默认公开

  protected String isPublicName = new String();

  protected String prjFromId = new String(); // 项目来源

  protected String prjFromName = new String(); // 项目来源

  protected String schemeId = new String(); // 项目类别

  protected String schemeName = new String(); // 项目类别名称

  protected String prjInternalNo = new String(); // 项目编号

  protected String prjExterNo = new String(); // 批准号

  protected String fundingYear = new String(); // 立项年度

  protected String amount = new String(); // 项目金额

  protected Integer statusCode; // 项目状态Id

  protected String statusName = new String(); // 项目状态名称

  protected String startDate = new String(); // 项目开始时间

  protected String endDate = new String(); // 项目结束时间

  protected Long fulltextId; // 全文id

  protected String fulltexName = new String();

  protected String srcFulltextUrl = new String(); // 来源全文路径

  protected List<PrjMemberBean> members; // 成员列表

  protected List<PrjAttachmentsBean> prjAttachments; // 附件信息

  protected Integer dataFrom; // 数据来源：0表单新增，1xls导入，2标准文件导入，3联邦检索，4基准库指派，9脚本修复

  protected String updateTime = new String(); // 更新时间

  protected String OA; // Open Access

  protected boolean HCP = false; // Highly Cited Paper 高被引用文章

  protected boolean HP = false; // Hot Paper 热门文章

  public Long getPrjId() {
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getDisciplineCode() {
    return disciplineCode;
  }

  public void setDisciplineCode(String disciplineCode) {
    this.disciplineCode = disciplineCode;
  }

  public String getDisciplineName() {
    return disciplineName;
  }

  public void setDisciplineName(String disciplineName) {
    this.disciplineName = disciplineName;
  }

  public Integer getIsPublicCode() {
    return isPublicCode;
  }

  public void setIsPublicCode(Integer isPublicCode) {
    this.isPublicCode = isPublicCode;
  }

  public String getIsPublicName() {
    return isPublicName;
  }

  public void setIsPublicName(String isPublicName) {
    this.isPublicName = isPublicName;
  }

  public String getPrjFromId() {
    return prjFromId;
  }

  public void setPrjFromId(String prjFromId) {
    this.prjFromId = prjFromId;
  }

  public String getPrjFromName() {
    return prjFromName;
  }

  public void setPrjFromName(String prjFromName) {
    this.prjFromName = prjFromName;
  }

  public String getSchemeId() {
    return schemeId;
  }

  public void setSchemeId(String schemeId) {
    this.schemeId = schemeId;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public String getPrjInternalNo() {
    return prjInternalNo;
  }

  public void setPrjInternalNo(String prjInternalNo) {
    this.prjInternalNo = prjInternalNo;
  }

  public String getPrjExterNo() {
    return prjExterNo;
  }

  public void setPrjExterNo(String prjExterNo) {
    this.prjExterNo = prjExterNo;
  }

  public String getFundingYear() {
    return fundingYear;
  }

  public void setFundingYear(String fundingYear) {
    this.fundingYear = fundingYear;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Long getFulltextId() {
    return fulltextId;
  }

  public void setFulltextId(Long fulltextId) {
    this.fulltextId = fulltextId;
  }

  public String getFulltexName() {
    return fulltexName;
  }

  public void setFulltexName(String fulltexName) {
    this.fulltexName = fulltexName;
  }

  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }

  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }

  public List<PrjMemberBean> getMembers() {
    return members;
  }

  public void setMembers(List<PrjMemberBean> members) {
    this.members = members;
  }

  public List<PrjAttachmentsBean> getPrjAttachments() {
    return prjAttachments;
  }

  public void setPrjAttachments(List<PrjAttachmentsBean> prjAttachments) {
    this.prjAttachments = prjAttachments;
  }

  public Integer getDataFrom() {
    return dataFrom;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getOA() {
    return OA;
  }

  public void setOA(String oA) {
    OA = oA;
  }

  public boolean isHCP() {
    return HCP;
  }

  public void setHCP(boolean hCP) {
    HCP = hCP;
  }

  public boolean isHP() {
    return HP;
  }

  public void setHP(boolean hP) {
    HP = hP;
  }

  @Override
  public String toString() {
    return "PrjDetailDOM [prjId=" + prjId + ", title=" + title + ", insId=" + insId + ", summary=" + summary
        + ", keywords=" + keywords + ", authorNames=" + authorNames + ", disciplineCode=" + disciplineCode
        + ", disciplineName=" + disciplineName + ", isPublicCode=" + isPublicCode + ", isPublicName=" + isPublicName
        + ", prjFromId=" + prjFromId + ", prjFromName=" + prjFromName + ", schemeId=" + schemeId + ", schemeName="
        + schemeName + ", prjInternalNo=" + prjInternalNo + ", prjExterNo=" + prjExterNo + ", fundingYear="
        + fundingYear + ", amount=" + amount + ", statusCode=" + statusCode + ", statusName=" + statusName
        + ", startDate=" + startDate + ", endDate=" + endDate + ", fulltextId=" + fulltextId + ", fulltexName="
        + fulltexName + ", srcFulltextUrl=" + srcFulltextUrl + ", members=" + members + ", prjAttachments="
        + prjAttachments + ", dataFrom=" + dataFrom + ", updateTime=" + updateTime + ", OA=" + OA + ", HCP=" + HCP
        + ", HP=" + HP + "]";
  }

}
