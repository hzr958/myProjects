package com.smate.sie.core.base.utils.service.ins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.SmateException;
import com.smate.sie.core.base.utils.dao.ins.SieConstInsTypeDao;
import com.smate.sie.core.base.utils.model.ins.SieConstInsType;

/**
 * 机构类型ServiceImpl
 * 
 * @author xr
 *
 */
@Service("SieConstInsTypeService")
@Transactional(rollbackFor = Exception.class)
public class SieConstInsTypeServiceImpl implements SieConstInsTypeService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstInsTypeDao sieConstInsTypeDao;

  @Override
  public Long getNatureByName(String zhName) throws SmateException {
    try {
      if (zhName == null) {
        return 99L;
      }
      // ConstDictionary c = sieconstDicDao.findConstByCategoryAndName("ins_type", name.trim());
      SieConstInsType c = sieConstInsTypeDao.getNatureByName(zhName);
      if (c != null) {
        return Long.valueOf(c.getNature());
      }
    } catch (Exception e) {
      logger.error("根据机构名获取机构类型失败", e);
      throw new SmateException("查询CONST_INS_TYPE表机构类型失败,zhName=" + zhName, e);
    }
    return 99L;
  }

}
