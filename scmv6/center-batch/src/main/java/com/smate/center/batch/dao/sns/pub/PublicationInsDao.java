package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PublicationIns;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果作者单位DAO:查找单位.
 * 
 * @author yamingd
 */
@Repository
public class PublicationInsDao extends SnsHibernateDao<PublicationIns, Long> {

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.publication.dao.PublicationInsService#lookUpByName(java. lang.String)
   */
  @SuppressWarnings("unchecked")
  public PublicationIns lookUpByName(String name) throws DaoException {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    name = name.trim().toLowerCase();
    if (name.length() > 300) {
      name = name.substring(0, 300);
    }
    String hql = "from PublicationIns t where lower(t.zhName)=? or lower(t.enName)=? order by t.enabled desc";
    Query query = super.createQuery(hql, new Object[] {name, name});
    List<PublicationIns> list = query.list();
    return CollectionUtils.isEmpty(list) ? null : list.get(0);
  }
}
