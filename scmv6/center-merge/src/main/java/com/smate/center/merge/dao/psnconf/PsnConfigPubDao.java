package com.smate.center.merge.dao.psnconf;

import com.smate.center.merge.model.sns.psnconf.PsnConfigPub;
import com.smate.center.merge.model.sns.psnconf.PsnConfigPubPk;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 个人配置：成果.
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


  public void updateConf(Long newcnfId, Long oldconfId, Long pubId) {
    super.update("update  PSN_CONFIG_PUB set cnf_Id=? where CNF_ID=? and pub_id=? ",
        new Object[] {newcnfId, oldconfId, pubId});
  }



  /**
   * 获取权限.
   * 
   * @param pubId not null
   * @return
   */
  public PsnConfigPub getPsnConfigPubByPubId(Long pubId) {
    String hql = " from   PsnConfigPub  p  where p.id.pubId =:pubId  ";
    List obj = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (obj != null && obj.size() > 0) {
      return (PsnConfigPub) obj.get(0);
    }
    return null;
  }
}
