package com.smate.web.psn.action.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.search.PersonSearch;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.search.PsnSearchService;

/**
 * 
 * @author LJ
 *
 *         2017年9月15日
 */
public class APPMyFriendAction extends ActionSupport implements ModelDriven<PsnListViewForm>, Preparable {
  private static final long serialVersionUID = -2122587022768797924L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PsnListViewForm form;

  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  // 缓存推荐人员id
  static final String RECOMMEND_PSN_CACHE = "recommend_psn_cache";
  // 缓存推荐人员id的key
  static final String RECOMMEND_PSNIDS = "recommend_psnids";
  @Autowired
  private FriendService friendService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  private Integer pageNo = 1;
  @Autowired
  private PsnSearchService psnSearchService;
  private Page<PersonSearch> psnpage = new Page<PersonSearch>(10);
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private RestTemplate restTemplate;

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  private String friendDes3PsnIds;

  public String getFriendDes3PsnIds() {
    return friendDes3PsnIds;
  }

  public void setFriendDes3PsnIds(String friendDes3PsnIds) {
    this.friendDes3PsnIds = friendDes3PsnIds;
  }

  /**
   * 消息中心-站内信-好友列表
   * 
   * @return
   */
  @Action("/app/psnweb/friend/ajaxgetchatfriendlist")
  public String showMsgChatPsnList() {
    if (StringUtils.isNotBlank(form.getDes3PsnId())) {
      form.setSearchType(0);
      form.getPage().setPageSize(5);
      form.getPage().setIgnoreMin(true);
      form.getPage().setPageNo(pageNo);
      try {
        friendService.getMsgChatPsnList(form);
        total = form.getPsnInfoList() == null ? 0 : form.getPsnInfoList().size();
        status = IOSHttpStatus.OK;
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("消息中心-站内信-好友/全站人员列表出错,form= " + form, e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(form.getPsnInfoList(), total, status);
    return null;

  }

  /**
   * 消息中心联合查询人员
   * 
   * @return
   */
  @Action("/app/psnweb/search/psnandfrd")
  public String mergeSearchPsn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(form.getDes3PsnId())) {
      form.setSearchType(0);
      form.getPage().setPageSize(5);
      form.getPage().setIgnoreMin(true);
      form.getPage().setPageNo(pageNo);
      form.setPsnTypeForChat("friend");
      try {
        friendService.getMsgChatPsnList(form);
        total = form.getPsnInfoList() == null ? 0 : form.getPsnInfoList().size();
        status = IOSHttpStatus.OK;
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("消息中心-站内信-好友/全站人员列表出错,form= " + form, e);
      }
      map.put("friendlist", form.getPsnInfoList());

      // 全站查询人员
      if (StringUtils.isNotBlank(form.getSearchKey())) {
        try {
          QueryFields queryFields = new QueryFields();
          queryFields.setSearchString(form.getSearchKey());
          psnpage.setPageNo(pageNo);
          psnpage.setIgnoreMin(true);
          psnpage.setPageSize(5);
          queryFields.setSearchType(0);
          psnSearchService.getPsnsForMsg(psnpage, queryFields);
          status = IOSHttpStatus.OK;
        } catch (Exception e) {
          logger.error("消息中心人员全站检索失败", e);
          status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        }
      }
      map.put("psnlist", psnpage.getResult());
      // 查找聊天记录
      MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
      params.add("des3PsnId", form.getDes3PsnId());
      params.add("searchKey", form.getSearchKey());
      Map<String, Object> result =
          restTemplate.postForObject(domainMobile + "/dyndata/msg/getsearchchatpsnlist", params, Map.class);
      if (result != null && "success".equals(result.get("status"))) {
        map.put("msgShowInfoList", result.get("msgShowInfoList"));
      } else {
        map.put("msgShowInfoList", "");
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 同意好友请求
   * 
   * @return
   */
  @Action("/app/psnweb/friendreq/ajaxaccept")
  public String acceptAddFriendRequest() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      // 校验请求人员ID
      String check = this.checkReqPsnIds(form);
      if (StringUtils.isNotBlank(check)) {
        status = IOSHttpStatus.BAD_REQUEST;
        data.put("result", check);
      } else {
        // 处理接受请求
        for (Long acPsnId : form.getPsnIds()) {
          String result = friendService.acceptAddFriendRequest(acPsnId, form.getPsnId());

          if (!"success".equals(result)) {
            if ("msg".equals(result)) {
              data.put("result", "friends request record does not exist");
            } else {
              data.put("result", result);
            }
            AppActionUtils.renderAPPReturnJson(data, total, status);
            return null;
          }
        }
        data.put("result", "success");
        status = IOSHttpStatus.OK;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("接受添加好友请求出错， reqPsnIds = " + form.getDes3ReqPsnIds() + ", currentPsnId" + form.getPsnId(), e);
      data.put("result", "error");
    }

    AppActionUtils.renderAPPReturnJson(data.get("result"), total, status);
    return null;
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
   * 好友请求列表-忽略操作
   * 
   * @return
   */
  @Action("/app/psnweb/friendreq/ajaxneglet")
  public String ajaxNeglet() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getTempPsnId() != null && form.getTempPsnId() > 0L) {
        buildForm(form);
        friendService.negletFriendReq(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("result", "error");
      }

    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("好友请求列表-忽略操作,出错,psnId= " + form.getPsnId(), e);
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;

  }

  /**
   * 删除好友
   * 
   * @return
   */
  @Action("/app/psnweb/friend/ajaxdel")
  public String ajaxDelFriend() {
    Map<String, String> map = new HashMap<String, String>();
    // String friendPsnIds = ServiceUtil.decodeFromDes3(friendDes3PsnIds);
    try {
      if (StringUtils.isNotBlank(friendDes3PsnIds)) {
        friendService.delFriendByPsnIds(friendDes3PsnIds);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("result", "error");
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("删除好友出错, desfriendIds=" + friendDes3PsnIds, e);
      map.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * 我的-好友推荐列表-移除
   * 
   * @return
   */
  @Action("/app/psnweb/recommend/ajaxremove")
  public String ajaxremove() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (form.getTempPsnId() != null && form.getTempPsnId() > 0l) {
        buildForm(form);
        friendService.addFriendTempSys(form);
        removeRecommendPsnCache();
        status = IOSHttpStatus.OK;
        map.put("result", "success");
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("result", "error");
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("我的-好友推荐列表-移除,出错,psnId= " + form.getPsnId(), e);
    }

    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;

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
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public PsnListViewForm getModel() {
    return form;
  }

  private void removeRecommendPsnCache() {
    cacheService.remove(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId());
  }
}
