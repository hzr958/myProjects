package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.oalib.OalibPubDup;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class OalibPubDupDao extends PdwhHibernateDao<OalibPubDup, Long> {

  /**
   * 获取重复的基准库数据.
   * 
   * @param titleHash
   * 
   * @param unitHash
   * @return
   */

  public OalibPubDup getOalibPub(Long pubId) {
    OalibPubDup dup = super.findUnique("from OalibPubDup t where t.pubId = ? ", pubId);
    return dup;
  }

  /**
   * 保存查重数据.
   * 
   * @param pubId
   */
  public void saveOalibPub(Long pubId, Long titleHashValue, Long sourceIdHashValue, Long unitHashValue) {
    OalibPubDup oalibPub = this.getOalibPub(pubId);
    if (oalibPub == null) {
      oalibPub = new OalibPubDup(pubId, titleHashValue, sourceIdHashValue, unitHashValue);
    } else {
      oalibPub.setSourceIdHash(sourceIdHashValue);
      oalibPub.setTitleHash(titleHashValue);
      oalibPub.setUnionHash(unitHashValue);
    }
    super.save(oalibPub);
  }

  public Long getDupOalibPub(Long titleHashValue, Long unionHashValue, Long sourceIdHash) {

    String hql =
        "select pubId from OalibPubDup where titleHash=:titleHash and sourceIdHash=:sourceIdHash and unionHash=:unionHash";

    return (Long) super.createQuery(hql).setParameter("titleHash", titleHashValue)
        .setParameter("sourceIdHash", sourceIdHash).setParameter("unionHash", unionHashValue).uniqueResult();

  }

  /**
   * 根据sourceIdhash获取成果id
   */
  public Long getPubBySourceIdHash(Long sourceIdHash) {
    String hql = "select pubId from OalibPubDup where sourceIdHash=:sourceIdHash order by pubId desc";
    @SuppressWarnings("unchecked")
    List<Long> list = super.createQuery(hql).setParameter("sourceIdHash", sourceIdHash).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return 0L;
  }
}
