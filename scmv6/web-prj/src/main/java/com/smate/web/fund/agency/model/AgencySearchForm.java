package com.smate.web.fund.agency.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 机构检索功能用form
 * 
 * @author wsn
 *
 */
public class AgencySearchForm implements Serializable {

  private static final long serialVersionUID = -8055429686787185666L;

  private Long insRegion; // 地区检索条件
  private Long insCharacter; // 单位性质,对应Institution的nature字段
  private List<InsInfo> insInfoList; // 单位信息List
  private Page<InsInfo> page = new Page<InsInfo>(); // 分页查询用
  private String searchString; // 查询字符
  private String allFilterValues; // 检索插件传递的检索的参数
  private List<InsSearchRegion> regionList; // 左边地区菜单
  private List<InsSearchCharacter> characterList;// 左边类别菜单
  private Integer createMenu = 1; // 是否创建左边菜单，1：是； 0：不是
  private Long psnId; // 人员ID
  private Long insId; // 机构ID
  private String desInsId; // 加密机构ID
  private Integer hasAward; // 是否已赞过， 1：赞过，0：未赞过
  private Integer status; // 赞操作类型， 1： 赞，0：取消赞
  private String initInsIds; // 需要初始化信息的机构ID， 逗号分隔
  private Integer hasLogin = 0; // 是否已登录， 1:已登陆，0：未登录
  private boolean showRegionMenu = false;// 是否显示地区菜单
  private boolean showCharacterMenu = false; // 是否显示类别菜单
  private String domain;// 域名
  private Integer type;// type 1赞2分享3关注4取消赞5取消关注

  public AgencySearchForm() {
    super();
  }

  public Long getInsRegion() {
    return insRegion;
  }

  public void setInsRegion(Long insRegion) {
    this.insRegion = insRegion;
  }

  public Long getInsCharacter() {
    return insCharacter;
  }

  public void setInsCharacter(Long insCharacter) {
    this.insCharacter = insCharacter;
  }

  public List<InsInfo> getInsInfoList() {
    return insInfoList;
  }

  public void setInsInfoList(List<InsInfo> insInfoList) {
    this.insInfoList = insInfoList;
  }

  public Page<InsInfo> getPage() {
    return page;
  }

  public void setPage(Page<InsInfo> page) {
    this.page = page;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String queryStr) {
    this.searchString = queryStr;
  }

  public String getAllFilterValues() {
    return allFilterValues;
  }

  public void setAllFilterValues(String allFilterValues) {
    this.allFilterValues = allFilterValues;
  }

  public List<InsSearchRegion> getRegionList() {
    return regionList;
  }

  public void setRegionList(List<InsSearchRegion> regionList) {
    this.regionList = regionList;
  }

  public List<InsSearchCharacter> getCharacterList() {
    return characterList;
  }

  public void setCharacterList(List<InsSearchCharacter> characterList) {
    this.characterList = characterList;
  }

  public Integer getCreateMenu() {
    return createMenu;
  }

  public void setCreateMenu(Integer createMenu) {
    this.createMenu = createMenu;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getInsId() {
    if (this.insId == null && StringUtils.isNotBlank(this.desInsId)) {
      insId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.desInsId));
    }
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getDesInsId() {
    return desInsId;
  }

  public void setDesInsId(String desInsId) {
    this.desInsId = desInsId;
  }

  public Integer getHasAward() {
    return hasAward;
  }

  public void setHasAward(Integer hasAward) {
    this.hasAward = hasAward;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getInitInsIds() {
    return initInsIds;
  }

  public void setInitInsIds(String initInsIds) {
    this.initInsIds = initInsIds;
  }

  public Integer getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(Integer hasLogin) {
    this.hasLogin = hasLogin;
  }

  public boolean getShowRegionMenu() {
    return showRegionMenu;
  }

  public void setShowRegionMenu(boolean showRegionMenu) {
    this.showRegionMenu = showRegionMenu;
  }

  public boolean getShowCharacterMenu() {
    return showCharacterMenu;
  }

  public void setShowCharacterMenu(boolean showCharacterMenu) {
    this.showCharacterMenu = showCharacterMenu;
  }

  @Override
  public String toString() {
    return "insRegion = " + this.getInsRegion() + ", insCharacter = " + this.getInsCharacter() + ", queryStr = "
        + this.getSearchString() + ", allFilterValues=" + this.getAllFilterValues();
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

}
