package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;
import com.smate.center.batch.service.pub.ConstPdwhPubRefDb;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationPdwhDao extends SnsHibernateDao<PublicationPdwh, Long> {

  /**
   * 批量获取成果基准库ID信息.
   * 
   * @param lastId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationPdwh> loadPubPdwhBatch(Long lastId) {
    String hql = "from PublicationPdwh t where t.pubId > ? order by t.pubId asc";
    return super.createQuery(hql, lastId).setMaxResults(100).list();
  }

  /**
   * 根据基准库成果ID查找出sns的成果ID 不包括自己
   * 
   * @param id
   * @param colomn
   * @return
   * @throws DaoException
   */

  public List<Long> findSnsPubIds(Long id, String column, Long pubId) throws DaoException {
    String hql = "select t.pubId from PublicationPdwh t where t.pubId != ? and t." + column + "=?";

    return super.createQuery(hql, pubId, id).list();

  }

  /**
   * 根据基准库成果ID查找出sns的成果ID
   * 
   * @param id
   * @param colomn
   * @return
   * @throws DaoException
   */
  public List<Long> findSnsPubIds(Long id, String column) throws DaoException {
    String hql = "select t.pubId from PublicationPdwh t where t." + column + "=?";
    return super.createQuery(hql, id).list();
  }

  /**
   * 根据基准库成果ID查找出sns的成果的psnIds
   * 
   * @param page
   * @param id
   * @param colomn
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findSnsPubOwnerPsnIds(Page page, Long id, String column) throws DaoException {
    String listHql = "select distinct p.psnId ";
    String countHql = "select count(distinct p.psnId) ";
    String hql = " from Publication p where p.id in(select t.pubId from PublicationPdwh t where t." + column
        + "=?) and p.articleType=1 and p.status = 0";
    Long totalCount = super.findUnique(countHql + hql, id);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(listHql + hql, id);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  /**
   * 根据基准库成果ID查找出sns的成果的psnIds 增加pub_know 的过滤 SCM-7804
   * 
   * @param page
   * @param id
   * @param colomn
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findSnsPubOwnerPsnIdsWithKnow(Page page, Long id, String column) throws DaoException {
    String listHql = "select distinct p.psnId ";
    String countHql = "select count(distinct p.psnId) ";
    String hql = " from Publication p where p.id in(select t.pubId from PublicationPdwh t where t." + column
        + "=?) and p.articleType=1 and p.status = 0 and exists (select 1 from PubKnow q where q.id=p.id "
        + "and q.psnId=p.psnId and q.isPubAuthors=1  ) ";
    Long totalCount = super.findUnique(countHql + hql, id);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(listHql + hql, id);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, String>> findSnsPubauthors(Page page, Long id, String column, Long pubId)
      throws DaoException {
    String sql = "select * from (with t_member as (select p.owner_psn_id,'' psn_name,q.seq_no "
        + "from publication p, pub_know q where q.pub_id = p.pub_id and q.owner_psn_id = p.owner_psn_id "
        + "and q.is_pub_authors = 1 and p.pub_id in (select t.pub_id from pub_pdwh t where t." + column + "=:id) "
        + "and p.article_type = 1 and p.status = 0) select t.member_psn_id, t.member_name psn_name, t.seq_no "
        + "from pub_member t where t.pub_id = :pubId and not exists "
        + "(select 1 from t_member a where a.seq_no = t.seq_no) union "
        + "select tm.owner_psn_id, tm.psn_name, tm.seq_no from t_member tm) "
        + "order by  seq_no nulls first, member_psn_id";
    Query query = super.getSession().createSQLQuery(sql);
    query.setParameter("pubId", pubId);
    query.setParameter("id", id);
    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    page.setTotalCount(query.list().size());
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    return query.list();
  }

  /**
   * 根据基准库成果ID查找出sns的成果的psnIds、pubIds
   * 
   * @param isFindFullText
   * @param maxSize
   * @param id
   * @param colomn
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findSnsPubPsnIdsAndPubIds(boolean isFindFullText, int maxSize, Long id,
      String column) throws DaoException {
    String hql = "select t1.psnId,t1.id from Publication t1 where t1.status = 0 ";
    if (isFindFullText)
      hql += " and t1.fulltextFileid is not null";
    hql += " and exists(select 1 from PublicationPdwh t2 where t2.pubId=t1.id and t2." + column + "=?)";
    List<Object[]> objList = super.createQuery(hql, id).setMaxResults(maxSize).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    for (Object[] objects : objList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", Long.valueOf(String.valueOf(objects[0])));
      map.put("pubId", Long.valueOf(String.valueOf(objects[1])));
      listMap.add(map);
    }
    return listMap;
  }

  /**
   * 根据基准库成果ID查找出sns的成果的pubIds
   * 
   * @param page
   * @param pubPdwhId
   * @param colomn
   * @param pubId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findSnsPubPubIds(Page page, Long pubPdwhId, String column, Long pubId) throws DaoException {
    String listHql = "select p.id ";
    String countHql = "select count(p.id) ";
    String hql = " from Publication p where p.id in(select t.pubId from PublicationPdwh t where t." + column
        + "=?) and p.status = 0 and p.id not in(?)";
    Long totalCount = super.findUnique(countHql + hql, pubPdwhId, pubId);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(listHql + hql, pubPdwhId, pubId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  /**
   * 根据基准库成果ID查找出sns的其它相关成果的pubId
   * 
   * @param pubId TODO
   * @param page
   * @param id
   * @param colomn
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findSnsOtherRelPubId(Long pubPdwhId, String column, Long pubId, Long psnId) throws DaoException {
    String hql = "select p.id from Publication p where p.id in(select t.pubId from PublicationPdwh t where t." + column
        + "=?) and p.status = 0 and p.id<>? and p.psnId<>?";
    return super.createQuery(hql, pubPdwhId, pubId, psnId).setMaxResults(1).list();
  }

  /**
   * 获取成果的基准库关联信息.
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationPdwh> getPubPdwhListByPubIds(List<Long> pubIds) {
    if (CollectionUtils.isEmpty(pubIds)) {
      return null;
    }
    String hql = "from PublicationPdwh t where t.pubId in(:pubIds)";
    Collection<Collection<Long>> container = ServiceUtil.splitList(pubIds, 80);
    List<PublicationPdwh> listResult = new ArrayList<PublicationPdwh>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("pubIds", item).list());
    }
    return listResult;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdByPdwhId(List<Long> pdwhIds, int type) {

    List<Long> listResult = new ArrayList<Long>();
    String hql = "select t.pubId from PublicationPdwh t where ";
    if (type == ConstPdwhPubRefDb.ISI) {
      hql += " t.isiId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.EI) {
      hql += " t.eiId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.SCOPUS) {
      hql += " t.spsId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.CNKI) {
      hql += " t.cnkiId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.WanFang) {
      hql += " t.wfId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.CNIPR) {
      hql += " t.cniprId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.PubMed) {
      hql += " t.pubmedId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.ScienceDirect) {
      hql += " t.scdId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.IEEE) {
      hql += " t.ieeeXpId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.Baidu) {
      hql += " t.baiduId in(:pdwhIds) ";
    } else if (type == ConstPdwhPubRefDb.Cnkipat) {
      hql += " t.cnkiPatId in(:pdwhIds) ";
    } else {
      return listResult;
    }
    Collection<Collection<Long>> container = ServiceUtil.splitList(pdwhIds, 80);
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("pdwhIds", item).list());
    }
    return listResult;
  }

  @SuppressWarnings("rawtypes")
  public int getPubPdwhIdByPsnFriend(Long psnId, Long pdwhPubId, int dbid) {
    String hql =
        "select count(t.pubId) from PublicationPdwh t,PubKnow t2,Friend t3 where t.pubId=t2.id and t2.psnId=t3.friendPsnId and t3.psnId=:psnId and ";
    return makeFindPwdhPub(psnId, pdwhPubId, dbid, hql);
  }


  @SuppressWarnings("rawtypes")
  private int makeFindPwdhPub(Long psnId, Long pdwhPubId, int dbid, String hql) {
    if (dbid == ConstPdwhPubRefDb.ISI) {
      hql += " t.isiId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.EI) {
      hql += " t.eiId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.SCOPUS) {
      hql += " t.spsId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.CNKI) {
      hql += " t.cnkiId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.WanFang) {
      hql += " t.wfId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.CNIPR) {
      hql += " t.cniprId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.PubMed) {
      hql += " t.pubmedId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.ScienceDirect) {
      hql += " t.scdId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.IEEE) {
      hql += " t.ieeeXpId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.Baidu) {
      hql += " t.baiduId in(:pdwhIds) ";
    } else if (dbid == ConstPdwhPubRefDb.Cnkipat) {
      hql += " t.cnkiPatId in(:pdwhIds) ";
    } else {
      return 0;
    }
    List list = super.createQuery(hql).setParameter("psnId", psnId).setParameter("pdwhIds", pdwhPubId).list();
    return CollectionUtils.isEmpty(list) ? 0 : NumberUtils.toInt(ObjectUtils.toString(list.get(0)));
  }

  public PublicationPdwh getPublicationPdwhByPubId(Long pubId) throws DaoException {
    String hql = "from PublicationPdwh t where t.pubId=?";
    return super.findUnique(hql, pubId);
  }

  /**
   * 通过基准库各来源库id得到成果关系
   * 
   * @param pdwhPubMap
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationPdwh> getPubPdwhByPdwhId(Map<String, Long> pdwhPubMap) {

    StringBuffer hql = new StringBuffer();
    hql.append(" from PublicationPdwh t where ");
    for (String key : pdwhPubMap.keySet()) {
      hql.append(" t." + key + " = " + pdwhPubMap.get(key) + " or ");
    }
    hql.append(" 1=0 ");
    return super.createQuery(hql.toString()).list();
  }
}
