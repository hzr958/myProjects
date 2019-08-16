package com.smate.web.v8pub.service.handler.pubsave;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 基准库成果全文更新处理器
 * 
 * @author YJ 2018年7月27日
 */

@PubHandlerMapping(pubHandlerName = "updatePdwhFullTextHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPdwhFullTextSaveImpl extends PubHandlerServiceBaseBean {

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
    checkConfigList.add(new CheckConfig("srcDbId", Integer.class, false));
    checkConfigList.add(new CheckConfig("fullText", JSONObject.class));

  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    // TODO 验证文件主表中，文件是否存在
  }

}
