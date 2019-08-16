package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubIndexUrl;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubIndexUrlDao extends SnsHibernateDao<PubIndexUrl, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getNeedInitPubId(Integer index, Integer batchSize) {
    String hql = "select t.pubId from PubIndexUrl t where t.pubIndexUrl is null";
    return this.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getLongNeedInitPubId(Integer index, Integer batchSize) {
    String hql = "select t.pubId from PubIndexUrl t where t.pubLongIndexUrl is null";
    return this.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();
  }

  public String getStringUrlByPubId(Long pubId) {
    String hql = "select t.pubIndexUrl from PubIndexUrl t where t.pubId =:pubId";
    return (String) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public Long getCount() {
    String hql = "select count(pubId) from PubIndexUrl";
    return (Long) this.createQuery(hql).uniqueResult();
  }

  /**
   * 判断是否需要插入数据
   * 
   * @return
   */
  public boolean isNeedInsertData() {

    String sql = "select  count(1) from Publication t1 where t1.id not in(select t.pubId from PubIndexUrl t)";
    Long count = (Long) super.createQuery(sql).uniqueResult();
    if (count > 0L) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * 插入需要 初始化的个人成果短地址数据，publication有pubIndexUrl中没有的
   */
  public void insertInitData() {
    String sql =
        "insert into V_PUB_INDEX_URL t (select t1.pub_id,NULL,NULL,SYSDATE,NULL from publication t1 where t1.pub_id not in(select t.pub_id from V_PUB_INDEX_URL t))";
    super.update(sql);
  }

}
