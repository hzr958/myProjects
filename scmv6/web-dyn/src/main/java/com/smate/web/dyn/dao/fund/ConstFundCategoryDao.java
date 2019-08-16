package com.smate.web.dyn.dao.fund;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.dyn.model.fund.ConstFundCategory;

/*
 * @author zjh 基金constFundCategoryDao
 */
@Repository
public class ConstFundCategoryDao extends RcmdHibernateDao<ConstFundCategory, Long> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据IDs获取基金信息
   * 
   * @param fundIds
   * @return
   */
  public List<ConstFundCategory> findConstFundCategoryByIds(List<Long> fundIds) {
    String hql = " from ConstFundCategory t where t.id in (:fundIds)";
    return super.createQuery(hql).setParameterList("fundIds", fundIds).list();
  }

}
