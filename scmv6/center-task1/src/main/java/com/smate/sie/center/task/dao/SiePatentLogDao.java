package com.smate.sie.center.task.dao;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePatentLog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 专利日志Dao.
 * 
 * @author jszhou
 */
@Repository
public class SiePatentLogDao extends SieHibernateDao<SiePatentLog, Long> {

  /**
   * @param patId 专利ID
   * @param opPsnId 操作人
   * @param insId 单位ID
   * @param op 操作枚举
   * @param opDetail 操作详细
   * @param versionNo 专利版本号
   * @throws DaoException
   */
  public void logOp(long patId, long opPsnId, Long insId, String op, String opDetail) throws DaoException {
    SiePatentLog patlog = new SiePatentLog();
    patlog.setOpPsnId(opPsnId);
    patlog.setOpAction(op);
    patlog.setOpDate(new Date());
    patlog.setOpDetail(opDetail);
    patlog.setPatId(patId);
    patlog.setInsId(insId);
    super.save(patlog);
  }

  /**
   * 查询专利对应的日志记录.
   * 
   * @param patId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<SiePatentLog> queryPubLogByPatId(Long patId) throws DaoException {
    return super.createQuery("from SiePatentLog t where t.patId=?", new Object[] {patId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePatentLog> queryPatentLogByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from SiePatentLog t where t.opPsnId=?", new Object[] {psnId}).list();
  }

  public void deletePatentLogById(Long id) throws DaoException {
    super.createQuery("delete from SiePatentLog t where t.id=?", id).executeUpdate();
  }
}
