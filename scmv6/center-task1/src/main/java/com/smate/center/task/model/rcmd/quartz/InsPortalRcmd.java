package com.smate.center.task.model.rcmd.quartz;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "INS_PORTAL")
public class InsPortalRcmd implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 3562446275518036028L;

  private String domain;

  private String logo;

  private Long insId;

  private String zhTitle;

  private String indexPage;

  private String defaultLang;

  private String switchLang;

  private String enTitle;

  private String initTitle;

  private int rolNodeId;

  private int snsNodeId;

  private String webCtx;

  private List<Integer> otherRolNodes;

  private Integer version;

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the insId
   */
  @Id
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param domain the domain to set
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * @return the logo
   */
  @Column(name = "LOGO")
  public String getLogo() {
    return logo;
  }

  /**
   * @param logo the logo to set
   */
  public void setLogo(String logo) {
    this.logo = logo;
  }

  /**
   * @return the indexPage
   */
  @Column(name = "INDEX_PAGE")
  public String getIndexPage() {
    return indexPage;
  }

  /**
   * @param indexPage the indexPage to set
   */
  public void setIndexPage(String indexPage) {
    this.indexPage = indexPage;
  }

  /**
   * @return the defaultLang
   */
  @Column(name = "DEFAULT_LANG")
  public String getDefaultLang() {
    return defaultLang;
  }

  /**
   * @param defaultLang the defaultLang to set
   */
  public void setDefaultLang(String defaultLang) {
    this.defaultLang = defaultLang;
  }

  /**
   * @return the switchLang
   */
  @Column(name = "SWITCH_LANG")
  public String getSwitchLang() {
    return switchLang;
  }

  /**
   * @param switchLang the switchLang to set
   */
  public void setSwitchLang(String switchLang) {
    this.switchLang = switchLang;
  }

  /**
   * @return the etitle
   */
  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  /**
   * @param etitle the etitle to set
   */
  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  /**
   * @return the etitle
   */
  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  @Column(name = "ROL_NODE_ID")
  public int getRolNodeId() {
    return rolNodeId;
  }

  @Column(name = "SNS_NODE_ID")
  public int getSnsNodeId() {
    return snsNodeId;
  }

  public void setRolNodeId(int rolNodeId) {
    this.rolNodeId = rolNodeId;
  }

  public void setSnsNodeId(int snsNodeId) {
    this.snsNodeId = snsNodeId;
  }

  @Transient
  public String getInitTitle() {
    return initTitle;
  }

  public void setInitTitle(String initTitle) {
    this.initTitle = initTitle;
  }

  @Transient
  public List<Integer> getOtherRolNodes() {
    return otherRolNodes;
  }

  /**
   * @param otherRolNodes the otherRolNodes to set
   */
  public void setOtherRolNodes(List<Integer> otherRolNodes) {
    this.otherRolNodes = otherRolNodes;
  }

  /**
   * @return the webCtx
   */
  @Transient
  public String getWebCtx() {
    return webCtx;
  }

  /**
   * @param webCtx the webCtx to set
   */
  public void setWebCtx(String webCtx) {
    this.webCtx = webCtx;
  }

  @Column(name = "VERSION")
  public Integer getVersion() {
    if (version == null) {
      version = 0;
    }
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

}
