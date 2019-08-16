package com.smate.web.v8pub.service.handler.assembly.pubtypebean;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别：其他 构建成果详情的类别对象
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASOtherInfoSaveImpl implements PubHandlerAssemblyService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType == PublicationTypeEnum.OTHERS) {
      pub.pubTypeInfoBean = null;
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.OTHERBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("其他成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "其他成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    Map<String, String> countryInfo = null;
    Map<String, String> map = new HashMap<String, String>();
    if (!NumberUtils.isNullOrZero(pub.countryId)) {
      countryInfo = constRegionService.buildCountryAndCityName(pub.countryId, PubLocaleUtils.getLocale(pub.title));
    }
    map.put("country_name".toUpperCase(), !CollectionUtils.isEmpty(countryInfo) ? countryInfo.get("countryName") : "");
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(pub.publishDate, "-"));
    return map;
  }

}
