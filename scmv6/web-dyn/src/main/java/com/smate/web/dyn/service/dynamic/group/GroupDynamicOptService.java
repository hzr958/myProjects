package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynOptForm;
import com.smate.web.dyn.form.dynamic.group.GroupDynProduceForm;

/**
 * 群组动态操作服务接口
 * 
 * @author tsz
 *
 */
public interface GroupDynamicOptService {

  /**
   * // 动态赞
   * 
   * @throws DynGroupException
   */
  public Map<String, Object> groupDynAward(GroupDynOptForm form) throws DynGroupException;

  /**
   * // 动态评论
   * 
   * @throws DynGroupException
   */
  public void groupDynComment(GroupDynOptForm form) throws DynGroupException;

  /**
   * // 动态分享
   * 
   * @throws DynGroupException
   */
  public void groupDynShare(GroupDynOptForm form) throws DynGroupException;

  /**
   * 发布群组动态
   * 
   * @param form
   * @return error 失败 success 成功
   * @throws Exception
   */
  String produceGroupDyn(GroupDynProduceForm form) throws DynGroupException;

  /**
   * 单纯的发布群组动态，不带更新资源统计数
   * 
   * @param form
   * @return
   * @throws DynGroupException
   */
  String produceGroupDynWithoutUpdateStatistics(GroupDynProduceForm form) throws DynGroupException;

}
