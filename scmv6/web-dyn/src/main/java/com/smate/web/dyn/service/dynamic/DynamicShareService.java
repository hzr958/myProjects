package com.smate.web.dyn.service.dynamic;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 分享动态服务接口
 * 
 * @author zk
 *
 */
public interface DynamicShareService {

  /**
   * 分享动态
   * 
   * @param form
   * @throws DynException
   */
  void shareDynamic(DynamicForm form) throws DynException;

  /**
   * 分享到站外，ajax添加分享记录
   * 
   * @param form
   * @throws DynException
   */
  void ajaxAddResShareCounts(DynamicForm form) throws DynException;

  void addPdwhPubShareStatistics(Long resId, DynamicForm form) throws DynException;

  void addPubShareStatistics(DynamicForm form, Long snsPubId) throws DynException;

  /**
   * 操作个人库关联成果，同步基准库和其他关联成果数据
   * 
   * @param form
   * @throws DynException
   */
  void sysSnsPdwhShareStatistics(DynamicForm form) throws DynException;

  /**
   * 操作基准库关联成果，同步个人库数据
   * 
   * @param form
   * @throws DynException
   */
  void sysPdwhSnsShare(DynamicForm form) throws DynException;
}
