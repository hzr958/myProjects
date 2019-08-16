package com.smate.center.job.business.pdwhpub.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.job.business.pdwhpub.model.PdwhPubAddrInsRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果单位和scm单位常量信息匹配记录dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhPubAddrInsRecordDao extends PdwhHibernateDao<PdwhPubAddrInsRecord, Long> {
	@SuppressWarnings("unchecked")
	public List<PdwhPubAddrInsRecord> findRecByPubIdAndInsId(Long pubId, Long insId) {
		String hql = "from PdwhPubAddrInsRecord where pubId=:pubId and insId=:insId";
		return super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId).list();

	}

	@SuppressWarnings("unchecked")
	public List<PdwhPubAddrInsRecord> findRecByPubIdAndAddr(Long pubId, Long addrHash) {
		String hql = "from PdwhPubAddrInsRecord where pubId=:pubId and insNameHash=:addrHash";
		return super.createQuery(hql).setParameter("pubId", pubId).setParameter("addrHash", addrHash).list();

	}

	/**
	 * 更新记录
	 * 
	 * @param id
	 * @author LIJUN
	 * @date 2018年3月29日
	 */
	public void updateTime(Long id) {
		String hql = "update PdwhPubAddrInsRecord set updateTime=:updateTime where id=:id ";
		super.createQuery(hql).setParameter("updateTime", new java.util.Date()).setParameter("id", id).executeUpdate();
	}

	/**
	 * 删除不在地址列表中的地址
	 * 
	 * @param pubId
	 * @param list
	 * @author LIJUN
	 * @date 2018年3月29日
	 */
	public void deleteRecordByPubIdAndAddrHashList(Long pubId, List<Long> list) {
		String hql = "delete PdwhPubAddrInsRecord where pubId=:pubId and insNameHash not in (:addrHashList)";
		super.createQuery(hql).setParameter("pubId", pubId).setParameterList("addrHashList", list).executeUpdate();

	}

	/**
	 * 删除没有被用户确认的地址
	 * 
	 * @param pubId
	 * @author LIJUN
	 * @date 2018年3月30日
	 */
	public void deleteUnconfirmedRecord(Long pubId) {
		String hql = "delete PdwhPubAddrInsRecord where pubId=:pubId and status=0";
		super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
	}

	/**
	 * 根据pubId查询对应的匹配记录
	 * 
	 * @param pubId
	 * @return
	 * @author LIJUN
	 * @date 2018年3月20日
	 */
	@SuppressWarnings("unchecked")
	public List<PdwhPubAddrInsRecord> getPubAddrInsRecordByPubId(Long pubId) {
		String hql = "from PdwhPubAddrInsRecord where pubId=:pubId";
		return super.createQuery(hql).setParameter("pubId", pubId).list();
	}

	/**
	 * 根据constId查询对应的匹配记录
	 * 
	 * @param constId
	 * @return
	 * @author LIJUN
	 * @date 2018年3月20日
	 */

	@SuppressWarnings("unchecked")
	public List<PdwhPubAddrInsRecord> getPubAddrInsRecordByConst(Long constId) {
		String hql = "from PdwhPubAddrInsRecord where constId=:constId";
		return super.createQuery(hql).setParameter("constId", constId).list();
	}

	@SuppressWarnings("unchecked")
	public List<Long> gegetNeedAssignPubIds(Long pubId, Integer size) {
		String hql = "select distinct(pubId) from PdwhPubAddrInsRecord where pubId >:pubId order by pubId ";
		return super.createQuery(hql).setParameter("pubId", pubId).setMaxResults(size).list();
	}

	/**
	 * 新开启事务保存
	 * 
	 * @param pdwhPubAddrInsRecord
	 * @author LIJUN
	 * @date 2018年7月6日
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveWithNewTransaction(PdwhPubAddrInsRecord pdwhPubAddrInsRecord) {
		super.save(pdwhPubAddrInsRecord);

	}

}
