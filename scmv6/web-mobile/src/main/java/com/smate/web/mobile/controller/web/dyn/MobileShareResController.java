package com.smate.web.mobile.controller.web.dyn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.psn.model.info.PsnInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.consts.SmateShareConstant;
import com.smate.web.mobile.share.service.FindResInfoService;
import com.smate.web.mobile.share.service.ShareToSmateService;
import com.smate.web.mobile.share.vo.SmateShareVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.MobileGrpFileApiConsts;

/**
 * 移动端资源分享控制器
 * 
 * @author wsn
 * @date May 23, 2019
 */
@Controller
public class MobileShareResController {

  private static final Logger logger = LoggerFactory.getLogger(MobileDynShareController.class);
  @Value("${domainMobile}")
  private String domainMobile;
  @Value("${domainscm}")
  private String domainScm;
  private Map<String, ShareToSmateService> shareServiceMap;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private ShareToSmateService shareResToDynService;
  @Autowired
  private ShareToSmateService shareResToFriendService;
  @Autowired
  private ShareToSmateService shareResToGrpService;
  @Autowired
  private FindResInfoService findResInfoService;


  /**
   * 移动端资源分享入口 "/psn/outside/share/res",
   * 
   * @param vo
   * @return
   */
  @RequestMapping(value = "/psn/res/ajaxshare", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object shareResToSmate(SmateShareVO vo) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> result = new HashMap<String, Object>();
    String status = "error";
    try {
      // 需要分享到的模块
      if (StringUtils.isNotBlank(vo.getShareTo()) && shareServiceMap.containsKey(vo.getShareTo())
          && NumberUtils.isNotNullOrZero(psnId)) {
        vo.setDomainMobile(domainMobile);
        vo.setDes3CurrentPsnId(Des3Utils.encodeToDes3(psnId.toString()));
        result = shareServiceMap.get(vo.getShareTo()).doShare(vo);
        status = "success";
      } else {
        vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_CHECK_FALSE);
        vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_CHECK_FALSE);
      }
    } catch (Exception e) {
      logger.error("移动端分享资源异常，psnId={}, resId={}, resType={}, shareTo={}, grpId={}, friendId={}, shareText={}", psnId,
          vo.getDes3ResId(), vo.getResType(), vo.getShareTo(), vo.getDes3GrpId(), vo.getDes3FriendIds(),
          vo.getShareText(), e);
      vo.setErrorCode(SmateShareConstant.SHARE_ERROR_CODE_INTERFACE_ERROR);
      vo.setErrorMsg(SmateShareConstant.SHARE_ERROR_MSG_INTERFACE_ERROR);
    }
    result.put("result", status);
    result.put("error_code", vo.getErrorCode());
    result.put("error_msg", vo.getErrorMsg());
    result.put("warn_code", vo.getWarnCode());
    result.put("warn_msg", vo.getWarnMsg());
    return result;
  }



  /**
   * 移动端分享页面入口
   * 
   * 分享页面逻辑：
   * 
   * 1.页面加载完后，调用js方法loadEditPage，虽然看着是form，但是其实是转成用ajax方式提交了 ，加载了真正的分享编辑页面然后放到页面中显示
   * 
   * 2.进入选择好友或群组页面，也是用的ajax方式加载的，虽然看着是form提交
   * 
   * 3.选好好友或群组后，确定或取消按钮都是用的form转ajax提交回到编辑页面
   * 
   * 用ajax是为了分享完后能比较方便的回到分享前的页面
   * 
   * @return
   */
  @RequestMapping(value = "/psn/share/page")
  public ModelAndView mobileResShare(SmateShareVO vo) {
    ModelAndView modelAndView = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (StringUtils.isNotBlank(vo.getResType()) && StringUtils.isNotBlank(vo.getDes3ResId())
          && NumberUtils.isNotNullOrZero(psnId)) {
        vo.setDomainMobile(domainMobile);
        vo.setDomainScm(domainScm);
        vo.setShareTo(StringUtils.isNotBlank(vo.getShareTo()) ? vo.getShareTo() : "friend");
        vo.setDes3CurrentPsnId(Des3Utils.encodeToDes3(psnId.toString()));
        doHideModule(vo);
      }
    } catch (Exception e) {
      logger.error("进入分享页面异常,des3ResId={},shareTo={}", vo.getDes3ResId(), vo.getShareTo(), e);
    }
    modelAndView.addObject("vo", vo);
    modelAndView.setViewName("resshare/mobile_share_res_main");
    return modelAndView;
  }

  /**
   * 处理现实分享模块
   */
  private void doHideModule(SmateShareVO vo) {
    String resType = vo.getResType();
    switch (resType) {
      case "grpfile":// 群组文件不显示分享到个人动态
        vo.setHideModule(1);
        break;
      default:
        break;
    }
  }

  /**
   * 移动端分享编辑页面
   * 
   * @return
   */
  @RequestMapping(value = "/psn/share/edit")
  public ModelAndView mobileResShareEditPage(SmateShareVO vo) {
    ModelAndView modelAndView = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (StringUtils.isNotBlank(vo.getResType()) && StringUtils.isNotBlank(vo.getDes3ResId())
          && NumberUtils.isNotNullOrZero(psnId)) {
        vo.setDomainMobile(domainMobile);
        vo.setDomainScm(domainScm);
        vo.setShareTo(StringUtils.isNotBlank(vo.getShareTo()) ? vo.getShareTo() : "friend");
        vo.setDes3CurrentPsnId(Des3Utils.encodeToDes3(psnId.toString()));
        findResInfoService.findResInfo(vo);
      }
    } catch (Exception e) {
      logger.error("进入分享页面异常,des3ResId={},shareTo={}", vo.getDes3ResId(), vo.getShareTo(), e);
    }
    modelAndView.addObject("vo", vo);
    modelAndView.setViewName("resshare/mobile_share_edit_page");
    return modelAndView;
  }

  /**
   * 获取能分享的好友列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/share/ajaxfriends", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public ModelAndView findShareFriendList(SmateShareVO vo) {
    ModelAndView modelAndView = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      params.add("psnId", Objects.toString(psnId, ""));
      Map<String, Object> psnResult =
          restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_SHARE_FRIEND,
              RestUtils.buildPostRequestEntity(params), Map.class);
      if (psnResult != null && "200".equalsIgnoreCase(Objects.toString(psnResult.get("status"), ""))) {
        modelAndView.addObject("friendList", psnResult.get("result"));
      }
    } catch (Exception e) {
      logger.error("获取能分享的好友信息异常， psnId={}", psnId, e);
    }
    modelAndView.addObject("vo", vo);
    modelAndView.setViewName("resshare/mobile_may_share_friends");
    return modelAndView;
  }



  /**
   * 获取选中的待分享的群组信息
   * 
   * @param vo
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/share/ajaxgrpinfo", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object findSelectedGrpInfo(SmateShareVO vo) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(vo.getDes3GrpId())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", vo.getDes3GrpId());
        result = restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_SHARE_GRP_INFO,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端分享页面获取群组信息异常， psnId={}, grpId", psnId, vo.getDes3GrpId(), e);
      result.put("status", "500");
    }
    return result;
  }


  /**
   * 进入群组选择页面
   * 
   * @param groupFile
   * @return
   */
  @RequestMapping(value = "/psn/share/choosegrp")
  public ModelAndView chooseGrp(SmateShareVO vo) {
    ModelAndView view = new ModelAndView();
    view.addObject("vo", vo);
    view.setViewName("/resshare/mobile_share_choose_grp");
    return view;
  }



  /**
   * 获取群组列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/share/ajaxgrplist")
  public ModelAndView showGrpList(SmateShareVO vo) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("searchKey", vo.getSearchKey());
        params.add("pageNo", vo.getPage().getPageNo().toString());
        Map<String, Object> grpResult = restTemplate.postForObject(domainMobile + SmateShareConstant.SHARE_GRP_LIST,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if (grpResult != null && "success".equals(grpResult.get("status"))) {
          view.addObject("grpInfos", grpResult.get("grpInfos"));
          view.addObject("totalCount", grpResult.get("totalCount"));
        }
      }
    } catch (Exception e) {
      logger.error("获取待选择群组列表异常,psnId={}", psnId, e);
    }
    view.setViewName("/resshare/mobile_share_choose_grp_list");
    return view;
  }



  /**
   * 进入好友选择页面
   * 
   * @return
   */
  @RequestMapping(value = "/psn/share/choosefriend")
  public ModelAndView chooseFriend(SmateShareVO vo) {
    ModelAndView view = new ModelAndView();
    view.addObject("vo", vo);
    view.setViewName("/resshare/mobile_share_choose_contact");
    return view;
  }



  /**
   * 获取好友列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/psn/share/ajaxfriendlist")
  public ModelAndView showFriendList(SmateShareVO vo) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("psnId", psnId.toString());
        params.add("searchKey", vo.getSearchKey());
        params.add("page.pageNo", vo.getPage().getPageNo().toString());
        Map<String, Object> psnResult =
            restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_SHARE_FRIEND,
                RestUtils.buildPostRequestEntity(params), Map.class);
        List<PsnInfo> psnInfos = psnResult.get("result") != null ? (List<PsnInfo>) psnResult.get("result") : null;
        view.addObject("psnInfos", psnInfos);
      }
    } catch (Exception e) {
      logger.error("移动端分享进入选择联系人页面异常,psnId={}", psnId, e);
    }
    view.setViewName("/resshare/mobile_share_choose_contact_list");
    return view;
  }



  /**
   * 执行每个RequestMapping之前都会先执行这个
   */
  @ModelAttribute
  private void initShareServiceMap() {
    if (shareServiceMap == null) {
      shareServiceMap = new HashMap<String, ShareToSmateService>();
      shareServiceMap.put(SmateShareConstant.SHARE_TO_DYNAMIC, shareResToDynService);
      shareServiceMap.put(SmateShareConstant.SHARE_TO_FRIEND, shareResToFriendService);
      shareServiceMap.put(SmateShareConstant.SHARE_TO_GROUP, shareResToGrpService);
    }
  }
}
