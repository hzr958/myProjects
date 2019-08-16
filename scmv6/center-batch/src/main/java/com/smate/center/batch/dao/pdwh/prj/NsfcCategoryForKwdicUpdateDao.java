package com.smate.center.batch.dao.pdwh.prj;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.prj.NsfcCategoryForKwdicUpdate;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcCategoryForKwdicUpdateDao extends PdwhHibernateDao<NsfcCategoryForKwdicUpdate, Long> {

    public List<NsfcCategoryForKwdicUpdate> getToHandleTask(Integer size) {
        String hql = "from NsfcCategoryForKwdicUpdate t where t.status =5";
        return super.createQuery(hql).setMaxResults(size).list();
    }

}
