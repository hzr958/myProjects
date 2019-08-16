package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.DbCacheBfetch;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 批量抓取成果XML处理DAO.
 * 
 * @author LJ 2017-2-28
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
    super.createQuery(hql, StringUtils.substring(message, 0, 200), id).executeUpdate();
  }

  /**
   * 处理成功更新状态
   * 
   * @param id
   */
  public void modifyStatus(Long id) {

    String hql = "update DbCacheBfetch t set t.status = 1 ,t.errorMsg= '' where t.xmlId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取待处理成果XML列表.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCacheBfetch> getToHandleXmlList(int size) {

    String hql = "from DbCacheBfetch t where t.status = 0 order by t.xmlId";

    return super.createQuery(hql).setMaxResults(size).list();
  }

}
