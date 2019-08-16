package com.smate.web.group.dao.group;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GroupBriefDescription;

/**
 * 群组简介Dao
 * 
 * @author lhd
 *
 */
@Deprecated
@Repository
public class GroupBriefDescriptionDao extends SnsHibernateDao<GroupBriefDescription, Serializable> {

  public GroupBriefDescription findMyGroupBrief(Long groupId) throws Exception {
    String hql = "from GroupBriefDescription where groupId=? ";
    return super.findUnique(hql, groupId);
  }

  public void updateMyGroupBrief(GroupBriefDescription briefDescription) throws Exception {
    String hql = "update GroupBriefDescription t set t.edt=?,t.groupBriefDescription=? where t.groupId=? ";
    super.createQuery(hql, briefDescription.getEdt(), briefDescription.getGroupBriefDescription(),
        briefDescription.getGroupId()).executeUpdate();
  }

}
