package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.AcAwardCategoryDao;
import com.smate.center.batch.dao.sns.pub.AcAwardGradeDao;
import com.smate.center.batch.dao.sns.pub.AcAwardIssueInsDao;
import com.smate.center.batch.dao.sns.pub.AcCityDao;
import com.smate.center.batch.dao.sns.pub.AcConCityDao;
import com.smate.center.batch.dao.sns.pub.AcConfNameDao;
import com.smate.center.batch.dao.sns.pub.AcConfOrganizerDao;
import com.smate.center.batch.dao.sns.pub.AcDiscKeyEnDao;
import com.smate.center.batch.dao.sns.pub.AcDisciplineKeyDao;
import com.smate.center.batch.dao.sns.pub.AcFundCategoryDao;
import com.smate.center.batch.dao.sns.pub.AcInstitutionDao;
import com.smate.center.batch.dao.sns.pub.AcMyDisciplineKeyDao;
import com.smate.center.batch.dao.sns.pub.AcPatentOrgDao;
import com.smate.center.batch.dao.sns.pub.AcProvinceDao;
import com.smate.center.batch.dao.sns.pub.AcPsnDisciplineKeyDao;
import com.smate.center.batch.dao.sns.pub.AcPublisherDao;
import com.smate.center.batch.dao.sns.pub.AcThesisOrgDao;
import com.smate.center.batch.dao.sns.pub.ConstRegionDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcAwardCategory;
import com.smate.center.batch.model.sns.pub.AcAwardGrade;
import com.smate.center.batch.model.sns.pub.AcAwardIssueIns;
import com.smate.center.batch.model.sns.pub.AcCity;
import com.smate.center.batch.model.sns.pub.AcConCity;
import com.smate.center.batch.model.sns.pub.AcConfName;
import com.smate.center.batch.model.sns.pub.AcConfOrganizer;
import com.smate.center.batch.model.sns.pub.AcDiscKeyEn;
import com.smate.center.batch.model.sns.pub.AcDisciplineKey;
import com.smate.center.batch.model.sns.pub.AcFundCategory;
import com.smate.center.batch.model.sns.pub.AcInstitution;
import com.smate.center.batch.model.sns.pub.AcPatentOrg;
import com.smate.center.batch.model.sns.pub.AcPrjScheme;
import com.smate.center.batch.model.sns.pub.AcPrjSchemeAgency;
import com.smate.center.batch.model.sns.pub.AcProvince;
import com.smate.center.batch.model.sns.pub.AcPsnDisciplineKey;
import com.smate.center.batch.model.sns.pub.AcPublisher;
import com.smate.center.batch.model.sns.pub.AcThesisOrg;
import com.smate.center.batch.model.sns.pub.ConstDisciplineKey;
import com.smate.center.batch.model.sns.pub.ConstPosition;
import com.smate.center.batch.model.sns.pub.ConstRegion;
import com.smate.center.batch.util.pub.JsonUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.security.SecurityUtils;

import net.sf.ehcache.Element;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

/**
 * 智能提示service.
 * 
 * @author liqinghua
 * 
 */
@Service("autoCompleteService")
@Transactional(rollbackFor = Exception.class)
public class AutoCompleteServiceImpl implements AutoCompleteService {

  private static final long serialVersionUID = -4645322304028117669L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AcAwardCategoryDao acAwardCategoryDao;
  @Autowired
  private AcAwardGradeDao acAwardGradeDao;
  @Autowired
  private AcAwardIssueInsDao acAwardIssueInsDao;
  @Autowired
  private AcCityDao acCityDao;
  @Autowired
  private AcProvinceDao acProvinceDao;
  @Autowired
  private AcConCityDao acConstCityDao;
  @Autowired
  private AcConfNameDao acConfNameDao;
  @Autowired
  private AcConfOrganizerDao acConfOrganizerDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private AcInstitutionDao acInstitutionDao;
  @Autowired
  private AcPatentOrgDao acPatentOrgDao;
  @Autowired
  private AcThesisOrgDao acThesisOrgDao;
  @Autowired
  private AcPublisherDao acPublisherDao;
  @Autowired
  private AcDisciplineKeyDao acDisciplineKeyDao;
  @Autowired
  private AcMyDisciplineKeyDao acMyDisciplineKeyDao;
  @Autowired
  private AcDiscKeyEnDao acDiscKeyEnDao;
  @Autowired
  private AcPsnDisciplineKeyDao acpsnDisciplineKeyDao;
  @Autowired
  private AcFundCategoryDao acFundCategoryDao;
  @Autowired
  private ConstDisciplineManage constDisciplineManage;
  @Autowired
  private ConstPositionService constPositionService;
  @Autowired
  private PrjSchemeAgencyService prjSchemeAgencyService;
  @Autowired
  private PrjSchemeService prjSchemeService;
  @Autowired
  private CacheService cacheService;



  /**
   * 获取智能匹配奖励类别自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcAwardCategory(String startWith, int size) throws ServiceException {

    try {

      List<AcAwardCategory> list = acAwardCategoryDao.getAcAwardCategory(startWith, size);
      return covertJson(list);

    } catch (Exception e) {
      logger.error("getAcAwardCategory获取智能匹配奖励类别自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 转换成JSON.
   * 
   * @param list
   * @return
   */
  @SuppressWarnings("unchecked")
  public static String covertJson(List list) {

    // 不需要转换的属性
    String[] excludes =
        new String[] {"createAt", "query", "superId", "id", "zhName", "region", "superRegionId", "superRegion"};
    // 日期转换
    JsonConfig jsonConfig = JsonUtils.configJson(excludes, "yyyy-MM-dd");
    JSON json = JSONSerializer.toJSON(list, jsonConfig);
    return json.toString();
  }

  /**
   * 获取智能匹配奖励等级列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcAwardGrade(String startWith, int size) throws ServiceException {

    try {

      List<AcAwardGrade> list = acAwardGradeDao.getAcAwardGrade(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcAwardGrade获取智能匹配奖励等级列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配颁奖机构列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcAwardIssueIns(String startWith, int size) throws ServiceException {

    try {

      List<AcAwardIssueIns> list = acAwardIssueInsDao.getAcAwardIssueIns(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcAwardIssueIns获取智能匹配颁奖机构列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配城市自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcCity(String startWith, int size) throws ServiceException {

    try {

      List<AcCity> list = acCityDao.getAcCity(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcCity获取智能匹配城市自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配城市自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcConstCity(String startWith, Long prvId, int size) throws ServiceException {
    try {

      List<AcConCity> list = acConstCityDao.getAcConstCity(startWith, prvId, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcConstCity获取智能匹配省份自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配省份自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcProvince(String startWith, int size) throws ServiceException {

    try {

      List<AcProvince> list = acProvinceDao.getAcProvince(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcProvince获取智能匹配省份自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配会议名称自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcConfName(String startWith, int size) throws ServiceException {

    try {

      List<AcConfName> list = acConfNameDao.getAcConfName(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcConfName获取智能匹配会议名称自动提示列表，返回最多size条记录startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配会议组织者自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcConfOrganizer(String startWith, int size) throws ServiceException {

    try {

      List<AcConfOrganizer> list = acConfOrganizerDao.getAcConfOrganizer(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcConfOrganizer获取智能匹配会议组织者自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配国别列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcCountryRegion(String startWith, int size) throws ServiceException {

    try {

      List<ConstRegion> list = constRegionDao.getAcCountryRegion(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcCountryRegion获取智能匹配国别列表，只读size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配单位列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  @SuppressWarnings("unchecked")
  public String getAcInstitution(String startWith, String excludes, int size) throws ServiceException {

    try {
      List<AcInstitution> list = null;
      Element element = (Element) cacheService.get("AcInstitution", "Element");
      if (element != null) {

        list = (List<AcInstitution>) element.getValue();

      } else {

        list = acInstitutionDao.getAcInstitution(startWith, excludes, size);
        if (list != null && list.size() > 0) {
          Element cacheEle = new Element(startWith, list);
          cacheService.put("AcInstitution", "Element", cacheEle);
        }
      }
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcInstitution获取智能匹配单位列表，只读size条记录startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  @Override
  public String getStaAcInstitution(String startWith, Long prvId, Integer ignorePriv, int size)
      throws ServiceException {
    try {
      if (ignorePriv == null) {
        ignorePriv = 0;
      }
      List<AcInstitution> list = acInstitutionDao.getStaAcInstitution(startWith, prvId, null, null, ignorePriv, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcInstitution获取智能匹配单位列表，只读size条记录startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  @Override
  public String getStaAcInstitution(String startWith, Long prvId, Long cyId, Long disId, Integer ignorePriv, int size)
      throws ServiceException {
    try {
      if (ignorePriv == null) {
        ignorePriv = 0;
      }
      List<AcInstitution> list = acInstitutionDao.getStaAcInstitution(startWith, prvId, cyId, disId, ignorePriv, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcInstitution获取智能匹配单位列表，只读size条记录startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 智能匹配单位列表，如果未输入任何数据，则查询本单位数据.
   * 
   * @param startWith
   * @param excludes
   * @param size
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcAuthorInstitution(String startWith, String excludes, int size) throws ServiceException {

    try {
      if (StringUtils.isBlank(startWith)) {

        AcInstitution ins = null;
        Long insId = SecurityUtils.getCurrentInsId();

        if (StringUtils.isBlank(excludes) || ("," + excludes + ",").indexOf("," + insId.toString() + ",") == -1) {
          ins = acInstitutionDao.getAcInstitution(insId);
        }
        if (ins == null) {
          return "[]";
        } else {
          List<AcInstitution> list = new ArrayList<AcInstitution>();
          list.add(ins);
          return covertJson(list);
        }

      } else {
        List<AcInstitution> list = acInstitutionDao.getAcInstitution(startWith, excludes, size);
        return covertJson(list);
      }

    } catch (DaoException e) {
      logger.error("getAcInstitution获取智能匹配国别列表，只读size条记录startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配发证单位自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcPatentOrg(String startWith, int size) throws ServiceException {

    try {

      List<AcPatentOrg> list = acPatentOrgDao.getAcPatentOrg(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcPatentOrg获取智能匹配发证单位自动提示列表，返回最多size条记录startWith:", e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配颁发单位自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcThesisOrg(String startWith, int size) throws ServiceException {

    try {

      List<AcThesisOrg> list = acThesisOrgDao.getAcThesisOrg(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcThesisOrg获取智能匹配颁发单位自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取智能匹配出版社自动提示列表，返回最多size条记录.
   * 
   * @param startWith
   * @return
   * @throws ServiceException @
   */
  @Override
  public String getAcPublisher(String startWith, int size) throws ServiceException {

    try {

      List<AcPublisher> list = acPublisherDao.getAcPublisher(startWith, size);
      return covertJson(list);

    } catch (DaoException e) {
      logger.error("getAcPublisher 获取智能匹配出版社自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  /**
   * 通过任务保存自动提示信息.
   * 
   * @param text
   * @param type
   * @throws ServiceException @
   */
  @Override
  public void saveAcDataByTask(String text, String type) throws ServiceException {

    try {

      if ("award_category".equalsIgnoreCase(type)) {

        acAwardCategoryDao.saveAcAwardCategory(text);
      } else if ("award_grade".equalsIgnoreCase(type)) {

        acAwardGradeDao.saveAcAwardGrade(text);
      } else if ("issue_ins_name".equalsIgnoreCase(type)) {

        acAwardIssueInsDao.saveAcAwardIssueIns(text);
      } else if ("city".equalsIgnoreCase(type)) {

        acCityDao.saveAcCity(text);
      } else if ("conf_name".equalsIgnoreCase(type)) {

        acConfNameDao.saveAcConfName(text);
      } else if ("conf_organizer".equalsIgnoreCase(type)) {

        acConfOrganizerDao.saveAcConfOrganizer(text);
      } else if ("patent_org".equalsIgnoreCase(type)) {

        acPatentOrgDao.saveAcPatentOrg(text);
      } else if ("issue_org".equalsIgnoreCase(type)) {

        acThesisOrgDao.saveAcThesisOrg(text);
      } else if ("publisher".equalsIgnoreCase(type)) {
        acPublisherDao.saveAcPublisher(text);
      }
    } catch (DaoException e) {
      logger.error("saveAcDataByTask 通过任务保存自动提示信息出错text:" + text + " type:" + type, e);
      throw new ServiceException();
    }

  }

  @Override
  public String getDiscKeyWords(String startWith, String excludes, int size) throws ServiceException {
    try {

      List<ConstDisciplineKey> list = this.constDisciplineManage.findAutoCdKey(startWith, excludes, size);
      if (list == null) {
        return "";
      }
      return JSONSerializer.toJSON(list).toString();

    } catch (Exception e) {
      logger.error("getDiscKeyWords智能匹配学科关键字列表startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  @Override
  public String getDiscKeyWordsEn(String startWith, Long keyId, int size) throws ServiceException {
    try {
      List<AcDiscKeyEn> list = this.acDiscKeyEnDao.getAcDiscKeyEn(startWith, keyId, size);
      if (list == null) {
        return "";
      }
      return JSONSerializer.toJSON(list).toString();

    } catch (Exception e) {
      logger.error("getDiscKeyWordsEn智能匹配学科关键字列表startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  @Override
  public String getAcPosition(String startWith, int size) throws ServiceException {
    try {
      List<ConstPosition> list = constPositionService.getPosLike(startWith, size);
      if (list == null) {
        return "";
      }
      return JSONSerializer.toJSON(list).toString();

    } catch (Exception e) {
      logger.error("智能匹配职务列表startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  @Override
  public String getAcPrjSchemeAgency(String startStr, int size, String language) throws ServiceException {
    try {

      List<AcPrjSchemeAgency> list = prjSchemeAgencyService.getAcPrjSchemeAgency(startStr, size);
      if (CollectionUtils.isEmpty(list)) {
        return "";
      }
      List<AcPrjSchemeAgency> list2 = new ArrayList<AcPrjSchemeAgency>();
      for (AcPrjSchemeAgency obj : list) {
        AcPrjSchemeAgency obj2 = new AcPrjSchemeAgency();
        obj2.setCode(obj.getCode());
        obj2.setOrderCode(obj.getOrderCode());
        if ("zh".equalsIgnoreCase(language)) {
          obj2.setName(obj.getName());
        } else {
          if (StringUtils.isNotBlank(obj.getEnName())) {
            obj2.setName(obj.getEnName());
          }
        }
        if (StringUtils.isNotBlank(obj2.getName())) {
          list2.add(obj2);
        }
      }
      return JSONSerializer.toJSON(list2).toString();
    } catch (Exception e) {
      logger.error("项目资助机构列表startWith:" + startStr, e);
      throw new ServiceException();
    }

  }

  @Override
  public String getAcPrjScheme(String startStr, Long agencyId, int size, String language) throws ServiceException {
    try {

      List<AcPrjScheme> list = prjSchemeService.getAcPrjScheme(startStr, agencyId, size);
      if (list == null) {
        return "";
      }
      for (AcPrjScheme obj : list) {
        if ("zh".equalsIgnoreCase(language)) {
          obj.setName(obj.getName());
        } else {
          obj.setName(obj.getEnName());
        }
      }
      return JSONSerializer.toJSON(list).toString();
    } catch (Exception e) {
      logger.error("项目资助类别列表startWith:" + startStr, e);
      throw new ServiceException();
    }
  }

  /**
   * 获取单位信息.
   * 
   * @param name
   * @return
   * @throws ServiceException @
   */
  @Override
  public AcInstitution getAcInstitutionByName(String name) throws ServiceException {
    try {

      return this.acInstitutionDao.getByName(name);

    } catch (Exception e) {
      logger.error("获取单位信息", e);
      throw new ServiceException();
    }
  }

  /**
   * @param autoCompleteInsNameCache the autoCompleteInsNameCache to set
   */
  public void setCacheService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  @Override
  public String getAcDisciplineKey(String startWith, Long disId, int size) {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      List<AcDisciplineKey> list = acDisciplineKeyDao.getAcDisciplineKey(startWith, disId, psnId, size);
      Locale locale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(locale)) {
        for (AcDisciplineKey obj : list) {
          obj.setKeyWord(obj.getEnKeyWord() == null ? obj.getZhKeyWord() : obj.getEnKeyWord());
        }
      } else {
        for (AcDisciplineKey obj : list) {
          obj.setKeyWord(obj.getZhKeyWord() == null ? obj.getEnKeyWord() : obj.getZhKeyWord());
        }

      }
      return JSONSerializer.toJSON(list).toString();
    } catch (DaoException e) {
      logger.error("关键词自动提示查询时出错啦！", e);
      return "";
    }
  }

  @Override
  public String getAcDisciplineKey(String startWith, Long disId, int viewType, int size) {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      List<AcDisciplineKey> list = acDisciplineKeyDao.getAcDisciplineKey(startWith, disId, viewType, psnId, size);
      Locale locale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(locale)) {
        for (AcDisciplineKey obj : list) {
          obj.setKeyWord(obj.getEnKeyWord() == null ? obj.getZhKeyWord() : obj.getEnKeyWord());
        }
      } else {
        for (AcDisciplineKey obj : list) {
          obj.setKeyWord(obj.getZhKeyWord() == null ? obj.getEnKeyWord() : obj.getZhKeyWord());
        }

      }
      return JSONSerializer.toJSON(list).toString();
    } catch (DaoException e) {
      logger.error("关键词自动提示查询时出错啦！", e);
      return "";
    }
  }

  @Override
  public String getAcPsnDisciplineKey(String startWith, Long psnDisId, int size) {
    try {
      List<AcPsnDisciplineKey> list = acpsnDisciplineKeyDao.getAcPsnDisciplineKey(startWith, psnDisId, size);
      Locale locale = LocaleContextHolder.getLocale();
      if (Locale.US.equals(locale)) {
        for (AcPsnDisciplineKey obj : list) {
          obj.setKeyWord(obj.getEnKeyWord() == null ? obj.getZhKeyWord() : obj.getEnKeyWord());
        }
      } else {
        for (AcPsnDisciplineKey obj : list) {
          obj.setKeyWord(obj.getZhKeyWord() == null ? obj.getEnKeyWord() : obj.getZhKeyWord());
        }

      }
      return JSONSerializer.toJSON(list).toString();
    } catch (DaoException e) {
      logger.error("个人关键词自动提示查询时出错啦！", e);
      return "";
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getAcMyDisKeyAutoDis(String startStr, int size) {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Locale locale = LocaleContextHolder.getLocale();
      List list = acMyDisciplineKeyDao.getAcMyDisKeyAutoDis(psnId, startStr, size);
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      if (CollectionUtils.isNotEmpty(list)) {
        for (Object object : list) {
          Map map = (Map) object;
          Long disId = Long.valueOf(String.valueOf(map.get("ID")));
          String zhName = String.valueOf(map.get("ZH_NAME"));
          String enName = String.valueOf(map.get("EN_NAME"));
          String code = String.valueOf(map.get("DISC_CODE"));
          Map<String, Object> newMap = new HashMap<String, Object>();
          newMap.put("id", disId);
          if (Locale.US.equals(locale)) {
            newMap.put("name", code + "-" + enName);
          } else {
            newMap.put("name", code + "-" + zhName);
          }
          mapList.add(newMap);
        }
      }
      return JSONSerializer.toJSON(mapList).toString();
    } catch (Exception e) {
      logger.error("查询自己填写的关键词提示相关学科领域出错啦！", e);
      return "";
    }

  }

  @Override
  public String getAcFundCategory(String startStr, int agencyFlag, int size) {
    try {
      List<AcFundCategory> list = acFundCategoryDao.getAcFundCategory(startStr, agencyFlag, size);
      JSONArray jsonArray = new JSONArray();
      for (AcFundCategory fc : list) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("code", fc.getId());
        String name = "";
        if (StringUtils.isAsciiPrintable(startStr)) {
          name = (fc.getEnCategory() != null && !"".equals(fc.getEnCategory())) ? fc.getEnCategory() : fc.getCategory();
        } else {
          name = (fc.getCategory() != null && !"".equals(fc.getCategory())) ? fc.getCategory() : fc.getEnCategory();
        }
        jsonObj.put("name", name);
        jsonObj.put("category", fc.getCategory());
        jsonObj.put("enCategory", fc.getEnCategory());
        jsonObj.put("agencyFlag", fc.getAgencyFlag());
        jsonObj.put("fullCategory", fc.getFullCategory());
        jsonObj.put("enFullCategory", fc.getEnFullCategory());
        jsonArray.add(jsonObj);
      }
      return jsonArray.toString();
    } catch (DaoException e) {
      logger.error("基金申请计划类别自动提示查询时出错啦！", e);
      return "";
    }
  }
}
