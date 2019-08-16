package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.DbCache;
import com.smate.center.batch.model.pdwh.pub.DbCacheError;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * DbCache DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class DbCacheDao extends PdwhHibernateDao<DbCache, Long> {

  /**
   * 批量获取DbCache.
   * 
   * @param startId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCache> getDbCacheBatch(Long startId, int size) {

    String hql = "from DbCache t where t.xmlId > ? order by t.xmlId asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 删除临时Dbcache.
   * 
   * @param xmlIds
   */
  public void removeDbCache(List<Long> xmlIds) {

    if (CollectionUtils.isEmpty(xmlIds)) {
      return;
    }
    String hql = "delete from DbCache t where t.xmlId in(:xmlIds)";
    super.createQuery(hql).setParameterList("xmlIds", xmlIds).executeUpdate();

    hql = "delete from DbCacheError t where t.xmlId in(:xmlIds)";
    super.createQuery(hql).setParameterList("xmlIds", xmlIds).executeUpdate();
  }

  /**
   * 保存错误信息.
   * 
   * @param error
   */
  public void saveErrorMsg(DbCacheError error) {

    String hql = "delete from DbCacheError t where t.xmlId = ? ";
    super.createQuery(hql, error.getXmlId()).executeUpdate();
    super.getSession().save(error);
  }
}
