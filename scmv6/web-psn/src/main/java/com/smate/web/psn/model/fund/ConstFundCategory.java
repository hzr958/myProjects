package com.smate.web.psn.model.fund;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 基金机构类别.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_CATEGORY")
public class ConstFundCategory implements Serializable {
  private static final long serialVersionUID = 5581604028026966929L;
  private Long id;
  private String des3Id;
  // 机构id，关联const_fund_agency表主键
  private Long agencyId;
  private String agencyDes3Id;
  // 列表显示机构名称(根据当前语言环境显示相应中英文字段)
  private String agencyViewName;
  // 列表显示机构logo
  private String logoUrl;
  // 类别中文名称
  private String nameZh;
  // 类别英文名称
  private String nameEn;
  // 基金类别列表显示名称
  private String categoryViewName;
  // 类别代码
  private String code;
  // 类别缩写
  private String abbr;
  // 类别语言
  private Long language;
  // 类别描述
  private String description;
  // 含有html标签的字段
  private String descriptionHtml;
  // 申报指南网址
  private String guideUrl;
  // 申报网址
  private String declareUrl;
  // 开始日期
  private Date startDate;
  // 截止日期
  private Date endDate;
  // 职称要求and学位要求1(职称)如：高级,中级
  private String titleRequire1;
  // 职称要求and学位要求1(学位)如：学士,博士
  private String degreeRequire1;
  // 职称and学位1与职称and学位2的关系：0表示and，1表示or
  private Integer relationship;
  // 职称要求and学位要求2(职称)如：高级,中级
  private String titleRequire2;
  // 职称要求and学位要求2(学位)如：学士,博士
  private String degreeRequire2;
  // 最佳职称
  private String titleBest;
  // 最佳学位
  private String degreeBest;
  // 出生年月要求-最小
  private Date birthLeast;
  // 出生年月要求-最大
  private Date birthMax;
  // 最佳年龄-最小
  private Integer ageLeast;
  // 最佳年龄-最大
  private Integer ageMax;
  // 联系方式
  private String contact;
  // 老数据：申报条件，参照FUND_CONDITION表中的ID字段，格式为spring el表达式，条件用[]括起
  private String condition;
  // ---------
  private int isOver;
  private Integer starNum;
  // 领域id：多个以分号","分割
  private String disList;
  // 领域冗余[{id:id,name:name}],用于编辑时显示
  private String disJson;
  // 关键词：多个以分号","分割
  private String keywordList;
  // 地区：json数组格式：[{'val':val1,'text':text1},{'val':val2,'text':text2}]
  private String regionList;
  // 附件：json数组格式：[{fileName,filePath},fileName,filePath}],附件保存在bpo
  private String fileList;
  // 资助最小类别中文名称，属于NAME_ZH部分内容
  private String grantNameZh;
  // 资助最小类别英文名称，属于NAME_EN部分内容
  private String grantNameEn;

  // ====================
  // 項目期限
  private String deadline;
  // 资助强度
  private String strength;
  // 单位id，0为bpo管理的基准数据
  private Long insId;
  private String remark;
  private Long psnId;
  private Date createDate;
  private Long year;
  // 审核状态：0通过，1拒绝
  private Integer status = 0;
  // 单位名称冗余
  private String insName;
  private Integer isMatch;// 是否配套 0:不配套 1:配套
  private String percentage;// 比例

  public ConstFundCategory() {
    super();
  }

  public ConstFundCategory(Long id, Long agencyId, String nameZh, String nameEn, Date startDate, Date endDate) {
    super();
    this.id = id;
    this.agencyId = agencyId;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public ConstFundCategory(Long id, Long agencyId, String nameZh, String nameEn, String guideUrl, String declareUrl,
      Date startDate, Date endDate, String condition, Integer starNum) {
    super();
    this.id = id;
    this.agencyId = agencyId;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
    this.guideUrl = guideUrl;
    this.declareUrl = declareUrl;
    this.startDate = startDate;
    this.endDate = endDate;
    this.condition = condition;
    this.starNum = starNum;
  }

  public ConstFundCategory(String nameZh, String nameEn) {
    super();
    this.nameZh = nameZh;
    this.nameEn = nameEn;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_CAT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FUND_AGENCY_ID")
  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Transient
  public String getAgencyViewName() {
    return agencyViewName;
  }

  public void setAgencyViewName(String agencyViewName) {
    this.agencyViewName = agencyViewName;
  }

  @Column(name = "NAME_ZH")
  public String getNameZh() {
    return nameZh;
  }

  public void setNameZh(String nameZh) {
    this.nameZh = nameZh;
  }

  @Column(name = "NAME_EN")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Transient
  public String getCategoryViewName() {
    return categoryViewName;
  }

  public void setCategoryViewName(String categoryViewName) {
    this.categoryViewName = categoryViewName;
  }

  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(name = "ABBR")
  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  @Column(name = "LANGUAGE")
  public Long getLanguage() {
    return language;
  }

  public void setLanguage(Long language) {
    this.language = language;
  }

  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "GUIDE_URL")
  public String getGuideUrl() {
    return guideUrl;
  }

  public void setGuideUrl(String guideUrl) {
    this.guideUrl = guideUrl;
  }

  @Column(name = "DECLARE_URL")
  public String getDeclareUrl() {
    return declareUrl;
  }

  public void setDeclareUrl(String declareUrl) {
    this.declareUrl = declareUrl;
  }

  @Column(name = "START_DATE")
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Column(name = "END_DATE")
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Column(name = "TITLE_REQUIRE1")
  public String getTitleRequire1() {
    return titleRequire1;
  }

  public void setTitleRequire1(String titleRequire1) {
    this.titleRequire1 = titleRequire1;
  }

  @Column(name = "DEGREE_REQUIRE1")
  public String getDegreeRequire1() {
    return degreeRequire1;
  }

  public void setDegreeRequire1(String degreeRequire1) {
    this.degreeRequire1 = degreeRequire1;
  }

  @Column(name = "TITLE_REQUIRE2")
  public String getTitleRequire2() {
    return titleRequire2;
  }

  public void setTitleRequire2(String titleRequire2) {
    this.titleRequire2 = titleRequire2;
  }

  @Column(name = "DEGREE_REQUIRE2")
  public String getDegreeRequire2() {
    return degreeRequire2;
  }

  public void setDegreeRequire2(String degreeRequire2) {
    this.degreeRequire2 = degreeRequire2;
  }

  @Column(name = "TITLE_BEST")
  public String getTitleBest() {
    return titleBest;
  }

  public void setTitleBest(String titleBest) {
    this.titleBest = titleBest;
  }

  @Column(name = "DEGREE_BEST")
  public String getDegreeBest() {
    return degreeBest;
  }

  public void setDegreeBest(String degreeBest) {
    this.degreeBest = degreeBest;
  }

  @Column(name = "BIRTH_LEAST")
  public Date getBirthLeast() {
    return birthLeast;
  }

  public void setBirthLeast(Date birthLeast) {
    this.birthLeast = birthLeast;
  }

  @Column(name = "BIRTH_MAX")
  public Date getBirthMax() {
    return birthMax;
  }

  public void setBirthMax(Date birthMax) {
    this.birthMax = birthMax;
  }

  @Column(name = "AGE_LEAST")
  public Integer getAgeLeast() {
    return ageLeast;
  }

  public void setAgeLeast(Integer ageLeast) {
    this.ageLeast = ageLeast;
  }

  @Column(name = "AGE_MAX")
  public Integer getAgeMax() {
    return ageMax;
  }

  public void setAgeMax(Integer ageMax) {
    this.ageMax = ageMax;
  }

  @Column(name = "CONDITION")
  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  @Column(name = "CONTACT")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  @Column(name = "RELATIONSHIP")
  public Integer getRelationship() {
    return relationship;
  }

  public void setRelationship(Integer relationship) {
    this.relationship = relationship;
  }

  @Column(name = "DEADLINE")
  public String getDeadline() {
    return deadline;
  }

  public void setDeadline(String deadline) {
    this.deadline = deadline;
  }

  @Column(name = "STRENGTH")
  public String getStrength() {
    return strength;
  }

  public void setStrength(String strength) {
    this.strength = strength;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Transient
  public String getRegionList() {
    return regionList;
  }

  public void setRegionList(String regionList) {
    this.regionList = regionList;
  }

  public void setIsOver(int isOver) {
    this.isOver = isOver;
  }

  @Transient
  public Integer getStarNum() {
    return starNum;
  }

  public void setStarNum(Integer starNum) {
    this.starNum = starNum;
  }

  @Transient
  public String getDisList() {
    return disList;
  }

  public void setDisList(String disList) {
    this.disList = disList;
  }

  @Transient
  public String getDisJson() {
    return disJson;
  }

  public void setDisJson(String disJson) {
    this.disJson = disJson;
  }

  @Transient
  public String getKeywordList() {
    return keywordList;
  }

  public void setKeywordList(String keywordList) {
    this.keywordList = keywordList;
  }

  @Transient
  public String getFileList() {
    return fileList;
  }

  public void setFileList(String fileList) {
    this.fileList = fileList;
  }

  @Transient
  public String getDes3Id() {
    if (this.id != null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Transient
  public String getAgencyDes3Id() {
    if (this.agencyId != null) {
      agencyDes3Id = ServiceUtil.encodeToDes3(this.agencyId.toString());
    }
    return agencyDes3Id;
  }

  public void setAgencyDes3Id(String agencyDes3Id) {
    this.agencyDes3Id = agencyDes3Id;
  }

  @Column(name = "GRANT_NAME_ZH")
  public String getGrantNameZh() {
    return grantNameZh;
  }

  @Column(name = "GRANT_NAME_EN")
  public String getGrantNameEn() {
    return grantNameEn;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Column(name = "YEAR")
  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public void setGrantNameZh(String grantNameZh) {
    this.grantNameZh = grantNameZh;
  }

  public void setGrantNameEn(String grantNameEn) {
    this.grantNameEn = grantNameEn;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Transient
  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  @Transient
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "ISMATCH")
  public Integer getIsMatch() {
    return isMatch;
  }

  public void setIsMatch(Integer isMatch) {
    this.isMatch = isMatch;
  }

  @Column(name = "PERCENTAGE")
  public String getPercentage() {
    return percentage;
  }

  public void setPercentage(String percentage) {
    this.percentage = percentage;
  }

  @Transient
  public String getDescriptionHtml() {
    return descriptionHtml;
  }

  public void setDescriptionHtml(String descriptionHtml) {
    this.descriptionHtml = descriptionHtml;
  }
}
