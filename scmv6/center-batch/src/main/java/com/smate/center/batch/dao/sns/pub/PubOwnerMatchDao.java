package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubOwnerMatch;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果作者匹配表，用于确定用户与作者的关系.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubOwnerMatchDao extends SnsHibernateDao<PubOwnerMatch, Long> {

  /**
   * 保存作者匹配结果.
   * 
   * @param pubId
   * @param psnId
   * @param auSeq
   * @param auPos
   */
  public void savePubOwnerMatch(Long pubId, Long psnId, Integer auSeq, Integer auPos) {

    auSeq = auSeq == null ? 0 : auSeq;
    auPos = auPos == null ? 0 : auPos;
    PubOwnerMatch pom = super.get(pubId);
    if (pom == null) {
      pom = new PubOwnerMatch(pubId, psnId, auSeq, auPos);
    } else {
      pom.setPsnId(psnId);
      pom.setAuSeq(auSeq);
      pom.setAuPos(auPos);
    }
    super.save(pom);
  }

  /**
   * 删除数据.
   * 
   * @param pubId
   */
  public void delPubOwnerMatch(Long pubId) {

    String hql = "delete from PubOwnerMatch t where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  public PubOwnerMatch getPubOwnerMatch(Long pubId) {

    String hql = "from PubOwnerMatch t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 成果所有人是否与成果作者匹配.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public boolean isPubOwnerMatch(Long pubId, Long psnId) {

    String hql = "select count(pubId) from PubOwnerMatch t where t.pubId = ? and t.psnId = ? and t.auSeq > 0";
    Long count = super.findUnique(hql, pubId, psnId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
