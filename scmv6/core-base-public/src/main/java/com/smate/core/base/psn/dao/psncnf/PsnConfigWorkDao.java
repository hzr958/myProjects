package com.smate.core.base.psn.dao.psncnf;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.model.psncnf.PsnConfigWorkPk;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人配置：工作经历
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnConfigWorkDao extends SnsHibernateDao<PsnConfigWork, PsnConfigWorkPk> {

  @SuppressWarnings("unchecked")
  public List<PsnConfigWork> gets(Long cnfId) {
    return super.createQuery(" from PsnConfigWork where id.cnfId = ?", cnfId).list();
  }

  public void dels(Long cnfId) {
    super.createQuery("delete from PsnConfigWork where id.cnfId=?", cnfId).executeUpdate();
  }

  public Long queryPsnConfigWorkByIns(Long psnId, Long insId, String insNameZh, String insNameEn, Integer anyView) {
    String hql =
        "from PsnConfigWork w,PsnWork h where w.id.workId = h.workId and h.psnId = ? and w.anyView >= ? and ( h.insId = ? or (h.insName = ? or h.insName = ?))";
    return super.countHqlResult(hql, psnId, anyView, insId == null ? -1 : insId, insNameZh, insNameEn);
  }

  // 是否有人员工作经历配置信息
  public boolean hasPsnConfigWork(Long cnfId) {
    String hql = "select count(1) from PsnConfigWork where id.cnfId = :cnfId";
    Long count = (Long) super.createQuery(hql).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }
}
