package com.smate.center.task.dao.sns.psn;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.FriendSysRecommend;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

@Repository
public class FriendSysRecommendDao extends SnsHibernateDao<FriendSysRecommend, Serializable> {

  public void upAppSettingConstantByRecommend(String key, Object value) {
    String sql = "update application_setting set value=? where key=?";
    super.update(sql, new Object[] {value, key});
  }

  /**
   * 最少两个学科相同才推荐.
   * 
   * @param psnId
   * @param page
   * @return
   */
  public Page findFriendDisByPsnId(Long psnId, Page page) {
    String sql =
        "select t2.psn_id,count(t2.psn_id) from psn_discipline t,psn_discipline t2,person_know t3 where t2.psn_id=t3.psn_id and t3.is_addfrd=0 and t3.is_private=0 and t3.is_login=1 and t.dis_id=t2.dis_id and t.psn_id=? and t2.psn_id not in(select t4.friend_psn_id from psn_friend t4 where t4.psn_id=?) and t2.psn_id not in(select t5.temp_psn_id from psn_friend_sys t5 where t5.psn_id=?) group by t2.psn_id having count(t2.psn_id)>1 order by t2.psn_id asc";
    return super.QueryTable(sql, new Object[] {psnId, psnId, psnId}, page);
  }

  public void findRecommendByDelPsnIds(Long psnId) {
    String hql = "delete from FriendSysRecommend where psnId= :psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public boolean isMax(Long psnId) throws DaoException {
    String hql = "select count(id) from FriendSysRecommend where psnId = :psnId";
    return (Long) createQuery(hql).setParameter("psnId", psnId).uniqueResult() >= 100 ? true : false;
  }

  @SuppressWarnings("rawtypes")
  public FriendSysRecommend getRecommendMin(Long psnId) throws DaoException {
    String hql = "from FriendSysRecommend where psnId = :psnId order by score asc";
    List list = super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(1).list();
    return CollectionUtils.isNotEmpty(list) ? (FriendSysRecommend) list.get(0) : null;
  }

  public FriendSysRecommend getFriendSysRecommend(Long psnId, Long tempPsnId) {
    String hql = "from FriendSysRecommend where psnId = :psnId and tempPsnId = :tempPsnId";
    return (FriendSysRecommend) super.createQuery(hql).setParameter("psnId", psnId).setParameter("tempPsnId", tempPsnId)
        .uniqueResult();
  }

}
