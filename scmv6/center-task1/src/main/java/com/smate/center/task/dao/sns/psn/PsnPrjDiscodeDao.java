package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.PsnPrjDiscode;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人申请基金代码.
 * 
 * @author liangguokeng
 * 
 */
@Repository
public class PsnPrjDiscodeDao extends SnsHibernateDao<PsnPrjDiscode, Long> {

  @SuppressWarnings("unchecked")
  public List<String> findPsnDiscodeByPsnId(Long psnId) throws DaoException {
    String hql = "select disCode from PsnPrjDiscode where psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
