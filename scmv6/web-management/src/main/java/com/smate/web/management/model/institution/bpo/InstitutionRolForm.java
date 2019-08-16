package com.smate.web.management.model.institution.bpo;

import java.util.List;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.management.model.institution.rol.ConstCnCity;
import com.smate.web.management.model.institution.rol.ConstCnProvince;

/**
 * 单位信息维护web类.
 * 
 * @author lqh
 * 
 */
public class InstitutionRolForm {

  /**
   * 
   */
  private static final long serialVersionUID = -4043783308170755250L;

  private String insName;
  private Long insId;
  private String des3InsId;
  private String insDomain;
  private String insDomainSuffix;
  // 中文名
  private String zhName;
  // 英文名
  private String enName;
  // 中文名(申请修改)
  private String zhNameCr;
  // 英文名(申请修改)
  private String enNameCr;
  // 状态
  private Long status;
  // 中文地址
  private String zhAddress;
  // 英文地址
  private String enAddress;
  // 单位地址
  private String address;
  // 联系电话
  private String tel;
  // 单位网址
  private String url;
  // 地区主键
  private Long regionId;
  // logo文件地址
  private String logoAddr;
  // 服务电话
  private String serverTel;
  // 服务邮件
  private String serverEmail;
  // 联系人
  private String contactPerson;
  // 加载国家或地区、省份、城市JSON
  private String countyJson;
  private String provinceJson;
  // 省号
  private Long provinceId;
  private String cityJson;
  private String actionMsg;
  private Integer applyNameCr;
  private String regionIds;
  private Long proviceId;
  private Long cityId;
  private String cityName;

  // 剪切logo时使用
  private Integer logoX;
  private Integer logoY;
  private Integer logoW;
  private Integer logoH;

  // 经度
  private String longitude;
  // 纬度
  private String latitude;
  // 是否开通报表统计功能
  private int stat;
  // 是否开通报表对比功能
  private int cons;

  // 邮编
  private String postcode;
  // 机构简介
  private String insBrief;
  // 机构来源，0在线注册/1邀请加入/2ISIS关联开通/3ISIS同步数据
  private Integer insSource;
  // 修改单位备注信息总数
  private Long insEditRemarkCount;

  private String jsonParam;

  private List<ConstCnProvince> provinceList;

  private List<ConstCnCity> cityList;
  // 单位性质 1: 高校; 2: 研究中心; 3: 资助机构 4:企业 ，5：出版社 ，6 协会 99: 其它
  private Long nature;
  private List<ConstDictionary> natureList;
  private String remark;

  public InstitutionRolForm() {
    super();
  }

  public String getServerTel() {
    return serverTel;
  }

  public void setServerTel(String serverTel) {
    this.serverTel = serverTel;
  }

  public String getServerEmail() {
    return serverEmail;
  }

  public void setServerEmail(String serverEmail) {
    this.serverEmail = serverEmail;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  /**
   * @return the insName
   */
  public String getInsName() {
    return insName;
  }

  /**
   * @param insName the insName to set
   */
  public void setInsName(String insName) {
    this.insName = insName;
  }

  /**
   * @return the insId
   */
  public Long getInsId() {
    return insId;
  }

  public String getRegionIds() {
    return regionIds;
  }

  public void setRegionIds(String regionIds) {
    this.regionIds = regionIds;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public String getZhAddress() {
    return zhAddress;
  }

  public void setZhAddress(String zhAddress) {
    this.zhAddress = zhAddress;
  }

  public String getEnAddress() {
    return enAddress;
  }

  public void setEnAddress(String enAddress) {
    this.enAddress = enAddress;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getLogoAddr() {
    return logoAddr;
  }

  public void setLogoAddr(String logoAddr) {
    this.logoAddr = logoAddr;
  }

  public String getProvinceJson() {
    return provinceJson;
  }

  public void setProvinceJson(String provinceJson) {
    this.provinceJson = provinceJson;
  }

  public String getActionMsg() {
    return actionMsg;
  }

  public void setActionMsg(String actionMsg) {
    this.actionMsg = actionMsg;
  }

  public String getCountyJson() {
    return countyJson;
  }

  public String getCityJson() {
    return cityJson;
  }

  public void setCountyJson(String countyJson) {
    this.countyJson = countyJson;
  }

  public void setCityJson(String cityJson) {
    this.cityJson = cityJson;
  }

  public Integer getApplyNameCr() {
    return applyNameCr;
  }

  public void setApplyNameCr(Integer applyNameCr) {
    this.applyNameCr = applyNameCr;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getZhNameCr() {
    return zhNameCr;
  }

  public String getEnNameCr() {
    return enNameCr;
  }

  public void setZhNameCr(String zhNameCr) {
    this.zhNameCr = zhNameCr;
  }

  public void setEnNameCr(String enNameCr) {
    this.enNameCr = enNameCr;
  }

  public String getDes3InsId() {
    if (this.insId != null && des3InsId == null) {
      des3InsId = ServiceUtil.encodeToDes3(this.insId.toString());
    }
    return des3InsId;
  }

  public void setDes3InsId(String des3InsId) {
    this.des3InsId = des3InsId;
  }

  public Integer getLogoX() {
    return logoX;
  }

  public Integer getLogoY() {
    return logoY;
  }

  public Integer getLogoW() {
    return logoW;
  }

  public Integer getLogoH() {
    return logoH;
  }

  public void setLogoX(Integer logoX) {
    this.logoX = logoX;
  }

  public void setLogoY(Integer logoY) {
    this.logoY = logoY;
  }

  public void setLogoW(Integer logoW) {
    this.logoW = logoW;
  }

  public void setLogoH(Integer logoH) {
    this.logoH = logoH;
  }

  /**
   * @return the insBrief
   */
  public String getInsBrief() {
    return insBrief;
  }

  /**
   * @param insBrief the insBrief to set
   */
  public void setInsBrief(String insBrief) {
    this.insBrief = insBrief;
  }

  public Integer getInsSource() {
    return insSource;
  }

  public void setInsSource(Integer insSource) {
    this.insSource = insSource;
  }

  public List<ConstCnProvince> getProvinceList() {
    return provinceList;
  }

  public void setProvinceList(List<ConstCnProvince> provinceList) {
    this.provinceList = provinceList;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public int getStat() {
    return stat;
  }

  public void setStat(int stat) {
    this.stat = stat;
  }

  public int getCons() {
    return cons;
  }

  public void setCons(int cons) {
    this.cons = cons;
  }

  public Long getProviceId() {
    return proviceId;
  }

  public void setProviceId(Long proviceId) {
    this.proviceId = proviceId;
  }

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public String getInsDomain() {
    return insDomain;
  }

  public void setInsDomain(String insDomain) {
    this.insDomain = insDomain;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Long getInsEditRemarkCount() {
    return insEditRemarkCount;
  }

  public void setInsEditRemarkCount(Long insEditRemarkCount) {
    this.insEditRemarkCount = insEditRemarkCount;
  }

  public String getJsonParam() {
    return jsonParam;
  }

  public void setJsonParam(String jsonParam) {
    this.jsonParam = jsonParam;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getInsDomainSuffix() {
    return insDomainSuffix;
  }

  public void setInsDomainSuffix(String insDomainSuffix) {
    this.insDomainSuffix = insDomainSuffix;
  }

  public List<ConstCnCity> getCityList() {
    return cityList;
  }

  public void setCityList(List<ConstCnCity> cityList) {
    this.cityList = cityList;
  }

  public List<ConstDictionary> getNatureList() {
    return natureList;
  }

  public void setNatureList(List<ConstDictionary> natureList) {
    this.natureList = natureList;
  }

  public Long getNature() {
    return nature;
  }

  public void setNature(Long nature) {
    this.nature = nature;
  }

  public Long getProvinceId() {
    return provinceId;
  }

  public void setProvinceId(Long provinceId) {
    this.provinceId = provinceId;
  }

}
