package com.smate.center.batch.service.rol.pub;

import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;


/**
 * freemarker模板构造接口
 * 
 * @author zk
 * 
 */
public interface FtlBuilder {

  public Map<String, String> builderHtml(Object... params) throws ServiceException;

}
