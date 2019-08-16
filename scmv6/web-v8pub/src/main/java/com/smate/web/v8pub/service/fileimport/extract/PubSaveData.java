package com.smate.web.v8pub.service.fileimport.extract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成果保存的数据，统一格式
 * 
 * @author aijiangbin
 * @date 2018年8月2日
 */
public class PubSaveData {
  /** 默认的个人成果保存 */
  public String pubHandlerName = "snsPubSaveHandler";

  public Long pubId;

  public Long psnId;

  public String title; // 成果标题

  public String publishDate; // 出版日期，发表日期

  public Long countryId; // 来源国家地区id

  public String fundInfo;// 基金标注（资助标注）

  public Integer citations; // 引用次数

  public String doi; // doi

  public String summary; // 摘要，概要

  public String keywords; // 成果关键词(多个拼接)

  public String srcFulltextUrl; // 全文链接

  public Integer pubType; // 成果类型 1:奖励；2:书/著作；3:会议论文；4:期刊论文；5:专利；7:其他；8:学位论文；10:书籍章节

  public Integer recordFrom; // 记录来源 0, "手工录入" 1, "数据库导入" 2, "文件导入" 3, "基准库导入"

  public String remarks; // 备注

  public String organization = "";// 单位


  /**
   * 成果类型对象的构造标准 学位 "pubTypeInfo":{ "degree":"", // 学位 "defenseDate":"", // 答辩日期 "issuingAuthority":""
   * // 颁发单位 "department":"", // 部门 "ISBN":, // 国际标准图书编号 }, 期刊 "pubTypeInfo":{ "jid":"", // 期刊id
   * "name":"", // 期刊名称 "volumeNo":, // 卷号 "issue":, // 期号 "articleNo":, // 文章号 "startPage":, // 起始页码
   * "endPage":, // 结束页码 "publishStatus":"", // 发表状态(P已发表/A已接收) "impactFactors":"" // 影响因子(发表年/最新年)
   * "acceptDate":, // 接收日期,发表状态为已接受才有 }, 专利 "pubTypeInfo":{ "type":, // 专利类别，发明专利51/实用新型52/外观设计53
   * "name":, // 专利名称 "area":, // 专利国家 中国专利/美国专利/欧洲专利/WIPO专利/日本专利/其他 "status":"" // 专利状态，申请/授权
   * "applier":, // 申请人 (专利为申请状态) "applicationDate":, // 申请日期 "patentee":, // 专利权人 (专利为授权状态)
   * "startDate":, // 专利生效起始日期 (专利为授权状态) "endDate":"", // 专利失效结束日期 (专利为授权状态) "applicationNo":"", //
   * 申请号 "publicationOpenNo":"", // 专利公开（公告）号 "IPC":, // IPC号 "CPC":, // cpc号 "transitionStatus":, //
   * 专利转换状态 许可/转让/作价投资/未转化 "price":, // 交易金额 "issuingAuthority":, // 专利授权组织，颁发机构 }, 会议 "pubTypeInfo":{
   * "paperType":, // 论文类别 国际学术会议/国内学术会议 "title":, // 会议标题 "name":, // 会议名称 "organizer":, // 会议组织者
   * "startDate":, // 开始日期 "endDate":"", // 结束日期 "startPage":"", // 起始页码 "endPage":, // 结束页码 }, 奖励
   * "pubTypeInfo":{ "name":"", // 奖励名称 "category":, // 奖项种类 "issuingAuthority":, // 授奖机构
   * "issueInsId":, // 授奖机构id "certificateNo":"", // 证书编号 "awardDate":, // 授奖日期 "grade":, // 奖项等级 },
   * 书/著作 书籍章节 注： 在书籍/著作类别中专著题目存储在主字段title中 在书籍章节类别中，标题为主字段的title 书名为下面的name属性 "pubTypeInfo":{
   * "name":"", // 书籍名 "seriesName":, // 丛书名称 "editors":, // 编辑 "ISBN":, // 国际标准图书编号 "publisher":, //
   * 出版社 "totalWords":, // 总字数 "startPage":, // 起始页码 "endPage":, // 结束页码
   * 
   * "type":, // 书籍/著作类型 "totalPages":"", // 总页数 "chapterNo":, // 章节号 "articleNo":, // 文章号
   * "publishStatus":, // 已出版、未出版 }, 其他 注：不传什么对象时就为其他成果类型 "pubTypeInfo":{
   * 
   * },
   */
  public Map<String, Object> pubTypeInfo = new HashMap<>();


  /**
   * 收录 "sitations":[ { "pubId":, // 成果id "libraryName":"", // 收录机构名 "sitStatus":, // 收录状态 0:未收录 ，1:收录
   * "sitOriginStatus": // 原始收录状态 0:未收录 ，1:收录 "srcUrl":"", // 来源URL "srcDbId":"", // 来源dbid
   * "srcId":"", // 来源唯一标识 } ],
   */
  public List<SitationInfo> sitations = new ArrayList<>();

  public String getPubHandlerName() {
    return pubHandlerName;
  }


  public void setPubHandlerName(String pubHandlerName) {
    this.pubHandlerName = pubHandlerName;
  }


  public Long getPubId() {
    return pubId;
  }


  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }


  public Long getPsnId() {
    return psnId;
  }


  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getPublishDate() {
    return publishDate;
  }


  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }


  public Long getCountryId() {
    return countryId;
  }


  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }


  public String getFundInfo() {
    return fundInfo;
  }


  public void setFundInfo(String fundInfo) {
    this.fundInfo = fundInfo;
  }


  public Integer getCitations() {
    return citations;
  }


  public void setCitations(Integer citations) {
    this.citations = citations;
  }


  public String getDoi() {
    return doi;
  }


  public void setDoi(String doi) {
    this.doi = doi;
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


  public String getSrcFulltextUrl() {
    return srcFulltextUrl;
  }


  public void setSrcFulltextUrl(String srcFulltextUrl) {
    this.srcFulltextUrl = srcFulltextUrl;
  }


  public Integer getPubType() {
    return pubType;
  }


  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }


  public Integer getRecordFrom() {
    return recordFrom;
  }


  public void setRecordFrom(Integer recordFrom) {
    this.recordFrom = recordFrom;
  }


  public String getRemarks() {
    return remarks;
  }


  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }


  public String getOrganization() {
    return organization;
  }


  public void setOrganization(String organization) {
    this.organization = organization;
  }


  public Map<String, Object> getPubTypeInfo() {
    return pubTypeInfo;
  }


  public void setPubTypeInfo(Map<String, Object> pubTypeInfo) {
    this.pubTypeInfo = pubTypeInfo;
  }


  public List<SitationInfo> getSitations() {
    return sitations;
  }


  public void setSitations(List<SitationInfo> sitations) {
    this.sitations = sitations;
  }



}
