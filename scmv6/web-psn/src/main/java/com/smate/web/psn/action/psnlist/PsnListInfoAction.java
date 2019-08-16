package com.smate.web.psn.action.psnlist;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;

/**
 * 人员列表Action
 *
 * @author wsn
 * @createTime 2017年3月25日 下午5:06:51
 *
 */
@Results({@Result(name = "psnList", location = "/WEB-INF/jsp/pc/psnlist/pc_psn_list.jsp"),
    @Result(name = "sendFriendReq", location = "/WEB-INF/jsp/friend/friendrequest/send_addfriend_list.jsp"),
    @Result(name = "sendFriendReqMore", location = "/WEB-INF/jsp/friend/friendrequest/send_addfriend_list_more.jsp"),
    @Result(name = "req_addfriend_list", location = "/WEB-INF/jsp/friend/friendrequest/req_addfriend_list.jsp"),
    @Result(name = "req_addfriend_list_more",
        location = "/WEB-INF/jsp/friend/friendrequest/req_addfriend_list_more.jsp"),
    @Result(name = "req_addmobilefriend_list",
        location = "/WEB-INF/jsp/mobile/friend/mobile_friend_addrequest_list.jsp"),
    @Result(name = "home_friend", location = "/WEB-INF/jsp/friend/friendrequest/home_friend.jsp"),
    @Result(name = "home_friend_list", location = "/WEB-INF/jsp/friend/friendrequest/home_friend_list.jsp")})
public class PsnListInfoAction extends ActionSupport implements ModelDriven<PsnListViewForm>, Preparable {

  private static final long serialVersionUID = -4642238256971146771L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PsnListViewForm form;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;
  @Autowired
  private FriendService friendService;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnListViewForm();
    }
  }

  @Override
  public PsnListViewForm getModel() {
    return form;
  }

  @Action("/psnweb/psnlist/ajaxshow")
  public String getPsnListInfo() {
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      psnListViewService.getPsnListViewInfo(form);
    } catch (Exception e) {
      logger.error("查看人员列表出错， 人员列表类型serviceType=" + form.getServiceType(), e);
    }
    // 以后若要返回不同的人员列表页面，可以加参数进行区分
    return "psnList";
  }

  /**
   * 已经发送的未被处理的好友请求列表
   * 
   * @return
   */
  @Action("/psnweb/sendfriendreq/ajaxlist")
  public String ajaxSendFriendReq() {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (psnId != null && psnId > 0l) {
        form.setPsnId(psnId);
        List<Long> receiverIds = friendService.findSendFriendReqHistory(form);
        if (CollectionUtils.isNotEmpty(receiverIds)) {
          form.setPsnIds(receiverIds);
          form.setServiceType("common");
          psnListViewService.getPsnListViewInfo(form);
        }
      }
    } catch (Exception e) {
      logger.error("查询未被处理的历史好友请求列表失败" + psnId, e);
    }
    if (form.getIsAll() == 1) {
      return "sendFriendReqMore";
    }
    return "sendFriendReq";
  }

  /**
   * 好友请求列表
   */
  @Actions({@Action("/psnweb/friendreq/ajaxlist"), @Action("/psnweb/mobile/friendreq")})
  public String friendReq() {
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      List<Long> reqPsnIds = friendService.showFriendRequest(form);
      if (CollectionUtils.isNotEmpty(friendService.findSendFriendReqHistory(form))) {
        form.setHasSendReqList(true);
      }
      if (CollectionUtils.isNotEmpty(reqPsnIds)) {
        form.setPsnIds(reqPsnIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
      }
    } catch (Exception e) {
      logger.error("好友请求列表,出错,psnId=" + form.getPsnId(), e);
    }
    if (true == form.getMobile()) {
      return "req_addmobilefriend_list";
    } else {
      return "req_addfriend_list";
    }
  }

  /**
   * 新-好友请求列表
   */
  @Action("/psnweb/friendreq/ajaxnewlist")
  public String newFriendReq() {
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      } else {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      List<Long> reqPsnIds = friendService.newFriendRequest(form);
      if (CollectionUtils.isNotEmpty(friendService.findSendFriendReqHistory(form))) {
        form.setHasSendReqList(true);
      }
      if (CollectionUtils.isNotEmpty(reqPsnIds)) {
        form.setPsnIds(reqPsnIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
      }
    } catch (Exception e) {
      logger.error("好友请求列表,出错,psnId=" + form.getPsnId(), e);
    }
    return "req_addfriend_list";
  }

  /**
   * 首页-显示好友请求模块
   * 
   * @author zzx
   * @return
   */
  @Action("/psnweb/mainmodule/ajaxgetfriendreq")
  public String getTomoduleFriendReq() {
    form.setPsnId(SecurityUtils.getCurrentUserId());
    try {
      Long userId = SecurityUtils.getCurrentUserId();
      if (userId == null || userId == 0L) {
        return null;
      }
      form.setPsnId(userId);
      friendService.queryFriendsReq(form);
      if (CollectionUtils.isEmpty(form.getPsnInfoList())) {
        Struts2Utils.getRequest().setAttribute("randomModule", "no");
      }
    } catch (Exception e) {
      logger.error("首页-显示好友请求模块出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 1) {
      return "home_friend_list";
    }
    return "home_friend";
  }

}
