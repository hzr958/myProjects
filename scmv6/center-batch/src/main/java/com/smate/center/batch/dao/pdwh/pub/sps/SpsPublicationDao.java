package com.smate.center.batch.dao.pdwh.pub.sps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAssign;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubDup;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubExtend;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPubExtendOld;
import com.smate.center.batch.model.pdwh.pub.sps.SpsPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * scopus基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SpsPublicationDao extends PdwhHibernateDao<SpsPublication, Long> {

  /**
   * 获取需要发送到单位的成果XML列表.
   * 
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SpsPubAssign> getNeedSendPubCacheBatch(int batchSize) {

    // 查询ID
    List<SpsPubAssign> pubAssigns =
        super.createQuery("from SpsPubAssign t where result = 1 and isSend = 0  ").setMaxResults(batchSize).list();
    if (pubAssigns == null || pubAssigns.size() == 0) {
      return null;
    }
    List<Long> pubIds = new ArrayList<Long>();
    for (SpsPubAssign assign : pubAssigns) {
      pubIds.add(assign.getPubId());
    }
    // 查询数据
    String hql = "from SpsPubExtend t where t.pubId in(:pubIds)";
    List<SpsPubExtend> pubExtends = super.createQuery(hql).setParameterList("pubIds", pubIds).list();
    for (SpsPubAssign assign : pubAssigns) {
      for (SpsPubExtend extend : pubExtends) {
        if (assign.getPubId().equals(extend.getPubId())) {
          assign.setXmlData(extend.getXmlData());
        }
      }
    }
    return pubAssigns;
  }

  /**
   * 获取需要删除发送到单位的成果列表.
   * 
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SpsPubAssign> getNeedDelPubCacheBatch(int batchSize) {

    // 查询ID
    List<SpsPubAssign> pubAssigns =
        super.createQuery("from SpsPubAssign t where result = 9 and isSend = 0  ").setMaxResults(batchSize).list();
    return pubAssigns;
  }

  /**
   * 加载标题以及作者.
   * 
   * @param xmlIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, Map<String, Object>> loadPubTitleAuthor(List<Object> pubIds) {
    String hql = "select pubId,title,authorNames from SpsPublication t where t.pubId in(:pubIds) ";
    List<Object[]> list = super.createQuery(hql).setParameterList("pubIds", pubIds).list();
    Map<Long, Map<String, Object>> cacheMap = new HashMap<Long, Map<String, Object>>();
    for (Object[] objs : list) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("xmlId", (Long) objs[0]);
      map.put("title", (String) objs[1]);
      map.put("authorNames", (String) objs[2]);
      cacheMap.put((Long) objs[0], map);
    }
    return cacheMap;
  }

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveSpsPubExtend(Long pubId, String xmlData) {

    SpsPubExtend extend = getSpsPubExtend(pubId);
    if (extend == null) {
      extend = new SpsPubExtend(pubId, xmlData);
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
  public SpsPubExtend getSpsPubExtend(Long pubId) {

    String hql = "from SpsPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  public SpsPubExtendOld getSpsPubExtendOld(Long pubId) {

    String hql = "from SpsPubExtendOld t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SpsPubExtend> getSpsPubExtends(List<Long> pubIds) {

    String hql = "from SpsPubExtend t where t.pubId in(:pubIds) ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveSpsPubDup(Long pubId, Long sourceIdHash, Long titleHash, Long unitHash) {

    SpsPubDup dup = new SpsPubDup(pubId, sourceIdHash, titleHash, unitHash);
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
  public SpsPubDup getSpsPubDup(Long sourceIdHash, Long titleHash, Long unitHash) {

    // source_id_hash
    SpsPubDup dup = null;
    if (unitHash != null && titleHash != null) {
      if (sourceIdHash == null) {
        sourceIdHash = 0l;
      }
      List<SpsPubDup> list =
          super.createQuery("from SpsPubDup t where (t.sourceIdHash = ? or t.unitHash = ?) and t.titleHash = ? ",
              sourceIdHash, unitHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    }
    return dup;
  }
}
