package com.smate.center.task.rcmd.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smate.center.task.base.AppSettingConstants;
import com.smate.center.task.base.AppSettingContext;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rol.quartz.PubRolPsnStatRefreshEnum;
import com.smate.center.task.model.rol.quartz.PubRolPsnStatRefreshMessage;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果认领状态统计更新消息 .
 * 
 * @author liqinghua
 * 
 */
@Component("pubRolPsnStatRefreshProducer")
public class PubRolPsnStatRefreshProducer {
  private static final long serialVersionUID = 4921768888877355258L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private String cBeanName = "pubRolPsnStatRefreshConsumer";
  @Autowired
  private PubRolPsnStatRefreshConsumer pubRolPsnStatRefreshConsumer;

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
    pubRolPsnStatRefreshConsumer.receive(msg);
    logger.debug("发送某单位指定人员成果认领状态统计更新消息消息insId:{},psnId:{}", insId, psnId);
  }

  private boolean needRefresh() {
    return AppSettingContext.getIntValue(AppSettingConstants.PUBPSN_STATREF_ENABLED) == 1;
  }

}
