package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PubMedPubDup;
import com.smate.center.task.model.pdwh.quartz.PubMedPubExtend;
import com.smate.center.task.model.pdwh.quartz.PubMedPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * pubMed基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubMedPublicationDao extends PdwhHibernateDao<PubMedPublication, Long> {

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void savePubMedPubExtend(Long pubId, String xmlData) {

    PubMedPubExtend extend = getPubMedPubExtend(pubId);
    if (extend == null) {
      extend = new PubMedPubExtend(pubId, xmlData);
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
  public PubMedPubExtend getPubMedPubExtend(Long pubId) {

    String hql = "from PubMedPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取基准库XML.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMedPubExtend> getPubMedPubExtends(List<Long> pubIds) {

    String hql = "from PubMedPubExtend t where t.pubId in(:pubIds) ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourcPubMeddHash
   * @param unitHash
   */
  public void savePubMedPubDup(Long pubId, Long sourceIdHash, Long titleHash, Long unitHash) {

    PubMedPubDup dup = new PubMedPubDup(pubId, sourceIdHash, titleHash, unitHash);
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
  public PubMedPubDup getPubMedPubDup(Long sourceIdHash, Long titleHash, Long unitHash) {

    // source_id_hash
    PubMedPubDup dup = null;
    if (unitHash != null && titleHash != null) {
      if (sourceIdHash == null) {
        sourceIdHash = 0l;
      }
      List<PubMedPubDup> list =
          super.createQuery("from PubMedPubDup t where (t.sourceIdHash = ? or t.unitHash = ?) and t.titleHash = ? ",
              sourceIdHash, unitHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }

    }
    return dup;
  }
}
