package com.smate.web.v8pub.vo;

import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 重复成果视图显示对象
 * 
 * @author aijiangbin
 * @date 2018年9月4日
 */
public class RepeatPubVO {

  private Long psnId;
  private String des3PsnId; // 人员加密串
  private String des3RecordId;
  private List<Integer> recordIdArr;
  private String des3PubId;
  private String des3pubSameItemId;

  private Map<String, Object> resultMap = new HashMap<>();
  private List<RepeatPubInfo> repeatPubInfoList = new ArrayList<>();

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

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public List<RepeatPubInfo> getRepeatPubInfoList() {
    return repeatPubInfoList;
  }

  public void setRepeatPubInfoList(List<RepeatPubInfo> repeatPubInfoList) {
    this.repeatPubInfoList = repeatPubInfoList;
  }

  public String getDes3RecordId() {
    return des3RecordId;
  }

  public void setDes3RecordId(String des3RecordId) {
    this.des3RecordId = des3RecordId;
  }

  public List<Integer> getRecordIdArr() {
    return recordIdArr;
  }

  public void setRecordIdArr(List<Integer> recordIdArr) {
    this.recordIdArr = recordIdArr;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public String getDes3pubSameItemId() {
    return des3pubSameItemId;
  }

  public void setDes3pubSameItemId(String des3pubSameItemId) {
    this.des3pubSameItemId = des3pubSameItemId;
  }



}
