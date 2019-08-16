package com.smate.web.group.service.group.consts;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstDisciplineDao;
import com.smate.core.base.consts.model.ConstDiscipline;

/**
 * 学科领域常量服务实现类
 * 
 * @author zjh
 *
 */
@Service("constDisciplineManage")
@Transactional(rollbackFor = Exception.class)
public class ConstDisciplineManageImpl implements ConstDisciplineManage {
  @Autowired
  private ConstDisciplineDao constDisciplineDao;
  private Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 根据学科Id得到相关的学科
   * 
   * @author zjh
   */
  @Override
  public String getDisciplineName(Long id, Locale locale) throws Exception {
    try {
      ConstDiscipline cd = this.constDisciplineDao.getConstDisciplineById(id);
      if (cd != null) {
        if (Locale.US.equals(locale)) {
          return cd.getDiscCode() + "-" + cd.getEnName();
        } else {
          return cd.getDiscCode() + "-" + cd.getZhName();
        }
      }
      return null;
    } catch (Exception e) {
      logger.error("获取学科领域名称错误.", e);
      throw new Exception(e);
    }
  }

  /**
   * 根据学科Id得到相关的学科
   * 
   * @author lhd
   */
  @Override
  public ConstDiscipline findDisciplineById(Long id) throws Exception {
    return constDisciplineDao.get(id);
  }


}
