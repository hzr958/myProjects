package com.smate.center.open.dao.nsfc.continueproject;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.continueproject.ConPrjRptPub;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 延续报告成果 dao
 * 
 * @author tsz
 *
 */
@Repository
public class ConPrjRptPubDao extends HibernateDao<ConPrjRptPub, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 查看某人已经提交的成果
   * 
   * @param psnId
   * @param nsfcRptId
   * @return
   */
  public List<ConPrjRptPub> findRptPubs(Long psnId, Long nsfcRptId) {
    String hql = "select t from ConPrjRptPub t,ContinueProjectReport b,ContinueProject c"
        + " where t.nsfcRptId = b.nsfcRptId and b.nsfcPrjCode = c.nsfcPrjCode and t.nsfcRptId = ? and c.psnId = ?"
        + " order by t.seqNo asc,t.pubYear desc";
    return this.createQuery(hql, nsfcRptId, psnId).list();
  }

}
