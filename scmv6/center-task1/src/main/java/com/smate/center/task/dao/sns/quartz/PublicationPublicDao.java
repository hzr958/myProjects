package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 成果-公共Dao类
 * 
 * @author tj
 * @since 6.0.1
 */
@Repository
public class PublicationPublicDao extends SnsHibernateDao<Publication, Long> {


  /**
   * 通过成果id,获取成果标题，来源，作者，来源库信息
   */
  public Publication getPubById(Long pubId) {
    String hql =
        "select  new Publication(p.pubId,p.zhTitle,p.enTitle,p.briefDesc,p.briefDescEn,p.authorNames,p.fullTextField,p.fullTextUrl,p.sourceDbId)  from Publication p where p.pubId=:pubId";
    return (Publication) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }


}
