package com.smate.center.merge.dao.thirdparth;

import com.smate.center.merge.model.sns.thirdparty.WeChatRelation;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 微信openid与科研之友openid关联Dao.
 * 
 * @author zk
 *
 */
@Repository
public class WeChatRelationDao extends SnsHibernateDao<WeChatRelation, Long> {
  /**
   * 根据smate的openId取消绑定.
   */
  public void cancelBindBySmateId(Long smateOpenId) {
    String hql = "delete from WeChatRelation w where w.smateOpenId=:smateOpenId";
    super.createQuery(hql).setParameter("smateOpenId", smateOpenId).executeUpdate();
  }

  /**
   * 根据SmateOpenId 取数据.
   * 
   * @return
   */
  public WeChatRelation getBySmateOpenId(Long smateOpenId) {
    String hql = "from WeChatRelation t where t.smateOpenId=:smateOpenId";
    List<WeChatRelation> list = super.createQuery(hql).setParameter("smateOpenId", smateOpenId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
