package com.smate.web.v8pub.vo;

import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.pub.vo.PubInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成果列表结果集
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public class PubListResult {
  /**
   * 状态 默认值=success error
   */
  public String status = "success";
  /**
   * 失败返回的信息
   */
  public String msg = "";
  /**
   * 总数 默认值为0
   */
  public Integer totalCount = 0;

  /**
   * 成果出版年份集合
   */
  public List<Integer> publishYearList = new ArrayList<>();
  /**
   * 成果列表
   */
  // @JsonIgnore
  public List<PubInfo> resultList = new ArrayList<>();
  public List<PubDetailVO> pubDetailVOList = new ArrayList<>();
  /**
   * 成果列表
   */
  // @JsonIgnore
  public Map<String, Object> resultData = new HashMap<String, Object>();

  public Map<String, Object> getResultData() {
    return resultData;
  }

  public void setResultData(Map<String, Object> resultData) {
    this.resultData = resultData;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public List<PubInfo> getResultList() {
    return resultList;
  }

  public void setResultList(List<PubInfo> resultList) {
    this.resultList = resultList;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public List<Integer> getPublishYearList() {
    return publishYearList;
  }

  public void setPublishYearList(List<Integer> publishYearList) {
    this.publishYearList = publishYearList;
  }

  public List<PubDetailVO> getPubDetailVOList() {
    return pubDetailVOList;
  }

  public void setPubDetailVOList(List<PubDetailVO> pubDetailVOList) {
    this.pubDetailVOList = pubDetailVOList;
  }
}
