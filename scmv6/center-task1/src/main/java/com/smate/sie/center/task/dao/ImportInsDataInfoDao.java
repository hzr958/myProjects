package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportInsDataInfo;

/**
 * 单位临时表dao
 * 
 * @author hd
 *
 */
@Repository
public class ImportInsDataInfoDao extends SieHibernateDao<ImportInsDataInfo, Long> {
  /**
   * 查询需要同步信息的单位
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ImportInsDataInfo> findSyncInsList(int maxSize, Long sysFlag) throws DaoException {
    String hql = "from ImportInsDataInfo where synFlag =:synFlag";
    Query queryResult = super.createQuery(hql).setParameter("synFlag", sysFlag);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

}
