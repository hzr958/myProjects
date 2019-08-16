package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAssign;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubDup;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExtend;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExtendOld;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubSourceDb;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPublication;
import com.smate.center.batch.service.pub.LibraryPubService;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * isi基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class IsiPublicationDao extends PdwhHibernateDao<IsiPublication, Long> {

  /**
   * 获取需要发送到单位的成果XML列表.
   * 
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubAssign> getNeedSendPubCacheBatch(int batchSize) {

    // 查询ID
    List<IsiPubAssign> pubAssigns =
        super.createQuery("from IsiPubAssign t where result = 1 and isSend = 0  ").setMaxResults(batchSize).list();
    if (pubAssigns == null || pubAssigns.size() == 0) {
      return null;
    }
    List<Long> pubIds = new ArrayList<Long>();
    for (IsiPubAssign assign : pubAssigns) {
      pubIds.add(assign.getPubId());
    }
    // 查询数据
    String hql = "from IsiPubExtend t where t.pubId in(:pubIds)";
    List<IsiPubExtend> pubExtends = super.createQuery(hql).setParameterList("pubIds", pubIds).list();
    for (IsiPubAssign assign : pubAssigns) {
      for (IsiPubExtend extend : pubExtends) {
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
  public List<IsiPubAssign> getNeedDelPubCacheBatch(int batchSize) {

    // 查询ID
    List<IsiPubAssign> pubAssigns =
        super.createQuery("from IsiPubAssign t where result = 9 and isSend = 0  ").setMaxResults(batchSize).list();
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
    String hql = "select pubId,title,authorNames from IsiPublication t where t.pubId in(:pubIds) ";
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

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveIsiPubExtend(Long pubId, String xmlData) {

    IsiPubExtend extend = getIsiPubExtend(pubId);
    if (extend == null) {
      extend = new IsiPubExtend(pubId, xmlData);
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
  public IsiPubExtend getIsiPubExtend(Long pubId) {

    String hql = "from IsiPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  public IsiPubExtendOld getIsiPubExtendOld(Long pubId) {

    String hql = "from IsiPubExtendOld t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubExtend> getIsiPubExtends(List<Long> pubIds) {

    String hql = "from IsiPubExtend t where t.pubId in(:pubIds) ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveIsiPubDup(Long pubId, Long sourceIdHash, Long titleHash, Long unitHash) {
    IsiPubDup dup = this.getIsiPubDup(pubId);
    if (dup == null) {
      dup = new IsiPubDup(pubId, sourceIdHash, titleHash, unitHash);
    } else {
      dup.setSourceIdHash(sourceIdHash);
      dup.setTitleHash(titleHash);
      dup.setUnitHash(unitHash);
    }
    super.getSession().save(dup);
  }

  /**
   * 获取查重数据
   * 
   * @param pubId
   * @return
   */
  public IsiPubDup getIsiPubDup(Long pubId) {

    String hql = "from IsiPubDup t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
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
  public IsiPubDup getIsiPubDup(Long sourceIdHash, Long titleHash, Long unitHash) {

    // source_id_hash
    IsiPubDup dup = null;
    if (unitHash != null && titleHash != null) {
      if (sourceIdHash == null) {
        sourceIdHash = 0l;
      }
      List<IsiPubDup> isiPubDups =
          super.createQuery("from IsiPubDup t where (t.sourceIdHash = ? or t.unitHash = ?) and t.titleHash = ? ",
              sourceIdHash, unitHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(isiPubDups)) {
        dup = isiPubDups.get(0);
      }
    }
    return dup;
  }

  /**
   * 保存isi文献库收录情况，只管=1的引用.
   * 
   * @param pubId
   * @param sci
   * @param ssci
   * @param istp
   */
  public void saveIsiPubSourceDb(Long pubId, Integer sci, Integer ssci, Integer istp) {

    String hql = "from IsiPubSourceDb t where t.pubId = ? ";
    IsiPubSourceDb pubSourceDb = super.findUnique(hql, pubId);
    if (pubSourceDb == null) {
      pubSourceDb = new IsiPubSourceDb(pubId);
    }
    if (sci != null && sci == 1) {
      pubSourceDb.setSci(sci);
    }
    if (ssci != null && ssci == 1) {
      pubSourceDb.setSsci(ssci);
    }
    if (istp != null && istp == 1) {
      pubSourceDb.setIstp(istp);
    }
    super.getSession().save(pubSourceDb);
  }

  /**
   * 获取待解析的成果列表.
   * 
   * @param minPubId 获取成果ID的最小值.
   * @param size 一次获取成果记录数.
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubExtend> getIsiPubExtendList(List<Long> pubIdList) {
    String hql = "from IsiPubExtend t where t.pubId in (:pubId) order by pubId ";
    List<Object> params = new ArrayList<Object>();
    return super.createQuery(hql, params.toArray()).list();
  }

  /**
   * 获取待处理的成果列表.
   * 
   * @param minPubId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getIsiPubIdList(Long minPubId, int size) {
    String hql = "select t.pubId from IsiPubExtend t where t.pubId>? order by pubId asc ";
    return super.createQuery(hql, minPubId).setMaxResults(size).list();
  }

  /**
   * 获取遗漏未拆分的成果任务列表.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMissPubIdTask(Integer maxSize) {
    String pubSource = LibraryPubService.PUB_LIBRARY_ISI;
    String hql =
        "select t.pubId from IsiPubExtend t where not exists (select 1 from PubSplitXmlTask p where t.pubId=p.pubId and p.result='1' and p.pubSource=?) order by t.pubId ";
    return super.createQuery(hql, pubSource).setMaxResults(maxSize).list();
  }

  public Long queryPubPdwhIdBySourceId(String sourceId) {
    return (Long) super.createQuery("select t.pubId from IsiPublication t where t.sourceId = ?", sourceId)
        .setMaxResults(1).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<IsiPublication> queryIsiPublicationList(List<Long> pubIdList) {
    String hql = "from IsiPublication t where t.pubId in (:pubIdList)";
    return super.createQuery(hql).setParameterList("pubIdList", pubIdList).list();
  }
}
