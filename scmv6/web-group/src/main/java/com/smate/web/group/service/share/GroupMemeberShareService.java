package com.smate.web.group.service.share;

import com.smate.web.group.form.GroupShareForm;

public interface GroupMemeberShareService {

  /**
   * 处理群组推荐人员
   * 
   * @param from
   * @throws Exception
   */
  public void getRecommendPsnIds(GroupShareForm form) throws Exception;

}
