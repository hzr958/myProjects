package com.smate.core.base.utils.dao.shorturl;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.shorturl.OpenShortUrlPreproduction;

/**
 * 短地址预产生
 * 
 * @author tsz
 *
 */
@Repository
public class OpenShortUrlPreproductionDao extends SnsHibernateDao<OpenShortUrlPreproduction, Long> {

  /**
   * 预取id
   * 
   * @return
   */
  public Long getNewShortUrlId() {
    String sql = "select SEQ_V_OPEN_SHORT_URL_Pre_1.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Long id = Long.parseLong(query.uniqueResult().toString());
    return id;
  }

  /**
   * 通过 id取短地址
   * 
   * @param id
   * @return
   */
  public String getShortUrlById(Long id) {
    String hql = "select t.shortUrl from OpenShortUrlPreproduction t where t.id=:id ";
    Object obj = this.createQuery(hql).setParameter("id", id).uniqueResult();
    if (obj != null) {
      return obj.toString();
    }
    return null;
  }

  /**
   * 查询短地址是否存在
   * 
   * @param shortUrl
   * @return
   */
  public boolean checkShortUrlIsExists(String shortUrl) {
    String hql = "from OpenShortUrlPreproduction t where t.shortUrl=:shortUrl ";
    Object obj = this.createQuery(hql).setParameter("shortUrl", shortUrl).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }
}
