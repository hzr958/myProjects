package com.smate.web.mobile.controller.data.list.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.solr.SolrUtil;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.dto.PubQueryDTO;
import com.smate.web.mobile.v8pub.service.MobilePubQueryService;
import com.smate.web.mobile.v8pub.vo.PubListVO;
import com.smate.web.mobile.v8pub.vo.PubQueryModel;

/**
 * @ClassName PubListController
 * @Description 成果列表获取控制器
 * @Author LIJUN
 * @Date 2018/8/15
 * @Version v1.0
 */
@RestController("mPubDataListController")
@RequestMapping("/data/pub/querylist")
public class PubDataListController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Page<?> page = new Page<Object>();
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private MobilePubQueryService mobilePubQueryService;

  /**
   * @return java.lang.Object
   * @Author LIJUN
   * @Description 成果列表数据
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id]
   **/
  @RequestMapping(value = "/psn", produces = "application/json;charset=UTF-8")
  @SuppressWarnings("unchecked")
  @ResponseBody
  public Object queryMyPubList(PubQueryModel pubQueryModel) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    pubListVO.setCurrentLoginPsnId(psnId);
    PubQueryDTO pubQueryDTO = this.initPubQueryDTO(pubQueryModel, psnId, pubListVO, "pubList");
    if (psnId != null && psnId > 0L && pubQueryDTO.getSearchPsnId() != null && pubQueryDTO.getSearchPsnId() > 0L
        && StringUtils.isNotBlank(pubQueryModel.getNextId())) {
      try {
        pubQueryDTO.setPsnId(psnId);
        pubQueryDTO.setPageNo(Integer.parseInt(pubQueryModel.getNextId()) + 1);
        mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
        resultmap.put("result", "success");
      } catch (Exception e) {
        resultmap.put("result", "error");
        resultmap.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取成果认领列表数据出错,psnId=" + psnId, e);
      }
    } else {
      resultmap.put("result", "error");
      resultmap.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * @return java.lang.Object
   * @Author LIJUN
   * @Description 成果收藏列表数据
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id]
   **/
  @RequestMapping(value = "/collect", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object queryCollectPubList(PubQueryModel pubQueryModel) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    pubListVO.setCurrentLoginPsnId(psnId);
    PubQueryDTO pubQueryDTO = this.initPubQueryDTO(pubQueryModel, psnId, pubListVO, "pubCollectedList");
    if (psnId != null && psnId > 0L && pubQueryDTO.getSearchPsnId() != null && pubQueryDTO.getSearchPsnId() > 0L) {
      try {
        mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
        resultmap.put("result", "success");
      } catch (Exception e) {
        resultmap.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取成果认领列表数据出错,psnId=" + psnId, e);
      }
    } else {
      resultmap.put("result", "error");
      resultmap.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * @return java.lang.Object
   * @Author LIJUN
   * @Description 获取个人成果认领列表数据
   * @Date 13:46 2018/8/17
   * @Param [pubQueryModel]
   **/
  @RequestMapping(value = "/confirm", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getPubconfirmList(PubQueryModel pubQueryModel) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    pubListVO.setCurrentLoginPsnId(psnId);
    PubQueryDTO pubQueryDTO = this.initPubQueryDTO(pubQueryModel, psnId, pubListVO, "pubConfirmList");
    if (psnId != null && psnId > 0L) {
      try {
        pubQueryDTO.setSearchPsnId(psnId);
        mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
        resultmap.put("result", "success");
      } catch (Exception e) {
        resultmap.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取成果认领列表数据出错,psnId=" + psnId, e);
      }
    } else {
      resultmap.put("result", "error");
      resultmap.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * @return java.lang.Object
   * @Author LIJUN
   * @Description //获取我的代表成果列表数据
   * @Date 14:14 2018/8/17
   * @Param [pubQueryModel]
   **/
  @RequestMapping(value = "/represent", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getRepresentPublist(PubQueryModel pubQueryModel) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    pubListVO.setCurrentLoginPsnId(psnId);
    // PubQueryDTO pubQueryDTO = this.initPubQueryDTO(pubQueryModel, psnId, pubListVO,
    // "psnRepresentPubList");
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setDes3SearchPsnId(Des3Utils.encodeToDes3(pubQueryModel.getSearchPsnId().toString()));
    pubQueryDTO.setPsnId(psnId);
    pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    pubQueryDTO.setServiceType("psnRepresentPubList");
    pubListVO.setPubQueryDTO(pubQueryDTO);
    pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);

    if (psnId != null && psnId > 0L && pubQueryDTO.getSearchPsnId() != null && pubQueryDTO.getSearchPsnId() > 0L) {
      try {
        mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
        resultmap.put("result", "success");
      } catch (Exception e) {
        resultmap.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取个人代表成果列表数据出错,psnId=" + psnId, e);
      }
    } else {
      resultmap.put("result", "error");
      resultmap.put("errmsg", "参数校验不通过");
    }

    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * @return java.lang.Object
   * @Author LTL
   * @Description //保存代表成果
   * @Date 14:14 2018/8/17
   * @Param [pubQueryModel]
   **/
  @RequestMapping(value = "/saverepresent", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object saveRepresent(PubQueryModel pubQueryModel) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    String addToRepresentPubIds = pubQueryModel.getRepresentDes3PubIds();
    if (psnId > 0 && StringUtils.isNotBlank(addToRepresentPubIds) && addToRepresentPubIds.split(",").length <= 10) {
      String representUrl = PubApiInfoConsts.SAVE_REPRESENT_PUB;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));

      param.add("addToRepresentPubIds", addToRepresentPubIds);
      resultMap = postFormUrl(param, representUrl);// 调接口查询代表成果
    } else {
      resultMap.put("result", "error");
      resultMap.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(null, null,
        AppActionUtils.changeResultStatus(Objects.toString(resultMap.get("result"), "error")),
        Objects.toString(resultMap.get("errmsg"), ""));
  }


  /**
   * @return java.lang.Object
   * @Author WSN
   * @Description 查询单个成果信息
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id, des3PubId 成果ID, pubDB 个人库（SNS）还是基准库(PDWH)成果]
   **/
  @RequestMapping(value = "/single", produces = "application/json;charset=UTF-8")
  @ResponseBody
  @SuppressWarnings("unchecked")
  public String querySnsPubListByPubId(PubQueryModel pubQueryModel) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    List<Map<String, Object>> resultList = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    // 要查询的成果ID
    Long searchPubId = pubQueryDTO.getSearchPubId();
    // 成果所属库，基准库还是个人库
    String pubDB = pubQueryModel.getPubDB();
    PubListVO pubListVO = new PubListVO();
    pubListVO.setCurrentLoginPsnId(SecurityUtils.getCurrentUserId());
    if (searchPubId != null && searchPubId > 0L && StringUtils.isNotBlank(pubDB)) {
      try {
        pubQueryDTO.setServiceType("pubQueryByPubId");
        if ("PDWH".equals(pubDB)) {
          pubQueryDTO.setServiceType("pdwhPubQueryByPubId");
        }
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);
        mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
        resultmap.put("result", "success");
      } catch (Exception e) {
        resultmap.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取查询单个成果数据出错,pubId=" + searchPubId + ", pubDB = " + pubDB, e);
      }
    } else {
      resultmap.put("result", "error");
      resultmap.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  private PubQueryDTO initPubQueryDTO(PubQueryModel pubQueryModel, Long psnId, PubListVO pubListVO,
      String serviceType) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    Long searchPsnId = pubQueryDTO.getSearchPsnId();
    if (searchPsnId == null && psnId != null) { // 没有传id就默认为当前用户
      searchPsnId = psnId;
      pubQueryDTO.setSearchPsnId(psnId);
    }
    if ("DEFAULT".equals(pubQueryDTO.getOrderBy())) {
      pubQueryDTO.setOrderBy("publishYear");
    }
    if (psnId != null && psnId > 0L && searchPsnId != null && searchPsnId > 0L) {
      pubQueryDTO.setDes3SearchPsnId(Des3Utils.encodeToDes3(searchPsnId.toString()));
      if (psnId.equals(searchPsnId)) {
        pubListVO.setSelf("yes");
        pubListVO.setOther(false);
      } else {
        pubListVO.setFromPage("otherpsn");
        pubListVO.setPsnName(pubQueryModel.getShowName());
        pubListVO.setOther(true);
      }
      pubQueryDTO.setServiceType(serviceType);
      pubListVO.setPubQueryDTO(pubQueryDTO);
    }
    pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);
    return pubQueryDTO;
  }

  /**
   * 从solr中检索基准库成果
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping(value = "/ajaxpdwhpub", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object searchPdwhPub(PubQueryModel pubQueryModel) {
    PubListVO pubListVO = new PubListVO();
    pubListVO.setCurrentLoginPsnId(SecurityUtils.getCurrentUserId());
    Map<String, Object> resultmap = new HashMap<String, Object>();
    try {
      String searchString = StringEscapeUtils.unescapeHtml4(pubQueryModel.getSearchString());
      Boolean isDoi = JnlFormateUtils.isDoi(searchString);
      // 特殊字符处理
      if (isDoi) {
        pubQueryModel.setIsDoi(true);
        pubQueryModel.setSearchString(FilterAllSpecialCharacter.StringFilter(searchString));
      } else {
        pubQueryModel.setIsDoi(false);
        // 只有这五种特殊字符 , exit
        if (FilterAllSpecialCharacter.isExcludedChars(searchString)) {
          pubQueryModel.setSearchString("");
        } else {
          pubQueryModel.setSearchString(SolrUtil.escapeQueryChars(searchString));
        }
      }
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      try {
        BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
        pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
        if (StringUtils.isBlank(pubQueryDTO.getSearchArea())) {
          pubQueryDTO.setSearchArea(getAreaString(pubQueryModel.getDes3AreaId()));
        }
      } catch (Exception e) {
        logger.error("复制属性异常", e);
        resultmap.put("errmsg", "pubQueryDTO从pubQueryModel复制属性异常");
      }
      if (StringUtils.isBlank(pubQueryDTO.getServiceType())) {
        pubQueryDTO.setServiceType("findListInSolr");
      }
      pubListVO.setPubQueryDTO(pubQueryDTO);
      pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);
      mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
      resultmap.put("result", "success");
    } catch (Exception e) {
      resultmap.put("result", "error");
      logger.error("移动端---》我的论文---》发现，获取论文列表失败", e);
    }
    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  private String getAreaString(String des3AreaId) {
    String areaStr = "";
    if (StringUtils.isNotBlank(des3AreaId)) {
      String[] areaIds = des3AreaId.split(",");
      areaStr = Stream.of(areaIds).map(ids -> Des3Utils.decodeFromDes3(ids)).collect(Collectors.joining(","));
    }
    return areaStr;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> postJsonUrl(MultiValueMap param, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return (Map<String, Object>) restTemplate.postForObject(domainscm + url, HttpEntity, Object.class);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> postFormUrl(MultiValueMap param, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return (Map<String, Object>) restTemplate.postForObject(domainscm + url, HttpEntity, Object.class);
  }
}
