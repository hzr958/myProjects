package com.smate.center.open.dao.publication;

import com.smate.center.open.model.publication.PubAssignLogDetail;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 指派过程匹配内容Dao
 * 
 * @author zzx
 *
 */
@Repository
public class PubAssignLogDetailDao extends SnsHibernateDao<PubAssignLogDetail, Long> {



  public List<PubAssignLogDetail> findByPubIdAndPsnId(Long pubId , Long psnId) {
    String hql = "from PubAssignLogDetail t where t.pubId=:pubId and t.psnId =:psnId ";
    return this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId",psnId).list();
  }

}
