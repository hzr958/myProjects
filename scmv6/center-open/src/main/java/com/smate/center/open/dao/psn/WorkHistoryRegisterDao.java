package com.smate.center.open.dao.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.psn.WorkHistoryRegister;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 工作经历
 * 
 * @author tsz
 *
 */
@Repository
public class WorkHistoryRegisterDao extends SnsHibernateDao<WorkHistoryRegister, Long> {


  public List<WorkHistoryRegister> findWorkByPsnId(Long psnId) {
    String hql = "from WorkHistoryRegister where psnId=?";
    return find(hql, psnId);
  }
}
