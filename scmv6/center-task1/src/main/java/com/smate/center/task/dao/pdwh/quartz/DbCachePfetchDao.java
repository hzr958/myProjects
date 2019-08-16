package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.DbCachePfetch;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 批量抓取成果XML处理DAO.
 * 
 * @author LJ 2017-3-10
 * 
 */
@Repository
public class DbCachePfetchDao extends PdwhHibernateDao<DbCachePfetch, Long> {

  /**
   * 更新为错误状态.
   * 
   * @param id
   * 
   */
  public void saveError(Long id) {
    String hql = "update DbCachePfetch t set t.status = 9 where t.xmlId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 处理成功更新状态
   * 
   * @param id
   */
  public void modifyStatus(Long id) {

    String hql = "update DbCachePfetch t set t.status = 1  where t.xmlId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取待处理成果XML列表.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DbCachePfetch> getToHandleXmlList(int size) {

    String hql = "from DbCachePfetch t where t.status = 0 order by t.xmlId";

    return super.createQuery(hql).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<DbCachePfetch> findByPsnId(Long psnId) {
    String hql = "from DbCachePfetch t where t.psnId=:psnId "
        + "and t.fetchTime >= to_date('2018-10-15 21:00:00','yyyy-mm-dd hh24:mi:ss') "
        + "and t.fetchTime <= to_date('2018-10-19 21:00:00','yyyy-mm-dd hh24:mi:ss')";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
