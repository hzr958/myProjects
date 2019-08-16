package com.smate.web.group.service.grp.manage;

import com.smate.web.group.action.grp.form.GrpMainForm;

/**
 * 群组创建业务处理service
 * 
 * @author zzx
 *
 */
public interface GrpCreateService {
  /**
   * 保存群组编辑
   * 
   * @param form
   * @return
   */
  void updateGrp(GrpMainForm form) throws Exception;

  /**
   * 保存-创建群组
   * 
   * @param form
   * @return
   */
  void createGrp(GrpMainForm form) throws Exception;

  /**
   * 复制群组
   * 
   * @param form
   * @throws Exception
   */
  void doCopyGrp(GrpMainForm form) throws Exception;

  /**
   * 保存群组头像
   * 
   * @param form
   */
  void saveGrpAvartars(GrpMainForm form) throws Exception;
}
