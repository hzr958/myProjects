package com.smate.web.fund.agency.dao;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.fund.agency.model.FundAgencyInterest;

/**
 * 关注资助机构DAO
 * 
 * @author wsn
 * @date Nov 13, 2018
 */
@Repository
public class FundAgencyInterestDao extends SnsHibernateDao<FundAgencyInterest, Long> {
  /**
   * 查询个人关注的资助机构
   * 
   * @param psnId
   * @return
   */
  public List<FundAgencyInterest> fundAgencyInteresByPsnId(Long psnId) {
    String updateHql = "update  FundAgencyInterest t set t.status=0 where t.psnId=:psnId and t.agencyId in("
        + "select a.agencyId from FundAgencyInterest a where "
        + "not exists(select 1 from ConstFundAgency b where a.agencyId=b.id)"
        + "or exists(select 1 from ConstFundAgency b where a.agencyId=b.id and b.status=1)" + "and a.psnId=:psnId"
        + ")";
    super.createQuery(updateHql).setParameter("psnId", psnId).executeUpdate();// 更新删除了的资助机构
    String hql = "from FundAgencyInterest where psnId=:psnId and status=1 order by agencyOrder,id desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 关注的资助机构状态设为0（取消关注）
   * 
   * @param psnId
   * @param agencyId
   */
  public void deleteFundAgencyInteresByPsnIdAndAgencyId(Long psnId, Long agencyId) {
    String hql =
        "update FundAgencyInterest t set t.status=0,t.updateDate=:updateDate where t.psnId=:psnId and t.agencyId=:agencyId";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("agencyId", agencyId)
        .setParameter("updateDate", new Date()).executeUpdate();
  }

  /**
   * 个人关注的资助机构状态全部设为0（取消关注）
   * 
   * @param psnId
   * @param agencyId
   */
  public void deletePsnAllFundAgencyInteresByPsnId(Long psnId) {
    String hql = "update FundAgencyInterest t set t.status=0, t.agencyOrder=0 where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 关注的资助机构状态设为0（取消关注）
   * 
   * @param psnId
   * @param agencyId
   */
  public FundAgencyInterest getFundAgencyInteresByPsnIdAndAgencyId(Long psnId, Long agencyId) {
    String hql = "from FundAgencyInterest t where t.psnId=:psnId and t.agencyId=:agencyId";
    return (FundAgencyInterest) super.createQuery(hql).setParameter("psnId", psnId).setParameter("agencyId", agencyId)
        .uniqueResult();
  }

  /**
   * 查找个人关注的资助机构的数量
   * 
   * @param psnId
   * @return
   */
  public Integer getPsnFundAgencyInteresNum(Long psnId) {
    String hql = "select count(1) from FundAgencyInterest where psnId=:psnId and status=1";
    return NumberUtils.toInt(Objects.toString(super.createQuery(hql).setParameter("psnId", psnId).uniqueResult()));
  }

  /**
   * 对应的人员是否已关注10个或更多的资助机构
   * 
   * @param psnId
   * @return
   */
  public boolean hasTenInterestAgencies(Long psnId) {
    String hql = "select count(1) from FundAgencyInterest t where t.psnId = :psnId and t.status = 1";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return count >= 10;
  }

  /**
   * 查询人员关注某个资助机构记录
   * 
   * @param psnId
   * @param agencyId
   * @return
   */
  public FundAgencyInterest findInterestAgencyByPsnIdAndAgencyId(Long psnId, Long agencyId) {
    String hql = " from FundAgencyInterest t where t.psnId = :psnId and t.agencyId = :agencyId";
    return (FundAgencyInterest) super.createQuery(hql).setParameter("psnId", psnId).setParameter("agencyId", agencyId)
        .uniqueResult();
  }

  /**
   * 获取已经赞过的资助机构ID
   * 
   * @param psnId
   * @param agencyIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findAllInterestAgencyIds(Long psnId) {
    String hql = "select t.agencyId from FundAgencyInterest t where t.psnId = :psnId and t.status = 1";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 是否已关注某资助机构
   * 
   * @param psnId
   * @param agencyId
   * @return
   */
  public boolean hasInterestedAgency(Long psnId, Long agencyId) {
    String hql = " from FundAgencyInterest t where t.psnId = :psnId and t.agencyId = :agencyId and t.status = 1";
    FundAgencyInterest ai = (FundAgencyInterest) super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("agencyId", agencyId).uniqueResult();
    if (ai != null) {
      return true;
    }
    return false;
  }
}
