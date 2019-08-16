package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.PublicationAllDis;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 文献-领域对应表Dao
 * 
 * @author warrior
 * 
 */
@Repository
public class PublicationAllDisDao extends PdwhHibernateDao<PublicationAllDis, Long> {

  /**
   * 找出文献推荐，所有的领域Id
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public List<Map<String, Object>> findRefDisId(Long psnId) throws DaoException {
    String hql =
        "select t.disId as discId,count(*) as total from PublicationAllDis t where t.pubAllId in (select b.pubAllId from PsnPubAllRelated b where b.psnId = ?) group by t.disId";
    Query query = super.createQuery(hql, psnId);
    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    return query.list();
  }

  public List<Map<String, Object>> findSearchRefDisId(Long psnId) throws DaoException {
    String hql =
        "select t.disId as discId,count(t.id) as total from PublicationAllDis t where t.pubAllId in (select b.pubAllId from PsnPubAllSearch b where b.psnId = ?) group by t.disId";
    Query query = super.createQuery(hql, psnId);
    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    return query.list();
  }

  public void deletePuballDisByPuballId(Long puballId) {
    String hql = "delete from PublicationAllDis where pubAllId=?";
    super.createQuery(hql, puballId).executeUpdate();
  }
}
