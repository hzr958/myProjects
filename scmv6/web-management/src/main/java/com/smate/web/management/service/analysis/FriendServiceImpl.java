package com.smate.web.management.service.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.management.dao.analysis.sns.FriendDao;

/**
 * 好友服务接口实现类.
 * 
 * @author lichangwen
 * 
 */
@Service("friendService")
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {

  /**
   * 
   */
  private static final long serialVersionUID = -5241515051929497408L;
  private static final String INITFRIENDGROUP = "psn_friend_group";
  private static final String RELATIONS = "fapp_relations";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private FriendDao friendDao;


  /**
   * 判断是否为好友
   */
  @Override
  public boolean isPsnFirend(Long curPsnId, Long psnId) {
    return friendDao.isFriend(curPsnId, psnId) > 0 ? true : false;
  }
}
