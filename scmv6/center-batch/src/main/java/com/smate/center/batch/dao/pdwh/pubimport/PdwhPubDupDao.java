package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubDup;
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

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsBySourceIdHash(Long sourceIdHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where t.isiSourceIdHash=:sourceIdHash or t.eiSourceIdHash=:sourceIdHash order by t.pubId asc";
    List<Long> pubIds = super.createQuery(hql).setParameter("sourceIdHash", sourceIdHash).list();
    return pubIds;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIdsByPatentInfo(Long patentNoHash, Long patentOpenNoHash) {
    String hql =
        "select t.pubId from PdwhPubDup t where (t.patentNoHash=:patentNoHash or t.patentOpenNoHash=:patentOpenNoHash) and t.patentNoHash != 0 and t.patentOpenNoHash !=0 order by t.pubId asc";
    List<Long> pubIds = super.createQuery(hql).setParameter("patentNoHash", patentNoHash)
        .setParameter("patentOpenNoHash", patentOpenNoHash).list();
    return pubIds;
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
  public List<Long> getDupPubIdsByTitle(Long zhTitleHashCode, Long enTitleHashCode, Integer pubType, Integer pubYear) {
    if (zhTitleHashCode == 0L && enTitleHashCode == 0L) {
      return null;
    }
    String hql =
        "select t.pubId from PdwhPubDup t where t.pubType=:pubType and t.pubYear=:pubYear and (t.zhTitleHash = :zhTitleHash or t.enTitleHash = :enTitleHash )";
    return super.createQuery(hql).setParameter("zhTitleHash", zhTitleHashCode)
        .setParameter("enTitleHash", enTitleHashCode).setParameter("pubType", pubType).setParameter("pubYear", pubYear)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getDupPubIds(Long zhTitleHash, Long enTitleHash, Long titleHashValue, Integer pubType,
      String pubYear) {
    if (zhTitleHash == 0L && enTitleHash == 0L && titleHashValue == 0L) {
      return null;
    }
    StringBuffer hql = new StringBuffer();
    Map<String, Object> parameters = new HashMap<String, Object>();
    hql.append("select t.pubId from PdwhPubDup t where t.pubType=:pubType and ");
    parameters.put("pubType", pubType);
    // zhTitleHash与enTitleHash任何一个不为0，则titleHash不为0
    if (zhTitleHash != 0L && enTitleHash != 0L) {
      hql.append(
          "(t.zhTitleHash=:zhTitleHash or t.enTitleHash=:enTitleHash or t.zhTitleHash=:enTitleHash or t.enTitleHash=:zhTitleHash or t.titleHash=:titleHash) ");
      parameters.put("zhTitleHash", zhTitleHash);
      parameters.put("enTitleHash", enTitleHash);
      parameters.put("titleHash", titleHashValue);
    }
    if (enTitleHash != 0L && zhTitleHash == 0L) {
      hql.append("(t.zhTitleHash=:enTitleHash or t.enTitleHash=:enTitleHash or t.titleHash=:titleHash) ");
      parameters.put("enTitleHash", enTitleHash);
      parameters.put("titleHash", titleHashValue);
    }
    if (zhTitleHash != 0L && enTitleHash == 0L) {
      hql.append("(t.zhTitleHash=:zhTitleHash or t.enTitleHash=:zhTitleHash or t.titleHash=:titleHash) ");
      parameters.put("zhTitleHash", zhTitleHash);
      parameters.put("titleHash", titleHashValue);
    }
    if (StringUtils.isNotBlank(pubYear)) {
      hql.append(" and t.pubYear=:pubYear ");
      parameters.put("pubYear", Integer.valueOf(pubYear));
    }

    hql.append("order by t.pubId asc");

    List<Long> pubIds = super.createQuery(hql.toString(), parameters).list();
    return pubIds;
  }

}
