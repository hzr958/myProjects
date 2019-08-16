package com.smate.center.batch.dao.sns.pub;

import org.hibernate.exception.DataException;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.base.AppSetting;
import com.smate.core.base.utils.data.SnsHibernateDao;



/**
 * 系统配置信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AppSettingDao extends SnsHibernateDao<AppSetting, Long> {

  /**
   * 获取KEY对应的系统配置.
   * 
   * @param key
   * @return
   */
  public String getAppSettingValue(String key) {
    return super.findUnique("select value from AppSetting where key = ? ", key.toLowerCase());
  }

  public int updateAppSetting(String key, String value) throws DataException {
    String hql = "update AppSetting t set t.value = ? where t.key = ?";
    return super.createQuery(hql, value, key).executeUpdate();
  }
}
