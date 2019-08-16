package com.smate.core.web.sns.menu.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.InsPortal;



/**
 * 登录人员单位信息类
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class InsPortalManagerImpl implements InsPortalManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsPortalDao insPortalDao;

  /**
   * 通过域名获取单位域名等信息.
   */
  public InsPortal getInsPortalByDomain(String domain) throws SysServiceException {

    try {

      return insPortalDao.getEntity(domain);
    } catch (Exception e) {
      logger.error("getInsPortalByDomain获取域名信息错误 : ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 保存单位域名等信息.
   * 
   * @param insPortal
   * @throws ServiceException
   */
  public void saveInsPortal(InsPortal insPortal) throws SysServiceException {
    try {

      insPortalDao.saveEntity(insPortal);
    } catch (Exception e) {
      logger.error("saveInsPortal保存域名信息错误 : ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 同步单位域名等信息时保存单位保存.
   * 
   * @param insPortal
   * @throws ServiceException
   */
  public void syncSaveInsPortal(InsPortal insPortal) throws SysServiceException {

    try {
      InsPortal pPortal = insPortalDao.get(insPortal.getInsId());
      if (pPortal != null) {
        pPortal.setDefaultLang(insPortal.getDefaultLang());
        pPortal.setDomain(insPortal.getDomain());
        pPortal.setEnTitle(insPortal.getEnTitle());
        // pPortal.setIndexPage(insPortal.getIndexPage());
        pPortal.setInitTitle(insPortal.getInitTitle());
        pPortal.setLogo(insPortal.getLogo());
        // pPortal.setRolNodeId(insPortal.getRolNodeId());
        // pPortal.setSnsNodeId(insPortal.getSnsNodeId());
        pPortal.setSwitchLang(insPortal.getSwitchLang());
        pPortal.setZhTitle(insPortal.getZhTitle());
        insPortalDao.saveEntity(pPortal);
      } else {
        insPortalDao.saveEntity(insPortal);
      }

    } catch (Exception e) {
      logger.error("syncSaveInsPortal保存域名信息错误 : ", e);
      throw new SysServiceException(e);
    }
  }

  @Transactional(rollbackFor = Exception.class, readOnly = true)
  public List<InsPortal> findUserRolUrl(Long psnId, Long insId) throws SysServiceException {
    try {
      if (psnId != null) {
        Locale local = LocaleContextHolder.getLocale();

        List<InsPortal> portal = insPortalDao.findUserRolUrl(psnId);
        if (portal != null && portal.size() > 0) {
          for (InsPortal insPortal : portal) {
            // if (insPortal.getInsId().equals(insId)) {
            // continue;
            // }
            if (local.equals(Locale.US)) {
              String enTitle = insPortal.getEnTitle();
              if (StringUtils.isBlank(enTitle)) {
                enTitle = insPortal.getZhTitle();
              }
              insPortal.setInitTitle(enTitle);
            } else {
              String zhTitle = insPortal.getZhTitle();
              if (StringUtils.isBlank(zhTitle)) {
                zhTitle = insPortal.getEnTitle();
              }
              insPortal.setInitTitle(zhTitle);
            }
          }
        }

        return portal;
      }
    } catch (SysDataException e) {
      logger.error("获取人员所有Rol地址错误 : ", e);
      throw new SysServiceException(e);
    }
    return null;
  }

  @Override
  public InsPortal getInsPortalByInsId(Long insId) throws SysServiceException {

    return insPortalDao.get(insId);
  }

  @Override
  public InsPortal saveInsLogo(Long insId, String path) throws SysServiceException {
    try {

      InsPortal insPortal = insPortalDao.get(insId);
      if (insPortal != null) {
        insPortal.setLogo(path);
        this.saveInsPortal(insPortal);
      }
      return insPortal;
    } catch (Exception e) {
      logger.error("修改单位LOGO错误 : ", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public Integer getInsNodeId(Long insId) throws SysServiceException {
    try {

      return this.insPortalDao.getInsNode(insId);
    } catch (Exception e) {
      logger.error("获取单位节点错误 : ", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public void syncInsPortalByOldInsPortal(Map<String, Object> oldData) throws SysServiceException {

    try {
      InsPortal insPortal = new InsPortal();
      Long insId = Long.valueOf(oldData.get("INS_ID").toString());
      String initTitle = oldData.get("PORTAL_NAME") == null ? null : oldData.get("PORTAL_NAME").toString();
      String zhTitle = oldData.get("TITLE") == null ? null : oldData.get("TITLE").toString();
      String enTitle = oldData.get("ETITLE") == null ? null : oldData.get("ETITLE").toString();
      String domain = oldData.get("DOMAIN") == null ? null : oldData.get("DOMAIN").toString();
      String indexPage = oldData.get("INDEX_PAGE") == null ? null : oldData.get("INDEX_PAGE").toString();
      String defaultLang = oldData.get("DEFAULT_LANG") == null ? null : oldData.get("DEFAULT_LANG").toString();
      String switchLang = oldData.get("SWITCH_LANG") == null ? null : oldData.get("SWITCH_LANG").toString();

      insPortal.setInsId(insId);
      insPortal.setInitTitle(initTitle);
      insPortal.setDomain(domain);
      insPortal.setZhTitle(zhTitle);
      insPortal.setEnTitle(enTitle);
      // insPortal.setIndexPage(indexPage);
      insPortal.setDefaultLang(defaultLang);
      insPortal.setSwitchLang(switchLang);
      // 节点信息
      // insPortal.setRolNodeId(10000);
      // insPortal.setSnsNodeId(1);

      this.insPortalDao.save(insPortal);
    } catch (Exception e) {
      logger.error("同步V2.6数据错误 : ", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 查找单位域名.
   */
  public List<InsPortal> searchPortalByKey(String key) throws SysServiceException {
    try {
      return this.insPortalDao.findInsPortalByKey(key);
    } catch (SysDataException e) {
      logger.error("查询单位域名时出错了！", e);
      throw new SysServiceException(e);
    }
  }

  @Override
  public List<InsPortal> getInsPortalByInsIds(List<Long> insIds) throws SysServiceException {
    try {
      return this.insPortalDao.getInsPortalByInsIds(insIds);
    } catch (Exception e) {
      logger.error("获取单位域名列表出错了！", e);
      throw new SysServiceException(e);
    }
  }

}
