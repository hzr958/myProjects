package com.smate.web.psn.action.mobile.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.friend.FriendForm;
import com.smate.web.psn.model.friend.FriendTemp;
import com.smate.web.psn.model.friend.PsnListViewForm;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.profile.psnListView.PsnListViewService;

/**
 * 
 * @author LJ
 *
 *         2017年6月27日
 */
public class APPFriendManageAction extends ActionSupport implements ModelDriven<FriendForm>, Preparable, Serializable {

  private static final long serialVersionUID = -6182980756811058080L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private FriendForm form;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Autowired
  private FriendService friendService;
  private Page page = new Page(10);
  // 缓存推荐人员id
  private static final String RECOMMEND_PSN_CACHE = "recommend_psn_cache";
  // 缓存推荐人员id的key
  private static final String RECOMMEND_PSNIDS = "recommend_psnids";
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Resource(name = "psnListViewService")
  private PsnListViewService psnListViewService;
  private String receivePhone;

  /**
   * 进入好友列表
   * 
   * @return
   */
  @Action("/app/psnweb/friendlist")
  public String searchFriendList() {
    List<PsnInfo> psnInfoList = null;
    try {
      // 获取人员好友信息List
      friendService.findFriendLis(form);
      // 对好友List进行排序、分类
      form.setPsnMap(friendService.sortFriendName(form.getPsnInfoList()));
      psnInfoList = form.getPsnInfoList();
      if (psnInfoList != null) {
        total = psnInfoList.size();
      }
      status = IOSHttpStatus.OK;

    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("获取好友列表出错， psnId=" + form.getPsnId(), e);
    }
    AppActionUtils.renderAPPReturnJson(psnInfoList, total, status);

    return null;
  }

  /**
   * app可能认识的人
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/app/psnweb/ajaxknownew")
  public String loadMobileMayKnow() {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<Long> knowList = this.getRecommendPsnCache();
    try {
      if (CollectionUtils.isEmpty(knowList)) {
        if (CollectionUtils.isEmpty(knowList)) {
          knowList = friendService.getMobileRecommendPsnList(psnId);
          if (CollectionUtils.isNotEmpty(knowList)) {
            putRecommendPsnCache(knowList);
          }
        }

      }
      if (CollectionUtils.isNotEmpty(knowList)) {
        // 不分页判断
        if (knowList.size() < (form.getPageNo() - 1) * 10) {
          status = IOSHttpStatus.OK;
          AppActionUtils.renderAPPReturnJson("", total, status);
          return null;
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
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app获取可能认识的人失败" + form.getPsnId(), e);
    }
    // total = page.getResult() == null ? 0 : page.getResult().size();
    total = page.getTotalCount().intValue();
    AppActionUtils.renderAPPReturnJson(page.getResult(), total, status);
    return null;
  }

  /**
   * 获取好友请求数量
   * 
   * @return
   */
  @Action("/app/psnweb/ajaxgetreqfriendnumber")
  public String getfriendNumber() {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      Long friendNumbers = friendService.getReqFriendNumber(form.getPsnId());
      resultMap.put("friendNumbers", friendNumbers);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("获取好友数量出错" + form.getPsnId(), e);
    }
    AppActionUtils.renderAPPReturnJson(resultMap, total, status);
    return null;
  }

  /**
   * 获取手机端通讯录联系人
   * 
   * @return
   */
  @Action("/app/psnweb/getcontactfriend")
  public String getContactfriend() {
    List<String> phoneList = new ArrayList<String>();
    try {
      if (StringUtils.isNotEmpty(receivePhone)) {
        String[] split = receivePhone.split(",");
        for (String string : split) {
          phoneList.add(string);
        }
      }
    } catch (Exception e) {
      status = IOSHttpStatus.BAD_REQUEST;
      logger.error("根据手机通讯录获取人员出错,", e);
      AppActionUtils.renderAPPReturnJson("bad request", total, status);
      return null;
    }
    List<Object> result = new ArrayList<Object>();
    for (String mobile : phoneList) {
      try {
        List<Person> person = friendService.getContactfriend(mobile);
        for (Person cperson : person) {
          boolean isfriend = friendService.isFriend(form.getPsnId(), cperson.getPersonId());
          List<FriendTemp> friendTempList = friendService.checkFriendTempExists(cperson.getPersonId(), form.getPsnId());
          // 添加好友请求记录存在
          if (CollectionUtils.isNotEmpty(friendTempList)) {
            cperson.setIsMyFriend(1);// 待验证
          } else {
            if (isfriend == true) {
              cperson.setIsMyFriend(2);// 是我的好友
            } else {
              cperson.setIsMyFriend(0);// 不是我的好友
            }
          }
          result.add(cperson);
        }
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("根据手机通讯录获取人员出错，mobile：" + mobile, e);
        AppActionUtils.renderAPPReturnJson(result, total, status);
        return null;
      }
    }
    total = result == null ? 0 : result.size();
    status = IOSHttpStatus.OK;
    AppActionUtils.renderAPPReturnJson(result, total, status);
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FriendForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public FriendForm getModel() {
    return form;
  }

  public String getReceivePhone() {
    return receivePhone;
  }

  public void setReceivePhone(String receivePhone) {
    this.receivePhone = receivePhone;
  }

  @SuppressWarnings("unchecked")
  private List<Long> getRecommendPsnCache() {
    return (List<Long>) cacheService.get(RECOMMEND_PSN_CACHE, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId());
  }

  @SuppressWarnings("rawtypes")
  private void putRecommendPsnCache(List<Long> recommendPsnIds) {
    cacheService.put(RECOMMEND_PSN_CACHE, CacheService.EXP_HOUR_1, RECOMMEND_PSNIDS + SecurityUtils.getCurrentUserId(),
        (ArrayList) recommendPsnIds);
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }
}
