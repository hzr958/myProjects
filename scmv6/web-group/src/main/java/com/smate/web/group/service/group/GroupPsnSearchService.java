package com.smate.web.group.service.group;

import com.smate.web.group.model.group.GroupControl;
import com.smate.web.group.model.group.GroupFilter;
import com.smate.web.group.model.group.GroupKeyDisc;
import com.smate.web.group.model.group.GroupPsn;

/*
 * * 群组检索服务
 * 
 * @author zjh
 *
 */
public interface GroupPsnSearchService {

  /**
   * 获取群组信息(封装为原群组主表对象以供检索调用).
   * 
   * @param groupId
   * @return
   */
  GroupPsn getBuildGroupPsn(Long groupId);

  /**
   * 获取群组检索过滤信息.
   * 
   * @param groupId
   * @return
   */
  GroupFilter getFileter(Long groupId);

  /**
   * 获取群组控制记录.
   * 
   * @param groupId
   * @return
   */
  GroupControl getControl(Long groupId);

  /**
   * 获取群组学科关键词记录.
   * 
   * @param groupId
   * @return
   */
  GroupKeyDisc getKeyDisc(Long groupId);

}
