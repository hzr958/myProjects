package com.smate.center.batch.dao.sns.friend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 群组人员邀请Dao.
 * 
 * oyh
 */
@Repository
public class GroupInviteDao extends SnsHibernateDao<GroupInvite, Long> {

  @SuppressWarnings("unchecked")
  public GroupInvite findGroupInviteByUserId(Long groupId, Long psnId, Long sendPsnId) throws DaoException {

    String hql = "from GroupInvite where groupId = ? and psnId = ? and sendPsnId=?";
    List<GroupInvite> list = super.createQuery(hql, groupId, psnId, sendPsnId).list();
    if (list != null && list.size() > 0)
      return list.get(0);
    return null;
  }

  @SuppressWarnings("unchecked")
  public GroupInvite findGroupInviteByEmail(Long groupId, String email, Long sendPsnId) throws DaoException {

    String hql = "from GroupInvite where groupId = ? and email = ? and sendPsnId=?";
    List<GroupInvite> list = super.createQuery(hql, groupId, email, sendPsnId).list();

    if (list != null && list.size() > 0)
      return list.get(0);
    return null;
  }

  /**
   * 根据邀请码查找群组邀请记录.
   * 
   * @param inviteCode
   * @return
   */
  @SuppressWarnings("unchecked")
  public GroupInvite findInviteRecordByInviteCode(String inviteCode) {
    String hql = "from GroupInvite where inviteCode = ?";
    List<GroupInvite> list = super.createQuery(hql, inviteCode).list();
    if (list != null && list.size() > 0)
      return list.get(0);
    return null;
  }


  public Object getEmailByPsnId(Long psnId) {
    String hql = "select t.email From Person t where t.personId=?";


    return super.createQuery(hql, new Object[] {psnId}).uniqueResult();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupInvite> getGroupInviteByPsnId(Long delPsnId, Long groupId) throws DaoException {
    String hql = "from GroupInvite where (sendPsnId=? or psnId=?) and groupId=?";
    return super.createQuery(hql, delPsnId, delPsnId, groupId).list();
  }
  // ==============人员合并 end============
}
