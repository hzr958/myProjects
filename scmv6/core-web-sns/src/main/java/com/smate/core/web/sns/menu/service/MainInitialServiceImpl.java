package com.smate.core.web.sns.menu.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.InsPortal;

/**
 * logo服务类
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class MainInitialServiceImpl implements MainInitialService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsPortalDao insPortalDao;

  public String[] loadMainLogo(Long insId) {
    String[] logoURL = new String[5];

    try {
      InsPortal insPortal = insPortalDao.get(insId);
      if (insPortal == null) {
        logger.warn("无法找到对应域名的单位！");
        // throw new ServiceException();
      }
      // 修改了获取单位名称的代码_MaoJianGuo_2013-01-30_ROL-396.
      logoURL[0] = insPortal.getZhTitle();
      logoURL[2] = insPortal.getEnTitle();
      logoURL[1] = this.loadRolMainLogo(insPortal.getInsId());
      logoURL[3] = insPortal.getDomain();
      // logoURL[4] = insPortal.getNewDomain();
    } catch (Exception e) {

      logger.error("读取单位logo失败", e);
    }

    return logoURL;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.system.main.service.MainInitialService#loadRolMainLogo(java .lang.String)
   */
  public String loadRolMainLogo(Long insId) {

    String logoUrl = "";
    String domain = "";

    try {
      InsPortal insPortal = insPortalDao.get(insId);
      logger.debug("加载单位（" + insId + "） logo图片");
      if (insPortal == null) {
        throw new SysServiceException();
      }

      String logoAddr = insPortal.getLogo();
      domain = insPortal.getDomain();

      if (StringUtils.isBlank(logoAddr)) {// 没有上传单位图片，则默认显示系统预定义的图片logo
        logoUrl = "";
      } else {
        logoUrl = "http://" + domain + "/" + logoAddr;
      }

    } catch (Exception e) {
      logger.error("获取单位logo失败！" + e.getMessage());

    }

    return logoUrl;
  }

  /**
   * 根据域名获取logo.
   */
  public String[] loadRolMainLogoByDomain(String domainURL) {

    String[] logoURL = new String[4];

    try {
      InsPortal insPortal = insPortalDao.getEntity(domainURL);
      if (insPortal == null) {
        logger.warn("无法找到对应域名的单位！");
        /* throw new ServiceException(); */
      } else {
        // 修改了获取单位名称的代码_MaoJianGuo_2013-01-30_ROL-396.
        logoURL[0] = insPortal.getZhTitle();
        logoURL[2] = insPortal.getEnTitle();
        logoURL[1] = this.loadRolMainLogo(insPortal.getInsId());
      }

    } catch (Exception e) {

      logger.error("读取单位logo失败", e);
    }

    return logoURL;

  }
}
