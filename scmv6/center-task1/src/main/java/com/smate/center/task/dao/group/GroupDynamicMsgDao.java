package com.smate.center.task.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.grp.GroupDynamicMsg;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GroupDynamicMsgDao extends SnsHibernateDao<GroupDynamicMsg, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getNeedSendMailGrpId(List<Long> grpIds) {
    String hql =
        "select distinct(t.groupId) from GroupDynamicMsg t where t.createDate>trunc(sysdate) and t.status=0 and t.groupId in (:grpIds) order by t.groupId desc";
    return super.createQuery(hql).setParameterList("grpIds", grpIds).setMaxResults(3).list();
  }

}
