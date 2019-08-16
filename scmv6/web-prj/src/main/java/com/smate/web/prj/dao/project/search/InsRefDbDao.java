package com.smate.web.prj.dao.project.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.search.InsRefDb;

/**
 * .机构/文献库关系表DAO
 * 
 * @author fanzhiqiang
 * 
 */
@Repository
public class InsRefDbDao extends SnsHibernateDao<InsRefDb, Long> {

  /**
   * 获取指定单位可用的文献库.
   * 
   * @param insId指定单位id
   * 
   * @return 返回可用单位列表
   */
  public List<InsRefDb> getDbByIns(List<Long> insIdList) {
    List<Criterion> criterionList = new ArrayList<Criterion>();
    criterionList.add(Restrictions.in("insRefDbId.insId", insIdList.toArray()));
    // criterionList.add(Restrictions.eq("enabled", 1));
    return find(criterionList.toArray(new Criterion[criterionList.size()]));
  }

  @SuppressWarnings("unchecked")
  public InsRefDb getURL(Long insId, Long dbId) {
    String sql = " from InsRefDb where ins_id=? and dbId=?";
    Query query = super.createQuery(sql, insId, dbId);
    List<InsRefDb> list = query.list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }

}
