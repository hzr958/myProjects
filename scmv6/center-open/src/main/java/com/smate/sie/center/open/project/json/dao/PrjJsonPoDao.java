package com.smate.sie.center.open.project.json.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.ProjectJsonPo;

/**
 * 项目json DAO层
 * 
 * @author lijianming
 *
 * @date 2019年6月5日
 */
@Repository
public class PrjJsonPoDao extends SieHibernateDao<ProjectJsonPo, Long> {

  public ProjectJsonPo getJsonByPrjId(Long prjId) {
    String hql = "from ProjectJsonPo t where t.prjId = :prjId";
    return (ProjectJsonPo) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }
}
