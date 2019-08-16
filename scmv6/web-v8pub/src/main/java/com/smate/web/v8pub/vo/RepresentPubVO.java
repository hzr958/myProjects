package com.smate.web.v8pub.vo;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 人员代表性成果用VO
 *
 * @author ltl
 *
 */
public class RepresentPubVO {

  /**
   * 
   */
  private static final long serialVersionUID = -4766454603058600274L;
  private Long psnId; // 人员ID
  private String des3PsnId; // 加密的人员ID

  private Long cnfId; // 人员配置信息ID
  private Integer anyUser; // 查看成果的权限
  private String addToRepresentPubIds; // 加入代表性成果的成果ID拼接成的字符串，逗号隔开
  private String searchKey; // 查询的字符串
  private Integer searchPubPageNo; // 查询公开成果时用的页数
  private boolean isMySelf; // 是否是本人
  private List<PubInfo> pubInfoList; // 显示成果信息用List
  private Long currentPsnId;// 当前登录的人id

  public Long getPsnId() {
    if (psnId == null || psnId == 0) {
      if (des3PsnId != null) {
        psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PsnId));
      } else {
        psnId = 0L;
      }
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

  public Long getCnfId() {
    return cnfId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public Integer getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  public String getAddToRepresentPubIds() {
    return addToRepresentPubIds;
  }

  public void setAddToRepresentPubIds(String addToRepresentPubIds) {
    this.addToRepresentPubIds = addToRepresentPubIds;
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

  public Integer getSearchPubPageNo() {
    return searchPubPageNo;
  }

  public void setSearchPubPageNo(Integer searchPubPageNo) {
    this.searchPubPageNo = searchPubPageNo;
  }

  public boolean getIsMySelf() {
    return isMySelf;
  }

  public void setIsMySelf(boolean isMySelf) {
    this.isMySelf = isMySelf;
  }

  /**
   * @return the pubInfoList
   */
  public List<PubInfo> getPubInfoList() {
    return pubInfoList;
  }

  /**
   * @param pubInfoList the pubInfoList to set
   */
  public void setPubInfoList(List<PubInfo> pubInfoList) {
    this.pubInfoList = pubInfoList;
  }

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

}
