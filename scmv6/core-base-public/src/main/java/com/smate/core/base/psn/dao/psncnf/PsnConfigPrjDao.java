package com.smate.core.base.psn.dao.psncnf;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrjPk;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人配置：项目
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnConfigPrjDao extends SnsHibernateDao<PsnConfigPrj, PsnConfigPrjPk> {

  @SuppressWarnings("unchecked")
  public List<PsnConfigPrj> gets(Long cnfId) {
    return super.createQuery(" from PsnConfigPrj where id.cnfId = ?", cnfId).list();
  }

  public PsnConfigPrj getBycnfIdAndPrjId(Long cnfId, Long prjId) {
    return (PsnConfigPrj) super.createQuery(" from PsnConfigPrj where id.cnfId = ? and id.prjId = ?", cnfId, prjId)
        .uniqueResult();
  }

  public void dels(Long cnfId) {
    super.createQuery("delete from PsnConfigPrj where id.cnfId=?", cnfId).executeUpdate();
  }

  public void delsByPrjId(Long prjId) {
    super.createQuery("delete from PsnConfigPrj where id.prjId=?", prjId).executeUpdate();
  }

  /**
   * 获取项目权限
   * 
   * @param cnfId
   * @param pubId
   * @return
   */
  public Integer getAnyUser(Long cnfId, Long prjId) {
    String sql = "from PsnConfigPrj t where t.id.cnfId=:cnfId and t.id.prjId=:prjId";
    PsnConfigPrj psnConfigPrj =
        (PsnConfigPrj) super.createQuery(sql).setParameter("cnfId", cnfId).setParameter("prjId", prjId).uniqueResult();
    if (psnConfigPrj != null) {
      return psnConfigPrj.getAnyUser();
    }
    return null;
  }

  /**
   * 用户是否有隐私的项目
   * 
   * @param cnfId
   * @return
   */
  public boolean hasPrivatePrj(Long cnfId, Long psnId) {
    String hql =
        "select count(1) from PsnConfigPrj p where p.anyUser <> 7 and p.id.cnfId = :cnfId and exists (select 1 from Project t where t.status = 0 and t.createPsnId = :psnId and t.id = p.id.prjId)";
    Long count = (Long) super.createQuery(hql).setParameter("cnfId", cnfId).setParameter("psnId", psnId).uniqueResult();
    boolean hasPrivatePrj = true;
    if (CommonUtils.compareLongValue(count, 0L)) {
      hasPrivatePrj = false;
    }
    return hasPrivatePrj;
  }
}
