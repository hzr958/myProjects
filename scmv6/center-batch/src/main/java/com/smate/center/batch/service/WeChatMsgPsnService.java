package com.smate.center.batch.service;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 微信发送任务service接口
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
public interface WeChatMsgPsnService {
  /**
   * id查询V_BATCH_PRE_WECHAT_PSN表中未处理的记录
   * 
   * @param Long id V_BATCH_PRE_WECHAT_PSN表中id
   * @return WeChatPreProcessPsn 消息实体
   * @version 6.0.1
   */
  public WeChatPreProcessPsn getUnProcessedWeChatPreProcessPsnById(Long id);

  /**
   * 更新记录状态，发送消息成功
   * 
   * @param Long msgId V_BATCH_PRE_WECHAT_PSN表中id
   * @return WeChatPreProcessPsn 消息实体
   * @version 6.0.1
   */
  public void updateStatusSuccess(Long msgId);

  /**
   * 更新记录状态，发送消息出错
   * 
   * @param Long V_BATCH_PRE_WECHAT_PSN表中id
   * @return
   * @version 6.0.1
   */
  public void updateStatusError(Long msgId);

  /**
   * 发送消息
   * 
   * @param WeChatPreProcessPsn msgPsn 消息实体
   * @return Map，微信返回发送结果List<Map>
   * @version 6.0.1
   */
  public List<Map> sendMsgPsn(WeChatPreProcessPsn msgPsn) throws BatchTaskException, Exception;

  /**
   * id查询V_BATCH_PRE_WECHAT_PSN表中的记录，无视发送状态
   * 
   * @param Long id V_BATCH_PRE_WECHAT_PSN表中id
   * @return WeChatPreProcessPsn 消息实体
   * @version 6.0.1
   */
  WeChatPreProcessPsn getWeChatPreProcessPsnById(Long id);
}
