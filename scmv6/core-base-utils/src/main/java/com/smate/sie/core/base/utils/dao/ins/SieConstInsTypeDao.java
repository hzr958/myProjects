package com.smate.sie.core.base.utils.dao.ins;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.ins.SieConstInsType;

/**
 * SIE 机构类型Dao
 * 
 * @author xr
 *
 */
@Repository
public class SieConstInsTypeDao extends SieHibernateDao<SieConstInsType, Long> {

  /**
   * 获取机构类型列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieConstInsType> getSieConstInsTypeList() {
    Criteria criteria = super.getSession().createCriteria(SieConstInsType.class);
    criteria.addOrder(Order.asc("nature"));
    return criteria.list();
  }

  /**
   * 通过机构类型名称获取SieConstInsType
   * 
   * @param zhName
   * @return
   */
  @SuppressWarnings("unchecked")
  public SieConstInsType getNatureByName(String zhName) {
    String hql = "from SieConstInsType t where t.zhName = ?";
    Query query = this.getSession().createQuery(hql).setParameter(0, zhName);
    List<SieConstInsType> list = query.list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
