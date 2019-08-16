package com.smate.center.batch.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PublicationLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果日志Dao.
 * 
 * @author yamingd
 */
@Repository
public class PublicationLogDao extends SnsHibernateDao<PublicationLog, Long> {

  /**
   * @param pubId 成果ID
   * @param opPsnId 操作人
   * @param insId 单位ID
   * @param op 操作枚举
   * @param opDetail 操作详细
   * @param versionNo 成果版本号
   * @throws DaoException
   */
  public void logOp(long pubId, long opPsnId, Long insId, PublicationOperationEnum op, String opDetail)
      throws DaoException {
    PublicationLog publog = new PublicationLog();
    publog.setOpPsnId(opPsnId);
    publog.setOpAction(op.ordinal());
    publog.setOpDate(new Date());
    publog.setOpDetail(opDetail);
    publog.setPubId(pubId);
    publog.setInsId(insId);
    super.save(publog);
  }

  /**
   * 查询成果对应的日志记录.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PublicationLog> queryPubLogByPubId(Long pubId) throws DaoException {
    return super.createQuery("from PublicationLog t where t.pubId=?", new Object[] {pubId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<PublicationLog> queryPubLogByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from PublicationLog t where t.opPsnId=?", new Object[] {psnId}).list();
  }

  public void deletePubLogById(Long id) throws DaoException {
    super.createQuery("delete from PublicationLog t where t.id=?", id).executeUpdate();
  }
}
