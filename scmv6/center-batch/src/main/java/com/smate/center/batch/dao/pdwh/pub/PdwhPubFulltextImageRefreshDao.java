package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.PdwhPubFulltextImageRefresh;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * pdwh成果全文转换图片刷新持久类
 * 
 * @author LJ
 *
 *         2017年9月12日
 */
@Repository
public class PdwhPubFulltextImageRefreshDao extends PdwhHibernateDao<PdwhPubFulltextImageRefresh, Long> {

  /**
   * 批量查询需要刷新的数据.
   * 
   * @param startId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPubFulltextImageRefresh> queryRefreshDataBatch(int maxSize) throws DaoException {
    String hql = "from PdwhPubFulltextImageRefresh t where t.status=? "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return super.createQuery(hql, new Object[] {0}).setMaxResults(maxSize).list();
  }

  /**
   * 删除成果全文图片刷新记录.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void deletePubFulltextImageRefresh(Long pubId) throws DaoException {
    super.createQuery("delete from PdwhPubFulltextImageRefresh t where t.pubId=?", pubId).executeUpdate();
  }

  public PdwhPubFulltextImageRefresh getPubFulltextImageRefreshById(Long id) throws BatchTaskException {
    String hql = "from PdwhPubFulltextImageRefresh t where t.pubId = ? "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return super.findUnique(hql, id);
  }

  /**
   * 获取基准库全文图片待处理数据
   * 
   * @param pubId
   * @return
   */
  public PdwhPubFulltextImageRefresh getPdwhPubFulltextImageRefreshById(Long pubId) {
    String hql = "from PdwhPubFulltextImageRefresh t where t.pubId = :pubId and t.fulltextNode=:fulltextNode "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return (PdwhPubFulltextImageRefresh) super.createQuery(hql).setParameter("pubId", pubId)
        .setParameter("fulltextNode", ServiceConstants.PDWH_NODE_ID).uniqueResult();
  }
}
