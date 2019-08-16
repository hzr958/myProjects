package com.smate.sie.center.task.pdwh.json.pubtype.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.constant.SieConstPatType;
import com.smate.core.base.utils.dao.consts.Sie6ConstDictionaryDao;
import com.smate.core.base.utils.dao.consts.SieConstPatTypeDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.pdwh.brief.BriefDriverFactory;
import com.smate.sie.center.task.pdwh.brief.IBriefDriver;
import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.json.pub.BriefFormatter;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 专利 构建成果详情的类别对象
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@Transactional(rollbackFor = Exception.class)
public class PubPatentInfoDealServiceImpl implements PubHandlerProcessService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BriefDriverFactory briefDriverFactory;
  @Autowired
  SieConstPatTypeDao sieConstPatTypeDao;
  @Autowired
  Sie6ConstDictionaryDao sie6ConstDictionaryDao;

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
    if (pub.pubTypeCode.intValue() == PublicationTypeEnum.PATENT) {
      try {
        pub.pubTypeInfoBean = constructParams(pub);
        pub.pubTypeName = "专利";
      } catch (Exception e) {
        logger.error("构建专利类别对象失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerProcessException(this.getClass().getSimpleName() + "构建专利类别对象失败！", e);
      }
    }
    // 文件导入，在预览页面就已经构造好来源，故这里做个判断，避免重复工作
    if (!pub.isImport) {
      try {
        Map<String, String> result = buildData(pub);
        IBriefDriver briefDriver = briefDriverFactory.getDriver(PublicationEnterFormEnum.SCHOLAR, pub.pubTypeCode);
        String pattern = briefDriver.getPattern();
        BriefFormatter formatter = new BriefFormatter(LocaleUtils.toLocale("zh_CN"), result);
        String briefDesc = formatter.format(pattern);
        pub.briefDesc = briefDesc;
      } catch (Exception e) {
        logger.error("专利构建briefDesc参数失败，pubTypeInfo={}", pub.pubTypeInfo, e);
        throw new PubHandlerProcessException(this.getClass().getSimpleName() + "专利构建briefDesc参数失败！", e);
      }
    }
    return resultMap;
  }

  private PubTypeInfoBean constructParams(PubJsonDTO pub) throws SysServiceException, IOException {
    PatentInfoBean patBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), PatentInfoBean.class);
    /* String applyNo = patBean.getApplicationNo(); */
    patBean.setAuthNo(StringUtils.substring(patBean.getAuthNo(), 0, 100));
    patBean.setCeritficateNo(StringUtils.substring(patBean.getCeritficateNo(), 0, 50));
    Map<String, String> map = JacksonUtils.json2Map(pub.pubTypeInfo.toJSONString());
    // 文件导入时，JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(),
    // PatentInfoBean.class)
    String CPC = patBean.getCPC();
    if (StringUtils.isBlank(CPC)) {
      CPC = map.get("cPC");
    }
    String IPC = patBean.getIPC();
    if (StringUtils.isBlank(IPC)) {
      IPC = map.get("iPC");
    }
    patBean.setCPC(StringUtils.substring(CPC, 0, 50));
    patBean.setIPC(StringUtils.substring(IPC, 0, 50));
    // applicationDate = publishDate
    patBean.setApplicationDate(patBean.getApplicationDate());
    // 专利类型
    String patentType = patBean.getTypeCode();
    if (!"".equalsIgnoreCase(patentType)) {
      SieConstPatType sieConstPatType = sieConstPatTypeDao.get(IrisNumberUtils.createInteger(patentType));
      if (sieConstPatType != null) {
        patBean.setTypeName(sieConstPatType.getName());
      }
    }
    // 法律类型
    String legalType = patBean.getPatentStatusCode();
    if (!"".equalsIgnoreCase(legalType)) {
      String legalTypeName = sie6ConstDictionaryDao.findZhNameByCategoryAndCode("pub_legal_status", legalType);
      if (legalTypeName != null) {
        patBean.setPatentStatusName(legalTypeName);
      }
    }
    return patBean;
  }

  private Map<String, String> buildData(PubJsonDTO pub) {
    PatentInfoBean patBean = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), PatentInfoBean.class);
    Map<String, String> map = new HashMap<String, String>();
    if (patBean != null) {
      map.put("patent_no".toUpperCase(), patBean.getApplicationNo());
      String publishDate = pub.publishDate;
      if (StringUtils.isNotEmpty(publishDate)) {
        String[] dates = DateUtils.splitToYearMothDayByStrUseToSie(patBean.getApplicationDate());
        map.put("publish_date".toUpperCase(), DateUtils.parseDate(dates[0], dates[1], dates[2], "-"));
      }
    }
    return map;
  }

  @Override
  public Integer getPubType() {
    return PublicationTypeEnum.PATENT;
  }

}
