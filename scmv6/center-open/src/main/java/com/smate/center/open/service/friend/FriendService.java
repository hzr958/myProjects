package com.smate.center.open.service.friend;


/**
 * 工作经历接口.
 * 
 * @author lichangwen
 * 
 */
public interface FriendService {

  /**
   * 判断传入的psnId是否与curPsnId是否是好友，curPsnId为当前节点用户lqh add.
   * 
   * @param psnId
   * @return
   */
  boolean isPsnFirend(Long curPsnId, Long psnId) throws Exception;
}
