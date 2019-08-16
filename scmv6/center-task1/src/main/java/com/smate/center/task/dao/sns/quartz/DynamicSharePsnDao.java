package com.smate.center.task.dao.sns.quartz;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.center.task.model.sns.quartz.DynamicSharePsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员分享Dao.
 * 
 * @author zk
 * 
 */
@Repository
public class DynamicSharePsnDao extends SnsHibernateDao<DynamicSharePsn, Long> {

  public List<DynamicSharePsn> listDynamicSharePsn(Long shareId) {
    String hql = "FROM DynamicSharePsn t where t.shareId =:shareId";
    return this.createQuery(hql).setParameter("shareId", shareId).list();
  }

}
