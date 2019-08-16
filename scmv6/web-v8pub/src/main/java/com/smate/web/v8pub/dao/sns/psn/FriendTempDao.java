package com.smate.web.v8pub.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.psn.FriendTemp;

/**
 * 好友分组数据层.
 * 
 * cwli
 */
@Repository
public class FriendTempDao extends SnsHibernateDao<FriendTemp, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> findReqTempPsnId(Long psnId) {
    String hql = "select distinct tempPsnId from FriendTemp where psnId=:psnId and tempPsnId is not null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
