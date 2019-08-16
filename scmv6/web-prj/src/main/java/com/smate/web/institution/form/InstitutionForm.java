package com.smate.web.institution.form;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.institution.model.ConstClassifyCsei;
import com.smate.web.institution.model.ConstClassifyEconomic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aijiangbin
 * @create 2019-07-02 9:39
 **/
public class InstitutionForm {

  private Long psnId = 0L; // 人员id

  private String economicIds =""; // 经济行业ID拼接字符串
  private Map<String, Object> economicMap = new HashMap<>(); // 用行业或者产业构建成的map

  private List<ConstClassifyEconomic> economicList = new ArrayList<>(); //  选中的
  private List<ConstClassifyCsei>  cseiList = new ArrayList<>(); //  选中的

  private Integer  checkInsNameType = 1  ; // 检查单位的类型  1校验单位名是否存在  ； 2校验域名是否存在
  private String   nature = ""; // 单位
  private String   insDomain = ""; // 域名
  private String   insName = "";  // 机构名称
  private String   url = ""; //   机构网站 ，单位域名
  private String   ecoCode = ""; //  多个行业代码英文逗号分隔
  private String   cseiCode = ""; //  多个产业代码英文逗号分隔
  private String   description = ""; //描述
  private String   countryId= ""; // 国家region_id
  private String   prvId= ""; //身份region_id
  private String   cyId= ""; //城市region_id
  private String   disId= ""; //区域region_id
  private String   psn_name= ""; //人名
  private String   forwardUrl= ""; // 跳转url

  private Long  insId = 0L ; //
  private String  des3InsId ="" ; //

  private Integer  insPageType = 1  ;  //机构主页类型  1= 我的 ； 2我关注的
  List<InstitutionInfo>  listInfo = new ArrayList<>();

  private Integer item = 1  ; //  1= 我的主页  2 关注主页

  public Integer getItem() {
    return item;
  }

  public void setItem(Integer item) {
    this.item = item;
  }

  public Long getInsId() {
    if(StringUtils.isNotBlank(des3InsId)){
      insId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3InsId));
    }
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getDes3InsId() {
    return des3InsId;
  }

  public void setDes3InsId(String des3InsId) {
    this.des3InsId = des3InsId;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public List<InstitutionInfo> getListInfo() {
    return listInfo;
  }

  public void setListInfo(List<InstitutionInfo> listInfo) {
    this.listInfo = listInfo;
  }

  public Integer getInsPageType() {
    return insPageType;
  }

  public void setInsPageType(Integer insPageType) {
    this.insPageType = insPageType;
  }

  public String getCseiCode() {
    return cseiCode;
  }

  public void setCseiCode(String cseiCode) {
    this.cseiCode = cseiCode;
  }

  public String getNature() {
    return nature;
  }

  public void setNature(String nature) {
    this.nature = nature;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEcoCode() {
    return ecoCode;
  }

  public void setEcoCode(String ecoCode) {
    this.ecoCode = ecoCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCountryId() {
    return countryId;
  }

  public void setCountryId(String countryId) {
    this.countryId = countryId;
  }

  public String getPrvId() {
    return prvId;
  }

  public void setPrvId(String prvId) {
    this.prvId = prvId;
  }

  public String getCyId() {
    return cyId;
  }

  public void setCyId(String cyId) {
    this.cyId = cyId;
  }

  public String getDisId() {
    return disId;
  }

  public void setDisId(String disId) {
    this.disId = disId;
  }

  public String getPsn_name() {
    return psn_name;
  }

  public void setPsn_name(String psn_name) {
    this.psn_name = psn_name;
  }

  public Integer getCheckInsNameType() {
    return checkInsNameType;
  }

  public void setCheckInsNameType(Integer checkInsNameType) {
    this.checkInsNameType = checkInsNameType;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getInsDomain() {
    if(StringUtils.isNotBlank(insDomain)){
      if(insDomain.startsWith("https://") || insDomain.startsWith("http://")){
        insDomain = insDomain.replace("https://","");
        insDomain = insDomain.replace("http://","");
      }
    }
    return insDomain;
  }

  public void setInsDomain(String insDomain) {
    this.insDomain = insDomain;
  }

  public List<ConstClassifyCsei> getCseiList() {
    return cseiList;
  }

  public void setCseiList(List<ConstClassifyCsei> cseiList) {
    this.cseiList = cseiList;
  }

  public Map<String, Object> getEconomicMap() {
    return economicMap;
  }

  public void setEconomicMap(Map<String, Object> economicMap) {
    this.economicMap = economicMap;
  }

  public List<ConstClassifyEconomic> getEconomicList() {
    return economicList;
  }

  public void setEconomicList(List<ConstClassifyEconomic> economicList) {
    this.economicList = economicList;
  }

  public String getEconomicIds() {
    return economicIds;
  }

  public void setEconomicIds(String economicIds) {
    this.economicIds = economicIds;
  }

  public Long getPsnId() {
    if(NumberUtils.isNullOrZero(psnId)){
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }
}
