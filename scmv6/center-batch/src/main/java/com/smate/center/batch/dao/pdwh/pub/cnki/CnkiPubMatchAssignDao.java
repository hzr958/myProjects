package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAssign;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * CNKI成果拆分内容匹配结果表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class CnkiPubMatchAssignDao extends PdwhHibernateDao<CnkiPubMatchAssign, Long> {

  /**
   * 获取用户匹配到的成果.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public CnkiPubMatchAssign getCnkiPubMatchAssign(Long pubId, Long psnId) {
    String hql = "from CnkiPubMatchAssign t where t.pubId=? and t.psnId=? ";
    return super.findUnique(hql, pubId, psnId);
  }

  /**
   * 根据人员ID获取匹配的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  public List<Long> getPubIdListByPsnId(Long psnId) {
    String hql = "select pubId from CnkiPubMatchAssign t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  /**
   * 判断成果是否已拆分.
   * 
   * @param pubId
   * @return true-已拆分；false-未拆分.
   */
  public Boolean isExistSplitedPub(Long pubId) {
    String hql = "select count(pubId) from CnkiPubMatchAssign t where t.pubId=? ";
    Long totalCount = super.findUnique(hql, pubId);
    if (totalCount.longValue() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 检查是否已匹配到成果.
   * 
   * @param psnId
   * @return true-已匹配到；false-未匹配到.
   */
  public boolean isExistMatchedPub(Long psnId) {
    String hql = "select count(*) from CnkiPubMatchAssign t where t.psnId=? and t.status=0 ";
    Long count = super.findUnique(hql.toString(), psnId);
    return count.intValue() > 0;
  }

  /**
   * 删除已匹配到的待确认的成果.
   * 
   * @param psnId
   */
  public void deleteUnFirmPub(Long psnId) {
    String hql = "delete from CnkiPubMatchAssign t where t.psnId=? and t.status=0 ";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 获取已拆分成果表中的最大成果ID.
   * 
   * @return
   */
  public Long getMaxPubId() {
    String hql = "select max(pubId) from CnkiPubMatchAssign ";
    Long maxPubId = super.findUnique(hql);
    return maxPubId;
  }

  /**
   * 更新成果匹配结果记录的状态.
   * 
   * @param valueOf
   * @param pubId
   */
  public void updatePubStatus(Long psnId, Long pubId, Integer status) {
    String hql = "update CnkiPubMatchAssign t set t.status=? where t.psnId=? and t.pubId=? ";
    super.createQuery(hql, status, psnId, pubId).executeUpdate();
  }

  /**
   * 取出分值最大的7条成果
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubMatchAssign> findMaxScorePubId(Long psnId, int size) throws DaoException {
    return super.createQuery("from CnkiPubMatchAssign where psnId=? and status=0 order by score desc", psnId)
        .setMaxResults(size).list();
  }

  /**
   * 统计需要认领的成果数
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long countPub(Long psnId) throws DaoException {
    return super.countHqlResult("from CnkiPubMatchAssign where psnId=? and status=0", psnId);
  }

  /**
   * @param psnId
   * @param pubIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubMatchAssign> findListByPubIds(Long psnId, List<Long> pubIds) {
    return super.createQuery("from CnkiPubMatchAssign where psnId=:psnId and pubId in (:pubIds)")
        .setParameter("psnId", psnId).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存匹配合作者的值.
   * 
   * @param psnId
   * @param pubId
   * @param jnlNum
   * @param score
   */
  public void updateAssignCoName(Long pubId, Long psnId, Integer coFNameNum, Integer score) {
    String hql = "update CnkiPubMatchAssign t set t.coName=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, coFNameNum, score, pubId, psnId).executeUpdate();
  }

  /**
   * 保存匹配期刊的值.
   * 
   * @param psnId
   * @param pubId
   * @param jnlNum
   * @param score
   */
  public void updateAssignJnl(Long pubId, Long psnId, Integer journal, Integer score) {
    String hql = "update CnkiPubMatchAssign t set t.journal=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, journal, score, pubId, psnId).executeUpdate();
  }

  /**
   * 保存匹配关键词的值.
   * 
   * @param psnId
   * @param pubId
   * @param jnlNum
   * @param score
   */
  public void updateAssignKw(Long pubId, Long psnId, Integer matchedKwNum, Integer score) {
    String hql = "update CnkiPubMatchAssign t set t.keyword=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, matchedKwNum, score, pubId, psnId).executeUpdate();
  }
}
