package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 群组主页成果表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "GROUP_HOMEPAGE_PUB")
public class GroupHomePagePub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1248123571206162052L;
  private GroupHomePagePubKey id;

  public GroupHomePagePub() {
    super();
  }

  public GroupHomePagePub(Long confId, Long pubId) {
    super();
    this.id = new GroupHomePagePubKey(confId, pubId);
  }

  @EmbeddedId
  public GroupHomePagePubKey getId() {
    return id;
  }

  public void setId(GroupHomePagePubKey id) {
    this.id = id;
  }

}
