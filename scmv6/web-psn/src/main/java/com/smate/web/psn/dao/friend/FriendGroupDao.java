package com.smate.web.psn.dao.friend;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.friend.FriendGroup;

/**
 * 分组好友dao.
 * 
 * @author lhd
 */
@Repository
public class FriendGroupDao extends SnsHibernateDao<FriendGroup, Long> {

  /**
   * 获取好友分组id
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long getGroupByElseId(Long psnId) throws DaoException {
    // String hql =
    // "select groupId from FriendGroup where psnId=:psnId and (nameZh like '%未分类%' or nameEn like
    // '%Undefined%')";
    String hql =
        "select groupId from FriendGroup where psnId=:psnId and (instr(nameZh,'未分类')>0 or instr(nameEn,'Undefined')>0)";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

}
