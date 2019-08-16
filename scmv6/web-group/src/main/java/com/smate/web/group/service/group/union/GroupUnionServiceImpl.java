package com.smate.web.group.service.group.union;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.group.dao.group.OpenGroupUnionDao;

/**
 * 群组互联实现类
 * 
 * @author AiJiangBin
 *
 */

@Service("groupUnionService")
@Transactional(rollbackFor = Exception.class)
public class GroupUnionServiceImpl implements GroupUnionService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;


  @Override
  public Boolean whetherGroupUnion(Long groupId) {
    if (groupId == null || groupId == 0L) {
      return false;
    }
    return openGroupUnionDao.groupHasUnionByGroupId(groupId);
  }


}
