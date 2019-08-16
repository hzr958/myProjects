package com.smate.web.psn.dao.psnhtml;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.psnhtml.PsnHtmlRefresh;

/**
 * 人员html刷新
 * 
 * @author zk
 * 
 */
@Repository
public class PsnHtmlRefreshDao extends SnsHibernateDao<PsnHtmlRefresh, Long> {

  /**
   * 通过人员和模板查询刷新记录
   * 
   * @param psnId
   * @param tempCode
   * @return
   */
  public PsnHtmlRefresh findByPsnIdAndTempCode(Long psnId, Integer tempCode) {
    String hql = "from PsnHtmlRefresh p where p.psnId=? and p.tempCode=? ";
    return (PsnHtmlRefresh) super.createQuery(hql, psnId, tempCode).uniqueResult();
  }

  /**
   * 通过人员查询刷新记录
   * 
   * @param psnId
   * @return
   */
  public PsnHtmlRefresh findByPsnId(Long psnId) {
    String hql = "from PsnHtmlRefresh p where p.psnId=? ";
    return (PsnHtmlRefresh) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 获取需要刷新的人员 废弃
   * 
   * @param max
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnHtmlRefresh> findNeedRefresh(Integer max) {
    String hql = "from PsnHtmlRefresh p where p.status=1";
    return super.createQuery(hql).setMaxResults(max).list();
  }

  /**
   * 获取需要刷新的人员 new
   * 
   * @param max
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnHtmlRefresh> findNeedRefresh(Integer max, Long psnId) {
    String hql = "from PsnHtmlRefresh p where p.status=1 and psnId > " + psnId + " order by psnId";
    return super.createQuery(hql).setMaxResults(max).list();
  }

  /**
   * 获取需要刷新的人员
   * 
   * @param psnId
   * @param status
   */
  public void updateRefresh(Long psnId, Integer status) {
    String hql = "update PsnHtmlRefresh p set p.status=:status where p.psnId=:psnId";
    super.createQuery(hql).setParameter("status", status).setParameter("psnId", psnId).executeUpdate();
  }
}
