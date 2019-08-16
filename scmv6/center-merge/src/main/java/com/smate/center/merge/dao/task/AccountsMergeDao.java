package com.smate.center.merge.dao.task;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.task.AccountsMerge;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 帐号合并dao
 * 
 * @author tsz
 *
 */
@Repository
public class AccountsMergeDao extends SnsHibernateDao<AccountsMerge, Long> {

  /**
   * 获取需要合并数据
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<AccountsMerge> getNeedMergeData() throws Exception {

    String hql = " from AccountsMerge t where t.status=0 ";

    return super.createQuery(hql).setMaxResults(10).list();

  }

}
