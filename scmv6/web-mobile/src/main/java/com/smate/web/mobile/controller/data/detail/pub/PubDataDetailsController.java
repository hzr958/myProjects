package com.smate.web.mobile.controller.data.detail.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @ClassName PubDetailsController
 * @Description 移动端成果详情数据接口
 * @Author LIJUN
 * @Date 2018/8/14
 * @Version v1.0
 */
@RestController("mPubDataDetailsController")
public class PubDataDetailsController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * @return org.springframework.web.servlet.ModelAndView
   * @Author LIJUN
   * @Description 个人库成果详情页面
   * @Date 15:53 2018/8/14
   * @Param [pubOperateVO]
   **/
  @RequestMapping(value = "/data/pub/details/sns", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "snsPub");
      params.put("psnId", psnId);
      result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL_SNS, params,
          Map.class);
    } catch (Exception e) {
      result.put("status", "error");
      result.put("errmsg", "调用远程数据接口异常");
      logger.error("mobile个人库成果详情页面获取数据出错,pubId=" + Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()), e);
    }
    return JacksonUtils.jsonObjectSerializerNoNull(result);
  }

  /**
   * 基准库成果详情页面{"pubId":"21000000021"}
   *
   * @return
   */
  @RequestMapping(value = "/data/pub/details/pdwh", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubPdwhDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    HashMap<String, Object> map = new HashMap<>();
    HashMap<String, Object> result = new HashMap<>();
    if (psnId != null && pubId != null && psnId > 0L && pubId > 0L) {
      try {
        params.put(V8pubConst.DES3_PUB_ID, pubOperateVO.getDes3PubId());
        params.put("serviceType", "pdwhPub");
        result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_DETAIL, params,
            Object.class);
        map.put("result", result);
        map.put("status", "success");
      } catch (Exception e) {
        map.put("status", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("基准库库成果详情页面获取数据出错,pubId=" + pubId, e);
      }
    } else {
      map.put("status", "error");
      map.put("errmsg", "参数校验不通过");
    }
    return JacksonUtils.jsonObjectSerializerNoNull(map);
  }

  /**
   * 项目详情页面{desPrjId}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/data/pub/details/prj", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object prjDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> params = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long prjId = Long.parseLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3PrjId()));
    HashMap<String, Object> result = new HashMap<>();
    if (psnId != null && prjId != null && psnId > 0L && prjId > 0L) {
      try {
        params.put(V8pubConst.DES3_PRJ_ID, pubOperateVO.getDes3PrjId());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PRJ_DETAI, params,
            Object.class);
      } catch (Exception e) {
        result.put("status", "error");
        result.put("errmsg", "调用远程数据接口异常");
        logger.error("项目详情页面获取数据出错,pubId=" + prjId, e);
      }
    } else {
      result.put("status", "error");
      result.put("errmsg", "参数校验不通过");
    }
    return JacksonUtils.jsonObjectSerializerNoNull(result);
  }

  /**
   * 基金详情页面{des3FundId}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/data/pub/details/fund", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object fundDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> params = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long fundId = Long.parseLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3FundId()));
    Object object = null;
    if (psnId != null && fundId != null && psnId > 0L && fundId > 0L) {
      try {
        params.put(V8pubConst.DES3_FUND_ID, pubOperateVO.getDes3FundId());
        object = restTemplate.postForObject(domainscm + PubApiInfoConsts.FUND_DETAI, params, Object.class);
        map.put("result", object);
        map.put("result", "success");
      } catch (Exception e) {
        map.put("result", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("基金详情页面获取数据出错,pubId=" + fundId, e);
      }
    } else {
      map.put("result", "error");
      map.put("errmsg", "参数校验不通过");
    }
    return JacksonUtils.jsonObjectSerializerNoNull(map);
  }

  /**
   * 全文认领，单个全文认领信息
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/details/pubfulltext", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object queryRcmdPubFulltextInfo(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long operatePsnId = SecurityUtils.getCurrentUserId();
    try {
      // TODO 校验操作者和成果拥有者是否一致
      pubOperateVO.setPsnId(operatePsnId);
      if (operatePsnId != null && operatePsnId > 0L) {
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_DETAILS, pubOperateVO, Map.class);
      } else {
        map.put("status", "error");
        map.put("errMsg", "operatePsnId is null");
      }
    } catch (Exception e) {
      logger.error("个人主页-成果模块-全文认领-出错,psnId= " + operatePsnId, e);
      map.put("status", "error");
    }
    return AppActionUtils.buildReturnInfo(map.get("list"), 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errMsg"), ""));
  }

  /**
   * 移动端，消息中心成果认领全文认领列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/getpubandfulltextrcmd", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getPubAndFullTextRcmd(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (NumberUtils.isNotNullOrZero(psnId)) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        // 获取全文认领数据
        params.put("psnId", psnId);
        result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_LIST,
            params, Map.class);
        String status = (String) result.get("status");
        if (result != null && V8pubConst.SUCCESS.equalsIgnoreCase(status)) {
          map.put("fullTextList", result.get("list"));
          Object fullTextTotalCount = result.get("totalCount");
          map.put("fullTextTotalCount",
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
          map.put("pubRcmdList", resultList);
          map.put("pubRcmdTotalCount", Objects.nonNull(resultList) && resultList.size() > 0 ? resultList.size() : 0);
        }
        map.put("status", "success");
      } catch (Exception e) {
        logger.error("查询全文认领和成果认领出错,psnId={}", psnId, e);
        map.put("status", "error");
        map.put("errMsg", "system error");
      }
    } else {
      map.put("status", "error");
      map.put("errMsg", "psnId is null");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errMsg"), ""));
  }
}
