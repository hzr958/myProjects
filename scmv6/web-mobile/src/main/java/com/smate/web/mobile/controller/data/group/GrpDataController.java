package com.smate.web.mobile.controller.data.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.controller.web.group.GroupController;
import com.smate.web.mobile.group.vo.GroupOperateVO;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;

@RestController
public class GrpDataController {
  final protected Logger logger = LoggerFactory.getLogger(GroupController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 发现群组--群组列表
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/findgrouplist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object findGroupList(GroupOperateVO groupVO) {
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (psnId != null && psnId != 0L) {
        if (NumberUtils.isNotNullOrZero(psnId)) {
          MultiValueMap<String, Object> params = getFindGoupList(groupVO);// 获取参数
          result = postData(params, GrpApiConsts.FIND_GROUP_LIST);
        }
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }

    String status = Objects.toString(result.get("status"));
    if ("200".equals(status)) {
      return result;
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, status, Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 群组推荐后操作（申请 ， 忽略）
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/optionRcmdGrp", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object optionRcmdGrp(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    try {
      MultiValueMap<String, Object> params = getOpGoupList(groupVO);// 获取参数
      HashMap<String, Object> result = postData(params, GrpApiConsts.OP_RCMD_GROUP);
      if ("200".equals(result.get("status"))) {// 成功后请求加入群组
        if (groupVO.getRcmdStatus() != 9) {
          MultiValueMap<String, Object> joinParams = getApplyJoinGrp(groupVO);// 获取参数
          HashMap<String, Object> joinResult = postData(joinParams, GrpApiConsts.OP_JOIN_GROUP);
          if ("200".equals(joinResult.get("status"))) {
            return result;
          }
        } else {
          return result;
        }
      }
    } catch (Exception e) {
      logger.error("群组推荐后操作（申请 ， 忽略）出错,psnid={}", psnId, e);
    }
    return AppActionUtils.buildReturnInfo(null, 0, "500", "加入群组失败");
  }

  /**
   * 群组推荐后操作（申请 ， 忽略）
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/applyjoingrp", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object applyJoinGrp(GroupOperateVO groupVO) {
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (psnId != null && psnId != 0L) {
        if (NumberUtils.isNotNullOrZero(psnId)) {
          MultiValueMap<String, Object> params = getApplyJoinGrp(groupVO);// 获取参数
          result = postData(params, GrpApiConsts.OP_JOIN_GROUP);
        }
      }
    } catch (Exception e) {
      logger.error("mobile群组推荐后操作（申请 ， 忽略）异常,psnId=" + psnId, e);
    }
    String status = Objects.toString(result.get("status"));
    if ("200".equals(status)) {
      return result;
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, status, Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 获取申请/取消申请加入群组接口参数
   * 
   * @param groupVO
   * @return
   */
  private MultiValueMap<String, Object> getApplyJoinGrp(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

    params.add("isApplyJoinGrp", groupVO.getIsApplyJoinGrp());// 操作类型
    params.add("des3GrpId", groupVO.getDes3GrpId());// 群组科技领域
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    return params;
  }

  /**
   * 获取调用群组推荐列表接口参数
   * 
   * @param groupVO
   * @return
   */
  private MultiValueMap<String, Object> getFindGoupList(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

    params.add("grpCategory", groupVO.getGrpCategory());// 群组类型
    params.add("searchByRole", groupVO.getSearchByRole());// 群组角色
    params.add("disciplineCategory", groupVO.getDisciplineCategory());// 群组科技领域
    params.add("searchKey", groupVO.getSearchKey());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", groupVO.getPage().getParamPageNo().toString());
    return params;
  }

  /**
   * 获取分享群组列表接口参数
   * 
   * @param groupVO
   * @return
   */
  private MultiValueMap<String, Object> getShareGoupList(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

    params.add("searchKey", groupVO.getSearchKey());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", groupVO.getPage().getParamPageNo().toString());
    return params;
  }

  /**
   * 群组推荐后操作接口参数
   * 
   * @param groupVO
   * @return
   */
  private MultiValueMap<String, Object> getOpGoupList(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

    params.add("des3GrpId", groupVO.getDes3GrpId());// 群组id
    params.add("rcmdStatus", groupVO.getRcmdStatus());// 操作
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    return params;
  }

  /**
   * 我的群组--群组列表
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/mygrouplist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object myGroupList(GroupOperateVO groupVO) {
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (psnId != null && psnId != 0L) {
        if (NumberUtils.isNotNullOrZero(psnId)) {
          MultiValueMap<String, Object> params = getFindGoupList(groupVO);// 获取参数
          result = postData(params, GrpApiConsts.MY_GROUP_LIST);
        }
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    String status = Objects.toString(result.get("status"));
    if ("200".equals(status)) {
      return result;
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, status, Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 我的群组--群组列表
   * 
   * @param prjOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/sharegrouplist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object shareGroupList(GroupOperateVO groupVO) {
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    try {
      if (psnId != null && psnId != 0L) {
        if (NumberUtils.isNotNullOrZero(psnId)) {
          MultiValueMap<String, Object> params = getShareGoupList(groupVO);// 获取参数
          result = postData(params, GrpApiConsts.GET_SHARE_GRPLIST);
        }
      }
    } catch (Exception e) {
      logger.error("mobile资助机构--基金列表异常,psnId=" + psnId, e);
    }
    String status = Objects.toString(result.get("status"));
    if ("200".equals(status)) {
      return result;
    } else {
      return AppActionUtils.buildReturnInfo(null, 0, status, Objects.toString(result.get("errmsg"), ""));
    }
  }

  /**
   * 群组推荐后操作（申请 ， 忽略）
   * 
   * @return
   */
  @RequestMapping(value = "/data/grp/getgrpcontrol", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getGrpContol(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      HashMap<String, Object> result = getGroupInfo(groupVO);
      if ("200".equals(result.get("status"))) {// 成功后请求加入群组
        return AppActionUtils.buildReturnInfo(result, 0, "200", "获取群组设置信息成功");
      }
    } catch (Exception e) {
      logger.error("群组推荐后操作（申请 ， 忽略）出错,psnid={}", psnId, e);
    }
    return AppActionUtils.buildReturnInfo(null, 0, "500", "获取群组设置信息失败");
  }

  private HashMap<String, Object> getGroupInfo(GroupOperateVO groupVO) throws Exception {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3GrpId", groupVO.getDes3GrpId());
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    HashMap<String, Object> result = postData(params, GrpApiConsts.GET_GRP_CONTROL);
    return result;
  }

  /**
   * 获取调用群组推荐列表接口参数
   * 
   * @param groupVO
   * @return
   */
  private MultiValueMap<String, Object> getMyGoupList(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();

    params.add("grpCategory", groupVO.getGrpCategory());// 群组类型
    params.add("searchByRole", groupVO.getSearchByRole());// 群组角色
    params.add("searchKey", groupVO.getSearchKey());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", groupVO.getPage().getParamPageNo().toString());
    return params;
  }

  public HashMap<String, Object> postData(MultiValueMap<String, Object> params, String url) throws Exception {
    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + url, params, Map.class);
    return result;
  }
}
