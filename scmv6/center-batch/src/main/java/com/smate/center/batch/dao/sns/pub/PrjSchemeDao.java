package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.AcPrjScheme;
import com.smate.center.batch.model.sns.pub.PrjScheme;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;


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
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcPrjScheme> getAcPrjSchemeAgency(String startStr, Long agencyId, int size) {

    String hql = "from AcPrjScheme where ( name like ?  or enName like ? ) and agencyId = ? order by orderCode ";
    Query query = super.createQuery(hql, "%" + startStr + "%", "%" + startStr + "%", agencyId);
    query.setMaxResults(size);
    return query.list();
  }

  /**
   * 通过名字查找项目资助类别.
   * 
   * @param name
   * @return
   */
  public PrjScheme findByName(String name, Long agencyId) {
    String hql = "from PrjScheme where (name = ? or enName = ? ) and agencyId = ? ";
    return super.findUnique(hql, name, name, agencyId);
  }

  public PrjScheme findByName(String name) {
    String hql = "from PrjScheme where (name = ? or enName = ? )";
    return super.findUnique(hql, name, name);
  }
}
