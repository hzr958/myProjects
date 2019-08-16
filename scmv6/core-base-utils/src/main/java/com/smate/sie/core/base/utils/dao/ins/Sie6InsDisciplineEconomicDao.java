package com.smate.sie.core.base.utils.dao.ins;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.ins.Sie6InsDisciplineEconomic;

/**
 * 机构国民经济行业分类DAO
 * 
 * @author xr 2019.4.25
 */
@Repository
public class Sie6InsDisciplineEconomicDao extends SieHibernateDao<Sie6InsDisciplineEconomic, Long> {

  @SuppressWarnings("unchecked")
  public List<Sie6InsDisciplineEconomic> getInsTradeList(Long insId) {
    String hql = "from Sie6InsDisciplineEconomic t where t.insId =:insId ";
    Query query = super.createQuery(hql).setParameter("insId", insId);
    return query.list();
  }

  public Sie6InsDisciplineEconomic getEconomicByInsId(Long insId) {
    String hql = "from Sie6InsDisciplineEconomic t where t.insId =:insId ";
    Query query = super.createQuery(hql).setParameter("insId", insId);
    if (CollectionUtils.isEmpty(query.list())) {
      return null;
    }
    return (Sie6InsDisciplineEconomic) query.list().get(0);
  }

}
