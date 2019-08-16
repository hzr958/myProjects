package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.cnipr.CniprPubDup;
import com.smate.center.task.model.pdwh.pub.cnipr.CniprPubExtend;
import com.smate.center.task.model.pdwh.pub.cnipr.CniprPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * Cnipr基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CniprPublicationDao extends PdwhHibernateDao<CniprPublication, Long> {

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveCniprPubExtend(Long pubId, String xmlData) {

    CniprPubExtend extend = getCniprPubExtend(pubId);
    if (extend == null) {
      extend = new CniprPubExtend(pubId, xmlData);
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
  public CniprPubExtend getCniprPubExtend(Long pubId) {

    String hql = "from CniprPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveCniprPubDup(Long pubId, Long titleHash, Long patentHash, Long patentOpenHash) {

    CniprPubDup dup = new CniprPubDup(pubId, titleHash, patentHash, patentOpenHash);
    super.getSession().save(dup);
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveCniprPubDup(CniprPubDup dup) {

    super.getSession().saveOrUpdate(dup);
  }

  /**
   * 获取重复的基准库数据.
   * 
   * @param titleHash
   * 
   * @param unitHash
   * @return
   */
  @SuppressWarnings("unchecked")
  public CniprPubDup getCniprPubDup(Long titleHash, Long patentHash, Long patentOpenHash) {

    CniprPubDup dup = null;

    if (patentHash != null && titleHash != null) {
      List<CniprPubDup> list =
          super.createQuery("from CniprPubDup t where t.patentHash = ? and t.titleHash = ? ", patentHash, titleHash)
              .list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    } else if (patentOpenHash != null && titleHash != null) {
      List<CniprPubDup> list = super.createQuery("from CniprPubDup t where t.patentOpenHash = ? and t.titleHash = ? ",
          patentOpenHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    }
    return dup;
  }

  /**
   * 获取重复的基准库数据.
   * 
   * @param titleHash
   * 
   * @param unitHash
   * @return
   */
  public CniprPubDup getCniprPubDup(Long pubId) {

    CniprPubDup dup = super.findUnique("from CniprPubDup t where t.pubId = ? ", pubId);
    return dup;
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CniprPubExtend> getCniprPubExtends(List<Long> pubIds) {

    String hql = "from CniprPubExtend t where t.pubId in(:pubIds) ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }
}
