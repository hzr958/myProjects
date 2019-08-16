package com.smate.web.v8pub.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 成果统计数结果
 * 
 * @author aijiangbin
 * @date 2018年7月17日
 */
public class PubCountResult {
  /**
   * 状态 默认值=success error
   */
  public String status = "success";
  /**
   * 失败返回的信息
   */
  public String msg = "";

  /**
   * 结果统计数
   */
  public Map<String, Object> resultCount = new HashMap<>();

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Map<String, Object> getResultCount() {
    return resultCount;
  }

  public void setResultCount(Map<String, Object> resultCount) {
    this.resultCount = resultCount;
  }



}
