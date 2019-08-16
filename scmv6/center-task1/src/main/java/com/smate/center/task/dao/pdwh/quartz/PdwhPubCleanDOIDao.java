package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPubCleanDOI;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhPubCleanDOIDao extends PdwhHibernateDao<PdwhPubCleanDOI, Long> {
  /**
   * 通过清理的doihash获取pubId
   */
  public Long getPubIdByCleanDoiHash(Long doiHash) {
    String hql = " select pubId from PdwhPubCleanDOI where cleanDoiHash=:cleanDoiHash order by pubId desc";

    @SuppressWarnings("unchecked")
    List<Long> list = super.createQuery(hql).setParameter("cleanDoiHash", doiHash).list();

    if (CollectionUtils.isEmpty(list)) {
      return 0L;
    }
    return list.get(0);
  }

  public void updatedoihash(Long pubId, Long doiHash, String cleanDoi) {

    String hql = "update PdwhPubCleanDOI set cleanDoi=:cleanDoi,cleanDoiHash=:cleanDoiHash where pubId=:pubId";

    super.createQuery(hql).setParameter("cleanDoi", cleanDoi).setParameter("cleanDoiHash", doiHash)
        .setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 获取需要生成doihash的id
   */
  public List<Long> getPubIdData(Integer size) {
    String hql = " select pubId from PdwhPubCleanDOI where cleanDoiHash is null";
    return super.createQuery(hql).setMaxResults(size).list();

  }

}
