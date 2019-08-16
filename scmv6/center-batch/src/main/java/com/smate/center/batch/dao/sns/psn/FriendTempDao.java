package com.smate.center.batch.dao.sns.psn;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.FriendTemp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 好友分组数据层.
 * 
 * cwli
 */
@Repository
public class FriendTempDao extends SnsHibernateDao<FriendTemp, Long> {

  public List<FriendTemp> getFriendTemp(Long sendPsnId, Long recPsnId) {
    String hql = "from FriendTemp where psnId=? and tempPsnId=?";
    return find(hql, sendPsnId, recPsnId);
  }

  public FriendTemp getFriendTemp(FriendTemp friendTemp) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    criterionList.add(Restrictions.eq("psnId", friendTemp.getPsnId()));
    if (friendTemp.getTempPsnId() != null) {
      criterionList.add(Restrictions.eq("tempPsnId", friendTemp.getTempPsnId()));
    } else {
      criterionList.add(Restrictions.isNull("tempPsnId"));
    }
    // if (StringUtils.isNotBlank(friendTemp.getSendMail()))
    // criterionList.add(Restrictions.eq("sendMail",
    // friendTemp.getSendMail()));
    return findUnique(criterionList.toArray(new Criterion[criterionList.size()]));
  }

  public int delFriendTemp(Long sendPsnId, Long recPsnId) {
    String hql = "delete FriendTemp where psnId=? and tempPsnId=?";
    return createQuery(hql, sendPsnId, recPsnId).executeUpdate();
  }

  public Long getFriendTempId(Long psnId, String mail) {
    String hql = "select id from FriendTemp where psnId=? and sendMail=? order by id desc";
    Query q = createQuery(hql, psnId, mail);
    q.setFirstResult(0);
    q.setMaxResults(1);
    return q.list() != null ? Long.valueOf(String.valueOf(q.list().get(0))) : null;
  }

  public Long findFriendIsReq(Long psnId, Long friendPsnId) throws DaoException {
    String hql = "select count(*) from FriendTemp where psnId=? and tempPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

  public List<Long> findReqTempPsnId(Long psnId) throws DaoException {
    String hql = "select tempPsnId from FriendTemp where psnId=?";
    return find(hql, psnId);
  }

  public void updateByGroups(Long psnId, Long groupId) {
    String gId = String.valueOf(groupId);
    String sql = "update FriendTemp set groups=replace(groups,?,'') where psnId=? and groups like ?";
    createQuery(sql, gId, psnId, "%" + gId + "%").executeUpdate();
  }

  /**
   * 删除指定人员信息(人员合并用) zk add.
   */
  public void delFriendTempByPsnId(Long psnId) throws DaoException {
    super.createQuery("delete from FriendTemp where psnId=? or tempPsnId=?", psnId, psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<FriendTemp> getFriendTemp(Long psnId) throws DaoException {
    return super.createQuery(" from FriendTemp where psnId=? or tempPsnId=?", psnId, psnId).list();
  }

}
