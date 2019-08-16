package com.smate.web.mobile.controller.web.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.dto.PubQueryDTO;
import com.smate.web.mobile.v8pub.service.PubCacheService;
import com.smate.web.mobile.v8pub.service.PubDataDetailsService;
import com.smate.web.mobile.v8pub.vo.pubfulltext.PubFulltextPsnRcmdVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;
import com.smate.web.mobile.wechat.base.vo.WechatBaseVO;

/**
 * @ClassName PubDetailsController
 * @Description 移动端成果详情
 * @Author LIJUN
 * @Date 2018/8/14
 * @Version v1.0
 */
@Controller("mPubWebDetailsController")
public class PubWebDetailsController extends WeChatBaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PubDataDetailsService pubDataDetailsService;
  @Autowired
  private PubCacheService pubCacheService;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * @Author LIJUN
   * @Description 个人库成果详情页面
   * @Date 15:53 2018/8/14
   * @Param [pubOperateVO]
   * @return org.springframework.web.servlet.ModelAndView
   **/
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/pub/details/snsnonext", "/pub/outside/details/snsnonext"})
  public ModelAndView pubNoNextDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId > 0) {
      view.addObject("hasLogin", "true");
    } else {
      view.addObject("hasLogin", "false");
    }
    HashMap<String, Object> result = new HashMap<>();
    String des3PubId = pubOperateVO.getDes3PubId();
    try {
      params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "snsPub");
      params.put("psnId", psnId);
      if (StringUtils.isNotBlank(pubOperateVO.getDes3relationid())) {// 分享id不为空，用于判断隐私分享
        params.put("des3relationid", pubOperateVO.getDes3relationid());
      }
      result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS, params,
          Map.class);
      pubOperateVO.setViewStatus((String) result.get("status"));
      view.addObject("pubOperateVO", pubOperateVO);
      view.addObject("pubDetailVO", result.get("result"));
      WechatBaseVO wechatVO = new WechatBaseVO();
      try {
        wechatVO = buildWeChatData();
      } catch (Exception e) {
        logger.error("构建微信签名失败, pubId = " + Des3Utils.decodeFromDes3(des3PubId), e);
      }
      view.addObject("wechatVO", wechatVO);

    } catch (Exception e) {
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    view.setViewName("pub/pubdetails/mobile_pubdetail_no_next");
    return view;
  }

  /**
   * @Author LIJUN
   * @Description 个人库成果详情页面,新的移动端成果列表详情页面改为/pub/details/list
   * @Date 15:53 2018/8/14
   * @Param [pubOperateVO]
   * @return org.springframework.web.servlet.ModelAndView
   **/
  @Deprecated
  @SuppressWarnings({"unchecked"})
  @RequestMapping(value = "/pub/details/sns")
  public ModelAndView pubDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    if (StringUtils.isBlank(pubOperateVO.getDes3SearchPsnId()) && !NumberUtils.isNullOrZero(psnId)) {
      pubOperateVO.setDes3SearchPsnId(Des3Utils.encodeToDes3(psnId.toString()));
    }
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      if ((pubOperateVO.getUseoldform() == true) && pubOperateVO.getPubId() == null) {
        Object obj = pubCacheService.get("mPubOperateVO", getSession().getId());
        if (obj != null) {
          PubOperateVO oldvo = new PubOperateVO();
          oldvo = (PubOperateVO) obj;
          BeanUtils.copyProperties(pubOperateVO, oldvo);
        }
      }
      params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "snsPub");
      params.put("psnId", psnId);
      result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS, params,
          Map.class);
      if (!NumberUtils.isNullOrZero(SecurityUtils.getCurrentUserId())) {
        pubOperateVO.setHasLogin(true);
      } else {
        pubOperateVO.setHasLogin(false);
      }
      view.addObject("pubOperateVO", pubOperateVO);
      String status = (String) result.get("status");
      object = result.get("result");
      if ("no permission".equals(status)) {
        view.setViewName("pub/mobile_pub_no_authority");
      } else if ("not exists".equals(status) || "hasDeleted".equals(status)) {
        view.setViewName("pub/mobile_pub_not_exists");
      } else {
        view.setViewName("pub/pubdetails/mobile_pubdetail");
      }
      view.addObject("pubDetailVO", object);
      pubCacheService.put("mPubOperateVO", 20 * 60, getSession().getId(), pubOperateVO);
    } catch (Exception e) {
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    return view;
  }


  @SuppressWarnings({"unchecked"})
  @RequestMapping(value = {"/pub/details/list", "/pub/outside/details/list"})
  public ModelAndView pubDetailsList(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    PubDetailVO detail = new PubDetailVO();
    Map<String, String> result = new HashMap<String, String>();
    Map<String, Object> param = new HashMap<String, Object>();
    try {
      // 是否已登录
      Long currentUserId = SecurityUtils.getCurrentUserId();
      if (NumberUtils.isNotNullOrZero(currentUserId)) {
        pubOperateVO.setHasLogin(true);
      }
      // 默认认为查看的是当前登录人员的成果
      String des3SearchPsnId = pubOperateVO.getDes3SearchPsnId();
      if (StringUtils.isBlank(des3SearchPsnId)) {
        des3SearchPsnId = Des3Utils.encodeToDes3(currentUserId.toString());
      }
      // 是否是本人
      if (CommonUtils.compareLongValue(SecurityUtils.getCurrentUserId(),
          NumberUtils.toLong(Des3Utils.decodeFromDes3(des3SearchPsnId), -1L))) {
        param.put("self", true);
        detail.setOwner(true);
      }
      detail.setDes3PubId(pubOperateVO.getDes3PubId());
      pubOperateVO.setDomain(domainMobile);
      // 一些检索条件是存在sessionStore中的
      param.put("des3PubId", pubOperateVO.getDes3PubId());
      param.put("orderBy", pubOperateVO.getOrderBy());
      param.put("publishYear", pubOperateVO.getPublishYear());
      param.put("searchPubType", pubOperateVO.getSearchPubType());
      param.put("includeType", pubOperateVO.getIncludeType());
      param.put("des3SearchPsnId", des3SearchPsnId);
      // 获取指定成果在检索结果中的下标和总的检索的成果总数
      result = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_INDEX_AND_TOTAL_COUNT, param, Map.class);
      if ("success".equals(result.get("status"))) {
        pubOperateVO.setPubSum(NumberUtils.toInt(result.get("totalCount"), 0));
        pubOperateVO.setDetailPageNo(NumberUtils.toInt(result.get("pubIndex"), 0));
      }
    } catch (Exception e) {
      logger.error("获取查询成果总数和指定成果在查询成果中的位置异常", e);
    }
    view.addObject("pubOperateVO", pubOperateVO);
    view.addObject("pubDetailVO", detail);
    view.setViewName("pub/pubdetails/mobile_pubdetail");
    return view;
  }

  /**
   * @Author LIJUN
   * @Description 个人库成果详情页面,左右滑动加载页面
   * @Date 15:53 2018/8/14
   * @Param [pubOperateVO]
   * @return org.springframework.web.servlet.ModelAndView
   **/
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/pub/details/ajaxsns", "/pub/outside/details/ajaxsns"})
  public ModelAndView pubAjaxDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    // 先查询成果id
    if (StringUtils.isBlank(pubOperateVO.getDes3PubId()) || "login".equals(pubOperateVO.getReferrer())) {
      List<Map<String, Object>> resultList = new ArrayList<>();
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      pubQueryDTO.setFirst(pubOperateVO.getDetailPageNo());
      pubQueryDTO.setPageSize(pubOperateVO.getDetailPageSize());
      pubQueryDTO.setSearchPubType(pubOperateVO.getSearchPubType());
      pubQueryDTO.setIncludeType(pubOperateVO.getIncludeType());
      pubQueryDTO.setPublishYear(pubOperateVO.getPublishYear());
      pubQueryDTO.setOrderBy(pubOperateVO.getOrderBy());
      pubQueryDTO.setServiceType("pubList");
      if ("login".equals(pubOperateVO.getReferrer())) {
        pubQueryDTO.setIsQueryAll(false);
      }
      if (StringUtils.isNotBlank(pubOperateVO.getDes3SearchPsnId())) {
        pubQueryDTO.setSearchPsnId(NumberUtils.parseLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3SearchPsnId())));
      }
      Map<String, Object> listobject = (Map<String, Object>) restTemplate
          .postForObject(domainscm + PubApiInfoConsts.PUB_LIST, pubQueryDTO, Object.class);
      if (listobject != null && listobject.get("status").equals(V8pubConst.SUCCESS)) {
        resultList = (List<Map<String, Object>>) listobject.get("resultList");
        if (resultList != null && resultList.size() > 0) {
          if ("login".equals(pubOperateVO.getReferrer())) {
            // SCM-22453 更新登陆后查看的个人成果列表的pubId对应的页码
            for (int i = 0; i < resultList.size(); i++) {
              Map<String, Object> firstmap = resultList.get(i);
              if (firstmap.get("des3PubId").toString().equals(pubOperateVO.getDes3PubId())) {
                pubOperateVO.setDetailPageNo(i);
                pubOperateVO.setPubSum(resultList.size());
                break;
              }
            }
          } else {
            Map<String, Object> firstmap = resultList.get(0);
            if (firstmap != null) {
              pubOperateVO.setDes3PubId(firstmap.get("des3PubId").toString());
            }
            pubOperateVO.setReferrer("No need to update");
          }
        }
      }
    }
    // 获取到成果id后查询详情
    try {
      params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "snsPub");
      params.put("psnId", psnId);
      result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS, params,
          Map.class);
      view.addObject("pubDetailVO", pubOperateVO);
      String status = (String) result.get("status");
      if ("success".equals(status)) {
        object = result.get("result");
      } else if ("no permission".equals(status)) {
        view.setViewName("pub/mobile_pub_no_authority");
      } else if ("not exists".equals(status)) {
        view.setViewName("pub/mobile_pub_not_exists");
      }
      Object obj = pubCacheService.get("mPubOperateVO", getSession().getId());
      if (obj != null) {
        pubCacheService.put("mPubOperateVO", 20 * 60, getSession().getId(), pubOperateVO);
      }
      view.addObject("pubOperateVO", pubOperateVO);
      view.addObject("pubDetailVO", object);
    } catch (Exception e) {
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    view.setViewName("pub/pubdetails/mobile_pubxml_sub");
    return view;
  }

  /**
   * 个人成果查看，不带评论操作等.
   *
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/details/sns/simple")
  public ModelAndView viewSimple(String des3PubId) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    if (StringUtils.isNotBlank(des3PubId)) {
      params.put("serviceType", "snsPub");
      params.put(V8pubConst.DES3_PUB_ID, des3PubId);
      try {
        PubDetailVO pubDetailVO =
            restTemplate.postForObject(domainscm + "/pubdata/query/pubdetail", params, PubDetailVO.class);
        view.addObject("pubDetailVO", pubDetailVO);
      } catch (Exception e) {
        logger.error("mobile个人库成果详情simple页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(des3PubId), e);
      }
      view.setViewName("pub/pubdetails/mobile_test");

    } else {
      view.setViewName("pub/pubdetails/mobile_test");
    }
    return view;
  }

  /**
   * 基准库成果详情页面{"pubId":"21000000021"}
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/pub/details/pdwh", "/pub/outside/pdwh"})
  public ModelAndView pubPdwhDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO();
    String des3PubId = pubOperateVO.getDes3PubId();
    params.put(V8pubConst.DES3_PUB_ID, des3PubId);
    params.put("serviceType", "pdwhPub");
    try {
      Map<String, Object> pubMap = (Map<String, Object>) restTemplate
          .postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL, params, Object.class);
      Long psnId = SecurityUtils.getCurrentUserId();
      view.setViewName("pub/pubdetails/mobile_pub_not_exists");
      if (pubMap != null) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("des3pdwhPubId", des3PubId);
        Map<String, Object> checkMap =
            (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.CHECK_PDWH_PUB_ISDEL,
                RestUtils.buildPostRequestEntity(param), Object.class);
        if ("not del".equals(checkMap.get("status"))) {
          pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubMap));
          params.put("des3PdwhPubId", des3PubId);
          params.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
          params.put("needLikeStatus", true);
          params.put("needCollectStatus", true);
          Map<String, Object> statusMap = (Map<String, Object>) restTemplate
              .postForObject(domainscm + PubApiInfoConsts.OPT_STATUS_PDWH, params, Object.class);
          if (statusMap != null) {
            pubDetailVO.setIsAward((boolean) statusMap.get("hasAward") ? 1 : 0);
            pubDetailVO.setIsCollection((boolean) statusMap.get("hasCollected") ? 1 : 0);
          }
          view.setViewName("pub/pubdetails/mobile_pdwh_pub_details_main");
        }
      }
      // 判断是否登录
      if (psnId != null && psnId > 0L) {
        pubDetailVO.setIsLogin(true);
        pubDetailVO.setPsnId(psnId);
      } else {
        pubDetailVO.setIsLogin(false);
      }
      view.addObject("pubOperateVO", pubOperateVO);
      view.addObject("pubDetailVO", pubDetailVO);
    } catch (Exception e) {
      logger.error("基准库库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    return view;
  }

  /**
   * 基准库成果查看，不带评论操作等.
   *
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/details/pdwh/simple")
  public ModelAndView viewPdwhSimple(String des3PubId) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    if (StringUtils.isNotBlank(des3PubId)) {
      params.put("serviceType", "pdwhPub");
      params.put(V8pubConst.DES3_PUB_ID, des3PubId);
      try {
        PubDetailVO pubDetailVO =
            restTemplate.postForObject(domainscm + "/pubdata/query/pubdetail", params, PubDetailVO.class);
        view.addObject("pubDetailVO", pubDetailVO);
      } catch (Exception e) {
        logger.error("mobile基准库成果详情simple页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(des3PubId), e);
      }
      view.setViewName("pub/pubdetails/mobile_test");

    } else {
      view.setViewName("pub/pubdetails/mobile_test");

    }
    return view;
  }

  /**
   * 全文认领，单个全文认领信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/details/pubfulltext")
  public ModelAndView queryRcmdPubFulltextInfo(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    Long operatePsnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> map = new HashMap<String, Object>();
    List<PubFulltextPsnRcmdVO> list = new ArrayList<PubFulltextPsnRcmdVO>();
    try {
      // TODO 校验操作者和成果拥有者是否一致
      pubOperateVO.setPsnId(operatePsnId);
      if (operatePsnId != null && operatePsnId > 0L) {
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_DETAILS, pubOperateVO, Map.class);
        if ("success".equals(map.get("status"))) {
          Object listObj = map.get("list");
          if (listObj != null) {
            list = (List<PubFulltextPsnRcmdVO>) listObj;
          }
        }
      } else {
        map.put("status", "error");
        map.put("errMsg", "operatePsnId is null");
      }
      view.addObject("wechatVO", buildWeChatData(
          this.getRequestDomain() + "/pub/details/pubfulltext" + this.handleRequestParams(this.getRequest())));
    } catch (Exception e) {
      logger.error("个人主页-成果模块-全文认领-出错,psnId= " + operatePsnId, e);
      map.put("status", "error");
    }
    view.addObject("fulltextList", list);
    view.addObject("model", pubOperateVO);
    view.setViewName("pub/pubdetails/mobile_pubfulltext_details");
    return view;
  }

  /**
   * 全文认领，单个全文认领信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/details/ajaxpubfulltext")
  public ModelAndView queryRcmdPubFulltextInfoSub(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    Long operatePsnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> map = new HashMap<String, Object>();
    List<PubFulltextPsnRcmdVO> list = new ArrayList<PubFulltextPsnRcmdVO>();
    try {
      // TODO 校验操作者和成果拥有者是否一致
      pubOperateVO.setPsnId(operatePsnId);
      if (operatePsnId != null && operatePsnId > 0L) {
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_DETAILS, pubOperateVO, Map.class);
        if ("success".equals(map.get("status"))) {
          Object listObj = map.get("list");
          if (listObj != null) {
            list = (List<PubFulltextPsnRcmdVO>) listObj;
          }
        }
      } else {
        map.put("status", "error");
        map.put("errMsg", "operatePsnId is null");
      }
    } catch (Exception e) {
      logger.error("个人主页-成果模块-全文认领-出错,psnId= " + operatePsnId, e);
      map.put("status", "error");
    }
    view.addObject("fulltextList", list);
    view.addObject("model", pubOperateVO);
    view.setViewName("pub/pubdetails/mobile_pubfulltext_details_sub");
    return view;
  }

  /**
   * 移动端检查成果是否隐私
   * 
   * @param des3PubId
   */
  @RequestMapping(value = "/pub/details/ajaxgetpubanyuser", produces = "application/json;charset=UTF-8",
      method = {RequestMethod.POST})
  @ResponseBody
  public String checkPubIsAnyUser(String des3PubId, String des3GrpId, HttpServletResponse response) {
    Map<String, Object> results = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (checkParam(psnId, des3PubId)) {
      try {

        Map<String, Object> params = new HashMap<>();

        HashMap<String, Object> resultData = new HashMap<>();
        params.put(V8pubConst.DES3_PUB_ID, des3PubId);
        params.put("des3GrpId", des3GrpId);
        params.put("serviceType", "snsPub");
        params.put("psnId", psnId);
        resultData = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS,
            params, Map.class);
        if (resultData != null) {
          HashMap<String, Object> result = (HashMap<String, Object>) resultData.get("result");
          results.put("isSelf", result.get("isOwner"));
          results.put("isAnyUser", (Integer) result.get("permission"));// 7为公开,4为隐私
          results.put("status", IOSHttpStatus.OK);
          results.put("result", "success");
          results.put("hasDeleted", "hasDeleted".equals(resultData.get("status")));
        }
      } catch (Exception e) {
        logger.info("检查成果是否隐私出错,des3PubId={}", des3PubId, e);
        results.put("status", IOSHttpStatus.INTERNAL_SERVER_ERROR);
        results.put("result", "error");
      }
    } else {
      results.put("status", IOSHttpStatus.BAD_REQUEST);
      results.put("result", "param is null");
    }
    return JacksonUtils.mapToJsonStr(results);
  }

  /**
   * 参数校验
   * 
   * @param psnId
   * @param des3PubId
   * @return
   */
  public boolean checkParam(Long psnId, String des3PubId) {
    if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNotEmpty(des3PubId)) {
      return true;
    }
    return false;
  }
}
