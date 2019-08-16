package com.smate.center.task.dao.fund.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundTopic;
import com.smate.core.base.utils.data.RcmdHibernateDao;


/**
 * 基金专题数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class ConstFundTopicDao extends RcmdHibernateDao<ConstFundTopic, Long> {

  /**
   * 获取基金的专题记录.
   * 
   * @param fundId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundTopic> getFundTopicList(Long fundId) {
    String hql = "from ConstFundTopic t where t.fundId=? ";
    return super.createQuery(hql, fundId).list();
  }

  /**
   * 获取基金的专题关键词列表.
   * 
   * @param fundId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getFundTopicKeyList(Long fundId) {
    String hql = "select keyword from ConstFundTopic t where t.fundId=? ";
    return super.createQuery(hql, fundId).list();
  }
}
