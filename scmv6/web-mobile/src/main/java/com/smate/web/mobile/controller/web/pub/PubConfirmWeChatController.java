package com.smate.web.mobile.controller.web.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.PubListVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * 移动端成果认领控制器
 * 
 * @author aijiangbin
 * @date 2018年9月4日
 */
@Controller()
public class PubConfirmWeChatController extends WeChatBaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private HttpServletRequest request;

  @RequestMapping(value = "/pub/wechat/ajaxmsgtips", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getTips() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && psnId != 0L) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      Map<String, Object> object =
          (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_CONFIRM_COUNT,
              JacksonUtils.mapToJsonStr(paramMap), Object.class);
      Map<String, Object> object2 =
          (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_CONFIRM_COUNT,
              JacksonUtils.mapToJsonStr(paramMap), Object.class);
      map.put("pubFulltextCount", object.get("pubFulltextCount"));
      map.put("pubConfirmCount", object2.get("pubConfirmCount"));
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 加载出成果认领和全文认领的数据,在页面上控制显示
   * 
   * @return
   */
  @RequestMapping(value = "/pub/wechat/ajaxpubfulltextshow")
  public ModelAndView ajaxPubFullTextShow(String whoFirst) {
    ModelAndView modelAndView = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (NumberUtils.isNotNullOrZero(psnId)) {
      try {
        /**
         * 构建微信端需要的数据,暂不知为何需要构建
         */
        buildWeChatData("/pub/wechat/ajaxpubfulltextshow", request);
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        // 获取全文认领数据
        params.put("psnId", psnId);
        result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_LIST,
            params, Map.class);
        String status = (String) result.get("status");
        if (result != null && V8pubConst.SUCCESS.equalsIgnoreCase(status)) {
          modelAndView.addObject("fullTextList", result.get("list"));
          Object fullTextTotalCount = result.get("totalCount");
          modelAndView.addObject("fullTextTotalCount",
              Objects.nonNull(fullTextTotalCount) ? NumberUtils.toLong(fullTextTotalCount.toString()) : 0);
        }
        // 获取成果认领数据
        params.remove("psnId");
        params.put("des3SearchPsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
        params.put("serviceType", "pubConfirmList");
        params.put("orderBy", "pubId");
        params.put("isAll", 1);
        params.put("isPage", 1);
        String confirmUrl = domainscm + PubApiInfoConsts.PUB_LIST;
        Map<String, Object> object = (Map<String, Object>) restTemplate.postForObject(confirmUrl, params, Object.class);
        if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
          String dataJson = JacksonUtils.jsonObjectSerializerNoNull(object.get("resultList"));
          List<PubInfo> resultList = JacksonUtils.jsonToCollection(dataJson, List.class, PubInfo.class);
          modelAndView.addObject("pubRcmdList", resultList);
          modelAndView.addObject("pubRcmdTotalCount",
              Objects.nonNull(resultList) && resultList.size() > 0 ? resultList.size() : 0);
        }
        if (!modelAndView.getModel().containsKey("pubRcmdTotalCount")) {
          // 当没有成果认领数据时,会导致pubRcmdTotalCount为空,导致前台显示不正确
          modelAndView.addObject("pubRcmdTotalCount", 0);
        }

        modelAndView.addObject("des3PsnId", Des3Utils.encodeToDes3(psnId == null ? "0" : psnId.toString()));
        modelAndView.addObject("whoFirst", whoFirst);
        modelAndView.setViewName("pub/fulltext/fulltext_pub_rcmd");
        modelAndView.addObject("status", "success");
        modelAndView.addObject("msg", "response success");
      } catch (Exception e) {
        logger.error("查询全文认领和成果认领出错,psnId={}", psnId, e);
        modelAndView.addObject("status", "error");
        modelAndView.addObject("msg", "system error");
      }
    } else {
      modelAndView.addObject("status", "error");
      modelAndView.addObject("msg", "psnId is null");
    }
    return modelAndView;
  }

  /**
   * 成果全文列表
   * 
   * @param pubOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/wechat/pubfulltextlist")
  public ModelAndView pubFulltextList(PubOperateVO pubOperateVO) {

    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    buildWeChatData("/pub/wechat/pubfulltextlist", request);
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    Long totalCount = 0L;
    try {
      if (psnId != null && psnId != 0L) {
        params.put("psnId", psnId);
        params.put("pageSize", 1000);
        result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_LIST,
            params, Map.class);
        String status = (String) result.get("status");
        if ("success".equalsIgnoreCase(status)) {
          object = result.get("list");
          totalCount = NumberUtils.toLong(result.get("totalCount").toString());
        }
      }
      view.addObject("pubRcmdftList", object);
      view.addObject("totalCount", totalCount);
      view.addObject("pubOperateVO", pubOperateVO);
      view.addObject("des3PsnId", Des3Utils.encodeToDes3(psnId == null ? "0" : psnId.toString()));
    } catch (Exception e) {
      logger.error("mobile个人库全文列表异常,psnId=" + psnId, e);
    }
    view.setViewName("pub/fulltext/mobile_pubfulltext");
    return view;
  }

  @RequestMapping(value = "/pub/wechat/ajaxconfirmpubft", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxconfirmpubft(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && psnId != 0L) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      Map<String, Object> object =
          (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_CONFIRM_COUNT,
              JacksonUtils.mapToJsonStr(paramMap), Object.class);
      Map<String, Object> object2 =
          (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_CONFIRM_COUNT,
              JacksonUtils.mapToJsonStr(paramMap), Object.class);
      map.put("pubFulltextCount", object.get("pubFulltextCount"));
      map.put("pubConfirmCount", object2.get("pubConfirmCount"));
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/confirmpublist")
  public ModelAndView pubConfirmList(PubOperateVO pubOperateVO) {
    ModelAndView modelAndView = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (NumberUtils.isNullOrZero(psnId)) {
      modelAndView.addObject("msg", "用户未登录");
      return modelAndView;
    }
    pubListVO.setHasLogin(1);
    pubListVO.setFromPage(pubOperateVO.getToBack());
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("des3SearchPsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
      params.put("serviceType", "pubConfirmList");
      params.put("orderBy", "pubId");
      params.put("isAll", 1);
      params.put("isPage", 1);
      String confirmUrl = domainscm + PubApiInfoConsts.PUB_LIST;
      Map<String, Object> object = (Map<String, Object>) restTemplate.postForObject(confirmUrl, params, Object.class);
      if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
        String dataJson = JacksonUtils.jsonObjectSerializerNoNull(object.get("resultList"));
        List<PubInfo> resultList = JacksonUtils.jsonToCollection(dataJson, List.class, PubInfo.class);
        pubListVO.setResultList(resultList);
      }
      pubListVO.setOther(false);
      modelAndView.addObject("pubListVO", pubListVO);
    } catch (Exception e) {
      logger.error("获取成果认领列表出错！", e);
    }
    modelAndView.setViewName("/pub/publist/mobile_confirm_pub");
    return modelAndView;
  }

  /**
   * 确认成果认领
   * 
   * @param pubId
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxAffirmPubComfirm", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxAffirmPubComfirm(@ModelAttribute("pubId") String pubId) {
    String SERVER_URL = domainscm + "/data/pub/affirmconfirm";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long pdwhPubId = Long.valueOf(pubId);
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 调用接口进行成果认领业务
        // 设置请求头部
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 构建成果保存对象
        Map<String, Object> data = new HashMap<>();
        data.put("des3PubId", Des3Utils.encodeToDes3(pubId));
        data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(SecurityUtils.getCurrentUserId())));
        HttpEntity<String> requestEntity =
            new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
        Map<String, Object> res =
            (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
        if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
          map.put("result", "success");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 忽略成果认领
   * 
   * @param pubId
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxIgnorePubComfirm", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxIgnorePubComfirm(@ModelAttribute("pubId") String pubId) {
    String SERVER_URL = domainscm + "/data/pub/ignoreconfirm";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long pdwhPubId = Long.valueOf(pubId);
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 调用接口进行成果认领业务
        // 设置请求头部
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 构建成果保存对象
        Map<String, Object> data = new HashMap<>();
        data.put("des3PubId", Des3Utils.encodeToDes3(pubId));
        data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(SecurityUtils.getCurrentUserId())));
        HttpEntity<String> requestEntity =
            new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
        Map<String, Object> res =
            (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
        if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
          map.put("result", "success");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 确认全文认领 是这篇成果的全文
   * 
   * @param ids PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
   * @param des3PsnId 当前操作者ID
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxConfirmPubft", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object confirmPub(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    String ids = pubOperateVO.getIds();
    try {
      pubOperateVO.setDes3PsnId(Des3Utils.encodeToDes3(currentPsnId.toString()));
      if (StringUtils.isNotBlank(ids) && currentPsnId != null && currentPsnId > 0L) {
        List<Long> idList = new ArrayList<Long>();
        idList = ServiceUtil.splitStrToLong(ids);
        if (!CollectionUtils.isEmpty(idList)) {
          for (Long id : idList) {
            // TODO 判断操作者和全文推荐的成果拥有者是否是同一个人
            map =
                restTemplate.postForObject(domainscm + PubApiInfoConsts.CONFIRM_PUB_FULLTEXT, pubOperateVO, Map.class);
          }
        }
      } else {
        map.put("status", "error");
        map.put("errMsg", "ids or psnId is null");
      }
    } catch (Exception e) {
      logger.error("移动端，全文认领操作，是这篇成果的全文操作出错，ids =" + ids + ", psnId = " + currentPsnId, e);
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 确认全文认领 不是这篇成果的全文
   * 
   * @param ids PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
   * @param des3PsnId 当前操作者ID
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxRejectPubft", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object rejectPubFulltext(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    String ids = pubOperateVO.getIds();
    try {
      pubOperateVO.setPsnId(currentPsnId);
      if (StringUtils.isNotBlank(ids) && currentPsnId != null && currentPsnId > 0L) {
        List<Long> idList = new ArrayList<Long>();
        idList = ServiceUtil.splitStrToLong(ids);
        if (!CollectionUtils.isEmpty(idList)) {
          for (Long id : idList) {
            // TODO 判断操作者和全文推荐的成果拥有者是否是同一个人
            map = restTemplate.postForObject(domainscm + PubApiInfoConsts.REJECT_PUB_FULLTEXT, pubOperateVO, Map.class);
          }
        }
      } else {
        map.put("status", "error");
        map.put("errMsg", "ids or psnId is null");
      }
    } catch (Exception e) {
      logger.error("移动端，全文认领操作，不是这篇成果的全文操作出错，ids =" + ids + ", psnId = " + currentPsnId, e);
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 移动端-放大图片
   * 
   * @return
   */
  @RequestMapping("/pub/mobile/pubfulltextimg")
  public ModelAndView pubFulltextImg(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    view.addObject("model", pubOperateVO);
    view.setViewName("pub/fulltext/mobile_pubfulltext_pic");
    return view;
  }

}
