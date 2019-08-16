package com.smate.center.open.dao.inspg;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.inspg.Inspg;
import com.smate.core.base.utils.data.SnsHibernateDao;



/**
 * 机构主页实体Dao
 * 
 * 
 * @author ajb
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Repository
public class InspgDao extends SnsHibernateDao<Inspg, Long> {



  /**
   * 获取机构主页信息
   * 
   * @param inspgIds
   * @return
   * @throws InspgDataException
   */

  @SuppressWarnings("unchecked")
  public List<Inspg> getInspgByInspgIds(List<Long> inspgAdminIds) {
    if (CollectionUtils.isEmpty(inspgAdminIds)) {
      return new ArrayList<Inspg>();
    }
    String hql =
        "select new Inspg(i.id,i.name,i.logoUrl ,i.createTime) from Inspg i where i.id in (:inspgAdminIds)  and  i.status = 1 and i.inspgType = 4 order by  i.createTime desc";
    Query query = super.createQuery(hql).setParameterList("inspgAdminIds", inspgAdminIds);

    return query.list();
  }
}
