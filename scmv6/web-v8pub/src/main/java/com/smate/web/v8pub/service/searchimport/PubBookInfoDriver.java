package com.smate.web.v8pub.service.searchimport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.utils.DateFormatter;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * @author yamingd 书籍类型Brief生成驱动.
 */
public class PubBookInfoDriver implements PubImportInfoDriver {

  /**
   * 格式化Pattern.
   */
  // private final String pattern = "<out>${publisher}</out><page>,
  // ${total_pages}</page><date>, ${publish_date}</date><out>,
  // ${language}</out><out></out>";

  @SuppressWarnings("rawtypes")
  @Override
  public String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception {
    Map<String, String> datas = new HashMap<String, String>();
    BookInfoDTO book = (BookInfoDTO) pubvo.getPubTypeInfo();
    datas.put("publisher".toUpperCase(), book.getPublisher());
    if (book.getTotalPages() != null && book.getTotalPages() != 0) {
      datas.put("total_pages".toUpperCase(), book.getTotalPages() + "");
    }
    String publishDate = DateFormatter.dateFormater(pubvo.getPublishDate(), "-");
    datas.put("publish_date".toUpperCase(), publishDate);
    datas.put("language".toUpperCase(), book.getLanguage());
    BriefFormatter formatter = new BriefFormatter(locale, datas);
    return formatter.format(BriefFormatter.BOOKBRIEF_PATTERN);
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.BOOK;
  }

  @Override
  public String getPattern() {
    return BriefFormatter.BOOKBRIEF_PATTERN;
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
        (PendingImportPubVO) JacksonUtils.jsonObject(json, new TypeReference<PendingImportPubVO<BookInfoDTO>>() {});
    VO.setPubType(2);
    return VO;
  }

  /**
   * 构建language对象
   * 
   * @param locale
   * @return
   */
  private String getLanguage(Locale locale) {
    String language = "";
    if (Locale.CHINA.equals(locale)) {
      language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "中文", "Chinese");
    } else {
      language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "外文", "Foreign language");
    }
    return language;
  }

  @Override
  public PubTypeInfoDTO getNewPubTypeInfo(Integer pubType) {
    return new BookInfoDTO();
  }

  @Override
  public PubTypeInfoDTO buildTypeInfo(String typeInfoJson) {
    if (StringUtils.isBlank(typeInfoJson)) {
      return new BookInfoDTO();
    }
    return JacksonUtils.jsonObject(typeInfoJson, BookInfoDTO.class);
  }
}
