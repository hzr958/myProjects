package com.smate.web.dyn.service.dynamic.group;

import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;

/**
 * 群组动态显示服务
 * 
 * @author tsz
 *
 */
public interface GroupDynamicShowService {

  /**
   * // 获取群组动态列表
   * 
   * @param form
   */
  public void buildGroupDynList(GroupDynShowForm form) throws DynGroupException;

  /**
   * // 获取动态评论列表
   * 
   * @param form
   */
  public void getGroupDynCommentList(GroupDynShowForm form) throws DynGroupException;

  public void delGrpDyn(GroupDynShowForm form) throws DynGroupException;

  /**
   * 获取单个群组动态信息
   * 
   * @param form
   * @throws DynGroupException
   */
  public void buildGrpDynDetailInfo(GroupDynShowForm form) throws Exception;

  /**
   * 构建返回json格式的群组动态信息
   * 
   * @param form
   * @throws DynGroupException
   */
  public void buildGrpDynListJsonInfo(GroupDynShowForm form) throws DynGroupException;

}
