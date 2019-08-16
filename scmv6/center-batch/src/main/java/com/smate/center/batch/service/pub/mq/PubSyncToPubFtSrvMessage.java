package com.smate.center.batch.service.pub.mq;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.enums.pub.PubSyncToPubFtSrvTypeEnum;

/**
 * 同步成果冗余信息到pubftsrv消息体.
 * 
 * @author pwl
 * 
 */
public class PubSyncToPubFtSrvMessage {

  /**
   * 
   */

  private List<Map<String, Object>> list = null;

  private Long pubId;

  private Long psnId;

  private Long fulltextFileId;

  private int permission;

  private PubSyncToPubFtSrvTypeEnum type;

  private Integer pubOwnerMatch;

  public PubSyncToPubFtSrvMessage(List<Map<String, Object>> list, PubSyncToPubFtSrvTypeEnum type, int fromNode) {
    this.list = list;
    this.type = type;
  }

  public PubSyncToPubFtSrvMessage(Long pubId, Long psnId, Long fulltextFileId, int permission,
      PubSyncToPubFtSrvTypeEnum type, int fromNode) {
    this.pubId = pubId;
    this.psnId = psnId;
    this.fulltextFileId = fulltextFileId;
    this.permission = permission;
    this.type = type;
  }

  public PubSyncToPubFtSrvMessage(Long pubId, Long psnId, Integer pubOwnerMatch, PubSyncToPubFtSrvTypeEnum type,
      int fromNode) {
    this.pubId = pubId;
    this.psnId = psnId;
    this.pubOwnerMatch = pubOwnerMatch;
    this.type = type;
  }

  public List<Map<String, Object>> getList() {
    return list;
  }

  public void setList(List<Map<String, Object>> list) {
    this.list = list;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  public PubSyncToPubFtSrvTypeEnum getType() {
    return type;
  }

  public void setType(PubSyncToPubFtSrvTypeEnum type) {
    this.type = type;
  }

  public Integer getPubOwnerMatch() {
    return pubOwnerMatch;
  }

  public void setPubOwnerMatch(Integer pubOwnerMatch) {
    this.pubOwnerMatch = pubOwnerMatch;
  }

  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }
}
