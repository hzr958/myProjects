package com.smate.web.v8pub.service.handler.assembly.pubtypebean;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别：会议论文 构建成果详情的类别对象
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConferencePaperSaveImpl implements PubHandlerAssemblyService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  private PubTypeInfoBean constructParams(PubDTO pub) {
    ConferencePaperBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), ConferencePaperBean.class);
    if (a != null) {
      a.setName(StringUtils.substring(a.getName(), 0, 1000));
      a.setPageNumber(StringUtils.substring(a.getPageNumber(), 0, 100));
      a.setOrganizer(StringUtils.substring(a.getOrganizer(), 0, 100));
      a.setEndDate(PubParamUtils.formatDate(a.getEndDate()));
      a.setStartDate(PubParamUtils.formatDate(a.getStartDate()));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.CONFERENCE_PAPER) {
      // 构建pubTypeInfoBean
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建会议类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建会议类别对象失败！", e);
      }
      // 构建briefDesc
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.CONFPAPERBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        // 不抛出异常
        logger.error("会议成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "会议成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    Map<String, String> countryInfo = null;
    ConferencePaperBean a = (ConferencePaperBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("conf_name".toUpperCase(), a.getName());
      // 这里的日期格式必须是以 / 隔开的
      map.put("start_date".toUpperCase(), DataFormatUtils.parseDate(a.getStartDate(), "/"));
      map.put("end_date".toUpperCase(), DataFormatUtils.parseDate(a.getEndDate(), "/"));
      if (StringUtils.isNotEmpty(a.getPageNumber())) {
        map.put("page_number".toUpperCase(), a.getPageNumber());
      }
      if (!NumberUtils.isNullOrZero(pub.countryId)) {
        countryInfo = constRegionService.buildCountryAndCityName(pub.countryId, PubLocaleUtils.getLocale(pub.title));
      }
      map.put("city".toUpperCase(), CollectionUtils.isEmpty(countryInfo) ? "" : countryInfo.get("cityName"));
      map.put("confType".toUpperCase(), getPaperType(PubLocaleUtils.getLocale(pub.title), a.getPaperType()));
      // 这里的日期格式必须是以 - 隔开的
      map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(pub.publishDate, "-"));
    }
    return map;
  }

  /**
   * 获取会议类别名称
   * 
   * @param locale
   * @param paperType
   * @return
   */
  private String getPaperType(Locale locale, PubConferencePaperTypeEnum paperType) {
    if (Locale.CHINA.equals(locale)) {
      return paperType.getZhDescription();
    } else {
      return paperType.getEnDescription();
    }
  }

}
