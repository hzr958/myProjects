package com.smate.web.dyn.dao.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubConfirmRecord;

/**
 * 成果确认记录.
 * 
 * @author lqh
 * 
 */
@Repository
public class PubConfirmRecordDao extends SnsHibernateDao<PubConfirmRecord, Long> {

  public PubConfirmRecord getRecordBySnsPubId(Long snsPubId) {
    String hql = "from PubConfirmRecord t where t.snsPubId = ? and t.syncRcmd = 1";
    List<PubConfirmRecord> list = createQuery(hql, snsPubId).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    } else {
      return list.get(0);
    }
  }


}
