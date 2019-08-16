package com.smate.sie.core.base.utils.dao.validate.tmp;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateDetail;

/**
 * TASK_KPI_VALIDATE_DETAIL Dao
 * 
 * @author ztg
 *
 */
@Repository
public class SieTaskKpiValidateDetailDao extends SieHibernateDao<SieTaskKpiValidateDetail, Long> {

  public Long getAllCountByUUID(String uuId) {
    String hql = "select count(id) from SieTaskKpiValidateDetail where uuId = ?";
    return findUnique(hql, uuId);
  }

  public List<SieTaskKpiValidateDetail> getByUUID(String uuId) {
    String hql = "from SieTaskKpiValidateDetail where splitStatus = 0 and uuId = ?";
    Query queryResult = super.createQuery(hql);
    queryResult.setString(0, uuId);
    return queryResult.list();
  }

  public Long getSuccessCountByUUID(String uuId) {
    String hql = "select count(id) from SieTaskKpiValidateDetail where splitStatus = 1 and  uuId = ?";
    return findUnique(hql, uuId);
  }

  public Long getErrorCountByUUID(String uuId) {
    String hql = "select count(id) from SieTaskKpiValidateDetail where splitStatus = 9 and uuId = ?";
    return findUnique(hql, uuId);
  }


}
