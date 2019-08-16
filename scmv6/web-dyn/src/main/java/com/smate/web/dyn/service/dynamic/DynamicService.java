package com.smate.web.dyn.service.dynamic;

import java.util.Map;

import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 动态相关服务类接口
 * 
 * @author zk
 *
 */
public interface DynamicService {

  /**
   * 动态列表获取
   * 
   * @param form
   * @throws Exception
   */
  public void dynamicShow(DynamicForm form) throws Exception;

  /**
   * 删除动态
   * 
   * @param from
   * @throws Exception
   */
  public void deleteDyn(DynamicForm form) throws Exception;

  /**
   * 屏蔽人员动态
   * 
   * @param form
   * @throws Exception
   */
  void skipPsnDyn(DynamicForm form) throws Exception;

  /**
   * 屏蔽此类动态
   * 
   * @param form
   * @throws Exception
   */
  void skipTypeDyn(DynamicForm form) throws Exception;

  /**
   * 获取动态内容
   * 
   * @param form
   * @throws DynException
   */
  public void buildDynamicDetail(DynamicForm form) throws DynException;

  public void getShareTxt(DynamicForm form) throws Exception;

  public void buildDynPubRecommend(DynamicForm form, Map<String, Object> map);

  /**
   * 分享统计数加一接口用
   * 
   * @param form
   * @throws Exception
   */
  public void updateResShareStatic(DynamicForm form) throws Exception;

  /**
   * 获取资源删除状态 return 0:未删除， 1：已删除
   * 
   * @param resType
   * @param resId
   * @throws DynException
   */
  int findResDeleteStatus(Integer resType, Long resId) throws DynException;
}
