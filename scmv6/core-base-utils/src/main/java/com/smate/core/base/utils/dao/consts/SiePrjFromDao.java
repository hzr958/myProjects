package com.smate.core.base.utils.dao.consts;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.prjform.PrjFrom;

/**
 * 
 * @author yxs
 * @since 2018年3月1日
 * @descript 项目来源dao
 */
@Repository
public class SiePrjFromDao extends SieHibernateDao<PrjFrom, Long> {

  /**
   * 
   * 
   * @descript 获取项目来源列表
   */
  @SuppressWarnings("unchecked")
  public List<PrjFrom> getPrjFromList() {
    Criteria criteria = super.getSession().createCriteria(PrjFrom.class);
    criteria.addOrder(Order.asc("id"));
    return criteria.list();
  }
}
