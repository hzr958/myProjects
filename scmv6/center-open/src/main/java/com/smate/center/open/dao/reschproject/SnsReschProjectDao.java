package com.smate.center.open.dao.reschproject;



import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 
 * nsfc项目dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class SnsReschProjectDao extends HibernateDao<NsfcReschProject, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public NsfcReschProject getNsfcReschProject(Long nsfcPrjId) throws Exception {
    String hql = "from NsfcReschProject where nsfcPrjId=?";
    return findUnique(hql, nsfcPrjId);
  }


  public void updateReschPsnId(Long prjId, Long psnId) throws Exception {
    String hql = "update NsfcReschProject t set t.piPsnId = ? where t.prjId = ?";
    super.createQuery(hql, psnId, prjId).executeUpdate();
  }



}
