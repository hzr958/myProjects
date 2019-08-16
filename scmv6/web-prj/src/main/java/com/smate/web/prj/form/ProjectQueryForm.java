package com.smate.web.prj.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 
 * 项目列表
 * 
 * @author zx
 *
 */
public class ProjectQueryForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4164333030779935093L;
  /**
   * 发表年份(开始).
   */
  private Integer fromYear;
  /**
   * 当前的加密psnId
   */
  private String des3CurrentId;
  /**
   * 发表年份(结束).
   */
  private Integer toYear;
  /**
   * 所属收藏夹.
   */
  private Long folderId;
  /**
   * 收藏夹列表.
   */
  private String folderIds;
  private String actionMsg;

  /**
   * 项目列表.
   */
  private String prjIds;
  private Long prjId;
  private String des3PrjId;
  private String des3Id;

  // 资助机构名称
  private List<String> agencyName;

  // 接受前端传来的资助机构名称
  private String agency;

  // 用于年份解析后的资助机构名称
  private List<String> agencyYear;
  // 用于回调函数用的机构名称
  private List<String> agencyCallback;
  // 全部资助机构列表名称
  private List<String> allAgencyName;
  private List<Map<String, Object>> agencyNameList;// 资助机构名称统计信息
  private String forwardUrl;
  private String ownerUrl;
  private String isBack;
  private String leftSearch;
  private String orderBy;

  // 文件夹
  // list=List<PrjFolder>,notClassifiedCount=int
  private Map prjFolderMap;
  // 年份
  // list=List<Integer>,notClassifiedCount=int
  private Map yearMap;

  // 是否是从检索页面进入或者返回
  private Integer isSearch;
  // 仅列出导入的项目:0/1
  private Integer onlyImports;
  private Long searchId;
  // 左边快速浏览名称：如：categeroy,publishYear等
  private Integer publishYear;
  private String searchName;
  private String divOpenList;
  private Date nowDate;
  private String exportType;
  // 弹出项目列表
  private String psnIds;
  private String actionOp;
  // 项目年度
  private Integer fundingYear;
  private Long groupId;

  private String folderImgSrc;
  private String yearImgSrc;
  private String groupImgSrc;
  private Date from;
  private Date to;
  /**
   * 检索内容
   */
  private String searchKey;
  private String strSearchKey;
  private int flag = 0;
  private String inputTip;
  private int prjTotalCount = 0;
  private boolean outside = false;// 是否是站外访问
  private boolean othersSee = false;// 他人查看
  private boolean noPrjFulltextList = false;// 未上传全文的列表
  // 是否显示项目-群组标识
  private boolean showPrjGroup = false;
  private String searchDes3PsnId;
  private Long searchPsnId = 0L;

  // 站外人员ID
  private String outsideDes3PsnId;
  private Integer fulltextCount = 0;// 已上传全文数

  private Integer currentYear;// 当前年份
  private boolean isVIP;

  public String getDes3PrjId() {
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public Integer getFulltextCount() {
    return fulltextCount;
  }

  public void setFulltextCount(Integer fulltextCount) {
    this.fulltextCount = fulltextCount;
  }

  public String getPsnIds() {
    return psnIds;
  }

  public String getActionOp() {
    return actionOp;
  }

  public void setPsnIds(String psnIds) {
    this.psnIds = psnIds;
  }

  public void setActionOp(String actionOp) {
    this.actionOp = actionOp;
  }

  public String getExportType() {
    return exportType;
  }

  public void setExportType(String exportType) {
    this.exportType = exportType;
  }

  public Date getNowDate() {
    return nowDate;
  }

  public void setNowDate(Date nowDate) {
    this.nowDate = nowDate;
  }

  public String getDivOpenList() {
    return divOpenList;
  }

  public void setDivOpenList(String divOpenList) {
    this.divOpenList = divOpenList;
  }

  public Long getSearchId() {
    return searchId;
  }

  public String getSearchName() {
    return searchName;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public void setSearchId(Long searchId) {
    this.searchId = searchId;
  }

  /**
   * @return the fromYear
   */
  public Integer getFromYear() {
    return fromYear;
  }

  /**
   * @param fromYear the fromYear to set
   */
  public void setFromYear(Integer fromYear) {
    this.fromYear = fromYear;
  }

  public String getLeftSearch() {
    return leftSearch;
  }

  public void setLeftSearch(String leftSearch) {
    this.leftSearch = leftSearch;
  }

  /**
   * @return the toYear
   */
  public Integer getToYear() {
    return toYear;
  }

  public String getOwnerUrl() {
    return ownerUrl;
  }

  public void setOwnerUrl(String ownerUrl) {
    this.ownerUrl = ownerUrl;
  }

  /**
   * @param toYear the toYear to set
   */
  public void setToYear(Integer toYear) {
    this.toYear = toYear;
  }

  /**
   * @return the folderId
   */
  public Long getFolderId() {
    return folderId;
  }

  /**
   * @param folderId the folderId to set
   */
  public void setFolderId(Long folderId) {
    this.folderId = folderId;
  }

  public String getFolderIds() {
    return folderIds;
  }

  public void setFolderIds(String folderIds) {
    this.folderIds = folderIds;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public String getActionMsg() {
    return actionMsg;
  }

  public void setActionMsg(String actionMsg) {
    this.actionMsg = actionMsg;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getIsBack() {
    return isBack;
  }

  public Integer getIsSearch() {
    return isSearch;
  }

  public void setIsBack(String isBack) {
    this.isBack = isBack;
  }

  public void setIsSearch(Integer isSearch) {
    this.isSearch = isSearch;
  }

  /**
   * @return the onlyImports
   */
  public Integer getOnlyImports() {
    return onlyImports;
  }

  /**
   * @param onlyImports the onlyImports to set
   */
  public void setOnlyImports(Integer onlyImports) {
    this.onlyImports = onlyImports;
  }

  public String getPrjIds() {
    return prjIds;
  }

  public Long getPrjId() {
    if (prjId == null && StringUtils.isNotBlank(des3PrjId)) {
      prjId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PrjId), 0L);
    }
    return prjId;
  }

  public void setPrjIds(String prjIds) {
    this.prjIds = prjIds;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public Integer getFundingYear() {
    return fundingYear;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setFundingYear(Integer fundingYear) {
    this.fundingYear = fundingYear;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public String getFolderImgSrc() {
    return folderImgSrc;
  }

  public void setFolderImgSrc(String folderImgSrc) {
    this.folderImgSrc = folderImgSrc;
  }

  public String getYearImgSrc() {
    return yearImgSrc;
  }

  public void setYearImgSrc(String yearImgSrc) {
    this.yearImgSrc = yearImgSrc;
  }

  public String getGroupImgSrc() {
    return groupImgSrc;
  }

  public void setGroupImgSrc(String groupImgSrc) {
    this.groupImgSrc = groupImgSrc;
  }

  /**
   * @return the from
   */
  public Date getFrom() {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom(Date from) {
    this.from = from;
  }

  /**
   * @return the to
   */
  public Date getTo() {
    return to;
  }

  /**
   * @param to the to to set
   */
  public void setTo(Date to) {
    this.to = to;
  }

  /**
   * @return the searchKey
   */
  public String getSearchKey() {
    return searchKey;
  }

  /**
   * @param searchKey the searchKey to set
   */
  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public String getInputTip() {
    return inputTip;
  }

  public void setInputTip(String inputTip) {
    this.inputTip = inputTip;
  }

  public String getStrSearchKey() {
    strSearchKey = searchKey;
    if (StringUtils.isNotEmpty(strSearchKey) && strSearchKey.equals(this.getInputTip())) {
      strSearchKey = "";
    }
    if (StringUtils.isNotEmpty(strSearchKey) && flag == 0) {
      if ("输入信息检索".equals(strSearchKey.trim()) || "Pleas enter information to search".equals(strSearchKey.trim())) {
        strSearchKey = "";
      }
    }
    return strSearchKey;
  }

  public void setStrSearchKey(String strSearchKey) {
    this.strSearchKey = strSearchKey;
  }

  public Map getPrjFolderMap() {
    return prjFolderMap;
  }

  public void setPrjFolderMap(Map prjFolderMap) {
    this.prjFolderMap = prjFolderMap;
  }

  public Map getYearMap() {
    return yearMap;
  }

  public void setYearMap(Map yearMap) {
    this.yearMap = yearMap;
  }

  public int getPrjTotalCount() {
    return prjTotalCount;
  }

  public void setPrjTotalCount(int prjTotalCount) {
    this.prjTotalCount = prjTotalCount;
  }

  public boolean getShowPrjGroup() {
    return showPrjGroup;
  }

  public void setShowPrjGroup(boolean showPrjGroup) {
    this.showPrjGroup = showPrjGroup;
  }

  public String getDes3CurrentId() {
    return des3CurrentId;
  }

  public void setDes3CurrentId(String des3CurrentId) {
    this.des3CurrentId = des3CurrentId;
  }

  public boolean getOutside() {
    return outside;
  }

  public void setOutside(boolean outside) {
    this.outside = outside;
  }

  public boolean getOthersSee() {
    return othersSee;
  }

  public void setOthersSee(boolean othersSee) {
    this.othersSee = othersSee;
  }

  public boolean getNoPrjFulltextList() {
    return noPrjFulltextList;
  }

  public void setNoPrjFulltextList(boolean noPrjFulltextList) {
    this.noPrjFulltextList = noPrjFulltextList;
  }

  public String getOutsideDes3PsnId() {
    return outsideDes3PsnId;
  }

  public void setOutsideDes3PsnId(String outsideDes3PsnId) {
    this.outsideDes3PsnId = outsideDes3PsnId;
  }

  public List<String> getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(List<String> agencyName) {
    this.agencyName = agencyName;
  }

  public List<Map<String, Object>> getAgencyNameList() {
    return agencyNameList;
  }

  public void setAgencyNameList(List<Map<String, Object>> agencyNameList) {
    this.agencyNameList = agencyNameList;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public String getAgency() {
    return agency;
  }

  public void setAgency(String agency) {
    this.agency = agency;
  }

  public List<String> getAgencyYear() {
    return agencyYear;
  }

  public void setAgencyYear(List<String> agencyYear) {
    this.agencyYear = agencyYear;
  }

  public List<String> getAgencyCallback() {
    return agencyCallback;
  }

  public void setAgencyCallback(List<String> agencyCallback) {
    this.agencyCallback = agencyCallback;
  }

  public List<String> getAllAgencyName() {
    return allAgencyName;
  }

  public void setAllAgencyName(List<String> allAgencyName) {
    this.allAgencyName = allAgencyName;
  }

  public Integer getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(Integer currentYear) {
    this.currentYear = currentYear;
  }

  public String getSearchDes3PsnId() {
    return searchDes3PsnId;
  }

  public void setSearchDes3PsnId(String searchDes3PsnId) {
    this.searchDes3PsnId = searchDes3PsnId;
  }

  public Long getSearchPsnId() {
    if (searchPsnId == null && StringUtils.isNotBlank(searchDes3PsnId)) {
      searchPsnId = Long.parseLong(Des3Utils.decodeFromDes3(searchDes3PsnId));
    }
    return searchPsnId;
  }

  public void setSearchPsnId(Long searchPsnId) {
    this.searchPsnId = searchPsnId;
  }

  public boolean getIsVIP() {
    return isVIP;
  }

  public void setIsVIP(boolean isVIP) {
    this.isVIP = isVIP;
  }

}
