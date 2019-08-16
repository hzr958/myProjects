package com.smate.web.v8pub.vo.searchimport;

import java.io.Serializable;
import java.util.List;

/**
 * 更新成果引用VO
 * 
 * @author wsn
 * @date 2018年8月28日
 */
public class PubCitedVo implements Serializable {

  private Long psnId; // 人员ID
  private String updateDes3PubIds; // 加密的待更新的成果ID
  private List<Long> pubIds; // 成果ID
  private String citedXml; // 成果引用数据xml
  private String des3PubId; // 加密的成果ID
  private Integer srcDbId;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getUpdateDes3PubIds() {
    return updateDes3PubIds;
  }

  public void setUpdateDes3PubIds(String updateDes3PubIds) {
    this.updateDes3PubIds = updateDes3PubIds;
  }

  public List<Long> getPubIds() {
    return pubIds;
  }

  public void setPubIds(List<Long> pubIds) {
    this.pubIds = pubIds;
  }

  public String getCitedXml() {
    return citedXml;
  }

  public void setCitedXml(String citedXml) {
    this.citedXml = citedXml;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(Integer srcDbId) {
    this.srcDbId = srcDbId;
  }

}
