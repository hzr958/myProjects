package com.smate.web.v8pub.vo;

import java.io.Serializable;
import java.util.List;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * @author yamingd 查询页Form,封装查询条件
 */
public class PsnInfluencePubVO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -855850927203939438L;
  private List<PubInfo> pubHindexList; // 显示成果信息用List
  private String des3PsnId;
  private Long psnId;
  private Integer hIndex;// H-index指数
  private String self;// 是否是自己的
  private String hasLogin;// 是否已经登录

  public String getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(String hasLogin) {
    this.hasLogin = hasLogin;
  }

  public List<PubInfo> getPubHindexList() {
    return pubHindexList;
  }

  public void setPubHindexList(List<PubInfo> pubHindexList) {
    this.pubHindexList = pubHindexList;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (psnId == null && des3PsnId != null) {
      psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer gethIndex() {
    return hIndex;
  }

  public void sethIndex(Integer hIndex) {
    this.hIndex = hIndex;
  }

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

}
