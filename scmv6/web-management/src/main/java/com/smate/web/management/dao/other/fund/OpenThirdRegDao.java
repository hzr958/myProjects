package com.smate.web.management.dao.other.fund;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.web.management.model.other.fund.OpenThirdReg;

/**
 * 第三方系统注册表Dao
 * 
 * 
 */
@Repository
public class OpenThirdRegDao extends HibernateDao<OpenThirdReg, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }


  protected Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 通过token查询第三方登录系统名称
   *
   * @parameter token
   */
  public String getThirdSysNameByToken(String token) {
    String hql = "select t.thirdSysName from OpenThirdReg t where t.token = ?";
    return super.findUnique(hql, token);
  }

}
