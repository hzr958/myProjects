package com.smate.web.management.service.analysis;

import java.io.Serializable;


/**
 * 工作经历服务接口.
 * 
 * @author lichangwen
 * 
 */
public interface FriendService extends Serializable {

  boolean isPsnFirend(Long curPsnId, Long psnId);


}
