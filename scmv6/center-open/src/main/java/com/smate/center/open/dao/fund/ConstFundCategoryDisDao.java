package com.smate.center.open.dao.fund;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.fund.ConstFundCategoryDis;
import com.smate.core.base.utils.data.RcmdHibernateDao;

@Repository
public class ConstFundCategoryDisDao extends RcmdHibernateDao<ConstFundCategoryDis, Long> {

  /**
   * 获取基金的学科代码ID.
   * 
   * @param fundId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getFundDiscIdList(Long fundId) {
    String hql = "select disId  from ConstFundCategoryDis where categoryId =? ";
    return super.createQuery(hql, fundId).list();
  }

}
