package com.smate.web.mobile.controller.web.prj;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.PrjListVO;


/**
 * 编辑代表项目页面
 * 
 * @author ltl
 *
 */
@Controller
public class EditRepresentPrjController {
  final protected Logger logger = LoggerFactory.getLogger(EditRepresentPrjController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 进入编辑代表项目页面
   * 
   * @return
   */
  @RequestMapping(value = "/prj/mobile/represent/editenter")
  public ModelAndView enterRrjEditPage() {
    ModelAndView model = new ModelAndView();
    model.setViewName("/prj/represent/edit_represent_main");
    return model;
  }

  /**
   * 添加代表项目列表页面
   * 
   * @return
   */
  @RequestMapping(value = "/prj/mobile/represent/addenter")
  public ModelAndView enterAddRrjPage(PrjListVO prjListVO) {
    ModelAndView model = new ModelAndView();

    PrjListVO result = getPresentPrj();
    String prjids = getPresentDes3PrjIds(result.getResultList());// 已经添加的项目ids

    prjListVO.setAddToRepresentPrjIds(prjids);
    model.addObject("prjListVO", prjListVO);
    model.setViewName("/prj/represent/add_represent_main");
    return model;
  }

  private String getPresentDes3PrjIds(List<ProjectInfo> prjlist) {
    return Optional.of(prjlist).map(list -> list.stream().map(ProjectInfo::getDes3Id).collect(Collectors.joining(",")))
        .orElseGet(() -> "");
  }

  @SuppressWarnings("unchecked")
  private PrjListVO getPresentPrj() {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    List<Map<String, Object>> resultList = new ArrayList<>();
    PrjListVO prjListVO = new PrjListVO();// 返回页面的对象

    if (psnId > 0) {
      String representUrl = PubApiInfoConsts.REPRESENT_PRJ_LIST;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.add("des3OtherPsnId", Des3Utils.encodeToDes3(psnId.toString()));
      resultMap = postFormUrl(param, representUrl);// 调接口查询代表项目

      if (resultMap != null && resultMap.get("status").equals(V8pubConst.SUCCESS)) {
        resultList = (List<Map<String, Object>>) resultMap.get("resultList");
        Object totalCount = resultMap.get("totalCount");
        List<ProjectInfo> list = buildPrjInfoList(resultList);
        prjListVO.setResultList(list);
        prjListVO.setTotalCount(NumberUtils.toInt(Objects.toString(totalCount)));
      }
    }
    return prjListVO;
  }

  /**
   * 添加代表项目列表条件设置页面
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/prj/mobile/represent/enteraddprjcondition")
  public ModelAndView enterAddRrjCondition(PrjListVO prjListVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    List<Map<String, Object>> resultList = new ArrayList<>();

    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("searchDes3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    String representUrl = PubApiInfoConsts.REPRESENT_PRJ_CONDITION_AGENCY;
    resultMap = postFormUrl(param, representUrl);// 调接口查询代表项目资助机构
    if (resultMap != null && resultMap.get("status").equals(V8pubConst.SUCCESS)) {
      resultList = (List<Map<String, Object>>) resultMap.get("resultList");
      prjListVO.setAgencyNameList(resultList);
    }
    prjListVO.setCurrentYear(Calendar.getInstance().get(Calendar.YEAR));

    ModelAndView model = new ModelAndView();
    model.addObject("prjListVO", prjListVO);
    model.setViewName("/prj/represent/search_prj_conditions");
    return model;
  }

  /**
   * 获取代表项目列表
   * 
   * @return
   */
  @RequestMapping(value = "/prj/mobile/represent/ajaxprjlist")
  public ModelAndView getRrjList() {
    ModelAndView model = new ModelAndView();
    model.addObject("prjListVO", getPresentPrj());// 调用接口返回代表项目列表
    model.setViewName("/prj/represent/represent_prj_list");
    return model;
  }

  /**
   * 获取公开项目列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/prj/mobile/represent/ajaxopenprjlist")
  public ModelAndView getOpenRrjList(PrjListVO prjListVO) {
    ModelAndView model = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    List<Map<String, Object>> resultList = new ArrayList<>();

    if (psnId > 0) {
      String representUrl = PubApiInfoConsts.PERSON_PRJ_LIST;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();

      param.add("searchDes3PsnId", Des3Utils.encodeToDes3(psnId.toString()));

      param.add("searchKey", prjListVO.getSearchKey());
      param.add("orderBy", prjListVO.getOrderBy());
      param.add("fundingYear", prjListVO.getFundingYear());
      param.add("agencyNames", prjListVO.getAgencyNames());
      param.add("page.pageNo", prjListVO.getPage().getPageNo().toString());// 查询页数

      resultMap = postFormUrl(param, representUrl);// 调接口查询代表项目

      if (resultMap != null && resultMap.get("status").equals(V8pubConst.SUCCESS)) {
        resultList = (List<Map<String, Object>>) resultMap.get("resultList");
        Object totalCount = resultMap.get("totalCount");
        List<ProjectInfo> list = buildPrjInfoList(resultList);
        prjListVO.setResultList(list);
        prjListVO.setTotalCount(NumberUtils.toInt(Objects.toString(totalCount)));
      }
    }
    model.addObject("prjListVO", prjListVO);
    model.setViewName("/prj/represent/add_prj_list");
    return model;
  }



  /**
   * 保存代表项目
   * 
   * @return
   */
  @RequestMapping(value = "/prj/mobile/represent/ajaxsaverepresentprj")
  @ResponseBody
  public Object saveRepresentRrj(PrjListVO prjListVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    String addToRepresentPrjIds = prjListVO.getAddToRepresentPrjIds();
    if (psnId > 0) {
      String representUrl = PubApiInfoConsts.REPRESENT_SAVE_PRJ;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));

      param.add("addToRepresentPrjIds", addToRepresentPrjIds);
      resultMap = postFormUrl(param, representUrl);// 调接口查询代表项目
    } else {
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 复制ProjectInfo
   * 
   * @param resultList
   * @return
   */
  private List<ProjectInfo> buildPrjInfoList(List<Map<String, Object>> resultList) {
    List<ProjectInfo> list = new ArrayList<>(16);
    if (resultList != null && resultList.size() > 0) {
      for (Map<String, Object> map : resultList) {
        ProjectInfo prjInfo = new ProjectInfo();
        try {
          ConvertUtils.register(new DateConverter(null), java.util.Date.class);
          Map<String, Object> value = (Map<String, Object>) map.get("projectStatistics");
          map.remove("projectStatistics");
          ProjectStatistics projectStatistics = new ProjectStatistics();
          BeanUtils.populate(projectStatistics, value);
          BeanUtils.populate(prjInfo, map);
          prjInfo.setProjectStatistics(projectStatistics);
          if (StringUtils.isBlank(prjInfo.getDes3Id()) && prjInfo.getPrjId() != null) {
            prjInfo.setDes3Id(Des3Utils.encodeToDes3(prjInfo.getPrjId().toString()));
          }
          prjInfo.setShowAuthorNames(
              StringUtils.isNotBlank(prjInfo.getAuthorNames()) ? prjInfo.getAuthorNames() : prjInfo.getAuthorNamesEn());
          prjInfo
              .setShowTitle(StringUtils.isNotBlank(prjInfo.getZhTitle()) ? prjInfo.getZhTitle() : prjInfo.getEnTitle());
          prjInfo.setShowBriefDesc(
              StringUtils.isNotBlank(prjInfo.getBriefDesc()) ? prjInfo.getBriefDesc() : prjInfo.getBriefDescEn());
          list.add(prjInfo);
        } catch (IllegalAccessException | InvocationTargetException e) {
          logger.error("复制属性异常", e);
        }
      }
    }
    return list;
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> postJsonUrl(MultiValueMap param, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return (Map<String, Object>) restTemplate.postForObject(domainMobile + url, HttpEntity, Object.class);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private Map<String, Object> postFormUrl(MultiValueMap param, String url) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return (Map<String, Object>) restTemplate.postForObject(domainMobile + url, HttpEntity, Object.class);
  }
}
