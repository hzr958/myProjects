package com.smate.center.batch.service.dynamic;

import com.smate.center.batch.tasklet.dynamic.AttentionDynamicForm;

/**
 * 关注成员动态任务
 * 
 * @author aijiangbin
 *
 */
public interface AttentionDynamicService {

  /**
   * 添加关注动态
   * 
   * @param psnId 当前人， 关注的人
   * @param attPsnId
   */
  public void addAttentionDynamic(AttentionDynamicForm form);

  /**
   * 删除关注动态
   * 
   * @param psnId 当前人， 关注的人
   * @param attPsnId
   */
  public void delAttentionDynamic(AttentionDynamicForm form);

  /**
   *
   * @param psnId 当前人， 关注的人
   * @param attPsnId
   */
  public void getDynamicMsgCount(AttentionDynamicForm form);

  /**
   * 处理当前动态
   * 
   * @param form
   */
  public void dealAttentionDynamic(AttentionDynamicForm form);
}
