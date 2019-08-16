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
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.vo.PendingImportPubVO;

public class PubSoftwareCopyrightDriver implements PubImportInfoDriver {

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    SoftwareCopyrightDTO softwareCopyright = (SoftwareCopyrightDTO) pubvo.getPubTypeInfo();
    if (softwareCopyright != null) {
      datas.put("register_no".toUpperCase(), softwareCopyright.getRegisterNo());
    }
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.SOFTWARECOPYRIGHTBRIEF_PATTERN);
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.SOFTWARE_COPYRIGHT;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.SOFTWARECOPYRIGHTBRIEF_PATTERN;
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
        new TypeReference<PendingImportPubVO<SoftwareCopyrightDTO>>() {});
    VO.setPubType(12);
    return VO;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new SoftwareCopyrightDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new SoftwareCopyrightDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, SoftwareCopyrightDTO.class);
  }
}
