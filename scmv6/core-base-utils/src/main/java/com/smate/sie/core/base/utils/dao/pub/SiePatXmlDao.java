package com.smate.sie.core.base.utils.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.pub.SiePatXml;

/**
 * 专利XMLDao.
 * 
 * @author jszhou
 *
 */
@Repository
public class SiePatXmlDao extends SieHibernateDao<SiePatXml, Long> {
  /**
   * 保存专利XML.
   * 
   * @param patId 专利ID
   * @param xml 专利XML
   * @throws Exception Exception
   */
  public void saveXml(Long patId, String xml) throws Exception {

    String hql = "update SiePatXml t set t.patXml=? where t.patId=?";
    int count = super.createQuery(hql, xml, patId).executeUpdate();
    if (count == 0) {
      SiePatXml entity = new SiePatXml();
      entity.setPatId(patId);
      entity.setPatXml(xml);
      super.save(entity);
    }
  }

  /**
   * 获取成果XML数据.
   * 
   * @param patId
   * @return
   * @throws Exception
   */
  public SiePatXml getXmlByPatId(Long patId) throws Exception {

    return super.get(patId);
  }

  /**
   * 获取成果XML数据.
   * 
   * @param patId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<SiePatXml> getBatchXmlByPatIds(Long[] patIds) throws Exception {

    return super.getSession().createQuery("from SiePatXml t where t.patId in(:patIds)")
        .setParameterList("patIds", patIds).list();
  }
}
