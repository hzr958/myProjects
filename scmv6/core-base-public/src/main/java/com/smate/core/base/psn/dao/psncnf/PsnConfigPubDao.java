package com.smate.core.base.psn.dao.psncnf;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人配置：成果
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnConfigPubDao extends SnsHibernateDao<PsnConfigPub, PsnConfigPubPk> {
  @SuppressWarnings("unchecked")
  public List<PsnConfigPub> gets(Long cnfId) {
    return super.createQuery(" from PsnConfigPub where id.cnfId = ?", cnfId).list();
  }

  public void dels(Long cnfId) {
    super.createQuery("delete from PsnConfigPub where id.cnfId=?", cnfId).executeUpdate();
  }

  public boolean checkPubIsShow(Long pubId, Integer anyUser, Long confId) {
    String sql = "from PsnConfigPub where id.pubId=:pubId and anyUser>=:anyUser and id.cnfId=:confId";
    Object obj = super.createQuery(sql).setParameter("pubId", pubId).setParameter("anyUser", anyUser)
        .setParameter("confId", confId).uniqueResult();
    if (obj != null) {
      return true;
    }
    return false;
  }

  /**
   * 获取成果权限
   * 
   * @param cnfId
   * @param pubId
   * @return
   */
  public Integer getAnyUser(Long cnfId, Long pubId) {
    String sql = "from PsnConfigPub t where t.id.cnfId=:cnfId and t.id.pubId=:pubId";
    PsnConfigPub psnConfigPub =
        (PsnConfigPub) super.createQuery(sql).setParameter("cnfId", cnfId).setParameter("pubId", pubId).uniqueResult();
    if (psnConfigPub != null) {
      return psnConfigPub.getAnyUser();
    }
    return null;
  }

  public PsnConfigPub getPsnConfigPubByPubId(Long pubId) {
    String hql = " from   PsnConfigPub  p  where p.id.pubId =:pubId  ";
    List obj = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (obj != null && obj.size() > 0) {
      return (PsnConfigPub) obj.get(0);
    }
    return null;
  }

  /**
   * 用户是否有隐私的成果
   * 
   * @param cnfId
   * @return
   */
  public boolean hasPrivatePub(Long cnfId, Long psnId) {
    String hql =
        "select count(1) from PsnConfigPub p where p.anyUser <> 7 and p.id.cnfId = :cnfId and exists (select 1 from PubSnsPO t where t.status = 0 and t.createPsnId = :psnId and t.pubId = p.id.pubId)";
    Long count = (Long) super.createQuery(hql).setParameter("cnfId", cnfId).setParameter("psnId", psnId).uniqueResult();
    boolean hasPrivatePub = true;
    if (CommonUtils.compareLongValue(count, 0L)) {
      hasPrivatePub = false;
    }
    return hasPrivatePub;
  }
}
