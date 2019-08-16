package com.smate.center.batch.service.pub.mq;


/**
 * 人员统计信息刷新消息.
 * 
 * @author lqh
 * 
 */
public class PsnStatisticsRefreshMessage {

  /**
   * 
   */
  private Long psnId;
  private int pub = 0;
  private int prj = 0;
  private int group = 0;
  private int friend = 0;
  private int pubaward = 0;

  public PsnStatisticsRefreshMessage() {
    super();
  }

  public PsnStatisticsRefreshMessage(long psnId) {
    this.psnId = psnId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public int getPub() {
    return pub;
  }

  public void setPub(int pub) {
    this.pub = pub;
  }

  public int getPrj() {
    return prj;
  }

  public void setPrj(int prj) {
    this.prj = prj;
  }

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  public int getFriend() {
    return friend;
  }

  public void setFriend(int friend) {
    this.friend = friend;
  }

  public int getPubaward() {
    return pubaward;
  }

  public void setPubaward(int pubaward) {
    this.pubaward = pubaward;
  }

}
