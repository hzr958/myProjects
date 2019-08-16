package com.smate.center.open.dao.data.isis;


import java.util.List;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isis.NsfcProject;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * dao
 * 
 * @author hp
 * 
 */
@Repository
public class NsfcProjectDao extends RolHibernateDao<NsfcProject, Long> {
  @SuppressWarnings("unchecked")
  public List<NsfcProject> getNsfcProjectListByRptId(Set<Long> prjIdSet) {
    String hql = "from NsfcProject n where n.prjId in (:prjIdSet) ";
    return super.createQuery(hql).setParameterList("prjIdSet", prjIdSet).list();
  }


}
