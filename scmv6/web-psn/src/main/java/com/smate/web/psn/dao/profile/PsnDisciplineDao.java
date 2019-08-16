package com.smate.web.psn.dao.profile;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.PsnInfoDaoException;

/**
 * 人员专长数据层接口 PersonalDao
 * 
 * @author Administrator
 *
 */
@Repository
public class PsnDisciplineDao extends SnsHibernateDao<Personal, Long> {


  /**
   * 判断用户的课题专长是否存在.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public boolean isPsnDiscExit(Long psnId) throws PsnInfoDaoException {

    Long count = super.findUnique("select count(t.id.psnId) from PsnDiscipline t where t.id.psnId = ? ", psnId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取人员熟悉的学科领域ID列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnDiscId(Long psnId) {

    String hql = "select disId from  PsnDiscipline where psnId = ? ";
    return super.createQuery(hql, psnId).list();
  }
}
