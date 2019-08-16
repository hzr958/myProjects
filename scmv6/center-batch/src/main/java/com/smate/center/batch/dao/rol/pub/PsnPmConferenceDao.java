package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 用户确认成果会议记录.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnPmConferenceDao extends RolHibernateDao<PsnPmConference, Long> {

  /**
   * 获取确认过的成果会议记录.
   * 
   * @param nameHash
   * @param psnId
   * @return
   */
  public PsnPmConference getPsnPmConference(Integer nameHash, Long psnId) {
    String hql = "from PsnPmConference where nameHash = ? and psnId = ? ";
    List<PsnPmConference> list = super.find(hql, nameHash, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 根据人员获取记录.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmConference> getPsnPmConferenceList(Long psnId) {

    String ql = "from PsnPmConference where psnId = ?";
    return super.createQuery(ql, psnId).list();
  }

  /**
   * 根据人员ID删除记录.
   * 
   * @param psnId
   */
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from PsnPmConference where psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }
}
