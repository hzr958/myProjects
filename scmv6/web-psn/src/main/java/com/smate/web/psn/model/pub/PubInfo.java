package com.smate.web.psn.model.pub;

import java.io.Serializable;
import java.util.List;

/**
 * 成果显示信息对象
 * 
 * @author tsz
 *
 */
public class PubInfo implements Serializable, Comparable<PubInfo> {
  // 显示数据
  private String patentStatus; // 专利类型
  private String confType; // 会议论文类型（特邀报告、墙报展示）
  private String authorFlag; // 作者标志
  private String authors; // 作者字符串
  private String awardAuthorList; // 奖励作者列表
  private String source; // 来源
  private String title; // 显示的成果标题
  private String pubTypeName; // 成果类别名称
  private Long pubId; // 成果ID
  private Integer seqNo; // 序号，排序用
  private Integer pubType; // 成果类型
  private Integer isPublished = 1; // 是否已发表（暂时只用于期刊论文）， 1：已发表，0：未发表
  private List<PubMember> memberList; // 人员列表
  private String pubUrl; // 成果地址，优先取短地址，没有则拼接长地址
  private Integer wordHrefSeq; // word里面设置链接用的序号

  public PubInfo() {
    super();
  }

  public String getPatentStatus() {
    return patentStatus;
  }

  public void setPatentStatus(String patentStatus) {
    this.patentStatus = patentStatus;
  }

  public String getConfType() {
    return confType;
  }

  public void setConfType(String confType) {
    this.confType = confType;
  }

  public String getAuthorFlag() {
    return authorFlag;
  }

  public void setAuthorFlag(String authorFlag) {
    this.authorFlag = authorFlag;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getAwardAuthorList() {
    return awardAuthorList;
  }

  public void setAwardAuthorList(String awardAuthorList) {
    this.awardAuthorList = awardAuthorList;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPubTypeName() {
    return pubTypeName;
  }

  public void setPubTypeName(String pubTypeName) {
    this.pubTypeName = pubTypeName;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Integer getIsPublished() {
    return isPublished;
  }

  public void setIsPublished(Integer isPublished) {
    this.isPublished = isPublished;
  }

  public List<PubMember> getMemberList() {
    return memberList;
  }

  public void setMemberList(List<PubMember> memberList) {
    this.memberList = memberList;
  }

  public String getPubUrl() {
    return pubUrl;
  }

  public void setPubUrl(String pubUrl) {
    this.pubUrl = pubUrl;
  }

  public Integer getWordHrefSeq() {
    return wordHrefSeq;
  }

  public void setWordHrefSeq(Integer wordHrefSeq) {
    this.wordHrefSeq = wordHrefSeq;
  }

  @Override
  public int compareTo(PubInfo o) {
    if (o.getSeqNo() < this.getSeqNo()) {
      return 1;
    } else if (o.getSeqNo() > this.getSeqNo()) {
      return -1;
    }
    return 0;
  }

}
