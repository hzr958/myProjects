package com.smate.web.psn.service.psncnf;

import java.util.List;

import com.smate.web.psn.exception.ServiceException;

/**
 * 个人权限消息（成果、项目、工作经历和教育经历）处理接口
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfReceive {
  /**
   * 根据psnId和idList进行删除（idList可能是pubIdList等）
   * 
   * @param psnId
   * @param idList
   * @throws ServiceException
   */
  void delCnfByIds(Long psnId, List<Long> idList) throws ServiceException;

  /**
   * 使用指定权限保存
   * 
   * @param psnId
   * @param objs 如：["pubId1,权限值1","pubId2,权限值2"]
   * @throws ServiceException
   */
  void saveCnfByObjs(Long psnId, List<String> objs) throws ServiceException;

  /**
   * 使用默认权限保存
   * 
   * @param psnId
   * @param ids
   * @throws ServiceException
   */
  void saveCnfByIds(Long psnId, String ids) throws ServiceException;

}
