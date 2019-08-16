package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.DynamicPsnRefresh;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class DynamicPsnRefreshDao extends SnsHibernateDao<DynamicPsnRefresh, Long> {
  @SuppressWarnings("unchecked")
  public List<DynamicPsnRefresh> getDynPsnRefreshList(int maxSize) throws DaoException {
    String hql = "from DynamicPsnRefresh t";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }
}
