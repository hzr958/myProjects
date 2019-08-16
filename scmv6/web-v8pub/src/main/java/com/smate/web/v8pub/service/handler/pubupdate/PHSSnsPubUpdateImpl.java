package com.smate.web.v8pub.service.handler.pubupdate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;

/**
 * 个人库个人成果更新处理器
 * 
 * @author tsz
 *
 * @date 2018年7月13日
 */
@PubHandlerMapping(pubHandlerName = "updateSnsPubHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSSnsPubUpdateImpl extends PubHandlerServiceBaseBean {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
    checkConfigList.add(new CheckConfig("pubGenre", Integer.class, false));
    checkConfigList.add(new CheckConfig("des3PsnId", String.class, false));
    checkConfigList.add(new CheckConfig("permission", Integer.class));
    checkConfigList.add(new CheckConfig("des3PdwhPubId", String.class));
    checkConfigList.add(new CheckConfig("title", String.class, false));
    checkConfigList.add(new CheckConfig("publishDate", String.class));
    checkConfigList.add(new CheckConfig("countryId", Long.class));
    checkConfigList.add(new CheckConfig("fundInfo", String.class));
    checkConfigList.add(new CheckConfig("citations", Integer.class));
    checkConfigList.add(new CheckConfig("doi", String.class));
    checkConfigList.add(new CheckConfig("srcDbId", Integer.class));
    checkConfigList.add(new CheckConfig("summary", String.class));
    checkConfigList.add(new CheckConfig("citedUrl", String.class));
    checkConfigList.add(new CheckConfig("sourceUrl", String.class));
    checkConfigList.add(new CheckConfig("sourceId", String.class));
    checkConfigList.add(new CheckConfig("keywords", String.class));
    checkConfigList.add(new CheckConfig("srcFulltextUrl", String.class));
    checkConfigList.add(new CheckConfig("pubType", Integer.class, false));
    checkConfigList.add(new CheckConfig("recordFrom", PubSnsRecordFromEnum.class));
    checkConfigList.add(new CheckConfig("fulltextId", Long.class));
    checkConfigList.add(new CheckConfig("organization", String.class));
    checkConfigList.add(new CheckConfig("pubTypeInfo", JSONObject.class));
    checkConfigList.add(new CheckConfig("fullText", JSONObject.class));
    checkConfigList.add(new CheckConfig("members", JSONArray.class));
    checkConfigList.add(new CheckConfig("situations", JSONArray.class));
    checkConfigList.add(new CheckConfig("scienceAreas", JSONArray.class));
    checkConfigList.add(new CheckConfig("industrys", JSONArray.class));
    checkConfigList.add(new CheckConfig("accessorys", JSONArray.class));
    checkConfigList.add(new CheckConfig("des3GrpId", String.class));
    checkConfigList.add(new CheckConfig("isProjectPub", Integer.class));
    checkConfigList.add(new CheckConfig("isPubConfirm", Integer.class));
    checkConfigList.add(new CheckConfig("HCP", Integer.class));
    checkConfigList.add(new CheckConfig("HP", Integer.class));
    checkConfigList.add(new CheckConfig("OA", String.class));
    checkConfigList.add(new CheckConfig("pubSourceFileId", Long.class));
    checkConfigList.add(new CheckConfig("isEdit", Integer.class));
    // 只有更新时，基准库更新至个人库时会用到updateMark字段
    checkConfigList.add(new CheckConfig("updateMark", Integer.class));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {}

}
