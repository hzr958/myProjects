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

import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别：学位论文 构建成果详情的类别对象
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASThesisInfoSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionService constRegionService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  private PubTypeInfoBean constructParams(PubDTO pub) {
    ThesisInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), ThesisInfoBean.class);
    if (a != null) {
      a.setISBN(StringUtils.substring(a.getISBN(), 0, 80));
      a.setDepartment(StringUtils.substring(a.getDepartment(), 0, 200));
      a.setIssuingAuthority(StringUtils.substring(a.getIssuingAuthority(), 0, 200));
      a.setDefenseDate(PubParamUtils.formatDate(a.getDefenseDate()));
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.THESIS) {
      // 构建pubTypeInfoBean
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建学位类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建学位成果类别对象失败！", e);
      }
      // 构建briefDesc
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.THEISBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("学位成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "学位成果构建briefDesc参数失败！", e);
      }
    }
    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    Map<String, String> countryInfo = null;
    Locale locale = PubLocaleUtils.getLocale(pub.title);
    ThesisInfoBean a = (ThesisInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("department".toUpperCase(), a.getDepartment());
      map.put("degree".toUpperCase(), buildDegree(a.getDegree(), locale));
      map.put("issuing_authority".toUpperCase(), a.getIssuingAuthority());
      if (!NumberUtils.isNullOrZero(pub.countryId)) {
        countryInfo = constRegionService.buildCountryAndCityName(pub.countryId, PubLocaleUtils.getLocale(pub.title));
      }
      map.put("country_name".toUpperCase(),
          !CollectionUtils.isEmpty(countryInfo) ? countryInfo.get("countryName") : "");
      map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(a.getDefenseDate(), "-"));
    }
    return map;
  }

  private String buildDegree(PubThesisDegreeEnum degree, Locale locale) {
    if (Locale.CHINA.equals(locale)) {
      return degree.getZhDescription();
    } else {
      return degree.getEnDescription();
    }
  }
}
