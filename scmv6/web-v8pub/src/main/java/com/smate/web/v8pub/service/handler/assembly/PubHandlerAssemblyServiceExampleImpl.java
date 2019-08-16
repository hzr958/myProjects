package com.smate.web.v8pub.service.handler.assembly;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;

/**
 * 组装范例
 * 
 * @author tsz
 *
 * @date 2018年6月11日
 */
@Transactional(rollbackFor = Exception.class)
public class PubHandlerAssemblyServiceExampleImpl implements PubHandlerAssemblyService {

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    System.out.println("组装类1的实现");
    return null;
  }

}
