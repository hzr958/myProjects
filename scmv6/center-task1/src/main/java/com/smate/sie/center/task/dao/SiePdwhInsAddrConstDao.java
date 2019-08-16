package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.pub.PdwhInsAddrConst;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePdwhInsAddrConst;

/**
 * 基准库标准单位地址信息常量dao
 * 
 * @author YEXINGYUAN
 * @date 2018年7月17日
 */
@Repository
public class SiePdwhInsAddrConstDao extends SieHibernateDao<SiePdwhInsAddrConst, Long> {

  /**
   * 根据namehash获取常量信息
   * 
   * @param addrHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PdwhInsAddrConst> getInsInfoByNameHash(Long addrHash) {
    String hql = " from SiePdwhInsAddrConst where insNameHash=:addrHash and enable=1 order by insId asc ";
    return super.createQuery(hql).setParameter("addrHash", addrHash).list();

  }

  @SuppressWarnings("unchecked")
  public SiePdwhInsAddrConst getInsInfoByInsId(Long insIdval) {
    SiePdwhInsAddrConst siePdwhInsAddrConst = null;
    List<SiePdwhInsAddrConst> sielist = null;
    String hql = " from SiePdwhInsAddrConst where insId=:insIdVal ";
    sielist = super.createQuery(hql).setParameter("insIdVal", insIdval).list();
    if (sielist.size() > 0) {
      siePdwhInsAddrConst = sielist.get(0);
    }
    return siePdwhInsAddrConst;

  }

  @SuppressWarnings("unchecked")
  public List<String> batchGetAddr(int batchSize, int index) {
    String hql = " select distinct(insName) from SiePdwhInsAddrConst where  enable=1";
    return super.createQuery(hql).setMaxResults(batchSize).setFirstResult(batchSize * (index - 1)).list();

  }

  @SuppressWarnings("unchecked")
  public List<Long> getInsIdByNameHash(Long cleanPubAddrHash) {
    String hql = " select distinct(insId) from SiePdwhInsAddrConst where insNameHash=:addrHash ";
    return super.createQuery(hql).setParameter("addrHash", cleanPubAddrHash).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePdwhInsAddrConst> queryNeedRefresh(int maxSize) throws DaoException {
    return super.createQuery("from SiePdwhInsAddrConst t where t.flushStatus=0").setMaxResults(maxSize).list();
  }

  public void create(SiePdwhInsAddrConst entity) {
    Assert.notNull(entity, "entity不能为空");
    super.getSession().save(entity);
    logger.debug("save or update entity: {}", entity);
  }

  @SuppressWarnings("unchecked")
  public List<SiePdwhInsAddrConst> getListByInsId(Long insId) {
    String hql = " from SiePdwhInsAddrConst t where t.insId= ? order by t.constId ";
    return super.createQuery(hql, insId).list();
  }
}
