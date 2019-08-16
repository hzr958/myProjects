package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubDuplicatePO;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.number.NumberUtils;

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
    List<Long> dupPubIds = super.createQuery(hql).setParameter("hashTPP", hashTPP).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    return dupPubIds;
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
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where (t.hashDoi=:hashDoi or t.hashCnkiDoi=:hashDoi) "
        + " order by t.pdwhPubId asc";
    List<Long> dupPubIds = super.createQuery(hql).setParameter("hashDoi", String.valueOf(hashDoi)).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    return dupPubIds;
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
        + " where (t.hashIsiSourceId=:hashSourceId or t.hashEiSourceId=:hashSourceId) order by t.pdwhPubId asc";
    List<Long> dupPubIds = super.createQuery(hql).setParameter("hashSourceId", String.valueOf(hashSourceId)).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    return dupPubIds;
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
        + " where (t.hashApplicationNo=:hashApplicationNo or t.hashPublicationOpenNo=:hashPublicationOpenNo) "
        + " and t.hashApplicationNo != 0 and t.hashPublicationOpenNo !=0 order by t.pdwhPubId asc ";
    List<Long> dupPubIds = super.createQuery(hql).setParameter("hashApplicationNo", String.valueOf(hashApplicationNo))
        .setParameter("hashPublicationOpenNo", String.valueOf(hashPublicationOpenNo)).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    return dupPubIds;
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
    if (StringUtils.isEmpty(hashTP)) {
      return null;
    }
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where t.hashTP=:hashTP or t.hashTPP=:hashTPP "
        + " order by t.pdwhPubId asc";
    List<Long> dupPubIds =
        super.createQuery(hql).setParameter("hashTP", hashTP).setParameter("hashTPP", hashTPP).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    return dupPubIds;
  }

  @SuppressWarnings("unchecked")
  public Long getPubIdByPatentNo(Long patentNoHash, Long patentOpenNoHash) {
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where "
        + " t.hashApplicationNo= :patentNoHash or t.hashPublicationOpenNo = :patentOpenNoHash ";
    List<Long> dupPubIds = super.createQuery(hql).setParameter("patentNoHash", String.valueOf(patentNoHash))
        .setParameter("patentOpenNoHash", String.valueOf(patentOpenNoHash)).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Long getPubIdByCleanDoiHash(Long doiHash, Long doiCleanHash) {
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where (t.hashDoi=:hashDoi or t.hashCnkiDoi=:hashDoi "
        + " t.hashCleanDoi=:doiCleanHash or t.hashCleanCnkiDoi=:doiCleanHash )";
    List<Long> dupPubIds = super.createQuery(hql).setParameter("hashDoi", String.valueOf(doiHash))
        .setParameter("doiCleanHash", String.valueOf(doiCleanHash)).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Long getPubPdwhIdByTitleHash(Long strHashCode) {
    String hql = "select t.pdwhPubId from PdwhPubDuplicatePO t where t.hashTitle=:hashTitle ";
    List<Long> dupPubIds = super.createQuery(hql).setParameter("hashTitle", String.valueOf(strHashCode)).list();
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      dupPubIds = findExistsPubIds(dupPubIds);
    }
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Long> findExistsPubIds(List<Long> dupPubIds) {
    String hql = "select p.pubId from PubPdwhPO p where p.pubId in(:ids) and p.status = 0 ";
    return this.createQuery(hql).setParameterList("ids", dupPubIds).list();
  }

}
