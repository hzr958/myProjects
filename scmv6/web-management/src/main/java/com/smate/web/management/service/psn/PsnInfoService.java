package com.smate.web.management.service.psn;

import com.smate.web.management.model.psn.PsnInfoForm;

/**
 * 人员信息显示服务接口
 * 
 * @author zll
 * 
 */
public interface PsnInfoService {
  /**
   * 分页显示人员信息
   * 
   * @param page
   * @param form
   * @return
   */

  void getPsnInfo(PsnInfoForm form);


  void getPsnEmailListInfo(PsnInfoForm form);



  String dealMergeCount(PsnInfoForm form);

}
