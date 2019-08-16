package com.smate.web.psn.service.autocomplete;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstKeyDiscDao;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.dao.consts.ConstDictionaryDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.autocomplete.AcAwardCategoryDao;
import com.smate.web.psn.dao.autocomplete.AcAwardGradeDao;
import com.smate.web.psn.dao.autocomplete.AcAwardIssueInsDao;
import com.smate.web.psn.dao.autocomplete.AcConfNameDao;
import com.smate.web.psn.dao.autocomplete.AcConfOrganizerDao;
import com.smate.web.psn.dao.autocomplete.AcInsUnitDao;
import com.smate.web.psn.dao.autocomplete.AcInstitutionDao;
import com.smate.web.psn.dao.autocomplete.AcPatentOrgDao;
import com.smate.web.psn.dao.autocomplete.AcPublisherDao;
import com.smate.web.psn.dao.autocomplete.AcThesisOrgDao;
import com.smate.web.psn.dao.autocomplete.BaseJournalPdwhDao;
import com.smate.web.psn.dao.autocomplete.DisciplineDao;
import com.smate.web.psn.dao.autocomplete.JournalDAO;
import com.smate.web.psn.dao.cooperation.CooperationDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.autocomplete.AutoCompleteForm;
import com.smate.web.psn.model.autocomplete.AcAwardCategory;
import com.smate.web.psn.model.autocomplete.AcAwardGrade;
import com.smate.web.psn.model.autocomplete.AcAwardIssueIns;
import com.smate.web.psn.model.autocomplete.AcConfName;
import com.smate.web.psn.model.autocomplete.AcConfOrganizer;
import com.smate.web.psn.model.autocomplete.AcInsUnit;
import com.smate.web.psn.model.autocomplete.AcInstitution;
import com.smate.web.psn.model.autocomplete.AcJournal;
import com.smate.web.psn.model.autocomplete.AcPatentOrg;
import com.smate.web.psn.model.autocomplete.AcPublisher;
import com.smate.web.psn.v8pub.dao.pdwh.pub.PubPdwhDetailDAO;
import com.smate.web.v8pub.dom.PubPdwhDetailDOM;

/**
 * 获取自动填充的数据service实现类
 * 
 * @author zjh
 *
 */
@Service(value = "autoCompleteSnsService")
@Transactional(rollbackFor = Exception.class)
public class AutoCompleteSnsServiceImpl implements AutoCompleteSnsService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DisciplineDao disciplineDao;
  @Autowired
  private AcInsUnitDao acInsUnitDao;
  @Autowired
  private AcInstitutionDao acInstitutionDao;
  @Autowired
  private ConstDictionaryDao constDictionaryDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private CooperationDao cooperationDao;
  @Autowired
  private AcAwardCategoryDao acAwardCategoryDao;
  @Autowired
  private AcAwardGradeDao acAwardGradeDao;
  @Autowired
  private AcAwardIssueInsDao acAwardIssueInsDao;
  @Autowired
  private AcConfNameDao acConfNameDao;
  @Autowired
  private AcConfOrganizerDao acConfOrganizerDao;
  @Autowired
  private BaseJournalPdwhDao baseJournalPdwhDao;
  @Autowired
  private SnsCacheService cacheService;
  @Autowired
  private AcPatentOrgDao acPatentOrgDao;
  @Autowired
  private AcThesisOrgDao acThesisOrgDao;
  @Autowired
  private AcPublisherDao acPublisherDao;
  @Autowired
  private ConstKeyDiscDao constKeyDiscDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private SolrIndexService solrIndexService;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private JournalDAO journalDAO;

  /**
   * 获取自动填充的研究领域
   */
  @Override
  public String autoCompleteDiscipline(int maxSize, String startWith) throws Exception {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      List<Map<Long, String>> disciplineLists = disciplineDao.getDisciplineLists(startWith, maxSize);
      if (disciplineLists == null)
        return "";
      /*
       * JSONArray jsonArray = new JSONArray(); boolean isEnglish =
       * StringUtils.isAsciiPrintable(startWith); for (Discipline keyword : disciplineLists) { JSONObject
       * jsonObj = new JSONObject(); jsonObj.put("code", ""); jsonObj.put("name",
       * keyword.getSearchdiscipline()); jsonArray.add(jsonObj); } return jsonArray.toString();
       */
      return JacksonUtils.listToJsonStr(disciplineLists);
    } catch (Exception e) {
      logger.error("getPersnoalKeywords编辑个人主页关键词列表startWith:" + startWith, e);
      throw new Exception();
    }
  }

  /**
   * 获取自动填充的部门
   */
  @Override
  public List<AcInsUnit> getAcInsUnit(AutoCompleteForm form) throws Exception {
    return acInsUnitDao.getAcInsUnit(form.getSearchKey(), form.getInsName(), 5);
  }

  /**
   * 获取自动填充的机构名称
   */
  @Override
  public List<ConstRegion> getAcregion(String searchKey, String excludeIns, int size) throws Exception {
    return constRegionDao.getAcregion(searchKey, excludeIns, size);
  }

  /**
   * xiexing 2019-1-22 获取自动填充的地区名称逻辑改为 : 1、获取当前语言环境，优先匹配当前语言(中英文、首字母、拼音) 2、先显示高校、再科研机构、再其他
   * 3、科研机构按照psn_count排序
   */
  @Override
  public List<AcInstitution> getAcInstitution(String searchKey, String excludeIns, int size) throws Exception {
    List<AcInstitution> list = new ArrayList<AcInstitution>();
    // 高校
    List<AcInstitution> universities = acInstitutionDao.getAcInstitution(searchKey, excludeIns, size, 1);
    list.addAll(universities);
    if (list.size() < 5) {
      // 科研机构
      List<AcInstitution> researches = acInstitutionDao.getAcInstitution(searchKey, excludeIns, size, 2);
      list.addAll(researches);
    }
    if (list.size() < 5) {
      // 其他
      List<AcInstitution> others = acInstitutionDao.getAcInstitution(searchKey, excludeIns, size, 3);
      list.addAll(others);
    }
    if (CollectionUtils.isNotEmpty(list)) {
      Locale locale = LocaleContextHolder.getLocale();
      for (AcInstitution ac : list) {
        ConstRegion region = new ConstRegion();
        if (ac.getRegionId() != null) {
          region = getConstRegion(ac.getRegionId(), region);
        }
        if (locale.US.equals(locale)) {
          ac.setName(ac.getEnName() == null ? ac.getZhName() : ac.getEnName());
          ac.setCountry(region.getEnName() == null ? ac.getZhName() : ac.getEnName());
        } else {
          ac.setName(ac.getZhName() == null ? ac.getEnName() : ac.getZhName());
          ac.setCountry(region.getZhName() == null ? ac.getEnName() : ac.getZhName());
        }
      }
    }
    return list;
  }

  /**
   * 获取国别
   */
  public ConstRegion getConstRegion(Long regionId, ConstRegion region) {
    ConstRegion newRegion = constRegionDao.findRegionNameById(regionId);
    if (Objects.nonNull(newRegion)) {
      if (newRegion.getSuperRegionId() == null) {
        return newRegion;
      } else {
        ConstRegion superRegion = constRegionDao.findRegionNameById(newRegion.getSuperRegionId());
        return superRegion == null ? newRegion : superRegion;
      }
    }
    return region;
  }

  /**
   * 获取自动填充的学历
   */
  @Override
  public List<ConstDictionary> getAcDegree(String category, String searchKey) throws Exception {
    return constDictionaryDao.getAcDegree(category, searchKey);
  }

  @Override
  public List<Map<String, Object>> searchConstRegionInfo(String searchKey, String category, Integer size)
      throws Exception {
    if (size == null || size == 0) {
      size = 10;
    }
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      if (StringUtils.isNotBlank(searchKey)) {
        // 判断搜索的字符串是否是纯中文，是就提示中文的地区信息，否则提示英文地区信息
        String reg = "[\\u4e00-\\u9fa5]+";
        boolean isChinese = searchKey.matches(reg);
        List<ConstRegion> regionList = constRegionDao.searchForConstRegion(searchKey, size);
        if (CollectionUtils.isNotEmpty(regionList)) {
          for (ConstRegion region : regionList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", region.getId());
            map.put("name", this.buildRegionStr(region, isChinese));
            mapList.add(map);
          }
        }
      }
    } catch (Exception e) {
      logger.error("构建地区信息出错，searchKey=" + searchKey, e);
      throw new ServiceException(e);
    }
    return mapList;
  }

  /**
   * 构建地区信息
   * 
   * @param region constRegion对象
   * @param isChinese 搜索的字符串是否是纯中文
   * @return
   */
  private String buildRegionStr(ConstRegion region, boolean isChinese) {
    String zhPsnRegionName = "";
    String enPsnRegionName = "";
    if (region != null) {
      zhPsnRegionName = region.getZhName();
      enPsnRegionName = region.getEnName();
      Long regionId = region.getSuperRegionId();
      int i = 0;
      while (regionId != null) {
        // 防止死循环
        if (i++ > 6) {
          break;
        }
        ConstRegion cre = this.constRegionDao.findRegionNameById(regionId);
        if (cre != null) {
          regionId = cre.getSuperRegionId();
          if (!isChinese) {
            if (StringUtils.isNotBlank(enPsnRegionName)) {
              enPsnRegionName = cre.getEnName() + ", " + enPsnRegionName;
            } else {
              enPsnRegionName = cre.getEnName();
            }
          } else {
            if (StringUtils.isNotBlank(zhPsnRegionName)) {
              zhPsnRegionName = cre.getZhName() + ", " + zhPsnRegionName;
            } else {
              zhPsnRegionName = cre.getZhName();
            }
          }
        } else {
          break;
        }
      }
    }
    return isChinese ? zhPsnRegionName : enPsnRegionName;
  }

  @Override
  public List<Map<String, Object>> searchPsnCooperator(AutoCompleteForm form, int size) throws Exception {
    // 当前人
    Long psnId = SecurityUtils.getCurrentUserId();
    String searchKey = form.getSearchKey();
    if (size == 0) {
      size = 10;
    }
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      if (StringUtils.isNotBlank(searchKey)) {
        // 判断搜索的字符串是否是纯中文
        /*
         * String reg = "[\\u4e00-\\u9fa5]+"; boolean isChinese = searchKey.matches(reg);
         */
        boolean isChinese = !StringUtils.isAsciiPrintable(searchKey);
        List<Long> excludePsnIds = buildExcludePsnIdList(form.getDes3PsnId());
        List<Person> personList = cooperationDao.searchPsnCooperator(searchKey, excludePsnIds, psnId, size);
        // 也匹配当前人
        Person currenPerson = personDao.findPersonByPsnId(psnId);
        if (currenPerson.getName() != null && currenPerson.getName().contains(searchKey)
            || currenPerson.getEnName() != null && currenPerson.getEnName().contains(searchKey)) {
          personList.add(currenPerson);
        }
        if (excludePsnIds != null && excludePsnIds.size() > 0) {
          for (Long p : excludePsnIds) {
            if (p.toString().contains(psnId.toString())) {
              personList.remove(currenPerson);
            }
          }
        }
        if (CollectionUtils.isNotEmpty(personList)) {
          for (Person person : personList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", Des3Utils.encodeToDes3(person.getPersonId().toString()));
            boolean zhContain =
                person.getName() != null ? person.getName().toUpperCase().contains(searchKey.toUpperCase()) : false;
            boolean enContain =
                person.getEname() != null ? person.getEname().toUpperCase().contains(searchKey.toUpperCase()) : false;
            if (zhContain && enContain) {
              if (isChinese) {
                map.put("name", person.getName() == null ? person.getEname() : person.getName());
              } else {
                map.put("name", person.getEname() == null ? person.getName() : person.getEname());
              }
            } else if (enContain) {
              map.put("name", person.getEname() == null ? person.getName() : person.getEname());
              isChinese = false;
            } else {
              map.put("name", person.getName() == null ? person.getEname() : person.getName());
              isChinese = true;
            }

            Long insId = person.getInsId();
            String insName = getInsName(insId, isChinese);// 获取单位名称
            if (StringUtils.isBlank(insName)) {
              insName = person.getInsName() == null ? "" : person.getInsName();
            }

            String email = person.getEmail() == null ? "" : person.getEmail();
            Map<String, Object> otherMap = new HashMap<String, Object>();
            otherMap.put("des3PsnId", Des3Utils.encodeToDes3(person.getPersonId().toString()));
            otherMap.put("insName", insName);
            otherMap.put("insId", insId);
            otherMap.put("email", email);
            otherMap.put("owner", psnId.equals(person.getPersonId()));
            map.put("other", JacksonUtils.mapToJsonStr(otherMap));
            mapList.add(map);
          }
        }
      }
    } catch (Exception e) {
      logger.error("构建地区信息出错，searchKey=" + searchKey, e);
      throw new ServiceException(e);
    }
    return mapList;
  }

  // 构建排除的人员id
  private List<Long> buildExcludePsnIdList(String psnIds) {
    List<Long> excludePsnIds = null;
    if (StringUtils.isNotBlank(psnIds)) {
      String[] splitPsnIds = psnIds.split(",");
      excludePsnIds = new ArrayList<>();
      for (String psnIdStr : splitPsnIds) {
        long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(psnIdStr));
        excludePsnIds.add(psnId);
      }
      if (excludePsnIds.size() > 1000) {
        excludePsnIds.subList(0, 1000);
      }
    }
    return excludePsnIds;
  }

  /**
   * 获取单位名称
   * 
   * @param insId
   * @param isChinese
   * @return
   */
  private String getInsName(Long insId, boolean isChinese) {
    String insName = "";
    if (insId != null) {
      Institution ins = institutionDao.getInsByIdNotStatus(insId);
      if (ins != null) {
        if (isChinese) {
          insName = ins.getZhName() == null ? ins.getEnName() : ins.getZhName();
        } else {
          insName = ins.getEnName() == null ? ins.getZhName() : ins.getEnName();
        }
      }
    }
    return insName;
  }

  @Override
  public List<Map<String, Object>> getAcAwardCategory(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcAwardCategory> list = acAwardCategoryDao.getAcAwardCategory(startWith, maxsize);

      String localeStr = !StringUtils.isAsciiPrintable(startWith) ? "zh_CN" : "en_US";
      if (StringUtils.isBlank(startWith)) {
        localeStr = LocaleContextHolder.getLocale().getLanguage();
      }
      for (AcAwardCategory acAwardCategory : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acAwardCategory.getCode());
        map.put("name",
            LocaleTextUtils.getStrByLocale(localeStr, acAwardCategory.getName(), acAwardCategory.getNameEn()));
        resultList.add(map);
      }

    } catch (DaoException e) {
      logger.error("getAcAwardCategory获取智能匹配奖励类别自动提示列表，返回最多size条记录出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcAwardGrade(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcAwardGrade> list = acAwardGradeDao.getAcAwardGrade(startWith, maxsize);
      String localeStr = !StringUtils.isAsciiPrintable(startWith) ? "zh_CN" : "en_US";
      if (StringUtils.isBlank(startWith)) {
        localeStr = LocaleContextHolder.getLocale().getLanguage();
      }
      for (AcAwardGrade acAwardGrade : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acAwardGrade.getCode());
        map.put("name", LocaleTextUtils.getStrByLocale(localeStr, acAwardGrade.getName(), acAwardGrade.getNameEn()));
        resultList.add(map);
      }
    } catch (DaoException e) {
      logger.error("getAcAwardGrade获取奖励等级列表出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Deprecated
  @Override
  public List<Map<String, Object>> getAcAwardIssueIns(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcAwardIssueIns> list = acAwardIssueInsDao.getAcAwardIssueIns(startWith, maxsize);
      for (AcAwardIssueIns acAwardIssueIns : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acAwardIssueIns.getCode());
        map.put("name", acAwardIssueIns.getName());
        resultList.add(map);
      }
    } catch (DaoException e) {
      logger.error("getAcAwardIssueIns获取颁奖机构列表出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcConfName(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcConfName> list = acConfNameDao.getAcConfName(startWith, maxsize);
      for (AcConfName acConfName : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acConfName.getCode());
        map.put("name", acConfName.getName());
        resultList.add(map);
      }
    } catch (DaoException e) {
      logger.error("getAcConfName会议名称自动提示出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcConfOrganizer(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcConfOrganizer> list = acConfOrganizerDao.getAcConfOrganizer(startWith, maxsize);
      for (AcConfOrganizer acConfOrganizer : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acConfOrganizer.getCode());
        map.put("name", acConfOrganizer.getName());
        resultList.add(map);
      }
    } catch (DaoException e) {
      logger.error("getAcAwardGrade获取会议组织者自动提示列表出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcJournal(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      List<AcJournal> list1 = null;
      // 英文期刊去除空格 就找不到了 SCM-23673 2019-03
      String startWithNoBlank = startWith.trim();
      if (!StringUtils.isAsciiPrintable(startWith)) {
        startWithNoBlank = startWith.replaceAll(" ", "");
      }
      Serializable cachList1 = cacheService.get("baseJournalSns", startWithNoBlank);
      if (cachList1 != null) {
        list1 = (List<AcJournal>) cachList1;
      } else {
        list1 = baseJournalPdwhDao.getAcJournal(startWith, maxsize);
        cacheService.put("baseJournalSns", 60 * 10, startWithNoBlank, (Serializable) list1);
      }

      if (CollectionUtils.isNotEmpty(list1)) {
        for (AcJournal acJournal : list1) {
          Map<String, Object> map = new HashMap<>();
          map.put("code", acJournal.getCode());
          map.put("name", acJournal.getName());
          map.put("issn", acJournal.getIssn());
          map.put("from", "journal");
          map.put("other", JacksonUtils.mapToJsonStr(map));
          resultList.add(map);
        }
      }
      // 2018-09-10 添加期刊
      Locale locale = LocaleContextHolder.getLocale();
      Map<String, Object> map = new HashMap<>();
      if (locale.equals(Locale.CHINA) || locale.equals(Locale.CHINESE)) {
        map.put("code", "AddNewJournal");
        map.put("name", "添加新期刊");
        map.put("issn", "添加新期刊");
      } else {
        map.put("code", "AddNewJournal");
        map.put("name", "Add New Journal");
        map.put("issn", "Add New Journal");
      }
      // SCM-21921 优先基准库的，然后是本人之前录入过的并且是matchBaseJnlId为空的
      if (resultList != null && resultList.size() < 4) {
        list1 = journalDAO.queryJournal(startWithNoBlank, maxsize, psnId);
        if (CollectionUtils.isNotEmpty(list1)) {
          for (AcJournal acJournal : list1) {
            Map<String, Object> acmap = new HashMap<>();
            acmap.put("code", acJournal.getCode());
            acmap.put("name", acJournal.getName());
            acmap.put("issn", StringUtils.isNotBlank(acJournal.getIssn()) ? acJournal.getIssn() : "");
            acmap.put("other", JacksonUtils.mapToJsonStr(acmap));
            acmap.put("from", "journal");
            resultList.add(acmap);
          }
        }
      }
      if (resultList != null && resultList.size() >= 5) {
        resultList = resultList.subList(0, 4);
      }
      resultList.add(map);
    } catch (DaoException e) {
      logger.error("getAcJournal 获取智能匹配期刊列表出错startWith:" + startWith, e);
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcPatentOrg(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcPatentOrg> list = acPatentOrgDao.getAcPatentOrg(startWith, maxsize);
      for (AcPatentOrg acPatentOrg : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acPatentOrg.getCode());
        map.put("name", acPatentOrg.getName());
        resultList.add(map);
      }
    } catch (DaoException e) {
      logger.error("getAcPatentOrg发证单位自动提示列表出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcThesisOrg(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    try {
      // SCM-18901 改为从单位表中大学列表中提示
      List<Institution> list = institutionDao.getCollegeInstitution(startWith, maxsize);
      for (Institution ins : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", ins.getId());
        if (isChinese) {
          map.put("name", ins.getZhName());
        } else {
          map.put("name", ins.getEnName());
        }
        resultList.add(map);
      }

    } catch (Exception e) {
      logger.error("颁发单位自动提示列表出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<Map<String, Object>> getAcPublisher(String startWith, int maxsize) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      List<AcPublisher> list = acPublisherDao.getAcPublisher(startWith, maxsize);
      for (AcPublisher acPublisher : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", acPublisher.getCode());
        map.put("name", acPublisher.getName());
        resultList.add(map);
      }

    } catch (DaoException e) {
      logger.error("getAcPublisher出版社自动提示列表出错startWith:" + startWith, e);
      throw new ServiceException();
    }
    return resultList;
  }

  @Override
  public List<ConstKeyDisc> getConstKeyDiscs(String seachKey, Integer size) throws Exception {
    return constKeyDiscDao.findKeys(seachKey, size);
  }

  @Override
  public List<Map<String, Object>> getAcTitle(String searchKey, String type) {
    // 特殊字符处理 排除这五个 _ . / -" " 字符 不过滤
    String searchTitle = "";
    if (FilterAllSpecialCharacter.isExcludedChars(searchKey)) {
      searchTitle = "";
    } else {
      searchTitle = FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchKey);
    }
    return getTitleAcSame(searchTitle, type);
  }

  /**
   * 获取标题自动补全的公用方法 hht
   * 
   * @param searchKey
   * @param type 成果的类型
   * @return
   */
  public List<Map<String, Object>> getTitleAcSame(String searchKey, String type) {
    Boolean isZh = false;
    if (StringUtils.isBlank(searchKey)) {
      return null;
    } else {
      if (XmlUtil.containZhChar(searchKey)) {
        isZh = true;
      }
      if (isZh && searchKey.length() < 2) {
        return null;
      }
      if (!isZh && searchKey.length() < 5) {
        return null;
      }
    }
    Integer typeNum = 0;
    switch (type) {
      case "conferenceTitle":
        typeNum = 3;
        break;
      case "journalTitle":
        typeNum = 4;
        break;
      case "patentTitle":
        typeNum = 5;
        break;
      default:
        break;
    }

    try {
      Map<String, Object> rsMap = this.solrIndexService.getInfoForPubInput(searchKey, typeNum);
      SolrDocumentList rsList = (SolrDocumentList) rsMap.get("items");
      if (rsList == null || rsList.size() == 0) {
        return null;
      }
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      Iterator<SolrDocument> it = rsList.iterator();
      int maxsize = 5;
      // 小于等于5条
      if ("patentTitle".equals(type)) {
        while (it.hasNext() && list.size() <= maxsize) {
          SolrDocument sd = it.next();
          this.getPatentInfo(list, sd, isZh);
        }
      } else {
        while (it.hasNext() && list.size() <= maxsize) {
          SolrDocument sd = it.next();
          this.getPubInfo(list, sd, isZh);
        }
      }
      return list;
    } catch (Exception e) {
      logger.error("手工录入成果，获取匹配标题信息出错", e);
    }
    return null;
  }

  private void getPatentInfo(List<Map<String, Object>> list, SolrDocument sd, Boolean isZh) {
    Map<String, Object> mp = new HashMap<String, Object>();
    Long pdwhPubId = 0L;
    if (sd.getFieldValue("patId") != null) {
      pdwhPubId = (Long) sd.getFieldValue("patId");
      // 验证基准库xml是否确实有数据
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findById(pdwhPubId);
      if (pubPdwhDetailDOM == null) {
        return;
      }
      // 加密基准库ID
      mp.put("des3pdwhPubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
    }
    // 都改为pubTitle
    String pubTitle = "";
    String authorName = "";
    if (StringUtils.isNotBlank(sd.getFieldValue("patTitle") + "")) {
      pubTitle = sd.getFieldValue("patTitle") + "";
      if (StringUtils.isNotEmpty(pubTitle)) {
        pubTitle = XmlUtil.trimAllHtml(pubTitle);
      }
    }
    authorName = cleanAuthorName(StringUtils.trimToEmpty((String) sd.getFieldValue("patAuthors")));
    mp.put("authors", authorName);
    mp.put("pubTitle", pubTitle);
    mp.put("name", authorName);
    mp.put("issn", pdwhPubId);
    mp.put("code", "patentTitle");
    mp.put("other", JacksonUtils.mapToJsonStr(mp));
    if (mp.size() > 0) {
      list.add(mp);
    }

  }

  private void getPubInfo(List<Map<String, Object>> list, SolrDocument sd, Boolean isZh) {
    Map<String, Object> mp = new HashMap<String, Object>();
    Long pdwhPubId = 0L;
    if (sd.getFieldValue("pubId") != null) {
      pdwhPubId = (Long) sd.getFieldValue("pubId");
      // 验证基准库xml是否确实有数据
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pdwhPubId);
      if (pubPdwhDetailDOM == null) {
        return;
      }
      mp.put("des3pdwhPubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
    }
    // 都改为pubTitle
    String pubTitle = "";

    if (StringUtils.isNotBlank(sd.getFieldValue("pubTitle") + "")) {
      pubTitle = sd.getFieldValue("pubTitle") + "";
      if (StringUtils.isNotEmpty(pubTitle)) {
        pubTitle = XmlUtil.trimAllHtml(pubTitle);
      }
    }
    String authorName = cleanAuthorName(StringUtils.trimToEmpty((String) sd.getFieldValue("authors")));
    mp.put("authors", authorName);
    mp.put("pubTitle", pubTitle);
    mp.put("name", authorName);
    mp.put("issn", pdwhPubId);
    // 3 会议 paperTitle
    // 4期刊 journalTitle
    // 5 专利 patentTitle
    Integer pubTypeId = (Integer) sd.getFieldValue("pubTypeId");
    switch (pubTypeId) {
      case 3:
        mp.put("code", "paperTitle");
        break;
      case 4:
        mp.put("code", "journalTitle");
        break;
      case 5:
        mp.put("code", "patentTitle");
        break;
      default:
        break;
    }
    mp.put("other", JacksonUtils.mapToJsonStr(mp));
    if (mp.size() > 0) {
      list.add(mp);
    }
  }

  private String cleanAuthorName(String authorName) {
    if (StringUtils.isEmpty(authorName)) {
      return "";
    }
    // 合并多个空格为一个
    authorName = authorName.replaceAll("\\s+", " ");
    // 去除逗号与分号前的空格
    authorName = authorName.replaceAll("\\s*,", ",");
    authorName = authorName.replaceAll("\\s*;", ";");
    // authorName = authorName.replaceAll(";[\\u4e00-\\u9fa5_a-zA-Z]+",
    // ";\\s[\\\\u4e00-\\\\u9fa5_a-zA-Z]+");
    // 去除姓名的数字
    authorName = authorName.replaceAll("[0-9]*", "");
    return authorName;
  }

  @Override
  public List<Map<String, Object>> getPdwhSearchSuggest(String searchKey, Integer type) {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    try {
      Map<String, Object> rsMap = this.solrIndexService.getPdwhSearchSuggestStr(searchKey, type);
      SolrDocumentList sList = (SolrDocumentList) rsMap.get("items");
      for (SolrDocument sd : sList) {
        if (sd.get("suggestStr") == null || StringUtils.isEmpty((String) sd.get("suggestStr"))) {
          continue;
        }
        // sd.getFieldValuesMap()重写了map方面，无法遍历entrySet，序列化报错。重新读取建立map
        Map<String, Object> sdMap = new HashMap<String, Object>();
        for (Entry<String, Object> et : sd.entrySet()) {
          if (et.getValue() == null) {
            continue;
          }
          if ("suggestPsnId".equalsIgnoreCase(et.getKey()) || "suggestInsId".equalsIgnoreCase(et.getKey())
              || "suggestType".equalsIgnoreCase(et.getKey())) {
            sdMap.put(et.getKey(), ServiceUtil.encodeToDes3(String.valueOf(et.getValue())));
            buildImg(et, sdMap);// 构建图片信息
          } else {
            sdMap.put(et.getKey(), et.getValue());
          }
        }
        if (sdMap.size() > 0) {
          resultList.add(sdMap);
        }
      }
    } catch (Exception e) {
      logger.error("获取检索提示出错", e);
    }
    return resultList;
  }

  /**
   * 构建图片信息（包括人员头像，机构部门图片，如果有的话使用对应的图片，否则使用默认图片）
   */
  private void buildImg(Entry<String, Object> entry, Map<String, Object> sdMap) {
    String key = entry.getKey();
    if (key.equalsIgnoreCase("suggestPsnId")) {
      String psnAvatars = "/resmod/smate-pc/img/acquiescence-psn-avator.png";// 默认头像
      if (entry.getValue() != null) {
        String avatars = personDao.getPsnImgByObjectId(Long.parseLong(entry.getValue().toString()));
        if (StringUtils.isNotEmpty(avatars)) {
          psnAvatars = avatars;
        }
      }
      sdMap.put("psnAvatars", psnAvatars);
    } else if (key.equalsIgnoreCase("suggestInsId")) {
      String insLogo = "/resmod/smate-pc/img/acquiescence-mec-avator.png";// 默认机构logo
      if (StringUtils.isNotEmpty(entry.getValue().toString())) {
        insLogo = "/insLogo/" + entry.getValue().toString() + ".jpg";
      }
      sdMap.put("insLogo", insLogo);
    }
  }

  /**
   * 判断是否为可用的头像（默认头像和没有头像都是不符合）， 默认头像规则：1、图像地址没有带A= 参数 （为好早之前自动生成的）
   * 2、图像的地址A参数的值中包含D字母的,因为在自己上传头像时，会将A参数的值中不包含D
   * 
   * @return
   */
  // https://test.scholarmate.com/avatars/8e/30/d5/1000000048186.png?A=D297
  @Deprecated
  private boolean isAvaliableAvatars(String avatars) {
    /*
     * // 系统生成的默认图像会包含A参数，同时以D开头,参照PsnDefaultAvatarServiceImpl.java中checkNeedUpdate方法 if
     * (StringUtils.isNotEmpty(avatars) && !avatars.contains("?A=D")) { return true; }
     */
    if (StringUtils.isNotEmpty(avatars)) {
      if (avatars.indexOf("?") != -1) {
        if (!avatars.contains("?A=D")) {// 系统生成的默认图像会包含A参数，同时以D开头,参照PsnDefaultAvatarServiceImpl.java中checkNeedUpdate方法
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 获取自动填充的省份
   */
  @Override
  public List<ConstRegion> getAcprovinces(String searchKey, int size) throws Exception {
    return constRegionDao.getAcprovinces(searchKey, size);
  }


  public static void main(String[] args) throws SolrServerException {
    SolrIndexSerivceImpl sis = new SolrIndexSerivceImpl();
    Map<String, Object> rsMap = sis.getPdwhSearchSuggestStr("香港城市大学", 1);
    System.out.println(rsMap);
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    SolrDocumentList sList = (SolrDocumentList) rsMap.get("items");
    for (SolrDocument sd : sList) {
      resultList.add(sd.getFieldValueMap());
      for (Entry<String, Object> et : sd.entrySet()) {
        System.out.println(et.getKey() + ":   " + et.getValue());
      }
    }
    System.out.println(resultList);
  }

}
