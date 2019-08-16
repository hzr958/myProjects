package com.smate.web.v8pub.service.handler.assembly;

import java.util.Map;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;

/**
 * 组装类接口
 * 
 * @author tsz
 *
 * @date 2018年6月11日
 */
public interface PubHandlerAssemblyService {

  /**
   * 校验原始参数 ,会导致整个链 不执行的参数 (检验不通过 一律抛出异常)
   * 
   * @param pub
   * @throws PubHandlerAssemblyException
   */
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException;


  /**
   * 不会影响整个链,只会导致自己执行不了 不影响主线 (检验不通过 一律抛出异常)
   * 
   * @param pub
   * @throws PubHandlerAssemblyException
   */
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException;


  /**
   * 执行方法
   * 
   * @param pub
   * @return
   */
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException;


}
