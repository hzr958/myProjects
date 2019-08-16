package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.rcmd.quartz.RecomMsgForm;

/**
 * 
 * @author cht
 *
 */
public interface RecomMsgService {
  /**
   * 发送消息
   * 
   * @param msgType
   * @param senderId
   * @param receiverId
   */
  public void sendMsg(String msgType, Long senderId, Long receiverId, Long pubId, Long resCount) throws Exception;

  public List<RecomMsgForm> getRecomMsgFormList(Long type, Integer maxResults) throws Exception;

  public void buildRecomMsg(RecomMsgForm r) throws Exception;
}
