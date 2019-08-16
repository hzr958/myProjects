package com.smate.center.task.dao.pub.seo;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.model.pub.seo.PubIndexThirdLevel;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubSeoThirdLevelSerachDao extends SnsHibernateDao<PubIndexThirdLevel, Long> {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void savePubIndexThirdLevel(PubIndexThirdLevel pubIndexThirdLevel) {
    super.saveOrUpdate(pubIndexThirdLevel);
  }



  public PubIndexThirdLevel fundPubIndexThirdLevel(Long pubId) {
    String hql = "from PubIndexThirdLevel t where t.pubId = :pubId ";
    return (PubIndexThirdLevel) createQuery(hql).setParameter("pubId", pubId).uniqueResult();

  }



  public void deletePubSeoThirdLevel(String code, int secondGroup, int thirdGroup) {
    String hql =
        "delete From PubIndexThirdLevel t where t.firstLetter=:firstLetter and t.secondGroup=:secondGroup and t.thirdGroup=:thirdGroup";
    createQuery(hql).setParameter("firstLetter", code).setParameter("secondGroup", secondGroup)
        .setParameter("thirdGroup", thirdGroup).executeUpdate();
  }



  public void truncateThirdLevel() {
    Session session = this.getSession();
    String sql = "truncate table pub_index_third_level";
    session.createSQLQuery(sql).executeUpdate();
  }
}
