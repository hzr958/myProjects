package com.smate.web.v8pub.test;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 单元测试用
 * 
 * @author tsz
 *
 * @date 2018年6月6日
 */
@PubHandlerMapping(pubHandlerName = "example")
@Service
public class PubHandlerServiceExampleImpl extends PubHandlerServiceBaseBean {

  @Override
  public void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("pubId", Long.class));
    checkConfigList.add(new CheckConfig("insId", Long.class));
  }

  @Override
  public void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {

  }

  public static void main(String[] args) {
    System.out.println(Des3Utils.decodeFromDes3("JvUzHyT7%2BGKs8hFG0Pfeeg%3D%3D"));
  }

}
