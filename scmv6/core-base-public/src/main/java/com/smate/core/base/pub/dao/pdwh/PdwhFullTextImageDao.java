package com.smate.core.base.pub.dao.pdwh;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.pdwh.PdwhFullTextImage;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author LIJUN
 *
 */
@Repository
public class PdwhFullTextImageDao extends PdwhHibernateDao<PdwhFullTextImage, Long> {

  public PdwhFullTextImage getPubFulltextByFiledId(Long fulltextFileId) {
    String hql = "from PdwhFullTextImage t where t.fileId=:fulltextFileId";
    return (PdwhFullTextImage) super.createQuery(hql).setParameter("fulltextFileId", fulltextFileId).uniqueResult();
  }

  /**
   * 获取全文图片地址
   * 
   * @param pubId
   * @return
   */
  public String getPubFulltextImage(Long pubId) {
    // cnki全文不显示
    String hqlSourceDb = "select count(1) from PdwhPubSourceDb t where t.pubId =:pubId and  t.cnki = 1";
    Long count = (Long) super.createQuery(hqlSourceDb).setParameter("pubId", pubId).uniqueResult();
    if (count >= 1) {
      return null;
    }
    String hql = "select t.imagePath from PdwhFullTextImage t where t.pubId=:pubId";
    return (String) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 记录是否存在
   * 
   * @param pdwhPubId
   * @return
   */
  public boolean isExist(Long pubId) {
    String hql = " select count(1) from PdwhFullTextImage t where t.pubId=:pdwhPubId";

    BigDecimal count = (BigDecimal) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (count.intValue() > 0) {
      return true;
    } else {
      return false;
    }
  }

}
