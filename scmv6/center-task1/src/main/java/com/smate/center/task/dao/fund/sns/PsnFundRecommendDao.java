package com.smate.center.task.dao.fund.sns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.sns.PsnFundRecommend;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 人员推荐基金的结果记录数据库操作类.
 * 
 * @author liangguokeng
 * 
 */
@Repository
public class PsnFundRecommendDao extends SnsHibernateDao<PsnFundRecommend, Long> {



  @SuppressWarnings("unchecked")
  public List<PsnFundRecommend> getPsnFundRecommendList(Long psnId, List<Long> fundIds) {
    String hql =
        "from PsnFundRecommend t where t.psnId= :psnId  and t.fundId in (:fundIds) and t.isSendMail =0  order by t.recommendation desc,t.fundId asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("fundIds", fundIds).list();
  }

  /**
   * 页面获取当前人员的推荐基金ID.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnReFundIdList(Long psnId) {
    String hql = "select fundId from PsnFundRecommend t where t.psnId=?";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 获取人员的推荐基金.
   * 
   * @param psnId
   * @param fundId
   * @return
   */
  public PsnFundRecommend findPsnFundRecommend(Long fundId, Long psnId) {
    String hql = "from PsnFundRecommend t where t.psnId = :psnId and t.fundId = :fundId";
    return (PsnFundRecommend) super.createQuery(hql).setParameter("psnId", psnId).setParameter("fundId", fundId)
        .uniqueResult();
  }


  /**
   * 获取有推荐基金的人员ID列表<发送推荐基金的推广邮件处用到此方法>.
   * 
   * @param startPsnId
   * @param timeLimit 时间限制(小时数)<以当前时间为准往前推算>.
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnFundRePsnId(Long startPsnId, Long timeLimit, int maxSize) {
    String hql = "select distinct p.psnId from PsnFundRecommend p where p.psnId>? and p.creatDate>=? order by p.psnId";
    // 计算限制的时间(以当前时间往前推算起始计算时间).
    Date date = new Date(new Date().getTime() - timeLimit * 60 * 60 * 1000);
    return super.createQuery(hql, startPsnId, date).setMaxResults(maxSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdsByFund(List<Long> fundIds) {
    String sql =
        "Select distinct(t.psn_id) ,f.psn_fund_score FROM psn_fund_recommend  t Left Join user_union_login_log f On t.psn_id=f.psn_id Where t.is_send_mail=0 and t.fund_id in (:fundId)  Order By f.psn_fund_score Desc nulls Last ,t.psn_id";
    List<Object[]> objList =
        super.getSession().createSQLQuery(sql).setParameterList("fundId", fundIds).setMaxResults(1000).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Long> psnIdList = new ArrayList<Long>();
    for (Object[] objects : objList) {
      psnIdList.add(Long.valueOf(String.valueOf(objects[0])));
    }
    return psnIdList;
  }


  public void deletePsnFundRecommend(Long fundId, Date updateDate) {
    String hql = "delete PsnFundRecommend t where t.fundId = :fundId and t.creatDate < :updateDate ";
    super.createQuery(hql).setParameter("fundId", fundId).setParameter("updateDate", updateDate).executeUpdate();


  }


  public void updateSendmailStatus(Long psnId, List<Long> fundIds) {
    String hql = "update PsnFundRecommend t set t.isSendMail=1 where t.psnId= :psnId  and t.fundId in (:fundIds) ";
    super.createQuery(hql).setParameter("psnId", psnId).setParameterList("fundIds", fundIds).executeUpdate();
  }

  public void deletePsnFundRecommend(Long psnId) {
    String hql = "delete PsnFundRecommend t where t.psnId = :psnId ";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
