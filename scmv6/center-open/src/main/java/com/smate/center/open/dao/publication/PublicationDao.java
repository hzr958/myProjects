package com.smate.center.open.dao.publication;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.PubMember;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 成果、文献DAO.
 * 
 * @author ajb
 * 
 */
@Repository
public class PublicationDao extends HibernateDao<Publication, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * IRIS业务系统接口查询某人的公开成果总数.
   * 
   * @param psnId
   * @param keywords
   * @param uuid
   * @param permissions
   * @return
   * @throws DaoException
   */
  public Long queryPsnPublicPubCount(Long psnId, String keywords, String uuid, List<Integer> permissions,
      List<Integer> pubTypeList) {
    StringBuffer sb = new StringBuffer(
        "select count(t.pubId) from Publication t where t.articleType=:arcticleType and t.status=:status and t.ownerPsnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.pubType in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    sb.append(
        " and exists(select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=t.pubId and pcp.anyUser in(:permissions) )");
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like :zhTitle or lower(t.enTitle) like :enTitle)");
    }
    Query query = super.createQuery(sb.toString()).setParameter("arcticleType", 1).setParameter("status", 0)
        .setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    query.setParameterList("permissions", permissions);
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    return (Long) query.uniqueResult();
  }

  public Long getPsnNotExistsResumePubCount(Long psnId) {
    String hql =
        "select count(t.pubId) from Publication t where t.articleType=:arcticleType and t.status=:status and t.ownerPsnId=:psnId ";
    hql += " and not exists(select 1 from PsnConfigPub t1 where t.pubId=t1.id.pubId)";
    Query query =
        super.createQuery(hql).setParameter("arcticleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    return (Long) query.uniqueResult();
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubMember> getPubMembersByPubId(Long pubId) throws Exception {
    try {
      Query query = super.createQuery("from PubMember t where t.pubId= ? order by seqNo", new Object[] {pubId});
      List list = query.list();
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * IRIS业务系统接口查询某人的成果总数.
   * 
   * @param psnId
   * @param keywords
   * @param pubTypes TODO
   * @return
   * @throws DaoException
   */
  public Long queryPsnPubCount(Long psnId, String keywords, String authors, String uuid, List<Integer> pubTypes)
      throws Exception {
    StringBuffer sb = new StringBuffer(
        "select count(t.pubId) from Publication t where t.articleType=:articleType and t.status=:status and t.ownerPsnId=:psnId");
    if (uuid != null) {
      sb.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    if (CollectionUtils.isNotEmpty(pubTypes)) {
      sb.append(" and t.pubType in(:pubTypes)");
    }
    if (StringUtils.isNotBlank(authors)) {
      sb.append(" and lower(t.authorNames) like:authors ");
    }
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like:zhTitle or lower(t.enTitle) like:enTitle)");
    }
    Query q = super.createQuery(sb.toString());
    q.setParameter("articleType", 1);
    q.setParameter("status", 0);
    q.setParameter("psnId", psnId);
    if (uuid != null) {
      q.setParameter("uuid", uuid);
    }
    if (CollectionUtils.isNotEmpty(pubTypes)) {
      q.setParameterList("pubTypes", pubTypes);
    }
    if (StringUtils.isNotBlank(authors)) {
      q.setParameter("authors", "%" + authors.toLowerCase() + "%");
    }
    if (StringUtils.isNotBlank(keywords)) {
      q.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%");
      q.setParameter("enTitle", "%" + keywords.toLowerCase() + "%");
    }
    return (Long) q.uniqueResult();
  }

  /**
   * IRIS业务系统接口分页查询某人的公开成果记录.
   * 
   * @param psnId
   * @param keywords
   * @param uuid
   * @param permissions
   * @param sortType
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<Publication> queryPsnPublicPubByPage(Long psnId, String keywords, String uuid, List<Integer> permissions,
      String sortType, Page<Publication> page, List<Integer> pubTypeList) {
    StringBuffer sb = new StringBuffer(
        "from Publication t where t.articleType=:arcticleType and t.status=:status and t.ownerPsnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.pubType in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    sb.append(
        " and exists(select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=t.pubId and pcp.anyUser in(:permissions) )");
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like :zhTitle or lower(t.enTitle) like :enTitle)");
    }

    Query query = super.createQuery("select count(t.pubId) " + sb.toString()).setParameter("arcticleType", 1)
        .setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    query.setParameterList("permissions", permissions);
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    Long count = (Long) query.uniqueResult();
    page.setTotalCount(count);

    if ("updateTime".equals(sortType)) {
      sb.append(" order by t.updateDate desc, t.pubId desc");
    } else {
      sb.append(" order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.pubId desc");
    }

    query = super.createQuery(
        "select new Publication(t.pubId, t.zhTitle, t.enTitle, t.authorNames, t.pubType, t.briefDesc, t.briefDescEn, t.citedList, t.citedTimes, t.DOI, t.publishYear, t.publishMonth, t.publishDay, t.updateDate) "
            + sb.toString()).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize())
                .setParameter("arcticleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    query.setParameterList("permissions", permissions);
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    page.setResult(query.list());
    return page;
  }

  /**
   * IRIS业务系统接口分页查询某人的成果记录.
   * 
   * @param psnId
   * @param keywords
   * @param sortType
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<Publication> queryPsnPubByPage(Long psnId, String keywords, String authors, String uuid, String sortType,
      Page<Publication> page, List<Integer> pubTypeList) {
    StringBuffer sb = new StringBuffer(
        "from Publication t where t.articleType=:articleType and t.status=:status and t.ownerPsnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.pubType in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    if (StringUtils.isNotBlank(authors)) {
      sb.append(" and lower(t.authorNames) like :authorNames ");
    }
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like :zhTitle or lower(t.enTitle) like :enTitle)");
    }
    Query query = super.createQuery("select count(t.pubId) " + sb.toString()).setParameter("articleType", 1)
        .setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    if (StringUtils.isNotBlank(authors)) {
      query.setParameter("authorNames", "%" + authors.toLowerCase() + "%");
    }
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    Long count = (Long) query.uniqueResult();
    page.setTotalCount(count);

    if ("updateTime".equals(sortType)) {
      sb.append(" order by t.updateDate desc, t.pubId desc");
    } else {
      sb.append(" order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.pubId desc");
    }

    query = super.createQuery(
        "select new Publication(t.pubId, t.zhTitle, t.enTitle, t.authorNames, t.pubType, t.briefDesc, t.briefDescEn, t.citedList, t.citedTimes, t.DOI, t.publishYear, t.publishMonth, t.publishDay, t.updateDate) "
            + sb.toString()).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize())
                .setParameter("articleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      query.setParameterList("pubTypeList", pubTypeList);
    }
    if (uuid != null) {
      query.setParameter("uuid", uuid);
    }
    if (StringUtils.isNotBlank(authors)) {
      query.setParameter("authorNames", "%" + authors.toLowerCase() + "%");
    }
    if (StringUtils.isNotBlank(keywords)) {
      query.setParameter("zhTitle", "%" + keywords.toLowerCase() + "%").setParameter("enTitle",
          "%" + keywords.toLowerCase() + "%");
    }
    page.setResult(query.list());
    return page;
  }

  public List<Publication> findPublicationsToInterConnection(Long psnId, Integer pageNo, Integer pageSize) {
    String hql = "from Publication t where t.status=0  and   t.articleType= 1 and  t.ownerPsnId=:psnId ";
    Query query = this.createQuery(hql).setParameter("psnId", psnId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize);
    return query.list();
  }

  public Long findCount(Long psnId) {
    Query query = super.createQuery(
        "select count(t.pubId)  from Publication t where t.status=:status and t.articleType=:articleType and  t.ownerPsnId=:psnId")
            .setParameter("status", 0).setParameter("articleType", 1).setParameter("psnId", psnId);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 
   * @author liangguokeng
   */
  @SuppressWarnings("unchecked")
  public List<Publication> findPubIdsByPsnId(Long psnId) throws Exception {
    String hql = "from Publication t where t.status=0 and t.articleType=1 and t.ownerPsnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 通过psnId 查找 pubIds
   * 
   * @author ajb
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPubIdListByPsnId(Long psnId, Integer pageNo, Integer pageSize) {
    String hql = "select   t.pubId  from Publication t where t.status=0 and t.articleType=1 and t.ownerPsnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdListByPsnId(Long psnId) {
    String hql = "select t.pubId from Publication t where t.status = 0 and t.articleType = 1 and t.ownerPsnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
