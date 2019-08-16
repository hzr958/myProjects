package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.DbCacheBfetch;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 批量抓取成果XML临时存储表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class DbCacheBfetchDao extends PdwhHibernateDao<DbCacheBfetch, Long> {

  /**
   * 保存错误信息.
   * 
   * @param id
   * @param message
   */
  public void saveError(Long id, String message) {

    String hql = "update DbCacheBfetch t set t.status = 9,t.errorMsg = ? where t.xmlId = ? ";
    super.createQuery(hql, StringUtils.substring(message, 0, 50), id).executeUpdate();
  }

  /**
   * 保存成功信息.
   * 
   * @param id
   */
  public void saveSuccess(Long id) {

    String hql = "update DbCacheBfetch t set t.status = 1 where t.xmlId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取待展开成果XML列表.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCacheBfetch> loadExpandBatch(int size) {

    String hql = "from DbCacheBfetch t where t.status = 0 ";

    return super.createQuery(hql).setMaxResults(size).list();
  }
}
