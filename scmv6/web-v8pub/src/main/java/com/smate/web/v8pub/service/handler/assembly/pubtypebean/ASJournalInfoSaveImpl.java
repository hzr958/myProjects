package com.smate.web.v8pub.service.handler.assembly.pubtypebean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.DataFormatUtils;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * 成果类别：期刊论文 构建成果详情的类别对象
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@Transactional(rollbackFor = Exception.class)
public class ASJournalInfoSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  private PubTypeInfoBean constructParams(PubDTO pub) {
    JournalInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), JournalInfoBean.class);
    if (a != null) {
      a.setName(StringUtils.substring(a.getName(), 0, 500));
      a.setVolumeNo(StringUtils.substring(a.getVolumeNo(), 0, 40));
      a.setIssue(StringUtils.substring(a.getIssue(), 0, 40));
      a.setPageNumber(StringUtils.substring(a.getPageNumber(), 0, 100));
      if (StringUtils.isBlank(a.getPublishStatus())) {
        a.setPublishStatus("P");
      }
    }
    return a;
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubType.intValue() == PublicationTypeEnum.JOURNAL_ARTICLE) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建期刊类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "构建期刊类别对象失败！", e);
      }
      try {
        Map<String, String> result = buildData(pub);
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(pub.title), result);
        String briefDesc = formatter.format(BriefFormatter.JOURNALBRIEF_PATTERN);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("会议成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "会议成果构建briefDesc参数失败！", e);
      }
    }

    return null;
  }

  private Map<String, String> buildData(PubDTO pub) {
    JournalInfoBean a = (JournalInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("name".toUpperCase(), a.getName());
      map.put("volume_no".toUpperCase(), a.getVolumeNo());
      map.put("issue".toUpperCase(), a.getIssue());
      if (StringUtils.isNotEmpty(a.getPageNumber())) {
        map.put("page_number".toUpperCase(), a.getPageNumber());
      }
      map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(pub.publishDate, "-"));
    }
    return map;
  }

}
