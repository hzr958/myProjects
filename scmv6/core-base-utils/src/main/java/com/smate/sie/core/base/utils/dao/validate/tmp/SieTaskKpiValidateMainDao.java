package com.smate.sie.core.base.utils.dao.validate.tmp;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.tmp.SieTaskKpiValidateMain;

/**
 * TASK_KPI_VALIDATE_MAIN 表 Dao层
 * 
 * @author ztg
 *
 */
@Repository
public class SieTaskKpiValidateMainDao extends SieHibernateDao<SieTaskKpiValidateMain, String> {

  public Long countNeedHandleKeyId() {
    String hql = "select count(uuId) from SieTaskKpiValidateMain where splitStatus=0";// uuId =
    // '4028bc8267c548970167c59644410002'
    // and
    return findUnique(hql);
  }

  public List<SieTaskKpiValidateMain> loadNeedHandleKeyId(int maxSize) {
    // String hql = "from SieTaskKpiValidateMain k where k.status = 0 order by k.smDate desc";
    String hql = "from SieTaskKpiValidateMain k where k.splitStatus = 0 ";// k.uuId = '4028bc8367cb18090167cc4f5d1901a2'
    // and
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

}
