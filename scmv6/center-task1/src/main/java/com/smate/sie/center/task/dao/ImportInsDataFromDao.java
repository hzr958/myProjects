package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportInsDataFrom;
import com.smate.sie.center.task.model.ImportInsDataFromId;

/**
 * 单位数据来源Dao
 * 
 * @author hd
 *
 */
@Repository
public class ImportInsDataFromDao extends SieHibernateDao<ImportInsDataFrom, ImportInsDataFromId> {

  @SuppressWarnings("unchecked")
  public ImportInsDataFrom findByInsIdAndToken(Long insId, String token) {
    List<ImportInsDataFrom> list =
        super.createQuery("from ImportInsDataFrom t where t.id.token = :token and t.insId =:insId")
            .setParameter("insId", insId).setParameter("token", token).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
