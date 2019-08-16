package com.smate.center.batch.dao.pdwh.pub;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.model.pdwh.pub.PdwhPubAuthorSnsPsnRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果作者和sns人员对应关系 dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhPubAuthorSnsPsnRecordDao extends PdwhHibernateDao<PdwhPubAuthorSnsPsnRecord, Long> {
  /**
   * 根据pubId删除记录
   * 
   * @param pubId
   * @author LIJUN
   * @date 2018年3月20日
   */
  public void deleteRecordByPubId(Long pubId) {
    String hql = "delete PdwhPubAuthorSnsPsnRecord where pubId=:pubId";

    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 获取被确认的记录
   * 
   * @param pubId
   * @param matchName
   * @return
   * @author LIJUN
   * @date 2018年3月30日
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPubAuthorSnsPsnRecord> findRecByPubIdAndName(Long pubId, String matchName) {
    String hql = "from PdwhPubAuthorSnsPsnRecord t where t.pubId=:pubId and t.psnName=:matchName and t.status in(3,4) "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return super.createQuery(hql).setParameter("pubId", pubId).setParameter("matchName", matchName).list();
  }

  /**
   * 更新记录
   * 
   * @param id
   * @author LIJUN
   * @date 2018年3月29日
   */
  public void updateTime(Long id) {
    String hql = "update PdwhPubAuthorSnsPsnRecord set updateTime=:updateTime where id=:id ";
    super.createQuery(hql).setParameter("updateTime", new java.util.Date()).setParameter("id", id).executeUpdate();
  }

  /**
   * 删除不在地址列表中的地址
   * 
   * @param pubId
   * @param list
   * @author LIJUN
   * @date 2018年3月29日
   */
  public void deleteRecordByPubIdAndNameList(Long pubId, List<String> list) {
    String hql = "delete PdwhPubAuthorSnsPsnRecord where pubId=:pubId and psnName not in (:psnNameList)";
    super.createQuery(hql).setParameter("pubId", pubId).setParameterList("psnNameList", list).executeUpdate();

  }

  /**
   * 删除没有被用户确认的记录
   * 
   * @param pubId
   * @author LIJUN
   * @date 2018年3月30日
   */
  public void deleteUnconfirmedRecord(Long pubId) {
    String hql = "delete PdwhPubAuthorSnsPsnRecord where pubId=:pubId and status not in(3,4)";
    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 查询记录
   * 
   * @param pubId
   * @param psnId
   * @param insId
   * @param psnName
   * @param nameType
   * @return
   * @author LIJUN
   * @date 2018年5月23日
   */
  public PdwhPubAuthorSnsPsnRecord getPsnRecord(Long pubId, Long psnId, Long insId, String psnName, Integer nameType) {
    String hql = "select new PdwhPubAuthorSnsPsnRecord(t.pubId, t.psnId, t.nameType,t.psnName) from "
        + " PdwhPubAuthorSnsPsnRecord t where t.pubId=:pubId and t.psnId =:psnId and t.insId=:insId "
        + " and t.psnName=:psnName and t.nameType=:nameType "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return (PdwhPubAuthorSnsPsnRecord) super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId)
        .setParameter("psnId", psnId).setParameter("nameType", nameType).setParameter("psnName", psnName)
        .uniqueResult();
  }

  /**
   * 获取被确认的记录
   * 
   * @param pubId
   * @param matchName
   * @return
   * @author LIJUN
   * @date 2018年3月30日
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPubAuthorSnsPsnRecord> findConfirmRecByPubIdAndName(Long pubId, String matchName) {
    String hql = "from PdwhPubAuthorSnsPsnRecord t where t.pubId=:pubId and t.psnName=:matchName and t.status in(3,4) "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId)";
    return super.createQuery(hql).setParameter("pubId", pubId).setParameter("matchName", matchName).list();
  }

  public void saveNewPsnRecord(Long pubId, Long psnId, String matchName, Long insId, String insName, int status,
      Date date, Integer type) {
    super.save(new PdwhPubAuthorSnsPsnRecord(pubId, psnId, matchName, insId, insName, status, date, type));

  }

  /**
   * 新开启事务保存
   *
   * @author LIJUN
   * @date 2018年7月6日
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithNewTransaction(PdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord) {
    super.save(pdwhPubAuthorSnsPsnRecord);
  }

}
