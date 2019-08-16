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
import com.smate.core.base.utils.dao.consts.Sie6ConstDictionaryDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.pdwh.brief.BriefDriverFactory;
import com.smate.sie.center.task.pdwh.brief.IBriefDriver;
import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.json.pub.BriefFormatter;
import com.smate.sie.core.base.utils.pub.dom.AwardsInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 奖励 构建成果详情的类别对象
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@Transactional(rollbackFor = Exception.class)
public class PubAwardsInfoDealServiceImpl implements PubHandlerProcessService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BriefDriverFactory briefDriverFactory;
  @Autowired
  private Sie6ConstDictionaryDao sie6ConstDictionaryDao;

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
    if (pub.pubTypeCode.intValue() == PublicationTypeEnum.AWARD) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
      } catch (Exception e) {
        logger.error("构建奖励类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerProcessException(this.getClass().getSimpleName() + "构建奖励类别对象失败!", e);
      }
      // 文件导入，在预览页面就已经构造好来源，故这里做个判断，避免重复工作，
      if (!pub.isImport) {
        try {
          IBriefDriver briefDriver = null;
          Map<String, String> result = null;
          result = buildData(pub);
          briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, pub.pubTypeCode);
          String pattern = briefDriver.getPattern();
          BriefFormatter formatter = new BriefFormatter(LocaleUtils.toLocale("zh_CN"), result);
          String briefDesc = formatter.format(pattern);
          pub.briefDesc = briefDesc;
        } catch (Exception e) {
          logger.error("奖励成果构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
          throw new PubHandlerProcessException(this.getClass().getSimpleName() + "奖励成果构建briefDesc参数失败！", e);
        }
      }
    }
    return resultMap;
  }

  /**
   * 参数处理 主要是长度的处理
   * 
   * @param pub
   * @return
   */
  private PubTypeInfoBean constructParams(PubJsonDTO pub) {
    AwardsInfoBean a = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), AwardsInfoBean.class);
    if (a != null) {
      a.setCertificateNo(StringUtils.substring(a.getCertificateNo(), 0, 100));
      if (StringUtils.isNotBlank(a.getCategoryCode())) {
        String categoryName = sie6ConstDictionaryDao.findZhNameByCategoryAndCode("award_category", a.getCategoryCode());
        a.setCategoryCode(a.getCategoryCode());
        a.setCategoryName(categoryName);
      }
      if (StringUtils.isNotBlank(a.getGradeCode())) {
        String gradeName = sie6ConstDictionaryDao.findZhNameByCategoryAndCode("award_grade", a.getGradeCode());
        a.setGradeCode(a.getGradeCode());
        a.setGradeName(gradeName);
      }
      a.setIssuingAuthority(StringUtils.substring(a.getIssuingAuthority(), 0, 100));
      a.setAwardDate(pub.publishDate);
    }
    return a;
  }

  private Map<String, String> buildData(PubJsonDTO pub) {
    AwardsInfoBean a = (AwardsInfoBean) pub.pubTypeInfoBean;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("issue_ins_name".toUpperCase(), a.getIssuingAuthority());
      map.put("award_category_name".toUpperCase(), a.getCategoryName());
      map.put("award_grade_name".toUpperCase(), a.getGradeName());
      String publishDate = pub.publishDate;
      if (StringUtils.isNotEmpty(publishDate)) {
        String[] dates = DateUtils.splitToYearMothDayByStrUseToSie(pub.publishDate);
        map.put("award_date".toUpperCase(), DateUtils.parseDate(dates[0], dates[1], dates[2], "-"));
      }
    }
    return map;
  }

  @Override
  public Integer getPubType() {
    return PublicationTypeEnum.AWARD;
  }

}
