package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PrjPubFund;
import com.smate.center.batch.model.rol.pub.PrjPubFundRefresh;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * 项目关联上的成果（成果里面的fundinfo编号关联）.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PrjPubFundDao extends RolHibernateDao<PrjPubFund, Long> {

  /**
   * 获取需要刷新项目关联成果的项目列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PrjPubFundRefresh> loadRefreshList() {

    String hql = "from PrjPubFundRefresh t where t.status = 0 ";
    return super.createQuery(hql).setMaxResults(100).list();
  }

  /**
   * 标记刷新项目关联成果失败.
   * 
   * @param prjId
   */
  public void markRefreshError(Long prjId) {

    String hql = "update PrjPubFundRefresh t set t.status = 9 where t.prjId = ? ";
    super.createQuery(hql, prjId).executeUpdate();
  }

  /**
   * 删除刷新项目关联成果信息.
   * 
   * @param prjId
   */
  public void removeRefresh(Long prjId) {

    String hql = "delete from PrjPubFundRefresh t where t.prjId = ?";
    super.createQuery(hql, prjId).executeUpdate();
  }

  /**
   * 项目信息改变，刷新关联的成果信息.
   * 
   * @param prjId
   * @param insId
   */
  public void addRefresh(Long prjId, Long insId) {

    String hql = "from PrjPubFundRefresh t where t.prjId = ? ";
    PrjPubFundRefresh refresh = super.findUnique(hql, prjId);
    if (refresh == null) {
      refresh = new PrjPubFundRefresh(prjId, insId);
    }
    refresh.setStatus(0);
    super.getSession().save(refresh);
  }

  /**
   * 加载项目关联成果.
   * 
   * @param prjId
   * @param pubId
   * @return
   */
  public PrjPubFund loadPrjPubFund(Long prjId, Long pubId) {

    String hql = "from PrjPubFund t where t.prjId = ? and t.pubId = ? ";
    return super.findUnique(hql, prjId, pubId);
  }

  /**
   * 是否存在项目成果关联信息.
   * 
   * @param prjId
   * @param pubId
   * @return
   */
  public boolean isExistsPrjPubFund(Long prjId, Long pubId) {

    String hql = "select count(id) from PrjPubFund t where t.prjId = ? and t.pubId = ? ";
    Long count = super.findUnique(hql, prjId, pubId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 保存项目成果关联信息.
   * 
   * @param prjId
   * @param pubId
   * @param insId
   */
  public void savePrjPubFund(Long prjId, Long pubId, Long insId) {

    if (this.isExistsPrjPubFund(prjId, pubId)) {
      return;
    }
    PrjPubFund prjPub = new PrjPubFund(prjId, pubId, insId);
    super.save(prjPub);
  }

  /**
   * 加载项目关联的成果信息.
   * 
   * @param prjId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PrjPubFund> loadPrjPub(Long prjId) {

    String hql = "from PrjPubFund t where t.prjId = ? ";
    return super.createQuery(hql, prjId).list();
  }

  /**
   * 加载成果关联的项目信息.
   * 
   * @param prjId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PrjPubFund> loadPubPrj(Long pubId) {

    String hql = "from PrjPubFund t where t.pubId = ? ";
    return super.createQuery(hql, pubId).list();
  }

  /**
   * 删除项目关联的成果信息，排除用户已经删除的.
   * 
   * @param prjId
   */
  public void removePrjPubFund(Long prjId) {

    String hql = "delete from PrjPubFund t where t.prjId = ? and t.status = 1";
    super.createQuery(hql, prjId).executeUpdate();
  }

  /**
   * 删除成果关联的项目信息，排除用户已经删除的.
   * 
   * @param pubId
   */
  public void remvovePubFundPrj(Long pubId) {

    String hql = "delete from PrjPubFund t where t.pubId = ? and t.status = 1";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 用户删除项目成果管理.
   * 
   * @param prjId
   * @param pubId
   * @param insId
   */
  public void removePrjPubByUser(Long prjId, Long pubId, Long insId) {

    String hql = "update PrjPubFund t set t.status = 9 where t.pubId = ? and t.prjId = ? and t.insId = ? ";
    super.createQuery(hql, pubId, prjId, insId).executeUpdate();
  }

  /**
   * 获取项目关联成果个数.
   * 
   * @param prjIds
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> queryPrjPubNum(List<Long> prjIds, Long insId) {

    String hql =
        "select t.prjId,count(t.id) from PrjPubFund t where t.prjId in(:prjIds) and t.insId = :insId and t.status = 1 group by t.prjId";
    return super.createQuery(hql).setParameterList("prjIds", prjIds).setParameter("insId", insId).list();
  }

  /**
   * 获取单位项目关联的成果数据
   * 
   * @param insId
   * @param prjId
   * @return
   */
  public Long getPrjLinkPubs(Long insId, Long prjId) {
    String hql = "select count(id) from PrjPubFund where insId=? and prjId=? and status=?";
    return findUnique(hql, insId, prjId, 1);
  }

  public Long getPrjLinkPubs(Long prjId) {
    String hql = "select count(id) from PrjPubFund where prjId=? and status=?";
    return findUnique(hql, prjId, 1);
  }

  public Page getPrjRelatedPubIdsPage(Page page, Long prjId) {
    String listHql = "select t.pubId ";
    String countHql = "select count(t.id) ";
    String orderHql = " order by t.id desc";
    StringBuilder hql = new StringBuilder();
    hql.append(" from PrjPubFund t where t.prjId=? ");
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, prjId);
    page.setTotalCount(totalCount);

    // 数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, prjId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }
}
