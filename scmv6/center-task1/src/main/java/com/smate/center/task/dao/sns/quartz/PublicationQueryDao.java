package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author yamingd 个人成果、文献管理查询专用。
 */
@Repository
public class PublicationQueryDao extends SnsHibernateDao<Publication, Long> {
  /**
   * 得到来自ISI的个人成果.
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Integer queryPubsCiteTimesByPsnId(Long psnId) {
    String hql =
        "select sum(t.citedTimes) from Publication t where t.articleType=1 and t.status = 0 and  t.psnId = :psnId";
    List list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isEmpty(list) || list.get(0) == null) {
      return 0;
    }
    return Integer.parseInt(String.valueOf(list.get(0)));
  }

  /**
   * 得到来自ISI的个人成果.
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Publication> queryPubsByPsnId(Long psnId) {
    String hql =
        "select new Publication(t.citedTimes,t.id,t.zhTitleHash) from Publication t where t.articleType=1 and t.status = 0 and  t.psnId = :psnId order by nvl(t.citedTimes,-9999999) desc,t.id";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }


  /**
   * 统计成果数.
   * 
   * @param psnId
   * @return
   */
  public Long countPubByPsnId(Long psnId, Integer pubType) {
    String hql = "select count(t.id) from Publication t where t.psnId=? and t.articleType=? and t.status=?";
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    params.add(1);
    params.add(0);
    if (pubType != null) {
      hql += " and t.typeId=?";
      params.add(pubType);
    }
    return super.countHqlResult(hql, params.toArray());
  }

  @SuppressWarnings("rawtypes")
  public List<Map> getMyPubYearGroup(Long currentUserId, int articleType) {
    String hql =
        "select new Map(t.publishYear as year,count(t.id) as count) from Publication t where t.psnId=? and t.articleType=? and t.status=0 and t.publishYear is not null group by t.publishYear order by publishYear desc";
    return super.find(hql, new Object[] {currentUserId, articleType});
  }

  /**
   * 查找成果，基金委成果在线
   * 
   * @param psnId
   * @param articleType
   * @param pubIds
   * @return
   * @throws DaoException
   */
  public List<Publication> findPubsForNsfc(List<Long> pubIds) throws DaoException {
    List params = new ArrayList();
    StringBuilder hql = new StringBuilder("select t");
    hql.append(" from Publication t");
    hql.append(" where 1=1 ");
    if (CollectionUtils.isNotEmpty(pubIds)) {
      String pubs = "";
      for (Long pubId : pubIds) {
        if ("".equals(pubs)) {
          pubs = String.valueOf(pubId);
        } else {
          pubs += "," + String.valueOf(pubId);
        }
      }
      hql.append(" and t.id in (" + pubs + ")");
    }
    hql.append(" order by nvl(t.publishYear,0) desc, nvl(t.publishMonth,0) desc, nvl(t.publishDay,0) desc, t.id ");

    // 查询数据实体
    return super.createQuery(hql.toString(), params.toArray()).list();

  }

  /**
   * @param psnId 记录所有人
   * @param articleType 1为成果,2为文献
   * @param form TODO
   * @param query 查询条件
   * @param page 分页信息
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Publication> searchPublication(Long psnId, Integer articleType, List<Long> pubIds) throws DaoException {
    List params = new ArrayList();
    StringBuilder hql = new StringBuilder("select t");
    hql.append(" from Publication t");
    hql.append(" where 1=1 ");
    if (psnId != null) {
      hql.append(" and t.psnId=? and t.status=0");
      params.add(psnId);
    }
    if (articleType != null) {
      hql.append(" and t.articleType=?");
      params.add(articleType);
    }

    if (CollectionUtils.isNotEmpty(pubIds)) {
      String pubs = "";
      for (Long pubId : pubIds) {
        if ("".equals(pubs)) {
          pubs = String.valueOf(pubId);
        } else {
          pubs += "," + String.valueOf(pubId);
        }
      }
      hql.append(" and t.id in (" + pubs + ")");
    }
    hql.append(" order by nvl(t.publishYear,0) desc, nvl(t.publishMonth,0) desc, nvl(t.publishDay,0) desc, t.id ");

    // 查询数据实体
    return super.createQuery(hql.toString(), params.toArray()).list();

  }

}
