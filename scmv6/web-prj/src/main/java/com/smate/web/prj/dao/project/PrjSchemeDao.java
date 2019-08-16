package com.smate.web.prj.dao.project;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.PrjScheme;

/**
 * 项目资助类别.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PrjSchemeDao extends SnsHibernateDao<PrjScheme, Long> {

  /**
   * 查询指定智能匹配前 N 条数据.
   * 
   * @param 检索的字符串
   * @param 提示10或者20个内容
   * @return 检索结果列表
   */
  @SuppressWarnings("unchecked")
  public List<PrjScheme> searchPrjSchemeAgencies(String nameKeyword, Long agencyId, int maxSize) {
    String hql = "from PrjScheme where ( name like ?  or enName like ? ) and agencyId = ? order by code ";
    Query query = super.createQuery(hql, "%" + nameKeyword + "%", "%" + nameKeyword + "%", agencyId);
    query.setMaxResults(maxSize);
    return query.list();
  }

  public PrjScheme find(String name, Long agencyId) {
    String hql = "from PrjScheme where (name = ? or enName = ? ) and agencyId = ? ";
    return super.findUnique(hql, name, name, agencyId);
  }

  /**
   * 通过名字查找项目资助类别.
   * 
   * @param name
   * @return
   */
  public PrjScheme findByName(String name) {
    String hql = "from PrjScheme where (name = ? or enName = ? )";
    return super.findUnique(hql, name, name);
  }
}
