package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubPsn;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * CNKI的成果人员关系表(对科研之友成果关系进行冗余)的数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class CnkiPubPsnDao extends PdwhHibernateDao<CnkiPubPsn, Long> {

  /**
   * 获取人员已确认或已拒绝的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  public List<CnkiPubPsn> getCnkiPubPsnByPsn(Long psnId) {
    String hql = "select new CnkiPubPsn(pubId,result) from CnkiPubPsn t where t.result in (1,2) and t.psnId=? ";
    return super.find(hql, psnId);
  }

  /**
   * 获取所有成果人员关系列表.
   * 
   * @param psnId
   * @return
   */
  public List<CnkiPubPsn> getAllByPsnId(Long psnId) {
    String hql = "from CnkiPubPsn t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  /**
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete CnkiPubPsn t where t.psnId=?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  public CnkiPubPsn getCnkiPubPsn(Long psnId, Long pubId) throws DaoException {
    return super.findUnique("from CnkiPubPsn t where t.pubId=? and t.psnId=?", pubId, psnId);
  }
}
