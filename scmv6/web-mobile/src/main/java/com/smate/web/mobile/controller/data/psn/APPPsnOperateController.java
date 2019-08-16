package com.smate.web.mobile.controller.data.psn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PsnApiConsts;
import com.smate.web.mobile.v8pub.vo.sns.PsnOperateVO;

@RestController
public class APPPsnOperateController {
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping(value = "data/psn/mobile/editareas", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object editPsnArea() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    Map<String, Object> map = restTemplate.postForObject(domainMobile + PsnApiConsts.PSN_EDIT_AREAS, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  @RequestMapping(value = "data/psn/mobile/editkeyword", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object editPsnKeyWord() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.PSN_EDIT_KEY_WORD, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 自动填充学科关键词
   * 
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/autoconstkeydiscs", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String autoConstKeyDiscs(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("searchKey", vo.getSearchKey());
    params.add("keySize", vo.getKeySize());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.AUTOCONST_KEY_DISCS, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 保存人员关键词
   * 
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/savepsnkeywords", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String savePsnKeywords(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("psnKeyStr", vo.getPsnKeyStr());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_KEYWORDS, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 绑定手机号时候发送的验证码 result:exist(绑定了账号),error(不是手机号), success(成功)
   *
   * @return
   */
  @RequestMapping(value = "data/outside/psn/sendMobileCode", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String sendMobileCode(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("mobileNumber", vo.getMobileNumber());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.SEND_MOBILE_CODE, params, Map.class);
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("result"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("result"), ""));
    }
  }

  /**
   * 登录时候发的验证码 result:notExist(手机未绑定账号),error(不是手机号), success(成功)
   *
   * @return
   */
  @RequestMapping(value = "data/outside/psn/sendLonginMobileCode", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String sendLonginMobileCode(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("mobileNumber", vo.getMobileNumber());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.SEND_LOGIN_MOBILE_CODE, params, Map.class);
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("result"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("result"), ""));
    }
  }

  /**
   * 获取个人工作经历
   *
   * @param workId
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/showworkhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String showEditPsnWorkHistory(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    params.add("workId", vo.getWorkId());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSN_WORDHISTORY, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 保存个人工作经历
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/saveworkhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String savePsnWorkHistory(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isNoneBlank(vo.getInsName())) {
      params.add("psnId", SecurityUtils.getCurrentUserId().toString());
      params.add("workId", vo.getWorkId());
      params.add("insName", vo.getInsName());
      params.add("anyUser", "7");
      params.add("department", vo.getDepartment());
      params.add("position", vo.getPosition());
      params.add("fromYear", vo.getFromYear());
      params.add("fromMonth", vo.getFromMonth());
      params.add("toYear", vo.getToYear() == null ? "" : vo.getToYear());
      params.add("toMonth", vo.getToMonth() == null ? "" : vo.getToMonth());
      params.add("isActive", vo.getIsActive().toString());
      params.add("description", vo.getDescription());
      map = restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_WORDHISTORY, params, Map.class);
    } else {
      map.put("result", "error");
      map.put("msg", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("workId"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 删除个人工作经历
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/delworkhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String delPsnWorkHistory(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isNoneBlank(vo.getWorkId())) {
      params.add("psnId", SecurityUtils.getCurrentUserId().toString());
      params.add("workId", vo.getWorkId());
      map = restTemplate.postForObject(domainMobile + PsnApiConsts.DEL_PSN_WORDHISTORY, params, Map.class);
    } else {
      map.put("result", "error");
      map.put("msg", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }


  /**
   * 获取个人教育经历
   *
   * @param eduId
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/showeduhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String showEditPsnEduHistory(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    params.add("eduId", vo.getEduId());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSN_EDUCATEHISTORY, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 保存个人教育经历
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/saveeduhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String savePsnEduHistory(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isNoneBlank(vo.getInsName())) {
      params.add("psnId", SecurityUtils.getCurrentUserId().toString());
      params.add("eduId", StringUtils.isNoneBlank(vo.getEduId()) ? vo.getEduId() : "");
      params.add("insName", vo.getInsName());
      params.add("anyUser", "7");
      params.add("study", vo.getStudy());
      params.add("degreeName", vo.getDegreeName());
      params.add("fromYear", vo.getFromYear());
      params.add("fromMonth", vo.getFromMonth());
      params.add("toYear", vo.getToYear() == null ? "" : vo.getToYear());
      params.add("toMonth", vo.getToMonth() == null ? "" : vo.getToMonth());
      params.add("isActive", vo.getIsActive().toString());
      params.add("description", vo.getDescription());
      map = restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_EDUCATEHISTORY, params, Map.class);
    } else {
      map.put("result", "error");
      map.put("msg", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("eduId"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 删除个人教育经历
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/deleduhistory", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String delPsnEduHistory(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isNoneBlank(vo.getEduId())) {
      params.add("psnId", SecurityUtils.getCurrentUserId().toString());
      params.add("eduId", vo.getEduId());
      map = restTemplate.postForObject(domainMobile + PsnApiConsts.DEL_PSN_EDUCATEHISTORY, params, Map.class);
    } else {
      map.put("result", "error");
      map.put("msg", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 获取个人简介数据
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/getpsnintro", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getPsnIntro() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.EDIT_PSN_PSNINTRO, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 保存个人简介
   *
   * @param psnBriefDesc
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/savepsnintro", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String savePsnIntro(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    params.add("psnBriefDesc", vo.getPsnBriefDesc());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_PSNINTRO, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 获取个人头衔数据
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/getpsninfo", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getPsnInfo() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.EDIT_PSN_PSNINFO, params, Map.class);
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map.get("data"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 获取个人头衔 -- 工作经历列表
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/getpsnworkhistorylist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getPsnWorkHistoryList() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("psnId", SecurityUtils.getCurrentUserId().toString());
    Map<String, Object> map =
        restTemplate.postForObject(domainMobile + PsnApiConsts.GET_PSNINFO_WORKHISTORY, params, Map.class);
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 保存个人头衔接口
   *
   * @return
   */
  @RequestMapping(value = "data/psn/mobile/savepsninfo", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String savePsnInfo(PsnOperateVO vo) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (StringUtils.isNoneBlank(vo.getLastName()) && NumberUtils.isNotNullOrZero(psnId)
        && NumberUtils.isNotNullOrZero(vo.getRegionId()) && StringUtils.isNoneBlank(vo.getName())) {
      params.add("psnId", psnId.toString());
      params.add("titolo", vo.getTitolo());
      params.add("regionId", NumberUtils.isNotNullOrZero(vo.getRegionId()) ? vo.getRegionId().toString() : "");
      params.add("workId", vo.getWorkId());
      params.add("firstName", vo.getFirstName());
      params.add("lastName", vo.getLastName());
      params.add("zhFirstName", vo.getZhFirstName());
      params.add("zhLastName", vo.getZhLastName());
      params.add("name", vo.getName());
      params.add("otherName", vo.getOtherName());
      map = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.SAVE_PSN_PSNINFO,
          RestUtils.buildPostRequestEntity(params), Map.class);
    } else {
      map.put("status", "error");
      map.put("msg", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("result"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  /**
   * 个人主页--获取自动填充的机构名称
   */
  @RequestMapping(value = "data/psn/mobile/ajaxautoinstitution", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getAutoInstitution(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String insName = psnOperateVO.getInsName();
    if (StringUtils.isNoneBlank(insName) && NumberUtils.isNotNullOrZero(psnId)) {
      params.add("psnId", psnId.toString());
      params.add("searchKey", insName);
      params.add("locale", "zh_CN");
      String acInstitutionListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_INSTITUTION,
          RestUtils.buildPostRequestEntity(params), String.class);
      return AppActionUtils.buildReturnInfo(JacksonUtils.jsonToList(acInstitutionListStr), 0,
          AppActionUtils.changeResultStatus("success"), Objects.toString(null, ""));
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
        Objects.toString(null, ""));
  }

  /**
   * 个人主页-获取自动填充的部门(学院)
   */
  @RequestMapping(value = "data/psn/mobile/ajaxautoinsunit", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getAutoInsUnit(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String insName = psnOperateVO.getInsName();
    if (StringUtils.isNoneBlank(insName) && NumberUtils.isNotNullOrZero(psnId)
        && StringUtils.isNoneBlank(psnOperateVO.getSearchKey())) {
      params.add("psnId", psnId.toString());
      params.add("insName", insName);
      params.add("searchKey", psnOperateVO.getSearchKey());
      params.add("locale", "zh_CN");
      String acInsunitListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_INSUNIT,
          RestUtils.buildPostRequestEntity(params), String.class);
      return AppActionUtils.buildReturnInfo(JacksonUtils.jsonToList(acInsunitListStr), 0,
          AppActionUtils.changeResultStatus("success"), Objects.toString(null, ""));
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
        Objects.toString(null, ""));
  }

  /**
   * 个人主页-获取自动填充的学历
   */
  @RequestMapping(value = "data/psn/mobile/ajaxautodegree", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAutoDegree(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String searchKey = psnOperateVO.getDegreeName();
    if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(searchKey)) {
      params.add("psnId", psnId.toString());
      params.add("searchKey", searchKey);
      params.add("locale", "zh_CN");
      String acDegreeListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_DEGREE,
          RestUtils.buildPostRequestEntity(params), String.class);
      return AppActionUtils.buildReturnInfo(JacksonUtils.jsonToList(acDegreeListStr), 0,
          AppActionUtils.changeResultStatus("success"), Objects.toString(null, ""));
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
        Objects.toString(null, ""));
  }

  /**
   * 个人主页-获取自动填充的职称
   */
  @RequestMapping(value = "data/psn/mobile/ajaxautoposition", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getAutoPostion(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String searchKey = psnOperateVO.getPosition();
    if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(searchKey)) {
      params.add("psnId", psnId.toString());
      params.add("searchKey", searchKey);
      params.add("locale", "zh_CN");
      String acPositionListStr = restTemplate.postForObject(domainMobile + PsnApiConsts.GET_AUTO_POSITION,
          RestUtils.buildPostRequestEntity(params), String.class);
      return AppActionUtils.buildReturnInfo(JacksonUtils.jsonToList(acPositionListStr), 0,
          AppActionUtils.changeResultStatus("success"), Objects.toString(null, ""));
    }
    return AppActionUtils.buildReturnInfo(null, 0, AppActionUtils.changeResultStatus("error"),
        Objects.toString(null, ""));
  }

  /**
   * 成果认领-邀请合作者成为联系人
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/psn/mobile/invitetofriend", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object inviteFriend(PsnOperateVO psnOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String des3PdwhPubId = psnOperateVO.getDes3PdwhPubId();
    Map<String, Object> map = new HashMap<>();
    if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNoneBlank(des3PdwhPubId)) {
      params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      params.add("des3PdwhPubId", des3PdwhPubId);
      map = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.INVIETO_FRIEND,
          RestUtils.buildPostRequestEntity(params), Map.class);

    } else {
      map.put("status", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("status"))) {
      return AppActionUtils.buildReturnInfo(map.get("status"), 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(map.get("status"), 0, AppActionUtils.changeResultStatus("error"),
          Objects.toString(map.get("errmsg"), ""));
    }
  }

  // 关键词认同
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/psn/keyword/identifickeyword", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object identificKeyword(PsnOperateVO psnOperateVO) {
    Long currentPsnId = SecurityUtils.getCurrentUserId();// 操作人
    Long keyWordPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(psnOperateVO.getDes3PsnId()));// 关键词拥有者
    Long oneKeyWordId = psnOperateVO.getOneKeyWordId();// 关键词id
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

    Map<String, Object> map = new HashMap<>();
    if (checkIdentificKeywordParam(currentPsnId, keyWordPsnId, oneKeyWordId)) {
      if (!currentPsnId.equals(keyWordPsnId)) {
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(currentPsnId.toString()));
        params.add("des3PsnId", psnOperateVO.getDes3PsnId());
        params.add("oneKeyWordId", oneKeyWordId.toString());
        map = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + PsnApiConsts.IDENTIFIC_KEYWORD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } else {
        map.put("status", "不能认同自己的关键词");
      }

    } else {
      map.put("status", "参数校验不通过");
    }
    if (map != null && "success".equals(map.get("result"))) {
      return AppActionUtils.buildReturnInfo(map, 0, AppActionUtils.changeResultStatus("success"),
          Objects.toString(map.get("errmsg"), ""));
    } else {
      return AppActionUtils.buildReturnInfo(map, 0, AppActionUtils.changeResultStatus("result"),
          Objects.toString(map.get("result"), ""));
    }
  }

  private boolean checkIdentificKeywordParam(Long currentPsnId, Long keyWordPsnId, Long oneKeyWordId) {
    return NumberUtils.isNotNullOrZero(currentPsnId) && NumberUtils.isNotNullOrZero(keyWordPsnId)
        && NumberUtils.isNotNullOrZero(oneKeyWordId);
  }

}
