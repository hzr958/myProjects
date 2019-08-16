package com.smate.center.task.single.service.pub;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ConstPubTypeDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.ConstPubType;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 成果类别Service.
 * 
 */
@Service("constPubTypeService")
@Transactional(rollbackFor = ServiceException.class)
public class ConstPubTypeServiceImpl implements ConstPubTypeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPubTypeDao constPubTypeDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.publication.service.PublicationTypeService#getAll()
   */
  @Override
  public List<ConstPubType> getAll() throws ServiceException {
    try {
      return constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("getAll获取ConstPubType列表失败", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<ConstPubType> getTypes(int articleType) throws ServiceException {
    try {
      List<ConstPubType> ret = constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());
      for (ConstPubType constPubType : ret) {
        constPubType.setCount(
            constPubTypeDao.getPubTypeNum(constPubType.getId(), articleType, SecurityUtils.getCurrentUserId()));
      }
      return ret;
    } catch (Exception e) {
      logger.error("getAll获取ConstPubType列表失败", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public ConstPubType get(int id) throws ServiceException {

    ConstPubType ret;
    try {
      ret = constPubTypeDao.get(id, LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("get ConstPubType by id失败", e);
      throw new ServiceException(e);
    }
    return ret;
  }

  @Override
  public ConstPubType get(int id, Locale locale) throws ServiceException {

    ConstPubType ret;
    try {
      ret = constPubTypeDao.get(id, locale);
    } catch (Exception e) {
      logger.error("get ConstPubType by id失败", e);
      throw new ServiceException(e);
    }
    return ret;
  }

  @Override
  public ConstPubType endnoteGet(int id) throws ServiceException {
    ConstPubType ret;
    try {
      ret = constPubTypeDao.get(id);
    } catch (Exception e) {
      logger.error("get ConstPubType by id失败", e);
      throw new ServiceException(e);
    }
    return ret;
  }

  @Override
  public void pullConstPubTypeSyn(List<ConstPubType> list) throws ServiceException {
    try {
      this.constPubTypeDao.removeAll();
      for (ConstPubType constPubType : list) {
        this.constPubTypeDao.save(constPubType);
      }
    } catch (Exception e) {
      logger.error("pullConstPubTypeSyn接收成果类别同步错误.size:{}", list.size(), e);
      throw new ServiceException("pullConstPubTypeSyn接收成果类别同步错误.", e);
    }
  }

  @Override
  public String queryResultTypeName(Integer typeId) throws ServiceException {
    ConstPubType type = this.get(typeId);
    return type.getName();
  }

}
