package com.smate.web.psn.action.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.model.friend.FriendTemp;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.mobile.homepage.PersonHomepageMobileService;

/**
 * 好友添加接口
 * 
 * @author LJ
 *
 *         2017年9月23日
 */
public class APPFriendConfigAction extends ActionSupport implements ModelDriven<Personal>, Preparable, Serializable {

  private static final long serialVersionUID = -2039590622512087582L;
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
  @Autowired
  private PersonHomepageMobileService personHomepageMobileService;
  @Value("${domainscm}")
  private String domainscm;
  private boolean isCoopartner = false;// 用于判断是否为合作者
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * app发送添加好友请求
   * 
   * @return
   * @throws Exception
   */
  @Action("/app/friend/addfriend")
  public String addFriendReq() throws Exception {
    String resultJson = "";
    try {
      String reqPsnIds = form.getDes3Id();
      String[] reqPsnId = reqPsnIds.split(",");
      status = IOSHttpStatus.OK;
      for (int j = 0; j < reqPsnId.length; j++) {
        if (StringUtils.isNotBlank(reqPsnId[j])) {
          // 被请求的人的Id
          Long psnId = Long.valueOf(ServiceUtil.decodeFromDes3(reqPsnId[j]));
          Boolean isPsnPrivate = friendService.isPsnAddFrdPrivacy(psnId);
          if (isPsnPrivate) {
            friendService.addFriendReq(psnId, domainscm, form);
            resultJson = "success";
          } else {
            status = IOSHttpStatus.NOT_MODIFIED;
            resultJson = "用户不允许加他为好友";
          }
          // SCM-6680,发送添加好友请求成功后清除缓存中的该对象
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
          status = IOSHttpStatus.NOT_FOUND;
          resultJson = "failed";
        }
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("系统推荐——添加好友发送请求出错", e);
      resultJson = "failed";
    }
    AppActionUtils.renderAPPReturnJson(resultJson, total, status);
    return null;
  }

  /**
   * 扫描二维码添加好友跳转页
   * 
   * @return
   * @throws Exception
   */
  @Action("/app/friend/preaddfriend")
  public String preAddFriend() throws Exception {
    HashMap<String, Object> map = new HashMap<String, Object>();

    if (StringUtils.isNotBlank(form.getDes3Id())) {
      Long reqpsnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3Id()));
      // 获取人员信息
      Person reqfriend = friendService.getPsnInfo(reqpsnId);
      if (reqfriend == null) {
        status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
        AppActionUtils.renderAPPReturnJson("该人员不存在", total, status);
        return null;
      }
      boolean isfriend = friendService.isFriend(form.getPsnId(), reqpsnId);
      List<FriendTemp> friendTempList = friendService.checkFriendTempExists(reqpsnId, form.getPsnId());
      // 添加好友请求记录存在
      if (CollectionUtils.isNotEmpty(friendTempList)) {
        reqfriend.setIsMyFriend(1);// 待验证
      } else {
        if (isfriend == true) {
          reqfriend.setIsMyFriend(2);// 是我的好友
        } else {
          reqfriend.setIsMyFriend(0);// 不是我的好友
        }
      }
      map.put("psnBaseInfo", reqfriend);
      List<PsnScienceArea> PsnScienceAreaList = personHomepageMobileService.findPsnScienceAreaList(reqpsnId);
      map.put("psnScienceArea", PsnScienceAreaList);
    } else {
      status = IOSHttpStatus.BAD_REQUEST;// 请求参数不合法
      AppActionUtils.renderAPPReturnJson("请求参数不正确", total, status);
      return null;
    }
    status = IOSHttpStatus.OK;
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new Personal();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
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
