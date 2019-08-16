package com.smate.core.base.utils.data;

import com.smate.core.base.utils.constant.DBSessionEnum;

import java.io.Serializable;


/**
 * sns库 dao 父类
 *
 * @author tsz
 *
 */
public class SnsHibernateDao<T, PK extends Serializable> extends HibernateDao<T, PK> {

  /**
   * 数据库 session类型
   */
  @Override
  public DBSessionEnum getDbSession() {

    return DBSessionEnum.SNS;
  }
}
