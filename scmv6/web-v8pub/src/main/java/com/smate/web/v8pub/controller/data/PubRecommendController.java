package com.smate.web.v8pub.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.po.sns.psn.RecommendDisciplineKey;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubRecommendService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.pdwh.PubRecommendVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/*
 * 成果推荐
 */
@RestController
public class PubRecommendController {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRecommendService pubRecommendService;

  /**
   * 论文推荐获取设置条件接口
   * 
   * @return
   */
  @RequestMapping(value = "/data/pubrecommend/getconditional")
  @ResponseBody()
  public Object pubRecommendConditional(@RequestBody PubQueryDTO pubQueryDTO) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // Long psnId = pubQueryDTO.getSearchPsnId();
      long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(pubQueryDTO.getDes3SearchPsnId()), 0L);
      if (psnId > 0) {
        pubQueryDTO.setSearchPsnId(psnId);
        pubRecommendService.initScienAreaAndCode(psnId);
        pubRecommendService.pubRecommendConditionsShow(pubQueryDTO);
        result.put("areaList", pubQueryDTO.getAreaList());
        result.put("keyList", pubQueryDTO.getKeyList());
        result.put("pubTypeList", pubQueryDTO.getPubTypeList());
        result.put("pubYearMap", pubQueryDTO.getPubYearMap());
        result.put("defultArea", pubQueryDTO.getDefultArea());
        result.put("defultKeyJson", pubQueryDTO.getDefultKeyJson());
      }
    } catch (Exception e) {
      logger.error("进入推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return result;
  }

  /**
   * 论文推荐查询接口
   * 
   * @param pubQueryDTO
   * @return
   */
  @RequestMapping(value = "/data/pubrecommend/publist")
  @ResponseBody()
  public Object searchPubList(@RequestBody PubQueryDTO pubQueryDTO) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    PubListVO pubListVO = new PubListVO();
    try {
      if (pubQueryDTO.getSearchPsnId() > 0) {
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setServiceType("recommendListInSolr");
        pubRecommendService.pubRecommendListSearch(pubListVO);
        // 获取数据
        resultMap.put("resultList", pubListVO.getResultList());
        resultMap.put("totalCount", pubListVO.getTotalCount());
      }
    } catch (Exception e) {
      logger.error("查询推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return resultMap;
  }

  /**
   * 移动端保存科技领域设置
   * 
   * @return
   */
  @RequestMapping(value = "/data/pubrecommend/savepsnareas")
  @ResponseBody
  public Object savePsnAreas(@RequestBody Map<String, Object> map) {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      String des3PsnId = (String) map.get("des3PsnId");
      if (des3PsnId != null) {
        Long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
        String defultArea = (String) map.get("defultArea");
        pubRecommendService.savePsnArea(defultArea, psnId);
        resultMap.put("result", "success");
      }
    } catch (Exception e) {
      resultMap.put("result", "error");
      logger.error("移动端保存科技领域设置出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return resultMap;
  }

  /**
   * 移动端保存关键词设置
   * 
   * @return
   */
  @RequestMapping(value = "/data/pubrecommend/savepsnkeywords")
  @ResponseBody()
  public Object savePsnKeyWords(@RequestBody Map<String, Object> map) {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      String des3PsnId = (String) map.get("des3PsnId");
      if (des3PsnId != null) {
        Long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
        String defultKeyJson = (String) map.get("defultKeyJson");
        pubRecommendService.savePsnKeyWord(defultKeyJson, psnId);
        resultMap.put("result", "success");
      }
    } catch (Exception e) {
      resultMap.put("result", "error");
      logger.error("移动端保存关键词设置出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return resultMap;
  }

  /**
   * 添加科技领域
   */
  @RequestMapping(value = "/data/pubrecommend/addscienarea")
  @ResponseBody
  public String addScienArea(String des3PsnId, String addPsnAreaCode) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(addPsnAreaCode)) {
        resultMap = pubRecommendService.pubAddScienArea(psnId, addPsnAreaCode);
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 批量添加科技领域
   */
  @RequestMapping(value = "/data/pub/recommend/addscienareas", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String addScienAreasBatch(PubRecommendVO VO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(VO.getDes3PsnId()));
    String addPsnAreaCodes = VO.getAddPsnAreaCodeList();
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(addPsnAreaCodes)) {
        String[] codeArr = addPsnAreaCodes.split(",");
        if (codeArr != null && codeArr.length > 0) {
          for (String code : codeArr) {
            pubRecommendService.pubAddScienArea(psnId, code);
          }
        }
        resultMap.put("result", "success");
      } else {
        resultMap.put("errMsg", "psnId or addPsnAreaCodes is null");
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return JacksonUtils.jsonMapSerializer(resultMap);
  }

  /**
   * 删除科技领域
   */
  @RequestMapping(value = "/data/pubrecommend/deletescienarea")
  @ResponseBody
  public String deleteScienArea(String des3PsnId, String deletePsnAreaCode) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(deletePsnAreaCode)) {
        resultMap = pubRecommendService.pubDeleteScienArea(psnId, deletePsnAreaCode);
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 批量删除科技领域
   */
  @RequestMapping(value = "/data/pub/recommend/deletescienareas", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object deleteScienAreasBatch(PubRecommendVO VO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(VO.getDes3PsnId()));
    String deletePsnAreaCodes = VO.getDeletePsnAreaCodeList();
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(deletePsnAreaCodes)) {
        String[] codeArr = deletePsnAreaCodes.split(",");
        if (codeArr != null && codeArr.length > 0) {
          for (String code : codeArr) {
            pubRecommendService.pubDeleteScienArea(psnId, code);
          }
        }
        resultMap.put("result", "success");
      } else {
        resultMap.put("errMsg", "psnId or deletePsnAreaCodes is null");
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 增加关键词
   */
  @RequestMapping(value = "/data/pubrecommend/addkeyword")
  @ResponseBody
  public String addKeyWord(String des3PsnId, String addPsnKeyWord) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(addPsnKeyWord)) {
        resultMap = pubRecommendService.pubAddKeyWord(psnId, addPsnKeyWord);
      }
    } catch (Exception e) {
      logger.error("论文推荐添加关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 批量增加关键词
   */
  @RequestMapping(value = "/data/pub/recommend/addkeywords", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object addKeyWordBatch(PubRecommendVO VO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(VO.getDes3PsnId()));
    String addPsnKeyWord = VO.getAddPsnKeyWord();// 单个关键词
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(addPsnKeyWord)) {
        pubRecommendService.pubAddKeyWord(psnId, addPsnKeyWord);
        resultMap.put("result", "success");
      } else {
        resultMap.put("errMsg", "psnId or addPsnKeyWords is null");
      }
    } catch (Exception e) {
      logger.error("论文推荐添加关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 批量删除关键词
   */
  @RequestMapping(value = "/data/pub/recommend/deletekeywords", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object deleteKeyWord(PubRecommendVO VO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(VO.getDes3PsnId()));
    String deletePsnKeyWord = VO.getDeletePsnKeyWord();// 单个关键词
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(deletePsnKeyWord)) {
        pubRecommendService.pubDeleteKeyWord(psnId, deletePsnKeyWord);
        resultMap.put("result", "success");
      } else {
        resultMap.put("errMsg", "psnId or addPsnKeyWords is null");
      }
    } catch (Exception e) {
      logger.error("论文推荐删除关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 删除关键词
   */
  @RequestMapping(value = "/data/pubrecommend/deletekeyword")
  @ResponseBody
  public String deleteKeyWordBatch(String des3PsnId, String deletePsnKeyWord) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    try {
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(deletePsnKeyWord)) {
        resultMap = pubRecommendService.pubDeleteKeyWord(psnId, deletePsnKeyWord);
      }
    } catch (Exception e) {
      logger.error("论文推荐删除关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "0");
    }

    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 获取全部关键词
   */
  @RequestMapping(value = "/data/pubrecommend/allpsnkeylist")
  @ResponseBody()
  public Object allPsnKeyList(@RequestBody String des3PsnId) {
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    List<RecommendDisciplineKey> allkeyList = new ArrayList<RecommendDisciplineKey>();
    try {
      if (!NumberUtils.isNullOrZero(psnId)) {
        allkeyList = pubRecommendService.getPsnAllKeyWord(psnId);
      }
    } catch (Exception e) {
      logger.error("论文推荐删除关键词出错， psnId = " + psnId, e);
    }
    return allkeyList;
  }

  /**
   * 首页动态列表论文推荐
   * 
   * @param pubQueryDTO
   * @return
   */
  @RequestMapping(value = "/data/pub/recommend/getpubrecommendshowindyn")
  @ResponseBody()
  public Object getPubRecommendShowInDyn(@RequestBody PubQueryDTO pubQueryDTO) {
    Map<String, Object> result = new HashMap<String, Object>();
    PubListVO pubListVO = new PubListVO();
    try {
      Long psnId = pubQueryDTO.getPsnId();
      if (psnId > 0) {
        pubQueryDTO.setSearchPsnId(psnId);
        // pubRecommendService.initScienAreaAndCode(psnId);
        pubRecommendService.pubRecommendConditions(pubQueryDTO);
        if (StringUtils.isBlank(pubQueryDTO.getSearchPsnKey())) {
          pubQueryDTO.setSearchPsnKey(pubQueryDTO.getDefultKeyJson());
        }
        pubQueryDTO.setPageSize(1);
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setServiceType("recommendListInSolr");
        pubRecommendService.pubRecommendListSearch(pubListVO);
        // 获取数据
        result.put("resultList", pubListVO.getResultList());
        result.put("status", "success");
      }
    } catch (Exception e) {
      result.put("status", "error");
      logger.error("获取首页动态列表论文推荐出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return result;
  }

  @RequestMapping("/data/pub/opt/uninterestedremdpub")
  @ResponseBody()
  public Object getPdwhCommentNumber(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    try {
      if (psnId > 0) {
        pubRecommendService.insertPubRecmRecord(psnId, pubId);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("首页动态推荐论文不感兴趣操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }
}
