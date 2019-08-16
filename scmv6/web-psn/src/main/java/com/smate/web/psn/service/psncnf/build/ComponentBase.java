package com.smate.web.psn.service.psncnf.build;

import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;


/**
 * 个人配置：组件接口
 * 
 * @author zhuangyanming
 * 
 */
interface ComponentBase {
  /**
   * 组装方法
   * 
   * @param cnfBuild
   * @throws ServiceException
   */
  void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException;
}
