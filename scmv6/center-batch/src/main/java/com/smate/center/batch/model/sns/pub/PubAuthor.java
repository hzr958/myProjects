package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

public class PubAuthor implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8663406590671321695L;

  private Long psnId;
  private String name;
  // 头像地址
  private String avatars;
  // 头衔
  private String viewTitolo;
  private Integer isPsnFriend = 0;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

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

  public String getViewTitolo() {
    return viewTitolo;
  }

  public void setViewTitolo(String viewTitolo) {
    this.viewTitolo = viewTitolo;
  }

  public Integer getIsPsnFriend() {
    return isPsnFriend;
  }

  public void setIsPsnFriend(Integer isPsnFriend) {
    this.isPsnFriend = isPsnFriend;
  }

}
