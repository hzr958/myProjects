package com.smate.web.mobile.controller.web.pub;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.filter.FilterAllSpecialCharacter;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.dto.PubQueryDTO;
import com.smate.web.mobile.v8pub.service.MobilePubQueryService;
import com.smate.web.mobile.v8pub.vo.PubCommentVO;
import com.smate.web.mobile.v8pub.vo.PubListVO;
import com.smate.web.mobile.v8pub.vo.PubQueryModel;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @ClassName PubListController
 * @Description 成果列表获取控制器
 * @Author LIJUN
 * @Date 2018/8/15
 * @Version v1.0
 */
@Controller("mPubWebListController")
public class PubWebListController extends WeChatBaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  private String referer;
  @Autowired
  private MobilePubQueryService mobilePubQueryService;

  /**
   * @return java.lang.Object
   * @Author LIJUN
   * @Description 成果列表数据
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id]
   **/
  @RequestMapping(value = {"/pub/querylist/psn", "/pub/outside/querylist/psn"})
  @SuppressWarnings("unchecked")
  public ModelAndView queryMyPubList(HttpServletRequest request, PubQueryModel pubQueryModel) {
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    PubQueryDTO pubQueryDTO = this.initPubQueryDTO(pubQueryModel, psnId, pubListVO, "pubList");
    if (!"yes".equals(pubListVO.getSelf()) && pubQueryDTO.getSearchPsnId() != null) {// 查看是否有隐私成果
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      params.add("des3PsnId", Des3Utils.encodeToDes3(pubQueryDTO.getSearchPsnId().toString()));
      Map<String, Object> object = (Map<String, Object>) restTemplate
          .postForObject(domainscm + PubApiInfoConsts.PSN_HAS_PRIVATE_PUB, params, Object.class);
      if (object != null) {
        Boolean hasPrivatePub = (Boolean) object.get("hasPrivatePub");
        if (hasPrivatePub != null) {
          pubListVO.setHasPrivatePub(hasPrivatePub);
        }
      }
    }
    if (!NumberUtils.isNullOrZero(psnId)) {
      pubQueryDTO.setPsnId(psnId);
      pubListVO.setHasLogin(1);
    }
    if (StringUtils.isNoneBlank(pubQueryModel.getDes3SearchPsnId())) {
      pubListVO.setOther(true);
    } else {
      pubListVO.setOther(false);
    }
    // 添加返回上一个界面
    String tempReferer = request.getHeader("referer");
    if (StringUtils.isBlank(tempReferer)) {
      setReferer("/pub/querylist/psn");
    } else if (!tempReferer.contains("querylist/psn")) {
      setReferer(tempReferer);
    }
    ModelAndView view = new ModelAndView();
    view.addObject("pubListVO", pubListVO);
    view.addObject("pubQueryModel", pubQueryModel);
    if ("dyn".equals(pubQueryModel.getFromPage())) {
      view.setViewName("/pub/publist/mobile_dyn_publish_pub_main");
    } else {
      view.setViewName("/pub/publist/new_mobile_pub");
    }
    return view;
  }

  /**
   * @return java.lang.Object
   * @Author LIJUN
   * @Description 成果列表数据 ajax请求用
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id]
   **/
  @RequestMapping(value = {"/pub/querylist/ajaxpsn", "/pub/outside/querylist/ajaxpsn"})
  @SuppressWarnings("unchecked")
  public ModelAndView queryMyPubSubList(PubQueryModel pubQueryModel) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      PubListVO pubListVO = new PubListVO();
      if (!NumberUtils.isNullOrZero(psnId)) {
        pubListVO.setHasLogin(1);
      }
      pubListVO.setCurrentLoginPsnId(psnId);
      PubQueryDTO pubQueryDTO = this.initPubQueryDTO(pubQueryModel, psnId, pubListVO, "pubList");
      pubQueryDTO.setPsnId(psnId);
      // 设置查询属性
      pubQueryDTO.setPageNo(Integer.parseInt(pubQueryModel.getNextId()) + 1);
      mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
      view.addObject("pubQueryModel", pubQueryModel);
      view.addObject("pubListVO", pubListVO);
    } catch (Exception e) {
      logger.error("mobile获取成果列表数据出错,pubId=" + psnId, e);
    }
    if ("dyn".equals(pubQueryModel.getFromPage())) {
      view.setViewName("/pub/publist/mobile_dyn_publish_pub_list");
    } else {
      view.setViewName("/pub/publist/new_mobile_pub_sub");
    }
    return view;
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
    // 由于在get方法里面做了处理，所以不知道searchPsnId到底有没有值
    Long searchPsnId = pubQueryDTO.getSearchPsnId();
    pubQueryDTO.setSearchPsnId(searchPsnId);
    if ((psnId != null && psnId > 0L) || (searchPsnId != null && searchPsnId > 0L)) {
      pubQueryDTO.setDes3SearchPsnId(Des3Utils.encodeToDes3(searchPsnId.toString()));
      if (CommonUtils.compareLongValue(psnId, searchPsnId)) {
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
   * @return java.lang.Object
   * @Author LIJUN
   * @Description //获取我的代表成果列表数据
   * @Date 14:14 2018/8/17
   * @Param [pubQueryModel]
   **/
  @RequestMapping(value = {"/pub/outside/querylist/represent", "/pub/querylist/represent"})
  @SuppressWarnings("unchecked")
  public ModelAndView getRepresentPublist(PubQueryModel pubQueryModel) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    Long searchPsnId = pubQueryDTO.getSearchPsnId();
    PubListVO pubListVO = new PubListVO();
    if (searchPsnId == null && psnId != null) { // 没有传id就默认为当前用户
      searchPsnId = psnId;
      pubListVO.setFromPage("psn");
      pubListVO.setSelf("yes");
    } else if (psnId != null && psnId.equals(searchPsnId)) {
      pubListVO.setFromPage("psn");
      pubListVO.setSelf("yes");
    } else {
      pubListVO.setFromPage("otherpsn");
      pubListVO.setSelf("no");
      pubListVO.setDes3SearchPsnId(Des3Utils.encodeToDes3(searchPsnId.toString()));
    }

    pubQueryDTO.setPsnId(psnId);
    ModelAndView view = new ModelAndView();
    if ((psnId != null && psnId > 0L) || (searchPsnId != null && searchPsnId > 0L)) {
      try {
        pubQueryDTO.setServiceType("psnRepresentPubList");
        pubListVO.setPubQueryDTO(pubQueryDTO);
        Map<String, Object> object = (Map<String, Object>) restTemplate
            .postForObject(domainscm + PubApiInfoConsts.PUB_LIST, pubListVO.getPubQueryDTO(), Object.class);
        if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
          resultList = (List<Map<String, Object>>) object.get("resultList");
          Object totalCount = object.get("totalCount");
          // pubListVO.setTotalCount(NumberUtils.toInt(totalCount.toString()));
          pubListVO.setHasLogin(1);
          List<PubInfo> list = new ArrayList<>(16);

          if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
              PubInfo pubInfo = new PubInfo();
              try {
                BeanUtils.populate(pubInfo, map);
                list.add(pubInfo);
              } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("复制属性异常", e);
              }
            }
          }
          pubListVO.setResultList(list);
        }
        view.addObject("pubQueryModel", pubQueryModel);

        view.addObject("pubListVO", pubListVO);
      } catch (Exception e) {
        logger.error("mobile获取个人代表成果列表数据出错,pubId=" + psnId, e);
      }
    }
    view.setViewName("/pub/publist/mobile_represent_pub");
    return view;
  }

  /**
   * 个人库成果评论列表
   *
   * @param pubCommentVO{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/pub/querylist/ajaxcommentlist", "/pub/outside/ajaxcommentlist"})
  public ModelAndView pubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubCommentVO.getPubId();
    ModelAndView view = new ModelAndView();
    if (pubId != null && pubId > 0L) {
      try {
        Integer pageSize = pubCommentVO.getMaxresult();
        pubCommentVO.setPsnId(psnId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(V8pubConst.DES3_PUB_ID, pubCommentVO.getDes3PubId());
        params.add("serviceType", "snsPub");
        params.add("psnId", psnId.toString());
        params.add("pageNo", pubCommentVO.getPageNo().toString());
        params.add("page.ignoreMin", Boolean.TRUE.toString());
        params.add("page.pageSize", NumberUtils.isNotNullOrZero(pageSize) ? pageSize.toString()
            : pubCommentVO.getPage().getPageSize().toString());
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_LIST_SNS,
            RestUtils.buildPostRequestEntity(params), Map.class);
        view.addObject("replyInfoList", map);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("获取个人库评论列表数据接口出错" + pubCommentVO.getPubId(), e);
      }
    }
    view.addObject("pubCommentVO", pubCommentVO);
    view.setViewName("/pub/pubdetails/mobile_pub_reply");
    return view;
  }

  /**
   * 个人收藏的成果列表
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping("/pub/querylist/ajaxcollect")
  public ModelAndView psnCollectedPubList(PubQueryModel pubQueryModel) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    pubListVO.setDes3PsnId(Des3Utils.encodeToDes3(currentPsnId.toString()));
    pubListVO.setCurrentLoginPsnId(currentPsnId);
    try {
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      try {
        BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
        pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
      } catch (Exception e) {
        logger.error("复制属性异常", e);
      }
      pubQueryDTO.setServiceType("pubCollectedList");
      pubQueryDTO.setSearchPsnId(currentPsnId);
      pubListVO.setPubQueryDTO(pubQueryDTO);
      pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);
      mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
    } catch (Exception e) {
      logger.error("移动端获取个人收藏成果列表出错， psnId = " + currentPsnId, e);
    }
    // 对所有标题的符号进行转义
    List<PubInfo> resultList = pubListVO.getResultList();
    for (int i = 0; i < resultList.size(); i++) {
      PubInfo pubInfo = resultList.get(i);
      String title = StringEscapeUtils.unescapeHtml4(pubInfo.getTitle());
      pubInfo.setTitle(title);
      resultList.set(i, pubInfo);
    }
    pubListVO.setResultList(resultList);
    view.addObject("pubListVO", pubListVO);
    view.setViewName("/pub/publist/mobile_collect_pub_list");
    return view;
  }

  @RequestMapping("/pub/querylist/ajaxpdwhpub")
  public ModelAndView searchPdwhPub(PubQueryModel pubQueryModel) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    pubListVO.setCurrentLoginPsnId(SecurityUtils.getCurrentUserId());
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
          pubQueryModel.setSearchString(FilterAllSpecialCharacter.stringFilterExcludeFiveChar(searchString));
        }
      }
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      try {
        BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
        pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
        pubQueryDTO.setSearchArea(getAreaString(pubQueryModel.getDes3AreaId()));
      } catch (Exception e) {
        logger.error("复制属性异常", e);
      }
      if (StringUtils.isBlank(pubQueryDTO.getServiceType())) {
        pubQueryDTO.setServiceType("findListInSolr");
      }
      pubListVO.setPubQueryDTO(pubQueryDTO);
      pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);
      mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
    } catch (Exception e) {
      logger.error("移动端---》我的论文---》发现，获取论文列表失败", e);
    }
    view.addObject("pubListVO", pubListVO);
    view.setViewName("/pub/publist/mobile_find_pub_list");
    return view;
  }

  private String getAreaString(String des3AreaId) {
    String areaStr = "";
    if (StringUtils.isNotBlank(des3AreaId)) {
      String[] areaIds = des3AreaId.split(",");
      areaStr = Stream.of(areaIds).map(ids -> Des3Utils.decodeFromDes3(ids)).collect(Collectors.joining(","));
    }
    return areaStr;
  }

  /**
   * 基准库成果评论列表
   *
   * @param pubCommentVO{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/pub/querylist/ajaxpdwhcomment", "/pub/outside/ajaxpdwhcomment"})
  public ModelAndView pdwhPubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubCommentVO.getPubId();
    ModelAndView view = new ModelAndView();
    if (psnId != null && psnId > 0L && pubId != null && pubId > 0L) {
      try {
        pubCommentVO.setPsnId(psnId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(V8pubConst.DES3_PUB_ID, pubCommentVO.getDes3PubId());
        params.add("serviceType", "pdwhPub");
        params.add("psnId", psnId.toString());
        params.add("page.pageSize", pubCommentVO.getPage().getPageSize().toString());
        params.add("page.pageNo", pubCommentVO.getPage().getPageNo().toString());
        params.add("page.ignoreMin", Boolean.TRUE.toString());
        params.add("locale", "zh_CN");
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_LIST_PDWH,
            RestUtils.buildPostRequestEntity(params), Map.class);
        view.addObject("replyInfoList", map);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("获取个人库评论列表数据接口出错" + pubCommentVO.getPubId(), e);
      }
    }
    view.addObject("pubCommentVO", pubCommentVO);
    view.setViewName("/pub/pubdetails/mobile_pub_reply");
    return view;
  }

  /**
   * @return java.lang.Object
   * @Author WSN
   * @Description 移动端聊天窗口选择成果列表
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id]
   **/
  @RequestMapping(value = "/pub/querylist/ajaxsimple")
  @SuppressWarnings("unchecked")
  public ModelAndView queryPubListForChat(PubQueryModel pubQueryModel) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setCurrentLoginPsnId(psnId);
    ModelAndView view = new ModelAndView();
    if (psnId != null && psnId > 0L) {
      try {
        pubQueryDTO.setSelf(true);
        pubQueryDTO.setDes3SearchPsnId(Des3Utils.encodeToDes3(psnId.toString()));
        pubQueryDTO.setServiceType("pubList");
        // 设置查询属性
        // pubQueryDTO.setPageNo(Integer.parseInt(pubQueryModel.getNextId())
        // + 1);
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.setRestfulUrl(domainscm + PubApiInfoConsts.PUB_LIST);
        mobilePubQueryService.buildPubListVO(pubListVO, Object.class);
        view.addObject("pubQueryModel", pubQueryModel);
        view.addObject("pubListVO", pubListVO);
      } catch (Exception e) {
        logger.error("mobile获取成果列表数据出错,pubId=" + psnId, e);
      }
    }
    view.setViewName("/pub/publist/mobile_simple_pub_list");
    return view;
  }

  @RequestMapping("/pub/querylist/find/conditions")
  public ModelAndView showFindPubConditions(PubQueryModel model) {
    ModelAndView view = new ModelAndView();
    try {
      // 获取当前年份
      Calendar cal = Calendar.getInstance();
      model.setCurrentYear(cal.get(Calendar.YEAR));
    } catch (Exception e) {
      logger.error("移动端个人成果--》检索条件页面出错, psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject("model", model);
    view.setViewName("/pub/collectpub/mobile_find_psn_pub_conditions");
    return view;
  }

  /**
   * 获取成果所在位置和总共要显示的成果数量
   * 
   * @return
   */
  @RequestMapping(value = "/pub/outside/ajaxindex", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String findPsnPubIndexAndTotalCount(PubOperateVO pubOperateVO) {
    Map<String, String> result = new HashMap<String, String>();
    Map<String, Object> param = new HashMap<String, Object>();
    try {
      // 默认按发表年份排序
      if (StringUtils.isBlank(pubOperateVO.getOrderBy()) || "DEFAULT".equalsIgnoreCase(pubOperateVO.getOrderBy())) {
        pubOperateVO.setOrderBy("PUBLISH_YEAR");
      }
      // 默认认为查看的是当前登录人员的成果
      String des3SearchPsnId = pubOperateVO.getDes3SearchPsnId();
      if (StringUtils.isBlank(des3SearchPsnId)) {
        des3SearchPsnId = Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString());
      }
      // 是否是本人
      if (CommonUtils.compareLongValue(SecurityUtils.getCurrentUserId(), NumberUtils.toLong(des3SearchPsnId, -1L))) {
        param.put("self", true);
      }
      param.put("des3PubId", pubOperateVO.getDes3PubId());
      param.put("orderBy", pubOperateVO.getOrderBy());
      param.put("publishYear", pubOperateVO.getPublishYear());
      param.put("searchPubType", pubOperateVO.getSearchPubType());
      param.put("includeType", pubOperateVO.getIncludeType());
      param.put("des3SearchPsnId", des3SearchPsnId);
      result = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_INDEX_AND_TOTAL_COUNT, param, Map.class);
    } catch (Exception e) {
      logger.error("获取查询成果总数和指定成果在查询成果中的位置异常", e);
    }
    return JacksonUtils.mapToJsonStr(result);
  }

  public String getReferer() {
    return referer;
  }

  public void setReferer(String referer) {
    this.referer = referer;
  }

}
