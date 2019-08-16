package com.smate.center.batch.service.pub.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.batch.base.AppSettingConstants;
import com.smate.center.batch.base.AppSettingContext;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果认领状态统计更新消息 .
 * 
 * @author liqinghua
 * 
 */
@Component("pubRolPsnStatRefreshProducer")
public class PubRolPsnStatRefreshProducer extends AbstractLocalQueneMessageProducer {

  /**
   * 
   */
  private static final long serialVersionUID = 4921768888877355258L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String cBeanName = "pubRolPsnStatRefreshConsumer";

  /**
   * 发送某单位指定人员成果认领状态统计更新消息.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void sendRefreshMessage(Long insId, Long psnId) throws ServiceException {
    if (!needRefresh()) {
      return;
    }
    Assert.notNull(insId, "insId不能为空.");
    Assert.notNull(psnId, "psnId不能为空.");
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    PubRolPsnStatRefreshMessage msg =
        new PubRolPsnStatRefreshMessage(insId, psnId, PubRolPsnStatRefreshEnum.REFRESH_INS_PSN, nodeId);
    this.sendMessage(msg);
    logger.debug("发送某单位指定人员成果认领状态统计更新消息消息insId:{},psnId:{}", insId, psnId);
  }

  /**
   * 发送某单位批量人员成果认领状态统计更新消息.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void sendRefreshMessage(Long insId, List<Long> psnIds) throws ServiceException {
    if (!needRefresh()) {
      return;
    }
    Assert.notNull(insId, "insId不能为空.");
    Assert.notNull(psnIds, "psnId不能为空.");
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    PubRolPsnStatRefreshMessage msg =
        new PubRolPsnStatRefreshMessage(insId, psnIds, PubRolPsnStatRefreshEnum.REFRESH_INS_PSN_BATCH, nodeId);
    this.sendMessage(msg);
    logger.debug("发送某单位批量人员成果认领状态统计更新消息insId:{},psnIds:{}", insId, psnIds);
  }

  /**
   * 发送某单位批量人员成果认领状态统计更新消息.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void sendPubRefreshMessage(Long insId, Long pubId) throws ServiceException {
    if (!needRefresh()) {
      return;
    }
    Assert.notNull(insId, "insId不能为空.");
    Assert.notNull(pubId, "pubId不能为空.");
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    PubRolPsnStatRefreshMessage msg =
        new PubRolPsnStatRefreshMessage(insId, PubRolPsnStatRefreshEnum.REFRESH_PUB_PSN, pubId, nodeId);
    this.sendMessage(msg);
    logger.debug("发送某单位成果认领状态统计更新消息insId:{},pubId:{}", insId, pubId);
  }

  /**
   * 发送某单位指定成果认领状态统计更新消息.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void sendRefreshMessage(Long insId) throws ServiceException {
    if (!needRefresh()) {
      return;
    }
    Assert.notNull(insId, "insId不能为空.");
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    PubRolPsnStatRefreshMessage msg =
        new PubRolPsnStatRefreshMessage(insId, PubRolPsnStatRefreshEnum.REFRESH_INS_PSN, nodeId);
    this.sendMessage(msg);
    logger.debug("发送某单位指定成果认领状态统计更新消息insId:{}", insId);
  }

  private boolean needRefresh() {
    // 设置为true
    // return AppSettingContext.getIntValue(AppSettingConstants.PUBPSN_STATREF_ENABLED) == 1;
    return true;
  }

  @Override
  public String getCbeanName() {
    return cBeanName;
  }

}
