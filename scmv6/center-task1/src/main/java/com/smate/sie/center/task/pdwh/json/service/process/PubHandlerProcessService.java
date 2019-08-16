package com.smate.sie.center.task.pdwh.json.service.process;

import java.util.Map;

import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;


/**
 * 成果处理过程
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
public interface PubHandlerProcessService {

  /**
   * 成果类型ID.
   * 
   * @return Integer
   */
  Integer getPubType();

  /**
   * 校验原始参数 ,会导致整个链 不执行的参数 (检验不通过 一律抛出异常)
   * 
   * @param pub
   * @throws PubHandlerAssemblyException
   */
  public void checkSourcesParameter(PubJsonDTO pub) throws PubHandlerProcessException;

  /**
   * 不会影响整个链,只会导致自己执行不了 不影响主线 (检验不通过 一律抛出异常)
   * 
   * @param pub
   * @throws PubHandlerAssemblyException
   */
  public void checkRebuildParameter(PubJsonDTO pub) throws PubHandlerProcessException;

  /**
   * 执行方法
   * 
   * @param pub
   * @return
   */
  public Map<String, Object> excute(PubJsonDTO pub) throws PubHandlerProcessException;

}
