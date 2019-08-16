package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.center.task.model.Sie6InsUnit;

/**
 * 单位部门DAO.
 * 
 * @author xys
 *
 */
@Repository
public class Sie6InsUnitDao extends SieHibernateDao<Sie6InsUnit, Long> {
  @SuppressWarnings("unchecked")
  public List<Sie6InsUnit> getListByInsId(Long insId) {
    String hql = " from Sie6InsUnit t where t.insId= ? order by id ";
    return super.createQuery(hql, insId).list();
  }

  @SuppressWarnings("unchecked")
  public Page<Sie6InsUnit> getAllUnits(Page<Sie6InsUnit> page) {
    String hql = "select new Sie6InsUnit(t.id) from SieInsUnit t";
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<Sie6InsUnit> result = q.list();
    page.setResult(result);
    return page;
  }

}
