package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.batch.dao.sns.pub.InstitutionAddDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.center.batch.model.sns.pub.InstitutionAdd;

/**
 * 单位机构服务接实现类.
 * 
 * @author lichangwen
 * 
 */
@Service("institutionAddManager")
@Transactional(rollbackFor = Exception.class)
public class InstitutionAddManagerImpl implements InstitutionAddManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InstitutionAddDao institutionDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.profile.InstitutionManager#findListByName (java.lang.String)
   */
  @Override
  public String findListByName(String name) throws ServiceException {
    try {
      List<InstitutionAdd> inses = institutionDao.findListByName(name);
      String value = "";
      if (inses == null) {
        return null;
      }
      for (InstitutionAdd ins : inses) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.US)) {
          if (ins.getEnAddress() != null)
            value += ins.getEnName() + "|" + ins.getId() + " \n ";
        } else {
          if (ins.getZhName() != null)
            value += ins.getZhName() + "|" + ins.getId() + " \n ";
        }
      }
      return value;
    } catch (DaoException e) {
      logger.error("去单位列表出错:", e);
      throw new ServiceException();
    }

  }

  /**
   * 根据机构名称获取机构列表.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  @Override
  public List<InstitutionAdd> getListByName(String name) throws DaoException {
    return institutionDao.findListByName(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.scmweb.service.profile.InstitutionManager#getInstitution (java.lang.Long)
   */
  @Override
  public InstitutionAdd getInstitution(Long insId) throws ServiceException {
    try {

      return institutionDao.findById(insId);

    } catch (DaoException e) {
      logger.error("取单位列表出错:", e);
      throw new ServiceException();
    }
  }

  @Override
  public void saveInstitution(InstitutionAdd institution) throws ServiceException {

    Assert.notNull(institution, "institution不允许为空！");
    try {
      institutionDao.save(institution);
    } catch (Exception e) {

      logger.error("同步更新R库institution表失败！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long getInsIdByName(String insNameZh, String insNameEn) throws ServiceException {
    try {
      return institutionDao.getInsIdByName(insNameZh, insNameEn);
    } catch (DaoException e) {
      logger.error("根据单位名查找单位ID出错", e);
    }
    return null;
  }

  @Override
  public void saveInstitution(InstitutionRol insRol) throws ServiceException {
    try {
      InstitutionAdd institution = this.institutionDao.get(insRol.getId());
      if (institution == null)
        institution = new InstitutionAdd();
      institution.setId(insRol.getId());
      institution.setZhName(insRol.getZhName());
      institution.setEnName(insRol.getEnName());
      institution.setZhAddress(insRol.getZhAddress());
      institution.setEnAddress(insRol.getEnAddress());
      institution.setContactPerson(insRol.getContactPerson());
      institution.setUrl(insRol.getUrl());
      institution.setTel(insRol.getTel());
      institution.setStatus(insRol.getStatus());
      institution.setNature(insRol.getNature());
      institution.setPostcode(insRol.getPostcode());
      institution.setAbbreviation(insRol.getAbbreviation());
      institutionDao.save(institution);
    } catch (Exception e) {
      logger.error("从单位端同步单位失败！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> getInsIdsByName(String name) throws ServiceException {

    try {
      if (StringUtils.isBlank(name))
        return null;
      return this.institutionDao.getInsIdsByName(name);
    } catch (Exception e) {
      logger.error("模糊匹配机构名称，获取机构ID列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getInsIdByName(String name) throws ServiceException {

    try {
      if (StringUtils.isBlank(name))
        return null;
      return this.institutionDao.getInsIdByName(name);
    } catch (Exception e) {
      logger.error("匹配机构名称，获取机构ID", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 模糊匹配机构名称，获取机构ID列表.
   * 
   * @param name
   * @return
   */
  @Override
  public List<InstitutionAdd> getInsListByName(String name, int size) {
    return this.institutionDao.getInsListByName(name, size);
  }
}
