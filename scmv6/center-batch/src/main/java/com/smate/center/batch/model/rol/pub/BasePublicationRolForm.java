package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 成果form.
 * 
 * @author liqinghua
 *
 */
public class BasePublicationRolForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4635956023033379067L;

  // Action要跳转到的目的页.
  private String forwardUrl;
  // 当前页的URL
  private String ownerUrl;
  // 是否是回退URL
  private String isBack;

  private String prePage;

  public String getIsBack() {
    return isBack;
  }

  public void setIsBack(String isBack) {
    this.isBack = isBack;
  }

  public String getOwnerUrl() {
    return ownerUrl;
  }

  public void setOwnerUrl(String ownerUrl) {
    this.ownerUrl = ownerUrl;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getPrePage() {
    return prePage;
  }

  public void setPrePage(String prePage) {
    this.prePage = prePage;
  }


}
