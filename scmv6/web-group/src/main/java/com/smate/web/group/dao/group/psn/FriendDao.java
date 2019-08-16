package com.smate.web.group.dao.group.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.psn.Friend;

/**
 * 个人好友Dao.
 * 
 * cwli
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {

  public Long isFriend(Long psnId, Long friendPsnId) {
    if (psnId.equals(friendPsnId)) {
      return 1L;
    }
    String hql = "select count(*) from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

  public List<Object> getFriendList(Long psnId) {
    String sql =
        "select  t.friend_Psn_Id from PSN_FRIEND t left join Recent_Selected t2 on t.friend_psn_id = t2.selected_psn_id and t2.psn_Id =:psnId where  t.psn_Id=:psnId and exists (select 1 from person  p where p.psn_id = t.friend_psn_id) order by nvl(t2.selected_date,to_date('1900-01-01','yyyy-mm-dd')) desc ";

    return super.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
  }

}
