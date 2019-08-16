package com.smate.center.task.dao.pub.seo;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.pub.seo.PubIndexSecondLevel;
import com.smate.center.task.model.search.PubSearchForm;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 首页论文seo Dao服务
 * 
 * @author tsz
 * 
 */

@Repository
public class PubSeoSecondLevelSerachDao extends SnsHibernateDao<PubIndexSecondLevel, Long> {

  /**
   * 根据字母 及第二层级分组 查找成果
   * 
   */
  public void getFirstLevel(String code, Integer num, Page<PubIndexSecondLevel> page) {
    String hql = "from PubIndexSecondLevel t where t.firstLetter=? and t.secondGroup=? order by t.orderMark,t.id";
    String countHql = "select count(t.pubId) ";
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, code, num);
    page.setTotalCount(totalCount);
    // 查询数据实体
    Query queryResult = super.createQuery(hql, code, num);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  /**
   * 通过标题查询首页SEO成果
   * 
   * @param form
   */
  public void findPubByName(PubSearchForm form) {
    String pubName = form.getPubName();
    Integer start = form.getStart();
    Integer maxLoadSize = PubSearchForm.MAX_LOAD_SIZE;
    Long maxSize = Long.valueOf(PubSearchForm.MAX_SIZE);

    String hql =
        "select new PubIndexSecondLevel(pubId,title,dbid) from PubIndexSecondLevel t where t.title like ? and rownum<=? ";
    String orderBy = " order by t.orderMark,t.id";
    String countHql = "select count(t.pubId) from PubIndexSecondLevel t where t.title like ? and rownum<=?)";
    // 记录数
    Long totalCount = super.findUnique(countHql, "%" + pubName + "%", maxSize);
    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderBy, "%" + pubName + "%", maxSize);
    queryResult.setFirstResult(start == 0 ? 0 : start - 1);
    queryResult.setMaxResults(maxLoadSize);
    form.setTotalSize(totalCount.intValue());
    form.setPubList(queryResult.list());
  }

  /**
   * 清空第二层级数据
   */

  public void deletePubIndexSecondLevel(String code) {
    String hql = "delete From PubIndexSecondLevel t where t.firstLetter=? ";
    createQuery(hql, new Object[] {code}).executeUpdate();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void savePubSeoSecondLevel(PubIndexSecondLevel pubIndexSecondLevel) {
    this.saveOrUpdate(pubIndexSecondLevel);
  }


  public void updateFirstLetter(Long pubId, String firstLetter, int titleType) {
    String hql =
        "update PubIndexSecondLevel t set t.FirstLetter =:firstLetter where t.pubId =:pubId and t.id.titleType =:titleType";
    createQuery(hql, pubId, firstLetter, titleType).executeUpdate();
  }

  public void deletePubIndexSecondLevel(String code, int secondGroup) {
    String hql = "delete From PubIndexSecondLevel t where t.firstGroup=:firstGroup and t.secondGroup=:secondGroup ";
    createQuery(hql).setParameter("firstGroup", code).setParameter("secondGroup", secondGroup).executeUpdate();
  }

  public void truncateSecondLevel() {
    Session session = getSession();
    String sql = "truncate table pub_index_second_level";
    session.createSQLQuery(sql).executeUpdate();
  }

}
