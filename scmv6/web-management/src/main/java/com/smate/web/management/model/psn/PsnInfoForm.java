package com.smate.web.management.model.psn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.smate.core.base.utils.model.Page;

public class PsnInfoForm implements Serializable {

  private static final long serialVersionUID = -9086474457478862209L;
  private Long psnId;// 人员Id
  private String psnEmail;// email
  private String loginName;// 人员登录账号
  private Date lastLoginTime;// 最近登录时间
  private Integer pubSum;// 成果数
  private Integer prjSum;// 项目数
  private Integer patentSum;// 专利数
  private String psnUrl;// 人员首页的url
  private Long totalCount;// 人员总数
  private Page page = new Page(10);
  private List<PsnInfo> psnInfoList;
  private List<MailLog> mailInfoList;
  private String template;// 邮件模板名称
  private Date lastSend;// 发送时间
  private Integer status;// 发送状态
  private String nowDate;
  private Integer typeId;// 邮件时间 1：前一个月，2 ：前两个月，23：前三个月，
  // 每页的数量
  private Integer pageSize;
  // 当前页数
  private Integer pageNo;
  // 总页数
  private Integer totalPages;
  private String nameSearchContent;// 人名检索
  private String emailSearchContent;// 账号/邮箱检索
  private Integer searchType; // 检索类型 1：精确检索 0 ：模糊检索
  private String mergeCount;// 保留账号
  private String deleteCount;// 删除帐号
  private String msgInfo;// 合并返回消息

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getPatentSum() {
    return patentSum;
  }

  public void setPatentSum(Integer patentSum) {
    this.patentSum = patentSum;
  }

  public String getPsnUrl() {
    return psnUrl;
  }

  public void setPsnUrl(String psnUrl) {
    this.psnUrl = psnUrl;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Date getLastSend() {
    return lastSend;
  }

  public void setLastSend(Date lastSend) {
    this.lastSend = lastSend;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public List<MailLog> getMailInfoList() {
    return mailInfoList;
  }

  public void setMailInfoList(List<MailLog> mailInfoList) {
    this.mailInfoList = mailInfoList;
  }

  public String getNowDate() {
    return nowDate;
  }

  public void setNowDate(String nowDate) {
    this.nowDate = nowDate;
  }

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public String getNameSearchContent() {
    return nameSearchContent;
  }

  public void setNameSearchContent(String nameSearchContent) {
    this.nameSearchContent = nameSearchContent;
  }

  public String getEmailSearchContent() {
    return emailSearchContent;
  }

  public void setEmailSearchContent(String emailSearchContent) {
    this.emailSearchContent = emailSearchContent;
  }

  public Integer getSearchType() {
    return searchType;
  }

  public void setSearchType(Integer searchType) {
    this.searchType = searchType;
  }

  public String getMergeCount() {
    return mergeCount;
  }

  public void setMergeCount(String mergeCount) {
    this.mergeCount = mergeCount;
  }

  public String getDeleteCount() {
    return deleteCount;
  }

  public void setDeleteCount(String deleteCount) {
    this.deleteCount = deleteCount;
  }

  public String getMsgInfo() {
    return msgInfo;
  }

  public void setMsgInfo(String msgInfo) {
    this.msgInfo = msgInfo;
  }

}
