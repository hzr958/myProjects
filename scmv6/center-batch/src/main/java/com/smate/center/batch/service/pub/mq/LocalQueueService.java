package com.smate.center.batch.service.pub.mq;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.LocalQueueMessage;


/**
 * 本地消息服务.
 * 
 * @author liqinghua
 * 
 */
public interface LocalQueueService extends Serializable {

  /**
   * 创建消息体.
   * 
   * @param cbeanName消费者beanName
   * @param quene
   * @return
   * @throws ServiceException
   */
  public Long createMsg(String cbeanName, BaseLocalQueneMessage quene) throws ServiceException;

  /**
   * 创建消息体.
   * 
   * @param cbeanName消费者beanName
   * @param quene
   * @return
   * @throws ServiceException
   */
  public Long createMsg(String cbeanName, Integer priority, BaseLocalQueneMessage quene) throws ServiceException;

  /**
   * 删除消息体.
   * 
   * @param msgId
   * @throws ServiceException
   */
  public void deleteMsg(Long msgId) throws ServiceException;

  /**
   * 标记状态.
   * 
   * @param msgId
   */
  public void updateLocalQueneState(Long msgId, int state) throws ServiceException;

  /**
   * 加载需要执行的消息数据.
   * 
   * @param batchSize
   * 
   * @return
   * @throws ServiceException
   */
  public List<LocalQueueMessage> loadSendMessage(int batchSize) throws ServiceException;

  /**
   * 标记消息执行错误.
   * 
   * @param msgId
   * @param ex
   * @throws ServiceException
   */
  public void remarkError(Long msgId, Exception ex) throws ServiceException;

  /**
   * 标记消息执行错误.
   * 
   * @param msgId
   * @param ex
   * @throws ServiceException
   */
  public void remarkError(Long msgId, String ex) throws ServiceException;

  public LocalQueueMessage getMsgByMsgId(Long msgId);
}
