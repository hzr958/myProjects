package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果、文献DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationDao extends SnsHibernateDao<Publication, Long> {

  @SuppressWarnings("unchecked")
  public List<Publication> batchGetPublist(Long lastId, Integer batchSize) {
    String hql = "from Publication t where  t.id>:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();

  }

  /**
   * 个人成果的总数.
   * 
   * @param psnId
   * @throws DaoException
   */
  public Long getTotalPubsByPsnId(Long psnId) throws DaoException {
    Long count =
        super.findUnique("select count(t.id) from Publication t where t.psnId=? and t.articleType = 1  and t.status=0",
            new Object[] {psnId});
    return count;
  }

  /**
   * 获取上个月的个人成果引用数 zk
   */
  @SuppressWarnings("rawtypes")
  public List getLastMonthCitedTimes(Integer size) throws DaoException {
    // String hql =
    // "select count(p.id) as count,p.psnId as psnId from Publication p
    // where p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and
    // p.articleType = 1 and p.status =0 group by p.psnId having
    // count(p.psnId)>=1";
    // 最近1周，七天
    String hql =
        "select  sum(nvl(p.citedTimes,0)) as count,p.psnId  as psnId from Publication p where trunc( p.createDate)>=trunc(sysdate-7) and p.articleType = 1  and p.status =0  group by p.psnId having count(p.psnId)>=1";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  /**
   * 获取上个月的个人成果总数 zk
   */
  @SuppressWarnings("rawtypes")
  public List getLastMonthPsnPubs(Integer size) throws DaoException {
    // String hql =
    // "select count(p.id) as count,p.psnId as psnId from Publication p
    // where p.createDate>=trunc(last_day(add_months(sysdate,-2))+1)and
    // p.createDate<trunc(last_day(add_months(sysdate,-1))+1) and
    // p.articleType = 1 and p.status =0 group by p.psnId having
    // count(p.psnId)>=1";
    // 最近一周，七天
    String hql =
        "select  count(p.id) as count,p.psnId  as psnId from Publication p where trunc( p.createDate)>=trunc(sysdate-7) and p.articleType = 1  and p.status =0  group by p.psnId having count(p.psnId)>=1 ";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }



  @SuppressWarnings("unchecked")
  public List<Publication> findPubByPnsId(Long psnId) throws ServiceException {
    String hql =
        "select distinct new Publication(t.psnId,t.articleType,t.zhTitleHash,t.enTitleHash) from PubKnow t where t.psnId=? and t.status in (0,2,3,4,5) and t.articleType=1";
    return super.createQuery(hql, new Object[] {psnId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> matchPubPsnIds(Publication pub) throws ServiceException {
    String hql =
        "select t1.psnId,count(t1.psnId) from PubKnow t1,PersonKnow t2 where t1.psnId=t2.personId and t1.status=0 and t1.articleType=1 and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t1.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) ";
    List<Object> params = new ArrayList<Object>();
    params.add(pub.getPsnId());
    if (pub.getZhTitleHash() != null) {
      hql += "and t1.zhTitleHash=? ";
      params.add(pub.getZhTitleHash());
    }
    if (pub.getEnTitleHash() != null) {
      hql += "and t1.enTitleHash=? ";
      params.add(pub.getEnTitleHash());
    }
    hql += "group by t1.psnId";
    return rebuildResutl(hql, params);
  }


  @SuppressWarnings("unchecked")
  private List<Map<String, Object>> rebuildResutl(String hql, List<Object> params) {
    List<Object[]> objList = super.createQuery(hql, params.toArray()).list();
    if (CollectionUtils.isEmpty(objList))
      return null;
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    for (Object[] objects : objList) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", Long.valueOf(String.valueOf(objects[0])));
      map.put("count", Integer.valueOf(String.valueOf(objects[1])));
      listMap.add(map);
    }
    return listMap;
  }

  public List<Map<String, Object>> matchPubConfimPsnIds(Publication pub) throws ServiceException {
    String hql =
        "select t1.psnId,count(t1.psnId) from PubConfirmKnowFields t1,PersonKnow t2 where t1.psnId=t2.personId and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t1.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) ";
    List<Object> params = new ArrayList<Object>();
    params.add(pub.getPsnId());
    if (pub.getZhTitleHash() != null) {
      hql += "and t1.zhTitleHash=? ";
      params.add(pub.getZhTitleHash());
    }
    if (pub.getEnTitleHash() != null) {
      hql += "and t1.enTitleHash=? ";
      params.add(pub.getEnTitleHash());
    }
    hql += "group by t1.psnId";
    return rebuildResutl(hql, params);
  }

  @Override
  public void save(Publication entity) {
    super.save(entity);
  }



  @SuppressWarnings("rawtypes")
  public int pubMatchName(Long tmPsnId, String zhName, String likeName) {
    zhName = StringUtils.isBlank(zhName) ? "" : zhName.trim();
    likeName = StringUtils.isBlank(likeName) ? "" : likeName.trim().toLowerCase();
    String hql =
        "select count(t1.id) from PubKnow t1,PubMember t2 where t1.id=t2.pubId and (t2.name=? or instr(lower(t2.name),?)>0) and t1.psnId=?";
    List list = createQuery(hql, zhName, likeName, tmPsnId).list();
    return CollectionUtils.isEmpty(list) ? 0 : Integer.valueOf(list.get(0).toString());
  }

  @SuppressWarnings("rawtypes")
  public List findPubIdsBatch(Long lastId, int batchSize) throws DaoException {
    String sql =
        "select t.pub_id from task_pub_ids t where t.pub_id > ? and t.status =? and rownum <= ? order by t.pub_id asc";
    return super.queryForList(sql, new Object[] {lastId, 0, batchSize});
  }


  /**
   * 
   * @author liangguokeng
   */
  @SuppressWarnings("unchecked")
  public List<Publication> findPubIdsByPsnId(Long psnId) throws DaoException {
    String hql = "from Publication t where t.status=0 and t.articleType=1 and t.psnId=? ";
    return super.createQuery(hql, psnId).list();
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
      List<Integer> pubTypeList) throws DaoException {
    StringBuffer sb = new StringBuffer(
        "select count(t.id) from Publication t where t.articleType=:arcticleType and t.status=:status and t.psnId=:psnId");
    if (CollectionUtils.isNotEmpty(pubTypeList)) {
      sb.append(" and t.typeId in(:pubTypeList)");
    }
    if (uuid != null) {
      sb.append(" and t.id not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=:uuid)");
    }
    sb.append(
        " and exists(select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=t.id and pcp.anyUser in(:permissions) )");
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
        "select count(t.id) from Publication t where t.articleType=:arcticleType and t.status=:status and t.psnId=:psnId ";
    hql += " and not exists(select 1 from PsnConfigPub t1 where t.id=t1.id.pubId)";
    Query query =
        super.createQuery(hql).setParameter("arcticleType", 1).setParameter("status", 0).setParameter("psnId", psnId);
    return (Long) query.uniqueResult();
  }


  /**
   * 查询成果推荐基本信息.
   * 
   * @param pubId
   * @return
   */
  public Publication getPubBasicRcmdInfo(Long pubId, Integer publishYear) {

    String hql =
        "select new Publication(id, psnId, typeId, publishYear,publishMonth,publishDay,impactFactors,citedTimes,sourceDbId,doi,isiId) from"
            + " Publication t where t.id = ? and t.publishYear >= ?";
    return super.findUnique(hql, pubId, publishYear);
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubIdListByPsnId(Long psnId) {
    String hql = "select t.id from Publication t where t.status = 0 and t.articleType = 1 and t.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public int getSourceDbId(Long pubId) {
    String hql = "select t.sourceDbId from  Publication t where t.pubId=:pubId";
    return (int) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Publication> getPubByPsnId() {
    String hql =
        "select new Publication(psnId,id,zhTitle,enTitle,sourceDbId ) from Publication t where exists (select 1 from PsnTmp p where p.personId=t.psnId) and t.sourceDbId in (6,11,21)";
    return super.createQuery(hql).list();
  }


  @SuppressWarnings("unchecked")
  public List<Publication> getPubDetailByPubIds(List<Long> pubIds) {
    String hql =
        "select new Publication(t.id,t.zhTitle,t.enTitle,t.briefDesc,t.briefDescEn,t.authorNames) from Publication  t where t.id in (:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  public void updateBrief(String zhBrief, String enBrief, Long pubId) {
    String hql = "update Publication set briefDesc=:zhBrief,briefDescEn=:enBrief where id=:pubId";
    super.createQuery(hql).setParameter("zhBrief", zhBrief).setParameter("enBrief", enBrief)
        .setParameter("pubId", pubId).executeUpdate();
  }
}
