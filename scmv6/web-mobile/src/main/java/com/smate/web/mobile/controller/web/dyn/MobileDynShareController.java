package com.smate.web.mobile.controller.web.dyn;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.mobile.dyn.vo.DynamicForm;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.enums.MobileResShareEnums;

/**
 * @description 移动端动态分享控制器
 * @author xiexing
 * @date 2019年4月3日
 */
@Controller
public class MobileDynShareController {
  private static final Logger logger = LoggerFactory.getLogger(MobileDynShareController.class);

  @Value("${domainscm}")
  private String domainScm;

  @Autowired
  private RestTemplate restTemplate;


  /**
   * 移动端动态分享数据跳转页
   * 
   * @return
   */
  @RequestMapping(value = "/dyn/mobile/middynshare")
  public ModelAndView midDynShare(DynamicForm form) {
    ModelAndView modelAndView = new ModelAndView();
    Long dynId = form.getDynId();
    if (NumberUtils.isNotNullOrZero(dynId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("dynId", String.valueOf(dynId));
        Map<String, Object> result = restTemplate.postForObject(domainScm + "/dyndata/dynamic/getshareinfo",
            RestUtils.buildPostRequestEntity(params), Map.class);
        form.setDes3DynId(Des3Utils.encodeToDes3(String.valueOf(dynId)));
        Long resId = NumberUtils.toLong(String.valueOf(result.get("resId")));
        form.setDes3ResId(Des3Utils.encodeToDes3(String.valueOf(resId)));
        form.setTitle(deleteBlankLine(form.getTitle()));// 防止特殊字符分享出错的问题
        form.setDynText(deleteBlankLine(form.getDynText()));
        modelAndView.addObject("form", form);
        DynamicMsg dynamicMsg = new DynamicMsg(String.valueOf(result.get("dynType")), resId,
            NumberUtils.toInt(String.valueOf(result.get("resType"))));
        modelAndView.addObject("dynamicMsg", dynamicMsg);
      } catch (Exception e) {
        logger.error("获取动态相关信息出错,dynId={}", dynId, e);
      }
    }
    // 异常/资源不存在/正常返回都返回到页面上,在页面上控制响应结果
    modelAndView.setViewName("dyn/dyn_main_share");
    return modelAndView;
  }

  /**
   * 删除字符串空行,并且删除首尾空格
   * 
   * @param target
   * @return
   */
  public String deleteBlankLine(String target) {
    if (StringUtils.isEmpty(target)) {
      return "";
    }
    return target.replaceAll("((\r\n)|\n)", "").trim();
  }

  /**
   * 移动端项目/个人成果/基准库成果/资助机构/基金分享统一入口
   * 
   * @param des3ResId
   * @param shareType
   * @param queryStr 基准库回显使用
   * @return
   */
  @RequestMapping(value = "/dyn/mobile/resshare")
  public ModelAndView mobileResShare(String des3ResId, String shareType, String des3GrpId, String queryStr) {
    ModelAndView modelAndView = new ModelAndView();
    if (doVerify(des3ResId, shareType)) {
      try {
        // 七种类型统一按此顺序存放,页面上按照此顺序显示
        String first = "";// title
        String second = "";// authorNames
        String third = "";// briefDesc
        String fourth = "";// hasFullText
        String fifth = "";// thumbnailPath
        String sixth = "";// publishYear
        String seventh = "";// enShowDesc
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        // 获取项目详情
        if (MobileResShareEnums.PRJ.getValue().equalsIgnoreCase(shareType)
            || MobileResShareEnums.PRJDETAIL.getValue().equals(shareType)) {
          params.add("des3PrjId", des3ResId);
          Map<String, Object> result = getReqResult(params, "/prjdata/project/getdetails");
          if (Objects.nonNull(result)) {
            first = result.get("title") != null ? result.get("title").toString() : "";
            second = result.get("authorNames") != null ? result.get("authorNames").toString() : "";
            third = result.get("briefDesc") != null ? HtmlUtils.htmlUnescape(result.get("briefDesc").toString()) : "";
          }
        }
        // 获取个人库/基准库成果详情
        if (MobileResShareEnums.SNS.getValue().equalsIgnoreCase(shareType)
            || MobileResShareEnums.PDWH.getValue().equalsIgnoreCase(shareType)
            || MobileResShareEnums.SNSDETAIL.getValue().equals(shareType)
            || MobileResShareEnums.PDWHDETAIL.getValue().equals(shareType)) {
          params.add("des3PubId", des3ResId);
          params.add("type", "sns");
          if (MobileResShareEnums.PDWH.getValue().equalsIgnoreCase(shareType)
              || MobileResShareEnums.PDWHDETAIL.getValue().equals(shareType)) {
            params.remove("type");
            params.add("type", "pdwh");
          }
          Map<String, Object> result = getReqResult(params, "/data/pub/details/forshare");
          if (Objects.nonNull(result)) {
            first = result.get("title") != null ? result.get("title").toString() : "";
            second = result.get("authorNames") != null ? result.get("authorNames").toString() : "";
            third = result.get("briefDesc") != null ? HtmlUtils.htmlUnescape(result.get("briefDesc").toString()) : "";
            fourth = result.get("hasFullText") != null ? result.get("hasFullText").toString() : "";
            fifth = result.get("thumbnailPath") != null ? result.get("thumbnailPath").toString() : "";
            sixth = result.get("publishYear") != null ? result.get("publishYear").toString() : "";
          }
        }
        // 获取基金详情
        if (MobileResShareEnums.FUND.getValue().equalsIgnoreCase(shareType)
            || MobileResShareEnums.FUNDDETAIL.getValue().equals(shareType)
            || MobileResShareEnums.AIDINSDETAIL.getValue().equals(shareType)) {
          params.add("des3FundId", des3ResId);
          Map<String, Object> result = getReqResult(params, "/prjdata/funddetails/forshare");
          first = result.get("zhTitle") != null ? result.get("zhTitle").toString() : "";
          second = result.get("agencyName") != null ? result.get("agencyName").toString() : "";
          third = result.get("timer") != null ? result.get("timer").toString() : "";
          fourth = result.get("logo") != null ? result.get("logo").toString() : "";
          fifth = result.get("enTitle") != null ? result.get("enTitle").toString() : "";
          sixth = result.get("zhShowDesc") != null ? result.get("zhShowDesc").toString() : "";
          seventh = result.get("enShowDesc") != null ? result.get("enShowDesc").toString() : "";
        }
        // 获取资助机构详情
        if (MobileResShareEnums.AIDINS.getValue().equalsIgnoreCase(shareType)) {
          params.add("Des3FundAgencyId", des3ResId);
          Map<String, Object> result = getReqResult(params, "/prjdata/aidinsdetail/forshare");
          first = result.get("zhName") != null ? result.get("zhName").toString() : "";
          second = result.get("address") != null ? result.get("address").toString() : "";
          third = result.get("logo") != null ? result.get("logo").toString() : "";
        }
        // 新闻
        if (MobileResShareEnums.NEWS.getValue().equalsIgnoreCase(shareType)) {
          params.add("des3NewsId", des3ResId);
          Map<String, Object> result = getReqResult(params, "/dynweb/news/mobile/forshare");
          first = result.get("title") != null ? result.get("title").toString() : "";
          second = result.get("brief") != null ? result.get("brief").toString() : "";
          third = result.get("image") != null ? result.get("image").toString() : "";
        }
        modelAndView.addObject("first", HtmlUtils.htmlUnescape(first));
        modelAndView.addObject("second", HtmlUtils.htmlUnescape(second));
        modelAndView.addObject("third", third);
        modelAndView.addObject("fourth", fourth);
        modelAndView.addObject("fifth", fifth);
        modelAndView.addObject("sixth", sixth);
        modelAndView.addObject("seventh", seventh);
        modelAndView.addObject("des3ResId", des3ResId);
        modelAndView.addObject("resId", Des3Utils.decodeFromDes3(des3ResId));
        modelAndView.addObject("shareType", shareType);
        modelAndView.addObject("queryStr", queryStr);
        modelAndView.addObject("status", "success");
        modelAndView.addObject("msg", "get data success");
      } catch (Exception e) {
        logger.error("分享资源出错,des3ResId={},shareType={}", des3ResId, shareType, e);
        modelAndView.addObject("status", "error");
        modelAndView.addObject("msg", "system error");
      }
    } else {
      modelAndView.addObject("status", "error");
      modelAndView.addObject("msg", "param verify fail");
    }
    modelAndView.setViewName("resshare/mobile_res_share");
    return modelAndView;
  }

  /**
   * 参数校验
   * 
   * @param des3ResId
   * @param shareType
   * @return
   */
  public boolean doVerify(String des3ResId, String shareType) {
    if (StringUtils.isNotEmpty(des3ResId) && StringUtils.isNotEmpty(shareType)) {
      return true;
    }
    return false;
  }

  /**
   * 发送请求并返回结果
   * 
   * @param params
   * @param restUrl
   * @return
   */
  public Map<String, Object> getReqResult(MultiValueMap<String, String> params, String restUrl) {
    if (params.size() <= 0 || StringUtils.isEmpty(restUrl)) {
      return null;
    }
    Map<String, Object> result =
        restTemplate.postForObject(domainScm + restUrl, RestUtils.buildPostRequestEntity(params), Map.class);
    return result;
  }

  /**
   * 截取多于字符串,修改为利用样式进行截取显示
   * 
   * @param target
   * @param type
   * @return
   */
  // public String subUnnecessaryStr(String target, String type) {
  // if (StringUtils.isEmpty(target) && StringUtils.isEmpty(type)) {
  // return "";
  // }
  // target = target.trim();
  // if (target.length() > 18) {
  // if ("title".equals(type) && target.length() > 18) {
  // target = target.substring(0, 18).trim() + "...";
  // } else if ("names".equals(type)) {
  // // 说明已经第十八位已经是;结尾
  // if (";".equals(String.valueOf(target.charAt(17)))) {
  // target = target.substring(0, 18).trim() + "...";
  // } else {
  // // 防止名称被截断的问题
  // String one = target.substring(0, 18).trim();// 前半段
  // String two = target.substring(18, target.length());// 后半段
  // int index = two.indexOf(";");
  // if (index != -1) {
  // target = one + two.substring(0, two.indexOf(";")) + "...";
  // } else {
  // target = one + "...";
  // }
  // }
  // }
  // return target;
  // }
  // return target;
  // }
}
