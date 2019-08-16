package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

/**
 * 成果信息.
 * 
 * @author lqh
 * 
 */
public class PubInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3368382540722297433L;

  private String zhTitle;
  private String enTitle;
  private String zhAbs;
  private String enAbs;
  private String zhKws;
  private String enKws;

  public PubInfo() {}

  public PubInfo(String zhTitle, String enTitle, String zhAbs, String enAbs, String zhKws, String enKws) {
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.zhAbs = zhAbs;
    this.enAbs = enAbs;
    this.zhKws = zhKws;
    this.enKws = enKws;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public String getEnTitle() {
    return enTitle;
  }

  public String getZhAbs() {
    return zhAbs;
  }

  public String getEnAbs() {
    return enAbs;
  }

  public String getEnKws() {
    return enKws;
  }

  public String getZhKws() {
    return zhKws;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public void setZhAbs(String zhAbs) {
    this.zhAbs = zhAbs;
  }

  public void setEnAbs(String enAbs) {
    this.enAbs = enAbs;
  }

  public void setEnKws(String enKws) {
    this.enKws = enKws;
  }

  public void setZhKws(String zhKws) {
    this.zhKws = zhKws;
  }

}
