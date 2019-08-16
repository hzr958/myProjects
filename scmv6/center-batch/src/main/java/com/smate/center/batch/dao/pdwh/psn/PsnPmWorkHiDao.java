package com.smate.center.batch.dao.pdwh.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmWorkHi;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配-用户工作经历表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmWorkHiDao extends PdwhHibernateDao<PsnPmWorkHi, Long> {

  /**
   * 获取用户工作经历记录.
   * 
   * @param psnId
   * @return
   */
  public List<PsnPmWorkHi> getPsnPmWorkHiList(Long psnId) {
    String hql = "from PsnPmWorkHi t where t.psnId=? ";
    return super.find(hql, psnId);
  }

  public void deleteAllWorkHiByPsnId(Long psnId) {
    String hql = "delete from PsnPmWorkHi t where t.psnId = ?";
    super.createQuery(hql, new Object[] {psnId}).executeUpdate();
  }

  public PsnPmWorkHi findByPsnIdAndWorkId(Long psnId, Long workId) {
    String hql = "from PsnPmWorkHi t where t.psnId=? and t.workId=?";
    return super.findUnique(hql, psnId, workId);
  }

  public void deleteNotExists(Long psnId, List<Long> workIdList) {
    String hql = "delete from PsnPmWorkHi t where t.psnId=:psnId and t.workId not in(:workIdList)";
    super.createQuery(hql).setParameter("psnId", psnId).setParameterList("workIdList", workIdList).executeUpdate();
  }
}
