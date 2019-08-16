/**
 * 
 */
package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.DateFormatter;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * @author yamingd 学位论文Brief生成驱动.
 */
public class PubTheisInfoDriver implements PubImportInfoDriver {

  @Autowired
  private ConstRegionService constRegionService;

  /**
   * 格式化Pattern.
   */
  // private final String pattern = "<out>${department}</out><out>,
  // ${degree}</out><out>, ${issuing_authority}</out><out>,
  // ${country_name}</out><date>, ${publish_date}</date><out></out>";

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    ThesisInfoDTO thesis = (ThesisInfoDTO) pubvo.getPubTypeInfo();
    Map<String, String> countryInfo = null;
    datas.put("department".toUpperCase(), thesis.getDepartment());
    datas.put("degree".toUpperCase(), this.buildDegree(thesis.getDegree(), locale));
    datas.put("issuing_authority".toUpperCase(), thesis.getIssuingAuthority());
    if (!NumberUtils.isNullOrZero(pubvo.getCountryId())) {
      countryInfo = constRegionService.buildCountryAndCityName(pubvo.getCountryId(), locale);
    }
    datas.put("country_name".toUpperCase(),
        !CollectionUtils.isEmpty(countryInfo) ? countryInfo.get("countryName") : "");
    String publishDate = DateFormatter.dateFormater(pubvo.getPublishDate(), "-");
    datas.put("publish_date".toUpperCase(), publishDate);
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.THEISBRIEF_PATTERN);
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.THESIS;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.THEISBRIEF_PATTERN;
  }

  @Override
  public PubSaveData buildPubSaveDataByJson(String json) {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public PendingImportPubVO buildPendingImportPubVoByJson(String json) {
    PendingImportPubVO VO =
        (PendingImportPubVO) JacksonUtils.jsonObject(json, new TypeReference<PendingImportPubVO<ThesisInfoDTO>>() {});
    VO.setPubType(8);
    return VO;
  }

  private String buildDegree(PubThesisDegreeEnum degree, Locale locale) {
    if (degree != null) {
      if (Locale.CHINA.equals(locale)) {
        return degree.getZhDescription();
      } else {
        return degree.getEnDescription();
      }
    }
    return "";
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new ThesisInfoDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new ThesisInfoDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, ThesisInfoDTO.class);
  }
}
