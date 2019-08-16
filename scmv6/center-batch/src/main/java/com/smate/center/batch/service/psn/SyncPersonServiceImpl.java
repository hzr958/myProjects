package com.smate.center.batch.service.psn;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.user.UserService;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

@Transactional(rollbackFor = Exception.class)
@Service("syncPersonService")
public class SyncPersonServiceImpl implements SyncPersonService {


  protected final Logger logger = LoggerFactory.getLogger(getClass());
  // 查找或创建个人主页url
  private PsnProfileUrlService psnProfileUrlService;

  @Autowired
  private PersonManager personManager;
  @Autowired
  private UserService userService;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public String getDomainUrlByNodeId(Integer nodeId) throws ServiceException {

    try {
      String webDomain = sysDomainConst.getSnsDomain();
      return webDomain.replace("http://", "");
    } catch (Exception e) {
      logger.info("根据节点获取域名", e);
      throw new ServiceException("根据节点获取域名", e);
    }
  }

  @Override
  public Person loadSystemPerson() throws ServiceException {

    try {
      User user = userService.findSystemUser();
      return personManager.getPerson(user.getId());
    } catch (Exception e) {
      logger.info("获取系统管理员", e);
      throw new ServiceException("获取系统管理员", e);
    }

  }

  @Override
  public String buildResumeUrl(Long psnId) throws ServiceException {
    try {
      String resumeUrl = "";
      String webUrl = "https://" + sysDomainConst.getSnsDomain();
      if (sysDomainConst.getSnsContext() != null) {
        webUrl = webUrl + "/" + sysDomainConst.getSnsContext();
      }
      String fixUrl = psnProfileUrlService.findUrl(psnId);
      if (fixUrl == null) {
        resumeUrl = webUrl + "/in/view?des3PsnId=" + ServiceUtil.encodeToDes3(psnId.toString());
      } else {
        resumeUrl = webUrl + "/in/" + fixUrl;
      }

      return resumeUrl;
    } catch (Exception e) {
      logger.info("科研简历url", e);
      throw new ServiceException("科研简历url", e);
    }
  }

}
