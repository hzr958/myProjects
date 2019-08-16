package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubSituationPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人库成果被收录情况Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PubSituationDAO extends SnsHibernateDao<PubSituationPO, Long> {

  /**
   * 根据pubId和收录机构名获取成果收录情况对象
   * 
   * @param pubId 成果id
   * @param libraryName 收录机构名
   * @return
   */
  public PubSituationPO findByPubIdAndLibraryName(Long pubId, String libraryName) {
    String hql = "from PubSituationPO p where p.pubId=:pubId and p.libraryName=:libraryName";
    List list = this.createQuery(hql).setParameter("pubId", pubId)
        .setParameter("libraryName", libraryName.toUpperCase()).list();
    if (!list.isEmpty() && list.size() > 0) {
      return (PubSituationPO) list.get(0);
    }
    return null;
  }

  public List<PubSituationPO> listSituationByPsnId(Long psnId) {
    String hql =
        "FROM PubSituationPO p where exists( SELECT 1 FROM PsnPubPO t where t.pubId = p.pubId and  t.ownerPsnId =: psnId )";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public void deleteAll(Long pubId) {
    String hql = "delete from PubSituationPO p where p.pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubSituationPO> getByPubId(Long pubId) {
    String hql = "from PubSituationPO p where p.pubId=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }


}
