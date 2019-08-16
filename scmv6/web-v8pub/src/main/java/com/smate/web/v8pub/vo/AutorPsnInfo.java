package com.smate.web.v8pub.vo;

public class AutorPsnInfo implements Comparable<AutorPsnInfo> {
  private Long psnId;
  private String name;
  private String avatars;
  private String des3PsnId;
  private String psnInfo;
  private String isFriend = "0";
  private Integer prjCount = 0;
  private Integer pubCount = 0;
  private Integer hIndex = 0;
  private String psnUrl;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getPsnInfo() {
    return psnInfo;
  }

  public void setPsnInfo(String psnInfo) {
    this.psnInfo = psnInfo;
  }

  public String getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(String isFriend) {
    this.isFriend = isFriend;
  }

  public Integer getPrjCount() {
    return prjCount;
  }

  public void setPrjCount(Integer prjCount) {
    this.prjCount = prjCount;
  }

  public Integer getPubCount() {
    return pubCount;
  }

  public void setPubCount(Integer pubCount) {
    this.pubCount = pubCount;
  }

  public Integer gethIndex() {
    return hIndex;
  }

  public void sethIndex(Integer hIndex) {
    this.hIndex = hIndex;
  }

  @Override
  public int compareTo(AutorPsnInfo o) {
    int i = o.getPubCount() - this.getPubCount();
    if (i == 0) {
      i = o.getPrjCount() - this.getPrjCount();
      if (i == 0) {
        i = o.gethIndex() - this.gethIndex();
        if (i == 0) {
          i = (int) (o.getPsnId() - this.getPsnId());
        }
      }
    }
    return i;
  }

  public String getPsnUrl() {
    return psnUrl;
  }

  public void setPsnUrl(String psnUrl) {
    this.psnUrl = psnUrl;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

}
