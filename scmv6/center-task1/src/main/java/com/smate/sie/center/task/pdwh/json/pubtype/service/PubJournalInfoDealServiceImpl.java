package com.smate.sie.center.task.pdwh.json.pubtype.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.journal.model.SieJournal;
import com.smate.sie.center.task.journal.service.SieJournalService;
import com.smate.sie.center.task.pdwh.brief.BriefDriverFactory;
import com.smate.sie.center.task.pdwh.brief.IBriefDriver;
import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.json.pub.BriefFormatter;
import com.smate.sie.core.base.utils.pub.dom.JournalInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 期刊论文 构建成果详情的类别对象
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@Transactional(rollbackFor = Exception.class)
public class PubJournalInfoDealServiceImpl implements PubHandlerProcessService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BriefDriverFactory briefDriverFactory;
  @Autowired
  private SieJournalService sieJournalService;

  private Map<String, String> buildData(PubJsonDTO pub) {
    JournalInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), JournalInfoBean.class);
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("jname".toUpperCase(), a.getName());
      map.put("volume".toUpperCase(), a.getVolumeNo());
      map.put("issue".toUpperCase(), a.getIssue());
      if (StringUtils.isNotEmpty(a.getStartPage()) || StringUtils.isNotEmpty(a.getEndPage())) {
        map.put("start_page".toUpperCase(), a.getStartPage());
        map.put("end_page".toUpperCase(), a.getEndPage());
      } else {
        map.put("start_page".toUpperCase(), a.getArticleNo());
      }
      String publishDate = pub.publishDate;
      if (StringUtils.isNotEmpty(publishDate)) {
        String[] dates = DateUtils.splitToYearMothDayByStrUseToSie(pub.publishDate);
        map.put("publish_date".toUpperCase(), DateUtils.parseDate(dates[0], dates[1], dates[2], "-"));
      }
    }
    return map;
  }

  @Override
  public void checkSourcesParameter(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, Object> excute(PubJsonDTO pub) throws PubHandlerProcessException {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if (pub.pubTypeCode.intValue() == PublicationTypeEnum.JOURNAL_ARTICLE) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
        pub.pubTypeName = "期刊论文";
      } catch (Exception e) {
        logger.error("构建期刊类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerProcessException(this.getClass().getSimpleName() + "构建期刊类别对象失败！", e);
      }
      // 文件导入，在预览页面就已经构造好来源，故这里做个判断，避免重复工作，
      if (!pub.isImport) {
        try {
          Map<String, String> result = buildData(pub);
          IBriefDriver briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, pub.pubTypeCode);
          String pattern = briefDriver.getPattern();
          BriefFormatter formatter = new BriefFormatter(LocaleUtils.toLocale("zh_CN"), result);
          String briefDesc = formatter.format(pattern);
          pub.briefDesc = briefDesc;
        } catch (Exception e) {
          logger.error("期刊论文构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
          throw new PubHandlerProcessException(this.getClass().getSimpleName() + "期刊论文构建briefDesc参数失败！", e);
        }
      }
    }
    return resultMap;
  }

  private PubTypeInfoBean constructParams(PubJsonDTO pub) throws Exception {
    JournalInfoBean journal = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), JournalInfoBean.class);
    // 文件导入时，JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(),
    // JournalInfoBean.class) 转为bean时issn值会丢失
    Map<String, String> map = JacksonUtils.json2Map(pub.pubTypeInfo.toJSONString());
    String ISSN = journal.getISSN();
    if (StringUtils.isBlank(ISSN)) {
      ISSN = map.get("iSSN");
    }
    String jname = journal.getName();
    if (journal != null) {
      journal.setName(StringUtils.substring(jname, 0, 500));
      journal.setVolumeNo(journal.getVolumeNo());
      journal.setIssue(StringUtils.substring(journal.getIssue(), 0, 20));
      journal.setISSN(ISSN);
      if ("A".equals(journal.getPublishStatusCode())) {
        journal.setPublishStatusName("已接收");
        journal.setPublishStatusCode("A");
      } else {
        journal.setPublishStatusName("已发表");
        journal.setPublishStatusCode("P");
      }
    }
    // 获取期刊id
    jname = XmlUtil.formatJnlTitle(jname);
    if (StringUtils.isNotBlank(ISSN)) {
      ISSN = ISSN.toUpperCase();
    }
    if (StringUtils.isNotBlank(jname)) {
      try {
        SieJournal sieJournal = sieJournalService.addJournalByPubEnter(jname, ISSN, pub.insId);
        journal.setJid(sieJournal.getId());
      } catch (SysServiceException e) {
        logger.error("saveJournal失败,jname=" + jname + ", jissn=" + ISSN + ", isnId=" + pub.insId, e);
      }
    }

    return journal;
  }

  @Override
  public Integer getPubType() {
    return PublicationTypeEnum.JOURNAL_ARTICLE;
  }

}
