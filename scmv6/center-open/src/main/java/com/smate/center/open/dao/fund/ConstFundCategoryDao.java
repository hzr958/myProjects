package com.smate.center.open.dao.fund;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.fund.ConstFundCategory;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/*
 * @author zjh 基金constFundCategoryDao
 */
@Repository
public class ConstFundCategoryDao extends RcmdHibernateDao<ConstFundCategory, Long> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  public ConstFundCategory getFundInfo(Long fundId) {
    String hql = " from ConstFundCategory t where t.id = :fundId and t.insId=0";
    return (ConstFundCategory) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();
  }

}
