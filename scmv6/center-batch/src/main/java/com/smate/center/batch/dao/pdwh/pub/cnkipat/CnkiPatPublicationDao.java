package com.smate.center.batch.dao.pdwh.pub.cnkipat;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubDup;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubExtend;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubExtendOld;
import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * CNKI专利基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPatPublicationDao extends PdwhHibernateDao<CnkiPatPublication, Long> {

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveCnkiPatPubExtend(Long pubId, String xmlData) {

    CnkiPatPubExtend extend = getCnkiPatPubExtend(pubId);
    if (extend == null) {
      extend = new CnkiPatPubExtend(pubId, xmlData);
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
  public CnkiPatPubExtend getCnkiPatPubExtend(Long pubId) {

    String hql = "from CnkiPatPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  public CnkiPatPubExtendOld getCnkiPatPubExtendOld(Long pubId) {

    String hql = "from CnkiPatPubExtendOld t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveCnkiPatPubDup(Long pubId, Long titleHash, Long patentHash, Long patentOpenHash) {
    CnkiPatPubDup dup = this.getCnkiPatPubDup(pubId);
    if (dup == null) {
      dup = new CnkiPatPubDup(pubId, titleHash, patentHash, patentOpenHash);
    } else {
      dup.setTitleHash(titleHash);
      dup.setPatentHash(patentHash);
      dup.setPatentOpenHash(patentOpenHash);
    }
    super.getSession().save(dup);
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveCnkiPatPubDup(CnkiPatPubDup dup) {

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
  public CnkiPatPubDup getCnkiPatPubDup(Long titleHash, Long patentHash, Long patentOpenHash) {

    CnkiPatPubDup dup = null;

    if (patentHash != null && titleHash != null) {
      List<CnkiPatPubDup> list =
          super.createQuery("from CnkiPatPubDup t where t.patentHash = ? and t.titleHash = ? ", patentHash, titleHash)
              .list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    } else if (patentOpenHash != null && titleHash != null) {
      List<CnkiPatPubDup> list =
          super.createQuery("from CnkiPatPubDup t where t.patentOpenHash = ? and t.titleHash = ? ", patentOpenHash,
              titleHash).list();
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
  public CnkiPatPubDup getCnkiPatPubDup(Long pubId) {

    CnkiPatPubDup dup = super.findUnique("from CnkiPatPubDup t where t.pubId = ? ", pubId);
    return dup;
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CnkiPatPubExtend> getCnkiPatPubExtends(List<Long> pubIds) {

    String hql = "from CnkiPatPubExtend t where t.pubId in(:pubIds) ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }
}
