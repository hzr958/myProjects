package com.smate.web.v8pub.service.sns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.statistics.dao.RecommendInitDao;
import com.smate.core.base.statistics.model.RecommendInit;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.sns.CategoryScmDao;
import com.smate.web.v8pub.dao.sns.PubRecommendRecordDAO;
import com.smate.web.v8pub.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.web.v8pub.dao.sns.psn.PsnScienceAreaDao;
import com.smate.web.v8pub.dao.sns.psn.RecommendDisciplineKeyDao;
import com.smate.web.v8pub.dao.sns.psn.RecommendScienceAreaDao;
import com.smate.web.v8pub.dao.sns.pubtype.ConstPubTypeDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.po.sns.CategoryScm;
import com.smate.web.v8pub.po.sns.PubRecommendRecordPO;
import com.smate.web.v8pub.po.sns.psn.PsnScienceArea;
import com.smate.web.v8pub.po.sns.psn.RecommendDisciplineKey;
import com.smate.web.v8pub.po.sns.psn.RecommendScienceArea;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.vo.PubListResult;
import com.smate.web.v8pub.vo.PubListVO;

import net.sf.json.JSONArray;

@Service("pubRecommendService")
@Transactional(rollbackFor = Exception.class)
public class PubRecommendServiceImpl implements PubRecommendService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final int MAX_URL_LEN = 25;
  @Resource
  private PubQueryhandlerService pubQueryhandlerService;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private RecommendInitDao recommendInitDao;
  @Autowired
  private RecommendScienceAreaDao recommendScienceAreaDao;
  @Autowired
  private RecommendDisciplineKeyDao recommendDisciplineKeyDao;
  @Autowired
  private CategoryScmDao categoryScmDao;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private PubRecommendRecordDAO pubRecommendRecordDAO;

  @Override
  public void initScienAreaAndCode(Long psnId) throws ServiceException {
    if (psnId > 0 && !hasInit(psnId)) {
      List<PsnScienceArea> areaList = psnScienceAreaDao.findPsnScienceAreaList(psnId, 1);
      List<PsnDisciplineKey> keyList = psnDisciplineKeyDao.findPsnDisciplineKeyByPsnId(psnId, 1);
      List<RecommendScienceArea> recommendareaList = recommendScienceAreaDao.findRecommendScienceAreaList(psnId);
      List<RecommendDisciplineKey> recommendkeyList = recommendDisciplineKeyDao.findAllDisciplineKeyByPsnId(psnId);
      if (recommendareaList == null || recommendareaList.size() == 0) {
        for (int i = 0; i < areaList.size() && i < 3; i++) {// 最多添加3个科技领域
          PsnScienceArea psnarea = areaList.get(i);
          addRecommendArea(psnarea, psnId);
        }
      }
      if (recommendkeyList == null || recommendkeyList.size() == 0) {
        for (int i = 0; i < keyList.size() && i < 10; ++i) {// 最多添加10关键词
          PsnDisciplineKey psnkey = keyList.get(i);
          addRecommendKey(psnkey, psnId);
        }
      }
      saveRecommendInit(psnId);// 保存已经初始化的标志表
    }
  }

  @Override
  public void pubRecommendConditionsShow(PubQueryDTO pubQueryDTO) {
    Long psnId = pubQueryDTO.getSearchPsnId();
    // 查找论文推荐左侧查询条件
    List<RecommendScienceArea> areaList = recommendScienceAreaDao.findRecommendScienceAreaList(psnId);
    pubQueryDTO.setAreaList(areaList);
    List<RecommendDisciplineKey> keyList = recommendDisciplineKeyDao.findRecommendDisciplineKeyByPsnId(psnId);
    keyList = dealKey(keyList);
    List<ConstPubType> pubTypeList = constPubTypeDao.getTypesExcludePat(LocaleContextHolder.getLocale());
    Map<String, String> pubYearMap = this.builPubYearMap(LocaleContextHolder.getLocale());
    pubQueryDTO.setKeyList(keyList);
    pubQueryDTO.setPubTypeList(pubTypeList);
    pubQueryDTO.setPubYearMap(pubYearMap);
    // 把这些条件放到默认的查询里
    setDefultSearch(pubQueryDTO);
    // 判断是否为APP的请求
    if (com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(SecurityUtils.getCurrentUserId())) {
      // 论文推荐的默认关键词不能元素不能带双引号
      List<String> keyStrList = new ArrayList<String>();
      for (RecommendDisciplineKey key : pubQueryDTO.getKeyList()) {// 构建默认关键词，用,分隔
        keyStrList.add(key.getKeyWords());
      }
      pubQueryDTO.setDefultKeyJson(keyStrList.toString());
    }
  }

  private void setDefultSearch(PubQueryDTO pubQueryDTO) {
    String areaStr = "";
    List<String> keyStrList = new ArrayList<String>();
    for (RecommendScienceArea area : pubQueryDTO.getAreaList()) {// 构建默认科研领域id，用,分隔
      if ("".equals(areaStr)) {
        areaStr += area.getScienceAreaId();
      } else {
        areaStr += "," + area.getScienceAreaId();
      }
    }
    for (RecommendDisciplineKey key : pubQueryDTO.getKeyList()) {// 构建默认关键词，用,分隔
      keyStrList.add(key.getKeyWords());
    }
    JSONArray jsonarray = JSONArray.fromObject(keyStrList);
    pubQueryDTO.setDefultArea(areaStr);
    pubQueryDTO.setSearchArea(areaStr);
    pubQueryDTO.setDefultKeyJson(jsonarray.toString());
    pubQueryDTO.setSearchKey(jsonarray.toString());
  }

  /**
   * 换掉&quot;，变成"
   * 
   * @return
   */
  private List<RecommendDisciplineKey> dealKey(List<RecommendDisciplineKey> keyList) {
    return keyList.stream().map(disciplineKey -> {
      String key = disciplineKey.getKeyWords().replaceAll("&quot;", "\"");
      disciplineKey.setKeyWords(key);
      return disciplineKey;
    }).collect(Collectors.toList());
  }

  private Map<String, String> builPubYearMap(Locale locale) {
    Map<String, String> pubYear = new LinkedHashMap<String, String>();
    String sinceStr = "", yearStr = "";
    if ("en_US".equals(locale.toString())) {
      sinceStr = "Since ";
    } else {
      yearStr = " 年以来";
    }
    Integer year = Calendar.getInstance().get(Calendar.YEAR);
    pubYear.put("1", sinceStr + year + yearStr);// 当年至今
    pubYear.put("2", sinceStr + (year - 2) + yearStr);// 当年-2 至今
    pubYear.put("3", sinceStr + (year - 4) + yearStr);// 当年-5 至今
    pubYear.put("0", "en_US".equals(locale.toString()) ? "All" : "不限");
    return pubYear;
  }

  @Override
  public void pubRecommendListSearch(PubListVO pubListVO) {
    try {
      this.buildSearchParam(pubListVO.getPubQueryDTO());
      PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
      if (result.status.equals(V8pubConst.SUCCESS)) {
        pubListVO.setResultList(result.resultList);
        pubListVO.setTotalCount(result.totalCount);
      }
    } catch (Exception e) {
      logger.error("查询论文推荐列表 异常", e);
      throw new ServiceException(e);
    }
  }

  private void buildSearchParam(PubQueryDTO pubQueryDTO) {
    String searchAreaCode =
        Optional.ofNullable(pubQueryDTO.getSearchArea()).orElseGet(() -> pubQueryDTO.getDefultArea());
    pubQueryDTO.setSearchArea(searchAreaCode.replaceAll(",", " "));
    String serchKeyJson =
        Optional.ofNullable(pubQueryDTO.getSearchPsnKey()).filter(StringUtils::isNotEmpty).orElse("[]");
    String serchKey = getSearchKey(serchKeyJson);// 查询的关键词
    pubQueryDTO.setSearchPsnKey(serchKey);
    String searchPubType = Optional.ofNullable(pubQueryDTO.getSearchPubType()).map(type -> type.replaceAll(",", " "))
        .orElseGet(() -> null);// 查询的成果类型
    pubQueryDTO.setSearchPubType(searchPubType);
    Integer timeGap = Optional.ofNullable(pubQueryDTO.getSearchPubYear()).orElseGet(() -> 0);// 查询的出版时间
    pubQueryDTO.setSearchPubYear(timeGap);
  }

  @SuppressWarnings("unchecked")
  private String getSearchKey(String serchKeyJson) {
    List<String> keyList = new ArrayList<>();
    if (com.smate.core.base.utils.string.StringUtils.isNotBlank(StringUtils.strip(serchKeyJson, "[]"))) {
      for (String keyWord : StringUtils.strip(serchKeyJson, "[]").split(",")) {
        // SCM-22183
        if (keyWord.charAt(0) != '\"' && keyWord.charAt(keyWord.length() - 1) != '\"') {
          keyWord = "\"" + keyWord.trim() + "\"";
        }
        keyList.add(keyWord);
      }

      String serchKey = keyList.stream().map(str -> str.replaceAll("[\\p{Punct}]+", "")).map(key -> {
        if (key.length() > MAX_URL_LEN) {// 字符还是超长截取前MAX_URL_LEN个字符
          key = key.substring(0, MAX_URL_LEN);
        }
        return key;
      }).collect(Collectors.joining(","));
      return serchKey;
    }
    return "";
  }

  /**
   * 保存设置条件初始化标志
   * 
   * @param psnId
   */
  private void saveRecommendInit(Long psnId) {
    RecommendInit init = new RecommendInit();
    init.setPsnId(psnId);
    init.setPubRecommendInit(1);
    recommendInitDao.saveRecommendInit(init);
  }

  // 保存到成果推荐的个人关键词设置表
  private void addRecommendKey(PsnDisciplineKey psnkey, Long psnId) {
    String keyStr = psnkey.getKeyWords();
    if (keyStr.length() > MAX_URL_LEN) {// 字符还是超长截取前MAX_URL_LEN个字符
      keyStr = keyStr.substring(0, MAX_URL_LEN);
    }
    recommendDisciplineKeyDao.saveKeys(keyStr, psnkey.getPsnId());
  }

  // 保存到成果推荐的个人科技领域设置表
  private void addRecommendArea(PsnScienceArea psnarea, Long psnId) {
    RecommendScienceArea area = new RecommendScienceArea();
    area.setEnScienceArea(psnarea.getEnScienceArea());
    area.setPsnId(psnarea.getPsnId());
    area.setScienceArea(psnarea.getScienceArea());
    area.setScienceAreaId(psnarea.getScienceAreaId());
    area.setShowScienceArea(psnarea.getShowScienceArea());
    area.setUpdateDate(new Date());
    recommendScienceAreaDao.save(area);
  }

  /**
   * 推荐设置条件是否已经初始化了
   * 
   * @param psnId
   * @return
   */
  private boolean hasInit(Long psnId) {
    RecommendInit init = recommendInitDao.getRecommendInit(psnId);
    return Optional.ofNullable(init).map(RecommendInit::getPubRecommendInit).filter(i -> i == 1).isPresent();
  }

  @Override
  public Map<String, Object> pubEditScienArea(Long psnId, String addPsnAreaCodes) {
    Map<String, Object> resultMap = new HashMap<>();
    recommendScienceAreaDao.deletePsnAllArea(psnId);
    List<RecommendScienceArea> areaList = new ArrayList<RecommendScienceArea>();
    String[] strAreaCodes = addPsnAreaCodes.trim().replaceFirst("^,*", "").split(",");
    Long[] AreaCodes = com.smate.core.base.utils.number.NumberUtils.parseLongArry(strAreaCodes);
    for (int i = 0; i < AreaCodes.length; i++) {
      CategoryScm addCategoryScm = categoryScmDao.get(AreaCodes[i]);// 查找添加的科技领域
      RecommendScienceArea area = recommendScienceAreaDao.findRecommendScienceAreaByPsnIdAndId(psnId,
          addCategoryScm.getCategoryId().intValue());
      area = new RecommendScienceArea();
      area.setScienceAreaId(addCategoryScm.getCategoryId().intValue());
      area.setScienceArea(addCategoryScm.getCategoryZh());
      area.setEnScienceArea(addCategoryScm.getCategoryEn());
      areaList.add(area);
      area.setPsnId(psnId);
      area.setUpdateDate(new Date());
      area.setAreaOrder(i + 1);
      recommendScienceAreaDao.save(area);
    }
    resultMap.put("result", "success");
    resultMap.put("scienceAreaList", areaList);
    return resultMap;
  }

  @Override
  public Map<String, String> pubAddScienArea(Long psnId, String addPsnAreaCode) {
    Map<String, String> resultMap = new HashMap<>();
    // 个人的科技领域数
    Long psnAreaCount = recommendScienceAreaDao.findPsnHasScienceArea(psnId);
    if (psnAreaCount < 3) {// 最多3个
      CategoryScm addCategoryScm = categoryScmDao.get(Long.valueOf(addPsnAreaCode));// 查找添加的科技领域
      if (addCategoryScm != null) {
        RecommendScienceArea area = recommendScienceAreaDao.findRecommendScienceAreaByPsnIdAndId(psnId,
            addCategoryScm.getCategoryId().intValue());
        if (area != null) {// 已有存有的
          resultMap.put("result", "rep");// 已有该科技领域
        } else {// 添加
          area = new RecommendScienceArea();
          area.setScienceAreaId(addCategoryScm.getCategoryId().intValue());
          area.setScienceArea(addCategoryScm.getCategoryZh());
          area.setPsnId(psnId);
          area.setUpdateDate(new Date());
          area.setEnScienceArea(addCategoryScm.getCategoryEn());
          area.setAreaOrder(psnAreaCount.intValue() + 1);
          recommendScienceAreaDao.save(area);
          resultMap.put("result", "1");
        }
      } else {
        resultMap.put("result", "null");
      }
    } else {
      resultMap.put("result", "3");// 提示最多3个科技领域
    }
    return resultMap;
  }

  @Override
  public Map<String, String> pubDeleteScienArea(Long psnId, String deletePsnAreaCode) {
    Map<String, String> resultMap = new HashMap<>();
    Integer areaCode = Integer.valueOf(deletePsnAreaCode);
    if (areaCode != null) {
      recommendScienceAreaDao.deletePsnOneArea(psnId, areaCode);// 删除该科技领域
      resultMap.put("result", "success");
    }
    if (resultMap.get("result") == null) {
      resultMap.put("result", "0");// 没找到删除失败
    }
    return resultMap;
  }

  @Override
  public Map<String, String> pubAddKeyWord(Long psnId, String addPsnKeyWord) {
    Map<String, String> resultMap = new HashMap<>();
    // 个人的有效关键词数量
    Long keyCount = recommendDisciplineKeyDao.countRecommendDisciplineKey(psnId);
    if (keyCount < 10) {// 最多10个
      Integer result = recommendDisciplineKeyDao.saveKeys(HtmlUtils.htmlUnescape(addPsnKeyWord), psnId);
      if (result == 1) {
        resultMap.put("result", "1");// 保存成功
        resultMap.put("addKeyWord", addPsnKeyWord);// 关键词
      } else {
        resultMap.put("result", "rep");// 已有该关键词
      }
    } else {
      resultMap.put("result", "10");// 提示最多添加10个关键词
    }
    return resultMap;
  }

  @Override
  public Map<String, Object> pubSaveKeyWord(Long psnId, String addKeyWord) {
    List<String> KeyWordList = new ArrayList<>();
    Map<String, Object> resultMap = new HashMap<>();
    if (StringUtils.isNoneBlank(addKeyWord)) {
      List<String> initKeywords = new ArrayList<>();
      List<String> newAdddKeyWords = new ArrayList<>();
      List<RecommendDisciplineKey> recommendKeys = recommendDisciplineKeyDao.findRecommendDisciplineKeyByPsnId(psnId);
      for (RecommendDisciplineKey recommendKey : recommendKeys) {
        initKeywords.add(recommendKey.getKeyWords());
      }
      List<String> KeyWords = JacksonUtils.jsonToList(addKeyWord);
      if (KeyWords != null && KeyWords.size() > 0) {
        if (KeyWords.size() <= 10) {
          for (String keyWord : KeyWords) {
            keyWord = HtmlUtils.htmlEscape(keyWord.trim());
            if (StringUtils.isNotBlank(keyWord)) {
              if (!KeyWordList.contains(keyWord)) {
                // 同步人员信息
                KeyWordList.add(keyWord);
                if (!initKeywords.contains(keyWord)) {// 获取新添加的关键词
                  newAdddKeyWords.add(keyWord);
                }
              } else {
                resultMap.put("result", "rep");// 已有该关键词
                return resultMap;
              }
            } else {
              resultMap.put("result", "blank");// 关键词内容不能为空
              return resultMap;
            }
          }
          // Collections.reverse(newAdddKeyWords);// 按倒序显示
          resultMap.put("newAdddKeyWords", newAdddKeyWords);
        } else {
          resultMap.put("result", "exc");// 超过10个
          return resultMap;
        }
      }
    }

    recommendDisciplineKeyDao.deletePsnAllKey(psnId);// 保存前先清空所有的关键词
    if (StringUtils.isNoneBlank(addKeyWord)) {
      for (String keyWord : KeyWordList) {
        recommendDisciplineKeyDao.saveKeys(HtmlUtils.htmlUnescape(keyWord), psnId);
      }
    }
    // 个人的有效关键词数量
    Long keyCount = recommendDisciplineKeyDao.countRecommendDisciplineKey(psnId);
    resultMap.put("result", "success");// 保存成功
    Collections.reverse(KeyWordList);// 按倒序显示
    resultMap.put("keywordsList", String.join(";", KeyWordList));// 以分号保存,以分号返回
    resultMap.put("keyCount", keyCount.toString());
    return resultMap;

  }

  @Override
  public Map<String, String> pubDeleteKeyWord(Long psnId, String deletePsnKeyWord) {
    Map<String, String> resultMap = new HashMap<>();
    recommendDisciplineKeyDao.deleteKeys(HtmlUtils.htmlUnescape(deletePsnKeyWord), psnId);
    resultMap.put("result", "success");
    return resultMap;
  }

  @Override
  public List<RecommendDisciplineKey> getPsnAllKeyWord(Long psnId) throws ServiceException {
    List<RecommendDisciplineKey> keyList = recommendDisciplineKeyDao.findRecommendDisciplineKeyByPsnId(psnId);
    return keyList;
  }

  @Override
  public void savePsnArea(String areasStr, Long psnId) throws ServiceException {
    String[] areaArray = areasStr.split(",");
    List<RecommendScienceArea> recommendareaList = recommendScienceAreaDao.findRecommendScienceAreaList(psnId);
    recommendScienceAreaDao.deletePsnAllArea(psnId);// 删除所有
    int addNum = 0;
    for (int i = 0; i < areaArray.length && addNum < 3; i++) {// 最多保存3个科技领域
      Integer areaId = NumberUtils.toInt(areaArray[i], 0);
      CategoryScm cate = categoryScmDao.get(areaId.longValue());
      if (cate != null) {
        boolean isfind = false;
        for (RecommendScienceArea item : recommendareaList) {
          if (item.getScienceAreaId().equals(areaId)) {// 存在就保存
            addScienceArea(item);
            isfind = true;
            ++addNum;
          }
        }
        if (!isfind) {// 找不到就添加
          addScienceArea(cate, psnId);
          ++addNum;
        }
      }
    }

  }

  private void addScienceArea(CategoryScm cate, Long psnId) {
    RecommendScienceArea area = new RecommendScienceArea();
    area.setEnScienceArea(cate.getCategoryEn());
    area.setPsnId(psnId);
    area.setScienceArea(cate.getCategoryZh());
    area.setScienceAreaId(cate.getCategoryId().intValue());
    area.setUpdateDate(new Date());
    recommendScienceAreaDao.save(area);
  }

  private void addScienceArea(RecommendScienceArea item) {
    RecommendScienceArea area = new RecommendScienceArea();
    area.setEnScienceArea(item.getEnScienceArea());
    area.setPsnId(item.getPsnId());
    area.setScienceArea(item.getScienceArea());
    area.setScienceAreaId(item.getScienceAreaId());
    area.setUpdateDate(item.getUpdateDate());
    recommendScienceAreaDao.save(area);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void savePsnKeyWord(String jsonKeyWord, Long psnId) throws ServiceException {
    jsonKeyWord = HtmlUtils.htmlUnescape(jsonKeyWord);
    List<String> keywordList = JacksonUtils.jsonToList(jsonKeyWord);
    List<RecommendDisciplineKey> psnKeyList = recommendDisciplineKeyDao.findRecommendDisciplineKeyByPsnId(psnId);
    if (keywordList != null) {
      Set<String> keyset = new HashSet<String>();
      keyset.addAll(keywordList);// 排重
      recommendDisciplineKeyDao.deletePsnAllKey(psnId);// 删除所有
      int addNum = 0;
      for (int i = 0; i < keywordList.size() && addNum < 10; i++) {// 最多保存10个关键词
        String addword = keywordList.get(i);
        if (addword != null && addword.length() > MAX_URL_LEN) {// 截取
          addword = addword.substring(0, MAX_URL_LEN);
        }
        boolean isfind = false;
        for (RecommendDisciplineKey item : psnKeyList) {
          if (item.getKeyWords().equals(addword)) {// 存在就保存
            addKeyWord(item);
            isfind = true;
            ++addNum;
          }
        }
        if (!isfind) {// 找不到就添加
          recommendDisciplineKeyDao.saveKeys(addword, psnId);
          ++addNum;
        }
      }
    }
  }

  private void addKeyWord(RecommendDisciplineKey item) {
    RecommendDisciplineKey key = new RecommendDisciplineKey(item.getPsnId(), item.getKeyWords(), item.getUpdateDate());
    recommendDisciplineKeyDao.save(key);
  }

  @Override
  public void pubRecommendConditions(PubQueryDTO pubQueryDTO) {
    Long psnId = pubQueryDTO.getSearchPsnId();
    // 查找论文推荐左侧查询条件
    List<RecommendScienceArea> areaList = recommendScienceAreaDao.findRecommendScienceAreaList(psnId);
    pubQueryDTO.setAreaList(areaList);
    List<RecommendDisciplineKey> keyList = recommendDisciplineKeyDao.findRecommendDisciplineKeyByPsnId(psnId);
    keyList = dealKey(keyList);
    List<ConstPubType> pubTypeList = constPubTypeDao.getTypesExcludePat(LocaleContextHolder.getLocale());
    Map<String, String> pubYearMap = this.builPubYearMap(LocaleContextHolder.getLocale());
    pubQueryDTO.setKeyList(keyList);
    pubQueryDTO.setPubTypeList(pubTypeList);
    pubQueryDTO.setPubYearMap(pubYearMap);
    List<Long> excludePubIds = buildExcludePubIdList(pubQueryDTO.getDes3PubId(), psnId);
    pubQueryDTO.setPubIds(excludePubIds);
    // 把这些条件放到默认的查询里
    setDefultSearch(pubQueryDTO);

  }

  /**
   * 构建排除的成果id
   * 
   */
  private List<Long> buildExcludePubIdList(String pubIds, Long psnId) {
    // 先排除用户不感兴趣的推荐论文
    List<Long> excludePubIds = pubRecommendRecordDAO.getPubIdsByPsnId(psnId, 1);
    if (StringUtils.isNotBlank(pubIds)) {
      String[] splitPubIds = pubIds.split(",");
      for (String pubIdStr : splitPubIds) {
        long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubIdStr));
        excludePubIds.add(pubId);
      }
      if (excludePubIds.size() > 1000) {
        excludePubIds.subList(0, 1000);
      }
    }
    return excludePubIds;
  }

  @Override
  public void insertPubRecmRecord(Long psnId, Long pubId) throws ServiceException {
    try {
      PubRecommendRecordPO pubRecm = pubRecommendRecordDAO.findRecordByPubIdAndPsnId(psnId, pubId);
      if (pubRecm == null) {
        pubRecm = new PubRecommendRecordPO();
        pubRecm.setPsnId(psnId);
        pubRecm.setPubId(pubId);
        pubRecm.setStatus(1);// 状态：0正常，1不感兴趣
        pubRecm.setGmtCreate(new Date());
        pubRecm.setGmtModified(new Date());
        pubRecommendRecordDAO.save(pubRecm);
      }
    } catch (Exception e) {
      logger.error("插入V_PUB_RECOMMEND_RECORD表出错，psnId=" + psnId + ",pubId=" + pubId, e);
    }
  }
}
