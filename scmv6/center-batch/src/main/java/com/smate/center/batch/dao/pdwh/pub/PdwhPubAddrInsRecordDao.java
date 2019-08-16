package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果单位和scm单位常量信息匹配记录dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhPubAddrInsRecordDao extends PdwhHibernateDao<PdwhPubAddrInsRecord, Long> {
  /**
   * 
   * 根据pubId删除对应关系
   * 
   * @param pubId
   * @author LIJUN
   * @date 2018年3月20日
   */
  public void deleteRecordByPubId(Long pubId) {
    String hql = "delete PdwhPubAddrInsRecord where pubId=:pubId";
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
    String hql = "from PdwhPubAddrInsRecord t where t.pubId=:pubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubAddrInsRecord> findRecByPubIdAndAddr(Long pubId, Long addrHash) {
    String hql = "from PdwhPubAddrInsRecord t where t.pubId=:pubId and t.insNameHash=:addrHash "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
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

  @SuppressWarnings("unchecked")
  public List<PdwhPubAddrInsRecord> findRecByPubIdAndInsId(Long pubId, Long insId) {
    String hql = "from PdwhPubAddrInsRecord t where t.pubId=:pubId and t.insId=:insId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId).list();

  }

  /**
   * 处理联合唯一索引保存报错新开事务单独保存。
   *
   * @author LIJUN
   * @date 2018年7月5日
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithNewTransaction(PdwhPubAddrInsRecord insRecord) {
    super.save(insRecord);
  }
}
