package com.smate.web.v8pub.po.sns;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人成果详情表
 * 
 * @author aijiangbin
 * @date 2018年7月24日
 */
@Entity
@Table(name = "V_PUB_SNS_DETAIL")
public class PubSnsDetailPO {

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "PUB_JSON")
  private String pubJson;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getPubJson() {
    return pubJson;
  }

  public void setPubJson(String pubJson) {
    this.pubJson = pubJson;
  }



}
