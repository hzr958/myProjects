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
 * 成果全文更新处理器 组装类如下：<br/>
 * asPubFulltextSaveImpl <br/>
 * asPubGroupPubUpdateImpl <br/>
 * TODO 开启成果全文缩略图任务 <br/>
 * 
 * @author YJ 2018年7月27日
 */

@PubHandlerMapping(pubHandlerName = "updateSnsFullTextHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPubFullTextSaveImpl extends PubHandlerServiceBaseBean {

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class));
    checkConfigList.add(new CheckConfig("des3PsnId", String.class));
    checkConfigList.add(new CheckConfig("permission", Integer.class));
    checkConfigList.add(new CheckConfig("fullText", JSONObject.class));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {

  }

}
