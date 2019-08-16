package com.smate.center.batch.service.pub.mq;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.LocalQueueDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.LocalQueueError;
import com.smate.center.batch.model.rol.pub.LocalQueueMessage;
import com.smate.center.batch.util.pub.JsonUtils;
import com.smate.core.base.utils.string.ServiceUtil;

import net.sf.json.JSONObject;

/**
 * 本地消息服务.
 * 
 * @author liqinghua
 * 
 */
@Service("localQueueService")
@Transactional(rollbackFor = Exception.class)
public class LocalQueueServiceImpl implements LocalQueueService {

  /**
   * 
   */
  private static final long serialVersionUID = -6637777488161455030L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private LocalQueueDao localQueueDao;

  /**
   * 创建消息ID.
   * 
   * @return
   * @throws ServiceException
   */
  public Long generalMsgId() throws ServiceException {

    try {
      return localQueueDao.generalMsgId();
    } catch (Exception e) {
      logger.error("创建消息ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long createMsg(String cbeanName, BaseLocalQueneMessage quene) throws ServiceException {

    Integer priority = 9;
    if (quene.getPriority() != null) {
      priority = quene.getPriority();
    }
    return this.createMsg(cbeanName, priority, quene);
  }

  @Override
  public Long createMsg(String cbeanName, Integer priority, BaseLocalQueneMessage quene) throws ServiceException {
    try {
      Long msgId = this.generalMsgId();
      quene.setMsgId(msgId);
      String msgBody = JSONObject.fromObject(quene, JsonUtils.configJson(null, "yyyy-MM-dd HH:mm:ss")).toString();
      String msgClz = quene.getClass().getName();
      LocalQueueMessage msg = new LocalQueueMessage(msgId, cbeanName, msgBody, msgClz, priority);
      this.localQueueDao.save(msg);
      return msgId;
    } catch (Exception e) {
      logger.error("创建消息体", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteMsg(Long msgId) throws ServiceException {

    try {
      this.localQueueDao.removeMsg(msgId);

    } catch (Exception e) {
      logger.error("删除消息体", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void updateLocalQueneState(Long msgId, int state) throws ServiceException {
    try {
      this.localQueueDao.updateLocalQueneState(msgId, state);

    } catch (Exception e) {
      logger.error("标记状态msgId=" + msgId, e);
      throw new ServiceException("标记状态msgId=" + msgId, e);
    }
  }

  @Override
  public List<LocalQueueMessage> loadSendMessage(int batchSize) throws ServiceException {

    try {
      return this.localQueueDao.loadSendMessage(batchSize);
    } catch (Exception e) {
      logger.error("加载需要执行的消息数据", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void remarkError(Long msgId, Exception ex) throws ServiceException {
    this.remarkError(msgId, ServiceUtil.getErrorTranceStr(ex));
  }

  @Override
  public void remarkError(Long msgId, String ex) throws ServiceException {

    try {

      LocalQueueMessage msg = this.localQueueDao.get(msgId);
      if (msg != null) {
        int errorNum = msg.getErrorNum() == null ? 0 : msg.getErrorNum();
        ++errorNum;
        msg.setErrorNum(errorNum);
        if (msg.getErrorNum() != null && msg.getErrorNum() >= 3) {
          msg.setState(9);
        }
        this.localQueueDao.save(msg);
        // 记录错误信息
        if (StringUtils.isNotBlank(ex)) {
          LocalQueueError errorMsg = new LocalQueueError(msgId, ex.substring(0, 1700));
          this.localQueueDao.saveLocalQueueError(errorMsg);
        }
      }
    } catch (Exception e) {
      logger.error("标记消息执行错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public LocalQueueMessage getMsgByMsgId(Long msgId) {

    LocalQueueMessage msg = this.localQueueDao.get(msgId);

    return msg;
  }

}
