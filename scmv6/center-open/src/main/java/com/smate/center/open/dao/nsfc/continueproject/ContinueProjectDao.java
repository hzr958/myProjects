package com.smate.center.open.dao.nsfc.continueproject;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.continueproject.ContinueProject;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 延续项目
 * 
 * @author tsz
 *
 */
@Repository
public class ContinueProjectDao extends HibernateDao<ContinueProject, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 根据基金委报告ID查找项目
   * 
   * @param nsfcRptId
   * @return
   */
  public ContinueProject findByNsfcRptId(Long psnId, Long nsfcRptId) {
    String hql = "select t from ContinueProject t,ContinueProjectReport b where t.nsfcPrjCode = b.nsfcPrjCode"
        + " and t.psnId = ? and b.nsfcRptId = ?";
    return (ContinueProject) this.createQuery(hql, psnId, nsfcRptId).uniqueResult();
  }
}
