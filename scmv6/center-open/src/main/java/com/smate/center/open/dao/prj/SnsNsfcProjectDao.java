package com.smate.center.open.dao.prj;



import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 
 * nsfc项目dao.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class SnsNsfcProjectDao extends HibernateDao<NsfcProject, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public NsfcProject getNsfcProject(Long nsfcPrjId) throws Exception {
    String hql = "from NsfcProject where nsfcPrjId=?";
    return findUnique(hql, nsfcPrjId);
  }


}
