package com.smate.center.task.single.dao.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPubDup;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果查重dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubDupDao extends PdwhHibernateDao<PdwhPubDup, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByDoiHash(Long doiHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where t.doiHash =:doiHash or t.cnkiDoiHash =:doiHash order by t.pubId asc";
    List<Long> pubIds = super.createQuery(hql).setParameter("doiHash", doiHash).list();
    return pubIds;
  }

  /**
   * 
   * <p>
   * Description: 获取重复的成果Id中Id最大的
   * </p>
   * 
   * @author LIJUN
   * @date
   */
  @SuppressWarnings("unchecked")
  public Long getMaxDupPubIdByDoiHash(Long doiHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where t.doiHash =:doiHash or t.cnkiDoiHash =:doiHash order by t.pubId desc";
    List<Long> pubIds = super.createQuery(hql).setParameter("doiHash", doiHash).list();
    if (CollectionUtils.isNotEmpty(pubIds)) {
      return pubIds.get(0);
    } else {
      return 0L;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsBySourceIdHash(Long sourceIdHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where t.isiSourceIdHash=:sourceIdHash or t.eiSourceIdHash=:sourceIdHash order by t.pubId asc";
    List<Long> pubIds = super.createQuery(hql).setParameter("sourceIdHash", sourceIdHash).list();
    return pubIds;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByPatentInfo(Long patentNoHash, Long patentOpenNoHash) {
    List<Long> pubIds = new ArrayList<Long>();
    if (patentNoHash != 0L && patentOpenNoHash != 0L && patentNoHash != null && patentOpenNoHash != null) {
      String hql =
          "select t.pubId from PdwhPubDup t where t.patentNoHash=:patentNoHash or t.patentNoHash=:patentOpenNoHash or t.patentOpenNoHash=:patentNoHash or t.patentOpenNoHash=:patentOpenNoHash order by t.pubId asc";
      pubIds = super.createQuery(hql).setParameter("patentNoHash", patentNoHash)
          .setParameter("patentOpenNoHash", patentOpenNoHash).list();
      return pubIds;
    } else if (patentNoHash != 0L && patentOpenNoHash == 0L && patentNoHash != null && patentOpenNoHash != null) {
      String hql =
          "select t.pubId from PdwhPubDup t where t.patentNoHash=:patentNoHash or t.patentOpenNoHash=:patentNoHash order by t.pubId asc";
      pubIds = super.createQuery(hql).setParameter("patentNoHash", patentNoHash).list();
      return pubIds;
    } else if (patentNoHash == 0L && patentOpenNoHash != 0L && patentNoHash != null && patentOpenNoHash != null) {
      String hql =
          "select t.pubId from PdwhPubDup t where t.patentNoHash=:patentOpenNoHash or t.patentOpenNoHash=:patentOpenNoHash order by t.pubId asc";
      pubIds = super.createQuery(hql).setParameter("patentOpenNoHash", patentOpenNoHash).list();
      return pubIds;
    } else {
      return pubIds;
    }
  }

  @SuppressWarnings("unchecked")
  public Long queryPubPdwhIdByIsiSourceId(String sourceId) {
    String hql = "select t.pubId from PdwhPubDup t where t.isiSourceId =:sourceId order by t.pubId desc";
    return (Long) super.createQuery(hql).setParameter("sourceId", sourceId).setMaxResults(1).uniqueResult();
  }

  public Long queryPubPdwhIdByEISourceId(String sourceId) {
    String hql = "select t.pubId from PdwhPubDup t where t.eiSourceId =:sourceId order by t.pubId desc";
    return (Long) super.createQuery(hql).setParameter("sourceId", sourceId).setMaxResults(1).uniqueResult();
  }

  public Long getPubPdwhIdByZETitleHash(Long zhTitleHash, Long enTitleHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where t.zhTitleHash =:zhTitleHash and t.enTitleHash=:enTitleHash order by t.pubId desc";
    return (Long) super.createQuery(hql).setParameter("zhTitleHash", zhTitleHash)
        .setParameter("enTitleHash", enTitleHash).setMaxResults(1).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByTitle(Long zhTitleHash, Long enTitleHash, Long titleHash, Integer pubType,
      Integer pubYear) {
    if (zhTitleHash == 0L && enTitleHash == 0L && titleHash == 0L) {
      return null;
    }

    StringBuffer hql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<String, Object>();
    hql.append("select t.pubId from PdwhPubDup t where t.pubType=:pubType and t.pubYear=:pubYear and ");
    parameters.put("pubType", pubType);
    parameters.put("pubYear", pubYear);
    // zhTitleHash与enTitleHash任何一个不为0，则titleHash不为0
    if (zhTitleHash != 0L && enTitleHash != 0L) {
      hql.append(
          "(t.zhTitleHash=:zhTitleHash or t.enTitleHash=:enTitleHash or t.zhTitleHash=:enTitleHash or t.enTitleHash=:zhTitleHash or t.titleHash=:titleHash) ");
      parameters.put("zhTitleHash", zhTitleHash);
      parameters.put("enTitleHash", enTitleHash);
      parameters.put("titleHash", titleHash);
    }
    if (enTitleHash != 0L && zhTitleHash == 0L) {
      hql.append("(t.zhTitleHash=:enTitleHash or t.enTitleHash=:enTitleHash or t.titleHash=:titleHash) ");
      parameters.put("enTitleHash", enTitleHash);
      parameters.put("titleHash", titleHash);
    }
    if (zhTitleHash != 0L && enTitleHash == 0L) {
      hql.append("(t.zhTitleHash=:zhTitleHash or t.enTitleHash=:zhTitleHash or t.titleHash=:titleHash) ");
      parameters.put("zhTitleHash", zhTitleHash);
      parameters.put("titleHash", titleHash);
    }

    hql.append("order by t.pubId asc");

    List<Long> pubIds = super.createQuery(hql.toString(), parameters).list();
    return pubIds;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByTitle(Long zhTitleHash, Long enTitleHash, Long titleHash) {
    if (zhTitleHash == 0L && enTitleHash == 0L && titleHash == 0L) {
      return null;
    }

    StringBuffer hql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<String, Object>();
    hql.append("select t.pubId from PdwhPubDup t where ");
    // zhTitleHash与enTitleHash任何一个不为0，则titleHash不为0
    if (zhTitleHash != 0L && enTitleHash != 0L) {
      hql.append(
          "t.zhTitleHash=:zhTitleHash or t.enTitleHash=:enTitleHash or t.zhTitleHash=:enTitleHash or t.enTitleHash=:zhTitleHash or t.titleHash=:titleHash ");
      parameters.put("zhTitleHash", zhTitleHash);
      parameters.put("enTitleHash", enTitleHash);
      parameters.put("titleHash", titleHash);
    }
    if (enTitleHash != 0L && zhTitleHash == 0L) {
      hql.append("t.zhTitleHash=:enTitleHash or t.enTitleHash=:enTitleHash or t.titleHash=:titleHash ");
      parameters.put("enTitleHash", enTitleHash);
      parameters.put("titleHash", titleHash);
    }
    if (zhTitleHash != 0L && enTitleHash == 0L) {
      hql.append("t.zhTitleHash=:zhTitleHash or t.enTitleHash=:zhTitleHash or t.titleHash=:titleHash ");
      parameters.put("zhTitleHash", zhTitleHash);
      parameters.put("titleHash", titleHash);
    }

    hql.append("order by t.pubId asc");

    List<Long> pubIds = super.createQuery(hql.toString(), parameters).list();
    return pubIds;
  }

  public Long getPubIdByPatNoandOpenNoHash(Long patentNoHash, Long patentOpenNoHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where t.patentNoHash=:patentNoHash and t.patentOpenNoHash=:patentOpenNoHash order by t.pubId desc";
    return (Long) super.createQuery(hql).setParameter("patentNoHash", patentNoHash)
        .setParameter("patentOpenNoHash", patentOpenNoHash).setMaxResults(1).uniqueResult();

  }

  public String getDoiStringByPubId(Long pubId) {
    String hql = "select t.doi from PdwhPubDup t where t.pubId=:pubId";
    return (String) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();

  }
}
