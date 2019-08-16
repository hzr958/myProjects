package com.smate.center.open.service.group;

import java.io.Serializable;

/**
 * 群组同步接口
 * 
 * @author lhd
 *
 */
public interface GroupSnsService extends Serializable {

  /**
   * 保存群组统计信息
   * 
   * @author lhd
   * @param groupId
   * @throws Exception
   */
  void reCountGroupMembersByGroupId(Long groupId) throws Exception;
}
