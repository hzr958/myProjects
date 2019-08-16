package com.smate.center.batch.service.pub.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 工作经历，教育经历，项目录入编辑，成果录入编辑权限同步.
 * 
 * @author liangguokeng
 * 
 */
@Component("resumeAuthorityProducer")
public class ResumeAuthorityProducer {

  @Autowired
  private ResumeAuthorityConsumer resumeAuthorityConsumer;
  private static Logger logger = LoggerFactory.getLogger(ResumeAuthorityProducer.class);

  /**
   * 
   * @param psnId
   * @param pIds d集合, 用逗号分割
   * @param type 成果pub 项目prj,工作work,教育edu
   * @param fromNode
   * @throws ServiceException
   */
  public void syncResumeAuthority(Long psnId, String pIds, String type) throws ServiceException {
    try {
      ResumeAuthorityMessage message =
          new ResumeAuthorityMessage(psnId, pIds, type, SecurityUtils.getCurrentAllNodeId().get(0));
      resumeAuthorityConsumer.receive(message);
    } catch (Exception e) {
      logger.error("工作经历，教育经历，项目录入编辑，成果录入编辑权限同步失败！", e);
    }
  }

  /**
   * 
   * @param psnId
   * @param pIdsAndAuthority 成果或项目集合, String是id与权限（公开：7，好友：6，本人：4）用逗号分割组成
   * @param type 成果pub 项目prj
   * @param fromNode
   * @throws ServiceException
   */
  public void syncResumeAuthority(Long psnId, List<String> pIdsAndAuthority, String type) throws ServiceException {
    try {
      ResumeAuthorityMessage message =
          new ResumeAuthorityMessage(psnId, pIdsAndAuthority, type, SecurityUtils.getCurrentAllNodeId().get(0));
      resumeAuthorityConsumer.receive(message);
    } catch (Exception e) {
      logger.error("工作经历，教育经历，项目录入编辑，成果录入编辑权限同步！", e);
    }
  }

  /**
   * 
   * @param psnId
   * @param idList
   * @param type 成果pub 项目prj, 工作work，教育edu
   * @param isDel 1=删除
   * @param fromNode
   * @throws ServiceException
   */
  public void syncResumeAuthority(Long psnId, List<Long> idList, String type, Integer isDel) throws ServiceException {
    try {
      ResumeAuthorityMessage message =
          new ResumeAuthorityMessage(psnId, idList, type, isDel, SecurityUtils.getCurrentAllNodeId().get(0));
      resumeAuthorityConsumer.receive(message);
    } catch (Exception e) {
      logger.error("工作经历，教育经历，项目录入编辑，成果录入编辑权限同步！", e);
    }
  }


}
