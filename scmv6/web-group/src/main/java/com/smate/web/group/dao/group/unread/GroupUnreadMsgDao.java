package com.smate.web.group.dao.group.unread;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.unread.GroupDynamicMsg;

/**
 * 群组未读消息dao
 * 
 * @author YJ
 *
 */

@Repository(value = "groupUnreadMsgDao")
public class GroupUnreadMsgDao extends SnsHibernateDao<GroupDynamicMsg, Long> {

  /**
   * 获取未读消息数
   * 
   * @param psnId 人员id
   * @param grpId 群组id
   * @return 查询V_GRP_MEMBER 和 v_group_dynamic_msg
   */
  public Long getGroupUnreadCount(Long psnId, Long grpId) {
    // 下面这个sql效率更低
    // select count(*) from GroupDynamicMsg d where d.groupId =:grpId and d.createDate >= (select
    // m.lastVisitDate from GrpMember m where m.psnId =:psnId and m.grpId =d.groupId )
    String hql =
        "select count(*) from GroupDynamicMsg d where exists (select 1 from GrpMember m where d.createDate >= m.lastVisitDate and m.psnId =:psnId and m.grpId =d.groupId) and d.groupId =:grpId";
    return (Long) this.createQuery(hql).setParameter("psnId", psnId).setParameter("grpId", grpId).uniqueResult();
  }

}
