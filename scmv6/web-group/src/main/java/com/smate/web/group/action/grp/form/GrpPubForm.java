package com.smate.web.group.action.grp.form;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * 群组成果form
 * 
 * @author tsz
 */
public class GrpPubForm {

  private Long psnId;// 当前人psnId
  private String des3PsnId;// 加密的psnId
  private Long grpId;// 群组ID
  private String des3GrpId;
  private Long pubId;// 群组成果id
  private String pubIds; // 多个成果id 用逗号分隔

  private Integer role;// 群组角色权限[1=创建人,2=管理员, 3=组员]

  // 查询条件
  private String searchKey; // 成果列表检索关键字
  private Integer showPrjPub = 0; // 显示项目成果 1 是显示
  private Integer showRefPub = 0; // 显示 文献成果 1显示
  private String publishYear; // 发表年份 如果是多年用逗号隔开
  private String pubType; // 成果类型 如果是多个就用逗号隔开 用常量数字
  private String includeType;// 收录类别
  // 排序
  private String orderBy = "createDate"; // createDate-最新添加,publishYear-最近发表,citedTimes-引用次数

  private Page<GrpPubShowInfo> page = new Page<GrpPubShowInfo>();

  private List<GrpPubInfoVO> grpPubInfoVOs = new ArrayList<GrpPubInfoVO>();

  private String markType; // 标记类型 1,标记为项目成果 2.标记为文献
  private Map<String, String> resultMap;
  private Map<String, Object> result2Map;
  private String isPsnPubs;// 请求的是否是个人成果 1=个人成果
  private String self;// 是否是本人 "yes":是 "no":不是
  private Integer grpCategory; // 群组类别 群组类别 [ 10=课程群组 ， 11项目群组 ， 12兴趣群组]
  private boolean isRadio = false;
  private String module; // projectPub==项目成果 ， projectRef =项目文献
  private Integer savePubType = 0;// 0==群组文献， 1==群组项目成果 ，2==群组项目文献
  private String des3MemberId;
  private Long memberId;

  private Integer curentYear  ;   //当前年
  private Integer lastYear ;   //上一年
  private String  recentYear5 ="";  // 近5年
  private String  recentYear10="" ;

  public Integer getCurentYear() {
    if(curentYear == null || curentYear==0){
      Calendar c = Calendar.getInstance();
      curentYear = c.get(Calendar.YEAR);
    }
    return curentYear;
  }

  public void setCurentYear(Integer curentYear) {
    this.curentYear = curentYear;
  }

  public Integer getLastYear() {
    if(lastYear == null || lastYear == 0){
      lastYear = getCurentYear() -1;
    }
    return lastYear;
  }

  public void setLastYear(Integer lastYear) {
    this.lastYear = lastYear;
  }

  public String getRecentYear5() {
    if(StringUtils.isBlank(recentYear5)){
      for(int i = 0 ; i<5 ; i++){
        recentYear5 = recentYear5+ ","+(getCurentYear()-i);
      }
      recentYear5 = recentYear5.substring(1);
    }
    return recentYear5;
  }

  public void setRecentYear5(String recentYear5) {
    this.recentYear5 = recentYear5;
  }

  public String getRecentYear10() {
    if(StringUtils.isBlank(recentYear10)){
      for(int i = 0 ; i<10 ; i++){
        recentYear10 = recentYear10+ ","+(getCurentYear()-i);
      }
      recentYear10 = recentYear10.substring(1);
    }
    return recentYear10;
  }

  public void setRecentYear10(String recentYear10) {
    this.recentYear10 = recentYear10;
  }

  public boolean getIsRadio() {
    return isRadio;
  }

  public void setIsRadio(boolean isRadio) {
    this.isRadio = isRadio;
  }

  public Long getGrpId() {
    if (this.grpId == null && StringUtils.isNotBlank(this.des3GrpId)) {
      this.grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3GrpId));
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3GrpId() {
    if (StringUtils.isBlank(this.des3GrpId) && this.grpId != null) {
      this.des3GrpId = Des3Utils.encodeToDes3(this.grpId.toString());
    }
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getPsnId() {
    if (psnId == null && StringUtils.isNotBlank(des3PsnId)) {
      String str = ServiceUtil.decodeFromDes3(des3PsnId);
      if (StringUtils.isNotBlank(str)) {
        psnId = Long.parseLong(str);
      }
    }
    if (psnId == null) {
      psnId = SecurityUtils.getCurrentUserId();
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

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public String getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(String publishYear) {
    this.publishYear = publishYear;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getIncludeType() {
    return includeType;
  }

  public void setIncludeType(String includeType) {
    this.includeType = includeType;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Page<GrpPubShowInfo> getPage() {
    return page;
  }

  public void setPage(Page<GrpPubShowInfo> page) {
    this.page = page;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public String getMarkType() {
    return markType;
  }

  public void setMarkType(String markType) {
    this.markType = markType;
  }

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public Integer getShowPrjPub() {
    if (showPrjPub == null) {
      showPrjPub = 0;// 默认值
    }
    return showPrjPub;
  }

  public void setShowPrjPub(Integer showPrjPub) {
    this.showPrjPub = showPrjPub;
  }

  public Integer getShowRefPub() {
    if (showRefPub == null) {
      showRefPub = 0;// 默认值
    }
    return showRefPub;
  }

  public void setShowRefPub(Integer showRefPub) {
    this.showRefPub = showRefPub;
  }

  @Override
  public String toString() {
    return "GrpPubForm [psnId=" + psnId + ", grpId=" + grpId + ", pubId=" + pubId + ", pubIds=" + pubIds + ", role="
        + role + ", searchKey=" + searchKey + ", showPrjPub=" + showPrjPub + ", showRefPub=" + showRefPub
        + ", publishYear=" + publishYear + ", pubType=" + pubType + ", includeType=" + includeType + ", orderBy="
        + orderBy + ", page=" + page + ", markType=" + markType + "]";
  }

  public Map<String, String> getResultMap() {
    if (resultMap == null) {
      resultMap = new HashMap<String, String>();
    }
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

  public String getIsPsnPubs() {
    return isPsnPubs;
  }

  public void setIsPsnPubs(String isPsnPubs) {
    this.isPsnPubs = isPsnPubs;
  }

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public Map<String, Object> getResult2Map() {
    return result2Map;
  }

  public void setResult2Map(Map<String, Object> result2Map) {
    this.result2Map = result2Map;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public Integer getSavePubType() {
    return savePubType;
  }

  public void setSavePubType(Integer savePubType) {
    this.savePubType = savePubType;
  }

  public String getDes3MemberId() {
    return des3MemberId;
  }

  public void setDes3MemberId(String des3MemberId) {
    this.des3MemberId = des3MemberId;
  }

  public Long getMemberId() {
    if (this.memberId == null && StringUtils.isNotBlank(this.des3MemberId)) {
      this.memberId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3MemberId));
    }
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public List<GrpPubInfoVO> getGrpPubInfoVOs() {
    return grpPubInfoVOs;
  }

  public void setGrpPubInfoVOs(List<GrpPubInfoVO> grpPubInfoVOs) {
    this.grpPubInfoVOs = grpPubInfoVOs;
  }


}
