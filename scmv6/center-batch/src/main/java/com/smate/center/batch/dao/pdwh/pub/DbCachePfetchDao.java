package com.smate.center.batch.dao.pdwh.pub;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.DbCachePfetch;
import com.smate.center.batch.model.pdwh.pub.DbCachePfetchError;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 个人抓取成果XML临时存储表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class DbCachePfetchDao extends PdwhHibernateDao<DbCachePfetch, Long> {

  /**
   * 批量获取个人抓取成果XML临时存储表.
   * 
   * @param startId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCachePfetch> getDbCachePfetchBatch(Long startId, int size) {

    String hql = "from DbCachePfetch t where t.xmlId > ? and status = 0 order by t.xmlId asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 删除临时个人抓取成果XML临时存储表.
   * 
   * @param xmlIds
   */
  public void removeDbCachePfetch(List<Long> xmlIds) {
    if (CollectionUtils.isEmpty(xmlIds)) {
      return;
    }
    // 拆分10条分批处理
    Collection<Collection<Long>> cc = ServiceUtil.splitList(xmlIds, 10);
    for (Collection<Long> c : cc) {
      String hql = "delete from DbCachePfetch t where t.xmlId in(:xmlIds)";
      super.createQuery(hql).setParameterList("xmlIds", c).executeUpdate();

      hql = "delete from DbCachePfetchError t where t.xmlId in(:xmlIds)";
      super.createQuery(hql).setParameterList("xmlIds", c).executeUpdate();
    }
  }

  /**
   * 删除临时个人抓取成果XML临时存储表.
   * 
   * @param xmlIds
   */
  public void remarkUnValidate(List<Long> xmlIds) {
    if (CollectionUtils.isEmpty(xmlIds)) {
      return;
    }
    // 更新状态为异常状态
    String hql = "update DbCachePfetch t set t.status = 3 where t.xmlId in(:xmlIds) ";
    super.createQuery(hql).setParameterList("xmlIds", xmlIds).executeUpdate();
  }

  /**
   * 保存错误信息.
   * 
   * @param error
   */
  public void saveErrorMsg(DbCachePfetchError error) {

    // 更新状态为异常状态
    String hql = "update DbCachePfetch t set t.status = 9 where t.xmlId = ? ";
    super.createQuery(hql, error.getXmlId()).executeUpdate();

    hql = "delete from DbCachePfetchError t where t.xmlId = ? ";
    super.createQuery(hql, error.getXmlId()).executeUpdate();
    super.getSession().save(error);

  }
}
