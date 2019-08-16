package com.smate.web.psn.action.friend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.service.friend.FriendMailService;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.message.InboxService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;

/**
 * 个人好友Action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "recommend_main", location = "/WEB-INF/jsp/friend/recommendfriend/recommend_main.jsp"),
    @Result(name = "recommend_list", location = "/WEB-INF/jsp/friend/recommendfriend/recommend_list.jsp"),
    @Result(name = "friend_main", location = "/WEB-INF/jsp/friend/myfriend/friend_main.jsp"),
    @Result(name = "msg_chat_friend_list", location = "/WEB-INF/jsp/friend/msg_chat_friend_list.jsp"),
    @Result(name = "msg_chat_friend_list2", location = "/WEB-INF/jsp/friend/msg_chat_friend_list2.jsp"),
    @Result(name = "mobile_msg_chat_friend_list", location = "/WEB-INF/jsp/friend/mobile_msg_chat_friend_list.jsp"),
    @Result(name = "vist_psn_list", location = "/WEB-INF/jsp/friend/vist_psn_list.jsp"),
    @Result(name = "vist_psn_list_all", location = "/WEB-INF/jsp/friend/vist_psn_list_all.jsp"),
    @Result(name = "friend_list", location = "/WEB-INF/jsp/friend/myfriend/friend_list.jsp"),
    @Result(name = "recommended", location = "/WEB-INF/jsp/psnprofile/recommended_psn.jsp"),
    @Result(name = "recommended_psn_sub", location = "/WEB-INF/jsp/psnprofile/recommended_psn_sub.jsp"),
    @Result(name = "recommended_psn_more", location = "/WEB-INF/jsp/psnprofile/recommended_psn_more_sub.jsp")})

public class MyFriendAction extends ActionSupport implements ModelDriven<PsnListViewForm>, Preparable {

  private static final long serialVersionUID = -914649027219453039L;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final String FRIEND_CONFIG_CACHE = "sns_friend_config_cache";
  private static final String KNOWLIST = "knowList";
  // 缓存推荐人员id
  static final String RECOMMEND_PSN_CACHE = "recommend_psn_cache";
  // 缓存推荐人员id的key
  static final String RECOMMEND_PSNIDS = "recommend_psnids";
  private PsnListViewForm form;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private FriendService friendService;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private InboxService inboxService;

  /**
   * 消息中心-站内信-好友/全站人员列表
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxgetchatfriendlist")
  public String showMsgChatPsnList() {
    try {
      friendService.getMsgChatPsnList(form);
    } catch (Exception e) {
      logger.error("消息中心-站内信-好友/全站人员列表出错,form= " + form, e);
    }
    if (1 == form.getSearchType()) {
      return "msg_chat_friend_list2";
    } else {
      return "msg_chat_friend_list";
    }
  }

  /**
   * 消息中心-站内信-好友/全站人员列表
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxgetchatfriendlist")
  public String showMidMsgChatPsnList() {
    try {
      friendService.getMsgChatPsnList(form);
    } catch (Exception e) {
      logger.error("消息中心-站内信-好友/全站人员列表出错,form= " + form, e);
    }
    return "mobile_msg_chat_friend_list";
  }

  /**
   * 我的-好友推荐主页
   * 
   * @return
   */
  @Action("/psnweb/friend/main")
  public String showMain() {
    if (StringUtils.isBlank(form.getModule())) {
      form.setModule("rec");
    }
    return "recommend_main";
  }

  /**
   * 我的好友
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxmyfriend")
  public String myFriendMain() {
    return "friend_main";
  }

  /**
   * 个人好友列表
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxlist")
  public String showMyFriend() {
    try {
      buildForm(form);
      List<Long> friendIds = friendService.getFriendIds(form);
      if (CollectionUtils.isNotEmpty(friendIds)) {
        form.setPsnIds(friendIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
      }
    } catch (Exception e) {
      logger.error("个人好友列表,出了错,psnId= " + form.getPsnId(), e);
    }
    return "friend_list";
  }

  /**
   * 好友按地区分组统计(统计数最多的前5个)
   * 
   * @return
   */
  // @Action("/psnweb/friend/ajaxreg")
  // public String sortMyFriendByReg() {
  // try {
  // buildForm(form);
  // friendService.sortFriendByReg(form);
  // Struts2Utils.renderJson(JacksonUtils.listToJsonStr(form.getRegionList()),
  // "encoding:UTF-8");
  // } catch (Exception e) {
  // logger.error("好友按地区分组统计,出了错,psnId= " + form.getPsnId(), e);
  // }
  // return null;
  // }

  /**
   * 推荐列表机构分组(统计数最多的前5个)
   */
  @Action("/psnweb/recommend/ajaxins")
  public String sortRecommendPsnByIns() {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Map<String, String>> insTution = new ArrayList<Map<String, String>>();
    String jsonStr = null;
    if (psnId != null) {
      try {
        insTution = personManager.findInsNames(psnId);
      } catch (Exception e) {
        logger.error("查询地区出错,psnId= " + psnId, e);
      }
      jsonStr = JacksonUtils.jsonListSerializer(insTution);
    }
    Struts2Utils.renderJson(jsonStr, "encoding:UTF-8");

    return null;
  }

  /**
   * 推荐列表地区
   */
  @Action("/psnweb/recommend/ajaxreg")
  public String sortRecommendPsnByReg() {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Map<String, String>> insTution = new ArrayList<Map<String, String>>();
    String jsonStr = null;
    if (psnId != null) {
      try {
        insTution = personManager.findRegNames(psnId);
        jsonStr = JacksonUtils.jsonListSerializer(insTution);
      } catch (Exception e) {
        logger.error("查询地区出错,psnId= " + psnId, e);
      }
      jsonStr = JacksonUtils.jsonListSerializer(insTution);
    }
    Struts2Utils.renderJson(jsonStr, "encoding:UTF-8");
    return null;
  }

  /**
   * 根据psnId获取机构排序
   * 
   * @param form
   */
  @Action("/psnweb/friend/ajaxins")
  public String sortMyFriendByIns() {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Long> friendIds = new ArrayList<Long>();
    String jsonStr = null;
    List<Map<String, String>> insTution = new ArrayList<Map<String, String>>();
    try {
      friendIds = friendService.getFriendListByPsnId(psnId);
      if (CollectionUtils.isNotEmpty(friendIds)) {
        insTution = personManager.findInsNamesBypsnIds(friendIds);
      }
      jsonStr = JacksonUtils.jsonListSerializer(insTution);
    } catch (Exception e) {
      logger.error("查询ins机构出错,psnId= " + psnId, e);
    }
    Struts2Utils.renderJson(jsonStr, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除好友
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxdel")
  public String ajaxDelFriend() {
    Map<String, String> map = new HashMap<String, String>();
    String friendPsnIds = Struts2Utils.getParameter("friendPsnIds");
    try {
      if (StringUtils.isNotBlank(friendPsnIds)) {
        friendService.delFriendByPsnIds(friendPsnIds);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("删除好友出错, friendIds=" + friendPsnIds, e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 好友列表数据回显
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxfriendlistcallback")
  public String friendlistcallback() {
    try {
      buildForm(form);
      Map<String, Object> callBackMap = friendService.getFriendsCallBack(form);
      Struts2Utils.renderJson(callBackMap, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("好友列表数据回显,出错,psnId= " + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 可能认识的人数据回显
   * 
   * @return
   */
  @Action("/psnweb/recommend/ajaxfriendlistcallback")
  public String recommendlistcallback() {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Long> recommendIds = new ArrayList<Long>();
    String jsonStr = null;
    try {
      recommendIds = getRecommendPsnCache();
      if (CollectionUtils.isEmpty(recommendIds)) {
        recommendIds = friendService.getRecommendPsnListByPsnId(psnId);
        if (CollectionUtils.isNotEmpty(recommendIds)) {
          putRecommendPsnCache(recommendIds);

        }
      }
      // 查询数据回显
      jsonStr = personManager.getRecommendInsReg(recommendIds, form);
      Struts2Utils.renderJson(jsonStr, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("推荐列表数据回显,出错,psnId= " + psnId, e);
    }
    return null;
  }

  /**
   * 我的-个人主页-推荐人员列表
   * 
   * @return
   */
  @Action("/psnweb/homepage/ajaxrecommendpsn")
  public String homepageRecommendPsn() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      List<Long> knowList = this.getRecommendPsnCache();
      Page page = form.getPage();
      if (CollectionUtils.isEmpty(knowList)) {
        if (CollectionUtils.isEmpty(knowList)) {
          knowList = friendService.getMobileRecommendPsnList(psnId);
          if (CollectionUtils.isNotEmpty(knowList)) {
            putRecommendPsnCache(knowList);
          }
        }

      }
      if (CollectionUtils.isNotEmpty(knowList)) {

        // 对可能认识的人进行分页操作
        List<Long> psnIds = friendService.getMobileRecommendPsn(page.getPageNo(), knowList);
        if (form.getIsAll() == 0 && psnIds.size() > 3) {
          psnIds = psnIds.subList(0, 3);// 取三个
        }
        form.setPsnIds(psnIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
        page.setTotalCount(knowList.size());
        page.setResult(form.getPsnInfoList());
      }

      /*
       * buildForm(form); List<Long> psnIds = friendService.recommendPsn(form); List<Long> recommendpsnIds
       * = new ArrayList<Long>(); if (CollectionUtils.isNotEmpty(psnIds)) { if (form.getIsAll() == 0) {
       * bulidRecommendPsn(psnIds); form.setServiceType("common");
       * psnListViewService.getPsnListViewInfo(form); } else { form.setPsnIds(psnIds);
       * form.setServiceType("common"); psnListViewService.getPsnListViewInfo(form); } } else {
       * form.getPage().setTotalCount(0); }
       */
    } catch (Exception e) {
      logger.error("我的-好友推荐-推荐人员列表,出错,psnId= " + form.getPsnId(), e);
    }
    if (form.getIsAll() == 0) {
      return "recommended_psn_sub";
    } else {
      return "recommended_psn_more";
    }
  }

  /**
   * 我的-个人主页-推荐人员列表
   * 
   * @return
   */
  @Action("/psnweb/homepage/recommendpsn")
  public String showRecommendPsn() {
    return "recommended";
  }

  /**
   * 处理下显示推荐人员
   */
  public void bulidRecommendPsn(List<Long> psnIds) {
    List<Long> recommendpsnIds = new ArrayList<Long>();
    if (psnIds.size() > 3) {
      recommendpsnIds.add(psnIds.get(0));
      recommendpsnIds.add(psnIds.get(1));
      recommendpsnIds.add(psnIds.get(2));
      form.setPsnIds(recommendpsnIds);
    } else {
      form.setPsnIds(psnIds);
    }
  }

  /**
   * 我的-好友推荐-推荐人员列表
   * 
   * @return
   */
  @Action("/psnweb/recommend/ajaxrecommendpsn")
  public String recommendPsn() {
    try {
      buildForm(form);
      List<Long> psnIds = friendService.recommendPsn(form);
      if (CollectionUtils.isNotEmpty(psnIds)) {
        form.setPsnIds(psnIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
      } else {
        form.getPage().setTotalCount(0);
      }
    } catch (Exception e) {
      logger.error("我的-好友推荐-推荐人员列表,出错,psnId= " + form.getPsnId(), e);
    }
    return "recommend_list";
  }

  /**
   * 我的-好友推荐-推荐人员列表
   * 
   * @return
   */
  @Action("/psnweb/recommend/ajaxdatarecommendpsn")
  public void getRecommendPsn() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      buildForm(form);
      List<Long> psnIds = friendService.recommendPsn(form);
      if (CollectionUtils.isNotEmpty(psnIds)) {
        form.setPsnIds(psnIds);
        form.setServiceType("common");
        psnListViewService.getPsnListViewInfo(form);
      }
      // 获取数据
      result.put("psnInfoList", form.getPsnInfoList());
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "error");
      result.put("psnInfoList", null);
      logger.error("好友推荐-推荐人员列表,出错,psnId= " + form.getPsnId(), e);
    }
    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  /**
   * 我的-好友推荐-研究领域(查询的是个人的研究领域)
   * 
   * @return
   */
  @Action("/psnweb/recommend/ajaxsciencearea")
  public String ajaxmyScienceArea() {
    try {
      buildForm(form);
      scienceAreaService.myScienceArea(form);
      Struts2Utils.renderJson(JacksonUtils.listToJsonStr(form.getScienceAreaList()), "encoding:utf-8");
    } catch (Exception e) {
      logger.error("我的-好友推荐-研究领域(查询的是个人的研究领域),出错了,psnId= " + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 同意好友请求
   * 
   * @return
   */
  @Action("/psnweb/friendreq/ajaxaccept")
  public String acceptAddFriendRequest() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      buildForm(form);
      // 校验请求人员ID
      String check = this.checkReqPsnIds(form);
      if (StringUtils.isNotBlank(check)) {
        data.put("result", check);
      } else {
        Long currentPsnId = SecurityUtils.getCurrentUserId();
        // 处理接受请求
        for (Long psnId : form.getPsnIds()) {
          String result = friendService.acceptAddFriendRequest(psnId, currentPsnId);
          if (!"success".equals(result)) {
            data.put("result", result);
            Struts2Utils.renderJson(data, "encoding:UTF-8");
            return null;
          }
        }
        data.put("result", "success");
      }
    } catch (Exception e) {
      logger.error("接受添加好友请求出错， reqPsnIds = " + form.getDes3ReqPsnIds() + ", currentPsnId" + form.getPsnId(), e);
      data.put("result", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  @Action("/psnweb/friendreq/invitation")
  public void outsideInvitation() {

    String invitCode = Struts2Utils.getRequest().getParameter("invitationCode");
    Long invitPsnId = Long.parseLong(Des3Utils.decodeFromDes3(invitCode.split("-")[4]));

  }

  /**
   * 站外好友邀请，点击链接自动添加好友
   */
  @Action("/psnweb/friendreq/addfriend")
  public void autoAddFriendRequest() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    String invitCode = Struts2Utils.getRequest().getParameter("invitationCode");
    String locale = Struts2Utils.getRequest().getParameter("locale");
    Long invitPsnId = Long.parseLong(Des3Utils.decodeFromDes3(invitCode.split("-")[2]));
    try {
      if (com.smate.core.base.utils.number.NumberUtils.isNotZero(currentUserId)) {// 已登录
        // 当前登录的id不等于邀请人的id时才自动加为好友
        if (currentUserId.longValue() != invitPsnId) {
          friendService.autoAddFriend(invitPsnId);
        }
        // 跳转个人好友列表
        Struts2Utils.getResponse().sendRedirect("/psnweb/friend/main?module=myf&locale=" + locale);
      } else {// 未登录，跳转首页
        String service = Des3Utils.encodeToDes3(Struts2Utils.getRequest().getRequestURL().toString());
        Struts2Utils.getResponse().sendRedirect("/oauth/mobile/index?service=" + service);
      }
    } catch (ServiceException e) {
      logger.error("自动添加好友请求出错， reqPsnIds = " + form.getDes3ReqPsnIds() + ", currentPsnId" + form.getPsnId(), e);
    } catch (IOException e) {
      logger.error("跳转页面失败", e);
    }
  }

  /**
   * 同意好友请求------校验人员ID是否为空且可解密
   * 
   * @param form
   * @return
   */
  private String checkReqPsnIds(PsnListViewForm form) {
    String result = "";
    if (StringUtils.isBlank(form.getDes3ReqPsnIds())) {
      result = "request psnId is null";
      return result;
    }
    String[] reqPsnIds = form.getDes3ReqPsnIds().split(",");
    if (reqPsnIds == null || reqPsnIds.length == 0) {
      result = "request psnId is null";
      return result;
    }
    List<Long> psnIds = new ArrayList<Long>();
    for (String id : reqPsnIds) {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(id));
      if (psnId != null && !psnId.equals(0L)) {
        psnIds.add(psnId);
      } else {
        result = "decode psnId error";
        return result;
      }
    }
    form.setPsnIds(psnIds);
    return result;
  }

  /**
   * 我的-好友推荐列表-移除
   * 
   * @return
   */
  @Actions({@Action("/psnweb/recommend/ajaxremove"), @Action("/psnweb/mobile/ajaxremove")})
  public String ajaxremove() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getTempPsnId() != null && form.getTempPsnId() > 0l) {
        buildForm(form);
        friendService.addFriendTempSys(form);
        removeRecommendPsnCache();
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
      Struts2Utils.renderJson(map, "encoding:utf-8");
    } catch (Exception e) {
      logger.error("我的-好友推荐列表-移除,出错,psnId= " + form.getPsnId(), e);
    }
    return null;

  }

  /**
   * 取消发送
   * 
   * @return
   */
  @Action("/psnweb/friendreq/ajaxremove")
  public String removeAddFriendReq() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      friendService.removeAddFriendReq(form.getTempPsnId());
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("发送添加好友列表,取消好友发送失败", e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 好友请求列表-忽略操作
   * 
   * @return
   */
  @Action("/psnweb/friendreq/ajaxneglet")
  public String ajaxNeglet() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getTempPsnId() != null && form.getTempPsnId() > 0l) {
        buildForm(form);
        friendService.negletFriendReq(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
      Struts2Utils.renderJson(map, "encoding:utf-8");
    } catch (Exception e) {
      logger.error("好友请求列表-忽略操作,出错,psnId= " + form.getPsnId(), e);
    }
    return null;

  }

  /**
   * 获取最近来访人员列表
   * 
   * @return
   */
  @Action("/psnweb/friendreq/ajaxvistpsnlist")
  public String getVistStatisticsPsnList() {
    try {
      friendService.getVistStatisticsPsnList(form);
    } catch (Exception e) {
      logger.error("获取最近来访人员列表出错", e);
    }
    // 查看全部
    if (form.getIsAll() == 1) {
      return "vist_psn_list_all";
    } else {
      return "vist_psn_list";
    }
  }

  /**
   * 为第一次登录或者关注人数少于5人的用户自动关注
   * 
   * @return
   */
  @Action("/psnweb/friendreq/ajaxautofollowing")
  public String autoFollowingPsn() {
    try {
      friendService.autoFollowingPsn();
    } catch (Exception e) {
      logger.error("获取最近来访人员列表出错", e);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 邮件 邀请好友处理.
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/friend/requestMain")
  public String requestMain() throws Exception {
    // 获取邀请码.
    String invitationCode = Struts2Utils.getRequest().getParameter("invitationCode");
    String des3Key = Struts2Utils.getRequest().getParameter("key");
    String key = ServiceUtil.decodeFromDes3(des3Key);
    String verifyEmail = Struts2Utils.getRequest().getParameter("verifyEmail");
    // 响应站外的邀请邮件链接的响应(如果直接进入请求，未经过初始化逻辑时，对邀请进行初始化，绑定收件人)_MaoJianGuo_2012-11-08_SCM-1037.
    String des3mailId = Struts2Utils.getRequest().getParameter("des3mailId"); // 邀请发件箱的主键ID.
    Map<String, String> paramMap = this.buildParamMap(invitationCode, key, des3mailId, verifyEmail);
    // 调用发送者节点的service
    if (paramMap != null) {
      Integer result = inboxService.dealInviteBusiness(key, paramMap);
      // 1-邀请信息已被绑定且不是当前用户;2-群组已被删除或未找到群组记录_MJG_2013-03-06_SCM-1894.
      switch (result) {
        case 1:
          Struts2Utils.getRequest().setAttribute("warnMsg", getText("message.invite.group.link.invalid"));
          break;
        case 2:
          Struts2Utils.getRequest().setAttribute("warnMsg", getText("message.group.invite.delete"));
          break;
        default:
          break;
      }
    }
    Struts2Utils.getResponse().sendRedirect(domainscm + "/psnweb/friend/main");
    return null;
  }

  /**
   * 构造请求相应的参数map.
   * 
   * @param invitationCode
   * @param key
   * @param des3mailId
   * @param verifyEmail
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, String> buildParamMap(String invitationCode, String key, String des3mailId, String verifyEmail)
      throws Exception {
    Map<String, String> paramMap = null;
    if (StringUtils.isNotBlank(invitationCode)) {
      paramMap = this.splitInviteCode(invitationCode, key);
    } else if (StringUtils.isNotBlank(des3mailId)) {
      String nodeId = Struts2Utils.getRequest().getParameter("nodeId"); // 发出邀请的人的节点ID.
      String des3InviteId = Struts2Utils.getRequest().getParameter("des3InviteId"); // 邀请记录的主键ID.
      String des3inboxId = Struts2Utils.getRequest().getParameter("des3inboxId"); // 邀请收件箱的主键ID.
      if (nodeId == null || "".equals(nodeId)) {
        String des3NodeId = Struts2Utils.getRequest().getParameter("des3NodeId"); // 发出邀请的人的节点ID.
        nodeId = ServiceUtil.decodeFromDes3(des3NodeId);
      }
      paramMap = MapBuilder.getInstance().put("des3mailId", des3mailId).put("des3InviteId", des3InviteId)
          .put("des3inboxId", des3inboxId).put("nodeId", nodeId).getMap();
    }
    if (paramMap != null) {
      paramMap.put("verifyEmail", verifyEmail);
    }
    return paramMap;
  }

  /**
   * 将邀请码解析为参数集合.
   * 
   * @param invitationCode 邀请码.
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, String> splitInviteCode(String invitationCode, String key) {
    Map<String, String> paramMap = MapBuilder.getInstance().getMap();
    if (StringUtils.isNotBlank(invitationCode)) {
      String[] dataArr = invitationCode.split("-");
      String[] nameArr = this.getParamNameArr(key);
      if (nameArr.length > 0 && nameArr.length == dataArr.length) {
        for (int i = 0; i < nameArr.length; i++) {
          paramMap.put(nameArr[i], dataArr[i]);
        }
      }
    }
    return paramMap;
  }

  /**
   * 获取请求参数的名称数组.
   * 
   * @param key 邀请类型.
   * @return
   */
  private String[] getParamNameArr(String key) {
    String[] nameArr = new String[] {};
    if (FriendMailService.FRIEND_INVITE_KEY.equals(key)) {// 好友邀请.
      nameArr = FriendMailService.FRIEND_INVITATION_CODE_RULE.split("-");
    }
    return nameArr;
  }

  /**
   * 构建参数
   * 
   * @param form
   */
  private void buildForm(PsnListViewForm form) {
    if (form.getPsnId() == null && StringUtils.isNotBlank(form.getDes3PsnId())) {
      form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
    } else {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

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

  private void removeRecommendPsnCache() {
    cacheService.remove(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId());
  }

  @SuppressWarnings("unchecked")
  private List<Long> getRecommendPsnCache() {
    // SCM-16289
    return (List<Long>) cacheService.get(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId());
  }

  @SuppressWarnings("rawtypes")
  private void putRecommendPsnCache(List<Long> recommendPsnIds) {
    // SCM-16289
    cacheService.put(RECOMMEND_PSN_CACHE, CacheService.EXP_HOUR_1, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId(),
        (ArrayList) recommendPsnIds);
  }
}
