package com.smate.web.group.service.group;

import com.smate.web.group.form.GroupPsnForm;


/**
 * 同步群组信息到其他的项目
 * 
 * @author zjh
 *
 */
public interface SyncGroupService {

  /**
   * 同步数据到其他的系统
   */
  public Object syncGroupInfo(GroupPsnForm form) throws Exception;

}
