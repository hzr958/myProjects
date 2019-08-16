package com.smate.web.group.service.group.consts;

import java.util.Locale;

import com.smate.core.base.consts.model.ConstDiscipline;

public interface ConstDisciplineManage {
  /**
   * 根据学科id得到相关的学科
   * 
   * @author zjh
   * @param id
   * @return
   * @throws ServiceException
   */

  public String getDisciplineName(Long id, Locale locale) throws Exception;

  public ConstDiscipline findDisciplineById(Long id) throws Exception;

}
