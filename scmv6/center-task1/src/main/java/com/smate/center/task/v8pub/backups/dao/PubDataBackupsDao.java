package com.smate.center.task.v8pub.backups.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.center.task.v8pub.backups.model.PubDataBackups;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubDataBackupsDao extends SnsHibernateDao<PubDataBackups, Long> {

  /**
   * 获取指定数量的数据
   * 
   * @param sIZE
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubDataBackups> findPubBySize(Integer sIZE, Integer dataType) {
    String hql = "from PubDataBackups t where t.dataType=:dataType and t.status = 0";
    return this.createQuery(hql).setParameter("dataType", dataType).setMaxResults(sIZE).list();
  }

}
