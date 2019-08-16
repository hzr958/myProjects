package com.smate.web.dyn.service.psn;

import com.smate.web.dyn.exception.DynException;

/**
 * 个人信息服务接口
 * 
 * @author Administrator
 *
 */
public interface PersonManager {

  /**
   * 列表显示人员时显示的 “工作单位，部门，职称” 构建
   * 
   * @param psnId
   * @return
   * @throws DynException
   */
  String getPsnViewWorkHisInfo(Long psnId) throws DynException;

}
