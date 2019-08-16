package com.smate.web.psn.dao.recommend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.friend.InviteJionForm;
import com.smate.web.psn.model.recommend.FriendSysRecommend;

/**
 * 个人好友系统智能推荐dao
 * 
 * @author lhd
 * 
 */
@Repository
public class FriendSysRecommendDao extends SnsHibernateDao<FriendSysRecommend, Long> {

  /**
   * 查询个人系统推荐好友
   * 
   * @param form
   * @param pageSize
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> findFriendSysRecommend(InviteJionForm form, int pageSize) {
    String hql = "select t.tempPsnId from FriendSysRecommend t where t.psnId=:psnId "
        + "and not exists( select t2.recommendPsnId from GroupPsnRecommend t2 "
        + "where t2.groupId=:groupId and t.tempPsnId=t2.recommendPsnId) "
        + "and not exists(select t3.psnId from GroupInvitePsn t3 "
        + "where t3.groupId=:groupId2 and t.tempPsnId=t3.psnId "
        + "and (t3.isAccept='1' or t3.isAccept='2' or t3.isAccept is null) " + "and t3.status='01') "
        + "order by t.score desc";
    Page page = form.getPage();
    List<Long> sysRecFriList = super.createQuery(hql).setParameter("psnId", form.getPsnId())
        .setParameter("groupId", form.getGroupId()).setParameter("groupId2", form.getGroupId())
        .setFirstResult(page.getFirst() - 1).setMaxResults(pageSize).list();
    return sysRecFriList;
  }

  /**
   * 得到人员id
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findFriendListTempAutoSysPsnIds(Long psnId) throws DaoException {
    String hql = "select t.tempPsnId from FriendSysRecommend t where t.psnId=:psnId"
        + " and not exists(select 1 from FriendTemp t7 where (t7.tempPsnId=:psnId and t.tempPsnId= t7.psnId) or (t7.psnId=:psnId and t.tempPsnId= t7.tempPsnId))"
        + " and not exists(select 1 from FriendTempSys f where f.psnId =:psnId and f.tempPsnId=t.tempPsnId)"
        + " order by t.score desc,rowid asc";
    return createQuery(hql).setParameter("psnId", psnId).list();
  }

  public void syncPrivacySettingsToPersonKnow(Long psnId, int permission) {
    super.update("update person_know t set t.is_addfrd=? where t.psn_id=?", new Object[] {permission, psnId});
  }

}
