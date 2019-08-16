package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubViewPO;

/**
 * 成果查看、访问dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubViewDAO extends PdwhHibernateDao<PdwhPubViewPO, Long> {

  /**
   * 查找访问记录
   * 
   * @param psnId
   * @param pubId
   * @param formateDate
   * @param ip
   * @return
   */
  public PdwhPubViewPO findPdwhPubView(Long psnId, Long pdwhPubId, Long formateDate, String ip) {
    String hql = "from PdwhPubViewPO t where t.viewPsnId = ? and t.pdwhPubId = ? "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) "
        + " and t.formateDate = ? and ";
    List<PdwhPubViewPO> list = null;

    if (StringUtils.isNotBlank(ip)) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, pdwhPubId, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, pdwhPubId, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
