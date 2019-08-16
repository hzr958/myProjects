package com.smate.web.psn.action.mobile.friend;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.privacy.service.PublicPrivacyService;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.psn.model.friend.FriendForm;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;

/**
 * 好友列表Action
 *
 * @author wsn
 *
 */
@Results({@Result(name = "friendlist", location = "/WEB-INF/jsp/mobile/friend/mobile_friend_list_new.jsp"),
    @Result(name = "friend_list_to_select", location = "/WEB-INF/jsp/mobile/friend/mobile_friend_list_to_select.jsp"),
    @Result(name = "mobileKnow", location = "/WEB-INF/jsp/mobile/friend/mobile_friend_mayknownew.jsp")})
public class FriendManageAction extends WechatBaseAction implements ModelDriven<FriendForm>, Preparable, Serializable {

  private static final long serialVersionUID = 1L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private FriendForm form;

  private Page page = new Page(10);

  @Autowired
  private FriendService friendService;
  @Autowired
  private PersonManager personManager;

  // 缓存推荐人员id
  private static final String RECOMMEND_PSN_CACHE = "recommend_psn_cache";
  // 缓存推荐人员id的key
  private static final String RECOMMEND_PSNIDS = "recommend_psnids";
  private static final String FRIEND_CONFIG_CACHE = "sns_friend_config_cache";
  private static final String KNOWLIST = "knowList";
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;
  @Autowired
  private PublicPrivacyService publicPrivacyService;

  /**
   * 进入好友列表
   * 
   * @return
   */
  @Action("/psnweb/mobile/friendlist")
  public String searchFriendList() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/mobile/friendlist" + this.handleRequestParams());
      }
      HttpServletRequest request = Struts2Utils.getRequest();
      String agent = (String) request.getHeader("USER-AGENT");
      if (SmateMobileUtils.isAndroid(agent)) {// 是否是Android系统
        form.setAndroidSys(true);
      }
      // 获取人员好友信息List
      form.setPsnId(SecurityUtils.getCurrentUserId());
      friendService.findFriendLis(form);
      // 对好友List进行排序、分类
      form.setPsnMap(friendService.sortFriendName(form.getPsnInfoList()));
      form.setHasLogin(1);
    } catch (Exception e) {
      logger.error("获取好友列表出错， psnId=" + form.getPsnId(), e);
    }
    return "friendlist";
  }

  /**
   * 进入好友列表,来选择好友
   * 
   * @return
   */
  @Action("/psnweb/mobile/friendlistotselect")
  public String friendListToSelect() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/mobile/friendlist" + this.handleRequestParams());
      }
      // 获取人员好友信息List
      form.setPsnId(SecurityUtils.getCurrentUserId());
      friendService.findFriendLis(form);
      // 对好友List进行排序、分类
      form.setPsnMap(friendService.sortFriendName(form.getPsnInfoList()));
    } catch (Exception e) {
      logger.error("获取好友列表出错， psnId=" + form.getPsnId(), e);
    }
    return "friend_list_to_select";
  }

  /**
   * 站内查看他人好友列表
   * 
   * @return
   */
  @Action("/psnweb/mobile/otherfriendlist")
  public String otherfriendlist() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/mobile/otherfriendlist" + this.handleRequestParams());
      }
      if (form.getDes3PsnId() != null && !"".equals(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      }
      form.setOther("true");
      // scm-11065-wsn
      String locale = StringUtils.isNotBlank(form.getLocal()) ? form.getLocal() : "zh_CN";
      String psnName = personManager.getPsnNameByIdAndLocal(form.getPsnId(), locale);
      form.setPsnName(psnName);
      // 好友权限
      Boolean permit = publicPrivacyService.canLookConsumerFriends(SecurityUtils.getCurrentUserId(), form.getPsnId());
      if (permit) {
        form.setPermission(0);
      } else {
        form.setPermission(1);
      }
      // 获取人员好友信息List
      if (form.getPsnId() != null && permit) {
        try {
          friendService.findFriendLis(form);
          // 对好友List进行排序、分类
          form.setPsnMap(friendService.sortFriendName(form.getPsnInfoList()));
        } catch (Exception e) {
          logger.error("获取好友列表出错， psnId=" + form.getPsnId(), e);
        }
      }
      form.setHasLogin(1);
    } catch (Exception e) {
      logger.error("查看他人好友列表出错， psnId=" + form.getPsnId(), e);
    }
    return "friendlist";
  }

  /**
   * 站外查看他人好友列表
   * 
   * @return
   */
  @Action("/psnweb/outside/mobilefriendlist")
  public String otherfriendlistOutside() {
    try {
      Long loginPsnId = SecurityUtils.getCurrentUserId();
      // 已登录的跳转站内地址
      if (loginPsnId > 0L && StringUtils.isNotBlank(form.getDes3PsnId())) {
        Struts2Utils.getResponse().sendRedirect(this.getDomain() + "/psnweb/mobile/otherfriendlist?des3PsnId="
            + URLEncoder.encode(form.getDes3PsnId(), "utf-8"));
        return null;
      }
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        this.handleWxJsApiTicket(this.getDomain() + "/psnweb/outside/mobilefriendlist" + this.handleRequestParams());
      }
      if (form.getDes3PsnId() != null && !"".equals(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      }
      form.setOther("true");
      // scm-11065-wsn
      String locale = StringUtils.isNotBlank(form.getLocal()) ? form.getLocal() : "zh_CN";
      String psnName = personManager.getPsnNameByIdAndLocal(form.getPsnId(), locale);
      form.setPsnName(psnName);
      // 好友权限
      Boolean permit = publicPrivacyService.canLookConsumerFriends(loginPsnId, form.getPsnId());
      if (permit) {
        form.setPermission(0);
      } else {
        form.setPermission(1);
      }
      // 获取人员好友信息List
      if (form.getPsnId() != null && permit) {
        try {
          friendService.findFriendLis(form);
          // 对好友List进行排序、分类
          form.setPsnMap(friendService.sortFriendName(form.getPsnInfoList()));
        } catch (Exception e) {
          logger.error("获取好友列表出错， psnId=" + form.getPsnId(), e);
        }
      }
      form.setLoginTargetUrl(Des3Utils
          .encodeToDes3("/psnweb/mobile/otherfriendlist?des3PsnId=" + URLEncoder.encode(form.getDes3PsnId(), "utf-8")));
      if (loginPsnId > 0) {
        form.setHasLogin(1);
      }
    } catch (Exception e) {
      logger.error("查看他人好友列表出错， psnId=" + form.getPsnId(), e);
    }
    return "friendlist";
  }

  /**
   * 移动端可能认识的人
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxknowmew")
  public String loadMobileMayKnow() {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Long> knowList = this.getRecommendPsnCache();
    // List<Long> knowList = null;
    try {
      if (CollectionUtils.isEmpty(knowList)) {
        knowList = friendService.getMobileRecommendPsnList(psnId);
        if (CollectionUtils.isNotEmpty(knowList)) {
          putRecommendPsnCache(knowList);
        }
      }
      if (CollectionUtils.isNotEmpty(knowList)) {
        // 不分页判断
        if (knowList.size() < (form.getPageNo() - 1) * 10) {
          return "mobileKnow";
        }
        // 对可能认识的人进行分页操作
        List<Long> psnIds = friendService.getMobileRecommendPsn(form.getPageNo(), knowList);
        PsnListViewForm psnForm = new PsnListViewForm();
        psnForm.setPsnIds(psnIds);
        psnForm.setServiceType("common");
        psnListViewService.getPsnListViewInfo(psnForm);
        page.setTotalCount(knowList.size());
        form.setPageNo(form.getPageNo());
        page.setResult(psnForm.getPsnInfoList());
      }
    } catch (Exception e) {
      logger.error("获取可能认识的人失败" + psnId, e);
    }

    return "mobileKnow";
  }

  /**
   * 获取好友请求数量
   * 
   * @return
   */
  @Action("/psnweb/mobile/ajaxgetreqfriendnumber")
  public String getfriendNumber() {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      Long friendNumbers = friendService.getReqFriendNumber(psnId);
      resultMap.put("friendNumbers", friendNumbers);
      Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("获取好友数量出错" + psnId, e);
    }
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FriendForm();
    }

  }

  @Override
  public FriendForm getModel() {
    return form;
  }

  @SuppressWarnings("unchecked")
  private List<Long> getRecommendPsnCache() {
    // SCM-16289
    return (List<Long>) cacheService.get(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId());
  }

  @SuppressWarnings("rawtypes")
  private void putRecommendPsnCache(List<Long> recommendPsnIds) {// ArrayList<Long>
    // SCM-16289
    cacheService.put(RECOMMEND_PSN_CACHE, CacheService.EXP_HOUR_1, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId(),
        (ArrayList) recommendPsnIds);
  }

  private List<Person> getKnowListCache() {
    return (List<Person>) cacheService.get(FRIEND_CONFIG_CACHE, KNOWLIST + Struts2Utils.getSession().getId());
  }

  private void putKnowListCache(List<Person> knowList) {
    cacheService.put(FRIEND_CONFIG_CACHE, CacheService.EXP_HOUR_1, KNOWLIST + Struts2Utils.getSession().getId(),
        (ArrayList<Person>) knowList);
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }
}
