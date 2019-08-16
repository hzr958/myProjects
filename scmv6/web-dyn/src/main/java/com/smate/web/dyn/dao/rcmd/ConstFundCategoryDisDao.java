package com.smate.web.dyn.dao.rcmd;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.dyn.model.fund.rcmd.ConstFundCategoryDis;

/**
 * 基金机构领域DAO
 *
 * @author wsn
 * @createTime 2017年9月11日 下午3:21:01
 *
 */
@Repository
public class ConstFundCategoryDisDao extends RcmdHibernateDao<ConstFundCategoryDis, Long> {

  /**
   * 查找基金的科技领域
   * 
   * @param fundId
   * @return
   */
  public List<Long> findFundDisciplineIds(Long fundId) {
    String hql = "select t.disId from ConstFundCategoryDis t where t.categoryId = :fundId";
    return super.createQuery(hql).setParameter("fundId", fundId).list();
  }
}
