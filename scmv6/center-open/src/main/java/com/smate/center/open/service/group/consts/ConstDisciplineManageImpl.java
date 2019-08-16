package com.smate.center.open.service.group.consts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.ConstDisciplineDao;
import com.smate.center.open.model.group.ConstDiscipline;

/**
 * 学科领域常量服务实现类
 * 
 * @author lhd
 *
 */
@Service("constDisciplineManage")
@Transactional(rollbackFor = Exception.class)
public class ConstDisciplineManageImpl implements ConstDisciplineManage {
  @Autowired
  private ConstDisciplineDao constDisciplineDao;

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
