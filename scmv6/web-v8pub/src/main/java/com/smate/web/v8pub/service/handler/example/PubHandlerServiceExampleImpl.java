package com.smate.web.v8pub.service.handler.example;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 成果处理器范例
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
@PubHandlerMapping(pubHandlerName = "example")
@Service
@Transactional(rollbackFor = Exception.class)
public class PubHandlerServiceExampleImpl extends PubHandlerServiceBaseBean {

  @Override
  public void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("pubId", Long.class));
    checkConfigList.add(new CheckConfig("insId", Long.class));
  }

  @Override
  public void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {

  }

}
