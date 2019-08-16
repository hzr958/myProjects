package com.smate.web.mobile.controller.web.psn;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.psn.model.info.PsnInfo;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.group.vo.MobileGroupFileVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.MobileGrpFileApiConsts;
import com.smate.web.mobile.v8pub.enums.MobileResShareEnums;

/**
 * @description 移动端资源分享至群组/联系人控制器
 * @author xiexing
 * @date 2019年5月15日
 */
@Controller
public class MobilePsnShareController {
  private static final Logger logger = LoggerFactory.getLogger(MobilePsnShareController.class);

  @Value("${domainMobile}")
  private String domainMobile;

  @Autowired
  private RestTemplate restTemplate;

  /**
   * 移动端资源分享至群组/联系人跳转页
   * 
   * @return
   */
  @RequestMapping(value = "/psn/mobile/resshare")
  public ModelAndView resShare(MobileGroupFileVO groupFile) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    String des3ResId = groupFile.getDes3ResId();
    String resType = groupFile.getResType();
    if (verifyParam(des3ResId, resType, psnId)) {
      // 为了方便统计返回,所以统一为first,second...进行返回,具体请参照当前分享资源的返回值
      String first = "";
      String second = "";
      String third = "";
      String fourth = "";
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        if (MobileResShareEnums.GRPFILE.getValue().equalsIgnoreCase(resType)
            || MobileResShareEnums.GRPCOURSEFILE.getValue().equalsIgnoreCase(resType)
            || MobileResShareEnums.GRPWORKFILE.getValue().equalsIgnoreCase(resType)) {
          params.add("des3GrpFileId", des3ResId);
          // 获取文件详情
          Map<String, Object> resultFile = getReqResult(params, MobileGrpFileApiConsts.QUERY_FILE_DETAIL);
          first = Objects.toString(resultFile.get("fileName"), "");
          second = Objects.toString(resultFile.get("fileDesc"), "");
          third = Objects.toString(resultFile.get("fileType"), "");
          fourth = Objects.toString(resultFile.get("imgThumbUrl"), "");
          // 获取联系人
          params.clear();
          params.add("psnId", psnId.toString());
          Map<String, Object> resultPsn = getReqResult(params, MobileGrpFileApiConsts.QUERY_SHARE_FRIEND);
          List<PsnInfo> psnInfos = resultPsn.get("result") != null ? (List<PsnInfo>) resultPsn.get("result") : null;
          // 可能支持多群组分享,所以此处暂时使用des3GrpIds
          if (StringUtils.isNotEmpty(groupFile.getDes3GrpIds())) {
            params.clear();
            params.add("des3GrpId", groupFile.getDes3GrpIds());
            Map<String, Object> resultGrp = getReqResult(params, MobileGrpFileApiConsts.QUERY_SHARE_GRP_INFO);
            view.addObject("grpInfo", resultGrp.get("grp"));
          }
          view.addObject("psnInfos", psnInfos);
        }
        view.addObject("first", HtmlUtils.htmlUnescape(first));
        view.addObject("second", HtmlUtils.htmlUnescape(second));
        view.addObject("third", third);
        view.addObject("fourth", fourth);
        view.addObject("groupFile", groupFile);
        view.addObject("status", "success");
        view.setViewName("/group/file/mobile_grp_file_share");
      } catch (Exception e) {
        logger.error("进入分享页出错,des3ResId={}", des3ResId, e);
        view.addObject("status", "error");
      }
    } else {
      view.addObject("status", "error");
    }
    return view;
  }



  /**
   * 进入好友选择页面
   * 
   * @return
   */
  @RequestMapping(value = "/psn/mobile/choosefriend")
  public ModelAndView chooseFriend(MobileGroupFileVO groupFile) {
    ModelAndView view = new ModelAndView();
    view.addObject("groupFile", groupFile);
    view.setViewName("/group/file/mobile_grp_file_share_contact");
    return view;
  }

  /**
   * 进入群组选择页面
   * 
   * @param groupFile
   * @return
   */
  @RequestMapping(value = "/psn/mobile/choosegrp")
  public ModelAndView chooseGrp(MobileGroupFileVO groupFile) {
    ModelAndView view = new ModelAndView();
    view.addObject("groupFile", groupFile);
    view.setViewName("/group/file/mobile_grp_file_share_grp");
    return view;
  }

  /**
   * 群组文件分享-进入选择联系人
   * 
   * @return
   */
  @RequestMapping(value = "/psn/mobile/showfriendlist")
  public ModelAndView showFriendList(MobileGroupFileVO groupFile) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    if (NumberUtils.isNotNullOrZero(psnId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("psnId", psnId.toString());
        params.add("searchKey", groupFile.getSearchKey());
        params.add("page.pageNo", groupFile.getPage().getPageNo().toString());
        Map<String, Object> resultPsn = getReqResult(params, MobileGrpFileApiConsts.QUERY_SHARE_FRIEND);
        List<PsnInfo> psnInfos = resultPsn.get("result") != null ? (List<PsnInfo>) resultPsn.get("result") : null;
        view.addObject("psnInfos", psnInfos);
        view.addObject("status", "success");
      } catch (Exception e) {
        logger.error("进入选择联系人出错,psnId={}", psnId, e);
        view.addObject("status", "error");
      }
    } else {
      view.addObject("status", "error");
    }
    view.setViewName("/group/file/mobile_grp_file_friend_list");
    return view;
  }

  /**
   * 群组文件分享-进入选择群组
   * 
   * @return
   */
  @RequestMapping(value = "/psn/mobile/showgrplist")
  public ModelAndView showGrpList(MobileGroupFileVO groupFile) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    if (NumberUtils.isNotNullOrZero(psnId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("psnId", psnId.toString());
        params.add("searchKey", groupFile.getSearchKey());
        params.add("pageNo", groupFile.getPage().getPageNo().toString());
        Map<String, Object> resultGrp = getReqResult(params, MobileGrpFileApiConsts.QUERY_SHARE_GRP_LIST);
        if (resultGrp != null && resultGrp.get("results") != null) {
          HashMap<String, Object> resultMap = (HashMap<String, Object>) resultGrp.get("results");
          view.addObject("grpInfos", resultMap.get("commentlist"));
          view.addObject("totalCount", resultMap.get("total"));
          view.addObject("status", "success");
        }
      } catch (Exception e) {
        logger.error("进入选择联系人出错,psnId={}", psnId, e);
        view.addObject("status", "error");
      }
    } else {
      view.addObject("status", "error");
    }
    view.setViewName("/group/file/mobile_grp_file_grp_list");
    return view;
  }

  /**
   * 发送请求并返回结果
   * 
   * @param params
   * @param shareType
   * @param restUrl
   * @return
   */
  public Map<String, Object> getReqResult(MultiValueMap<String, String> params, String restUrl) {
    if (params.size() <= 0 || StringUtils.isEmpty(restUrl)) {
      return null;
    }
    Map<String, Object> result =
        restTemplate.postForObject(domainMobile + restUrl, RestUtils.buildPostRequestEntity(params), Map.class);
    return result;
  }

  /**
   * 参数校验
   * 
   * @param des3ResId
   * @param resType
   * @return
   */
  public boolean verifyParam(String des3ResId, String resType, Long psnId) {
    if (StringUtils.isNotEmpty(des3ResId) && StringUtils.isNotEmpty(resType) && NumberUtils.isNotNullOrZero(psnId)) {
      return true;
    }
    return false;
  }
}
