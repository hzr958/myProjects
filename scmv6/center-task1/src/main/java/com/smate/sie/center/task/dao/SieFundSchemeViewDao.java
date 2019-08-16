package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.SieConstFundCategoryView;

/**
 * 
 * @author lijianming
 * @descript 基金机会dao
 * 
 */
@Repository
public class SieFundSchemeViewDao extends SieHibernateDao<SieConstFundCategoryView, Long> {

    // 统计视图表所有的基金机会
    public Long countAllScheme() {
        String hql = "select count(t.grantId) from SieConstFundCategoryView t";
        Query query = super.createQuery(hql);
        return (Long) query.uniqueResult();
    }

    // 查找单位的基金机构下的基金机会业务表数据是否有缺失数据
    @SuppressWarnings("unchecked")
    public List<Long> checkFocusedSchemeIsAbsence(Long agencyId, Long insId) {
        String hql = "select t.grantId from SieConstFundCategoryView t where not exists (select 1 from SieInsFocusScheme s where s.grantId = t.grantId and s.insId = :insId) and t.agencyId = :agencyId";
        Query query = super.createQuery(hql).setParameter("insId", insId).setParameter("agencyId", agencyId);
        return query.list();
    }

    // 根据agencyId查找基金机构下的基金机会
    @SuppressWarnings("unchecked")
    public List<Long> getSchemesByAgencyId(Long agencyId) {
        String hql = "select t.grantId from SieConstFundCategoryView t where t.agencyId = :agencyId";
        Query query = super.createQuery(hql).setParameter("agencyId", agencyId);
        return query.list();
    }
}
