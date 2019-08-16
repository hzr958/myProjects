package com.smate.web.dyn.service.dynamic;

import java.util.List;
import java.util.Map;

import com.smate.core.base.app.model.AppVersionRecord;
import com.smate.web.dyn.form.dynamic.DynReplayInfo;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.IOSDynamicInfo;

/**
 * 科研之友IOS客户端动态获取业务接口
 * 
 * @author LJ
 *
 */

public interface IOSDynamicService {

  /**
   * 动态列表获取
   * 
   * @param form
   * @throws Exception
   */
  public List<IOSDynamicInfo> dynamicShow(Long psnid, Integer pagesize, Integer pagenumber) throws Exception;

  /**
   * 根据动态Id获取动态
   * 
   * @param psnid
   * @param dynId
   * @return
   * @throws Exception
   */
  public List<IOSDynamicInfo> getDynById(Long psnid, Long dynId, int flag) throws Exception;

  /**
   * 点赞
   * 
   * @param psnId
   * @param dynInfo
   * @return
   * @throws Exception
   */
  public Map<String, Object> addAward(DynamicForm form) throws Exception;

  /**
   * 分享
   * 
   * @param psnId
   * @param dynInfo
   * @return
   * @throws Exception
   */
  public Map<String, Object> quickshare(DynamicForm form) throws Exception;

  /**
   * 获取动态详细信息
   * 
   * @param dynId
   * @throws Exception
   */
  public IOSDynamicInfo buildDynamicDetail(Long psnId, Long dynId) throws Exception;

  /**
   * 获取动态回复信息列表
   * 
   * @param dynInfo
   */

  public List<DynReplayInfo> loadDynRely(DynamicForm form) throws Exception;

  /**
   * 获取动态统计数
   * 
   * @return
   * @throws Exception
   */
  public Map<String, Map<String, Object>> getStatistics(DynamicForm form) throws Exception;

  public AppVersionRecord getIosVersionInfo() throws Exception;

}
