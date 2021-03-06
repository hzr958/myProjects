package com.smate.sie.center.task.dao;

import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.PubDaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieConstPubType;

/**
 * 成果类型常数Dao.
 * 
 * @author jszhou
 *
 */
@Repository
public class SieConstPubTypeDao extends SieHibernateDao<SieConstPubType, Integer> {

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return List<SieConstPubType>
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public List<SieConstPubType> getAllTypes(Locale locale) throws PubDaoException {

    Query query = super.createQuery("from SieConstPubType t where enabled=1 order by t.seqNo");

    query.setCacheable(true);
    List<SieConstPubType> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (SieConstPubType cr : list) {

        if ("zh".equals(locale.getLanguage())) {
          cr.setZhName(cr.getZhName());
        } else {
          cr.setEnName(cr.getEnName());
        }
      }
    }
    return query.list();

  }
}
