package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubAssignScore;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果指派分数DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignScoreDao extends RolHibernateDao<PubAssignScore, Long> {

  /**
   * 获取成果指派分数.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public PubAssignScore getPubAssignScore(Long pubId, Long psnId) {

    String hql = "from PubAssignScore where pubId = ? and psnId = ? ";
    List<PubAssignScore> list = super.find(hql, pubId, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 删除人员所在机构成果匹配分数.
   * 
   * @param psnId
   * @param insId
   */
  public void remove(Long psnId, Long insId) {

    String hql = "delete from PubAssignScore t where t.psnId = ? and t.insId = ? ";
    super.createQuery(hql, psnId, insId).executeUpdate();
  }

  /**
   * 删除成果匹配分数.
   * 
   * @param pubId
   */
  public void removeByPubId(Long pubId) {
    String hql = "delete from PubAssignScore t where t.pubId = ?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 删除人员成果匹配分数.
   * 
   * @param pubId
   */
  public void removePubPsnAssignScore(Long pubId, Long psnId) {
    String hql = "delete from PubAssignScore t where t.pubId = ? and t.psnId = ? ";
    super.createQuery(hql, pubId, psnId).executeUpdate();
  }

  /**
   * 获取成果指派最大分数.
   * 
   * @param pubId
   * @param seqNo
   * @return
   */
  public Float getMaxScorePubPsnRol(Long pubId, Integer seqNo) {

    String hql = "select max(total) from PubAssignScore t where t.pubId = ? and seqNo = ? ";
    return super.findUnique(hql, pubId, seqNo);
  }

  public List<PubAssignScore> queryPubAssignScoreByPsnId(Long psnId) throws DaoException {
    return super.find("from PubAssignScore t where t.psnId=?", psnId);
  }
}
