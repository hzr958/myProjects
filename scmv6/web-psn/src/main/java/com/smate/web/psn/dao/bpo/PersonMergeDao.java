package com.smate.web.psn.dao.bpo;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.bpo.PersonMerge;

/**
 * 人员合并.
 * 
 * @author ajb
 * 
 */
@Repository
public class PersonMergeDao extends BpoHibernateDao<PersonMerge, Long> {

  /**
   * 保存需合并的人员记录.
   * 
   * @param mergePsn
   * @throws DaoException
   */
  public void savePersonMerge(PersonMerge mergePsn) throws DaoException {
    if (mergePsn != null) {
      // 如果已经保存合并记录，则直接返回.
      PersonMerge tempMerge = this.findPersonMerge(mergePsn.getSavePsnId(), mergePsn.getDelPsnId());
      if (tempMerge != null) {
        return;
      }
      super.save(mergePsn);
    }
  }

  /**
   * 
   * @param savePsnId
   * @param delPsnId
   * @return
   * @throws DaoException
   */
  public PersonMerge findPersonMerge(Long savePsnId, Long delPsnId) throws DaoException {
    String hql = " from PersonMerge t where t.savePsnId=:savePsnId and t.delPsnId=:delPsnId ";
    List list = this.createQuery(hql).setParameter("savePsnId", savePsnId).setParameter("delPsnId", delPsnId).list();
    if (list != null && list.size() > 0) {
      return (PersonMerge) list.get(0);
    }
    return null;
  }

}
