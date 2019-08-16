package com.smate.web.psn.model.resume;

import java.io.Serializable;

/**
 * 简历代表性成果信息
 * 
 * @author wsn
 *
 */
public class CVPubInfo implements Serializable, Comparable<CVPubInfo> {

  private static final long serialVersionUID = 4853661863073786625L;
  private Long pubId; // 成果ID
  private String des3PubId;// 加密成果id
  private Integer seqNo; // 顺序
  private String pubInfo; // 成果信息

  public CVPubInfo(Long pubId, Integer seqNo, String pubInfo) {
    super();
    this.pubId = pubId;
    this.seqNo = seqNo;
    this.pubInfo = pubInfo;
  }

  public CVPubInfo() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public String getPubInfo() {
    return pubInfo;
  }

  public void setPubInfo(String pubInfo) {
    this.pubInfo = pubInfo;
  }

  @Override
  public int compareTo(CVPubInfo o) {
    if (o.getSeqNo() > this.getSeqNo()) {
      return 1;
    } else if (o.getSeqNo() < this.getSeqNo()) {
      return -1;
    }
    return 0;
  }
}
