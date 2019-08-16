package com.smate.web.v8pub.service.handler.pubsave;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 基准库成果保存处理器
 * 
 * @author YJ
 *
 *         2018年8月10日
 */
@PubHandlerMapping(pubHandlerName = "savePdwhPubHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPdwhSaveImpl extends PubHandlerServiceBaseBean {

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PsnId", String.class, false));
    checkConfigList.add(new CheckConfig("title", String.class, false));
    checkConfigList.add(new CheckConfig("publishDate", String.class));
    checkConfigList.add(new CheckConfig("countryId", Long.class));
    checkConfigList.add(new CheckConfig("fundInfo", String.class));
    checkConfigList.add(new CheckConfig("citations", Integer.class));
    checkConfigList.add(new CheckConfig("doi", String.class));
    checkConfigList.add(new CheckConfig("summary", String.class));
    checkConfigList.add(new CheckConfig("citedUrl", String.class));
    checkConfigList.add(new CheckConfig("sourceUrl", String.class));
    checkConfigList.add(new CheckConfig("keywords", String.class));
    checkConfigList.add(new CheckConfig("srcFulltextUrl", String.class));
    checkConfigList.add(new CheckConfig("pubType", Integer.class));
    checkConfigList.add(new CheckConfig("fulltextId", Long.class));
    checkConfigList.add(new CheckConfig("insId", Long.class));
    checkConfigList.add(new CheckConfig("organization", String.class));
    checkConfigList.add(new CheckConfig("sourceId", String.class));
    checkConfigList.add(new CheckConfig("authorNames", String.class));
    checkConfigList.add(new CheckConfig("srcDbId", Integer.class));
    checkConfigList.add(new CheckConfig("pubTypeInfo", JSONObject.class));
    checkConfigList.add(new CheckConfig("fullText", JSONObject.class));
    checkConfigList.add(new CheckConfig("members", JSONArray.class));
    checkConfigList.add(new CheckConfig("situations", JSONArray.class));
    checkConfigList.add(new CheckConfig("HCP", Integer.class));
    checkConfigList.add(new CheckConfig("HP", Integer.class));
    checkConfigList.add(new CheckConfig("OA", String.class));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    // TODO Auto-generated method stub

  }

}
