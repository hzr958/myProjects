package com.smate.center.task.dao.rol.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rol.quartz.PubInsSync;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * pub-ins同步DAO.
 * 
 */
@Repository
public class PubInsSyncDao extends RolHibernateDao<PubInsSync, Long> {
  /**
   * 按主键读取.
   * 
   * @param snsPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubInsSync getPubInsSyncByPK(Long snsPubId, Long insId) throws DaoException {
    return (PubInsSync) super.createQuery("from PubInsSync t where t.id.snsPubId = ? and t.id.insId = ?",
        new Object[] {snsPubId, insId}).uniqueResult();
  }

  public void savePubInsSync(PubInsSync rec) throws DaoException {
    super.save(rec);

  }

}
