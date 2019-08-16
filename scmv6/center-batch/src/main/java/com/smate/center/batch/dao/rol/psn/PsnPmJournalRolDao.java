package com.smate.center.batch.dao.rol.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 用户确认过的期刊.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmJournalRolDao extends RolHibernateDao<PsnPmJournalRol, Long> {

  /**
   * 获取用户确认过的期刊.
   * 
   * @param jid
   * @param psnId
   * @return
   */
  public PsnPmJournalRol getPsnPmJournal(Long jid, Long psnId) {
    String hql = "from PsnPmJournalRol where jid=? and psnId = ? ";
    List<PsnPmJournalRol> list = super.find(hql, jid, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmJournalRol> getPsnPmJournalList(Long psnId) {

    String ql = "from PsnPmJournalRol where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmJournalRol where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
