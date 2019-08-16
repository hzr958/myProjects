package com.smate.web.v8pub.dao.sns;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.po.sns.PubDuplicatePO;

/**
 * 个人库成果查重记录Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubDuplicateDAO extends SnsHibernateDao<PubDuplicatePO, Long> {

  /**
   * 通过hashDoi进行查重
   *
   * @param hashDoi
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByDoi(Long hashDoi, Long hashCleanDoi, Long psnId, Long pubId) {
    if (NumberUtils.isNullOrZero(psnId)) {
      return null;
    }
    if (NumberUtils.isNullOrZero(hashDoi) && NumberUtils.isNullOrZero(hashCleanDoi)) {
      return null;
    }
    pubId = NumberUtils.isNotNullOrZero(pubId) ? pubId : 0L;
    String hql = "select t.pubId from PubDuplicatePO t where (t.hashDoi=:hashDoi or t.hashCnkiDoi=:hashDoi "
        + " or t.hashCleanDoi=:hashCleanDoi or t.hashCleanCnkiDoi=:hashCleanDoi ) "
        + " and exists(select 1 from PsnPubPO p where p.pubId=t.pubId and p.ownerPsnId=:psnId and p.status = 0) "
        + " and t.pubId <>:pubId order by t.pubId asc ";
    return super.createQuery(hql).setParameter("hashDoi", String.valueOf(hashDoi))
        .setParameter("hashCleanDoi", String.valueOf(hashCleanDoi)).setParameter("psnId", psnId)
        .setParameter("pubId", pubId).list();
  }

  /**
   * 通过hashSourceId进行查重
   *
   * @param hashSourceId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupBySourceId(Long hashSourceId, Long psnId, Long pubId) {
    if (NumberUtils.isNullOrZero(hashSourceId)) {
      return null;
    }
    if (NumberUtils.isNullOrZero(psnId)) {
      return null;
    }
    pubId = NumberUtils.isNotNullOrZero(pubId) ? pubId : 0L;
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where (t.hashIsiSourceId=:hashSourceId or t.hashEiSourceId=:hashSourceId) "
        + "and exists(select 1 from PsnPubPO p where p.pubId=t.pubId and p.ownerPsnId=:psnId and p.status = 0) "
        + " and t.pubId <>:pubId order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashSourceId", String.valueOf(hashSourceId))
        .setParameter("psnId", psnId).setParameter("pubId", pubId).list();
  }

  /**
   * 通过专利信息的hashApplicationNo和hashPublicationOpenNo进行查重
   *
   * @param hashApplicationNo
   * @param hashPublicationOpenNo
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo, Long psnId, Long pubId) {
    if (NumberUtils.isNullOrZero(hashApplicationNo) && NumberUtils.isNullOrZero(hashPublicationOpenNo)) {
      return null;
    }
    if (NumberUtils.isNullOrZero(psnId)) {
      return null;
    }
    pubId = NumberUtils.isNotNullOrZero(pubId) ? pubId : 0L;
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where ((t.hashApplicationNo=:hashApplicationNo and t.hashApplicationNo != 0) "
        + "or ( t.hashPublicationOpenNo=:hashPublicationOpenNo and t.hashPublicationOpenNo !=0)) "
        + "and exists(select 1 from PsnPubPO p where p.pubId=t.pubId and p.ownerPsnId=:psnId and p.status = 0) "
        + " and t.pubId <>:pubId order by t.pubId asc";

    return super.createQuery(hql).setParameter("hashApplicationNo", String.valueOf(hashApplicationNo))
        .setParameter("hashPublicationOpenNo", String.valueOf(hashPublicationOpenNo)).setParameter("psnId", psnId)
        .setParameter("pubId", pubId).list();
  }

  /**
   * 通过hashTP和hashTPP进行查重
   *
   * @param hashTP
   * @param hashTPP
   * @return
   */
  public List<Long> dupByPubInfo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    return dupByPubInfoNull(hashTP, hashTPP, psnId, pubId, "");
  }

  /**
   * 通过hashTP和hashTPP进行个人成果查重
   *
   * @param hashTP
   * @param hashTPP
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnPubDuplicate(String hashTP, String hashTPP) {
    if (StringUtils.isEmpty(hashTP)) {
      return null;
    }
    String hql =
        "select t.pubId from PubDuplicatePO t where t.hashTP=:hashTP or t.hashTPP=:hashTPP order by t.pubId asc";
    return this.createQuery(hql).setParameter("hashTP", hashTP).setParameter("hashTPP", hashTPP).list();
  }

  /**
   * 通过hashDoi进行查重
   *
   * @param hashDoi
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByDoi(Long hashDoi) {
    if (NumberUtils.isNullOrZero(hashDoi)) {
      return null;
    }
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where t.hashDoi=:hashDoi or t.hashCnkiDoi=:hashDoi order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashDoi", String.valueOf(hashDoi)).list();
  }

  /**
   * 通过hashSourceId进行查重
   *
   * @param hashSourceId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupBySourceId(Long hashSourceId) {
    if (NumberUtils.isNullOrZero(hashSourceId)) {
      return null;
    }
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where t.hashIsiSourceId=:hashSourceId or t.hashEiSourceId=:hashSourceId order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashSourceId", String.valueOf(hashSourceId)).list();
  }

  /**
   * 通过专利信息的hashApplicationNo和hashPublicationOpenNo进行查重
   *
   * @param hashApplicationNo
   * @param hashPublicationOpenNo
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByPatentInfo(Long hashApplicationNo, Long hashPublicationOpenNo) {
    if (NumberUtils.isNullOrZero(hashApplicationNo) && NumberUtils.isNullOrZero(hashPublicationOpenNo)) {
      return null;
    }
    String hql = "select t.pubId from PubDuplicatePO t "
        + "where (t.hashApplicationNo=:hashApplicationNo and t.hashApplicationNo != 0) "
        + "or ( t.hashPublicationOpenNo=:hashPublicationOpenNo and t.hashPublicationOpenNo !=0) "
        + "order by t.pubId asc";
    return super.createQuery(hql).setParameter("hashApplicationNo", String.valueOf(hashApplicationNo))
        .setParameter("hashPublicationOpenNo", String.valueOf(hashPublicationOpenNo)).list();
  }

  /**
   * 通过hashTP和hashTPP进行查重
   *
   * @param hashTP
   * @param hashTPP
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByPubInfo(String hashTP, String hashTPP) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<>();
    if (StringUtils.isBlank(hashTP)) {
      return null;
    }
    hql.append(" select t.pubId from PubDuplicatePO t where 1=1 and ");
    if (StringUtils.isNotBlank(hashTPP)) {
      params.add(hashTPP);
      params.add(hashTP);
      hql.append(" (t.hashTPP=? or (t.hashTP = ? and t.hashTPP is null))");
    } else {
      params.add(hashTP);
      hql.append(" t.hashTP=? ");
    }
    hql.append(" order by t.pubId asc ");
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> dupByStandardNo(Long hashStandardNo, Long psnId, Long pubId) {
    if (NumberUtils.isNullOrZero(hashStandardNo)) {
      return null;
    }
    pubId = NumberUtils.isNotNullOrZero(pubId) ? pubId : 0L;
    String hql = "select t.pubId from PubDuplicatePO t where t.hashStandardNo=:hashStandardNo "
        + "and exists(select 1 from PsnPubPO p where p.pubId=t.pubId and p.ownerPsnId=:psnId and p.status = 0) "
        + "and t.pubId <>:pubId order by t.pubId asc";
    return this.createQuery(hql).setParameter("hashStandardNo", String.valueOf(hashStandardNo))
        .setParameter("psnId", psnId).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> dupByRegisterNo(Long hashRegisterNo, Long psnId, Long pubId) {
    if (NumberUtils.isNullOrZero(hashRegisterNo)) {
      return null;
    }
    pubId = NumberUtils.isNotNullOrZero(pubId) ? pubId : 0L;
    String hql = "select t.pubId from PubDuplicatePO t where t.hashRegisterNo=:hashRegisterNo "
        + "and exists(select 1 from PsnPubPO p where p.pubId=t.pubId and p.ownerPsnId=:psnId and p.status = 0) "
        + "and t.pubId <>:pubId order by t.pubId asc";
    return this.createQuery(hql).setParameter("hashRegisterNo", String.valueOf(hashRegisterNo))
        .setParameter("psnId", psnId).setParameter("pubId", pubId).list();
  }

  public List<Long> dupByPubInfoNullAppNoAndOpenNo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    String patentNullFlag = "and t.hashApplicationNo is null and t.hashPublicationOpenNo is null";
    return dupByPubInfoNull(hashTP, hashTPP, psnId, pubId, patentNullFlag);
  }

  public List<Long> dupByPubInfoNullStandardNo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    String standardNoNullFlag = "and t.hashStandardNo is null";
    return dupByPubInfoNull(hashTP, hashTPP, psnId, pubId, standardNoNullFlag);
  }

  public List<Long> dupByPubInfoNullRegisterNo(String hashTP, String hashTPP, Long psnId, Long pubId) {
    String registerNoNullFlag = "and t.hashRegisterNo is null";
    return dupByPubInfoNull(hashTP, hashTPP, psnId, pubId, registerNoNullFlag);
  }

  public List<Long> dupByPubInfoNullSourceId(String hashTP, String hashTPP, Long psnId, Long pubId) {
    String sourceIdNullFlag = "and t.hashIsiSourceId is null and t.hashEiSourceId is null";
    return dupByPubInfoNull(hashTP, hashTPP, psnId, pubId, sourceIdNullFlag);
  }

  public List<Long> dupByPubInfoNullDoi(String hashTP, String hashTPP, Long psnId, Long pubId) {
    String doiNullFlag =
        "and t.hashCleanCnkiDoi is null and t.hashCleanDoi is null and t.hashDoi and t.hashCnkiDoi is null";
    return dupByPubInfoNull(hashTP, hashTPP, psnId, pubId, doiNullFlag);
  }

  @SuppressWarnings("unchecked")
  public List<Long> dupByPubInfoNull(String hashTP, String hashTPP, Long psnId, Long pubId, String nullFlag) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<>();
    if (StringUtils.isBlank(hashTP)) {
      return null;
    }
    pubId = NumberUtils.isNotNullOrZero(pubId) ? pubId : 0L;
    params.add(pubId);
    hql.append(" select t.pubId from PubDuplicatePO t where t.pubId <>? " + nullFlag + " and ");
    if (StringUtils.isNotBlank(hashTPP)) {
      params.add(hashTPP);
      params.add(hashTP);
      hql.append(" (t.hashTPP=? or (t.hashTP = ? and t.hashTPP is null))");
    } else {
      params.add(hashTP);
      hql.append(" t.hashTP=? ");
    }
    params.add(psnId);
    hql.append(
        " and exists(select 1 from PsnPubPO p where p.pubId=t.pubId and p.ownerPsnId=? and p.status = 0) order by t.pubId asc ");
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

}
