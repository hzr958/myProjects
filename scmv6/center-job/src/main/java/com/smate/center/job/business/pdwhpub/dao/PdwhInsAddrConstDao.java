package com.smate.center.job.business.pdwhpub.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.job.business.pdwhpub.model.PdwhInsAddrConst;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库标准单位地址信息常量dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhInsAddrConstDao extends PdwhHibernateDao<PdwhInsAddrConst, Long> {

	/**
	 * 根据namehash获取常量信息
	 * 
	 * @param addrHash
	 * @return
	 * @author LIJUN
	 * @date 2018年3月19日
	 */
	@SuppressWarnings("unchecked")
	public List<PdwhInsAddrConst> getInsInfoByNameHash(Long addrHash) {
		String hql = " from PdwhInsAddrConst where insNameHash=:addrHash and enable=1 order by insId asc ";
		return super.createQuery(hql).setParameter("addrHash", addrHash).list();

	}

	@SuppressWarnings("unchecked")
	public List<String> batchGetAddr(int batchSize, int index) {
		String hql = " select distinct(insName) from PdwhInsAddrConst where  enable=1";
		return super.createQuery(hql).setMaxResults(batchSize).setFirstResult(batchSize * (index - 1)).list();

	}

	@SuppressWarnings("unchecked")
	public List<Long> getInsIdByNameHash(Long cleanPubAddrHash) {
		String hql = " select distinct(insId) from PdwhInsAddrConst where insNameHash=:addrHash ";
		return super.createQuery(hql).setParameter("addrHash", cleanPubAddrHash).list();
	}

}
