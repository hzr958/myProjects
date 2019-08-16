package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.DbCachePfetchQuery;
import com.smate.center.batch.service.pdwh.pub.DbCachePfetchQueryServiceImpl;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 在线导入成果，查询基准库ID消息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class DbCachePfetchQueryDao extends PdwhHibernateDao<DbCachePfetchQuery, Long> {

  /**
   * 获取需要查询基准库ID的信息.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCachePfetchQuery> getNeedQueryList() {

    String hql = "from DbCachePfetchQuery t where t.queryNum <= ? and t.result = 0 ";
    return super.createQuery(hql, DbCachePfetchQueryServiceImpl.MAX_QUERY_NUM).setMaxResults(100).list();
  }

  /**
   * 获取需要发送的基准库ID的消息.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCachePfetchQuery> getNeedSendQueryList() {
    String hql = "from DbCachePfetchQuery t where  t.result = 1 and t.isSend = 0 ";
    return super.createQuery(hql).setMaxResults(100).list();
  }

  // -----------人员合并 start----------
  @SuppressWarnings("unchecked")
  public List<DbCachePfetchQuery> getQueryListByPsnId(Long psnId) {
    String hql = "from DbCachePfetchQuery t where t.psnId = ?";
    return super.createQuery(hql, psnId).list();
  }
  // -----------人员合并 end----------
}
