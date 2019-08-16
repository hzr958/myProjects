package com.smate.center.open.service.group.consts;

import com.smate.center.open.model.group.ConstDiscipline;


/**
 * 学科领域常量服务类
 * 
 * @author lhd
 *
 */
public interface ConstDisciplineManage {

  /**
   * 根据学科id得到相关的学科
   * 
   * @author lhd
   * @param id
   * @return
   * @throws ServiceException
   */
  ConstDiscipline findDisciplineById(Long id) throws Exception;

}
