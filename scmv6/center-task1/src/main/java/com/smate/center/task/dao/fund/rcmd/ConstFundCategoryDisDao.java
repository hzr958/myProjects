package com.smate.center.task.dao.fund.rcmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundCategoryDis;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryDisDao extends RcmdHibernateDao<ConstFundCategoryDis, Long> {

  public void deleteFundDisByCategoryId(Long categoryId) {
    String hql = "delete from ConstFundCategoryDis t where t.categoryId=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public List<ConstFundCategoryDis> findFundDisByCategoryId(Long categoryId) {
    String hql = "from ConstFundCategoryDis t where t.categoryId=?";
    return find(hql, categoryId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFundDiscByFundId(List<Long> fundIdList) {
    String hql = "select disId  from ConstFundCategoryDis where categoryId in (:fundIds)";
    return super.createQuery(hql).setParameterList("fundIds", fundIdList).list();
  }

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

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getAllCategoryDis() {
    String sql =
        "select c.id, c.FUND_CATEGORY_ID,t.disc_code from const_fund_category_discipline c left join const_discipline t on  c.dis_id=t.id";
    List<Object[]> objList = super.getSession().createSQLQuery(sql).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    for (Object[] objects : objList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("id", Long.valueOf(String.valueOf(objects[0])));
      map.put("categoryId", Long.valueOf(String.valueOf(objects[1])));
      map.put("discCode", String.valueOf(objects[2]));
      listMap.add(map);
    }
    return listMap;
  }

  public void updateCategroyDis(Long scmCategoryId, Long id) {
    String hql =
        "update ConstFundCategoryDis t set t.disId = :scmCategoryId ,t.superDisId = :superDisId where t.id = :id";
    super.createQuery(hql).setParameter("scmCategoryId", scmCategoryId)
        .setParameter("superDisId", Long.valueOf(scmCategoryId.toString().substring(0, 1))).setParameter("id", id)
        .executeUpdate();
  }
}
