package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubViewPO;

/**
 * 成果查看、访问dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubViewDAO extends SnsHibernateDao<PubViewPO, Long> {

  /**
   * 查找访问记录
   * 
   * @param psnId
   * @param pubId
   * @param formateDate
   * @param ip
   * @return
   */
  public PubViewPO findPubView(Long psnId, Long pubId, Long formateDate, String ip) {
    String hql = "from PubViewPO t where t.viewPsnId = ? and t.pubId = ? and t.formateDate = ? and ";
    List<PubViewPO> list = null;
    if (ip != null) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, pubId, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, pubId, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

}
