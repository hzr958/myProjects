package com.smate.web.v8pub.vo.importfile;

import java.util.List;

public class ChangePub {
  private Integer seqNo;// 成果序号
  private Integer selected;// 是否选中0没选中，1选中
  private List<String> sitations;// 收录情况
  private Integer pubType;// 成果类型
  private Integer pubPermission; // 4：隐私，7：公开
  private Integer repeatSelect;// 0跳过，1更新，2新增
  private Long pubId;
  private String dupPubId;
  private String des3PubFileId;

  public String getDes3PubFileId() {
    return des3PubFileId;
  }

  public void setDes3PubFileId(String des3PubFileId) {
    this.des3PubFileId = des3PubFileId;
  }

  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  public Integer getSelected() {
    return selected;
  }

  public void setSelected(Integer selected) {
    this.selected = selected;
  }

  public List<String> getSitations() {
    return sitations;
  }

  public void setSitations(List<String> sitations) {
    this.sitations = sitations;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public Integer getRepeatSelect() {
    return repeatSelect;
  }

  public void setRepeatSelect(Integer repeatSelect) {
    this.repeatSelect = repeatSelect;
  }

  public Integer getPubPermission() {
    return pubPermission;
  }

  public void setPubPermission(Integer pubPermission) {
    this.pubPermission = pubPermission;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(String dupPubId) {
    this.dupPubId = dupPubId;
  }

}
