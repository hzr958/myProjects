package com.smate.core.base.utils.dao.shorturl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.shorturl.OpenShortUrl;

@Repository
public class OpenShortUrlDao extends SnsHibernateDao<OpenShortUrl, Long> {
  /**
   * 通过短地址获取OpenShortUrl
   * 
   * @param shortUrl 短地址
   * @return
   */
  public OpenShortUrl getOpenShortUrlByShortUrl(String shortUrl) {
    String hql = "from OpenShortUrl t where t.shortUrl=:shortUrl ";
    return (OpenShortUrl) this.createQuery(hql).setParameter("shortUrl", shortUrl).uniqueResult();
  }

  /**
   * 修改短地址
   * 
   * @param oldShortUrl
   * @param newShortUrl
   */
  public void modifierShortUrl(String oldShortUrl, String newShortUrl) {
    String hql = "update OpenShortUrl t set t.shortUrl=:newShortUrl where t.shortUrl=:oldShortUrl";
    this.createQuery(hql).setParameter("newShortUrl", newShortUrl).setParameter("oldShortUrl", oldShortUrl)
        .executeUpdate();
  }

  /**
   * 判断短地址是否存在
   * 
   * @param shortUrl
   * @return
   */
  public boolean isExIst(String shortUrl) {
    String hql = "select count(t.id) from OpenShortUrl t where t.shortUrl=:shortUrl ";
    Object object = this.createQuery(hql).setParameter("shortUrl", shortUrl).uniqueResult();
    if (object != null && (Long) object > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 检查短地址是否存在
   * 
   * @param shortUrl
   * @return
   */
  public boolean checkShortUrlIsExists(String shortUrl) {
    String hql = "select  t.id  from  OpenShortUrl  t  where t.shortUrl =:shortUrl ";
    Object obj = this.createQuery(hql).setParameter("shortUrl", shortUrl).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  /**
   * 查询过期或者次数用完的短地址
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getHasExpiredUrl() {

    String hql =
        "select t.id from OpenShortUrl t where( t.hasExpirationTime=1 and t.expirationTime<:time )or( t.hasTimesLimit=1 and t.timesLimit=0 )";
    return super.createQuery(hql).setTimestamp("time", new Date()).list();

  }

}
