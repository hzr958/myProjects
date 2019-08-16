package com.smate.center.open.dao.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.exception.OpenDataGetThirdRegException;
import com.smate.center.open.exception.OpenDataGetThirdRegNameException;
import com.smate.center.open.model.OpenThirdReg;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

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
   * 通过token查询第三方登录系统信息实体
   *
   * @throws Exception
   *
   * @parameter token
   */
  public OpenThirdReg getOpenThirdRegByToken(String token) {

    try {
      String hql = "from OpenThirdReg t where t.token = ?";
      return super.findUnique(hql, token);
    } catch (Exception e) {
      logger.error("重数据库获取第三方系统注册数据异常!");
      throw new OpenDataGetThirdRegException(e);
    }
  }

  /**
   * 通过token查询第三方登录系统名称
   *
   * @parameter token
   */
  public String getThirdSysNameByToken(String token) {
    try {
      String hql = "select t.thirdSysName from OpenThirdReg t where t.token = ?";
      return super.findUnique(hql, token);
    } catch (Exception e) {
      logger.error("重数据库获取第三方系统注册名称异常!");
      throw new OpenDataGetThirdRegNameException(e);
    }
  }

  /**
   * 通过token查询第三方登录系统英文名称
   *
   * @parameter token
   */
  public String getThirdSysNameUsByToken(String token) {
    try {
      String hql = "select t.thirdSysNameUs from OpenThirdReg t where t.token = ?";
      return super.findUnique(hql, token);
    } catch (Exception e) {
      logger.error("重数据库获取第三方系统注册名称异常!");
      throw new OpenDataGetThirdRegNameException(e);
    }
  }


}
