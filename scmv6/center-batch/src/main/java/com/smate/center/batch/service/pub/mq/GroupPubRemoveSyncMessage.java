package com.smate.center.batch.service.pub.mq;

import java.io.Serializable;

/**
 * 同步从群组中移除成果文献.
 * 
 * @author LY
 * 
 */
public class GroupPubRemoveSyncMessage implements Serializable {

  private static final long serialVersionUID = -6086841435678410097L;
  private Integer articleType;
  private Integer nodeId;
  private Long groupId;
  private Long pubId;
  private Long psnId;
  /**
   * mypub,mygroup.
   */
  private String opAction;

  public String getOpAction() {
    return opAction;
  }

  public void setOpAction(String opAction) {
    this.opAction = opAction;
  }

  public Long getPubId() {
    return pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Integer getNodeId() {
    return nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

}
