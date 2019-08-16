package com.smate.center.task.dao.pdwh.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.EiPubDup;
import com.smate.center.task.model.pdwh.quartz.EiPubExtend;
import com.smate.center.task.model.pdwh.quartz.EiPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * ei基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class EiPublicationDao extends PdwhHibernateDao<EiPublication, Long> {

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveEiPubExtend(Long pubId, String xmlData) {

    EiPubExtend extend = getEiPubExtend(pubId);
    if (extend == null) {
      extend = new EiPubExtend(pubId, xmlData);
    } else {
      extend.setXmlData(xmlData);
    }
    super.getSession().save(extend);
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  public EiPubExtend getEiPubExtend(Long pubId) {

    String hql = "from EiPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveEiPubDup(Long pubId, Long sourceIdHash, Long titleHash, Long unitHash) {

    EiPubDup dup = new EiPubDup(pubId, sourceIdHash, titleHash, unitHash);
    super.getSession().save(dup);
  }

  /**
   * 获取重复的基准库数据.
   * 
   * @param sourceIdHash
   * @param titleHash
   * 
   * @param unitHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public EiPubDup getEiPubDup(Long sourceIdHash, Long titleHash, Long unitHash) {

    // source_id_hash
    EiPubDup dup = null;
    if (unitHash != null && titleHash != null) {
      if (sourceIdHash == null) {
        sourceIdHash = 0l;
      }
      List<EiPubDup> list =
          super.createQuery("from EiPubDup t where (t.sourceIdHash = ? or t.unitHash = ?) and t.titleHash = ? ",
              sourceIdHash, unitHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }

    }
    return dup;
  }

  /**
   * 加载标题以及作者.
   * 
   * @param xmlIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, Map<String, Object>> loadPubTitleAuthor(List<Object> pubIds) {
    String hql = "select pubId,title,authorNames from EiPublication t where t.pubId in(:pubIds) ";
    List<Object[]> list = super.createQuery(hql).setParameterList("pubIds", pubIds).list();
    Map<Long, Map<String, Object>> cacheMap = new HashMap<Long, Map<String, Object>>();
    for (Object[] objs : list) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("xmlId", objs[0]);
      map.put("title", objs[1]);
      map.put("authorNames", objs[2]);
      cacheMap.put((Long) objs[0], map);
    }
    return cacheMap;
  }
}
