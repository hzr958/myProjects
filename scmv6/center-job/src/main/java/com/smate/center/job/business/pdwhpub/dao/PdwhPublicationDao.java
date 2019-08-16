package com.smate.center.job.business.pdwhpub.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.job.business.pdwhpub.model.PdwhPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPublicationDao extends PdwhHibernateDao<PdwhPublication, Long> {
	@SuppressWarnings("unchecked")
	public List<PdwhPublication> findPubByBatchSize(Long lastId, Integer batchSize) {
		String hql = "from PdwhPublication t where t.pubType in (1, 2, 3, 4, 7, 8, 10) and t.pubId>:lastId order by t.pubId asc";
		return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
	}

	@SuppressWarnings("unchecked")
	public List<PdwhPublication> findPatByBatchSize(Long lastId, Integer batchSize) {
		String hql = "from PdwhPublication t where t.pubType = 5 and t.pubId>:lastId order by t.pubId asc";
		return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
	}

	/**
	 * 更新成果更新时间为当前时间
	 * 
	 * @param pubId
	 * @author LIJUN
	 * @date 2018年6月20日
	 */
	public void updatePubUpdateTime(Long pubId) {
		String hql = "update PdwhPublication t set t.updateDate =sysdate where t.pubId = :pubId";
		super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
	}

	public Long getMinPubAllId() {
		String hql = "select min(t.pubId) from PdwhPublication t";
		return super.findUnique(hql);
	}

	@SuppressWarnings("unchecked")
	public List<Long> findPubAllIdBySize(Long lastId, Integer batchSize) {
		String hql = "select t.pubId from PdwhPublication t where t.pubId>:lastId order by t.pubId asc";
		return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
	}

	public Long getPubIdByPatNoandOpenNo(String patentNo, String patentOpenNo) {
		String hql = "select t.pubId from PdwhPublication t where t.patentNo=:patentNo and  t.patentOpenNo=:patentOpenNo";
		return (Long) super.createQuery(hql).setParameter("patentNo", patentNo)
				.setParameter("patentOpenNo", patentOpenNo).setMaxResults(1).uniqueResult();

	}

	public Long getPubIdByTitle(String title) {
		String hql = "select t.pubId from PdwhPublication t where t.zhTitle=:zhTitle or  t.enTitle=:enTitle";
		return (Long) super.createQuery(hql).setParameter("zhTitle", title).setParameter("enTitle", title)
				.setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Long> batchGetPdwhPub(int index, Integer batchSize) {
		String hql = "select t.pubId from PdwhPublication t  order by t.pubId asc";
		return super.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();
	}

	public Long getPubCount() {
		String hql = "select count(1) from PdwhPublication t";
		return (Long) super.createQuery(hql).uniqueResult();
	}

	public Integer getDbIdById(Long currentPubId) {
		String hql = "select dbId from PdwhPublication t where t.pubId =:pubId";
		return (Integer) super.createQuery(hql).setParameter("pubId", currentPubId).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<PdwhPublication> getPdwhPubIds(Long lastPubId, int batchSize) {
		String hql = "select new PdwhPublication(t.pubId,t.dbId) from PdwhPublication t where t.dbId in (4,15,16,17) and t.pubId > :lastPubId order by t.pubId ";
		return super.createQuery(hql).setParameter("lastPubId", lastPubId).setMaxResults(batchSize).list();
	}

	public PdwhPublication getPubTitleById(Long pubId) {
		String hql = "select new PdwhPublication(t.pubId,t.zhTitle,t.enTitle) from PdwhPublication t where t.pubId =:pubId";
		return (PdwhPublication) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
	}

	public PdwhPublication getPubDupInfoById(Long pubId) {
		String hql = "select new PdwhPublication(t.pubId,t.zhTitle,t.enTitle,t.pubType,t.pubYear) from PdwhPublication t where t.pubId =:pubId";
		return (PdwhPublication) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
	}

	public void updateBrief(String zhBrief, String enBrief, Long pubId) {
		String hql = "update PdwhPublication set zhBriefDesc=:zhBrief,enBriefDesc=:enBrief where pubId=:pubId";
		super.createQuery(hql).setParameter("zhBrief", zhBrief).setParameter("enBrief", enBrief)
				.setParameter("pubId", pubId).executeUpdate();
	}

	public PdwhPublication getPubAuthorKwsById(Long pubId) {
		String hql = " from PdwhPublication t where t.pubId =:pubId";
		return (PdwhPublication) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
	}

}
