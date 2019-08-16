package com.smate.web.prj.dao.project.search;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.search.ConstRefDb;

/**
 * 文献数据库定义表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstRefDbDao extends SnsHibernateDao<ConstRefDb, Long> {


  /**
   * .根据文献库类型查询出所有的公用文献库
   * 
   * @param dbType
   * @param locale
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstRefDb> getAllPublicRefDBByDBType(String dbType, Locale locale) {
    String hql = " from ConstRefDb t where t.isPublic = 1 and t.dbType like :dbType  order by " + locale.getLanguage()
        + "SortKey asc";
    dbType = StringUtils.isBlank(dbType) ? "" : dbType;
    return super.createQuery(hql).setParameter("dbType", "%" + dbType + "%").list();
  }

  /**
   * 获取单个指定DbCode的数据.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstRefDb getConstRefDbByCode(String dbCode) throws DAOException {
    String hql = "from ConstRefDb where upper(code) = :dbCode";
    return (ConstRefDb) super.createQuery(hql).setParameter("dbCode", dbCode.toUpperCase()).uniqueResult();
  }

}
