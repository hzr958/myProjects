package com.smate.web.v8pub.dao.seo;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.seo.PubIndexFirstLevel;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 首页论文seo Dao服务
 * 
 * @author tsz
 *
 */

@Repository
public class PubSeoFristLevelSerachDao extends SnsHibernateDao<PubIndexFirstLevel, Long> {

  /**
   * 根据字母查第一层级分组
   */
  public List<PubIndexFirstLevel> getFirstLevel(String code) {
    if ("other".equals(code)) {
      code = "0";
    }
    String hql = "From PubIndexFirstLevel t where t.firstGroup=? order by t.secondGroup";
    return super.createQuery(hql, new Object[] {code}).list();
  }

  /**
   * 根据标签 模糊查询
   */
  public List<PubIndexFirstLevel> getFirstLevelByLabel(String code) {
    String hql = "From PubIndexFirstLevel t where t.firstLabel like ? order by t.secondGroup";
    return super.createQuery(hql, new Object[] {"%" + code + "%"}).list();
  }

  /**
   * 清空第一层级数据
   */

  public void deletePubIndexFirstLevel(String code, Integer flag) {
    String hql = "delete From PubIndexFirstLevel t where t.firstGroup=? and t.secondGroup=?";
    super.createQuery(hql, new Object[] {code, flag}).executeUpdate();
  }

  public void deletePubIndexFirstLevel(String code) {
    String hql = "delete From PubIndexFirstLevel t where t.firstGroup=?";
    super.createQuery(hql, new Object[] {code}).executeUpdate();
  }

  /**
   * 根据second_group获取first
   */
  public PubIndexFirstLevel findPubBySecondGroup(Long secondGroup) {
    String hql = "From PubIndexFirstLevel t where  t.secondGroup = " + secondGroup;
    List<PubIndexFirstLevel> list = super.createQuery(hql).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public void savePubSeoFristLevel(PubIndexFirstLevel pubIndexFirstLevel) {
    StatelessSession statelessSession = getSessionFactory().openStatelessSession();
    Transaction tx = statelessSession.beginTransaction();
    statelessSession.insert(pubIndexFirstLevel);
    tx.commit();
    statelessSession.close();
  }
}
