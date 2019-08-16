package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.sie.center.task.dao.SieConstPubTypeDao;
import com.smate.sie.center.task.model.SieConstPubType;

/**
 * 成果类别Service.
 * 
 * @author jszhou
 *
 */
@Service("sieConstPubTypeService")
@Transactional(rollbackFor = Exception.class)
public class SieConstPubTypeServiceImpl implements SieConstPubTypeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstPubTypeDao constPubTypeDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.publication.service.PublicationTypeService#getAll()
   */
  @Override
  public List<SieConstPubType> getAll() throws Exception {
    try {
      return constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("getAll获取SieConstPubType列表失败", e);
      throw new Exception(e);
    }
  }

  @Override
  public SieConstPubType get(int id) throws Exception {
    SieConstPubType ret;
    try {
      ret = constPubTypeDao.get(id);
    } catch (Exception e) {
      logger.error("get SieConstPubType by id失败", e);
      throw new Exception(e);
    }
    return ret;
  }

}
