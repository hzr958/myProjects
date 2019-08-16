package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.pub.PubFulltextImageRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 成果全文转换图片刷新持久类.
 * 
 * @author pwl
 * 
 */
@Repository
public class PubFulltextImageRefreshDao extends SnsHibernateDao<PubFulltextImageRefresh, Long> {

  /**
   * 批量查询需要刷新的数据.
   * 
   * @param startId
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubFulltextImageRefresh> queryRefreshDataBatch(int maxSize) throws DaoException {
    return super.createQuery("from PubFulltextImageRefresh t where t.status=?", new Object[] {0}).setMaxResults(maxSize)
        .list();
  }

  /**
   * 删除成果全文图片刷新记录.
   * 
   * @param pubId
   * @throws DaoException
   */
  public void deletePubFulltextImageRefresh(Long pubId) throws DaoException {
    super.createQuery("delete from PubFulltextImageRefresh t where t.pubId=?", pubId).executeUpdate();
  }

  public PubFulltextImageRefresh getPubFulltextImageRefreshById(Long id) throws BatchTaskException {
    String hql = "from PubFulltextImageRefresh t where t.pubId = ?";
    return super.findUnique(hql, id);
  }
}
