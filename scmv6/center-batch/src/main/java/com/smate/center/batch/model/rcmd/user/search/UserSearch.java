package com.smate.center.batch.model.rcmd.user.search;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 用户查找表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "USER_SEARCH")
/* @Indexed(index = "user_search") */
public class UserSearch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2673687003756900350L;

  private Long psnId;
  private String zhInfo;
  private String enInfo;
  private Integer pubFlag;
  private Integer scmFlag;
  private Integer nodeId;
  private Integer indexFlag;
  private Integer isPrivate;
  private int scoreNum;
  private int selfLogin;
  private String zhInfoIndex;
  private String insNameZh;// 用户所属单位中文名称_MJG_SCM-5103.
  private String insNameEn;// 用户所属单位英文名称_MJG_SCM-5103.
  private String insNameAbbr;// 所属单位简写_MJG_SCM-5103.
  private Long insId;// 所属单位ID_MJG_SCM-5103.

  public UserSearch() {

  }

  public UserSearch(Long psnId, String zhInfo, String enInfo, Integer pubFlag, Integer scmFlag, Integer nodeId,
      Integer indexFlag, Integer isPrivate) {
    super();
    this.psnId = psnId;
    this.zhInfo = zhInfo;
    this.enInfo = enInfo;
    this.pubFlag = pubFlag;
    this.scmFlag = scmFlag;
    this.nodeId = nodeId;
    this.indexFlag = indexFlag;
    this.isPrivate = isPrivate;
  }

  public UserSearch(Long psnId, String zhInfo, String enInfo, Integer pubFlag, Integer scmFlag, Integer nodeId,
      Integer indexFlag, Integer isPrivate, int scoreNum, int selfLogin, String zhInfoIndex) {
    super();
    this.psnId = psnId;
    this.zhInfo = zhInfo;
    this.enInfo = enInfo;
    this.pubFlag = pubFlag;
    this.scmFlag = scmFlag;
    this.nodeId = nodeId;
    this.indexFlag = indexFlag;
    this.isPrivate = isPrivate;
    this.scoreNum = scoreNum;
    this.selfLogin = selfLogin;
    this.zhInfoIndex = zhInfoIndex;
  }

  public UserSearch(Long psnId, String zhInfo, String enInfo, Integer pubFlag, Integer scmFlag, Integer nodeId,
      Integer indexFlag, Integer isPrivate, int scoreNum, int selfLogin, String zhInfoIndex, String insNameZh,
      String insNameEn, String insNameAbbr, Long insId) {
    super();
    this.psnId = psnId;
    this.zhInfo = zhInfo;
    this.enInfo = enInfo;
    this.pubFlag = pubFlag;
    this.scmFlag = scmFlag;
    this.nodeId = nodeId;
    this.indexFlag = indexFlag;
    this.isPrivate = isPrivate;
    this.scoreNum = scoreNum;
    this.selfLogin = selfLogin;
    this.zhInfoIndex = zhInfoIndex;
    this.insNameZh = insNameZh;
    this.insNameEn = insNameEn;
    this.insNameAbbr = insNameAbbr;
    this.insId = insId;
  }

  @Id
  /* @DocumentId */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * 测试要求比较准确，所以改成默认一元分词工具.
   * 
   * @return
   */
  @Column(name = "ZH_INFO")
  /*
   * @Field(index = Index.YES, store = Store.NO, analyzer = @Analyzer(impl =
   * SmartChineseAnalyzer.class))
   */
  public String getZhInfo() {
    return zhInfo;
  }

  @Column(name = "EN_INFO")
  /*
   * @Field(index = Index.YES, store = Store.NO, analyzer = @Analyzer(impl = EnglishAnalyzer.class))
   */
  public String getEnInfo() {
    return enInfo;
  }

  @Column(name = "PUB_FLAG")
  /*
   * @Field(index = Index.YES, store = Store.YES)
   * 
   * @NumericField
   */
  public Integer getPubFlag() {
    return pubFlag;
  }

  @Column(name = "SCM_FLAG")
  /*
   * @Field(index = Index.YES, store = Store.YES)
   * 
   * @NumericField
   */
  public Integer getScmFlag() {
    return scmFlag;
  }

  @Column(name = "NODE_ID")
  /*
   * @Field(index = Index.YES, store = Store.YES)
   * 
   * @NumericField
   */
  public Integer getNodeId() {
    return nodeId;
  }

  @Column(name = "INDEX_FLAG")
  public Integer getIndexFlag() {
    return indexFlag;
  }

  @Column(name = "IS_PRIVATE")
  /*
   * @Field(index = Index.NO, store = Store.YES)
   * 
   * @NumericField
   */
  public Integer getIsPrivate() {
    return isPrivate;
  }

  @Column(name = "SCORE_NUM")
  /*
   * @Field(index = Index.YES, store = Store.YES)
   * 
   * @NumericField
   */
  public int getScoreNum() {
    return scoreNum;
  }

  @Column(name = "SELF_LOGIN")
  /*
   * @Field(index = Index.YES, store = Store.YES)
   * 
   * @NumericField
   */
  public int getSelfLogin() {
    return selfLogin;
  }

  @Column(name = "ZH_INFO_INDEX")
  public String getZhInfoIndex() {
    return zhInfoIndex;
  }

  public void setIndexFlag(Integer indexFlag) {
    this.indexFlag = indexFlag;
  }

  public void setZhInfo(String zhInfo) {
    this.zhInfo = zhInfo;
  }

  public void setEnInfo(String enInfo) {
    this.enInfo = enInfo;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPubFlag(Integer pubFlag) {
    this.pubFlag = pubFlag;
  }

  public void setScmFlag(Integer scmFlag) {
    this.scmFlag = scmFlag;
  }

  public void setIsPrivate(Integer isPrivate) {
    this.isPrivate = isPrivate;
  }

  public void setScoreNum(int scoreNum) {
    this.scoreNum = scoreNum;
  }

  public void setSelfLogin(int selfLogin) {
    this.selfLogin = selfLogin;
  }

  public void setZhInfoIndex(String zhInfoIndex) {
    this.zhInfoIndex = zhInfoIndex;
  }

  @Column(name = "INS_ZH_NAME")
  public String getInsNameZh() {
    return insNameZh;
  }

  @Column(name = "INS_EN_NAME")
  public String getInsNameEn() {
    return insNameEn;
  }

  @Column(name = "INS_NAME_ABBR")
  public String getInsNameAbbr() {
    return insNameAbbr;
  }

  @Column(name = "INS_ID")
  /*
   * @Field(index = Index.NO, store = Store.YES)
   * 
   * @NumericField
   */
  public Long getInsId() {
    return insId;
  }

  public void setInsNameZh(String insNameZh) {
    this.insNameZh = insNameZh;
  }

  public void setInsNameEn(String insNameEn) {
    this.insNameEn = insNameEn;
  }

  public void setInsNameAbbr(String insNameAbbr) {
    this.insNameAbbr = insNameAbbr;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
