package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.utils.DateFormatter;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * @author yamingd 专利Brief生成驱动.
 */
public class PubPatentInfoDriver implements PubImportInfoDriver {

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    PatentInfoDTO patent = (PatentInfoDTO) pubvo.getPubTypeInfo();
    String startDate = DateFormatter.dateFormater(patent.getStartDate(), "-");
    datas.put("start_date".toUpperCase(), startDate);
    // 签发机构/发证单位
    datas.put("issuing_authority".toUpperCase(), patent.getIssuingAuthority());
    datas.put("application_no".toUpperCase(), patent.getApplicationNo());
    datas.put("country_name".toUpperCase(), "");
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.PATENTBRIEF_PATTERN);
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.PATENT;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.PATENTBRIEF_PATTERN;
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
        (PendingImportPubVO) JacksonUtils.jsonObject(json, new TypeReference<PendingImportPubVO<PatentInfoDTO>>() {});
    VO.setPubType(5);
    return VO;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new PatentInfoDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new PatentInfoDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, PatentInfoDTO.class);
  }

}
