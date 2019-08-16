package com.smate.web.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubAuthorSnsPsnRecord;

/**
 * 基准库成果作者和sns人员对应关系 dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhPubAuthorSnsPsnRecordDao extends PdwhHibernateDao<PdwhPubAuthorSnsPsnRecord, Long> {

  public void deleteUnconfirmedRecordByPsn(Long psnId) {
    String hql = "delete PdwhPubAuthorSnsPsnRecord where psnId=:psnId and status not in(3,4)";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public void delRecordByPubId(Long pubId) {
    String hql = "delete from PdwhPubAuthorSnsPsnRecord t where t. pubId = :pubId ";
    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }
}
