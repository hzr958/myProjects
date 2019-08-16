package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.vo.PendingImportPubVO;

public class PubStandardInfoDriver implements PubImportInfoDriver {

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    StandardInfoDTO standardInfo = (StandardInfoDTO) pubvo.getPubTypeInfo();
    if (standardInfo != null) {
      datas.put("standard_no".toUpperCase(), standardInfo.getStandardNo());
    }
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.STANDARDBRIEF_PATTERN);
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.STANDARD;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.STANDARDBRIEF_PATTERN;
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
        (PendingImportPubVO) JacksonUtils.jsonObject(json, new TypeReference<PendingImportPubVO<StandardInfoDTO>>() {});
    VO.setPubType(12);
    return VO;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new ThesisInfoDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new StandardInfoDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, StandardInfoDTO.class);
  }
}
