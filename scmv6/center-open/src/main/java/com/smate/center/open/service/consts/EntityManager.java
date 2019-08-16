package com.smate.center.open.service.consts;

import java.io.Serializable;
import java.util.List;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.PropertyFilter;

/**
 * 抽象的公共业务层接口.
 * 
 * @author zb
 * 
 * @param <T>
 * @param <PK>
 */
public abstract interface EntityManager<T, PK extends Serializable> {

  public abstract T get(final PK id) throws Exception;

  public abstract Page<T> getAll(final Page<T> page) throws Exception;

  public abstract List<T> getAll() throws Exception;

  public abstract Page<T> search(final Page<T> page, final List<PropertyFilter> filters) throws Exception;

  public abstract void save(final T entity) throws Exception;

  public abstract void delete(final PK id) throws Exception;

}
