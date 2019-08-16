package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.GroupCooperationStatistics;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 
 * 合作分析：群组统计
 */
@Repository
public class GroupCooperationStatisticsDao extends RolHibernateDao<GroupCooperationStatistics, Long> {

  public GroupCooperationStatistics findByGroupId(Long groupId) throws DaoException {
    String hql = "from GroupCooperationStatistics t where t.groupId = ?";

    return (GroupCooperationStatistics) super.createQuery(hql, new Object[] {groupId}).uniqueResult();
  }

  // 合作分析：群组统计
  @SuppressWarnings("unchecked")
  public Page<GroupCooperationStatistics> findGroupStatisticsList(Page<GroupCooperationStatistics> page,
      String category, Long unitId, Long insId) throws DaoException {
    StringBuilder countHql = new StringBuilder("select count(t.id) ");
    StringBuilder orderHql = new StringBuilder(" order by t.id desc");

    List<Object> params = new ArrayList<Object>();

    StringBuilder hql = new StringBuilder("from GroupCooperationStatistics t where ");

    if (unitId != null && unitId > 0) {// 部门
      hql.append(
          " exists(select 1 from GroupPsnRelation gp where gp.insId=? and (gp.unitId=? or gp.superUnitId=?) and gp.groupId=t.groupId)");
      params.add(insId);
      params.add(unitId);
      params.add(unitId);
    } else {// 单位
      hql.append(" exists(select 1 from GroupPsnRelation gp where gp.insId=? and gp.groupId=t.groupId)");
      params.add(insId);
    }
    // 群组分类
    if (category != null && !"0".equals(category)) {
      hql.append(" and t.groupCategory = ?");
      params.add(category);
    }

    // 记录数
    Long totalCount = super.findUnique(countHql.append(hql).toString(), params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(hql.append(orderHql).toString(), params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());

    return page;
  }

}
