package com.smate.web.v8pub.vo.exportfile;

import java.io.Serializable;

/**
 * 根据模板导出excel类.
 * 
 * @author chenxiangrong
 * 
 */
public class ExportPubTemp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8103256833097464147L;
  private Integer pubtype;// 成果类型
  private String publishyear;// 发表年份
  private String title;// 标题
  private String authornames;// 作者名
  private String insnames;// 单位名
  private String journalname;// 期刊名
  private String issn;// ISSN
  private String publishdate;// 发表日期
  private String publishstatus;// 状态
  private String volume;// 卷号
  private String issue;// 期号
  private String pagenumber;// 起止页码或文章号
  private String fundinfo;// 资助标注
  private String listscie;// SCIE收录
  private String listssci;// SSCI收录
  private String lististp;// ISTP收录
  private String listei;// EI收录
  private String citations;// 引用数
  private String doi;// DOI
  private String summary;// 摘要
  private String keywords;// 关键词
  private String fulltexturl;// 全文地址
  private String sheetIndex;// 工作表顺序

  private String papertype;// 会议类别
  private String conferencename;// 会议名称
  private String organizer;// 会议组织者
  private String country;// 国家或地区
  private String papersname;// 论文集名
  private String startdate;// 开始日期
  private String enddate;// 结束日期
  private String startpage;// 开始页码
  private String endpage;// 结束页码

  private String degree;// 学位
  private String defensedate;// 答辩日期
  private String thesisinsname;// 颁发单位
  private String department;// 部门

  private String patentarea;// 专利国家
  private String patenttype;// 专利类别
  private String patentstatus;// 专利状态
  private String applicationno;// 申请（专利）号
  private String patentopenno;// 公开（公告）号
  private String patentapplier;// 申请人（专利权人）
  private String ipc;// IPC号
  private String cpc;// CPC号
  private String applicationdate;// 申请（公开）日期
  private String patentchangestatus;// 转化状态
  private String patentprice;// 交易金额

  private String bookname;// 书名
  private String isbn;// ISBN
  private String chapterno;// 章节号码
  private String editors;// 编辑
  private String publisher;// 出版社

  private String seriesname;// 丛书名称
  private String booktype;// 专著类别
  private String language;// 语种
  private String totalpages;// 页数
  private String totalwords;// 总字数

  private String issueinsname;// 授奖机构
  private String certificateno;// 证书编号
  private String awarddate;// 授奖日期
  private String awardgrade;// 奖励等级
  private String awardcategory;// 奖励种类

  private String standardno;// 标准号
  private String standardtype;// 标准类型
  private String standardintitution;// 公布机构（归口单位）


  private String registerno; // 登记号
  private String acquisitiontype;// 权利获得方式
  private String scopetype;// 权利范围

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthornames() {
    return authornames;
  }

  public void setAuthornames(String authornames) {
    this.authornames = authornames;
  }

  public String getInsnames() {
    return insnames;
  }

  public void setInsnames(String insnames) {
    this.insnames = insnames;
  }

  public String getJournalname() {
    return journalname;
  }

  public void setJournalname(String journalname) {
    this.journalname = journalname;
  }

  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public String getPublishdate() {
    return publishdate;
  }

  public void setPublishdate(String publishdate) {
    this.publishdate = publishdate;
  }

  public String getPublishstatus() {
    return publishstatus;
  }

  public void setPublishstatus(String publishstatus) {
    this.publishstatus = publishstatus;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public String getPagenumber() {
    return pagenumber;
  }

  public void setPagenumber(String pagenumber) {
    this.pagenumber = pagenumber;
  }

  public String getFundinfo() {
    return fundinfo;
  }

  public void setFundinfo(String fundinfo) {
    this.fundinfo = fundinfo;
  }

  public String getListscie() {
    return listscie;
  }

  public void setListscie(String listscie) {
    this.listscie = listscie;
  }

  public String getListssci() {
    return listssci;
  }

  public void setListssci(String listssci) {
    this.listssci = listssci;
  }

  public String getLististp() {
    return lististp;
  }

  public void setLististp(String lististp) {
    this.lististp = lististp;
  }

  public String getListei() {
    return listei;
  }

  public void setListei(String listei) {
    this.listei = listei;
  }

  public String getCitations() {
    return citations;
  }

  public void setCitations(String citations) {
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

  public String getFulltexturl() {
    return fulltexturl;
  }

  public void setFulltexturl(String fulltexturl) {
    this.fulltexturl = fulltexturl;
  }

  public String getSheetIndex() {
    return sheetIndex;
  }

  public void setSheetIndex(String sheetIndex) {
    this.sheetIndex = sheetIndex;
  }

  public String getPapertype() {
    return papertype;
  }

  public void setPapertype(String papertype) {
    this.papertype = papertype;
  }

  public String getConferencename() {
    return conferencename;
  }

  public void setConferencename(String conferencename) {
    this.conferencename = conferencename;
  }

  public String getOrganizer() {
    return organizer;
  }

  public void setOrganizer(String organizer) {
    this.organizer = organizer;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPapersname() {
    return papersname;
  }

  public void setPapersname(String papersname) {
    this.papersname = papersname;
  }

  public String getStartdate() {
    return startdate;
  }

  public void setStartdate(String startdate) {
    this.startdate = startdate;
  }

  public String getEnddate() {
    return enddate;
  }

  public void setEnddate(String enddate) {
    this.enddate = enddate;
  }

  public String getStartpage() {
    return startpage;
  }

  public void setStartpage(String startpage) {
    this.startpage = startpage;
  }

  public String getEndpage() {
    return endpage;
  }

  public void setEndpage(String endpage) {
    this.endpage = endpage;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getDefensedate() {
    return defensedate;
  }

  public void setDefensedate(String defensedate) {
    this.defensedate = defensedate;
  }

  public String getThesisinsname() {
    return thesisinsname;
  }

  public void setThesisinsname(String thesisinsname) {
    this.thesisinsname = thesisinsname;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPatentarea() {
    return patentarea;
  }

  public void setPatentarea(String patentarea) {
    this.patentarea = patentarea;
  }

  public String getPatenttype() {
    return patenttype;
  }

  public void setPatenttype(String patenttype) {
    this.patenttype = patenttype;
  }

  public String getPatentstatus() {
    return patentstatus;
  }

  public void setPatentstatus(String patentstatus) {
    this.patentstatus = patentstatus;
  }

  public String getApplicationno() {
    return applicationno;
  }

  public void setApplicationno(String applicationno) {
    this.applicationno = applicationno;
  }

  public String getPatentopenno() {
    return patentopenno;
  }

  public void setPatentopenno(String patentopenno) {
    this.patentopenno = patentopenno;
  }

  public String getPatentapplier() {
    return patentapplier;
  }

  public void setPatentapplier(String patentapplier) {
    this.patentapplier = patentapplier;
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

  public String getApplicationdate() {
    return applicationdate;
  }

  public void setApplicationdate(String applicationdate) {
    this.applicationdate = applicationdate;
  }

  public String getPatentchangestatus() {
    return patentchangestatus;
  }

  public void setPatentchangestatus(String patentchangestatus) {
    this.patentchangestatus = patentchangestatus;
  }

  public String getPatentprice() {
    return patentprice;
  }

  public void setPatentprice(String patentprice) {
    this.patentprice = patentprice;
  }

  public String getBookname() {
    return bookname;
  }

  public void setBookname(String bookname) {
    this.bookname = bookname;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getChapterno() {
    return chapterno;
  }

  public void setChapterno(String chapterno) {
    this.chapterno = chapterno;
  }

  public String getEditors() {
    return editors;
  }

  public void setEditors(String editors) {
    this.editors = editors;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getSeriesname() {
    return seriesname;
  }

  public void setSeriesname(String seriesname) {
    this.seriesname = seriesname;
  }

  public String getBooktype() {
    return booktype;
  }

  public void setBooktype(String booktype) {
    this.booktype = booktype;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getTotalpages() {
    return totalpages;
  }

  public void setTotalpages(String totalpages) {
    this.totalpages = totalpages;
  }

  public String getTotalwords() {
    return totalwords;
  }

  public void setTotalwords(String totalwords) {
    this.totalwords = totalwords;
  }

  public String getIssueinsname() {
    return issueinsname;
  }

  public void setIssueinsname(String issueinsname) {
    this.issueinsname = issueinsname;
  }

  public String getCertificateno() {
    return certificateno;
  }

  public void setCertificateno(String certificateno) {
    this.certificateno = certificateno;
  }

  public String getAwarddate() {
    return awarddate;
  }

  public void setAwarddate(String awarddate) {
    this.awarddate = awarddate;
  }

  public String getAwardgrade() {
    return awardgrade;
  }

  public void setAwardgrade(String awardgrade) {
    this.awardgrade = awardgrade;
  }

  public String getAwardcategory() {
    return awardcategory;
  }

  public void setAwardcategory(String awardcategory) {
    this.awardcategory = awardcategory;
  }

  public String getStandardno() {
    return standardno;
  }

  public void setStandardno(String standardno) {
    this.standardno = standardno;
  }

  public String getStandardtype() {
    return standardtype;
  }

  public void setStandardtype(String standardtype) {
    this.standardtype = standardtype;
  }

  public String getStandardintitution() {
    return standardintitution;
  }

  public void setStandardintitution(String standardintitution) {
    this.standardintitution = standardintitution;
  }

  public String getRegisterno() {
    return registerno;
  }

  public void setRegisterno(String registerno) {
    this.registerno = registerno;
  }

  public String getAcquisitiontype() {
    return acquisitiontype;
  }

  public void setAcquisitiontype(String acquisitiontype) {
    this.acquisitiontype = acquisitiontype;
  }

  public String getScopetype() {
    return scopetype;
  }

  public void setScopetype(String scopetype) {
    this.scopetype = scopetype;
  }

  public Integer getPubtype() {
    return pubtype;
  }

  public void setPubtype(Integer pubtype) {
    this.pubtype = pubtype;
  }

  public String getPublishyear() {
    return publishyear;
  }

  public void setPublishyear(String publishyear) {
    this.publishyear = publishyear;
  }



}
