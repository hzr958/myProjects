package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPubKeywords20180511;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果关键词
 * 
 * @author aijiangbin
 * @date 2018年4月24日
 */
@Repository
public class PdwhPubKeywords20180511Dao extends PdwhHibernateDao<PdwhPubKeywords20180511, Long> {

  /**
   * 得到未处理的成果关键词
   * 
   * @param size
   * @return
   */
  public List<PdwhPubKeywords20180511> getNoDealPdwhPubKeywordsList(int size) {
    String hql = "from PdwhPubKeywords20180511  t where  t.status=0  ";
    List list = this.createQuery(hql).setMaxResults(size).list();
    return list;
  }

  public void deletePubKeywords(Long pubId, int type) {
    String hql = "delete PdwhPubKeywords20180511 t where t.pubId = :pubId and t.language = :type";
    super.createQuery(hql).setParameter("pubId", pubId).setParameter("type", type).executeUpdate();

  }

}
