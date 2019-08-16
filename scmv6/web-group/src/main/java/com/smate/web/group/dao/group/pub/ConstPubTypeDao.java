package com.smate.web.group.dao.group.pub;

import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.pub.ConstPubType;

@Repository
public class ConstPubTypeDao extends SnsHibernateDao<ConstPubType, Integer> {

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return List<PublicationType>
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstPubType> getAllTypes(Locale locale) {

    Query query = super.createQuery("from ConstPubType t where enabled=1 order by t.seqNo");

    query.setCacheable(true);
    List<ConstPubType> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstPubType cr : list) {

        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return query.list();

  }

}
