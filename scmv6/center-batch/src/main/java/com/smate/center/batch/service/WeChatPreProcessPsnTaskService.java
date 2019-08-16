package com.smate.center.batch.service;

import java.util.Date;
import java.util.List;

import com.smate.center.batch.model.sns.wechat.WeChatMessagePsn;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 微信消息去重任务业务service接口
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
public interface WeChatPreProcessPsnTaskService {

  /**
   * 根据状态查询微信消息表中记录
   * 
   * @param Long status 处理状态
   * @return List<WeChatMessagePsn>
   * @version 6.0.1
   */
  public List<WeChatMessagePsn> getWeChatMessagePsnListByStatus(Integer status);

  /**
   * 微信消息查重
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @return boolean
   * @version 6.0.1
   */
  public boolean checkDuplicate(WeChatMessagePsn weChatMessagePsn) throws BatchTaskException;

  /**
   * 查重记录后，插入V_BATCH_PRE_WECHAT_PSN表中
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @param Long id V_WECHAT_MESSAGE_PSN表主键id
   * @param String content 消息内容
   * @param String contentMd5 消息Md5码
   * @param Long openId 第三方用户来源标识
   * @param Date createTime 创建时间
   * @param Long psnId 发送对象scm中psnid
   * @param String token 第三方系统id
   * @return
   * @version 6.0.1
   */
  public void saveToPreProcess(WeChatMessagePsn weChatMessagePsn, Long id, String content, String contentMd5,
      Long openId, Date createTime, Long psnId, String token) throws BatchTaskException;

  /**
   * 记录插入历史表中
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @param Long id V_WECHAT_MESSAGE_PSN表主键id
   * @param String content 消息内容
   * @param Long openId 第三方用户来源标识
   * @param Date createTime 创建时间
   * @param Long psnId 发送对象scm中psnid
   * @param String token 第三方系统id
   * @return
   * @version 6.0.1
   */
  public void saveToHistory(WeChatMessagePsn weChatMessagePsn, Long id, String content, String contentMd5, Long openId,
      Date createTime, Long psnId, String token) throws BatchTaskException;

  /**
   * 向V_BATCH_JOBS总表中插入记录
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @return
   * @version 6.0.1
   */
  public void saveToBatchJobs(WeChatMessagePsn weChatMessagePsn) throws BatchTaskException;

  /**
   * 保存到微信消息表
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @return
   * @version 6.0.1
   */
  public void saveToWebChatMessagePsn(WeChatMessagePsn weChatMessagePsn);

  /**
   * 通过第三方系统openid与token查询psnid
   * 
   * @param Long openId 第三方用户来源标识
   * @param String token 第三方系统id
   * @return Long psnid
   * @version 6.0.1
   */
  public Long getPsnIdByOpenId(Long OpenId, String token) throws BatchTaskException;
}
