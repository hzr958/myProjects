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

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 基金机构.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "CONST_FUND_AGENCY")
public class ConstFundAgency implements Serializable {
  private static final long serialVersionUID = 252817680213415072L;

  private Long id;
  private String des3Id;
  // 机构中文名称
  private String nameZh;
  // 机构英文名称
  private String nameEn;
  // 显示名称
  private String nameView;
  // 机构标识
  private Integer flag;
  // 机构类别CONST_FUND_POSITION表id字段
  private Long type;
  // 类别显示名称
  private String typeView;
  // 国家/地区
  private Long regionId;
  // 地市的父级id
  private Long superRegionId;
  // 国家地区名称
  private String regionName;
  // 机构代码
  private String code;
  // 机构缩写
  private String abbr;
  // 地址
  private String address;
  // 机构网址
  private String url;
  // 联系方式
  private String contact;
  // logo图片地址
  private String logoUrl;
  // 单位Id,0为bpo管理的基准数据
  private Long insId;
  private Long psnId;
  private Date createDate;
  // 审核状态：0通过，1拒绝
  private Integer status = 0;
  // 单位名称冗余
  private String insName;
  // 最近使用时间
  private Date useDate;

  public ConstFundAgency() {
    super();
  }

  public ConstFundAgency(Long id, String nameZh, String nameEn, String code) {
    super();
    this.id = id;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
    this.code = code;
  }

  public ConstFundAgency(Long regionId, String nameZh, String nameEn) {
    super();
    this.regionId = regionId;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
  }

  public ConstFundAgency(String nameZh, String nameEn, String logoUrl, Long regionId) {
    super();
    this.regionId = regionId;
    this.nameZh = nameZh;
    this.nameEn = nameEn;
    this.logoUrl = logoUrl;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_AGENCY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @Column(name = "FLAG")
  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  @Column(name = "TYPE")
  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
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

  @Column(name = "ADDRESS")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "CONTACT")
  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  @Column(name = "LOGO_URL")
  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
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
  public String getNameView() {
    nameView = StringUtils.isBlank(this.nameZh) ? this.nameEn : this.nameZh;
    return nameView;
  }

  public void setNameView(String nameView) {
    this.nameView = nameView;
  }

  @Transient
  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  @Transient
  public Long getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(Long superRegionId) {
    this.superRegionId = superRegionId;
  }

  @Transient
  public String getTypeView() {
    return typeView;
  }

  public void setTypeView(String typeView) {
    this.typeView = typeView;
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
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "USE_DATE")
  public Date getUseDate() {
    return useDate;
  }

  public void setUseDate(Date useDate) {
    this.useDate = useDate;
  }
}
