package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PubCategoryPatent;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class PubCategoryPatentDao extends PdwhHibernateDao<PubCategoryPatent, Long> {

  public Long findTechfiledIdByPubId(Long pdwhPubId) {
    String hql = "select t.scmCategoryId from PubCategoryPatent t where t.pubId =:pubId";
    List<Long> list = super.createQuery(hql).setParameter("pubId", pdwhPubId).setMaxResults(1).list();
    if (list == null || list.size() == 0) {
      return null;
    } else {
      return Long.parseLong(list.get(0).toString().substring(0, 1));
    }
  }

  public boolean findExist(Long pubId) {
    String hql = "from PubCategoryPatent t where t.pubId =:pubId ";
    List list = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

}
