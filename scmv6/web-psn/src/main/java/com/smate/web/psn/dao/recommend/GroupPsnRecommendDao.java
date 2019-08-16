package com.smate.web.psn.dao.recommend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.friend.InviteJionForm;
import com.smate.web.psn.model.recommend.GroupPsnRecommend;

/**
 * 群组推荐人员Dao
 * 
 * @author lhd
 *
 */
@Repository
public class GroupPsnRecommendDao extends SnsHibernateDao<GroupPsnRecommend, Long> {

  /**
   * 根据groupId查找群组推荐人员psnId
   * 
   * @param form
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> findRecommendPsnIdByGroupId(InviteJionForm form) {
    String hql =
        "select t.recommendPsnId from GroupPsnRecommend t where t.groupId=:groupId and not exists(select t2.psnId from GroupInvitePsn t2 where t2.groupId=:groupId2 and t.recommendPsnId=t2.psnId and (t2.isAccept='1' or t2.isAccept='2' or t2.isAccept is null) and t2.status='01') order by t.score desc";
    Page page = form.getPage();
    List<Long> recommendPsnIdList = super.createQuery(hql).setParameter("groupId", form.getGroupId())
        .setParameter("groupId2", form.getGroupId()).setFirstResult(page.getFirst() - 1).setMaxResults(10).list();
    return recommendPsnIdList;
  }
}
