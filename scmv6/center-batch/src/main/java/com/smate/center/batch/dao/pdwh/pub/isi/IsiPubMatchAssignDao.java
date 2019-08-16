package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubMatchAssign;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ISI成果拆分内容匹配结果表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class IsiPubMatchAssignDao extends PdwhHibernateDao<IsiPubMatchAssign, Long> {

  /**
   * 根据人员ID获取匹配的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  public List<Long> getPubIdListByPsnId(Long psnId) {
    String hql = "select pubId from IsiPubMatchAssign t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  /**
   * 检查是否已匹配到成果.
   * 
   * @param psnId
   * @return true-已匹配到；false-未匹配到.
   */
  public boolean isExistMatchedPub(Long psnId) {
    String hql = "select count(*) from IsiPubMatchAssign t where t.psnId=? and t.status=0 ";
    Long count = super.findUnique(hql.toString(), psnId);
    return count.intValue() > 0;
  }

  /**
   * 获取人员匹配到的成果记录.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public IsiPubMatchAssign getIsiPubMatchAssign(Long pubId, Long psnId) {
    String hql = "from IsiPubMatchAssign t where t.pubId=? and t.psnId=? ";
    List<IsiPubMatchAssign> assignList = super.find(hql, pubId, psnId);
    if (CollectionUtils.isNotEmpty(assignList)) {
      return assignList.get(0);
    }
    return null;
  }

  /**
   * 判断成果是否已拆分.
   * 
   * @param pubId
   * @return true-已拆分；false-未拆分.
   */
  public Boolean isExistSplitedPub(Long pubId) {
    String hql = "select count(pubId) from IsiPubMatchAssign t where t.pubId=? and t.status=0 ";
    Long totalCount = super.findUnique(hql, pubId);
    if (totalCount.longValue() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 更新成果匹配结果记录的状态.
   * 
   * @param valueOf
   * @param pubId
   */
  public void updatePubStatus(Long psnId, Long pubId, Integer status) {
    String hql = "update IsiPubMatchAssign t set t.status=? where t.psnId=? and t.pubId=? ";
    super.createQuery(hql, status, psnId, pubId).executeUpdate();
  }

  /**
   * 取出分值最大的7条待确认成果
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubMatchAssign> findMaxScorePubId(Long psnId, int size) throws DaoException {
    return super.createQuery("from IsiPubMatchAssign where psnId=? and status=0 order by score desc", psnId)
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
    return super.countHqlResult("from IsiPubMatchAssign where psnId=? and status=0", psnId);
  }

  /**
   * @param psnId
   * @param pubIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubMatchAssign> findListByPubIds(Long psnId, List<Long> pubIds) {
    return super.createQuery("from IsiPubMatchAssign where psnId=:psnId and pubId in (:pubIds) ")
        .setParameter("psnId", psnId).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存成果匹配结果记录.
   * 
   * @param assign
   */
  public void savePubMatchAssign(IsiPubMatchAssign assign) {
    if (assign.getAssignId() != null) {
      super.getSession().update(assign);
    } else {
      IsiPubMatchAssign massign = this.getIsiPubMatchAssign(assign.getPsnId(), assign.getPsnId());
      if (massign != null) {
        massign.setAthId(assign.getAthId());
        massign.setAthPos(assign.getAthPos());
        massign.setAthSeq(assign.getAthSeq());
        massign.setCoEmail(assign.getCoEmail());
        massign.setCoFName(assign.getCoFName());
        massign.setCoIName(assign.getCoIName());
        massign.setEmail(assign.getEmail());
        massign.setfName(assign.getfName());
        massign.setiName(assign.getiName());
        massign.setJournal(assign.getJournal());
        massign.setKeyword(assign.getKeyword());
        massign.setScore(assign.getScore());
        massign.setStatus(assign.getStatus());
        super.getSession().update(massign);
      } else {
        super.getSession().save(assign);
      }
    }
  }

  /**
   * 删除推荐的未确认的成果.
   * 
   * @param psnId
   */
  public void deleteUnFirmPub(Long psnId) {
    String hql = "delete from IsiPubMatchAssign t where t.psnId=? and t.status=0 ";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 保存匹配合作者邮件的值.
   * 
   * @param psnId
   * @param pubId
   * @param emailMatchedNum
   * @param score
   */
  public void updateAssignCoEmail(Long psnId, Long pubId, Integer emailMatchedNum, Integer score) {
    String hql = "update IsiPubMatchAssign t set t.coEmail=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, emailMatchedNum, score, pubId, psnId).executeUpdate();
  }

  /**
   * 保存匹配合作者名称的值.
   * 
   * @param psnId
   * @param pubId
   * @param coFNameNum
   * @param coINameNum
   * @param score
   */
  public void updateAssignCoName(Long psnId, Long pubId, Integer coFNameNum, Integer coINameNum, Integer score) {
    String hql = "update IsiPubMatchAssign t set t.coIName=?,t.coFName=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, coINameNum, coFNameNum, score, pubId, psnId).executeUpdate();
  }

  /**
   * 保存匹配期刊的值.
   * 
   * @param psnId
   * @param pubId
   * @param jnlNum
   * @param score
   */
  public void updateAssignJnl(Long psnId, Long pubId, Integer jnlNum, Integer score) {
    String hql = "update IsiPubMatchAssign t set t.journal=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, jnlNum, score, pubId, psnId).executeUpdate();
  }

  /**
   * 保存匹配关键词的值.
   * 
   * @param psnId
   * @param pubId
   * @param matchedKwNum
   * @param score
   */
  public void updateAssignKw(Long psnId, Long pubId, Integer matchedKwNum, Integer score) {
    String hql = "update IsiPubMatchAssign t set t.keyword=?,t.score=? where t.pubId=? and t.psnId=? ";
    super.createQuery(hql, matchedKwNum, score, pubId, psnId).executeUpdate();
  }
}
