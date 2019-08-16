package com.smate.web.fund.recommend.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.fund.recommend.model.MyFund;

/**
 * 我的（收藏的）基金DAO
 * 
 * @author WSN
 *
 *         2017年8月18日 上午10:56:56
 *
 */
@Repository
public class MyFundDao extends SnsHibernateDao<MyFund, Long> {

  /**
   * 分页查询我的基金
   * 
   * @param form
   * @return
   */
  public Page<Long> searchMyFund(FundRecommendForm form) {
    Page<Long> page = form.getPage();
    if (page == null) {
      page = new Page<Long>();
    }
    String listHql = "select f.fundId";
    String countHql = "select count(*) ";
    String queryHql =
        " from ConstFundCategory c, MyFund f, ConstFundAgency a where f.fundId = c.id and c.agencyId = a.id and f.psnId = :psnId and c.insId=0";
    String orderBy = " order by f.collectTime desc";
    Long count = (Long) super.createQuery(countHql + queryHql).setParameter("psnId", form.getPsnId()).uniqueResult();
    page.setTotalCount(count);
    page.setTotalPages(count / page.getPageSize() + 1);
    List<Long> result = super.createQuery(listHql + queryHql + orderBy).setParameter("psnId", form.getPsnId())
        .setMaxResults(page.getPageSize()).setFirstResult(page.getFirst() - 1).list();
    page.setResult(result);
    form.setPage(page);
    return page;
  }

  /**
   * 查找是否收藏了某个基金
   * 
   * @param psnId
   * @param fundId
   * @return
   */
  public MyFund findMyFund(Long psnId, Long fundId) {
    String hql = " from MyFund t where t.fundId = :fundId and t.psnId = :psnId";
    return (MyFund) super.createQuery(hql).setParameter("fundId", fundId).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查询已收藏的所有基金ID
   * 
   * @param psnId
   * @return
   */
  public List<Long> findCollectFundIds(Long psnId) {
    String hql = "select t.fundId from MyFund t where t.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 找出已收藏的基金ID
   * 
   * @param fundIds
   * @param psnId
   * @return
   */
  public List<Long> findCollectFundIds(List<Long> fundIds, Long psnId) {
    String hql = "select t.fundId from MyFund t where t.psnId = :psnId and t.fundId in (:fundIds)";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("fundIds", fundIds).list();
  }

  /**
   * @Author LIJUN
   * @Description //检查基金数据是否完整
   * @Date 15:49 2018/7/9
   * @Param [fundId]
   * @return boolean ture 表示完整
   **/
  public boolean checkFundInfo(Long fundId) {

    String hql =
        " select count(1) from ConstFundAgency t where t.id=(select a.agencyId from ConstFundCategory a where a.id =:fundId)";

    Long count = (Long) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();

    if (count != null && count > 0) {
      return true;
    } else {
      return false;
    }
  }
}
