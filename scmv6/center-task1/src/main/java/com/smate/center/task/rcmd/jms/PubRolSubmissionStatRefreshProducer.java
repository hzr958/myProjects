package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatRefreshEnum;
import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatRefreshMessage;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果提交统计数更新消息.
 * 
 * @author liqinghua
 * 
 */
@Component("pubRolSubmissionStatRefreshProducer")
public class PubRolSubmissionStatRefreshProducer {
  /**
   * 
   */
  private static final long serialVersionUID = -7990696761850263658L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String cBeanName = "pubRolSubmissionStatRefreshConsumer";
  @Autowired
  private PubRolSubmissionStatRefreshConsumer pubRolSubmissionStatRefreshConsumer;

  /**
   * 发送某单位指定人员更新提交成果统计数更新消息.
   * 
   * @param insId
   * @param psnId
   * @throws ServiceException
   */
  public void sendRefreshMessage(Long insId, Long psnId) throws ServiceException {

    Assert.notNull(insId, "insId不能为空.");
    Assert.notNull(psnId, "psnId不能为空.");

    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    PubRolSubmissionStatRefreshMessage msg =
        new PubRolSubmissionStatRefreshMessage(insId, psnId, PubRolSubmissionStatRefreshEnum.REFRESH_INS_PSN, nodeId);
    pubRolSubmissionStatRefreshConsumer.receive(msg);
    logger.debug("发送某单位指定人员更新提交成果统计数更新消息insId:{},psnId:{}", insId, psnId);
  }
}
