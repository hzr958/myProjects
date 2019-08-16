package com.smate.center.task.dao.pdwh.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.prj.NsfcProjectPub;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class NsfcProjectPubDao extends PdwhHibernateDao<NsfcProjectPub, Long> {

    @SuppressWarnings("unchecked")
    public List<Long> getRcmdPdwhPubIds(String nsfcCatId) {
        String hql =
                "select t.pdwhPubId from  NsfcProjectPub  t where t.pdwhPubId is not null and t.nsfcCategory = :nsfcCatId";
        return super.createQuery(hql).setParameter("nsfcCatId", nsfcCatId).list();
    }

}
