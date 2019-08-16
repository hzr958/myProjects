package com.smate.center.task.dao.pdwh.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.CnkiPubAssign;
import com.smate.center.task.model.pdwh.quartz.CnkiPubDup;
import com.smate.center.task.model.pdwh.quartz.CnkiPubExtend;
import com.smate.center.task.model.pdwh.quartz.CnkiPublication;
import com.smate.center.task.single.service.pub.LibraryPubService;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * cnki基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPublicationDao extends PdwhHibernateDao<CnkiPublication, Long> {

  /**
   * 获取需要发送到单位的成果XML列表.
   * 
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubAssign> getNeedSendPubCacheBatch(int batchSize) {

    // 查询ID
    List<CnkiPubAssign> pubAssigns =
        super.createQuery("from CnkiPubAssign t where result = 1 and isSend = 0  ").setMaxResults(batchSize).list();
    if (pubAssigns == null || pubAssigns.size() == 0) {
      return null;
    }
    List<Long> pubIds = new ArrayList<Long>();
    for (CnkiPubAssign assign : pubAssigns) {
      pubIds.add(assign.getPubId());
    }
    // 查询数据
    String hql = "from CnkiPubExtend t where t.pubId in(:pubIds)";
    List<CnkiPubExtend> pubExtends = super.createQuery(hql).setParameterList("pubIds", pubIds).list();
    for (CnkiPubAssign assign : pubAssigns) {
      for (CnkiPubExtend extend : pubExtends) {
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
  public List<CnkiPubAssign> getNeedDelPubCacheBatch(int batchSize) {

    // 查询ID
    List<CnkiPubAssign> pubAssigns =
        super.createQuery("from CnkiPubAssign t where result = 9 and isSend = 0  ").setMaxResults(batchSize).list();
    return pubAssigns;
  }

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveCnkiPubExtend(Long pubId, String xmlData) {

    CnkiPubExtend extend = getCnkiPubExtend(pubId);
    if (extend == null) {
      extend = new CnkiPubExtend(pubId, xmlData);
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
  public CnkiPubExtend getCnkiPubExtend(Long pubId) {

    String hql = "from CnkiPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubExtend> getCnkiPubExtends(List<Long> pubIds) {

    String hql = "from CnkiPubExtend t where t.pubId in(:pubIds) ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param unitHash
   */
  public void saveCnkiPubDup(Long pubId, Long titleHash, Long unitHash) {

    CnkiPubDup dup = new CnkiPubDup(pubId, titleHash, unitHash);
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
  public CnkiPubDup getCnkiPubDup(Long titleHash, Long unitHash) {

    // source_id_hash
    CnkiPubDup dup = null;
    if (unitHash != null && titleHash != null) {
      List<CnkiPubDup> list =
          super.createQuery("from CnkiPubDup t where  t.unitHash = ? and t.titleHash = ? ", unitHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    }
    return dup;
  }

  /**
   * 获取待解析的成果列表.
   * 
   * @param pubIdList 成果ID列表.
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPubExtend> getCnkiPubExtendList(List<Long> pubIdList) {
    String hql = "from CnkiPubExtend t where t.pubId in (:pubId) order by pubId ";
    List<Object> params = new ArrayList<Object>();
    return super.createQuery(hql, params.toArray()).list();
  }

  /**
   * 获取待解析的成果ID列表.
   * 
   * @param minPubId 获取成果ID的最小值.
   * @param size 一次获取成果记录数.
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCnkiPubIdList(Long minPubId, int size) {
    String hql = "select t.pubId from CnkiPubExtend t where t.pubId>? order by pubId ";
    return super.createQuery(hql, minPubId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getMissPubIdTask(Integer maxSize) {
    String pubSource = LibraryPubService.PUB_LIBRARY_CNKI;
    String hql =
        "select t.pubId from CnkiPubExtend t where not exists (select 1 from PubSplitXmlTask p where t.pubId=p.pubId and p.result='1' and p.pubSource=?) order by t.pubId ";
    return super.createQuery(hql, pubSource).setMaxResults(maxSize).list();
  }
}
