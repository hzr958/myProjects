package com.smate.web.mobile.prj.vo;

import java.io.Serializable;

/**
 * 项目操作VO
 * 
 * @author wsn
 * @date May 17, 2019
 */
public class PrjOptVO implements Serializable {

  private String des3Id; // 加密的项目ID
  private Integer optType; // 操作类型， 1赞，0取消赞, 0收藏，1取消收藏
  private String des3FundId; // 加密的基金Id

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Integer getOptType() {
    return optType;
  }

  public void setOptType(Integer optType) {
    this.optType = optType;
  }

  public String getDes3FundId() {
    return des3FundId;
  }

  public void setDes3FundId(String des3FundId) {
    this.des3FundId = des3FundId;
  }



}
