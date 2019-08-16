package com.smate.web.v8pub.service.handler.pubupdate;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 基准库成果引用次数更新处理器
 * 
 * @author YJ
 *
 *         2018年8月10日
 */
@PubHandlerMapping(pubHandlerName = "updatePdwhCitationsHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPdwhCitationsUpdateImpl extends PubHandlerServiceBaseBean {

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
    checkConfigList.add(new CheckConfig("citedType", Integer.class));
    checkConfigList.add(new CheckConfig("srcDbId", Integer.class));
    checkConfigList.add(new CheckConfig("citations", Integer.class));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    // TODO Auto-generated method stub

  }

}
