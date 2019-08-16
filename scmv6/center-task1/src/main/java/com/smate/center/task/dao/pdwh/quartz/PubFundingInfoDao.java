package com.smate.center.task.dao.pdwh.quartz;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubFundingInfoDao extends PdwhHibernateDao<PubFundingInfo, Long> {
  public boolean getByPubAllId(Long pubAllId) {
    String hql = "select count(*) from PubFundingInfo where pubAllId =:pubAllId";
    Long count = (Long) super.createQuery(hql).setParameter("pubAllId", pubAllId).uniqueResult();
    if (count != null && count != 0) {
      return true;
    } else {
      return false;
    }
  }

  public void deleteByPubAllId(Long pubAllId) {
    String hql = "delete PubFundingInfo where pubAllId =:pubAllId";
    super.createQuery(hql).setParameter("pubAllId", pubAllId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubFundingInfo> getPubFundingInfoByFundingNo(String fundingNo) {
    String hql = "from PubFundingInfo t where t.fundingInfo like :fundingNo";
    return super.createQuery(hql).setParameter("fundingNo", "%" + fundingNo + "%").list();
  }
}
