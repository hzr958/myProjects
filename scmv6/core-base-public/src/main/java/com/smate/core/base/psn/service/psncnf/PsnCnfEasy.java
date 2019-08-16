package com.smate.core.base.psn.service.psncnf;

import java.util.List;

import com.smate.core.base.exception.ServiceException;

/**
 * 个人配置内部接口：查询、保存和删除
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfEasy {

  PsnCnfBase get(PsnCnfBase psnCnfBase) throws ServiceException;

  void save(PsnCnfBase psnCnfBase) throws ServiceException;

  void del(PsnCnfBase psnCnfBase) throws ServiceException;

  List<?> gets(PsnCnfBase psnCnfBase) throws ServiceException;

}
