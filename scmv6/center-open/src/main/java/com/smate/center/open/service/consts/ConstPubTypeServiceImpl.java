package com.smate.center.open.service.consts;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.model.consts.ConstPubType;


/**
 * 成果类别Service.
 * 
 */
@Service("constPubTypeService")
@Transactional(rollbackFor = Exception.class)
public class ConstPubTypeServiceImpl implements ConstPubTypeService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstPubTypeDao constPubTypeDao;

  @Override
  public List<ConstPubType> getAll() throws Exception {
    try {
      return constPubTypeDao.getAllTypes(LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("getAll获取ConstPubType列表失败", e);
      throw new Exception("getAll获取ConstPubType列表失败", e);
    }
  }

  @Override
  public ConstPubType get(int id) throws Exception {
    ConstPubType ret;
    try {
      ret = constPubTypeDao.get(id, LocaleContextHolder.getLocale());
    } catch (Exception e) {
      logger.error("get ConstPubType by id失败", e);
      throw new Exception(e);
    }
    return ret;
  }

  @Override
  public String queryResultTypeName(Integer typeId) throws Exception {
    ConstPubType type = this.get(typeId);
    return type.getName();
  }

}
