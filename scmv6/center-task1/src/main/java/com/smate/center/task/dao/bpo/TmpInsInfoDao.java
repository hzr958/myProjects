package com.smate.center.task.dao.bpo;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.bpo.TmpInsInfo;
import com.smate.core.base.utils.data.BpoHibernateDao;

/**
 * 单位临时表dao
 * 
 * @author hd
 *
 */
@Repository
public class TmpInsInfoDao extends BpoHibernateDao<TmpInsInfo, Long> {
  /**
   * 查询需要同步信息的单位
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<TmpInsInfo> findSyncInsList(int maxSize, Long sysFlag) throws DaoException {
    String hql = "from TmpInsInfo where synFlag =:synFlag";
    Query queryResult = super.createQuery(hql).setParameter("synFlag", sysFlag);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

}
