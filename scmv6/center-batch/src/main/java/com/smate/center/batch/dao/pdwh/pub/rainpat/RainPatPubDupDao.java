package com.smate.center.batch.dao.pdwh.pub.rainpat;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.rainpat.RainPatPubDup;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class RainPatPubDupDao extends PdwhHibernateDao<RainPatPubDup, Long> {


  /**
   * 
   * @param titleHash
   * @param patentHash
   * @param patentOpenHash
   * @return
   */

  @SuppressWarnings("unchecked")
  public RainPatPubDup getRainPatDupPub(Long titleHash, Long patentHash, Long patentOpenHash) {
    RainPatPubDup dup = null;

    if (patentHash != null && titleHash != null) {
      List<RainPatPubDup> list =
          super.createQuery("from RainPatPubDup t where t.patentHash = ? and t.titleHash = ? ", patentHash, titleHash)
              .list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    } else if (patentOpenHash != null && titleHash != null) {
      List<RainPatPubDup> list =
          super.createQuery("from RainPatPubDup t where t.patentOpenHash = ? and t.titleHash = ? ", patentOpenHash,
              titleHash).list();
      if (CollectionUtils.isNotEmpty(list)) {
        dup = list.get(0);
      }
    }
    return dup;

  }



  /**
   * 保存查重数据.
   * 
   * @param pubId
   * @param sourceIdHash
   * @param unitHash
   */
  public void saveRainPatPubDup(Long pubId, Long titleHash, Long patentHash, Long patentOpenHash) {
    RainPatPubDup dup = this.getRainPatDupPub(pubId);
    if (dup == null) {
      dup = new RainPatPubDup(pubId, titleHash, patentHash, patentOpenHash);
    } else {
      dup.setTitleHash(titleHash);
      dup.setPatentHash(patentHash);
      dup.setPatentOpenHash(patentOpenHash);
    }
    super.save(dup);
  }


  /**
   * 获取重复的基准库数据.
   * 
   * @param titleHash
   * 
   * @param unitHash
   * @return
   */

  private RainPatPubDup getRainPatDupPub(Long pubId) {
    RainPatPubDup dup = super.findUnique("from RainPatPubDup t where t.pubId = ? ", pubId);
    return dup;
  }
}
