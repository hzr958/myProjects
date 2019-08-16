package com.smate.center.task.dao.rol.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PsnPmKeyWord;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class PsnPmKeyWordDao extends RolHibernateDao<PsnPmKeyWord, Long> {
  /**
   * 获取用户确认过的成果关键词.
   * 
   * @param kwhash
   * @param psnId
   * @return
   */
  public PsnPmKeyWord getPsnPmKeyWord(Integer kwhash, Long psnId) {
    String hql = "from PsnPmKeyWord where kwhash =:kwhash and psnId =:psnId";
    List<PsnPmKeyWord> list = super.createQuery(hql).setParameter("kwhash", kwhash).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
