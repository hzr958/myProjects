package com.smate.sie.core.base.utils.dao.statistics;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.statistics.DiffKpiInsGrant;

/**
 * 
 * @author lijianming
 * @descript bpo资助机构、资助机会变化dao
 * 
 */
@Repository
public class DiffKpiInsGrantDao extends SieHibernateDao<DiffKpiInsGrant, Long> {

    // 获取最新的一条数据，即获取时间最近的一条数据
    @SuppressWarnings("unchecked")
    public DiffKpiInsGrant getInfoByRecentTime() {
        String hql = "from DiffKpiInsGrant t order by t.createDate desc";
        Query query = super.createQuery(hql);
        List<DiffKpiInsGrant> list = query.list();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
