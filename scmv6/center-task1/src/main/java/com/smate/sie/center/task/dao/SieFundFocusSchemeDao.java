package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieInsFocusScheme;

/**
 * 
 * @author lijianming
 * @descript 单位关注基金机会dao
 * 
 */
@Repository
public class SieFundFocusSchemeDao extends SieHibernateDao<SieInsFocusScheme, Long> {

    public void deleteFocusedSchemeByAgencyId(List<Long> agencyIds, Long insId) {
        String hql = "delete from SieInsFocusScheme where agencyId in (:agencyIds) and insId = :insId";
        Query query = super.createQuery(hql).setParameterList("agencyIds", agencyIds).setParameter("insId", insId);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<Long> getAgencyIdListByFocusedScheme(Long insId) {
        String hql = "select distinct t.agencyId from SieInsFocusScheme t where t.insId = :insId";
        Query query = super.createQuery(hql).setParameter("insId", insId);
        return query.list();
    }

    public void deleteFocusedSchemeByAgencyIdAndInsId(Long agencyId, Long insId) {
        String hql = "delete from SieInsFocusScheme where agencyId = :agencyId and insId = :insId";
        Query query = super.createQuery(hql).setParameter("agencyId", agencyId).setParameter("insId", insId);
        query.executeUpdate();
    }

    /**
     * 检查基金机会业务表因基金机会转移导致的数据冗余的数据
     * 
     * @param insId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SieInsFocusScheme> CheckAgencyIdAndGrandIdIsMate(Long insId) {
        String hql = "from SieInsFocusScheme t where not exists (select 1 from SieConstFundCategoryView s where t.agencyId = s.agencyId and t.grantId = s.grantId) and t.insId = :insId";
        Query query = super.createQuery(hql).setParameter("insId", insId);
        return query.list();
    }
}
