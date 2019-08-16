package com.smate.center.open.model.pdwh.jnl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.string.JnlFormateUtils;



/**
 * @author yamingd 期刊实体.
 */
@Entity
@Table(name = "JOURNAL")
public class Journal implements java.io.Serializable {

  private static final long serialVersionUID = -1684011248817152568L;

  private Long id;
  /**
   * 期刊中文名.
   */
  private String zhName;
  /**
   * 期刊繁体中文名.
   */
  private String zhTwName;
  /**
   * 期刊英文名.
   */
  private String enName;
  /**
   * ISSN.
   */
  private String issn;
  /**
   * 状态.
   */
  private Integer status;
  /**
   * SCIE收录.
   */
  private Integer listSCIE;
  /**
   * SCI收录.
   */
  private Integer listSCI;
  /**
   * AHCI收录.
   */
  private Integer listAHCI;
  /**
   * SSCI收录.
   */
  private Integer listSSCI;
  /**
   * EI收录.
   */
  private Integer listEI;
  /**
   * ISTP收录.
   */
  private Integer listISTP;
  /**
   * ISR收录.
   */
  private Integer listISR;
  /**
   * 在现实是同一本期刊的期刊ID.
   */
  private Long latestJid;
  /**
   * 最新Impact Factor年度.
   */
  private Integer latestImpactFactorYear;
  /**
   * 最新Impact Factor.
   */
  private Float latestImpactFactor;
  /**
   * 最新R-Impact Factor.
   */
  private Float latestRImpactFactor;
  /**
   * 添加人.
   */
  private Long addPsnId;
  /**
   * 来源.
   */
  private String fromFlag;
  /**
   * 节点ID.
   */
  private Integer nodeId;
  // 出版社
  private String publisher;
  // Journal type
  private String type;
  private Long regionId;
  // 描述
  private String description;
  // 导入时间
  private Date regDate;
  private String jcrAbbrTitle;
  private Integer issuesYear;
  private String journalLang;
  private String publishAddr;
  private String edition;
  private Date verifyDate;
  private Long verifyBy;

  // 匹配基础期刊状态，0或null未匹配，1已匹配 ,3，保留原样,2,匹配至基础期刊,4，新增期刊
  private Integer matchBaseStatus;
  // 基础期刊id
  private Long matchBaseJnlId;

  /**
   * 英文别名.
   */
  private String enameAlias;

  /**
   * 中文别名.
   */
  private String zhNameAlias;

  public Journal() {
    super();
  }

  public Journal(Long id) {
    super();
    this.id = id;
  }

  public Journal(Long id, String zhName, String enName, String issn) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.issn = issn;
  }

  public Journal(Long id, String zhName, String enName, String issn, Long matchBaseJnlId) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.issn = issn;
    this.matchBaseJnlId = matchBaseJnlId;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "JID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_JOURNAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  /**
   * @param issn the issn to set
   */
  public void setIssn(String issn) {
    this.issn = issn;
  }

  /**
   * @return the issn
   */
  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setZhTwName(String tcname) {
    this.zhTwName = tcname;
  }

  @Column(name = "ZH_TW_NAME")
  public String getZhTwName() {
    return zhTwName;
  }

  /**
   * @return the listSCIE
   */
  @Column(name = "LIST_SCIE")
  public Integer getListSCIE() {
    return listSCIE;
  }

  /**
   * @param listSCIE the listSCIE to set
   */
  public void setListSCIE(Integer listSCIE) {
    this.listSCIE = listSCIE;
  }

  /**
   * @return the listSCI
   */
  @Column(name = "LIST_SCI")
  public Integer getListSCI() {
    return listSCI;
  }

  /**
   * @param listSCI the listSCI to set
   */
  public void setListSCI(Integer listSCI) {
    this.listSCI = listSCI;
  }

  /**
   * @return the listAHCI
   */
  @Column(name = "LIST_AHCI")
  public Integer getListAHCI() {
    return listAHCI;
  }

  /**
   * @param listAHCI the listAHCI to set
   */
  public void setListAHCI(Integer listAHCI) {
    this.listAHCI = listAHCI;
  }

  /**
   * @return the listSSCI
   */
  @Column(name = "LIST_SSCI")
  public Integer getListSSCI() {
    return listSSCI;
  }

  /**
   * @param listSSCI the listSSCI to set
   */
  public void setListSSCI(Integer listSSCI) {
    this.listSSCI = listSSCI;
  }

  /**
   * @return the listEI
   */
  @Column(name = "LIST_EI")
  public Integer getListEI() {
    return listEI;
  }

  /**
   * @param listEI the listEI to set
   */
  public void setListEI(Integer listEI) {
    this.listEI = listEI;
  }

  /**
   * @return the listISTP
   */
  @Column(name = "LIST_ISTP")
  public Integer getListISTP() {
    return listISTP;
  }

  /**
   * @param listISTP the listISTP to set
   */
  public void setListISTP(Integer listISTP) {
    this.listISTP = listISTP;
  }

  /**
   * @return the listISR
   */
  @Column(name = "LIST_ISR")
  public Integer getListISR() {
    return listISR;
  }

  /**
   * @param listISR the listISR to set
   */
  public void setListISR(Integer listISR) {
    this.listISR = listISR;
  }

  /**
   * @return the latestJid
   */
  @Column(name = "LATEST_JID")
  public Long getLatestJid() {
    return latestJid;
  }

  /**
   * @param latestJid the latestJid to set
   */
  public void setLatestJid(Long latestJid) {
    this.latestJid = latestJid;
  }

  /**
   * @return the latestImpactFactorYear
   */
  @Column(name = "LATEST_IMPACT_FACTOR_YEAR")
  public Integer getLatestImpactFactorYear() {
    return latestImpactFactorYear;
  }

  /**
   * @param latestImpactFactorYear the latestImpactFactorYear to set
   */
  public void setLatestImpactFactorYear(Integer latestImpactFactorYear) {
    this.latestImpactFactorYear = latestImpactFactorYear;
  }

  /**
   * @return the latestImpactFactor
   */
  @Column(name = "LATEST_IMPACT_FACTOR")
  public Float getLatestImpactFactor() {
    return latestImpactFactor;
  }

  @Column(name = "JOURNAL_TYPE")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * @param latestImpactFactor the latestImpactFactor to set
   */
  public void setLatestImpactFactor(Float latestImpactFactor) {
    this.latestImpactFactor = latestImpactFactor;
  }

  /**
   * @return the latestRImpactFactor
   */
  @Column(name = "LATEST_R_IMPACT_FACTOR")
  public Float getLatestRImpactFactor() {
    return latestRImpactFactor;
  }

  /**
   * @param latestRImpactFactor the latestRImpactFactor to set
   */
  public void setLatestRImpactFactor(Float latestRImpactFactor) {
    this.latestRImpactFactor = latestRImpactFactor;
  }

  /**
   * @param addPsnId
   */
  public void setAddPsnId(Long addPsnId) {
    this.addPsnId = addPsnId;
  }

  /**
   * @return
   */
  @Column(name = "REG_PSN_ID")
  public Long getAddPsnId() {
    return addPsnId;
  }

  @Column(name = "PUBLISHER")
  public String getPublisher() {
    return publisher;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  /**
   * @param fromFlag
   */
  public void setFromFlag(String fromFlag) {
    this.fromFlag = fromFlag;
  }

  /**
   * @return String
   */
  @Column(name = "FROM_FLAG")
  public String getFromFlag() {
    return fromFlag;
  }

  @Column(name = "REG_DATE")
  public Date getRegDate() {
    return regDate;
  }

  public void setRegDate(Date regDate) {
    this.regDate = regDate;
  }

  @Transient
  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  @Column(name = "JCR_ABBR_TITLE")
  public String getJcrAbbrTitle() {
    return jcrAbbrTitle;
  }

  @Column(name = "ISSUES_YEAR")
  public Integer getIssuesYear() {
    return issuesYear;
  }

  @Column(name = "JOURNAL_LANGUAGE")
  public String getJournalLang() {
    return journalLang;
  }

  @Column(name = "PUBLISHER_ADDRESS")
  public String getPublishAddr() {
    return publishAddr;
  }

  @Column(name = "EDITION")
  public String getEdition() {
    return edition;
  }

  @Column(name = "VERIFY_DATE")
  public Date getVerifyDate() {
    return verifyDate;
  }

  @Column(name = "VERIFY_BY")
  public Long getVerifyBy() {
    return verifyBy;
  }

  public void setJcrAbbrTitle(String jcrAbbrTitle) {
    this.jcrAbbrTitle = jcrAbbrTitle;
  }

  public void setIssuesYear(Integer issuesYear) {
    this.issuesYear = issuesYear;
  }

  public void setJournalLang(String journalLang) {
    this.journalLang = journalLang;
  }

  public void setPublishAddr(String publishAddr) {
    this.publishAddr = publishAddr;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  public void setVerifyDate(Date verifyDate) {
    this.verifyDate = verifyDate;
  }

  public void setVerifyBy(Long verifyBy) {
    this.verifyBy = verifyBy;
  }

  public String toString() {
    return String.format("Journal(%s,%s,%s)", this.id, this.enName, this.zhName);
  }

  @Column(name = "MATCH_BASE_STATUS")
  public Integer getMatchBaseStatus() {
    return matchBaseStatus;
  }

  public void setMatchBaseStatus(Integer matchBaseStatus) {
    this.matchBaseStatus = matchBaseStatus;
  }

  @Column(name = "MATCH_BASE_JNL_ID")
  public Long getMatchBaseJnlId() {
    return matchBaseJnlId;
  }

  public void setMatchBaseJnlId(Long matchBaseJnlId) {
    this.matchBaseJnlId = matchBaseJnlId;
  }

  /**
   * @return the enameAlias
   */
  @Column(name = "ename_alias")
  public String getEnameAlias() {
    if (StringUtils.isNotBlank(this.enName))
      enameAlias = JnlFormateUtils.getStrAlias(this.enName);
    return enameAlias;
  }

  /**
   * @param enameAlias the enameAlias to set
   */
  public void setEnameAlias(String enameAlias) {
    this.enameAlias = enameAlias;
  }


  @Column(name = "ZNAME_ALIAS")
  public String getZhNameAlias() {
    if (StringUtils.isNotBlank(this.zhName)) {
      zhNameAlias = JnlFormateUtils.getStrAlias(this.zhName);
    }
    return zhNameAlias;
  }

  public void setZhNameAlias(String zhNameAlias) {
    this.zhNameAlias = zhNameAlias;
  }
}
