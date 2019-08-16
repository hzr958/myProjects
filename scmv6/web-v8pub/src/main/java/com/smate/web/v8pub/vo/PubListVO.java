package com.smate.web.v8pub.vo;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.service.query.PubQueryDTO;

/**
 * 成果列表视图显示对象
 * 
 * @author aijiangbin
 * @date 2018年8月7日
 */
public class PubListVO {

  public Page<PubInfo> page = new Page<>();

  // 个人psnid
  private Long psnId;
  private String des3PsnId; // 人员加密串
  private String self;// 是否是本人 "yes":是 "no":不是

  private String run_env = "dev";

  private String searchKey = "";

  public Long cnfId;

  private String snsDomain = "dev";
  private List<ConstPubType> pubTypes;

  public List<PubInfo> resultList;

  public Integer totalCount = 0;
  private Integer citedSum = 0; // 成果引用总数
  private Integer hIndex = 0; // H-index指数
  private Integer ConfirmCount = 0;
  private boolean hasPrivatePub; // 是否有隐私的成果
  private boolean hasPubfulltextConfirm = false; // 是否有成果全文认领

  // 下面三个Map对象只用第一次加载数据时将左侧分类信息进行显示
  private Map<Long, Long> yearMap;// 年份
  private Map<Long, Long> pubTypeMap;// 成果类型分类
  private Map<String, Long> languageMap;// 语言类型分类
  private Integer nowYear = null;
  private boolean searchMenu = false;// 是否查询分类菜单

  public Integer getNowYear() {
    if (nowYear == null) {
      nowYear = Calendar.getInstance().get(Calendar.YEAR);
    }
    return nowYear;
  }

  public void setNowYear(Integer nowYear) {
    this.nowYear = nowYear;
  }

  public boolean isHasPubfulltextConfirm() {
    return hasPubfulltextConfirm;
  }

  public void setHasPubfulltextConfirm(boolean hasPubfulltextConfirm) {
    this.hasPubfulltextConfirm = hasPubfulltextConfirm;
  }

  /**
   * 成果查询是传输的参数
   */
  private PubQueryDTO pubQueryDTO = new PubQueryDTO();

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      if (StringUtils.isNotEmpty(this.des3PsnId)) {
        return Long.valueOf(ServiceUtil.decodeFromDes3(this.des3PsnId));
      } else {
        psnId = SecurityUtils.getCurrentUserId();
      }
    }
    return psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getRun_env() {
    return run_env;
  }

  public void setRun_env(String run_env) {
    this.run_env = run_env;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }

  public PubQueryDTO getPubQueryDTO() {
    return pubQueryDTO;
  }

  public void setPubQueryDTO(PubQueryDTO pubQueryDTO) {
    this.pubQueryDTO = pubQueryDTO;
  }

  public List<PubInfo> getResultList() {
    return resultList;
  }

  public void setResultList(List<PubInfo> resultList) {
    this.resultList = resultList;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Page<PubInfo> getPage() {
    return page;
  }

  public void setPage(Page<PubInfo> page) {
    this.page = page;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getCnfId() {
    return cnfId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public Integer gethIndex() {
    return hIndex;
  }

  public void sethIndex(Integer hIndex) {
    this.hIndex = hIndex;
  }

  public Integer getCitedSum() {
    return citedSum;
  }

  public void setCitedSum(Integer citedSum) {
    this.citedSum = citedSum;
  }

  public Integer getConfirmCount() {
    return ConfirmCount;
  }

  public void setConfirmCount(Integer confirmCount) {
    ConfirmCount = confirmCount;
  }

  public boolean isHasPrivatePub() {
    return hasPrivatePub;
  }

  public void setHasPrivatePub(boolean hasPrivatePub) {
    this.hasPrivatePub = hasPrivatePub;
  }

  public List<ConstPubType> getPubTypes() {
    return pubTypes;
  }

  public void setPubTypes(List<ConstPubType> pubTypes) {
    this.pubTypes = pubTypes;
  }

  public Map<Long, Long> getYearMap() {
    return yearMap;
  }

  public void setYearMap(Map<Long, Long> yearMap) {
    this.yearMap = yearMap;
  }

  public Map<Long, Long> getPubTypeMap() {
    return pubTypeMap;
  }

  public void setPubTypeMap(Map<Long, Long> pubTypeMap) {
    this.pubTypeMap = pubTypeMap;
  }

  public Map<String, Long> getLanguageMap() {
    return languageMap;
  }

  public void setLanguageMap(Map<String, Long> languageMap) {
    this.languageMap = languageMap;
  }

  public boolean isSearchMenu() {
    return searchMenu;
  }

  public void setSearchMenu(boolean searchMenu) {
    this.searchMenu = searchMenu;
  }


}
