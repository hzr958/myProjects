package com.smate.web.dyn.service.dynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.dao.fund.CategoryMapBaseDao;
import com.smate.web.dyn.dao.fund.ConstFundAgencyDao;
import com.smate.web.dyn.dao.fund.ConstFundCategoryDao;
import com.smate.web.dyn.dao.news.NewsBaseDao;
import com.smate.web.dyn.dao.pdwh.PdwhFullTextFileDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubFullTextDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPublicationDao;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.pub.PubFulltextDao;
import com.smate.web.dyn.dao.pub.PubSimpleDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.dao.pub.PublicationDao;
import com.smate.web.dyn.dao.rcmd.ConstFundCategoryDisDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.fund.CategoryMapBase;
import com.smate.web.dyn.model.fund.ConstFundAgency;
import com.smate.web.dyn.model.fund.ConstFundCategory;
import com.smate.web.dyn.model.fund.ConstFundCategoryInfo;
import com.smate.web.dyn.model.msg.MsgShowInfo;
import com.smate.web.dyn.model.news.NewsBase;
import com.smate.web.dyn.model.pub.PdwhPubFullTextPO;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.service.psn.InsInfoService;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * 检查和生成模板数据的抽象类，具体动态数据生成的实现类需要继承这个抽象类
 * 
 * @author zjh
 *
 */
public abstract class DynamicDataDealBaseService implements DynamicDataDealService {
  // 没全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE = "/resmod/images_v5/images2016/file_img.jpg";
  // 有全文的默认图片
  public final static String DEFAULT_PUBFULLTEXT_IMAGE1 = "/resmod/images_v5/images2016/file_img1.jpg";

  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsFullTextDAO pubFullTextDAO;
  @Autowired
  private PubFulltextDao pubFullTextDao;
  @Autowired
  private PersonQueryservice personQueryservice;
  @Autowired
  private InsInfoService insInfoService;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private NewsBaseDao newsBaseDao;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public HashMap<String, Object> handlePrjInfo(DynamicForm form, HashMap<String, Object> dataMap) throws DynException {
    Project prj = projectDao.get(form.getResId());
    if (prj != null) {
      if (prj.getPsnId() != null) {
        dataMap.put(DynTemplateConstant.RES_OWNER_DES3ID, Des3Utils.encodeToDes3(prj.getPsnId().toString()));
      }
      dataMap.put(DynTemplateConstant.DES3_RES_ID, form.getDes3ResId());
      dataMap.put(DynTemplateConstant.PUB_INDEX_URL,
          domainscm + "/prjweb/project/detailsshow?des3PrjId=" + form.getDes3ResId());
      String zhTitle = StringUtils.isNotBlank(prj.getZhTitle()) ? prj.getZhTitle() : prj.getEnTitle();
      String enTitle = StringUtils.isNotBlank(prj.getEnTitle()) ? prj.getEnTitle() : prj.getZhTitle();
      dataMap.put(DynTemplateConstant.LINK_TITLE_ZH, zhTitle);
      dataMap.put(DynTemplateConstant.LINK_TITLE_EN, enTitle);
      String zhDescr = StringUtils.isNotBlank(prj.getBriefDesc()) ? prj.getBriefDesc() : prj.getBriefDescEn();
      String enDescr = StringUtils.isNotBlank(prj.getBriefDescEn()) ? prj.getBriefDescEn() : prj.getBriefDesc();
      dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, zhDescr);
      dataMap.put(DynTemplateConstant.PUB_DESCR_EN, enDescr);
      dataMap.put(DynTemplateConstant.PUB_AUTHORS,
          StringUtils.isBlank(prj.getAuthorNames()) ? prj.getAuthorNamesEn() : prj.getAuthorNames());
      dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE);
    } else {
      throw new DynException("生成项目动态失败，查找不到项目信息，prjId=" + form.getResId());
    }

    return dataMap;

  }

  /**
   * 检查及处理动态数据
   * 
   * @param form
   * @return
   * @throws DynException
   */
  public boolean checkAndDeal(DynamicForm form) throws DynException {
    try {
      String verifyResult = this.checkData(form);
      if (verifyResult == null) {
        // 进行数据处理
        this.dealData(form);
      } else {
        // 必要的参数校验没有通过
        logger.error("缺少必要的参数");
        throw new DynException("缺少必要的参数，dynType=" + form.getDynType() + ", verifyResult=" + verifyResult);
      }
    } catch (Exception e) {
      logger.error("检出及处理动态数据出错", e);
      throw new DynException(e);
    }
    return false;
  }

  /**
   * 处理成果信息
   * 
   * @param form
   * @param dataMap
   * @return
   * @throws DynException
   */
  public HashMap<String, Object> handlePubInfo(DynamicForm form, HashMap<String, Object> dataMap) throws DynException {
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(form.getPubId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      dataMap.put(DynTemplateConstant.PUB_INDEX_URL,
          domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }
    if (DynTemplateConstant.B3TEMP.equals(form.getDynType())) {
      PubSnsPO pubSns = pubSnsDAO.getpubBriefAndTitle(form.getPubId());
      if (pubSns != null) {
        if (pubSns.getCreatePsnId() != null) {
          dataMap.put(DynTemplateConstant.RES_OWNER_DES3ID, Des3Utils.encodeToDes3(pubSns.getCreatePsnId().toString()));
        }
        dataMap.put(DynTemplateConstant.HAS_DES3_PUB_ID, true);
        dataMap.put(DynTemplateConstant.DES3_RES_ID, form.getDes3PubId());
        dataMap.put(DynTemplateConstant.LINK_TITLE_ZH, pubSns.getTitle());
        dataMap.put(DynTemplateConstant.LINK_TITLE_EN, pubSns.getTitle());
        dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, pubSns.getBriefDesc());
        dataMap.put(DynTemplateConstant.PUB_DESCR_EN, pubSns.getBriefDesc());
        dataMap.put(DynTemplateConstant.PUB_AUTHORS, pubSns.getAuthorNames());
        dataMap.put(DynTemplateConstant.PUB_PUBLISHYEAR, pubSns.getPublishYear());
        String pubFulltextImage = null;
        PubFullTextPO fullText = pubFullTextDAO.getPubFullTextByPubId(form.getPubId());
        dataMap.put(DynTemplateConstant.FULLTEXT_DES3PUBID, form.getDes3PubId());
        if (fullText != null) {
          pubFulltextImage = fullText.getThumbnailPath();
          // 增加小图片
          dataMap.put(DynTemplateConstant.FULLTEXT_DES3FILEID,
              Des3Utils.encodeToDes3(String.valueOf(fullText.getFileId())));
          dataMap.put(DynTemplateConstant.FULLTEXT_IMAGE, Des3Utils.encodeToDes3(String.valueOf(fullText.getFileId())));
        }
        if (StringUtils.isBlank(pubFulltextImage)) {
          if (fullText != null && fullText.getFileId() != null) {
            dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE1);
          } else {
            dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE);
          }
        } else {
          dataMap.put(DynTemplateConstant.LINK_IMAGE, pubFulltextImage);
        }
      } else {
        produceDynamic(form, dataMap);
      }
    } else {
      produceDynamic(form, dataMap);
    }

    return dataMap;
  }

  /**
   * 基准库成果
   */
  public void producePdwhDynamic(DynamicForm form, HashMap<String, Object> dataMap) {
    PubPdwhPO pdwhPublication = pubPdwhDAO.get(form.getResId());
    if (pdwhPublication != null) {
      dataMap.put(DynTemplateConstant.HAS_DES3_PUB_ID, true);
      dataMap.put(DynTemplateConstant.DES3_RES_ID, form.getDes3PubId());
      String zhTitle = pdwhPublication.getTitle();
      String enTitle = pdwhPublication.getTitle();
      dataMap.put(DynTemplateConstant.LINK_TITLE_ZH, zhTitle);
      dataMap.put(DynTemplateConstant.LINK_TITLE_EN, enTitle);
      String zhDescr = pdwhPublication.getBriefDesc();
      String enDescr = pdwhPublication.getBriefDesc();
      dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, zhDescr);
      dataMap.put(DynTemplateConstant.PUB_DESCR_EN, enDescr);
      dataMap.put(DynTemplateConstant.PUB_AUTHORS, pdwhPublication.getAuthorNames());
      dataMap.put(DynTemplateConstant.PUB_PUBLISHYEAR, pdwhPublication.getPublishYear());
      dataMap.put(DynTemplateConstant.DB_ID, form.getDbId());
      dataMap.put(DynTemplateConstant.PDWH_URL, form.getDatabaseType());
      PdwhPubFullTextPO pdwhFullTextFile = pdwhPubFullTextDAO.getFullTextByPubId(form.getPubId());
      if (pdwhFullTextFile != null && null != pdwhFullTextFile.getFileId()) {
        dataMap.put(DynTemplateConstant.FULLTEXT_IMAGE, pdwhFullTextFile.getFileId());
      }
      // SCM-14785
      if (pdwhFullTextFile != null && pdwhFullTextFile.getFileId() != null) {
        dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE1);
      } else {
        dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE);
      }
    }
  }

  public void produceDynamic(DynamicForm form, HashMap<String, Object> dataMap) {
    PubSnsPO pubSns = pubSnsDAO.getpubBriefAndTitle(form.getPubId());
    if (pubSns != null) {
      if (pubSns.getCreatePsnId() != null) {
        dataMap.put(DynTemplateConstant.RES_OWNER_DES3ID, Des3Utils.encodeToDes3(pubSns.getCreatePsnId().toString()));
      }
      dataMap.put(DynTemplateConstant.HAS_DES3_PUB_ID, true);
      dataMap.put(DynTemplateConstant.DES3_RES_ID, form.getDes3PubId());
      dataMap.put(DynTemplateConstant.LINK_TITLE_ZH, pubSns.getTitle());
      dataMap.put(DynTemplateConstant.LINK_TITLE_EN, pubSns.getTitle());
      dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, pubSns.getBriefDesc());
      dataMap.put(DynTemplateConstant.PUB_DESCR_EN, pubSns.getBriefDesc());
      dataMap.put(DynTemplateConstant.PUB_AUTHORS, pubSns.getAuthorNames());
      dataMap.put(DynTemplateConstant.PUB_PUBLISHYEAR, pubSns.getPublishYear());
      String pubFulltextImage = null;
      PubFullTextPO fullText = pubFullTextDAO.getPubFullTextByPubId(form.getPubId());
      dataMap.put(DynTemplateConstant.FULLTEXT_DES3PUBID, form.getDes3PubId());
      if (fullText != null) {
        pubFulltextImage = fullText.getThumbnailPath();
        // 增加小图片
        dataMap.put(DynTemplateConstant.FULLTEXT_DES3FILEID,
            Des3Utils.encodeToDes3(String.valueOf(fullText.getFileId())));
        dataMap.put(DynTemplateConstant.FULLTEXT_IMAGE, Des3Utils.encodeToDes3(String.valueOf(fullText.getFileId())));
      }
      if (StringUtils.isBlank(pubFulltextImage)) {
        if (fullText != null && fullText.getFileId() != null) {
          dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE1);
        } else {
          dataMap.put(DynTemplateConstant.LINK_IMAGE, DEFAULT_PUBFULLTEXT_IMAGE);
        }
      } else {
        dataMap.put(DynTemplateConstant.LINK_IMAGE, pubFulltextImage);
      }
    }
  }

  /**
   * 处理人员信息
   * 
   * @param form
   * @param dataMap
   * @return
   * @throws DynException
   */
  public HashMap<String, Object> handlePsnInfo(DynamicForm form, HashMap<String, Object> dataMap) throws DynException {
    Person person = personQueryservice.findPersonBase(form.getPsnId());
    if (person == null) {
      throw new DynException("人员信息为空");
    }
    dataMap.put(DynTemplateConstant.PERSON_NAME_ZH, personQueryservice.getPsnName(person, "zh_CN"));
    dataMap.put(DynTemplateConstant.PERSON_NAME_EN, personQueryservice.getPsnName(person, "en_US"));
    dataMap.put(DynTemplateConstant.DES3_PRODUCER_PSN_ID, ServiceUtil.encodeToDes3(person.getPersonId().toString()));
    dataMap.put(DynTemplateConstant.PERSON_AVATARS, person.getAvatars());
    return dataMap;
  }

  /**
   * 处理单位信息
   * 
   * @param form
   * @param dataMap
   * @return
   * @throws DynException
   */
  public HashMap<String, Object> handleInsInfo(DynamicForm form, HashMap<String, Object> dataMap) throws DynException {
    Map<String, String> insInfoMap = insInfoService.findPsnInsInfo(form.getPsnId());
    if (insInfoMap != null) {
      dataMap.put(DynTemplateConstant.PERSON_INSINFO_ZH, insInfoMap.get(InsInfoService.INS_INFO_ZH));
      dataMap.put(DynTemplateConstant.PERSON_INSINFO_EN, insInfoMap.get(InsInfoService.INS_INFO_EN));
    }
    return dataMap;
  }

  /**
   * 处理基金信息
   * 
   * @param form
   * @param dataMap
   * @return
   */
  public HashMap handleFundInfo2(DynamicForm form, HashMap<String, Object> dataMap) {
    ConstFundCategory fund = constFundCategoryDao.get(form.getResId());
    if (fund != null) {
      dataMap.put(DynTemplateConstant.LINK_TITLE_EN,
          StringUtils.isNotBlank(fund.getNameEn()) ? fund.getNameEn() : fund.getNameZh());
      dataMap.put(DynTemplateConstant.LINK_TITLE_ZH,
          StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn());
      if (StringUtils.isNotBlank(form.getResInfoJson())) {
        Map<String, Object> info = JacksonUtils.jsonToMap(form.getResInfoJson());
        if (info != null) {
          // 描述
          dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, info.get("fund_desc_zh"));
          dataMap.put(DynTemplateConstant.PUB_DESCR_EN, info.get("fund_desc_en"));
        }
      } else {
        ConstFundCategoryInfo fundInfo = this.buildFundInfo(fund);
        if (fundInfo != null) {
          dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, fundInfo.getZhShowDesc());
          dataMap.put(DynTemplateConstant.PUB_DESCR_EN, fundInfo.getEnShowDesc());
        }
      }
      // logo url
      String defaultLogoUrl = "/ressns/images/default/default_fund_logo.jpg";
      if (fund.getAgencyId() != null) {
        String logoUrl = this.dealNullVal(constFundAgencyDao.findFundAgencyLogoUrl(fund.getAgencyId()));
        if (StringUtils.isNotBlank(logoUrl)) {
          if (logoUrl.contains("http")) {
            defaultLogoUrl = logoUrl;
          } else {
            defaultLogoUrl = "/resmod" + logoUrl;
          }
        }
      }
      dataMap.put(DynTemplateConstant.LINK_IMAGE, defaultLogoUrl);
    }
    return dataMap;
  }

  /**
   * 处理基金信息
   * 
   * @param form
   * @param dataMap
   * @return
   */
  public HashMap handleAgencyInfo(DynamicForm form, HashMap<String, Object> dataMap) {
    ConstFundAgency agency = constFundAgencyDao.get(form.getResId());
    if (agency != null) {
      dataMap.put(DynTemplateConstant.LINK_TITLE_EN,
          StringUtils.isNotBlank(agency.getNameEn()) ? agency.getNameEn() : agency.getNameZh());
      dataMap.put(DynTemplateConstant.LINK_TITLE_ZH,
          StringUtils.isNotBlank(agency.getNameZh()) ? agency.getNameZh() : agency.getNameEn());
      dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, agency.getAddress());
      dataMap.put(DynTemplateConstant.PUB_DESCR_EN, agency.getEnAddress());
      // logo url
      String defaultLogoUrl = "/resmod/smate-pc/img/logo_instdefault.png";
      String logoUrl = this.dealNullVal(agency.getLogoUrl());
      if (StringUtils.isNotBlank(logoUrl)) {
        if (logoUrl.contains("http")) {
          defaultLogoUrl = logoUrl;
        } else {
          defaultLogoUrl = "/resmod" + logoUrl;
        }
      }
      dataMap.put(DynTemplateConstant.LINK_IMAGE, defaultLogoUrl);
    }
    return dataMap;
  }

  /**
   * 处理新闻信息
   * 
   * @param form
   * @param dataMap
   * @return
   */
  public HashMap handleNewsInfo(DynamicForm form, HashMap<String, Object> dataMap) {
    NewsBase news = newsBaseDao.get(form.getResId());
    if (news != null) {
      dataMap.put(DynTemplateConstant.LINK_TITLE_EN, news.getTitle());
      dataMap.put(DynTemplateConstant.LINK_TITLE_ZH, news.getTitle());
      dataMap.put(DynTemplateConstant.PUB_DESCR_ZH, news.getBrief());
      dataMap.put(DynTemplateConstant.PUB_DESCR_EN, news.getBrief());
      // logo url
      String defaultLogoUrl = "/resmod/smate-pc/img/logo_newsdefault.png";
      String logoUrl = this.dealNullVal(news.getImage());
      if (StringUtils.isNotBlank(logoUrl)) {
        if (logoUrl.contains("http")) {
          defaultLogoUrl = logoUrl;
        } else {
          defaultLogoUrl = "/resmod" + logoUrl;
        }
      }
      dataMap.put(DynTemplateConstant.LINK_IMAGE, defaultLogoUrl);
    }
    return dataMap;
  }

  /**
   * 处理空值字符串，null的转为""
   * 
   * @param val
   * @return
   */
  private String dealNullVal(Object val) {
    if (val != null && StringUtils.isNotBlank(val.toString())) {
      return val.toString();
    } else {
      return "";
    }
  }

  private ConstFundCategoryInfo buildFundInfo(ConstFundCategory fund) {
    ConstFundCategoryInfo info = new ConstFundCategoryInfo();
    // 查询基金资助机构
    ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(fund.getAgencyId());
    // 标题
    info.setZhTitle(StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn());
    info.setEnTitle(StringUtils.isNotBlank(fund.getNameEn()) ? fund.getNameEn() : fund.getNameZh());
    // 科技领域
    Map<String, String> area = buildFundScienceAreaInfo(fund.getId());
    if (area != null) {
      info.setZhScienceArea(area.get("zhNames"));
      info.setEnScienceArea(area.get("enNames"));
    }
    if (agency != null) {
      // 图片
      info.setLogoUrl(agency.getLogoUrl());
      info.setZhAgencyName(agency.getNameZh());
      info.setEnAgencyName(agency.getNameEn());
    }
    // 开始结束时间
    info.setStart(fund.getStartDate());
    info.setEnd(fund.getEndDate());
    info.setShowDate(this.dealNullVal(buildFundApplyTime(info)));
    this.buildFundInternationalInfo(info);
    return info;
  }

  /**
   * 构建基金科技领域信息
   * 
   * @param fundId
   * @return
   */
  private Map<String, String> buildFundScienceAreaInfo(Long fundId) {
    List<Long> scienceAreaIds = constFundCategoryDisDao.findFundDisciplineIds(fundId);
    Map<String, String> area = new HashMap<String, String>();
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {
      List<Integer> ids = new ArrayList<Integer>();
      for (Long id : scienceAreaIds) {
        ids.add(id.intValue());
      }
      area = this.buildFundScienceAreaInfo(ids);
    }
    return area;
  }

  /**
   * 构建科技领域信息
   * 
   * @param scienceAreaIds
   * @return
   */
  private Map<String, String> buildFundScienceAreaInfo(List<Integer> idList) {
    Map<String, String> area = new HashMap<String, String>();
    if (CollectionUtils.isNotEmpty(idList)) {
      StringBuffer zhScienceAreaNames = new StringBuffer();
      StringBuffer enScienceAreaNames = new StringBuffer();
      // 获取科技领域
      List<CategoryMapBase> list = categoryMapBaseDao.findCategoryByIds(idList);
      if (CollectionUtils.isNotEmpty(list)) {
        // 构建科技领域信息
        for (CategoryMapBase ca : list) {
          String zhTitle = StringUtils.isNotBlank(ca.getCategoryZh()) ? ca.getCategoryZh() : ca.getCategoryEn();
          String enTitle = StringUtils.isNotBlank(ca.getCategoryEn()) ? ca.getCategoryEn() : ca.getCategoryZh();
          if (StringUtils.isBlank(zhScienceAreaNames.toString())) {
            zhScienceAreaNames.append(zhTitle);
          } else {
            zhScienceAreaNames.append("," + zhTitle);
          }
          if (StringUtils.isBlank(enScienceAreaNames.toString())) {
            enScienceAreaNames.append(enTitle);
          } else {
            enScienceAreaNames.append("," + enTitle);
          }
        }
        area.put("zhNames", zhScienceAreaNames.toString());
        area.put("enNames", enScienceAreaNames.toString());
      }
    }
    return area;
  }

  // 构建起止时间
  private String buildFundApplyTime(ConstFundCategoryInfo fundinfo) {
    SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
    Date startTime = fundinfo.getStart();
    Date endTime = fundinfo.getEnd();
    String start = "";
    String end = "";
    if (startTime != null) {
      start = smf.format(startTime);
    }
    if (endTime != null) {
      end = smf.format(endTime);
    }
    if (StringUtils.isNotBlank(start) || StringUtils.isNotBlank(end)) {
      return start + " ~ " + end;
    } else {
      return start + end;
    }
  }

  /**
   * 构建基金国际化信息，分享的时候有用到
   */
  private void buildFundInternationalInfo(ConstFundCategoryInfo fund) {
    String zhFundAgency = LocaleTextUtils.getStrByLocale("zh_CN", fund.getZhAgencyName(), fund.getEnAgencyName());
    String zhScienceArea = LocaleTextUtils.getStrByLocale("zh_CN", fund.getZhScienceArea(), fund.getEnScienceArea());
    String enFundAgency = LocaleTextUtils.getStrByLocale("en_US", fund.getZhAgencyName(), fund.getEnAgencyName());
    String enScienceArea = LocaleTextUtils.getStrByLocale("en_US", fund.getZhScienceArea(), fund.getEnScienceArea());
    fund.setZhShowDesc(this.getFundShowDescByLocale(zhFundAgency, zhScienceArea, fund.getShowDate(), "zh_CN"));
    fund.setEnShowDesc(this.getFundShowDescByLocale(enFundAgency, enScienceArea, fund.getShowDate(), "en_US"));
  }

  private String getFundShowDescByLocale(String fundAgency, String scienceArea, String applyTime, String locale) {
    String showDesc = "";
    String joinStr = "zh_CN".equals(locale) ? "，" : ", ";
    if (StringUtils.isNotBlank(fundAgency)) {
      showDesc += fundAgency;
    }
    if (StringUtils.isNotBlank(scienceArea)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + scienceArea;
      } else {
        showDesc += scienceArea;
      }
    }
    if (StringUtils.isNotBlank(applyTime)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + applyTime;
      } else {
        showDesc += applyTime;
      }
    }
    return showDesc;
  }

  // 构建基金显示信息
  public void buildFundShowInfo(MsgShowInfo msi) {
    if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FUND.equals(msi.getSmateInsideLetterType())) {
      String locale = LocaleContextHolder.getLocale().toString();
      if ("en_US".equals(locale)) {
        msi.setShowFundTitle(
            StringUtils.isNotBlank(msi.getFundEnTitle()) ? msi.getFundEnTitle() : msi.getFundZhTitle());
      } else {
        msi.setShowFundTitle(
            StringUtils.isNotBlank(msi.getFundZhTitle()) ? msi.getFundZhTitle() : msi.getFundEnTitle());
      }
      String showDesc = "";
      if (StringUtils.isNotBlank(msi.getFundAgencyName())) {
        showDesc += msi.getFundAgencyName();
      }
      if (StringUtils.isNotBlank(msi.getFundScienceArea())) {
        if (StringUtils.isNotBlank(showDesc)) {
          showDesc += ", " + msi.getFundScienceArea();
        } else {
          showDesc += msi.getFundScienceArea();
        }
      }
      if (StringUtils.isNotBlank(msi.getFundApplyTime())) {
        if (StringUtils.isNotBlank(showDesc)) {
          showDesc += ", " + msi.getFundApplyTime();
        } else {
          showDesc += msi.getFundApplyTime();
        }
      }
      msi.setShowDesc(showDesc);
    }
  }

}
