package com.smate.web.mobile.v8pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.consts.RestTemplateUtils;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.recommend.model.ConstPubType;
import com.smate.core.base.pub.recommend.model.RecommendDisciplineKey;
import com.smate.core.base.pub.recommend.model.RecommendScienceArea;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.dao.CategoryScmDao;
import com.smate.web.mobile.v8pub.sns.po.CategoryScm;
import com.smate.web.mobile.v8pub.vo.pdwh.PubRecommendVO;

@Service("pubRestemplateService")
@Transactional(rollbackFor = Exception.class)
public class PubRecommendRestServiceImpl implements PubRecommendRestService {
  public final static String GET_PUB_CONDITIONAL = "/data/pubrecommend/getconditional";// 查询推荐设置条件
  public final static String PUB_RECOMMEND_LIST = "/data/pubrecommend/publist";// 查询推荐成果
  public final static String ADD_SCIENAREA = "/data/pubrecommend/addscienarea";// 添加科技领域
  public final static String MOBILE_ADD_SCIENAREAS = "/data/pubrecommend/savepsnareas";// 移动端添加科技领域
  public final static String DELETE_SCIENAREA = "/data/pubrecommend/deletescienarea";// 删除科技领域
  public final static String ADD_KEYWORD = "/data/pubrecommend/addkeyword";// 添加关键词
  public final static String MOBILE_ADD_KEYWORDS = "/data/pubrecommend/savepsnkeywords";// 移动端添加关键词
  public final static String DELETE_KEYWORD = "/data/pubrecommend/deletekeyword";// 删除关键词
  public final static String ALL_KEYWORD_LIST = "/data/pubrecommend/allpsnkeylist";// 查询推荐成果

  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private CategoryScmDao categoryScmDao;

  @Override
  public PubRecommendVO pubRecommendConditional(Long psnId, PubRecommendVO pubVO) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    if (psnId != null && psnId > 0) {
      pubQueryDTO.setDes3SearchPsnId(Des3Utils.encodeToDes3(psnId.toString()));
      String jsonStr = RestTemplateUtils.post(restTemplate, domainscm + GET_PUB_CONDITIONAL,
          JacksonUtils.jsonObjectSerializer(pubQueryDTO));
      buildConditional(pubVO, jsonStr);// 构建获取参数
    }
    return pubVO;
  }

  /**
   * 获取接口返回参数
   * 
   * @param pubVO
   * @param jsonStr
   */
  @SuppressWarnings("unchecked")
  private void buildConditional(PubRecommendVO pubVO, String jsonStr) {
    Map<String, Object> result = JacksonUtils.jsonToMap(jsonStr);
    List<RecommendScienceArea> areaList =
        result.get("areaList") != null ? (List<RecommendScienceArea>) result.get("areaList") : null;
    List<RecommendDisciplineKey> keyList =
        result.get("keyList") != null ? (List<RecommendDisciplineKey>) result.get("keyList") : null;
    List<ConstPubType> pubTypeList =
        result.get("pubTypeList") != null ? (List<ConstPubType>) result.get("pubTypeList") : null;
    Map<String, String> pubYearMap =
        result.get("pubYearMap") != null ? (Map<String, String>) result.get("pubYearMap") : null;
    String defultArea = result.get("defultArea") != null ? Objects.toString(result.get("defultArea")) : null;
    String defultKeyJson = result.get("defultKeyJson") != null ? Objects.toString(result.get("defultKeyJson")) : null;
    pubVO.setAreaList(areaList);
    pubVO.setKeyList(keyList);
    pubVO.setPubTypeList(pubTypeList);
    pubVO.setPubYearMap(pubYearMap);
    pubVO.setDefultArea(defultArea);
    pubVO.setDefultKeyJson(defultKeyJson);
    pubVO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubVO.getSearchPsnKey()));// 转义的解码
  }

  @Override
  public void searchPubList(PubRecommendVO pubRecommendVO) {
    if (pubRecommendVO.getDes3PsnId() != null) {
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      setSelectParam(pubRecommendVO, pubQueryDTO);// 构建查询参数
      String jsonStr = RestTemplateUtils.post(restTemplate, domainscm + PUB_RECOMMEND_LIST,
          JacksonUtils.jsonObjectSerializer(pubQueryDTO));
      builSelectResult(jsonStr, pubRecommendVO);// 获取结果
    }
  }

  /**
   * 构建查询参数
   * 
   * @param pubListVO
   * @param pubDTO
   */
  private void setSelectParam(PubRecommendVO pubListVO, PubQueryDTO pubDTO) {
    pubDTO.setDes3SearchPsnId(pubListVO.getDes3PsnId());
    pubDTO.setDefultArea(pubListVO.getDefultArea());
    pubDTO.setDefultKeyJson(HtmlUtils.htmlUnescape(pubListVO.getDefultKeyJson()));
    pubDTO.setSearchArea(pubListVO.getSearchArea());
    pubDTO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubListVO.getSearchPsnKey()));
    pubDTO.setSearchPubYear(NumberUtils.parseInt(pubListVO.getSearchPubYear()));
    pubDTO.setSearchPubType(pubListVO.getSearchPubType());
    pubDTO.setPageNo(pubListVO.getPage().getPageNo());
    pubDTO.setPageSize(pubListVO.getPage().getPageSize());
  }

  /**
   * 获取返回对象的值
   * 
   * @param jsonStr
   * @param pubDTO
   */
  @SuppressWarnings("unchecked")
  private void builSelectResult(String jsonStr, PubRecommendVO pubListVO) {
    if (StringUtils.isBlank(jsonStr)) {
      return;
    }
    Map<String, Object> resultMap = JacksonUtils.jsonToMap(jsonStr);
    List<PubInfo> resultList = (List<PubInfo>) resultMap.get("resultList");
    Integer totalCount = (Integer) resultMap.get("totalCount");

    if (totalCount != null) {
      pubListVO.getPage().setResult(resultList);
      pubListVO.getPage().setTotalCount(totalCount);
    } else {
      pubListVO.getPage().setTotalCount(0);
    }

  }

  @Override
  public String addScienArea(String des3PsnId, String addAreaCode) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String result = "";
    if (psnId > 0) {
      Map<String, String> param = new HashMap<String, String>();
      param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.put("addAreaCode", addAreaCode);
      result =
          RestTemplateUtils.post(restTemplate, domainscm + ADD_SCIENAREA, JacksonUtils.jsonObjectSerializer(param));
    }
    return result;
  }

  @Override
  public String deleteScienArea(String des3PsnId, String deleteAreaCode) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String result = "";
    if (psnId > 0) {
      Map<String, String> param = new HashMap<String, String>();
      param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.put("deleteAreaCode", deleteAreaCode);
      result =
          RestTemplateUtils.post(restTemplate, domainscm + DELETE_SCIENAREA, JacksonUtils.jsonObjectSerializer(param));
    }
    return result;
  }

  @Override
  public String addKeyWord(String des3PsnId, String addKeyWord) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String result = "";
    if (psnId > 0) {
      Map<String, String> param = new HashMap<String, String>();
      param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.put("addKeyWord", addKeyWord);
      result = RestTemplateUtils.post(restTemplate, domainscm + ADD_KEYWORD, JacksonUtils.jsonObjectSerializer(param));
    }
    return result;
  }

  @Override
  public String deleteKeyWord(String des3PsnId, String deleteKeyWord) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String result = "";
    if (psnId > 0) {
      Map<String, String> param = new HashMap<String, String>();
      param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.put("deleteKeyWord", deleteKeyWord);
      result =
          RestTemplateUtils.post(restTemplate, domainscm + DELETE_KEYWORD, JacksonUtils.jsonObjectSerializer(param));
    }
    return result;
  }

  // 获取全部的我科技领域
  @Override
  public void getAllScienceArea(PubRecommendVO pubRecommendVO) {
    List<Map<String, Object>> allScienceAreaList = new ArrayList<Map<String, Object>>();
    List<CategoryScm> firstAreaList = categoryScmDao.findFirstScienceArea();
    Map<String, Object> areaItemMap = null;
    for (CategoryScm item : firstAreaList) {
      areaItemMap = new HashMap<String, Object>();
      areaItemMap.put("first", item);
      List<CategoryScm> secondAreaList = categoryScmDao.findSecondScienceArea(item.getCategoryId());
      areaItemMap.put("second", secondAreaList);
      allScienceAreaList.add(areaItemMap);
    }
    pubRecommendVO.setAllScienceAreaList(allScienceAreaList);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getPsnAllKeyWord(PubRecommendVO pubRecommendVO) {
    if (pubRecommendVO.getPsnId() != null) {
      String des3PsnId = Des3Utils.encodeToDes3(pubRecommendVO.getPsnId().toString());
      String jsonStr = RestTemplateUtils.post(restTemplate, domainscm + ALL_KEYWORD_LIST, des3PsnId);
      if (StringUtils.isNoneBlank(jsonStr)) {
        List<RecommendDisciplineKey> keyList = JacksonUtils.jsonToList(jsonStr);
        pubRecommendVO.setKeyList(keyList);
      }
    }
  }

  @Override
  public String addMobileScienAreas(String des3PsnId, String addPsnAreaCodes) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String result = "";
    if (psnId > 0) {
      Map<String, Object> param = new HashMap<String, Object>();
      param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.put("defultArea", addPsnAreaCodes);
      result =
          RestTemplateUtils.post(restTemplate, domainscm + MOBILE_ADD_SCIENAREAS, JacksonUtils.mapToJsonStr(param));
    }
    return result;
  }

  @Override
  public String addMobileKeyWords(String des3PsnId, String addPsnKeyWords) {
    Long psnId = SecurityUtils.getCurrentUserId();
    String result = "";
    if (psnId > 0) {
      Map<String, String> param = new HashMap<String, String>();
      param.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.put("defultKeyJson", addPsnKeyWords);
      result = RestTemplateUtils.post(restTemplate, domainscm + MOBILE_ADD_KEYWORDS, JacksonUtils.mapToJsonStr(param));
    }
    return result;
  }
}
