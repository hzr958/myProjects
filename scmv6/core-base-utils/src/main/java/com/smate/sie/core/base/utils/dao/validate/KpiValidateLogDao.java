package com.smate.sie.core.base.utils.dao.validate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.KpiValidateLog;

/**
 * 
 * @author ztg
 *
 */
@Repository
public class KpiValidateLogDao extends SieHibernateDao<KpiValidateLog, Long> {

  /**
   * 根据detailId获取记录
   * 
   * @param detailId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KpiValidateLog> getByDetail(Long detailId) {
    String hql = "from KpiValidateLog where detailId = :detailId";
    Query queryResult = super.createQuery(hql).setParameter("detailId", detailId);
    return queryResult.list();
  }

}
