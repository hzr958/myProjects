package com.smate.core.base.utils.service.consts;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.constant.SieConstPatType;
import com.smate.core.base.utils.dao.consts.SieConstPatTypeDao;
import com.smate.core.base.utils.exception.SysServiceException;

@Service("sieConstPatTypeService")
@Transactional(rollbackOn = Exception.class)
public class SieConstPatTypeServiceImpl implements SieConstPatTypeService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstPatTypeDao sieConstPatTypeDao;

  @Override
  public List<SieConstPatType> getAllTypes() throws SysServiceException {
    try {
      List<SieConstPatType> list = sieConstPatTypeDao.getAllTypes();
      return list;
    } catch (Exception e) {
      logger.error("获取CONST_PAT_TYPE表所有专利类型出错");
      throw new SysServiceException("获取CONST_PAT_TYPE表所有专利类型出错", e);
    }
  }

}
