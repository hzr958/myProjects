package com.smate.sie.center.task.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportInsDataSource;

/**
 * 单位数据来源常量表Dao
 * 
 * @author hd
 *
 */
@Repository
public class ImportInsDataSourceDao extends SieHibernateDao<ImportInsDataSource, String> {

  @SuppressWarnings("unchecked")
  public String findTokenBySoureceName(String sourName) throws DaoException {
    String hql = "from ImportInsDataSource where sourName =:sourName";
    Query queryResult = super.createQuery(hql).setParameter("sourName", sourName.trim());
    List<ImportInsDataSource> list = queryResult.list();
    String result = null;
    if (list.size() > 0) {
      result = list.get(0).getToken();
    }
    return result;
  }

}
