package com.smate.web.v8pub.dao.pdwh;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.po.pdwh.PdwhPubDuplicatePO;

/**
 * 基准库成果查重记录Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PdwhPubDuplicateDAO extends PdwhHibernateDao<PdwhPubDuplicatePO, Long> {

  /**
   * 获取hashTPP相同的基准库成果id
   * 
   * @param hashTPP
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> listDuplicatePubByTPP(String hashTPP) {
    if (StringUtils.isEmpty(hashTPP)) {
      return null;
    }
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where t.hashTPP=:hashTPP ";
    return super.createQuery(hql).setParameter("hashTPP", hashTPP).list();
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
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t "
        + "where t.hashIsiSourceId=:hashSourceId or t.hashEiSourceId=:hashSourceId order by t.pdwhPubId asc";
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
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t "
        + "where (t.hashApplicationNo=:hashApplicationNo and t.hashApplicationNo != 0) "
        + "or (t.hashPublicationOpenNo=:hashPublicationOpenNo and t.hashPublicationOpenNo !=0) "
        + "order by t.pdwhPubId asc";
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
  public List<Long> dupByPubInfo(String hashTP, String hashTPP) {
    return dupByPubInfoNull(hashTP, hashTPP, "1=1 and ");
  }


  /**
   * 通过hashTitle进行查重
   * 
   * @param hashTitle
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByPubInfo(String hashTitle) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<>();
    if (StringUtils.isBlank(hashTitle)) {
      return null;
    }
    hql.append("select t.pdwhPubId from PdwhPubDuplicatePO t where ");
    params.add(hashTitle);
    hql.append(" t.hashTitle=? ");
    hql.append(" order by t.pdwhPubId asc ");
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> dupByStandardNo(Long hashStandardNo) {
    if (NumberUtils.isNullOrZero(hashStandardNo)) {
      return null;
    }
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t "
        + "where t.hashStandardNo=:hashStandardNo order by t.pdwhPubId asc";
    return super.createQuery(hql).setParameter("hashStandardNo", String.valueOf(hashStandardNo)).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> dupByRegisterNo(Long hashRegisterNo) {
    if (NumberUtils.isNullOrZero(hashRegisterNo)) {
      return null;
    }
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t "
        + "where t.hashRegisterNo=:hashRegisterNo order by t.pdwhPubId asc";
    return super.createQuery(hql).setParameter("hashRegisterNo", String.valueOf(hashRegisterNo)).list();
  }

  /**
   * 通过hashDoi进行查重,前提hashDoi或者hashCnkiDoi不为空
   * 
   * @param hashDoi
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> dupByNotNullDoi(Long hashDoi, Long hashCleanDoi) {
    if (NumberUtils.isNullOrZero(hashDoi)) {
      return null;
    }
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where "
        + "(t.hashDoi=:hashDoi and t.hashDoi is not null) or (t.hashCnkiDoi is not null and t.hashCnkiDoi =:hashDoi) "
        + " or (t.hashCleanDoi=:hashCleanDoi and t.hashCleanDoi is not null) or (t.hashCleanCnkiDoi is not null and t.hashCleanCnkiDoi =:hashCleanDoi) "
        + " order by t.pdwhPubId asc";
    return super.createQuery(hql).setParameter("hashDoi", String.valueOf(hashDoi))
        .setParameter("hashCleanDoi", String.valueOf(hashCleanDoi)).list();
  }

  /**
   * 在表中hashDoi为空的前提下，通过hashTpp以及hashTp进行查重
   * 
   * @param hashDoi
   * @return
   */
  public List<Long> dupByPubInfoNullDoi(String hashTP, String hashTPP) {
    String doiNullFlag = "t.hashDoi is null and ";
    return dupByPubInfoNull(hashTP, hashTPP, doiNullFlag);
  }

  public List<Long> dupByPubInfoNullAppNoAndOpenNo(String hashTP, String hashTPP) {
    String patentNullFlag = "t.hashApplicationNo is null and t.hashPublicationOpenNo is null and ";
    return dupByPubInfoNull(hashTP, hashTPP, patentNullFlag);
  }

  public List<Long> dupByPubInfoNullStandardNo(String hashTP, String hashTPP) {
    String standardNoNullFlag = "t.hashStandardNo is null and ";
    return dupByPubInfoNull(hashTP, hashTPP, standardNoNullFlag);
  }

  public List<Long> dupByPubInfoNullRegisterNo(String hashTP, String hashTPP) {
    String registerNoNullFlag = "t.hashRegisterNo is null and ";
    return dupByPubInfoNull(hashTP, hashTPP, registerNoNullFlag);
  }

  public List<Long> dupByPubInfoNullSourceId(String hashTP, String hashTPP) {
    String sourceIdNullFlag = "t.hashIsiSourceId is null and t.hashEiSourceId is null and ";
    return dupByPubInfoNull(hashTP, hashTPP, sourceIdNullFlag);
  }

  @SuppressWarnings("unchecked")
  public List<Long> dupByPubInfoNull(String hashTP, String hashTPP, String nullFlag) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<>();
    if (StringUtils.isBlank(hashTP)) {
      return null;
    }
    hql.append("select t.pdwhPubId from PdwhPubDuplicatePO t where " + nullFlag);
    if (StringUtils.isNotBlank(hashTPP)) {
      params.add(hashTPP);
      params.add(hashTP);
      hql.append(" (t.hashTPP=? or (t.hashTP = ? and t.hashTPP is null)) ");
    } else {
      params.add(hashTP);
      hql.append(" t.hashTP=? ");
    }
    hql.append(" order by t.pdwhPubId asc ");
    return super.createQuery(hql.toString(), params.toArray()).list();
  }
}
