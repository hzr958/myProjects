package com.smate.web.v8pub.dao.sns;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubSituationPO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        "FROM PubSituationPO p where exists( SELECT 1 FROM PsnPubPO t where t.pubId = p.pubId and  t.ownerPsnId =: psnId ) "
            + " and p.sitStatus = 1";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 查找成果的收录情况
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSituationPO> findPubSituationsByPubId(Long pubId) {
    String hql = "from PubSituationPO p where p.pubId = :pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  /**
   * 查找成果ISIId
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public String findPubISIId(Long pubId) {
    String hql =
        "select distinct srcId from PubSituationPO where pubId = :pubId and srcDbId in (2, 15, 16, 17) and srcId is not null";
    List<String> srcIds = super.createQuery(hql).setParameter("pubId", pubId).list();
    if (CollectionUtils.isNotEmpty(srcIds)) {
      return srcIds.get(0);
    }
    return "";
  }

  public void deleteAll(Long pubId) {
    String hql = "delete from PubSituationPO t where t.pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
