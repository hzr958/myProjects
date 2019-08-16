package com.smate.web.mobile.controller.web.psn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.smate.core.base.utils.common.BeanUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.mobile.psn.vo.PsnOperateVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PsnApiConsts;
import com.smate.web.mobile.v8pub.service.MobilePubQueryService;

/**
 * @ClassName PersonHompageEditController
 * @Description 个人主页工作经历、教育经历、个人简介、个人头衔编辑
 * @Author YWL
 * @Date 2019/2/18
 * @Version v1.0
 */

@Controller
public class PersonHompageEditController {
  private static final String WorkHistory = null;
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private MobilePubQueryService mobilePubQueryService;

  /**
   * 工作经历编辑页面
   */
  @RequestMapping(value = "/psn/mobile/edit/workhistory", produces = "application/json;charset=UTF-8")
  public ModelAndView editWorkexperience(PsnOperateVO psnOperateVO) {
    ModelAndView model = new ModelAndView();
    try {
      if (!"add".equals(psnOperateVO.getType())) {
        if (NumberUtils.isNotNullOrZero(psnOperateVO.getWorkId())) {
          MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
          params.add("psnId", SecurityUtils.getCurrentUserId().toString());
          params.add("workId", psnOperateVO.getWorkId().toString());
          HashMap<String, Object> resultMap = new HashMap<>();
          resultMap =
              (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSN_WORDHISTORY,
                  RestUtils.buildPostRequestEntity(params), Map.class);
          if ("success".equals(resultMap.get("status"))) {
            String data = JacksonUtils.jsonObjectSerializerNoNull(resultMap.get("result"));
            Map<String, String> workHistoryMap = JacksonUtils.jsonToMap(data);
            BeanUtils.copyProperties(psnOperateVO, workHistoryMap);
          }
        }
      }
    } catch (Exception e) {
      logger.error("工作经历编辑页面异常,psnId=" + SecurityUtils.getCurrentUserId(), e);
    }
    DateFormat df = new SimpleDateFormat("yyyy-MM");
    psnOperateVO.setNowTime(df.format(new Date()).toString());
    model.addObject("psnOperateVO", psnOperateVO);
    model.setViewName("/psn/personhomepage/workhistory_edit");
    return model;
  }

  /**
   * 个人主页--获取自动填充的机构名称
   */
  @RequestMapping(value = "/psn/mobile/edit/ajaxautoinstitution", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAutoInstitution(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    String insName = psnOperateVO.getInsName();
    if (StringUtils.isNoneBlank(insName) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("searchKey", insName);
        params.add("locale", "zh_CN");
        String acInstitutionListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_INSTITUTION,
            RestUtils.buildPostRequestEntity(params), String.class);
        resultMap.put("result", JacksonUtils.jsonToList(acInstitutionListStr));
      } catch (Exception e) {
        logger.error("获取自动填充的机构名称异常,psnId=" + psnId, e);
      }
    }
    return resultMap;
  }

  /**
   * 个人主页-获取自动填充的部门(学院)
   */
  @RequestMapping(value = "/psn/mobile/edit/ajaxautoinsunit", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAutoInsUnit(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    String insName = psnOperateVO.getInsName();
    if (StringUtils.isNoneBlank(insName) && NumberUtils.isNotNullOrZero(psnId)
        && StringUtils.isNoneBlank(psnOperateVO.getSearchKey())) {
      try {
        params.add("psnId", psnId.toString());
        params.add("insName", insName);
        params.add("searchKey", psnOperateVO.getSearchKey());
        params.add("locale", "zh_CN");
        String acInsunitListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_INSUNIT,
            RestUtils.buildPostRequestEntity(params), String.class);
        resultMap.put("result", JacksonUtils.jsonToList(acInsunitListStr));
      } catch (Exception e) {
        logger.error("获取自动填充的部门(学院)异常,psnId=" + psnId, e);
      }
    }
    return resultMap;
  }

  /**
   * 个人主页-获取自动填充的学历
   */
  @RequestMapping(value = "/psn/mobile/edit/ajaxautodegree", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAutoDegree(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    String searchKey = psnOperateVO.getDegreeName();
    if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(searchKey)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("searchKey", searchKey);
        params.add("locale", "zh_CN");
        String acDegreeListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_DEGREE,
            RestUtils.buildPostRequestEntity(params), String.class);
        resultMap.put("result", JacksonUtils.jsonToList(acDegreeListStr));
      } catch (Exception e) {
        logger.error("获取自动填充的学历异常,psnId=" + psnId, e);
      }
    }
    return resultMap;
  }

  /**
   * 个人主页-获取自动填充的职称
   */
  @RequestMapping(value = "/psn/mobile/edit/ajaxautoposition", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAutoPostion(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    String searchKey = psnOperateVO.getPosition();
    if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(searchKey)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("searchKey", searchKey);
        params.add("locale", "zh_CN");
        String acPositionListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_POSITION,
            RestUtils.buildPostRequestEntity(params), String.class);
        resultMap.put("result", JacksonUtils.jsonToList(acPositionListStr));
      } catch (Exception e) {
        logger.error("获取自动填充的职称异常,psnId=" + psnId, e);
      }
    }
    return resultMap;
  }

  /**
   * 工作经历保存操作
   */
  @RequestMapping(value = "/psn/mobile/save/workhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object saveWorkhistory(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    String insName = psnOperateVO.getInsName();
    if (StringUtils.isNoneBlank(insName) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("workId",
            NumberUtils.isNotNullOrZero(psnOperateVO.getWorkId()) ? psnOperateVO.getWorkId().toString() : "");
        params.add("insName", insName);
        params.add("anyUser", "7");
        params.add("department", psnOperateVO.getDepartment());
        params.add("position", psnOperateVO.getPosition());
        params.add("fromYear", psnOperateVO.getFromYear().toString());
        params.add("fromMonth", psnOperateVO.getFromMonth().toString());
        params.add("toYear", psnOperateVO.getToYear() == null ? "" : psnOperateVO.getToYear().toString());
        params.add("toMonth", psnOperateVO.getToMonth() == null ? "" : psnOperateVO.getToMonth().toString());
        params.add("isActive", psnOperateVO.getIsActive().toString());
        params.add("description", psnOperateVO.getDescription());
        resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_WORDHISTORY,
                RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("保存工作经历异常,psnId=" + psnId, e);
      }
    } else {
      resultMap.put("status", "error");
    }
    return resultMap;
  }


  /**
   * 工作经历删除操作
   */
  @RequestMapping(value = "/psn/mobile/del/workhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object delWorkhistory(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    if (NumberUtils.isNotNullOrZero(psnOperateVO.getWorkId()) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("workId", psnOperateVO.getWorkId().toString());
        resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.DEL_PSN_WORDHISTORY,
                RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("删除工作经历异常,psnId=" + psnId, e);
      }
    } else {
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 教育经历编辑页面
   */
  @RequestMapping(value = "/psn/mobile/edit/educatehistory", produces = "application/json;charset=UTF-8")
  public ModelAndView editEducatehistory(PsnOperateVO psnOperateVO) {
    ModelAndView model = new ModelAndView();
    try {
      if (!"add".equals(psnOperateVO.getType())) {
        if (NumberUtils.isNotNullOrZero(psnOperateVO.getEduId())) {
          MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
          params.add("psnId", SecurityUtils.getCurrentUserId().toString());
          params.add("eduId", psnOperateVO.getEduId().toString());
          HashMap<String, Object> resultMap = new HashMap<>();
          resultMap =
              (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSN_EDUCATEHISTORY,
                  RestUtils.buildPostRequestEntity(params), Map.class);
          if ("success".equals(resultMap.get("status"))) {
            String data = JacksonUtils.jsonObjectSerializerNoNull(resultMap.get("result"));
            Map<String, String> workHistoryMap = JacksonUtils.jsonToMap(data);
            BeanUtils.copyProperties(psnOperateVO, workHistoryMap);
          }
        }
      }
    } catch (Exception e) {
      logger.error("教育经历编辑页面异常,psnId=" + SecurityUtils.getCurrentUserId(), e);
    }
    DateFormat df = new SimpleDateFormat("yyyy-MM");
    psnOperateVO.setNowTime(df.format(new Date()).toString());
    model.addObject("psnOperateVO", psnOperateVO);
    model.setViewName("/psn/personhomepage/educatehistory_edit");
    return model;
  }

  /**
   * 教育经历保存操作
   */
  @RequestMapping(value = "/psn/mobile/save/educatehistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object saveEducatehistory(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    String insName = psnOperateVO.getInsName();
    if (StringUtils.isNoneBlank(insName) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("eduId",
            NumberUtils.isNotNullOrZero(psnOperateVO.getEduId()) ? psnOperateVO.getEduId().toString() : "");
        params.add("insName", insName);
        params.add("anyUser", "7");
        params.add("study", psnOperateVO.getStudy());
        params.add("degreeName", psnOperateVO.getDegreeName());
        params.add("fromYear", psnOperateVO.getFromYear().toString());
        params.add("fromMonth", psnOperateVO.getFromMonth().toString());
        params.add("toYear", psnOperateVO.getToYear() == null ? "" : psnOperateVO.getToYear().toString());
        params.add("toMonth", psnOperateVO.getToMonth() == null ? "" : psnOperateVO.getToMonth().toString());
        params.add("isActive", psnOperateVO.getIsActive().toString());
        params.add("description", psnOperateVO.getDescription());
        resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_EDUCATEHISTORY,
                RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("保存教育经历异常,psnId=" + SecurityUtils.getCurrentUserId(), e);
      }
    } else {
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 教育经历删除操作
   */
  @RequestMapping(value = "/psn/mobile/del/educatehistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object delEducatehistory(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    if (NumberUtils.isNotNullOrZero(psnOperateVO.getEduId()) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("eduId", psnOperateVO.getEduId().toString());
        resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.DEL_PSN_EDUCATEHISTORY,
                RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("删除教育经历异常,psnId=" + SecurityUtils.getCurrentUserId(), e);
      }
    } else {
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 个人简介编辑页面
   */
  @RequestMapping(value = "/psn/mobile/edit/psnIntro", produces = "application/json;charset=UTF-8")
  public ModelAndView editPsnIntro() {
    ModelAndView model = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        HashMap<String, Object> resultMap = new HashMap<>();
        params.add("psnId", psnId.toString());
        resultMap = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.EDIT_PSN_PSNINTRO,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if ("success".equals(resultMap.get("status"))) {
          model.addObject("psnIntroContent", resultMap.get("result"));
        }
      }
    } catch (Exception e) {
      logger.error("个人简介编辑页面异常,psnId=" + psnId, e);
    }
    model.setViewName("/psn/personhomepage/psnIntro_edit");
    return model;
  }

  /**
   * 保存个人简介操作
   */
  @RequestMapping(value = "/psn/mobile/save/psnIntro", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object savePersonBrief(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    if (NumberUtils.isNotNullOrZero(psnId)) {
      try {
        params.add("psnId", psnId.toString());
        params.add("psnBriefDesc", psnOperateVO.getPsnBriefDesc());
        resultMap = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_PSNINTRO,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("保存个人简介异常,psnId=" + SecurityUtils.getCurrentUserId(), e);
      }
    } else {
      resultMap.put("status", "error");
    }
    return resultMap;
  }


  /**
   * 个人头衔地区数据获取
   */
  @RequestMapping(value = "/psn/mobile/psnInfo/ajaxRegion", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getRegion() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    HashMap<String, Object> resultMap = new HashMap<>();
    HashMap<String, Object> map = new HashMap<>();
    resultMap = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSNINFO_REGION,
        RestUtils.buildPostRequestEntity(params), Map.class);
    if ("success".equals(resultMap.get("status"))) {
      map.put("country", JacksonUtils.jsonToList(resultMap.get("countryList").toString()));
      map.put("province", JacksonUtils.jsonToList(resultMap.get("provinceList").toString()));
      map.put("city", JacksonUtils.jsonToMap(resultMap.get("cityMap").toString()));
    }
    return map;
  }

  /**
   * 构建工作经历选项数据获取
   */
  @RequestMapping(value = "/psn/mobile/psnInfo/ajaxworkhistorylist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getWorkHistoryList() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    HashMap<String, Object> resultMap = new HashMap<>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    resultMap =
        (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSNINFO_WORKHISTORY,
            RestUtils.buildPostRequestEntity(params), Map.class);
    return resultMap;
  }


  /**
   * 个人头衔编辑页面
   */
  @RequestMapping(value = "/psn/mobile/edit/psnInfo", produces = "application/json;charset=UTF-8")
  public ModelAndView editPsnInfo(PsnOperateVO psnOperateVO) {
    ModelAndView model = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        HashMap<String, Object> resultMap = new HashMap<>();
        params.add("psnId", psnId.toString());
        resultMap = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.EDIT_PSN_PSNINFO,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if ("success".equals(resultMap.get("result"))) {
          String data = JacksonUtils.jsonObjectSerializerNoNull(resultMap.get("data"));
          Map<String, String> psnInfoMap = JacksonUtils.jsonToMap(data);
          BeanUtils.copyProperties(psnOperateVO, psnInfoMap);
          model.addObject("psnOperateVO", psnOperateVO);
        }
      }
    } catch (Exception e) {
      logger.error("个人头衔编辑页面异常,psnId=" + psnId, e);
    }
    model.setViewName("/psn/personhomepage/psnInfo_edit");
    return model;
  }

  /**
   * 保存个人头衔操作
   */
  @RequestMapping(value = "/psn/mobile/save/psnInfo", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object savePsnInfo(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<>();
    if (StringUtils.isNoneBlank(psnOperateVO.getFirstName()) && StringUtils.isNoneBlank(psnOperateVO.getLastName())
        && NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(psnOperateVO.getRegionId())
        && StringUtils.isNoneBlank(psnOperateVO.getName())) {
      try {
        params.add("psnId", psnId.toString());
        params.add("titolo", psnOperateVO.getTitolo());
        params.add("regionId",
            NumberUtils.isNotNullOrZero(psnOperateVO.getRegionId()) ? psnOperateVO.getRegionId().toString() : "");
        params.add("workId", psnOperateVO.getWorkId().toString());
        params.add("firstName", psnOperateVO.getFirstName());
        params.add("lastName", psnOperateVO.getLastName());
        params.add("zhFirstName", psnOperateVO.getZhFirstName());
        params.add("zhLastName", psnOperateVO.getZhLastName());
        params.add("name", psnOperateVO.getName());
        params.add("otherName", psnOperateVO.getOtherName());
        resultMap = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_PSNINFO,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        logger.error("保存个人头衔异常,psnId=" + psnId, e);
      }
    } else {
      resultMap.put("status", "error");
      resultMap.put("msg", "参数校验不通过");
    }
    return resultMap;
  }

  /**
   * 编辑个人科技领域
   */
  @RequestMapping(value = "/psn/mobile/improvearea", produces = "application/json;charset=UTF-8")
  public ModelAndView editPsnArea(PsnOperateVO psnOperateVO) {
    ModelAndView view = new ModelAndView();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    Map<String, Object> map = restTemplate.postForObject(domainMobile + PsnApiConsts.PSN_EDIT_AREAS, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      view.addObject("allAreas", map.get("result"));
    }
    view.addObject("psnOperateVO", psnOperateVO);
    view.setViewName("/psn/personhomepage/edit_psn_areas");
    return view;
  }
}
