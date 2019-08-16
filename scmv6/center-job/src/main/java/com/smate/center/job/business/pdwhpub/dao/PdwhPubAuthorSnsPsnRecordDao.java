package com.smate.center.job.business.pdwhpub.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.job.business.pdwhpub.model.PdwhPubAuthorSnsPsnRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果作者和sns人员对应关系 dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhPubAuthorSnsPsnRecordDao extends PdwhHibernateDao<PdwhPubAuthorSnsPsnRecord, Long> {
	/**
	 * 获取被确认的记录
	 * 
	 * @param pubId
	 * @param matchName
	 * @return
	 * @author LIJUN
	 * @date 2018年3月30日
	 */
	@SuppressWarnings("unchecked")
	public List<PdwhPubAuthorSnsPsnRecord> findConfirmRecByPubIdAndName(Long pubId, String matchName) {
		String hql = "from PdwhPubAuthorSnsPsnRecord where pubId=:pubId and psnName=:matchName and status in(3,4)";
		return super.createQuery(hql).setParameter("pubId", pubId).setParameter("matchName", matchName).list();
	}

	/**
	 * 更新人名记录表Ins_id
	 * 
	 * @param pubId
	 * @param insId
	 * @author LIJUN
	 * @date 2018年3月31日
	 */
	public void UpdateInsIdRecByPubId(Long pubId, Long insId) {
		String hql = "update  PdwhPubAuthorSnsPsnRecord set insId=:insId ,updateTime=sysdate where pubId=:pubId ";
		super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId).executeUpdate();
	}

	/**
	 * 更新记录
	 * 
	 * @param id
	 * @author LIJUN
	 * @date 2018年3月29日
	 */
	public void updateTime(Long id) {
		String hql = "update PdwhPubAuthorSnsPsnRecord set updateTime=:updateTime where id=:id ";
		super.createQuery(hql).setParameter("updateTime", new java.util.Date()).setParameter("id", id).executeUpdate();
	}

	/**
	 * 删除没有被用户确认的记录
	 * 
	 * @param pubId
	 * @author LIJUN
	 * @date 2018年3月30日
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteUnconfirmedRecord(Long pubId) {
		String hql = "delete PdwhPubAuthorSnsPsnRecord where pubId=:pubId and status not in(3,4)";
		super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
	}

	/**
	 * 获取被确认的记录
	 * 
	 * @param psnId
	 * @return
	 * @author LIJUN
	 * @date 2018年4月4日
	 */
	@SuppressWarnings("unchecked")
	public List<PdwhPubAuthorSnsPsnRecord> getConfirmedRecByPsnId(Long psnId) {
		String hql = "from PdwhPubAuthorSnsPsnRecord where psnId=:psnId and status in(3,4)";
		return super.createQuery(hql).setParameter("psnId", psnId).list();

	}

	/**
	 * 获取未被确认的记录
	 * 
	 * @param psnId
	 * @return
	 * @author LIJUN
	 * @date 2018年4月4日
	 */
	@SuppressWarnings("unchecked")
	public List<PdwhPubAuthorSnsPsnRecord> getUnconfirmedRecByPsnId(Long psnId) {
		String hql = "from PdwhPubAuthorSnsPsnRecord where psnId=:psnId and status not in(3,4)";
		return super.createQuery(hql).setParameter("psnId", psnId).list();

	}

	/**
	 * 获取该人员所有的匹配记录
	 * 
	 * @param psnId
	 * @return
	 * @author LIJUN
	 * @date 2018年4月4日
	 */
	@SuppressWarnings("unchecked")
	public List<PdwhPubAuthorSnsPsnRecord> getAllRecByPsnId(Long psnId) {
		String hql = "from PdwhPubAuthorSnsPsnRecord where psnId=:psnId ";
		return super.createQuery(hql).setParameter("psnId", psnId).list();

	}

	@SuppressWarnings("unchecked")
	public List<Long> getPsnCooperatorIds(Long psnId, Long pdwhPubId) {
		String hql = "select t.psnId from PdwhPubAuthorSnsPsnRecord t where t.pubId = :pubId and t.psnId not in (:psnId)";
		return super.createQuery(hql).setParameter("pubId", pdwhPubId).setParameter("psnId", psnId).list();
	}

	public Integer getInsMatchStatus(Long psnId, Long insId, Long pdwhPubId) {
		String hql = "select t.status from PdwhPubAuthorSnsPsnRecord t where t.pubId = :pubId and t.psnId = :psnId and t.insId=:insId ";
		return (Integer) super.createQuery(hql).setParameter("pubId", pdwhPubId).setParameter("psnId", psnId)
				.setParameter("insId", insId).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<PdwhPubAuthorSnsPsnRecord> getPsnRecordByPubId(Long pdwhPubId) {
		String hql = "select new PdwhPubAuthorSnsPsnRecord(t.pubId, t.psnId, t.nameType,t.psnName) from PdwhPubAuthorSnsPsnRecord t where t.pubId= :pubId order by t.psnId";
		return super.createQuery(hql).setParameter("pubId", pdwhPubId).list();
	}

	@SuppressWarnings("unchecked")
	public List<PdwhPubAuthorSnsPsnRecord> getPsnRecord(Long pdwhPubId, Long psnId) {
		String hql = "select new PdwhPubAuthorSnsPsnRecord(t.pubId, t.psnId, t.nameType,t.psnName) from PdwhPubAuthorSnsPsnRecord t where t.pubId= :pubId and t.psnId = :psnId";
		return super.createQuery(hql).setParameter("pubId", pdwhPubId).setParameter("psnId", psnId).list();
	}

	/**
	 * 查询记录
	 * 
	 * @param pubId
	 * @param psnId
	 * @param insId
	 * @param psnName
	 * @param nameType
	 * @return
	 * @author LIJUN
	 * @date 2018年5月23日
	 */
	public PdwhPubAuthorSnsPsnRecord getPsnRecord(Long pubId, Long psnId, Long insId, String psnName,
			Integer nameType) {
		String hql = "select new PdwhPubAuthorSnsPsnRecord(t.pubId, t.psnId, t.nameType,t.psnName) from PdwhPubAuthorSnsPsnRecord t where t.pubId=:pubId and t.psnId =:psnId and t.insId=:insId and t.psnName=:psnName and t.nameType=:nameType";
		return (PdwhPubAuthorSnsPsnRecord) super.createQuery(hql).setParameter("pubId", pubId)
				.setParameter("insId", insId).setParameter("psnId", psnId).setParameter("nameType", nameType)
				.setParameter("psnName", psnName).uniqueResult();
	}

	public void saveNewPsnRecord(Long pubId, Long psnId, String matchName, Long insId, String insName, int status,
			Date date, Integer type) {
		super.save(new PdwhPubAuthorSnsPsnRecord(pubId, psnId, matchName, insId, insName, status, date, type));

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteUnconfirmedRecordByPsn(Long psnId) {
		String hql = "delete PdwhPubAuthorSnsPsnRecord where psnId=:psnId and status not in(3,4)";
		super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
	}

	/**
	 * 新开事务保存
	 * 
	 * @param pdwhPubAuthorSnsPsnRecord
	 * @author LIJUN
	 * @date 2018年7月6日
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveWithNewTransaction(PdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord) {
		super.save(pdwhPubAuthorSnsPsnRecord);

	}

}
