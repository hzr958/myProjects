package com.smate.core.base.utils.dao.consts;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.SieDiscipline;
import com.smate.core.base.utils.data.SieHibernateDao;

/**
 * 
 * @author yxs
 * @descript 学科dao
 */
@Repository
public class SieDisciplineDao extends SieHibernateDao<SieDiscipline, Long> {

  /**
   * 
   * @author yxs
   * @descript 查询学科常数表
   */
  public List<SieDiscipline> getDisciplinetNames(String superCode) {
    Criteria criteria = super.getSession().createCriteria(SieDiscipline.class);
    if (StringUtils.isNotBlank(superCode)) {
      criteria.add(Restrictions.eq("superCode", superCode));
    } else {
      criteria.add(Restrictions.isNull("superCode"));
    }
    criteria.addOrder(Order.asc("disCode"));
    return criteria.list();

  }
}
