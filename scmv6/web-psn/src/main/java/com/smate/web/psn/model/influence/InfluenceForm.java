package com.smate.web.psn.model.influence;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

public class InfluenceForm {
  private String domainscm;
  private Long psnId; // 人员ID
  private String des3PsnId; // 加密的人员ID
  private Integer hindex = 0; // hindex值
  private Integer citedSum = 0; // 总引用数
  private Long awardAndShareSum = 0L; // 赞、分享总数
  private Long downLoadSum = 0L; // 下载总数
  private Integer visitSum = 0; // 阅读总数
  private Integer citeRank = 1; // 引用数排名
  // private List<PsnVisitLineInfo> visitInfo; // 访问趋势数据
  // private List<Date> visitTimeInfo; // 访问的时间点
  private List<Integer> pubCitedInfo; // 成果引用数
  private String XAxisData; // X轴数据
  private String YAxisData; // Y轴数据
  private String pubCitedStr; // 成果引用数据
  private Integer xHinex = 0; // hindex值的x轴的值
  private Integer yHindex = 0; // hindex值的y轴的值
  private Long hindexRanking = 1L; // hindex在好友中的排名
  private Long visitSumRanking = 1L; // 访问数在好友中的排名
  private List<Map<String, String>> visitInsList;// 单位分布list
  private List<Map<String, String>> visitPosList;// 职称分布list
  private Integer friendSum; // 好友数
  private String hasCiteThead;// 是否有引用趋势图 "yes":有 "no":没有
  private Long maxVisitCount; // 最大阅读数
  private boolean isMyself = false; // 是否是本人
  private Long yMaxVal = 0L; // y轴最大坐标值
  private Long yMinVal = 0L; // y轴最小坐标值

  public String getDomainscm() {
    return domainscm;
  }

  public void setDomainscm(String domainscm) {
    this.domainscm = domainscm;
  }

  public Long getPsnId() {
    if (psnId == null && des3PsnId != null) {
      psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
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

  public Integer getHindex() {
    return hindex;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex;
  }

  public Integer getCitedSum() {
    return citedSum;
  }

  public void setCitedSum(Integer citedSum) {
    this.citedSum = citedSum;
  }

  public Long getAwardAndShareSum() {
    return awardAndShareSum;
  }

  public void setAwardAndShareSum(Long awardAndShareSum) {
    this.awardAndShareSum = awardAndShareSum;
  }

  public Long getDownLoadSum() {
    return downLoadSum;
  }

  public void setDownLoadSum(Long downLoadSum) {
    this.downLoadSum = downLoadSum;
  }

  public Integer getVisitSum() {
    return visitSum;
  }

  public void setVisitSum(Integer visitSum) {
    this.visitSum = visitSum;
  }

  /*
   * public List<PsnVisitLineInfo> getVisitInfo() { return visitInfo; }
   * 
   * public void setVisitInfo(List<PsnVisitLineInfo> visitInfo) { this.visitInfo = visitInfo; }
   * 
   * public List<Date> getVisitTimeInfo() { return visitTimeInfo; }
   * 
   * public void setVisitTimeInfo(List<Date> visitTimeInfo) { this.visitTimeInfo = visitTimeInfo; }
   */

  public List<Integer> getPubCitedInfo() {
    return pubCitedInfo;
  }

  public void setPubCitedInfo(List<Integer> pubCitedInfo) {
    this.pubCitedInfo = pubCitedInfo;
  }

  public String getXAxisData() {
    return XAxisData;
  }

  public void setXAxisData(String xAxisData) {
    XAxisData = xAxisData;
  }

  public String getYAxisData() {
    return YAxisData;
  }

  public void setYAxisData(String yAxisData) {
    YAxisData = yAxisData;
  }

  public String getPubCitedStr() {
    return pubCitedStr;
  }

  public void setPubCitedStr(String pubCitedStr) {
    this.pubCitedStr = pubCitedStr;
  }

  public List<Map<String, String>> getVisitInsList() {
    return visitInsList;
  }

  public void setVisitInsList(List<Map<String, String>> visitInsList) {
    this.visitInsList = visitInsList;
  }

  public List<Map<String, String>> getVisitPosList() {
    return visitPosList;
  }

  public void setVisitPosList(List<Map<String, String>> visitPosList) {
    this.visitPosList = visitPosList;
  }

  public Integer getxHinex() {
    return xHinex;
  }

  public void setxHinex(Integer xHinex) {
    this.xHinex = xHinex;
  }

  public Integer getyHindex() {
    return yHindex;
  }

  public void setyHindex(Integer yHindex) {
    this.yHindex = yHindex;
  }

  public Long getHindexRanking() {
    return hindexRanking;
  }

  public void setHindexRanking(Long hindexRanking) {
    this.hindexRanking = hindexRanking;
  }

  public Long getVisitSumRanking() {
    return visitSumRanking;
  }

  public void setVisitSumRanking(Long visitSumRanking) {
    this.visitSumRanking = visitSumRanking;
  }

  public Integer getFriendSum() {
    return friendSum;
  }

  public void setFriendSum(Integer friendSum) {
    this.friendSum = friendSum;
  }

  public Integer getCiteRank() {
    return citeRank;
  }

  public void setCiteRank(Integer citeRank) {
    this.citeRank = citeRank;
  }

  public String getHasCiteThead() {
    return hasCiteThead;
  }

  public void setHasCiteThead(String hasCiteThead) {
    this.hasCiteThead = hasCiteThead;
  }

  public Long getMaxVisitCount() {
    return maxVisitCount;
  }

  public void setMaxVisitCount(Long maxVisitCount) {
    this.maxVisitCount = maxVisitCount;
  }

  public boolean getIsMyself() {
    return isMyself;
  }

  public void setIsMyself(boolean isMyself) {
    this.isMyself = isMyself;
  }

  public Long getyMaxVal() {
    return yMaxVal;
  }

  public void setyMaxVal(Long yMaxVal) {
    this.yMaxVal = yMaxVal;
  }

  public Long getyMinVal() {
    return yMinVal;
  }

  public void setyMinVal(Long yMinVal) {
    this.yMinVal = yMinVal;
  }

}
