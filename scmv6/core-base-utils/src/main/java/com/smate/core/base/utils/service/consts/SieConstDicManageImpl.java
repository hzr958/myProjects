package com.smate.core.base.utils.service.consts;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.dao.consts.SieConstDicDao;
import com.smate.core.base.utils.exception.SmateException;

/**
 * @author yxs
 * @descript sie系统常量查询接口
 * 
 */
@Service("sieConstDicManage")
@Transactional(rollbackOn = Exception.class)
public class SieConstDicManageImpl implements SieConstDicManage {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstDicDao sieconstDicDao;

  @Override
  public List<ConstDictionary> getSieConstByGategory(String category) throws SmateException {
    try {
      return sieconstDicDao.findConstByCategory(category);
    } catch (Exception e) {
      logger.error("取常量错误", e);
      throw new SmateException(e);
    }
  }

  @Override
  public Long getNatureByName(String name) throws SmateException {
    try {
      if (name == null) {
        return 99L;
      }
      ConstDictionary c = sieconstDicDao.findConstByCategoryAndName("ins_type", name.trim());
      if (c != null) {
        return Long.valueOf(c.getKey().getCode());
      }
    } catch (Exception e) {
      logger.error("查询单位类型失败", e);
      throw new SmateException("查询CONST_DICTIONARY表单位类型失败,name=" + name, e);
    }
    return 99L;
  }
}
