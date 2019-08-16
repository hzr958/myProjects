package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.pub.PdwhPubFulltextImageRefresh;
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
    return super.createQuery("from PdwhPubFulltextImageRefresh t where t.status=?", new Object[] {0})
        .setMaxResults(maxSize).list();
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
    String hql = "from PdwhPubFulltextImageRefresh t where t.pubId = ?";
    return super.findUnique(hql, id);
  }

  /**
   * 获取基准库全文图片待处理数据
   * 
   * @param pubId
   * @return
   */
  public PdwhPubFulltextImageRefresh getPdwhPubFulltextImageRefreshById(Long pubId) {
    String hql = "from PdwhPubFulltextImageRefresh t where t.pubId = :pubId and t.fulltextNode=:fulltextNode";
    return (PdwhPubFulltextImageRefresh) super.createQuery(hql).setParameter("pubId", pubId)
        .setParameter("fulltextNode", ServiceConstants.PDWH_NODE_ID).uniqueResult();
  }
}
