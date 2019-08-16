package com.smate.core.base.app.dao;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.app.model.AppVersionRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class AppVersionRecordDao extends SnsHibernateDao<AppVersionRecord, Long> {
  /**
   * 获取最新的可用的ios app版本信息
   * 
   * @return
   * @author LIJUN
   * @date 2018年4月3日
   */
  public AppVersionRecord getNewestIOSVesion() {
    String hql = "from AppVersionRecord where status=0 and appType=1 order by updateTime desc";
    @SuppressWarnings("unchecked")
    List<AppVersionRecord> list = super.createQuery(hql).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

}
