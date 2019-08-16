package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.SiePsnHtmlRefresh;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 人员html刷新
 * 
 * @author zk
 * 
 */
@Repository
public class SiePsnHtmlRefreshDao extends RolHibernateDao<SiePsnHtmlRefresh, Long> {

  /**
   * 通过人员和模板查询刷新记录
   * 
   * @param psnId
   * @param tempCode
   * @return
   */
  public SiePsnHtmlRefresh findByPsnIdAndTempCode(Long psnId, Integer tempCode) {
    String hql = "from SiePsnHtmlRefresh p where p.psnId=? and p.tempCode=? ";
    return (SiePsnHtmlRefresh) super.createQuery(hql, psnId, tempCode).uniqueResult();
  }

  /**
   * 通过人员查询刷新记录
   * 
   * @param psnId
   * @return
   */
  public SiePsnHtmlRefresh findByPsnId(Long psnId) {
    String hql = "from SiePsnHtmlRefresh p where p.psnId=? ";
    return (SiePsnHtmlRefresh) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 获取需要刷新的人员 废弃
   * 
   * @param max
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePsnHtmlRefresh> findNeedRefresh(Integer max) {
    String hql = "from SiePsnHtmlRefresh p where p.status=1";
    return super.createQuery(hql).setMaxResults(max).list();
  }

  /**
   * 获取需要刷新的人员 new
   * 
   * @param max
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePsnHtmlRefresh> findNeedRefresh(Integer max, Long psnId) {
    String hql = "from SiePsnHtmlRefresh p where p.status=1 and psnId > " + psnId + " order by psnId";
    return super.createQuery(hql).setMaxResults(max).list();
  }

  /**
   * 获取需要刷新的人员
   * 
   * @param psnId
   * @param status
   */
  public void updateRefresh(Long psnId, Integer status) {
    String hql = "update SiePsnHtmlRefresh p set p.status=? where p.psnId=?";
    super.createQuery(hql, status, psnId).executeUpdate();
  }
}
