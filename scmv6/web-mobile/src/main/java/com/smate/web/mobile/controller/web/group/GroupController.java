package com.smate.web.mobile.controller.web.group;


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

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.group.vo.GroupOperateVO;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;



@Controller
public class GroupController {
  final protected Logger logger = LoggerFactory.getLogger(GroupController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 发现群组主页
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/findgroupmain")
  public ModelAndView findGoupMain(GroupOperateVO groupVO) {
    ModelAndView view = new ModelAndView();
    view.addObject("groupVO", groupVO);
    view.setViewName("/group/findGroup/mobile_find_group_main");
    return view;
  }


  /**
   * 发现群组列表
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/findgrouplist")
  public ModelAndView findGroupList(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      MultiValueMap<String, Object> params = getFindGoupList(groupVO);// 获取参数
      HashMap<String, Object> result = postData(params, GrpApiConsts.FIND_GROUP_LIST);
      if (result != null && result.get("results") != null) {
        HashMap<String, Object> resultMap = (HashMap<String, Object>) result.get("results");
        view.addObject("goupList", resultMap.get("commentlist"));
        view.addObject("totalCount", resultMap.get("total"));
      }
    } catch (Exception e) {
      logger.error("获取推荐群组列表出错,psnid={}", psnId, e);
    }
    view.setViewName("/group/findGroup/find_group_list");
    return view;
  }

  /**
   * 群组推荐后操作（申请 ， 忽略）
   * 
   * @return
   */
  @RequestMapping(value = "/grp/mobile/optionRcmdGrp")
  @ResponseBody
  public Object ajaxOptionRcmdGrp(GroupOperateVO groupVO) {
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
            resultMap.put("status", "success");
          } else {
            resultMap.put("status", "fail");
          }
        } else {
          resultMap.put("status", "success");
        }
      } else {
        resultMap.put("status", "fail");
      }
    } catch (Exception e) {
      logger.error("群组推荐后操作（申请 ， 忽略）出错,psnid={}", psnId, e);
      resultMap.put("status", "error");
    }
    return resultMap;
  }

  /**
   * 申请/取消申请加入群组
   * 
   * @return
   */
  @RequestMapping("/grp/mobile/applyjoingrp")
  @ResponseBody
  public Object applyJoinGrp(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    try {
      MultiValueMap<String, Object> params = getApplyJoinGrp(groupVO);// 获取参数
      HashMap<String, Object> result = postData(params, GrpApiConsts.OP_JOIN_GROUP);
      if ("200".equals(result.get("status"))) {
        resultMap.put("status", "success");
      } else {
        resultMap.put("status", "fail");
      }
    } catch (Exception e) {
      logger.error("申请/取消申请加入群组出错,psnid={}", psnId, e);
      resultMap.put("status", "error");
    }
    return resultMap;
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
    params.add("disciplineCategory", groupVO.getDisciplineCategory());// 群组科技领域
    params.add("searchKey", groupVO.getSearchKey());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", groupVO.getPage().getParamPageNo().toString());
    return params;
  }

  /**
   * 发现群组条件设置
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/findgroupconditions")
  public ModelAndView findGroupConditions(GroupOperateVO groupVO) {
    ModelAndView view = new ModelAndView();
    view.addObject("groupVO", groupVO);
    view.setViewName("/group/findGroup/find_group_conditions");
    return view;
  }

  /**
   * 我的群组主页
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/mygroupmain")
  public ModelAndView myGroup(GroupOperateVO groupVO) {
    ModelAndView view = new ModelAndView();
    view.addObject("groupVO", groupVO);
    view.setViewName("/group/myGroup/mobile_my_group_main");
    return view;
  }

  /**
   * 我的群组列表
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/mygrouplist")
  public ModelAndView myGroupList(GroupOperateVO groupVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      MultiValueMap<String, Object> params = getMyGoupList(groupVO);// 获取参数
      HashMap<String, Object> result = postData(params, GrpApiConsts.MY_GROUP_LIST);
      if (result != null && result.get("results") != null) {
        HashMap<String, Object> resultMap = (HashMap<String, Object>) result.get("results");
        view.addObject("goupList", resultMap.get("commentlist"));
        view.addObject("totalCount", resultMap.get("total"));
      }
    } catch (Exception e) {
      logger.error("获取推荐群组列表出错,psnid={}", psnId, e);
    }
    view.setViewName("/group/myGroup/my_group_list");
    return view;
  }

  /**
   * 获取调用我的群组列表接口参数
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
   * 我的群组条件设置
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/mygroupconditions")
  public ModelAndView myGroupConditions(GroupOperateVO groupVO) {
    ModelAndView view = new ModelAndView();
    view.addObject("groupVO", groupVO);
    view.setViewName("/group/myGroup/my_group_conditions");
    return view;
  }


  public HashMap<String, Object> postData(MultiValueMap<String, Object> params, String url) throws Exception {
    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + url, params, Map.class);
    return result;
  }
}
