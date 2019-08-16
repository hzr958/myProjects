package com.smate.web.mobile.controller.web.pub;

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

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.PubListVO;
import com.smate.web.mobile.v8pub.vo.PubQueryModel;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * 编辑代表成果页面
 * 
 * @author ltl
 *
 */
@Controller
public class EditRepresentPubController {
  final protected Logger logger = LoggerFactory.getLogger(EditRepresentPubController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 进入编辑代表成果页面
   * 
   * @return
   */
  @RequestMapping(value = "/pub/represent/editenter")
  public ModelAndView enterEditPage() {
    ModelAndView model = new ModelAndView();
    model.setViewName("/pub/represent/edit_represent_main");
    return model;
  }

  /**
   * 添加代表成果列表页面
   * 
   * @return
   */
  @RequestMapping(value = "/pub/represent/addenter")
  public ModelAndView enterAddPage(PubQueryModel pubQueryModel) {
    ModelAndView model = new ModelAndView();
    if ("DEFAULT".equals(pubQueryModel.getOrderBy())) {// 默认发表年份
      pubQueryModel.setOrderBy("pubLishDate");
    }
    PubListVO pubListVO = getPresentPub();
    String pubids = getPresentDes3PubIds(pubListVO.getResultList());// 已经添加的成果ids

    pubQueryModel.setRepresentDes3PubIds(pubids);
    model.addObject("pubQueryModel", pubQueryModel);
    model.setViewName("/pub/represent/add_represent_main");
    return model;
  }

  private String getPresentDes3PubIds(List<PubInfo> publist) {
    return Optional.of(publist).map(list -> list.stream().map(PubInfo::getDes3PubId).collect(Collectors.joining(",")))
        .orElseGet(() -> "");
  }

  @SuppressWarnings("unchecked")
  private PubListVO getPresentPub() {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    List<Map<String, Object>> resultList = new ArrayList<>();
    PubListVO pubListVO = new PubListVO();// 返回页面的对象

    if (psnId > 0) {
      String representUrl = PubApiInfoConsts.PUB_LIST;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.add("des3SearchPsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.add("serviceType", "psnRepresentPubList");
      resultMap = postJsonUrl(param, representUrl);// 调接口查询代表成果

      if (resultMap != null && resultMap.get("status").equals(V8pubConst.SUCCESS)) {
        resultList = (List<Map<String, Object>>) resultMap.get("resultList");
        Object totalCount = resultMap.get("totalCount");
        List<PubInfo> list = buildPubInfoList(resultList);
        pubListVO.setResultList(list);
        pubListVO.setTotalCount(NumberUtils.toInt(Objects.toString(totalCount)));
      }
    }
    return pubListVO;
  }

  /**
   * 添加代表成果列表条件设置页面
   * 
   * @return
   */
  @RequestMapping(value = "/pub/represent/enteraddpubcondition")
  public ModelAndView enterAddPubCondition(PubQueryModel pubQueryModel) {
    ModelAndView model = new ModelAndView();
    pubQueryModel.setCurrentYear(Calendar.getInstance().get(Calendar.YEAR));
    model.addObject("model", pubQueryModel);
    model.setViewName("/pub/represent/search_pub_conditions");
    return model;
  }

  /**
   * 获取代表成果列表
   * 
   * @return
   */
  @RequestMapping(value = "/pub/represent/ajaxpublist")
  public ModelAndView getPubList() {
    ModelAndView model = new ModelAndView();
    model.addObject("pubListVO", getPresentPub());// 调用接口返回代表成果列表
    model.setViewName("/pub/represent/represent_pub_list");
    return model;
  }

  /**
   * 获取公开成果列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/represent/ajaxopenpublist")
  public ModelAndView getOpenPubList(PubQueryModel pubQueryModel) {
    ModelAndView model = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    List<Map<String, Object>> resultList = new ArrayList<>();
    PubListVO pubListVO = new PubListVO();// 返回页面的对象
    pubListVO.setPage(pubQueryModel.getPage());
    if (psnId > 0) {
      String representUrl = PubApiInfoConsts.OPEN_PUB_LIST;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3SearchPsnId", Des3Utils.encodeToDes3(psnId.toString()));
      param.add("publishYear", pubQueryModel.getPublishYear());// 出版年份
      param.add("pubType", pubQueryModel.getPubType());// 成果类型
      param.add("includeType", pubQueryModel.getIncludeType());// 收录类型
      param.add("orderBy", pubQueryModel.getOrderBy());// 排序
      param.add("searchKey", pubQueryModel.getSearchKey());// 查询字符串
      param.add("pageNo", pubQueryModel.getPage().getPageNo().toString());// 查询页数

      resultMap = postFormUrl(param, representUrl);// 调接口查询代表成果

      if (resultMap != null && resultMap.get("status").equals(V8pubConst.SUCCESS)) {
        resultList = (List<Map<String, Object>>) resultMap.get("resultList");
        Object totalCount = resultMap.get("totalCount");
        List<PubInfo> list = buildPubInfoList(resultList);
        pubListVO.setResultList(list);
        pubListVO.setTotalCount(NumberUtils.toInt(Objects.toString(totalCount)));
      }
    }
    model.addObject("pubListVO", pubListVO);
    model.setViewName("/pub/represent/add_pub_list");
    return model;
  }

  /**
   * 保存代表成果
   * 
   * @return
   */
  @RequestMapping(value = "/pub/represent/ajaxsaverepresentpub")
  @ResponseBody
  public Object saveRepresentPub(PubOperateVO pubOperateVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();// 接口接收对象
    String addToRepresentPubIds = pubOperateVO.getRepresentDes3PubIds();
    if (psnId > 0) {
      String representUrl = PubApiInfoConsts.SAVE_REPRESENT_PUB;
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));

      param.add("addToRepresentPubIds", addToRepresentPubIds);
      resultMap = postFormUrl(param, representUrl);// 调接口查询代表成果
    } else {
      resultMap.put("result", "error");
    }
    return resultMap;
  }

  /**
   * 复制pubInfo
   * 
   * @param resultList
   * @return
   */
  private List<PubInfo> buildPubInfoList(List<Map<String, Object>> resultList) {
    List<PubInfo> list = new ArrayList<>(16);
    if (resultList != null && resultList.size() > 0) {
      for (Map<String, Object> map : resultList) {
        PubInfo pubInfo = new PubInfo();
        try {
          ConvertUtils.register(new DateConverter(null), java.util.Date.class);
          BeanUtils.populate(pubInfo, map);
          list.add(pubInfo);
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
