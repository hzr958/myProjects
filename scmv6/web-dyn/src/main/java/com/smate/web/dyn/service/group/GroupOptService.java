package com.smate.web.dyn.service.group;


/**
 * 群组操作服务类
 * 
 * @author zk
 *
 */
public interface GroupOptService {


  /**
   * 获取当前操作人 与群组的关系
   * 
   */
  Integer getRelationWithGroup(Long psnId, Long groupId) throws Exception;

  /**
   * 获取当前操作人 与新群组的关系
   * 
   * @param psnId
   * @param groupId
   * @return
   * @throws Exception
   */
  Integer getRelationWithGrp(Long psnId, Long groupId) throws Exception;


}
