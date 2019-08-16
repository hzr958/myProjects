package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果XMLDao.
 * 
 * @author yamingd
 */
@Repository
public class PublicationXmlDao extends SnsHibernateDao<PublicationXml, Long> {

  /**
   * 保存成果XML.
   * 
   * @param pubId 成果ID @param xml 成果XML @throws
   * 
   */
  public void saveXml(Long pubId, String xml) {

    String hql = "update PublicationXml t set t.xmlData=? where t.id=?";
    int count = super.createQuery(hql, xml, pubId).executeUpdate();
    if (count == 0) {
      PublicationXml entity = new PublicationXml();
      entity.setId(pubId);
      entity.setXmlData(xml);
      super.save(entity);
    }
  }

  /**
   * 获取成果XML数据.
   * 
   * @param pubId @return @throws
   */
  public PublicationXml getXmlByPubId(Long pubId) {

    return super.get(pubId);
  }

  /**
   * 获取成果XML数据.
   * 
   * @param pubId @return @throws
   */
  @SuppressWarnings("unchecked")
  public List<PublicationXml> getBatchXmlByPubIds(Long[] pubIds) {

    return super.getSession().createQuery("from PublicationXml t where t.id in(:pubIds)")
        .setParameterList("pubIds", pubIds).list();
  }
}
