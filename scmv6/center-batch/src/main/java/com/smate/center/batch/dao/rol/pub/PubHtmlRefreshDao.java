package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubHtmlRefresh;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 成果html刷新
 * 
 * @author zk
 * 
 */
@Repository
public class PubHtmlRefreshDao extends RolHibernateDao<PubHtmlRefresh, Long> {

  public PubHtmlRefresh findNeedRefresh(Long pubId, Integer tempCode) {
    String hql = "from PubHtmlRefresh p where p.pubId=? and p.tempCode=? and p.status =1";
    return (PubHtmlRefresh) super.createQuery(hql, pubId, tempCode).uniqueResult();
  }

  public PubHtmlRefresh findByPsnIdAndTempCode(Long pubId, Integer tempCode) {

    String hql = "from PubHtmlRefresh p where p.pubId=? and p.tempCode=? ";
    return (PubHtmlRefresh) super.createQuery(hql, pubId, tempCode).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubHtmlRefresh> findNeedRefresh(Integer tempCode, int size) {
    String hql = "from PubHtmlRefresh p where p.tempCode=? and p.status=1";
    return super.createQuery(hql, tempCode).setMaxResults(size).list();
  }

  public List<PubHtmlRefresh> findNeedRefresh(int size) {
    String hql = "from PubHtmlRefresh p where p.status=1 and rownum <= ?";
    return super.createQuery(hql, Long.valueOf(size)).setMaxResults(size).list();
  }
}
