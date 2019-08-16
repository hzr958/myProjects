package com.smate.core.base.utils.service.consts;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.dao.consts.ConstDicDao;
import com.smate.core.base.utils.exception.SmateException;
import com.smate.core.base.utils.model.consts.ConstDictionary2;

/**
 * 
 * @author zk
 * @since 6.0.1
 */
@Service("constDicManage")
@Transactional(rollbackOn = Exception.class)
public class ConstDicManageImpl implements ConstDicManage {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   */
  @Autowired
  private ConstDicDao constDicDao;

  @Override
  public List<ConstDictionary2> getConstByGategory(String gategory) throws SmateException {
    try {
      return constDicDao.findConstByCategory(gategory);
    } catch (Exception e) {
      logger.error("取常量错误", e);
      throw new SmateException(e);
    }
  }

}
