package com.smate.web.prj.dao.project;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.PrjSchemeAgency;

/**
 * 项目资助机构.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PrjSchemeAgencyDao extends SnsHibernateDao<PrjSchemeAgency, Long> {

  /**
   * 查询指定智能匹配前 N 条数据.
   * 
   * @param 检索的字符串
   * @param 提示10或者20个内容
   * @return 检索结果列表
   */
  @SuppressWarnings("unchecked")
  public List<PrjSchemeAgency> searchPrjSchemeAgencies(String nameKeyword, int maxSize) {
    String hql = "from PrjSchemeAgency where name like ? or enName like ? order by code ";
    Query query = super.createQuery(hql, "%" + nameKeyword + "%", "%" + nameKeyword + "%");
    query.setMaxResults(maxSize);
    return query.list();
  }

  /**
   * 通过名字查找项目资助机构.
   * 
   * @param name
   * @return
   */
  public PrjSchemeAgency findByName(String name) {
    String hql = "from PrjSchemeAgency where name = ? or enName = ? ";
    return super.findUnique(hql, name, name);
  }
}
