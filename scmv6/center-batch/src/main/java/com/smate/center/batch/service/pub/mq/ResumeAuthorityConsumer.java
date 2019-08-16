package com.smate.center.batch.service.pub.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.psn.PsnCnfReceive;
import com.smate.center.batch.service.psn.PsnCnfReceiveAdapter;

/**
 * 工作经历，教育经历，项目录入编辑，成果录入编辑权限同步, 根据要求，代码中不能抛出异常影响成果或项目保存. sns
 * 
 * @author liangguokeng
 * 
 */
@Component("resumeAuthorityConsumer")
public class ResumeAuthorityConsumer {

  /**
   * 
   */

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnCnfReceiveAdapter psnCnfReceiveAdapter;

  public void receive(ResumeAuthorityMessage message) throws ServiceException {
    ResumeAuthorityMessage msg = (ResumeAuthorityMessage) message;
    if (msg == null || msg.getType() == null || msg.getPsnId() == null) {
      return;
    }
    if (msg.getIds() == null && msg.getIdsResumes() == null && msg.getIsDel() == null) {
      return;
    }

    try {
      logger.info("同步人员psnId" + msg.getPsnId() + "," + msg.getType() + "权限");

      PsnCnfReceive receive = psnCnfReceiveAdapter.get(msg.getType());
      if (msg.getIds() != null) {
        receive.saveCnfByIds(msg.getPsnId(), msg.getIds());
      } else if (msg.getIdsResumes() != null) {
        receive.saveCnfByObjs(msg.getPsnId(), msg.getIdsResumes());
      } else if (null != msg.getIsDel() && 1 == msg.getIsDel() && msg.getIdList().size() > 0) {
        receive.delCnfByIds(msg.getPsnId(), msg.getIdList());
      }
    } catch (Exception e) {
      logger.error("工作经历，教育经历，项目录入编辑，成果录入编辑权限同步失败！", e);
    }
  }

}
