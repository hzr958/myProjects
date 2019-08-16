package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果 成员dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubMemberDAO extends SnsHibernateDao<PubMemberPO, Long> {

  public List<PubMemberPO> findByPubId(Long pubId) {
    String hql = "from PubMemberPO p where p.pubId =:pubId  order by p.seqNo asc  nulls last ";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    return list;
  }

  /**
   * 根据pubId和psnId 获取成果成员对象
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public PubMemberPO getByPubIdAndPsnId(Long pubId, Long psnId) {
    String hql = "from PubMemberPO p where p.pubId =:pubId and p.psnId =:psnId";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId).list();
    if (!list.isEmpty()) {
      return (PubMemberPO) list.get(0);
    }
    return null;
  }

  public void deleteAllMember(Long pubId) {
    // 先删除
    String hql = "delete from PubMemberPO p where p.pubId=:pubId ";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
