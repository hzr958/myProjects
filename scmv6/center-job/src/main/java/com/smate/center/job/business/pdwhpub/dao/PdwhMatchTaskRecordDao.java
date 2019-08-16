package com.smate.center.job.business.pdwhpub.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.job.business.pdwhpub.model.PdwhMatchTaskRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhMatchTaskRecordDao extends PdwhHibernateDao<PdwhMatchTaskRecord, Long> {

	@SuppressWarnings("unchecked")
	public List<Long> batchGetNeededData(Integer batchSize) {
		String hql = "select pdwhPubId from PdwhMatchTaskRecord where matchStatus=0 order by pdwhPubId desc";
		return super.createQuery(hql).setMaxResults(batchSize).list();

	}

}
