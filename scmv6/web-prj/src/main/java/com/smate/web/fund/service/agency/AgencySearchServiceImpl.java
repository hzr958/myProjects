package com.smate.web.fund.service.agency;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.url.HttpRequestUtils;
import com.smate.sie.core.base.utils.dao.statistics.SieInsStatisticsDao;
import com.smate.sie.core.base.utils.model.statistics.SieInsStatistics;
import com.smate.web.fund.agency.dao.InsStatisticsDao;
import com.smate.web.fund.agency.dao.PsnAwardInsDao;
import com.smate.web.fund.agency.model.AgencySearchForm;
import com.smate.web.fund.agency.model.InsInfo;
import com.smate.web.fund.agency.model.InsSearchCharacter;
import com.smate.web.fund.agency.model.InsSearchConst;
import com.smate.web.fund.agency.model.InsSearchRegion;
import com.smate.web.fund.agency.model.InsStatistics;
import com.smate.web.fund.agency.model.PsnAwardIns;
import com.smate.web.institution.consts.InstitutionConsts;

/**
 * 机构检索功能服务
 * 
 * @author wsn
 *
 */
@Service("insSearchService")
@Transactional(rollbackFor = Exception.class)
public class AgencySearchServiceImpl implements AgencySearchService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InsStatisticsDao insStatisticsDao;
  @Autowired
  private SieInsStatisticsDao sieInsStatisticsDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PsnAwardInsDao psnAwardInsDao;
  @Autowired
  private InsPortalDao insPortalDao;
  @Value("${domainrol.https}")
  private String domainrol;

  @Override
  public AgencySearchForm searchInstitution(AgencySearchForm form) throws ServiceException {
    try {
      List<Institution> insList = institutionDao.searchInstitution(form.getInsRegion(), form.getInsCharacter(),
          form.getPage(), form.getSearchString());
      List<InsInfo> insInfoList = new ArrayList<InsInfo>();
      String locale = LocaleContextHolder.getLocale().toString();
      if (CollectionUtils.isNotEmpty(insList)) {
        for (Institution ins : insList) {
          InsInfo info = new InsInfo();
          info.setInsId(ins.getId());
          info.setInsZhName(ins.getZhName());
          info.setInsEnName(ins.getEnName());
          info.setShowName(this.getShowName(ins.getZhName(), ins.getEnName(), locale));
          info.setRegionName(this.getRegionStrById(ins.getRegionId(), locale));
          info.setRegionId(ins.getRegionId());
          SieInsStatistics insStc = sieInsStatisticsDao.getSieInsStatisticsById(ins.getId());
          if (insStc != null) {
            info.setPrjSum(insStc.getPrjSum() == null ? 0 : insStc.getPrjSum());
            info.setPubSum(insStc.getPubSum() == null ? 0 : insStc.getPubSum());
            info.setPsnSum(insStc.getPsnSum() == null ? 0 : insStc.getPsnSum());

          }
          InsStatistics insStc2 = insStatisticsDao.get(ins.getId());// 用于获取点赞数
          if (insStc2 != null) {
            // info.setShareSum(insStc.getShareSum() == null ? 0 : insStc.getShareSum());
            info.setLikeSum(insStc2.getLikeSum() == null ? 0 : insStc2.getLikeSum());
          }
          insInfoList.add(info);
        }
      }
      form.setInsInfoList(insInfoList);
    } catch (Exception e) {
      logger.error("检索机构出错， 检索条件为：" + form.toString(), e);
      throw new ServiceException(e);
    }
    return form;
  }

  /**
   * 获取语言环境对应的名称
   * 
   * @param zhName
   * @param enName
   * @param locale
   * @return
   */
  private String getShowName(String zhName, String enName, String locale) {
    if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)) {
      return StringUtils.isNotBlank(zhName) ? zhName : enName;
    } else {
      return StringUtils.isNotBlank(enName) ? enName : zhName;
    }
  }

  /**
   * 根据regionId构建地区信息
   * 
   * @param regionId
   * @return
   */
  private String getRegionStrById(Long regionId, String locale) {
    StringBuffer regionName = new StringBuffer();
    if (regionId != null) {
      int i = 1;
      while (regionId != null && i < 5) {
        ConstRegion cr = constRegionDao.findRegionNameById(regionId);
        if (cr != null) {
          if ("zh_CN".equals(locale)) {
            regionName.append((StringUtils.isNotBlank(cr.getZhName()) ? cr.getZhName() : cr.getEnName()) + ", ");
          } else {
            regionName.append((StringUtils.isNotBlank(cr.getEnName()) ? cr.getEnName() : cr.getZhName()) + ", ");
          }
          regionId = cr.getSuperRegionId();
        } else {
          break;
        }
        i++;
      }
      if (StringUtils.isNotBlank(regionName) && regionName.indexOf(",") > -1) {
        return regionName.substring(0, regionName.lastIndexOf(","));
      }
    }
    return regionName.toString();
  }

  @Override
  public String searchInsCallback(AgencySearchForm form) throws ServiceException {
    // 先处理下检索插件传递的检索条件
    // this.dealFilterParameter(form);
    // 分组统计各个检索条件的统计数
    List<Map<String, String>> insCharacterCount = this.countInsCharacter(form);
    List<Map<String, String>> insRegionCount = this.countInsRegions(form);
    List<Map<String, Object>> listCount = new ArrayList<Map<String, Object>>();
    Map<String, Object> recommendCount = new HashMap<String, Object>();
    recommendCount.put("insCharacter", insCharacterCount);
    recommendCount.put("insRegion", insRegionCount);
    listCount.add(recommendCount);
    String count = JacksonUtils.listToJsonStr(listCount);
    count = count.replace("[]", "{}");
    count = count.replace("[", "");
    count = count.replace("]", "");
    return count;
  }

  @Override
  public String searchInsCallbackNew(AgencySearchForm form) throws ServiceException {
    List<Map<String, Object>> listCount = new ArrayList<Map<String, Object>>();
    Map<String, Object> insSearchCount = new HashMap<String, Object>();
    // 分组统计各个检索条件的统计数
    if (form.getShowCharacterMenu()) {
      List<InsSearchCharacter> insCharacterCount = this.countInsCharacterNew(form);
      form.setCharacterList(insCharacterCount);
      insSearchCount.put("insCharacter", insCharacterCount);
    }
    if (form.getShowRegionMenu()) {
      List<InsSearchRegion> insRegionCount = this.countInsRegionsNew(form);
      form.setRegionList(insRegionCount);
      insSearchCount.put("insRegion", insRegionCount);
    }
    listCount.add(insSearchCount);
    String count = JacksonUtils.listToJsonStr(listCount);
    count = count.replace("[]", "{}");
    count = count.replace("[", "");
    count = count.replace("]", "");
    return count;
  }

  /**
   * 处理下检索插件传递的检索条件
   * 
   * @param form
   */
  private void dealFilterParameter(AgencySearchForm form) {
    if (StringUtils.isNotBlank(form.getAllFilterValues())) {
      String allFilterValues = form.getAllFilterValues();
      Map<String, String> paramMap = JacksonUtils.jsonToMap(allFilterValues);
      if (paramMap != null) {
        if (paramMap.get("insRegion") != null) {
          // form.setInsRegion(paramMap.get("insRegion"));
        }
        if (paramMap.get("insCharacter") != null) {
          // form.setInsCharacter(paramMap.get("insCharacter"));
        }
      }
    }
  }

  /**
   * 分组统计机构类别
   * 
   * @return
   */
  private List<Map<String, String>> countInsCharacter(AgencySearchForm form) {
    List<Map<String, Object>> insCharacters = new ArrayList<Map<String, Object>>();
    List<Map<String, String>> insCharactersCount = new ArrayList<Map<String, String>>();
    Map<String, String> map = new HashMap<String, String>();
    try {
      insCharacters =
          institutionDao.countInsByNature(form.getInsRegion(), form.getInsCharacter(), form.getSearchString());
      if (CollectionUtils.isNotEmpty(insCharacters)) {
        for (Map<String, Object> list : insCharacters) {
          map.put(list.get("character").toString(), list.get("insCount").toString());
        }
        insCharactersCount.add(map);
      }
    } catch (Exception e) {
      logger.error("构建机构的数据回显出错， 检索条件：" + form.toString(), e);
    }
    return insCharactersCount;
  }

  /**
   * 分组统计机构地区数量
   * 
   * @param form
   * @return
   */
  private List<Map<String, String>> countInsRegions(AgencySearchForm form) {
    List<Map<String, Object>> insRegions = new ArrayList<Map<String, Object>>();
    List<Map<String, String>> insRegionsCount = new ArrayList<Map<String, String>>();
    Map<String, String> map = new HashMap<String, String>();
    try {
      insRegions = institutionDao.countInsByRegion(form.getInsRegion(), form.getInsCharacter(), form.getSearchString());
      if (CollectionUtils.isNotEmpty(insRegions)) {
        for (Map<String, Object> list : insRegions) {
          map.put(list.get("regionId").toString(), list.get("insCount").toString());
        }
        insRegionsCount.add(map);
      }
    } catch (Exception e) {
      logger.error("构建机构的数据回显， 检索条件：" + form.toString(), e);
    }
    return insRegionsCount;
  }

  /**
   * 分组统计机构类别
   * 
   * @return
   */
  private List<InsSearchCharacter> countInsCharacterNew(AgencySearchForm form) {
    List<Map<String, Object>> insCharacters = new ArrayList<Map<String, Object>>();
    List<InsSearchCharacter> insCharactersCount = new ArrayList<InsSearchCharacter>();
    try {
      String locale = LocaleContextHolder.getLocale().toString();
      insCharacters =
          institutionDao.countInsByNature(form.getInsRegion(), form.getInsCharacter(), form.getSearchString());
      if (CollectionUtils.isNotEmpty(insCharacters)) {
        for (Map<String, Object> list : insCharacters) {
          InsSearchCharacter isc = new InsSearchCharacter();
          isc.setCharacterId((Long) list.get("character"));
          isc.setCount((Long) list.get("insCount"));
          this.findInsCharacterName(list.get("character").toString(), isc, locale);
          insCharactersCount.add(isc);
        }
      }
      // 类别统计数为0的也要显示
      if (form.getInsCharacter() == null) {
        List<String> characterList = new ArrayList<String>();
        characterList.add("1");
        characterList.add("2");
        characterList.add("3");
        characterList.add("4");
        characterList.add("5");
        characterList.add("6");
        characterList.add("7");
        characterList.add("99");
        if (CollectionUtils.isEmpty(insCharactersCount)) {
          for (String id : characterList) {
            InsSearchCharacter character = new InsSearchCharacter();
            character.setCharacterId(NumberUtils.toLong(id));
            character.setCount(0L);
            this.findInsCharacterName(id, character, locale);
            insCharactersCount.add(character);
          }
        } /*
           * else {
           * 
           * for (String id : characterList) { boolean needAdd = true; for (InsSearchCharacter chara :
           * insCharactersCount) { if (id.equals(chara.getCharacterId().toString())) { needAdd = false; } } if
           * (needAdd) { InsSearchCharacter character = new InsSearchCharacter();
           * character.setCharacterId(NumberUtils.toLong(id)); character.setCount(0L);
           * this.findInsCharacterName(id, character, locale); insCharactersCount.add(character); } } }
           */
      }
    } catch (Exception e) {
      logger.error("构建机构的数据回显出错， 检索条件：" + form.toString(), e);
    }
    return insCharactersCount;
  }

  /**
   * 获取机构类别名称
   * 
   * @param characterId
   * @param isc
   * @param locale
   */
  private void findInsCharacterName(String characterId, InsSearchCharacter isc, String locale) {
    switch (characterId) {
      // 高等院校
      case "1":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_1_ZH, InsSearchConst.INS_CHARACTER_NEW_1_EN, locale));
        break;
      // 企业
      case "2":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_2_ZH, InsSearchConst.INS_CHARACTER_NEW_2_EN, locale));
        break;
      // 医疗机构
      case "3":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_3_ZH, InsSearchConst.INS_CHARACTER_NEW_3_EN, locale));
        break;
      // 研究机构
      case "4":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_4_ZH, InsSearchConst.INS_CHARACTER_NEW_4_EN, locale));
        break;
      // 政府机构
      case "5":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_5_ZH, InsSearchConst.INS_CHARACTER_NEW_5_EN, locale));
        break;
      // 出版机构
      case "6":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_6_ZH, InsSearchConst.INS_CHARACTER_NEW_6_EN, locale));
        break;
      // 虚拟机构
      case "7":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_7_ZH, InsSearchConst.INS_CHARACTER_NEW_7_EN, locale));
        break;
      // 其他
      case "99":
        isc.setShowName(
            this.getShowName(InsSearchConst.INS_CHARACTER_NEW_99_ZH, InsSearchConst.INS_CHARACTER_NEW_99_EN, locale));
        break;
    }
  }

  /**
   * 分组统计机构地区数量
   * 
   * @param form
   * @return
   */
  private List<InsSearchRegion> countInsRegionsNew(AgencySearchForm form) {
    List<Map<String, Object>> insRegions = new ArrayList<Map<String, Object>>();
    List<InsSearchRegion> insRegionsCount = new ArrayList<InsSearchRegion>();
    String locale = LocaleContextHolder.getLocale().toString();
    try {
      insRegions = institutionDao.countInsByRegion(form.getInsRegion(), form.getInsCharacter(), form.getSearchString());
      if (CollectionUtils.isNotEmpty(insRegions)) {
        for (Map<String, Object> list : insRegions) {
          InsSearchRegion region = new InsSearchRegion();
          region.setRegionId((Long) list.get("regionId"));
          region.setCount((Long) list.get("insCount"));
          ConstRegion constRegion = constRegionDao.findRegionNameById(region.getRegionId());
          if (constRegion != null) {
            region.setShowName(this.getShowName(constRegion.getZhName(), constRegion.getEnName(), locale));
          } else {
            region.setShowName(this.getShowName("其他", "Others", locale));
          }
          insRegionsCount.add(region);
        }
      }
    } catch (Exception e) {
      logger.error("构建机构的数据回显， 检索条件：" + form.toString(), e);
    }
    return insRegionsCount;
  }

  @Override
  public Integer awardInsOpt(AgencySearchForm form) throws ServiceException {
    // 重复的操作直接忽略
    int hasAwardRecord = psnAwardInsDao.hasAwardRecord(form.getInsId(), form.getPsnId(), form.getStatus());
    if (hasAwardRecord > 0) {
      logger.error("你已赞/取消赞过该机构,psnId= " + form.getPsnId() + " ,insId= " + form.getInsId());
      return -1;
    }
    PsnAwardIns psnAwardIns = psnAwardInsDao.getPsnAwardIns(form);
    InsStatistics insStatistics = insStatisticsDao.get(form.getInsId());
    if (form.getStatus() == 0) {// 取消赞
      if (psnAwardIns == null) {
        logger.error("取消赞时未查找到赞或取消赞机构的记录  insId= " + form.getInsId() + " ,awardPsnId= " + form.getPsnId());
        return -1;
      } else {
        psnAwardIns.setAwardDate(new Date());
        psnAwardIns.setStatus(form.getStatus());
        psnAwardInsDao.save(psnAwardIns);
      }
      if (insStatistics == null) {
        insStatistics = new InsStatistics();
        insStatistics.setInsId(form.getInsId());
        insStatistics.setLikeSum(0);
      } else {
        Integer awardCount = insStatistics.getLikeSum() == null ? 0 : insStatistics.getLikeSum();
        awardCount = awardCount - 1 < 0 ? 0 : awardCount - 1;
        insStatistics.setLikeSum(awardCount);
      }
      insStatisticsDao.save(insStatistics);
    } else {// 赞
      if (psnAwardIns == null) {
        psnAwardIns = new PsnAwardIns();
        psnAwardIns.setInsId(form.getInsId());
        psnAwardIns.setAwardPsnId(form.getPsnId());
      }
      psnAwardIns.setAwardDate(new Date());
      psnAwardIns.setStatus(form.getStatus());
      psnAwardInsDao.save(psnAwardIns);
      if (insStatistics == null) {
        insStatistics = new InsStatistics();
        insStatistics.setInsId(form.getInsId());
        insStatistics.setLikeSum(1);
      } else {
        Integer awardCount = insStatistics.getLikeSum() == null ? 0 : insStatistics.getLikeSum();
        insStatistics.setLikeSum(awardCount + 1);
      }
      insStatisticsDao.save(insStatistics);
    }
    return insStatistics.getLikeSum();

  }

  @Override
  public String initInsInfo(AgencySearchForm form) throws ServiceException {
    String result = "";
    try {
      if (StringUtils.isNotBlank(form.getInitInsIds()) && SecurityUtils.getCurrentUserId() > 0L) {
        // 分割出机构ID
        String[] desInsIds = form.getInitInsIds().split(",");
        List<Long> insIds = new ArrayList<Long>();
        if (desInsIds != null && desInsIds.length > 0) {
          for (String desInsId : desInsIds) {
            insIds.add(NumberUtils.toLong(Des3Utils.decodeFromDes3(desInsId)));
          }
        }
        // 获取赞过的机构
        if (CollectionUtils.isNotEmpty(insIds)) {
          List<Long> ist = psnAwardInsDao.findInsAwardStatus(insIds, SecurityUtils.getCurrentUserId(), 1);
          if (CollectionUtils.isNotEmpty(ist)) {
            for (Long id : ist) {
              result += id + ",";
            }
            result = result.substring(0, result.length() - 1);
          }
        }
      }
    } catch (Exception e) {
      logger.error("初始化机构信息出错， 机构ID为： " + form.getInitInsIds(), e);
      throw new ServiceException(e);
    }
    return result;
  }

  @Override
  public String findInsUrls(AgencySearchForm form) throws ServiceException {
    // Map<String, String> result = new HashMap<String, String>();
    String result = "";
    try {
      if (StringUtils.isNotBlank(form.getInitInsIds())) {
        String[] insIds = form.getInitInsIds().split(",");
        if (insIds != null && insIds.length > 0) {
          List<Long> ids = new ArrayList<Long>();
          for (String id : insIds) {
            ids.add(NumberUtils.toLong(Des3Utils.decodeFromDes3(id)));
          }
          if (CollectionUtils.isNotEmpty(ids)) {
            List<InsPortal> insPortalList = insPortalDao.findInsPortalUrlByInsIds(ids);
            if (CollectionUtils.isNotEmpty(insPortalList)) {
              result = JacksonUtils.listToJsonStr(insPortalList);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("获取机构URL出错， 机构ID : " + form.getInitInsIds(), e);
      throw new ServiceException();
    }
    return result;
  }

  /**
   * 检索机构
   */
  @Override
  public AgencySearchForm searchNewInstitution(AgencySearchForm form) throws ServiceException {
    try {
      List<InsInfo> insInfoList = null;
      List<InsSearchRegion> insRegions = null;
      List<InsSearchCharacter> insCharacters = null;
      Long totalCount = 0L;
      Map<String, Object> result = (Map<String, Object>) getRemoteInsInfo(form);
      if (result != null && result.get("status") != null && result.get("status").equals("success")) {
        Map<String, Object> resultList = (Map<String, Object>) result.get("result");
        totalCount = Long.parseLong(resultList.get("ins_total").toString());
        form.getPage().setTotalCount(totalCount);
        insInfoList = buildInsInfoList(resultList);// 机构列表信息
        insRegions = buildInsRegions(resultList);// 地区列表
        insCharacters = buildInsCharacter(resultList);// 类型列表
      }
      form.setInsInfoList(insInfoList);
      form.setRegionList(insRegions);
      form.setCharacterList(insCharacters);
    } catch (Exception e) {
      logger.error("检索机构出错， 检索条件为：" + form.toString(), e);
      throw new ServiceException(e);
    }
    return form;
  }

  public List<InsInfo> buildInsInfoList(Map<String, Object> result) {
    List<InsInfo> insInfoList = new ArrayList<InsInfo>();
    List<Map<String, Object>> insList = (List<Map<String, Object>>) result.get("institutions");
    if (insList != null && insList.size() > 0) {
      for (Map<String, Object> insMap : insList) {
        InsInfo info = new InsInfo();
        info.setInsId(Long.parseLong(String.valueOf(insMap.get("ins_id"))));
        info.setShowName(String.valueOf(insMap.get("ins_name")));
        info.setDescription(String.valueOf(insMap.get("description")));
        info.setLikeSum(Integer.parseInt(String.valueOf(insMap.get("st_award"))));
        info.setFollowSum(Integer.parseInt(String.valueOf(insMap.get("st_follow"))));
        String shareSum = String.valueOf(insMap.get("st_share"));
        info.setShareSum(Integer.parseInt(shareSum));
        String domain = String.valueOf(insMap.get("domain"));
        info.setDomain(domain);
        String insDomain = "http://" + domain + "/insweb/index";
        info.setDomainUrl(insDomain);
        info.setLogoUrl(String.valueOf(insMap.get("logo_url")));
        info.setIsAward(Integer.parseInt(String.valueOf(insMap.get("is_award"))));
        info.setIsFollow(Integer.parseInt(String.valueOf(insMap.get("is_follow"))));
        String characterId = String.valueOf(insMap.get("nature_id"));
        info.setCharacterId(Long.parseLong(characterId));
        info.setCharacterName(String.valueOf(insMap.get("nature_name")));
        if ("1".equals(characterId)) {
          info.setPrjSum(Integer.parseInt(String.valueOf(insMap.get("pat_num"))));
          info.setPsnSum(Integer.parseInt(String.valueOf(insMap.get("psn_num"))));
          info.setPubSum(Integer.parseInt(String.valueOf(insMap.get("pub_num"))));
        }
        insInfoList.add(info);
      }
    }
    return insInfoList;
  }

  public List<InsSearchRegion> buildInsRegions(Map<String, Object> result) {
    List<InsSearchRegion> insRegions = new ArrayList<InsSearchRegion>();
    List<Map<String, Object>> regionsList = (List<Map<String, Object>>) result.get("regions");
    if (regionsList != null && regionsList.size() > 0) {
      for (Map<String, Object> regMap : regionsList) {
        InsSearchRegion region = new InsSearchRegion();
        Long count = Long.parseLong(String.valueOf(regMap.get("region_count")));
        if (count != 0) {
          region.setRegionId(Long.parseLong(String.valueOf(regMap.get("region_id"))));
          region.setCount(count);
          region.setShowName(String.valueOf(regMap.get("region_name")));
          insRegions.add(region);
        }
      }
    }
    return insRegions;
  }

  private List<InsSearchCharacter> buildInsCharacter(Map<String, Object> result) {
    List<InsSearchCharacter> insCharacters = new ArrayList<InsSearchCharacter>();
    List<Map<String, Object>> naturesList = (List<Map<String, Object>>) result.get("natures");
    String locale = LocaleContextHolder.getLocale().toString();
    if (naturesList != null && naturesList.size() > 0) {
      for (Map<String, Object> naturesMap : naturesList) {
        InsSearchCharacter isc = new InsSearchCharacter();
        Long count = Long.parseLong(String.valueOf(naturesMap.get("nature_count")));
        if (count != 0) {
          isc.setCharacterId(Long.parseLong(String.valueOf(naturesMap.get("nature_id"))));
          isc.setCount(count);
          this.findInsCharacterName(naturesMap.get("nature_id").toString(), isc, locale);
          insCharacters.add(isc);
        }
      }
    }
    // 类别统计数为0的也要显示
    if (CollectionUtils.isEmpty(insCharacters)) {
      List<String> characterList = new ArrayList<String>();
      characterList.add("1");
      characterList.add("2");
      characterList.add("3");
      characterList.add("4");
      characterList.add("5");
      characterList.add("6");
      characterList.add("7");
      characterList.add("99");
      for (String id : characterList) {
        InsSearchCharacter character = new InsSearchCharacter();
        character.setCharacterId(NumberUtils.toLong(id));
        character.setCount(0L);
        this.findInsCharacterName(id, character, locale);
        insCharacters.add(character);
      }
    }
    return insCharacters;
  }

  public String optIns(AgencySearchForm form) throws ServiceException {
    String status = "success";
    try {
      boolean flag = doPostRequest(form.getPsnId(), form.getType(), form.getInsId());
      if (!flag) {
        status = "error";
      }
    } catch (Exception e) {
      logger.error("调用机构社交化操作接口出错， 机构ID : " + form.getInitInsIds() + "操作type:" + form.getType(), e);
      throw new ServiceException();
    }
    return status;
  }

  /**
   * type 1赞2分享3关注4取消赞5取消关注 ;ins_id 单位id; data_from 1机构主页-机构版 2个人版
   * psn_id=1100000003769&type=1&ins_id=857&data_from=2
   */
  public boolean doPostRequest(Long psnId, Integer type, Long insId) {
    String SERVER_URL = domainrol + InstitutionConsts.INS_SOCIAL_OPT;
    // String SERVER_URL = "http://sieuat.scholarmate.com/common/insindexsocial";
    StringBuilder sb = new StringBuilder();
    sb.append("psn_id=" + psnId);
    sb.append("&type=" + type);
    sb.append("&ins_id=" + insId);
    sb.append("&data_from=2");
    String result = HttpRequestUtils.sendPost(SERVER_URL, sb.toString());
    if (StringUtils.isNotBlank(result) && JacksonUtils.isJsonString(result)) {
      Map<String, String> resultMap = JacksonUtils.jsonToMap(result);
      if (resultMap != null && resultMap.get("status") != null && resultMap.get("status").equalsIgnoreCase("success")) {
        return true;
      }
    }
    return false;
  }

  /**
   * "psn_id":"12345678", "nature_id":"1", "region_id":"360000", "ins_name":"江西师范大学",
   * "page_size":"10", "page_no":"1"
   * 
   * @param form
   * @return
   */
  public Object getRemoteInsInfo(AgencySearchForm form) {
    String SERVER_URL = domainrol + InstitutionConsts.INS_SEARCH_LIST;
    // String SERVER_URL = "http://sieuat.scholarmate.com/common/insindexlist ";
    StringBuilder sb = new StringBuilder();
    sb.append("ins_name=" + form.getSearchString());
    if (form.getPsnId() != null) {
      sb.append("&psn_id=" + form.getPsnId());
    }
    if (form.getInsCharacter() != null) {
      sb.append("&nature_id=" + form.getInsCharacter());
    }
    if (form.getInsRegion() != null) {
      sb.append("&region_id=" + form.getInsRegion());
    }
    sb.append("&page_size=" + form.getPage().getPageSize());
    sb.append("&page_no=" + form.getPage().getParamPageNo());
    String result = HttpRequestUtils.sendPost(SERVER_URL, sb.toString());
    Map<String, String> resultMap = null;
    if (StringUtils.isNotBlank(result) && JacksonUtils.isJsonString(result)) {
      resultMap = JacksonUtils.jsonToMap(result);
    }
    // String s =
    // "{\"status\":\"success\",\"natures\":[{\"nature_id\":\"1\",\"nature_name\":\"大学\",\"nature_count\":\"10\"},{\"nature_id\":\"2\",\"nature_name\":\"企业\",\"nature_count\":\"10\"}],\"regions\":[{\"region_id\":\"360000\",\"region_name\":\"江西省\",\"region_count\":\"10\"},{\"region_id\":\"340000\",\"region_name\":\"湖南省\",\"region_count\":\"10\"}],\"institutions\":[{\"ins_id\":\"8571\",\"ins_name\":\"江西师范大学1\",\"domain\":\"nankaiuat.scholarmate.com\",\"nature_id\":\"1\",\"nature_name\":\"大学\",\"description\":\"254论文，300专利，200人员\",\"is_award\":\"1\",\"is_follow\":\"0\",\"st_award\":\"100\",\"st_follow\":\"150\",\"logo_url\":\"/insLogo/1789.jpg\"},{\"ins_id\":\"8572\",\"ins_name\":\"江西师范大学2\",\"domain\":\"nankaiuat.scholarmate.com\",\"nature_id\":\"2\",\"nature_name\":\"企业\",\"description\":\"企业\",\"is_award\":\"0\",\"is_follow\":\"1\",\"st_award\":\"100\",\"st_follow\":\"150\",\"logo_url\":\"xxxxxxxx\"}],\"ins_total\":\"2\"}";
    // Map<Object, Object> resultMap = JacksonUtils.jsonToMap(s);
    return resultMap;
  }

  public static void main(String[] args) {
    String s =
        "{\"result\":{\"natures\":[{\"nature_name\":\"高等院校\",\"nature_id\":1,\"nature_count\":475},{\"region_count\":1,\"region_id\":500000,\"region_name\":\"重庆市\"}],\"institutions\":[{\"ins_id\":108,\"ins_name\":\"大连海洋大学\",\"domain\":\"108.scholarmate.com\",\"nature_id\":1,\"nature_name\":\"高等院校\",\"description\":\"16论文,3专利,0人员\",\"st_award\":0,\"st_follow\":0,\"logo_url\":\"/sielogo/5e/a5/a3/108.png?A=D2kx\"},{\"ins_id\":162,\"ins_name\":\"福建医科大学\",\"domain\":\"162.scholarmate.com\",\"nature_id\":1,\"nature_name\":\"高等院校\",\"description\":\"1论文,0专利,0人员\",\"st_award\":0,\"st_follow\":0,\"logo_url\":\"/sielogo/f6/c5/05/162.png?A=DArt\"}],\"ins_total\":5707,\"page_total\":571},\"msg\":\"sie-000 数据处理成功\",\"status\":\"success\"}";
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(s);
    System.out.println(resultMap.get("result"));
    Map<String, Object> result = (Map<String, Object>) resultMap.get("result");
    System.out.println(result.get("ins_total"));
  }
}
