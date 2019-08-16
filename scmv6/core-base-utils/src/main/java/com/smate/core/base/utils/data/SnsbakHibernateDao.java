package com.smate.core.base.utils.data;

import java.io.Serializable;

import com.smate.core.base.utils.constant.DBSessionEnum;

/**
 * sns_bak库 dao 父类
 * 
 * @author zzx
 *
 * @param <T>
 * @param <PK>
 */
public class SnsbakHibernateDao<T, PK extends Serializable> extends HibernateDao<T, PK> {

  /**
   * 数据库 session类型
   */
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNSBAK;
  }

}
