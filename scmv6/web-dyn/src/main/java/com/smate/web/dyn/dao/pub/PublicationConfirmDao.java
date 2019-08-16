package com.smate.web.dyn.dao.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.dyn.model.pub.PublicationConfirm;

/**
 * 
 * 成果推荐列表
 * 
 * @author tj
 * 
 */

@Repository
public class PublicationConfirmDao extends RcmdHibernateDao<PublicationConfirm, Long> {

  /**
   * 
   * 查询需要确认的成果总数.
   * 
   * @param psnId
   * @return
   */
  public Long queryPubConfirmCount(Long psnId) {

    String hql =
        "select count(p.id) from PubConfirmRolPub t,PublicationConfirm p where t.rolPubId=p.rolPubId and p.confirmResult=0 and p.psnId=?";

    return super.findUnique(hql, psnId);

  }

}
