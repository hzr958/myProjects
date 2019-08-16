package com.smate.center.data.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.data.model.pub.PdwhPubKeywordsCategory;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 计算pdwh成果关键词组合频次相关dao
 * 
 * @author lhd
 *
 */
@Repository
public class PdwhPubKeywordsCategoryDao extends SnsHibernateDao<PdwhPubKeywordsCategory, Long> {

  /**
   * 获取指定数量的成果关键词
   * 
   * @param size
   * @param startPubId
   * @param endPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPubKeywordsCategory> getPdwhPubKeywords(Integer size, Long startId, Long endId) {
    String hql =
        "from PdwhPubKeywordsCategory t where t.status=0 and t.id > :startId and t.id <= :endId order by t.id ";
    return createQuery(hql).setParameter("startId", startId).setParameter("endId", endId).setMaxResults(size).list();
  }

  /**
   * 更新状态
   * 
   * @param pubId
   * @param status
   */
  public void saveOpResult(Long id, int status) {
    String hql = "update PdwhPubKeywordsCategory  t set t.status = :status where t.id = :id";
    super.createQuery(hql).setParameter("status", status).setParameter("id", id).executeUpdate();
  }

}
