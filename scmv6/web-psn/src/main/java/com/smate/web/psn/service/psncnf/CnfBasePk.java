package com.smate.web.psn.service.psncnf;

import java.io.Serializable;

public class CnfBasePk implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5987843426011388893L;
  // id可能的情况：pubId,prjId,eduId,workId
  private Long id;
  private Integer anyUser;

  /**
   * @return id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return anyUser
   */
  public Integer getAnyUser() {
    return anyUser;
  }

  /**
   * @param id 要设置的 id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @param anyUser 要设置的 anyUser
   */
  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

}
