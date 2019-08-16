package com.smate.center.open.dao.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.exception.OpenDataSaveLoginLogException;
import com.smate.center.open.exception.OpenSysDataException;
import com.smate.center.open.model.OpenUserLoginLog;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

/**
 * 人员关联sns登录信息表Dao
 * 
 * 
 */
@Repository
public class OpenUserLoginLogDao extends HibernateDao<OpenUserLoginLog, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 保存第三方用户登录信息
   * 
   * @throws OpenSysDataException
   * 
   * @parameter openUserLoginLog
   */
  public void saveUserLoginLog(OpenUserLoginLog openUserLoginLog) {
    try {
      super.save(openUserLoginLog);
    } catch (Exception e) {
      logger.error("保存用户验证登录日志异常");
      throw new OpenDataSaveLoginLogException(e);
    }
  }


}
