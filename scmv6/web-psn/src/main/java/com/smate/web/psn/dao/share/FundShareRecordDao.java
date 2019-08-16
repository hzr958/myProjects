package com.smate.web.psn.dao.share;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.share.FundShareRecord;

/**
 * 个人基金分享记录dao
 * 
 * @author WSN
 *
 */
@Repository
public class FundShareRecordDao extends SnsHibernateDao<FundShareRecord, Long> {

  @SuppressWarnings("unchecked")
  public List<FundShareRecord> findListByShareBaseId(Long shareBaseId) {
    String hql = "from FundShareRecord t where t.shareBaseId=:shareBaseId order by t.createDate desc ,t.id desc ";
    return this.createQuery(hql).setParameter("shareBaseId", shareBaseId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findFundIdByShareBaseId(Long shareBaseId) {
    String hql = "select distinct t.fundId from FundShareRecord t where t.shareBaseId=:shareBaseId ";
    return this.createQuery(hql).setParameter("shareBaseId", shareBaseId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPsnIdByShareBaseId(Long shareBaseId) {
    String hql = "select distinct t.reveiverId from FundShareRecord t where t.shareBaseId=:shareBaseId ";
    return this.createQuery(hql).setParameter("shareBaseId", shareBaseId).list();
  }
}
