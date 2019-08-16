package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.utils.DateFormatter;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * @author yamingd 奖励类型Brief生成驱动.
 */
public class PubAwardInfoDriver implements PubImportInfoDriver {

  /**
   * 格式化Pattern.
   */
  // private final String pattern = "<out>${issuing_authority}</out><out>,
  // ${category}</out><out>, ${grade}</out><date>,
  // ${award_date}</date><out></out>";

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    AwardsInfoDTO award = (AwardsInfoDTO) pubvo.getPubTypeInfo();
    datas.put("issuing_authority".toUpperCase(), award.getIssuingAuthority());
    datas.put("category".toUpperCase(), award.getCategory());
    datas.put("grade".toUpperCase(), award.getGrade());
    String awardDate = DateFormatter.dateFormater(award.getAwardDate(), "-");
    datas.put("award_date".toUpperCase(), awardDate);
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.AWARDBRIEF_PATTERN);
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.AWARD;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.AWARDBRIEF_PATTERN;
  }

  @Override
  public PubSaveData buildPubSaveDataByJson(String json) {

    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public PendingImportPubVO buildPendingImportPubVoByJson(String json) {
    PendingImportPubVO VO =
        (PendingImportPubVO) JacksonUtils.jsonObject(json, new TypeReference<PendingImportPubVO<AwardsInfoDTO>>() {});
    VO.setPubType(1);
    return VO;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new AwardsInfoDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new AwardsInfoDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, AwardsInfoDTO.class);
  }

}
