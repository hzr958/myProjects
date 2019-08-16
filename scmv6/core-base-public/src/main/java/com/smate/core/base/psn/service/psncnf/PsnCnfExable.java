package com.smate.core.base.psn.service.psncnf;

import com.smate.core.base.exception.ServiceException;

/**
 * 个人配置：扩展接口（如：工作经历新增、修改或删除后，列表结果汇总重算）
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfExable {

  void compose(PsnCnfBase psnCnfBase) throws ServiceException;
}
