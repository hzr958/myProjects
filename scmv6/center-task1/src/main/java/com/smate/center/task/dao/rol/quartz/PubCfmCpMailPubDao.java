package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubCfmCpMailPub;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PubCfmCpMailPubDao extends RolHibernateDao<PubCfmCpMailPub, Long> {
  /**
   * 保存成果确认记录.
   * 
   * @param psnId
   * @param insId
   * @param pubId
   */
  public void savePubCfmCpMailPub(Long psnId, Long insId, Long pubId) {

    String hql = "select count(id) from PubCfmCpMailPub t where t.psnId =:psnId and t.pubId =:pubId ";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
    if (count > 0) {
      return;
    }
    PubCfmCpMailPub cfm = new PubCfmCpMailPub(psnId, insId, pubId);
    super.save(cfm);
  }

}
