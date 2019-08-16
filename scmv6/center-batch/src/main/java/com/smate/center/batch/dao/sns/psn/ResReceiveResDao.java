package com.smate.center.batch.dao.sns.psn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.PsnResReceiveRes;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * 接收的资源Dao.
 * 
 * cwli
 */
@Repository
public class ResReceiveResDao extends SnsHibernateDao<PsnResReceiveRes, Long> implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4424306815872748390L;

  /**
   * 保存接收推荐-资源关系.
   * 
   * @param psnResReceiveRes
   * @return
   * @throws DaoException
   */
  public Long savePsnResReceiveRes(PsnResReceiveRes psnResReceiveRes) throws DaoException {
    return (Long) super.getSession().save(psnResReceiveRes);
  }

  public void deleteCommendPublication(Long pubId) {
    String hql = "update PsnResReceiveRes t set t.status=2 where t.resId=?";
    super.createQuery(hql, new Object[] {pubId}).executeUpdate();
  }

  /**
   * 确认成果.
   * 
   * @param pubId
   */
  public void updateCommendPublications(Long pubId) {
    String hql = "update PsnResReceiveRes t set t.status=1  where t.resId=?";
    super.createQuery(hql, new Object[] {pubId}).executeUpdate();
  }

  /**
   * 忽略推荐.
   * 
   * @param receiveId
   * @throws DaoException
   */
  public void cancelCommend(Long receiveId, Long resId) throws DaoException {
    String hql = "update PsnResReceiveRes t set t.status=2 where t.resRecId=? and t.resId=?";
    super.createQuery(hql, new Object[] {receiveId, resId}).executeUpdate();
  }

  /**
   * 修改资源状态.
   * 
   * @param receiveId
   * @param resId
   * @throws DaoException
   */
  public void updateResStatus(Long resId, int status) throws DaoException {
    String hql = "update PsnResReceiveRes t1 set t1.status=? where t1.resId=?"
        + " and exists (select 1 from PsnResReceive t2 where t1.resRecId=t2.resRecId and t2.resType=3)";
    super.createQuery(hql, new Object[] {status, resId}).executeUpdate();
  }

  /**
   * 确认推荐.
   * 
   * @param receiveId
   * @throws DaoException
   */
  public void confirmCommend(Long receiveId, Long resId) throws DaoException {
    String hql = "update PsnResReceiveRes t set t.status=1 where t.resRecId=? and t.resId=?";
    super.createQuery(hql, new Object[] {receiveId, resId}).executeUpdate();
  }

  /**
   * 查找接收资源.
   * 
   * @param receiveId
   * @return
   * @throws DaoException
   */
  public List<PsnResReceiveRes> findReceiveRess(Long receiveId) throws DaoException {
    String hql = "from PsnResReceiveRes t where t.resRecId=?";
    return super.find(hql, receiveId);
  }

  public void delReceiveRess(Long receiveId) throws DaoException {
    String hql = "delete from PsnResReceiveRes t where t.resRecId=?";
    super.createQuery(hql, receiveId).executeUpdate();
  }

  /**
   * 查找接收资源总数.
   * 
   * @param receiveId
   * @return
   * @throws DaoException
   */
  public Long getReceiveResTotal(Long receiveId) throws DaoException {
    String hql = "select count(*) from PsnResReceiveRes t where t.resRecId=?";
    return super.findUnique(hql, new Object[] {receiveId});
  }

  /**
   * 查找接收详细信息.
   * 
   * @param sendPsnId
   * @param type
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page findReceiveDetails(List<Long> resRecIds, Page page) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("from PsnResReceiveRes t1 where t1.resRecId in(");
    for (int i = 0; i < resRecIds.size() - 1; i++) {
      hql.append(resRecIds.get(i) + ",");
    }
    hql.append(resRecIds.get(resRecIds.size() - 1) + ") order by t1.id desc");

    // 查询总页数
    Query queryCt = super.getSession().createQuery("select count(t1.id) " + hql.toString());
    Long count = (Long) queryCt.uniqueResult();
    page.setTotalCount(count.intValue());

    // 查询数据实体
    Query queryResult = super.getSession().createQuery(hql.toString());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  /**
   * 找出资源的原信息
   * 
   * @param resId
   * @return
   * @throws DaoException
   */
  public PsnResReceiveRes findByResId(Long resId) throws DaoException {
    String hql = "from PsnResReceiveRes t where t.resId = ?";
    List<PsnResReceiveRes> result = super.createQuery(hql, resId).list();
    if (CollectionUtils.isNotEmpty(result)) {
      return result.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Page<PsnResReceiveRes> queryPsnResReceiveResByPage(Long resRecId, int pageNo, int pageSize)
      throws DaoException {
    String hql = "from PsnResReceiveRes t where t.resRecId=?";
    Page<PsnResReceiveRes> page = new Page<PsnResReceiveRes>();
    page.setPageNo(pageNo);
    page.setPageSize(pageSize);

    Long count = (Long) super.createQuery("select count(t.id) " + hql, new Object[] {resRecId}).uniqueResult();
    page.setTotalCount(count);

    List<PsnResReceiveRes> list = super.createQuery(hql + " order by t.id", new Object[] {resRecId})
        .setFirstResult(page.getFirst() - 1).setMaxResults(Integer.valueOf(count.toString())).list();
    page.setResult(list);

    return page;
  }

  @SuppressWarnings("unchecked")
  public List<PsnResReceiveRes> queryPsnResReceiveRes(List<Long> resIdList, Long resRecId) throws DaoException {
    return super.createQuery("from PsnResReceiveRes t where t.resRecId=:resRecId and t.resId in(:resIdList)")
        .setParameter("resRecId", resRecId).setParameterList("resIdList", resIdList).list();
  }

  public Long queryShareResCountByPsnAndType(Long receiverId, Integer[] resType) throws DaoException {
    return (Long) super.createQuery(
        "select count(t2.id) from PsnResReceive t1, PsnResReceiveRes t2 where t1.psnId=:psnId and t1.status=:status and (t1.deadline is null or t1.deadline>:deadline) and t1.resRecId=t2.resRecId and t2.status=:isImported and t2.resType in(:resType)")
            .setParameter("psnId", receiverId).setParameter("status", 0).setParameter("deadline", new Date())
            .setParameter("isImported", 0).setParameterList("resType", resType).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public Page<PsnResReceiveRes> queryPsnResReceiveResByPage(Long receiverId, Integer[] resType, int pageNo,
      int pageSize) throws DaoException {
    Page<PsnResReceiveRes> page = new Page<PsnResReceiveRes>(pageSize);
    page.setPageNo(pageNo);

    StringBuilder hql = new StringBuilder();
    hql.append(
        "from PsnResReceive t1, PsnResReceiveRes t2 where t1.psnId=:psnId and t1.status=:status and (t1.deadline is null or t1.deadline>:deadline) and t1.resRecId=t2.resRecId and t2.status=:isImported and t2.resType in(:resType)");
    Long count = (Long) super.createQuery("select count(t2.id) " + hql).setParameter("psnId", receiverId)
        .setParameter("status", 0).setParameter("deadline", new Date()).setParameter("isImported", 0)
        .setParameterList("resType", resType).uniqueResult();
    page.setTotalCount(count);

    List<PsnResReceiveRes> list = super.createQuery(
        "select new PsnResReceiveRes(t2.id, t2.resId, t2.resType, t2.resNodeId, t2.dbid, t2.resRecId, t2.status, t1.sendPsnId, t1.createDate) "
            + hql + " order by t1.createDate desc, t2.id desc").setParameter("psnId", receiverId)
                .setParameter("status", 0).setParameter("deadline", new Date()).setParameter("isImported", 0)
                .setParameterList("resType", resType).setFirstResult(page.getFirst() - 1).setMaxResults(pageSize)
                .list();
    page.setResult(list);

    return page;
  }

  @SuppressWarnings("unchecked")
  public PsnResReceiveRes queryPsnResReceiveRes(Long resId, Long resRecId) throws DaoException {
    List<PsnResReceiveRes> list =
        super.createQuery("from PsnResReceiveRes t where t.resRecId=:resRecId and t.resId=:resId")
            .setParameter("resRecId", resRecId).setParameter("resId", resId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<PsnResReceiveRes> queryPsnResReceiveResByResRecId(Long resRecId) throws DaoException {
    return super.createQuery("from PsnResReceiveRes t where t.resRecId=?", resRecId).list();
  }

  @SuppressWarnings("unchecked")
  public Integer queryIsOwnerShare(Long resId, Long resRecId) throws DaoException {
    List<Integer> list = super.createQuery(
        "select t2.isOwnerShare from PsnResReceive t1, PsnResReceiveRes t2 where t1.resRecId=? and t1.status=0 and t2.resId=? and t1.resRecId=t2.resRecId",
        new Object[] {resRecId, resId}).setMaxResults(1).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  public Long isNotImpToPdwh(Long resRecId, Long resId) throws DaoException {
    String hql = "select count(*) from PsnResReceiveRes t where t.status=0 and t.resRecId=? and t.resId=?";
    return findUnique(hql, resRecId, resId);
  }
}
