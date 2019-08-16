package com.smate.center.batch.model.rol.pub;

// import java.util.Date;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.center.batch.model.sns.pub.InstitutionAdd;

/**
 * 机构类.
 * 
 * @author zt
 * 
 */
@Entity
@Table(name = "INSTITUTION")
public class InstitutionRol implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -5467288486345274202L;
  // 主健
  private Long id;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 单位名，根据语音选择
  private String insName;
  // 名称缩写
  private String abbreviation;
  // 联系人
  private String contactPerson;
  // 中文地址
  private String zhAddress;
  // 英文地址
  private String enAddress;
  // 联系电话
  private String tel;
  // 单位网址
  private String url;
  // 单位状态， 0未开始使用,1:注册,2:审核通过,9:删除
  private Long status;
  // 单位性质单位性质1: college; 2: research center; 3: funding agency;99: others
  private Long nature;
  // 单位服务邮箱
  private String serverEmail;
  // 单位服务电话
  private String serverTel;
  // 地区编码，对应：const_region表主键
  private Long regionId;
  private List<Integer> otherRolNodes;
  private Integer nearSnsNodeId;
  // 邮编
  private String postcode;
  private String logoAddr;
  // 是否申请单位名称修改
  private Integer applyNameCr;
  // 中文名(申请修改)
  private String zhNameCr;
  // 英文名(申请修改)
  private String enNameCr;
  // 组织结构代码, 用于验证单位用户注册或者其他功能
  private String checkCode;
  // 单位用户注册时是否检查组织验证代码0/1
  private Integer isCheckCode;
  // 和ISIS系统对应的单位Code，同步数据需以此Code为准
  private Integer isisOrgCode;
  private String checkEmails;
  // 单位域名，真实数据放在ins_portal.
  private String insDomain;
  private String insBrief;
  // 国家、地区多ID拼接
  private String regionIds;

  public InstitutionRol() {

  }

  public InstitutionRol(InstitutionAdd ins) {
    this.id = ins.getId();
    this.zhName = ins.getZhName();
    this.enName = ins.getEnName();
    this.abbreviation = ins.getAbbreviation();
    this.contactPerson = ins.getContactPerson();
    this.zhAddress = ins.getZhAddress();
    this.enAddress = ins.getEnAddress();
    this.tel = ins.getTel();
    this.url = ins.getUrl();
    this.status = ins.getStatus();
    this.nature = ins.getNature();
    this.serverEmail = ins.getServerEmail();
    this.serverTel = ins.getServerTel();
    this.regionId = ins.getRegionId();
    this.postcode = ins.getPostcode();
    this.isisOrgCode = ins.getIsisOrgCode();
  }

  public InstitutionRol(Long id, String insName, String insDomain) {
    super();
    this.id = id;
    this.insName = insName;
    this.insDomain = insDomain;
  }

  public InstitutionRol(Long id, String insName, String contactPerson, String serverEmail, String serverTel,
      String insDomain) {
    super();
    this.id = id;
    this.insName = insName;
    this.contactPerson = contactPerson;
    this.serverEmail = serverEmail;
    this.serverTel = serverTel;
    this.insDomain = insDomain;
  }

  public InstitutionRol(Long id, String zhName, String enName, String insName, String contactPerson, String serverEmail,
      String serverTel, String insDomain) {
    super();
    this.id = id;
    this.zhName = zhName;
    this.enName = enName;
    this.insName = insName;
    this.contactPerson = contactPerson;
    this.serverEmail = serverEmail;
    this.serverTel = serverTel;
    this.insDomain = insDomain;
  }

  /**
   * @return
   */
  @Id
  @Column(name = "INS_ID")
  public Long getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  /**
   * @return
   */
  @Column(name = "ABBR")
  public String getAbbreviation() {
    return abbreviation;
  }

  /**
   * @param abbreviation
   */
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * @return
   */
  @Column(name = "CONTACT_PERSON")
  public String getContactPerson() {
    return contactPerson;
  }

  /**
   * @param contactPerson
   */
  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  @Column(name = "ZH_ADDRESS")
  public String getZhAddress() {
    return zhAddress;
  }

  public void setZhAddress(String zhAddress) {
    this.zhAddress = zhAddress;
  }

  @Column(name = "EN_ADDRESS")
  public String getEnAddress() {
    return enAddress;
  }

  public void setEnAddress(String enAddress) {
    this.enAddress = enAddress;
  }

  /**
   * @return
   */
  @Column(name = "TEL")
  public String getTel() {
    return tel;
  }

  /**
   * @param tel
   */
  public void setTel(String tel) {
    this.tel = tel;
  }

  /**
   * @return
   */
  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  @Column(name = "CHECK_EMAILS")
  public String getCheckEmails() {
    return checkEmails;
  }

  public void setCheckEmails(String checkEmails) {
    this.checkEmails = checkEmails;
  }

  /**
   * @param url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  /**
   * @return
   */
  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  /**
   * @param status
   */
  public void setStatus(Long status) {
    this.status = status;
  }

  /**
   * @return
   */
  @Column(name = "NATURE")
  public Long getNature() {
    return nature;
  }

  /**
   * @param nature
   */
  public void setNature(Long nature) {
    this.nature = nature;
  }

  @Column(name = "SERVER_EMAIL")
  public String getServerEmail() {
    return serverEmail;
  }

  public void setServerEmail(String serverEmail) {
    this.serverEmail = serverEmail;
  }

  @Column(name = "SERVER_TEL")
  public String getServerTel() {
    return serverTel;
  }

  public void setServerTel(String serverTel) {
    this.serverTel = serverTel;
  }

  /**
   * @return the otherRolNodes
   */
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
   * @return the nearSnsNodeId
   */
  @Transient
  public Integer getNearSnsNodeId() {
    return nearSnsNodeId;
  }

  /**
   * @param nearSnsNodeId the nearSnsNodeId to set
   */
  public void setNearSnsNodeId(Integer nearSnsNodeId) {
    this.nearSnsNodeId = nearSnsNodeId;
  }

  @Transient
  public String getLogoAddr() {
    return logoAddr;
  }

  public void setLogoAddr(String logoAddr) {
    this.logoAddr = logoAddr;
  }

  @Transient
  public Integer getApplyNameCr() {
    return applyNameCr;
  }

  @Column(name = "POST_CODE")
  public String getPostcode() {
    return postcode;
  }

  @Column(name = "CHECK_CODE")
  public String getCheckCode() {
    return checkCode;
  }

  @Column(name = "IS_CHECKCODE")
  public Integer getIsCheckCode() {
    return isCheckCode;
  }

  @Column(name = "ISIS_ORG_CODE")
  public Integer getIsisOrgCode() {
    return isisOrgCode;
  }

  public void setCheckCode(String checkCode) {
    this.checkCode = checkCode;
  }

  public void setIsCheckCode(Integer isCheckCode) {
    this.isCheckCode = isCheckCode;
  }

  public void setIsisOrgCode(Integer isisOrgCode) {
    this.isisOrgCode = isisOrgCode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public void setApplyNameCr(Integer applyNameCr) {
    this.applyNameCr = applyNameCr;
  }

  @Transient
  public String getZhNameCr() {
    return zhNameCr;
  }

  @Transient
  public String getEnNameCr() {
    return enNameCr;
  }

  public void setZhNameCr(String zhNameCr) {
    this.zhNameCr = zhNameCr;
  }

  public void setEnNameCr(String enNameCr) {
    this.enNameCr = enNameCr;
  }

  /**
   * 目前此方法添加了国际化的支持
   * 
   * @return
   */
  @Transient
  public String getInsName() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    // 如单位名称不为空，则不对单位名称进行赋值_MJG_2013-03-25_ROL-469.
    if (StringUtils.isBlank(insName)) {
      if ("zh".equals(language)) {
        this.insName = StringUtils.isNotBlank(zhName) ? zhName : enName;
      } else {
        this.insName = StringUtils.isNotBlank(enName) ? enName : zhName;
      }
    }
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Transient
  public String getInsDomain() {
    return insDomain;
  }

  public void setInsDomain(String insDomain) {
    this.insDomain = insDomain;
  }

  /**
   * @return the insBrief
   */
  @Transient
  public String getInsBrief() {
    return insBrief;
  }

  /**
   * @param insBrief the insBrief to set
   */
  public void setInsBrief(String insBrief) {
    this.insBrief = insBrief;
  }

  @Transient
  public String getRegionIds() {
    return regionIds;
  }

  public void setRegionIds(String regionIds) {
    this.regionIds = regionIds;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((regionId == null) ? 0 : regionId.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InstitutionRol other = (InstitutionRol) obj;
    if (abbreviation == null) {
      if (other.abbreviation != null)
        return false;
    } else if (!abbreviation.equals(other.abbreviation))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (regionId == null) {
      if (other.regionId != null)
        return false;
    } else if (!regionId.equals(other.regionId))
      return false;
    if (status == null) {
      if (other.status != null)
        return false;
    } else if (!status.equals(other.status))
      return false;
    return true;
  }

}
