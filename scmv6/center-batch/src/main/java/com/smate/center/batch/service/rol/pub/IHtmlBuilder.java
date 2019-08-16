package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * ftl构造接口
 * 
 * @author zk
 * 
 */
public interface IHtmlBuilder extends Serializable {

  /**
   * 构造html
   */
  public void buildHtml(Object... params) throws ServiceException;
}
