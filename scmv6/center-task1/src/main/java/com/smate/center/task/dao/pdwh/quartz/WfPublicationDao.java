package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.wanfang.WfPubDup;
import com.smate.center.task.model.pdwh.pub.wanfang.WfPubExtend;
import com.smate.center.task.model.pdwh.pub.wanfang.WfPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * wanfang基准库基本信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class WfPublicationDao extends PdwhHibernateDao<WfPublication, Long> {

  /**
   * 保存基准库xml.
   * 
   * @param pubId
   * @param xmlData
   */
  public void saveWfPubExtend(Long pubId, String xmlData) {

    WfPubExtend extend = getWfPubExtend(pubId);
    if (extend == null) {
      extend = new WfPubExtend(pubId, xmlData);
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
  public WfPubExtend getWfPubExtend(Long pubId) {

    String hql = "from WfPubExtend t where t.pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveWfPubDup(Long pubId, Long titleHash, Long unitHash) {

    WfPubDup dup = new WfPubDup(pubId, titleHash, unitHash);
    super.getSession().save(dup);
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
  public WfPubDup getWfPubDup(Long titleHash, Long unitHash) {

    WfPubDup dup = null;
    if (unitHash != null && titleHash != null) {
      List<WfPubDup> list =
          super.createQuery("from WfPubDup t where t.unitHash = ? and t.titleHash = ? ", unitHash, titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    }
    return dup;
  }
}
