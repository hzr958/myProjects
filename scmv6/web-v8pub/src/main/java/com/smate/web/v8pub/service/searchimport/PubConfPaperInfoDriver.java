package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.utils.DateFormatter;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * @author yamingd 会议论文Brief生成驱动.
 */
public class PubConfPaperInfoDriver implements PubImportInfoDriver {

  @Autowired
  private ConstRegionService constRegionService;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 格式化Pattern.
   */
  // private final String pattern = "<out>${conf_name}</out><date_interval>,
  // ${start_date,end_date}</date_interval><page>,
  // ${page_number}</page><out>,${city}</out><date>,
  // ${publish_date}</date><out>, ${confType}</out><out></out>";

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> countryInfo = null;
    Map<String, String> datas = new HashMap<String, String>();
    ConferencePaperDTO paper = (ConferencePaperDTO) pubvo.getPubTypeInfo();
    datas.put("conf_name".toUpperCase(), paper.getName());
    String startDate = DateFormatter.dateFormater(paper.getStartDate(), "/");
    datas.put("start_date".toUpperCase(), startDate);
    String endDate = DateFormatter.dateFormater(paper.getEndDate(), "/");
    datas.put("end_date".toUpperCase(), endDate);
    datas.put("page_number".toUpperCase(), paper.getPageNumber());
    if (!NumberUtils.isNullOrZero(pubvo.getCountryId())) {
      countryInfo = constRegionService.buildCountryAndCityName(pubvo.getCountryId(), locale);
    }
    datas.put("city_name".toUpperCase(), CollectionUtils.isEmpty(countryInfo) ? "" : countryInfo.get("cityName"));
    String publishDate = DateFormatter.dateFormater(pubvo.getPublishDate(), "-");
    datas.put("publish_date".toUpperCase(), publishDate);
    if (paper.getPaperType() != null) {
      datas.put("confType".toUpperCase(), XmlUtil.getLanguageSpecificText(locale.getLanguage(),
          paper.getPaperType().getZhDescription(), paper.getPaperType().getEnDescription()));
    }
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.CONFPAPERBRIEF_PATTERN);
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.CONFERENCE_PAPER;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.CONFPAPERBRIEF_PATTERN;
  }

  @Override
  public PubSaveData buildPubSaveDataByJson(String json) {
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public PendingImportPubVO buildPendingImportPubVoByJson(String json) {
    PendingImportPubVO VO = (PendingImportPubVO) JacksonUtils.jsonObject(json,
        new TypeReference<PendingImportPubVO<ConferencePaperDTO>>() {});
    VO.setPubType(3);
    return VO;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new ConferencePaperDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new ConferencePaperDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, ConferencePaperDTO.class);
  }

}
