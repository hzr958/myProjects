package com.smate.web.dyn.dao.group;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.group.GroupFile;

/**
 * 群组文件.
 * 
 * @author zk
 * 
 */
@Repository
public class GroupFileDao extends SnsHibernateDao<GroupFile, Long> {

  public GroupFile getGroupFile(Long groupId, Long fileId) {
    String hql = "from GroupFile where groupId = ? and fileId=? and fileStatus=0";
    return this.findUnique(hql, groupId, fileId);
  }
}
