package com.smate.core.base.utils.data;

import java.io.Serializable;

import com.smate.core.base.utils.constant.DBSessionEnum;

/**
 * 广西科研在线库 dao 父类
 * 
 * @author tsz
 *
 */
public class ZsRolHibernateDao<T, PK extends Serializable> extends HibernateDao<T, PK> {

  /**
   * 数据库 session类型
   */
  @Override
  public DBSessionEnum getDbSession() {

    return DBSessionEnum.ZSROL;
  }

}
