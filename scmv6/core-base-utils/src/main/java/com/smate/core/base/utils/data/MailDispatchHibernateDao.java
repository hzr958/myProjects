package com.smate.core.base.utils.data;

import java.io.Serializable;

import com.smate.core.base.utils.constant.DBSessionEnum;

/**
 * EMAILSRV库 dao 父类
 * 
 * @author tsz
 *
 */
public class MailDispatchHibernateDao<T, PK extends Serializable> extends HibernateDao<T, PK> {

  /**
   * 数据库 session类型
   */
  @Override
  public DBSessionEnum getDbSession() {

    return DBSessionEnum.MAILDISPACH;
  }

}
