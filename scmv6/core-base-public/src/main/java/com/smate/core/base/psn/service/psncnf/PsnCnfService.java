package com.smate.core.base.psn.service.psncnf;

import java.util.List;

import com.smate.core.base.exception.ServiceException;

/**
 * 个人配置服务接口：包含保存、删除和获取记录。 方法要求传入：PsnCnfBase的子类
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfService {

  <T> T get(PsnCnfBase psnCnfBase) throws ServiceException;

  <T> T get(Long psnId, PsnCnfBase psnCnfBase) throws ServiceException;

  void save(PsnCnfBase psnCnfBase) throws ServiceException;

  void save(Long psnId, PsnCnfBase psnCnfBase) throws ServiceException;

  void save(Long psnId, List<PsnCnfBase> cnfList) throws ServiceException;

  void del(PsnCnfBase psnCnfBase) throws ServiceException;

  void del(Long psnId, PsnCnfBase psnCnfBase) throws ServiceException;

  <T> T gets(PsnCnfBase psnCnfBase) throws ServiceException;

  void log(Long psnId, Integer status) throws ServiceException;

  void log(Long psnId, Integer status, String errMsg) throws ServiceException;
}
