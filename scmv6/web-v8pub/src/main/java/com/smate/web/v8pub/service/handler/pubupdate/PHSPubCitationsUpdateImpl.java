package com.smate.web.v8pub.service.handler.pubupdate;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 个人库成果引用更新处理器
 * 
 * @author YJ
 *
 *         2018年8月10日
 */
@PubHandlerMapping(pubHandlerName = "updateSnsCitationsHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPubCitationsUpdateImpl extends PubHandlerServiceBaseBean {

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
    checkConfigList.add(new CheckConfig("des3PsnId", String.class, false));
    checkConfigList.add(new CheckConfig("citations", Integer.class, false));
    checkConfigList.add(new CheckConfig("citedType", Integer.class));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    // TODO Auto-generated method stub

  }

}
