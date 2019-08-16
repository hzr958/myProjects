package com.smate.center.task.dao.sns.quartz;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.KeywordsDic;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关键词字典，用于拆分成果标题、摘要等使用.
 * 
 * @author lqh
 * 
 */
@Repository
public class KeywordsDicDao extends SnsHibernateDao<KeywordsDic, Long> {

  /**
   * 根据特征hash获取关键词列表.
   * 
   * @param fhashs
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KeywordsDic> loadKwListByFturesHash(Collection<Long> fhashs) {

    String hql = "from KeywordsDic t where t.fturesHash in (:fhashs)";
    return super.createQuery(hql).setParameterList("fhashs", fhashs).list();
  }


  /**
   * 批量获取type=2的kwtxt
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  public List<String> findKeywordTextByTwoType(Integer pageNo, Integer pageSize) {
    String hql = "select kd.kwtxt from KeywordsDic kd where kd.type =2 order by kd.id asc";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  public List<KeywordsDic> findKeywordText(Long lastId, Integer size) {
    String hql = "from KeywordsDic t where t.id >:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(size).list();
  }

  /**
   * 获取检索Suggest
   * 
   */
  @SuppressWarnings("unchecked")
  public List<BigDecimal> getSuggestStrToHandleList(Integer type) {
    String sql = "select t.suggest_id from suggest_str_init t where t.status = 0 and t.suggest_type =:type";
    return this.getSession().createSQLQuery(sql).setParameter("type", type).setMaxResults(2000).list();
  }

  public void updateSuggestStrStatus(Long suggestId, Integer type, Integer status) {
    String sql =
        "update suggest_str_init t set t.status =:status where t.suggest_id =:suggestId and t.suggest_type =:type";
    this.getSession().createSQLQuery(sql).setParameter("status", status).setParameter("suggestId", suggestId)
        .setParameter("type", type).executeUpdate();
  }

}
