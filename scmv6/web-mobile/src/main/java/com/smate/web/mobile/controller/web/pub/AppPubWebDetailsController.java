package com.smate.web.mobile.controller.web.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.dto.PubQueryDTO;
import com.smate.web.mobile.v8pub.service.PubCacheService;
import com.smate.web.mobile.v8pub.vo.pubfulltext.PubFulltextPsnRcmdVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @ClassName PubDetailsController
 * @Description 移动端成果详情
 * @Author LIJUN
 * @Date 2018/8/14
 * @Version v1.0
 */
@Controller
@RequestMapping("/data/pub/app")
public class AppPubWebDetailsController extends WeChatBaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PubCacheService pubCacheService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * @Author LIJUN
   * @Description 个人库成果详情页面
   * @Date 15:53 2018/8/14
   * @Param [pubOperateVO]
   * @return org.springframework.web.servlet.ModelAndView
   **/
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/snsnonext")
  public ModelAndView appPubNoNextDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    try {
      params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "snsPub");
      params.put("psnId", psnId);
      result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS, params,
          Map.class);
      String status = (String) result.get("status");
      object = result.get("result");
      /*
       * if ("success".equals(status)) { } else if ("no permission".equals(status)) { // TODO 没有查看权限页面 }
       */
      view.addObject("pubDetailVO", object);
      view.addObject("wechatVO", buildWeChatData(
          this.getRequestDomain() + "/pub/details/snsnonext" + this.handleRequestParams(this.getRequest())));
    } catch (Exception e) {
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    view.setViewName("pub/pubdetails/app/app_sns_pubdetail");
    return view;
  }

  /**
   * @Author LIJUN
   * @Description 个人库成果详情页面
   * @Date 15:53 2018/8/14
   * @Param [pubOperateVO]
   * @return org.springframework.web.servlet.ModelAndView
   **/
  @SuppressWarnings({"unchecked"})
  @RequestMapping(value = "/sns")
  public ModelAndView appPubDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
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
      if (StringUtils.isNoneBlank(pubOperateVO.getDes3relationid())) {
        params.put("des3relationid", pubOperateVO.getDes3relationid());
      }
      result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS, params,
          Map.class);
      if (!NumberUtils.isNullOrZero(psnId)) {
        pubOperateVO.setHasLogin(true);
      }
      String status = (String) result.get("status");
      object = result.get("result");
      if ("no permission".equals(status)) {
        view.setViewName("pub/mobile_pub_no_authority");
      } else if ("not exists".equals(status)) {
        view.setViewName("pub/mobile_pub_not_exists");
      }
      pubOperateVO.setViewStatus(status);
      view.addObject("pubOperateVO", pubOperateVO);
      view.addObject("pubDetailVO", object);
      pubCacheService.put("mPubOperateVO", 20 * 60, getSession().getId(), pubOperateVO);
      view.addObject("wechatVO",
          buildWeChatData(this.getRequestDomain() + "/pub/details/sns" + this.handleRequestParams(this.getRequest())));
    } catch (Exception e) {
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    view.setViewName("pub/pubdetails/app/app_sns_pubdetail");
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
  @RequestMapping(value = "/ajaxsns")
  public ModelAndView appPubAjaxDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    // 先查询成果id
    if (StringUtils.isBlank(pubOperateVO.getDes3PubId())) {
      List<Map<String, Object>> resultList = new ArrayList<>();
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      pubQueryDTO.setFirst(pubOperateVO.getDetailPageNo());
      pubQueryDTO.setPageSize(pubOperateVO.getDetailPageSize());
      pubQueryDTO.setServiceType("pubList");
      Map<String, Object> listobject = (Map<String, Object>) restTemplate
          .postForObject(domainscm + PubApiInfoConsts.PUB_LIST, pubQueryDTO, Object.class);
      if (listobject != null && listobject.get("status").equals(V8pubConst.SUCCESS)) {
        resultList = (List<Map<String, Object>>) listobject.get("resultList");
        if (resultList != null && resultList.size() > 0) {
          Map<String, Object> firstmap = resultList.get(0);
          if (firstmap != null) {
            pubOperateVO.setDes3PubId(firstmap.get("des3PubId").toString());
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
      view.addObject("pubDetailVO", object);
      String referer = getHttpReferer();
      if (pubOperateVO.getUseoldform() && StringUtils.isNotBlank(referer)) {
        view.addObject("wechatVO", buildWeChatData(referer));
      } else {
        view.addObject("wechatVO", buildWeChatData(
            this.getRequestDomain() + "/pub/details/sns" + this.handleRequestParams(this.getRequest())));
      }
    } catch (Exception e) {
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    view.setViewName("pub/pubdetails/app/app_sns_pubdetail");
    return view;
  }

  /**
   * 个人成果查看，不带评论操作等.
   *
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/sns/simple")
  public ModelAndView appViewSimple(String des3PubId) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    if (StringUtils.isNotBlank(des3PubId)) {
      params.put("serviceType", "snsPub");
      params.put(V8pubConst.DES3_PUB_ID, des3PubId);
      try {
        PubDetailVO pubDetailVO =
            restTemplate.postForObject(domainscm + "/pubdata/query/pubdetail", params, PubDetailVO.class);
        view.addObject("pubDetailVO", pubDetailVO);
        view.addObject("wechatVO", buildWeChatData(
            this.getRequestDomain() + "/pub/details/sns/simple" + this.handleRequestParams(this.getRequest())));
      } catch (Exception e) {
        logger.error("mobile个人库成果详情simple页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(des3PubId), e);
      }
      /* view.setViewName("pub/pubdetails/pub_details_simple_main"); */
      view.setViewName("pub/pubdetails/app/app_sns_pubdetail");

    } else {
      /* view.setViewName("pub/pubdetails/pubNotExit"); */
      view.setViewName("pub/pubdetails/app/app_sns_pubdetail");
    }
    return view;
  }

  /**
   * 基准库成果详情页面{"pubId":"21000000021"}
   *
   * @return
   */
  @RequestMapping(value = "/pdwh")
  public ModelAndView appPubPdwhDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO();
    params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
    params.put("serviceType", "pdwhPub");
    try {
      Map<String, Object> pubMap = (Map<String, Object>) restTemplate
          .postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL, params, Object.class);
      if (pubMap != null) {
        pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubMap));
        // 判断是否登录
        Long psnId = SecurityUtils.getCurrentUserId();
        if (psnId != null && psnId > 0L) {
          pubDetailVO.setIsLogin(true);
          pubDetailVO.setPsnId(psnId);
        }
        params.put("des3PdwhPubId", pubOperateVO.getDes3PubId());
        params.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.put("needLikeStatus", true);
        params.put("needCollectStatus", true);
        Map<String, Object> statusMap = (Map<String, Object>) restTemplate
            .postForObject(domainscm + PubApiInfoConsts.OPT_STATUS_PDWH, params, Object.class);
        if (statusMap != null) {
          pubDetailVO.setIsAward((boolean) statusMap.get("hasAward") ? 1 : 0);
          pubDetailVO.setIsCollection((boolean) statusMap.get("hasCollected") ? 1 : 0);
        }
      }
      view.addObject("pubDetailVO", pubDetailVO);
      view.addObject("wechatVO",
          buildWeChatData(this.getRequestDomain() + "/pub/details/pdwh" + this.handleRequestParams(this.getRequest())));
    } catch (Exception e) {
      logger.error("基准库库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    view.setViewName("pub/pubdetails/app/app_pdwh_pub_details");
    return view;
  }

  /**
   * 站外基准库成果详情页面
   *
   * @return
   */
  @RequestMapping(value = "/pdwh/outside")
  public ModelAndView appPubPdwhOutsideDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO<>();
    params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
    params.put("serviceType", "pdwhPub");
    try {
      // 判断是否登录
      Long psnId = SecurityUtils.getCurrentUserId();
      pubDetailVO = restTemplate.postForObject(domainscm + "/pubdata/query/pubdetail", params, PubDetailVO.class);
      view.addObject("pubDetailVO", pubDetailVO);
      view.addObject("wechatVO", buildWeChatData(
          this.getRequestDomain() + "/pub/details/pdwh/outside" + this.handleRequestParams(this.getRequest())));
    } catch (Exception e) {
      logger.error("mobile站外基准库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    view.setViewName("pub/pubdetails/app/app_pdwh_pub_details");
    return view;
  }

  /**
   * 基准库成果查看，不带评论操作等.
   *
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pdwh/simple")
  public ModelAndView appViewPdwhSimple(String des3PubId) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    if (StringUtils.isNotBlank(des3PubId)) {
      params.put("serviceType", "pdwhPub");
      params.put(V8pubConst.DES3_PUB_ID, des3PubId);
      try {
        PubDetailVO pubDetailVO =
            restTemplate.postForObject(domainscm + "/pubdata/query/pubdetail", params, PubDetailVO.class);
        view.addObject("pubDetailVO", pubDetailVO);
        view.addObject("wechatVO", buildWeChatData(
            this.getRequestDomain() + "/pub/details/pdwh/simple" + this.handleRequestParams(this.getRequest())));
      } catch (Exception e) {
        logger.error("mobile基准库成果详情simple页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(des3PubId), e);

      }
      /* view.setViewName("pub/pubdetails/pub_details_simple_main"); */
      view.setViewName("pub/pubdetails/app/app_pdwh_pub_details");

    } else {
      /* view.setViewName("pub/pubdetails/pubNotExit"); */
      view.setViewName("pub/pubdetails/app/app_pdwh_pub_details");

    }
    return view;
  }

  /**
   * 全文认领，单个全文认领信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pubfulltext")
  public ModelAndView appQueryRcmdPubFulltextInfo(PubOperateVO pubOperateVO) {
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
  @RequestMapping(value = "/ajaxpubfulltext")
  public ModelAndView appQueryRcmdPubFulltextInfoSub(PubOperateVO pubOperateVO) {
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
}
