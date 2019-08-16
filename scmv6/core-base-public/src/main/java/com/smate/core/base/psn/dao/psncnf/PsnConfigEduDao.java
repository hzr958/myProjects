package com.smate.core.base.psn.dao.psncnf;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigEduPk;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人配置：教育经历
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnConfigEduDao extends SnsHibernateDao<PsnConfigEdu, PsnConfigEduPk> {

  @SuppressWarnings("unchecked")
  public List<PsnConfigEdu> gets(Long cnfId) {
    return super.createQuery(" from PsnConfigEdu where id.cnfId = ?", cnfId).list();
  }

  public void dels(Long cnfId) {
    super.createQuery("delete from PsnConfigEdu where id.cnfId=?", cnfId).executeUpdate();
  }

  // 是否有人员教育经历配置信息
  public boolean hasPsnConfigWork(Long cnfId) {
    String hql = "select count(1) from PsnConfigEdu where id.cnfId = :cnfId";
    Long count = (Long) super.createQuery(hql).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }
}
