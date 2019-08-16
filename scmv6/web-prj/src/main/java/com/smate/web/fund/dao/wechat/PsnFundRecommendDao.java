package com.smate.web.fund.dao.wechat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.model.common.PsnFundRecommend;


@Repository
public class PsnFundRecommendDao extends SnsHibernateDao<PsnFundRecommend, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getFundIdList(Long psnId, Long fundId) {
    String hql =
        "select t.fundId from PsnFundRecommend t where t.psnId = :psnId and t.fundId <> :fundId order by t.updateDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("fundId", fundId).setMaxResults(5).list();
  }

}
