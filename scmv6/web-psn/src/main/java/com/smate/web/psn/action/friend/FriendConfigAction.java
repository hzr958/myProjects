package com.smate.web.psn.action.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.service.friend.FriendService;

/**
 * 
 * 可能认识的人
 * 
 * @author zx
 *
 */
@Results({@Result(name = "knownew", location = "/WEB-INF/jsp/friend/KnowNewFriend.jsp"),
    @Result(name = "knownew_sub", location = "/WEB-INF/jsp/friend/knowNewFriend_sub.jsp")})
public class FriendConfigAction extends ActionSupport implements ModelDriven<Personal>, Preparable, Serializable {

  private static final long serialVersionUID = -8967696433479787435L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Personal form;
  private boolean firstPage = true;
  private Page page = new Page(10);
  private String pubPartnersId;
  private String prjPartnersId;
  // 缓存推荐人员id
  private static final String RECOMMEND_PSN_CACHE = "recommend_psn_cache";
  // 缓存推荐人员id的key
  private static final String RECOMMEND_PSNIDS = "recommend_psnids";
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Autowired
  private FriendService friendService;
  @Value("${domainscm}")
  private String domainscm;
  private boolean isCoopartner = false;// 用于判断是否为合作者

  /**
   * 添加联系人
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxaddfriend")
  public String addFriendReq() {
    String resultJson = "";
    HashMap<Object, String> resultMap = new HashMap<Object, String>();
    Integer privateCount = 0;
    try {
      String reqPsnIds = form.getDes3Id();
      String[] reqPsnId = reqPsnIds.split(",");
      for (int j = 0; j < reqPsnId.length; j++) {
        if (StringUtils.isNotBlank(reqPsnId[j])) {
          // 被请求的人的Id
          Long psnId = Long.valueOf(ServiceUtil.decodeFromDes3(reqPsnId[j]));
          // 自己不能添加自己为联系人
          if (!psnId.equals(SecurityUtils.getCurrentUserId())) {
            Boolean isPsnPrivate = friendService.isPsnAddFrdPrivacy(psnId);
            if (isPsnPrivate) {
              friendService.addFriendReq(psnId, domainscm, form);
              resultMap.put("result", "true");
            } else {
              resultMap.put("result", "false");
              resultMap.put("msg", getText("friend.config.isprivate"));
              privateCount++;
            }
            // SCM-6680,发送添加联系人请求成功后清除缓存中的该对象
            List<Long> knowpsnList = getRecommendPsnCache();
            if (CollectionUtils.isNotEmpty(knowpsnList)) {
              for (int i = knowpsnList.size() - 1; i >= 0; i--) {
                Long sendPsnId = knowpsnList.get(i);
                if (psnId.longValue() == sendPsnId.longValue()) {
                  knowpsnList.remove(i);
                  putRecommendPsnCache(knowpsnList);
                  break;
                }
              }
            }

          } else {
            resultMap.put("result", "false");
            resultMap.put("msg", getText("friend.config.notAddSelf"));
          }
        } else {
          resultMap.put("result", "false");
          resultMap.put("msg", getText("friend.config.isNullRequester"));
          resultMap.put("reqNull", "1");
        }
      }
    } catch (Exception e) {
      logger.error("系统推荐——添加联系人发送请求出错", e);
      resultMap.put("result", "false");
      resultMap.put("msg", getText("friend.config.systemError"));
    }
    resultMap.put("privateCount", privateCount.toString());
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 可能认识的人
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxknownew")
  public String loadMayKnowPsnList() {
    List<Long> recommendIds = new ArrayList<Long>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      recommendIds = getRecommendPsnCache();
      if (CollectionUtils.isEmpty(recommendIds)) {
        recommendIds = friendService.getRecommendPsnListByPsnId(psnId);
        if (CollectionUtils.isNotEmpty(recommendIds)) {
          putRecommendPsnCache(recommendIds);

        }
      }
      /*
       * List<Person> knowList = (List<Person>) this.getKnowListCache(); if
       * (CollectionUtils.isEmpty(knowList)) { knowList =
       * friendService.findMayKnowPersonListByPsnIds(page, psnId, firstPage, "4", "5"); if (knowList !=
       * null) { putKnowListCache(knowList); } } if (CollectionUtils.isEmpty(knowList)) { knowList =
       * friendService.findPersonMayKnowByCurrentPsnId(psnId, firstPage, page); if (knowList != null) {
       * putKnowListCache(knowList); } }
       */
      if (CollectionUtils.isNotEmpty(recommendIds)) {
        // 基本信息的封装
        List<Person> knowList = friendService.getPersonBasePage(recommendIds);
        if (CollectionUtils.isNotEmpty(knowList)) {
          friendService.bulidBaseInfo(knowList, page, firstPage, Struts2Utils.getParameter("mayKnowPsnCount"));
          page.setTotalCount(knowList.size());
        } else {
          page.setTotalCount(0);
        }
      } else {
        page.setTotalCount(0);
      }
    } catch (Exception e) {
      logger.error("获取可能认识的人员出错", e);
    }
    if (firstPage != true) {
      return "knownew_sub";
    } else {
      return "knownew";
    }
  }

  /**
   * 邀请联系人
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxsendmail")
  public String sendMail() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(form.getPsnSendMail().getEmail())) {
        // 如果当前用户已经在科研之友， 就不要邀请了 ， SCM-15011 -ajb
        boolean isExists = friendService.IsExistsEmail(form.getPsnSendMail().getEmail());
        if (isExists) {
          map.put("result", "exists");
        } else {
          friendService.sendMail(form);
          map.put("result", "success");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("error", "网络异常");
      logger.error("邀请联系人出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  /**
   * 成果认领-邀请合作者成为联系人
   * 
   * @author lhd
   * @return
   */
  @Action("/psnweb/friend/ajaxinvitetofriend")
  public String inviteFriend() {
    try {
      if (form.getPdwhPubId() != null && form.getPdwhPubId() > 0L) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        List<Long> inviteFriendIds = friendService.getInviteFriendIds(form.getPdwhPubId(), currentUserId);
        if (CollectionUtils.isNotEmpty(inviteFriendIds)) {
          for (Long psnId : inviteFriendIds) {
            friendService.addFriendReq(psnId, domainscm, form);
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果认领-邀请合作者成为联系人出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    return null;
  }

  /**
   * 校验人员是否存在
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxcheckfriend")
  public String checkFriend() {
    Map<String, String> map = new HashMap<String, String>(1);
    try {
      if (form.getDes3Id() != null) {
        Long psnId = Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3Id()));
        if (friendService.getPsnInfo(psnId) == null) {
          map.put("status", "isDel");
        } else {
          map.put("status", "success");
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("校验人员是否存在出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new Personal();
    }
  }

  @Override
  public Personal getModel() {
    return form;
  }

  public boolean isCoopartner() {
    return isCoopartner;
  }

  public void setCoopartner(boolean isCoopartner) {
    this.isCoopartner = isCoopartner;
  }

  public String getPubPartnersId() {
    return pubPartnersId;
  }

  public void setPubPartnersId(String pubPartnersId) {
    this.pubPartnersId = pubPartnersId;
  }

  public String getPrjPartnersId() {
    return prjPartnersId;
  }

  public void setPrjPartnersId(String prjPartnersId) {
    this.prjPartnersId = prjPartnersId;
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

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public boolean getFirstPage() {
    return firstPage;
  }

  public void setFirstPage(boolean firstPage) {
    this.firstPage = firstPage;
  }
}
