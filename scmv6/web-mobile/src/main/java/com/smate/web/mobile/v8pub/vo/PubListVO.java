package com.smate.web.mobile.v8pub.vo;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.mobile.v8pub.dto.PubQueryDTO;

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
  private String des3SearchPsnId;
  private String self;// 是否是本人 "yes":是 "no":不是
  private Boolean other;// 是否是他人 true 是

  private String run_env = "dev";

  private String searchKey = "";

  public Long cnfId;

  private String snsDomain = "dev";

  public List<PubInfo> resultList;

  public Integer totalCount = 0;
  private Integer citedSum = 0; // 成果引用总数
  private Integer hIndex = 0; // H-index指数
  private Integer ConfirmCount = 0;
  private boolean hasPrivatePub; // 是否有隐私的成果

  private String fromPage = "psn"; // 来源页面 dyn , psn, otherpsn ,
  // mobileConfirmPub:来源移动端-消息中心-待确认成果页
  private Integer hasLogin = 0; // 是否已登录，1：是，0：否
  // 他人成果页面需要的他人名字
  private String psnName;
  /**
   * 排序的字段 citations:引用次数 title：标题 publishDate：发表日期 gmtModified ： 更新时间 下面是群组排序说明： createDate ： 创建时间
   * 默认排序 publishDate ： 最新发表 citations ： 最多引用排序
   */
  public String orderBy = "";

  /**
   * 成果类型 多个类型逗号隔离 例如： 4,3,5
   */
  public String pubType = "";
  private Integer articleType = 1; // 查询类型 1 成果 , 2 文献
  private String pubLocale = "zh_cn,en_us"; // zh_cn en_us,
  private String restfulUrl; // RESTful接口的url
  private Long currentLoginPsnId; // 当前登录的人员ID

  public Integer getArticleType() {
    return articleType;
  }

  public Boolean getOther() {
    return other;
  }

  public void setOther(Boolean other) {
    this.other = other;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public String getPubLocale() {
    return pubLocale;
  }

  public void setPubLocale(String pubLocale) {
    this.pubLocale = pubLocale;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getFromPage() {
    return fromPage;
  }

  public void setFromPage(String fromPage) {
    this.fromPage = fromPage;
  }

  public Integer getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(Integer hasLogin) {
    this.hasLogin = hasLogin;
  }

  /**
   * 成果查询是传输的参数
   */
  private PubQueryDTO pubQueryDTO = new PubQueryDTO();

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      if (StringUtils.isNotEmpty(this.des3PsnId)) {
        return Long.valueOf(ServiceUtil.decodeFromDes3(this.des3PsnId));
      }
    } else {
      psnId = SecurityUtils.getCurrentUserId();
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

  public String getRestfulUrl() {
    return restfulUrl;
  }

  public void setRestfulUrl(String restfulUrl) {
    this.restfulUrl = restfulUrl;
  }

  public Long getCurrentLoginPsnId() {
    return currentLoginPsnId;
  }

  public void setCurrentLoginPsnId(Long currentLoginPsnId) {
    this.currentLoginPsnId = currentLoginPsnId;
  }

  public String getDes3SearchPsnId() {
    return des3SearchPsnId;
  }

  public void setDes3SearchPsnId(String des3SearchPsnId) {
    this.des3SearchPsnId = des3SearchPsnId;
  }

}
