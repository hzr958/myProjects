package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportThirdPsns;
import com.smate.sie.center.task.model.ImportThirdPsnsPK;

/**
 * 第三方人员信息DAO.
 * 
 * @author xys
 *
 */
@Repository
public class ImportThirdPsnsDao extends SieHibernateDao<ImportThirdPsns, ImportThirdPsnsPK> {

  /**
   * 获取待导入数据.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ImportThirdPsns> getThirdPsnsNeedImport(int maxSize) {
    String hql = "from ImportThirdPsns t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }
}
