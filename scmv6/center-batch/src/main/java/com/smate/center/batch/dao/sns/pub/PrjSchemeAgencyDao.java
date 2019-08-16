package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.AcPrjSchemeAgency;
import com.smate.center.batch.model.sns.pub.PrjSchemeAgency;
import com.smate.core.base.utils.data.SnsHibernateDao;


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
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcPrjSchemeAgency> getAcPrjSchemeAgency(String startStr, int size) {

    String hql = "from AcPrjSchemeAgency where name like ? or enName like ? order by orderCode ";
    Query query = super.createQuery(hql, "%" + startStr + "%", "%" + startStr + "%");
    query.setMaxResults(size);
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
