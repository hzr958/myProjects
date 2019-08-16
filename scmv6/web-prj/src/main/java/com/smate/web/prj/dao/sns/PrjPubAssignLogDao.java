package com.smate.web.prj.dao.sns;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.PrjPubAssignLog;

/**
 * 项目成果指派记录表dao
 * 
 * @author yhx
 * @date 2019年8月9日
 *
 */
@Repository
public class PrjPubAssignLogDao extends SnsHibernateDao<PrjPubAssignLog, Serializable> {

  public void updateConfirmResult(Long prjId, Long pubId, Integer confirmResult) {
    String hql =
        "update PrjPubAssignLog t set t.confirmResult = :confirmResult ,t.confirmDate= :confirmDate where t.pubId= :pubId and t.prjId=:prjId";
    super.createQuery(hql).setParameter("confirmResult", confirmResult).setParameter("confirmDate", new Date())
        .setParameter("pubId", pubId).setParameter("prjId", prjId).executeUpdate();
  }
}
