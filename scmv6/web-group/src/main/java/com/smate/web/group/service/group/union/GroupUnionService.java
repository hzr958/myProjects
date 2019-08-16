package com.smate.web.group.service.group.union;

/**
 * 群组关联接口
 * 
 * @author AiJiangBin
 *
 */
public interface GroupUnionService {

  /**
   * 群组是否关联
   * 
   * @param groupId
   * @return
   */
  public Boolean whetherGroupUnion(Long groupId);

}
