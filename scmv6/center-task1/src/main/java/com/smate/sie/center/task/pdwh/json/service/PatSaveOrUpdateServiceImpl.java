package com.smate.sie.center.task.pdwh.json.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.task.model.PatDupParam;
import com.smate.sie.center.task.model.SiePatDupFields;
import com.smate.sie.center.task.pdwh.service.PatKeyWordsService;
import com.smate.sie.center.task.pdwh.service.SiePatDupFieldsService;
import com.smate.sie.core.base.utils.dao.pub.SiePatentDao;
import com.smate.sie.core.base.utils.date.DateUtils;
import com.smate.sie.core.base.utils.model.pub.SiePatent;
import com.smate.sie.core.base.utils.pub.dom.PatentInfoBean;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.sie.core.base.utils.pub.service.PatJsonPOService;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 专利保存服务类
 * 
 * @author lijianming
 * @date 2019年3月14日
 */
@Service("patSaveOrUpdateService")
@Transactional(rollbackFor = Exception.class)
public class PatSaveOrUpdateServiceImpl implements PatSaveOrUpdateService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SiePatentDao patentDao;
  @Autowired
  private PatentMemberService patentMemberService;
  @Autowired
  private SiePatDupFieldsService patDupFieldsService;
  @Autowired
  private PatKeyWordsService patKeyWordsService;
  @Autowired
  private PatJsonPOService patJsonPOService;

  /**
   * 新增
   * 
   * @throws SysServiceException
   */
  @Override
  public SiePatent createPatent(PubJsonDTO pubJson) throws ServiceException {
    try {
      Date now = new Date();
      SiePatent patent = new SiePatent();
      patent.setPatId(pubJson.pubId);
      patent.setInsId(pubJson.insId);
      patent.setCreatePsnId(-1L);
      patent.setCreateDt(now);
      patent.setStatus("0");
      wrapPubField(pubJson, patent);
      patentDao.save(patent);
      pubJson.pubId = patent.getPatId();
      // 建立专利、成员关系数据
      patentMemberService.savePatMember(pubJson);
      patent.setAuthors(pubJson.authorNames);
      patentDao.save(patent);

      /* 清除数据库的pub_error_fields,遍历xml中的pub_errors/error,把错误的字段名保存进pub_error_fields --此逻辑目前去掉，业务上没有用到。 */
      // savePatErrorFields(doc, context);

      // 保存专利查重字段
      patDupFieldsService.savePatDupFields(buildPatDupFields(pubJson, patent), pubJson.pubId, pubJson.insId,
          SiePatDupFields.NORMAL_STATUS);
      // 专利关键词拆分保存
      patKeyWordsService.savePatKeywords(patent.getPatId(), pubJson.insId, pubJson.keywords, pubJson.keywords);
      // 保存Json格式
      patJsonPOService.savePatJson(pubJson);
      return patent;
    } catch (Exception e) {
      logger.error("savePatCreate保存新添加专利出错, pubId=" + pubJson.pubId, e);
      StackTraceElement stackTraceElement = e.getStackTrace()[0];
      int lineNum = stackTraceElement.getLineNumber();
      String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "异常方法名 ： " + stackTraceElement.getMethodName()
          + "，错误行号 ： " + lineNum;
      throw new ServiceException(
          "createPatent保存新添加专利出错, pubId=" + pubJson.pubId + ", " + e.toString() + " --> " + errMsg, e);
    }
  }

  @Override
  public SiePatent updatePatent(PubJsonDTO pubJson, PubPdwhPO pdwhPublications) throws ServiceException {
    try {
      SiePatent patent = patentDao.get(pubJson.pubId);
      if (patent == null) {
        throw new ServiceException("updatePatent该专利不存在patId:" + pubJson.pubId);
      }
      // 表示数据已被用户在页面编辑过，该任务不再做更新。
      if (patent.getIsVaild() == 1) {
        return patent;
      }
      patent.setPatId(pubJson.pubId);
      patent.setInsId(pubJson.insId);
      patent.setStatus("0");
      if (patent.getUpdateDt().before(pdwhPublications.getGmtModified())) {
        patent.setUpdateDt(pdwhPublications.getGmtModified());
      }
      wrapPubField(pubJson, patent);
      patent.setPdwhImportStatus(true);
      patentDao.save(patent);
      // 建立专利、成员关系数据
      patentMemberService.savePatMember(pubJson);
      patent.setAuthors(pubJson.authorNames);
      // 保存专利查重字段
      patDupFieldsService.savePatDupFields(buildPatDupFields(pubJson, patent), pubJson.pubId, pubJson.insId,
          SiePatDupFields.NORMAL_STATUS);
      // 专利关键词拆分保存
      patKeyWordsService.savePatKeywords(patent.getPatId(), pubJson.insId, pubJson.keywords, pubJson.keywords);
      // 保存Json格式
      patJsonPOService.savePatJson(pubJson);
      return patent;
    } catch (Exception e) {
      logger.error("savePatUpdate保存更新专利出错 , pubId=" + pubJson.pubId, e);
      StackTraceElement stackTraceElement = e.getStackTrace()[0];
      int lineNum = stackTraceElement.getLineNumber();
      String errMsg = "异常类文件名： " + stackTraceElement.getFileName() + "异常方法名 ： " + stackTraceElement.getMethodName()
          + "，错误行号 ： " + lineNum;
      throw new ServiceException(
          "updatePatent保存更新专利出错, pubId=" + pubJson.pubId + ", " + e.toString() + " --> " + errMsg, e);
    }
  }

  private void wrapPubField(PubJsonDTO patJson, SiePatent patent) throws ServiceException {
    // json转相对应的成果类型bean，这里转专利
    String json = JacksonUtils.jsonObjectSerializer(patJson.pubTypeInfoBean);
    PatentInfoBean patBean = JacksonUtils.jsonObject(json, PatentInfoBean.class);
    // 申请
    String[] dates = DateUtils.splitToYearMothDayByStr(patBean.getApplicationDate());
    patent.setApplyDay(StringUtils.isNotBlank(dates[2]) ? NumberUtils.toInt(dates[2]) : null);// IrisNumberUtils.createInteger
    patent.setApplyMonth(StringUtils.isNotBlank(dates[1]) ? NumberUtils.toInt(dates[1]) : null);
    patent.setApplyYear(StringUtils.isNotBlank(dates[0]) ? NumberUtils.toInt(dates[0]) : null);
    patent.setApplyNo(patBean.getApplicationNo());
    // 授权
    // 授权日期分割年月日
    String[] authDate = DateUtils.splitToYearMothDayByStr(patBean.getAuthDate());
    patent.setAuthDay(StringUtils.isNotBlank(authDate[2]) ? NumberUtils.toInt(authDate[2]) : null);
    patent.setAuthMonth(StringUtils.isNotBlank(authDate[1]) ? NumberUtils.toInt(authDate[1]) : null);
    patent.setAuthYear(StringUtils.isNotBlank(authDate[0]) ? NumberUtils.toInt(authDate[0]) : null);
    patent.setAuthNo(patBean.getAuthNo());

    patent.setZhTitle(StringUtils.substring(patJson.title, 0, 250));
    // patent.setZhTitlehash(IrisNumberUtils.createLong(PublicationHash.cleanTitleHash(patJson.title).toString()));
    patent.setEnTitle(StringUtils.substring(patJson.title, 0, 250));
    // patent.setEnTitleHash(IrisNumberUtils.createLong(PublicationHash.cleanTitleHash(patJson.title).toString()));
    patent.setBriefDesc(StringUtils.substring(patJson.briefDesc, 0, 500));
    patent.setBriefDescEn(StringUtils.substring(patJson.briefDesc, 0, 500));
    patent.setDataFrom(patJson.dataFrom);
    patent.setDbId(patJson.srcDbId);
    patent.setCeritfNo(patBean.getCeritficateNo());
    patent.setFulltextUrl(patJson.srcFulltextUrl);

    // 专利类型
    String patentType = patBean.getTypeCode();
    if (StringUtils.isNotBlank(patentType)) {
      patent.setPatType(Integer.valueOf(patentType));
    } else {
      patent.setPatType(null);
    }
    // 法律状态
    String legalStatus = patBean.getPatentStatusCode();
    if (StringUtils.isNotBlank(legalStatus)) {
      patent.setLevelStatus(legalStatus);
    } else {
      patent.setLevelStatus(null);
    }
    patent.setUpdateDt(new Date());
    patent.setUpPsnId(-1L);
    if (patJson.isPublicCode != null) {
      patent.setIsPublic(patJson.isPublicCode);
    }
    // 页面编辑过的数据 IsValid字段的值赋予1，表示基准库同步时不做更新
    if (patJson.isEdit) {
      patent.setIsVaild(1);
    } else {
      patent.setIsVaild(0);
    }
  }

  public PatDupParam buildPatDupFields(PubJsonDTO pubJson, SiePatent patent) throws ServiceException {
    String zhTitle = patent.getTitle();
    String enTitle = zhTitle;
    // json转相对应的成果类型bean，这里转专利
    String json = JacksonUtils.jsonObjectSerializer(pubJson.pubTypeInfoBean);
    PatentInfoBean patBean = JacksonUtils.jsonObject(json, PatentInfoBean.class);
    String patentNo = patBean.getApplicationNo();
    Integer pubYear = patent.getApplyYear();
    Integer sourceDbId = patent.getDbId();
    PatDupParam param = new PatDupParam();
    param.setPatentNo(patentNo);
    param.setEnTitle(enTitle);
    param.setPubYear(pubYear);
    param.setSourceDbId(sourceDbId);
    param.setZhTitle(zhTitle);
    return param;
  }


}
