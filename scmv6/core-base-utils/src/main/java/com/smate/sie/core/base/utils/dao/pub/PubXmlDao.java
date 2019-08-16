package com.smate.sie.core.base.utils.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.pub.SiePubXml;

/**
 * 成果XML.
 * 
 * @author ztt
 */
@Repository
public class PubXmlDao extends SieHibernateDao<SiePubXml, Long> {
  /**
   * 保存成果XML.
   * 
   * @param pubId 成果ID
   * @param xml 成果XML
   * @throws Exception Exception
   */
  public void saveXml(Long pubId, String xml) throws Exception {

    String hql = "update SiePubXml t set t.pubXml=? where t.pubId=?";
    int count = super.createQuery(hql, xml, pubId).executeUpdate();
    if (count == 0) {
      SiePubXml entity = new SiePubXml();
      entity.setPubId(pubId);
      entity.setPubXml(xml);
      super.save(entity);
    }
  }

  /**
   * 获取成果XML数据.
   * 
   * @param pubId
   * @return
   * @throws Exception
   */
  public SiePubXml getXmlByPubId(Long pubId) throws Exception {

    return super.get(pubId);
  }

  /**
   * 获取成果XML数据.
   * 
   * @param pubId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<SiePubXml> getBatchXmlByPubIds(Long[] pubIds) throws Exception {

    return super.getSession().createQuery("from SiePubXml t where t.pubId in(:pubIds)")
        .setParameterList("pubIds", pubIds).list();
  }
}
