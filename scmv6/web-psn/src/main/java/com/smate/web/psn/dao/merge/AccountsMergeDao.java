package com.smate.web.psn.dao.merge;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.merge.AccountsMerge;

/**
 * 帐号合并dao
 * 
 * @author tsz
 *
 */
@Repository
public class AccountsMergeDao extends SnsHibernateDao<AccountsMerge, Long> {

  public List<AccountsMerge> getAccountsMerge(Long savePsnId, Long delPsnId) {
    String hql = " from  AccountsMerge  a  where a.savePsnId=:savePsnId and  a.delPsnId =:delPsnId ";
    return this.createQuery(hql).setParameter("savePsnId", savePsnId).setParameter("delPsnId", delPsnId).list();
  }

  public Long findPsnIdByMergePsnId(Long psnId) {
    String hql = "select a.savePsnId from AccountsMerge a where a.delPsnId=? ";
    return this.findUnique(hql, psnId);
  }


}
