package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.utils.DateFormatter;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * @author yamingd 期刊文章Brief生成驱动.
 */
public class PubJournalArticleInfoDriver implements PubImportInfoDriver {

  /**
   * 格式化Pattern.
   */
  // private final String pattern = "<out>${name}</out><vol_issue>,
  // ${volume_no,issue}</vol_issue><page>, ${page_number}</page><date>,
  // ${publish_date}</date><out></out>";

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    JournalInfoDTO journal = (JournalInfoDTO) pubvo.getPubTypeInfo();
    datas.put("name".toUpperCase(), journal.getName());
    // SD库插件抓取的带了英文单词得去掉
    datas.put("volume_no".toUpperCase(),
        ObjectUtils.toString(journal.getVolumeNo(), "").toUpperCase().replace("VOLUME", "").trim());
    datas.put("issue".toUpperCase(),
        ObjectUtils.toString(journal.getIssue(), "").toUpperCase().replace("ISSUE", "").trim());
    datas.put("page_number".toUpperCase(), journal.getPageNumber());
    String publishDate = DateFormatter.dateFormater(pubvo.getPublishDate(), "-");
    datas.put("publish_date".toUpperCase(), publishDate);
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.JOURNALBRIEF_PATTERN);
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.JOURNAL_ARTICLE;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.JOURNALBRIEF_PATTERN;
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
        (PendingImportPubVO) JacksonUtils.jsonObject(json, new TypeReference<PendingImportPubVO<JournalInfoDTO>>() {});
    VO.setPubType(4);
    return VO;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new JournalInfoDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new JournalInfoDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, JournalInfoDTO.class);
  }
}
