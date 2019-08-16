package com.smate.web.v8pub.service.handler.pubsave;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigService;

/**
 * 个人库个人成果保存处理器
 * 
 * @author tsz
 *
 * @date 2018年7月13日
 */
@PubHandlerMapping(pubHandlerName = "saveSnsPubHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSSnsPubSaveImpl extends PubHandlerServiceBaseBean {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnConfigService psnConfigService;

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PsnId", String.class));
    checkConfigList.add(new CheckConfig("des3PdwhPubId", String.class));
    checkConfigList.add(new CheckConfig("des3GrpId", String.class));
    checkConfigList.add(new CheckConfig("pubGenre", Integer.class, false));
    checkConfigList.add(new CheckConfig("permission", Integer.class));
    checkConfigList.add(new CheckConfig("title", String.class, false));
    checkConfigList.add(new CheckConfig("publishDate", String.class));
    checkConfigList.add(new CheckConfig("countryId", Long.class));
    checkConfigList.add(new CheckConfig("briefDesc", String.class));
    checkConfigList.add(new CheckConfig("fundInfo", String.class));
    checkConfigList.add(new CheckConfig("briefDesc", String.class));
    checkConfigList.add(new CheckConfig("citations", Integer.class));
    checkConfigList.add(new CheckConfig("doi", String.class));
    checkConfigList.add(new CheckConfig("authorNames", String.class));
    checkConfigList.add(new CheckConfig("summary", String.class));
    checkConfigList.add(new CheckConfig("keywords", String.class));
    checkConfigList.add(new CheckConfig("srcFulltextUrl", String.class));
    checkConfigList.add(new CheckConfig("sourceId", String.class));
    checkConfigList.add(new CheckConfig("sourceUrl", String.class));
    checkConfigList.add(new CheckConfig("srcDbId", Integer.class));
    checkConfigList.add(new CheckConfig("pubType", Integer.class, false));
    checkConfigList.add(new CheckConfig("recordFrom", PubSnsRecordFromEnum.class, false));
    checkConfigList.add(new CheckConfig("fulltextId", Long.class));
    checkConfigList.add(new CheckConfig("organization", String.class));
    checkConfigList.add(new CheckConfig("pubTypeInfo", JSONObject.class));
    checkConfigList.add(new CheckConfig("fullText", JSONObject.class));
    checkConfigList.add(new CheckConfig("members", JSONArray.class));
    checkConfigList.add(new CheckConfig("situations", JSONArray.class));
    checkConfigList.add(new CheckConfig("scienceAreas", JSONArray.class));
    checkConfigList.add(new CheckConfig("industrys", JSONArray.class));
    checkConfigList.add(new CheckConfig("accessorys", JSONArray.class));
    checkConfigList.add(new CheckConfig("authorNamesAbbr", String.class));
    checkConfigList.add(new CheckConfig("isProjectPub", Integer.class));
    checkConfigList.add(new CheckConfig("isPubConfirm", Integer.class));
    checkConfigList.add(new CheckConfig("updateMark", Integer.class));
    checkConfigList.add(new CheckConfig("HCP", Integer.class));
    checkConfigList.add(new CheckConfig("HP", Integer.class));
    checkConfigList.add(new CheckConfig("OA", String.class));
    checkConfigList.add(new CheckConfig("pubSourceFileId", Long.class));
    checkConfigList.add(new CheckConfig("isEdit", Integer.class));

  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    // 校验psnId是否有效
    Long psnId = Long.valueOf(Des3Utils.decodeFromDes3(pub.des3PsnId));
    Long cnfId = psnConfigService.getCnfIdByPsnId(psnId);
    if (NumberUtils.isNullOrZero(cnfId)) {
      logger.error("人员不存在psnId=" + psnId);
      throw new PubHandlerCheckParameterException("人员不存在psnId=" + psnId);
    }
  }

}
