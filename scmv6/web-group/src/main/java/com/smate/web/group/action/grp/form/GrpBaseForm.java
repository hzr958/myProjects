package com.smate.web.group.action.grp.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 群组主表
 * 
 * @author AiJiangBin
 *
 */
public class GrpBaseForm {
  private Map<String, Object> result;
  private Long psnId;// 当前人psnId
  private String des3PsnId;
  private Long grpId;// 群组ID
  private String des3GrpId;
  private List<GrpShowInfo> grpShowInfoList; // 群组显示对象
  private Map<Integer, Integer> grpStatisticMap; // 群组分类
  private Integer rcmdStatus = 0; // 0：推荐 1：申请 9 忽略
  private String searchGrpCategory; // 搜索的群组类别 ,分号隔离 ;
  private String searchDiscipline; // 查询研群组究领域

  private String searchKey; // 检索内容
  private List<Integer> searchGrpCategoryList;
  private List<Integer> searchDisciplineList;
  private List<Long> grpIdList; // 群组id集合
  private Integer disciplineCategory;// 研究领域类别 目前有 1-7
  private Page<Object> page = new Page<Object>();
  private Integer firstCategoryId; // 学科 一级领域 ，常量
  private Integer secondCategoryId; // 学科 二级领域 ，，常量
  private Integer grpCategory;// 群组分类 10:兴趣群组 ， 11项目群组
  private Integer keywordsSize = 5; // 提示关键词的数量
  private String appStatus;
  private Integer appTotal = 0;
  private String msg;
  private Integer grpRcmdTotal;// 群组推荐总数

  public List<GrpShowInfo> getGrpShowInfoList() {
    return grpShowInfoList;
  }

  public void setGrpShowInfoList(List<GrpShowInfo> grpShowInfoList) {
    this.grpShowInfoList = grpShowInfoList;
  }

  public Map<Integer, Integer> getGrpStatisticMap() {
    return grpStatisticMap;
  }

  public void setGrpStatisticMap(Map<Integer, Integer> grpStatisticMap) {
    this.grpStatisticMap = grpStatisticMap;
  }

  public Map<String, Object> getResult() {
    return result;
  }

  public void setResult(Map<String, Object> result) {
    this.result = result;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getGrpId() {
    if ((grpId == null || grpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
      Long.getLong("");
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getRcmdStatus() {
    return rcmdStatus;
  }

  public void setRcmdStatus(Integer rcmdStatus) {
    this.rcmdStatus = rcmdStatus;
  }

  public String getSearchGrpCategory() {
    return searchGrpCategory;
  }

  public void setSearchGrpCategory(String searchGrpCategory) {
    this.searchGrpCategory = searchGrpCategory;
  }

  public String getSearchDiscipline() {
    return searchDiscipline;
  }

  public void setSearchDiscipline(String searchDiscipline) {
    this.searchDiscipline = searchDiscipline;
  }

  public List<Integer> getSearchGrpCategoryList() {
    if (StringUtils.isNotBlank(searchGrpCategory)) {
      searchGrpCategoryList = new ArrayList<Integer>();
      String[] strArr = searchGrpCategory.split(";");
      if (strArr != null && strArr.length > 0) {
        for (String string : strArr) {
          searchGrpCategoryList.add(NumberUtils.toInt(string));
        }
      }
      return searchGrpCategoryList;
    }
    return null;
  }

  public void setSearchGrpCategoryList(List<Integer> searchGrpCategoryList) {
    this.searchGrpCategoryList = searchGrpCategoryList;
  }

  public List<Integer> getSearchDisciplineList() {
    if (StringUtils.isNotBlank(searchDiscipline)) {
      searchDisciplineList = new ArrayList<Integer>();
      String[] strArr = searchDiscipline.split(";");
      if (strArr != null && strArr.length > 0) {
        for (String string : strArr) {
          searchDisciplineList.add(NumberUtils.toInt(string));
        }
      }
      return searchDisciplineList;
    }
    return null;
  }

  public void setSearchDisciplineList(List<Integer> searchDisciplineList) {
    this.searchDisciplineList = searchDisciplineList;
  }

  public List<Long> getGrpIdList() {
    return grpIdList;
  }

  public void setGrpIdList(List<Long> grpIdList) {
    this.grpIdList = grpIdList;
  }

  public Integer getDisciplineCategory() {
    return disciplineCategory;
  }

  public void setDisciplineCategory(Integer disciplineCategory) {
    this.disciplineCategory = disciplineCategory;
  }

  public Page<Object> getPage() {
    return page;
  }

  public void setPage(Page<Object> page) {
    this.page = page;
  }

  public Integer getFirstCategoryId() {
    return firstCategoryId;
  }

  public void setFirstCategoryId(Integer firstCategoryId) {
    this.firstCategoryId = firstCategoryId;
  }

  public Integer getSecondCategoryId() {
    return secondCategoryId;
  }

  public void setSecondCategoryId(Integer secondCategoryId) {
    this.secondCategoryId = secondCategoryId;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Integer getKeywordsSize() {
    return keywordsSize;
  }

  public void setKeywordsSize(Integer keywordsSize) {
    this.keywordsSize = keywordsSize;
  }

  public String getAppStatus() {
    return appStatus;
  }

  public void setAppStatus(String appStatus) {
    this.appStatus = appStatus;
  }

  public Integer getAppTotal() {
    return appTotal;
  }

  public void setAppTotal(Integer appTotal) {
    this.appTotal = appTotal;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Integer getGrpRcmdTotal() {
    return grpRcmdTotal;
  }

  public void setGrpRcmdTotal(Integer grpRcmdTotal) {
    this.grpRcmdTotal = grpRcmdTotal;
  }


}
