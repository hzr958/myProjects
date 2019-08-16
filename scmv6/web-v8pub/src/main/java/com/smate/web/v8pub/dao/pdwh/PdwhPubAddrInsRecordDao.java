package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubAddrInsRecord;

/**
 * 基准库成果单位和scm单位常量信息匹配记录dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhPubAddrInsRecordDao extends PdwhHibernateDao<PdwhPubAddrInsRecord, Long> {

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
        + "and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pubId)";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  public void delRecordByPubId(Long pubId) {
    String hql = "delete from PdwhPubAddrInsRecord t where t. pubId = :pubId ";
    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubRegionIdByPubId(Long pubId) {
    String hql = "select regionId from PdwhPubAddrInsRecord where pubId=:pubId and regionId is not null";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
