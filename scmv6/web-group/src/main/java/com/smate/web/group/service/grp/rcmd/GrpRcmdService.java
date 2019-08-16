package com.smate.web.group.service.grp.rcmd;

import com.smate.web.group.action.grp.form.GrpBaseForm;

/**
 * 群组推荐service
 * 
 * @author AiJiangBin
 *
 */
public interface GrpRcmdService {


  /**
   * 获取群组推荐列表
   * 
   * @param form
   */
  public void getRcmdGrpList(GrpBaseForm form) throws Exception;

  /**
   * 获取群组推荐列表统计数
   * 
   * @param form
   */
  public void getRcmdGrpListStatistics(GrpBaseForm form) throws Exception;

  public void optionRcmdGrp(Long psnId, Long grpId, Integer rcmdStatus) throws Exception;

}
