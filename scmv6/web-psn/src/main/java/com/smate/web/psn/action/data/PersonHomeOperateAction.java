package com.smate.web.psn.action.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.msg.MobileMessageForm;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyService;
import com.smate.core.base.utils.service.msg.MobileMessageWwxyServiceImpl;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.keyword.CategoryMapBaseService;
import com.smate.web.psn.service.profile.KeywordIdentificationService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.representprj.RepresentPrjService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;
import com.smate.web.psn.service.user.UserService;

/**
 * 个人主页数据接口
 * 
 * @author Administrator
 *
 */
public class PersonHomeOperateAction extends ActionSupport implements ModelDriven<PsnHomepageMobileForm>, Preparable {
  private static final long serialVersionUID = -2705820249331202691L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PsnHomepageMobileForm form;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;
  @Autowired
  private UserService userService;
  @Resource
  private MobileMessageWwxyService mobileMessageWwxyService;
  @Resource
  private PsnCacheService cacheService;
  @Autowired
  private RepresentPrjService representPrjService;
  @Autowired
  private FriendService friendService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  KeywordIdentificationService identificationService;
  @Autowired
  private PersonManager personManager;

  /**
   * 编辑个人科技领域信息
   * 
   * @return
   */
  @Action("/psndata/mobile/editareas")
  public void psnEditAreas() {
    Map<String, Object> result = new HashMap<String, Object>();
    List<Map<String, Object>> areaList = null;
    try {
      if (form.getDes3PsnId() != null) {
        Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
        // 获取科技领域构建成的Map
        List<Integer> selectAreaIds = scienceAreaService.findPsnAreaId(psnId);
        areaList = categoryMapBaseService.buildEditScienceAreaInfo(selectAreaIds);
        result.put("status", "success");
      }
    } catch (Exception e) {
      result.put("status", "error");
      result.put("errmsg", "获取编辑科技领域出错，des3PsnId=" + form.getDes3PsnId());
      logger.error("获取科技领域信息出错，psnId=" + form.getDes3PsnId(), e);
    }
    result.put("result", areaList);
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 编辑个人主页关键词
   * 
   * @return
   */
  @Action("/psndata/mobile/editkeyword")
  public void psnEditKeyWord() {
    Map<String, Object> result = new HashMap<String, Object>();
    List<PsnDisciplineKey> psnKeyword = null;
    try {
      if (form.getDes3PsnId() != null) {
        Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
        // 获取科技领域构建成的Map
        psnKeyword = psnDisciplineKeyService.findPsnEffectiveKeywods(psnId);
        result.put("status", "success");
      }
    } catch (Exception e) {
      result.put("status", "error");
      result.put("errmsg", "获取个人设置的关键词出错，des3PsnId=" + form.getDes3PsnId());
      logger.error("获取个人设置的关键词出错，psnId=" + form.getDes3PsnId(), e);
    }
    result.put("result", psnKeyword);
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 自动填充学科关键词
   * 
   * @return
   */
  @Action("/psndata/mobile/autoconstkeydiscs")
  public String autoConstKeyDiscs() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isEmpty(form.getSearchKey())) {
        result.put("status", "error");
        result.put("errmsg", "searchKey不能为空");
        Struts2Utils.renderJson(result, "encoding:UTF-8");
      }
      List<ConstKeyDisc> constKeyDiscList =
          psnDisciplineKeyService.getConstKeyDiscs(form.getSearchKey(), form.getKeySize());
      if (constKeyDiscList != null && constKeyDiscList.size() > 0) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (ConstKeyDisc c : constKeyDiscList) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("code", c.getDiscCodes());
          map.put("name", c.getKeyWord());
          map.put("keyId", c.getId().toString());
          list.add(map);
        }
        result.put("status", "success");
        result.put("result", list);
      }
    } catch (Exception e) {
      result.put("status", "error");
      logger.error("自动填充学科关键词", e);
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 保存人员关键词
   * 
   * @return
   */
  @Action("/psndata/mobile/savepsnkeywords")
  public String savePsnKeywords() {
    Map<String, Object> data = new HashMap<String, Object>();
    Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Objects.toString(form.getDes3PsnId(), "")));
    try {
      if (!checkParam(psnId, form.getPsnKeyStr(), data)) {
        Struts2Utils.renderJson(data, "encoding:UTF-8");
      }
      // 校验是否超过10个关键词
      List<String> keyList = new ArrayList<String>();
      if (StringUtils.isNotBlank(form.getPsnKeyStr())) {
        keyList = Arrays.asList(form.getPsnKeyStr().split("@@"));
      }
      if (CollectionUtils.isNotEmpty(keyList) && keyList.size() > 10) {
        data.put("status", "error");
        data.put("errmsg", "sumLimit");
      } else {
        psnDisciplineKeyService.mobileSavePsnKeywords(psnId, keyList, form.getAnyUser());
        data.put("status", "success");
      }
    } catch (Exception e) {
      logger.error("移动端保存人员关键词信息出错, psnId = " + psnId + ", keywords=" + form.getPsnKeyStr(), e);
      data.put("status", "error");
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取人员的代表成果
   *
   * @return
   */
  @Action("/psndata/mobile/getrepresentprjlist")
  public String getRepresentPrjList() {
    Map<String, Object> data = new HashMap<String, Object>();
    List<Project> list = new ArrayList<Project>();
    try {
      boolean isMyself = false;
      Long currentUserId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
      form.setPsnId(currentUserId);
      String des3OtherPsnId = form.getDes3OtherPsnId();
      Long otherPsnId = 0L;
      if (StringUtils.isNotBlank(des3OtherPsnId)) {
        otherPsnId = Long.parseLong(Des3Utils.decodeFromDes3(des3OtherPsnId));
      }
      if (currentUserId.equals(otherPsnId)) {
        isMyself = true;
      }
      if (otherPsnId != 0L) {
        list = representPrjService.buildMobilePsnRepresentPrjInfo(form.getPsnId(), isMyself);
        data.put("status", "success");
        data.put("resultList", list);
        data.put("totalCount", list.size());
      }
    } catch (Exception e) {
      data.put("status", "error");
      logger.error("移动端显示人员代表性项目出错， psnId = " + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 保存人员的代表成果
   *
   * @return
   */
  @Action("/psndata/mobile/saverepresentprjlist")
  public String saveRepresentPrjList() {
    Map<String, Object> data = new HashMap<String, Object>();
    try {

      Long currentUserId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
      form.setPsnId(currentUserId);
      try {
        representPrjService.savePsnRepresentPrj(form.getPsnId(), form.getAddToRepresentPrjIds());
        data.put("result", "success");
      } catch (Exception e) {
        logger.error("保存人员代表性项目出错， psnId = " + form.getPsnId() + ", prjIds=" + form.getAddToRepresentPrjIds(), e);
        data.put("result", "error");
      }
      Struts2Utils.renderJson(data, "encoding:UTF-8");
    } catch (Exception e) {
      data.put("status", "error");
      logger.error("移动端显示人员代表性项目出错， psnId = " + form.getPsnId(), e);
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 参数校验
   */
  public boolean checkParam(Long psnId, String psnKeyStr, Map<String, Object> data) {
    if (psnId <= 0) {
      data.put("status", "error");
      data.put("errmsg", "人员id为空");
      return false;
    }
    if (StringUtils.isEmpty(psnKeyStr)) {
      data.put("status", "error");
      data.put("errmsg", "psnKeyStr不能为空");
      return false;
    }
    return true;
  }

  /**
   * 发送手机验证码
   *
   * @return
   */
  @Action("/psndata/mobile/sendMobileCode")
  public String sendMobileCode() {
    Boolean isMobile = com.smate.core.base.utils.string.StringUtils.isMobileNumber(form.getMobileNumber());
    Map resMap = new HashMap();
    if (isMobile) {
      User user = userService.findUserByMobile(form.getMobileNumber());
      if (user == null) {
        String code = RandomStringUtils.randomNumeric(6);
        MobileMessageForm messsage = new MobileMessageForm();
        messsage.setDestId(form.getMobileNumber());
        // 注册验证码
        messsage.setPsnId(SecurityUtils.getCurrentUserId());
        messsage.setSmsType(MobileMessageWwxyServiceImpl.REG_TYPE);
        messsage.setContent(MobileMessageWwxyServiceImpl.buildRegMessage(code));
        cacheService.put(MobileMessageWwxyService.CACHE_NAME, MobileMessageWwxyService.EXPIRE_DATE,
            form.getMobileNumber(), code);
        mobileMessageWwxyService.initSendMessage(messsage);
        resMap.put("result", "success");
      } else {
        resMap.put("result", "exist");
      }
    } else {
      resMap.put("result", "error");
    }

    Struts2Utils.renderJson(resMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 成果认领-邀请合作者成为联系人
   * 
   * @author lhd
   * @return
   */
  @SuppressWarnings("unchecked")
  @Action("/psndata/friend/invitetofriend")
  public String inviteFriend() {
    Map resMap = new HashMap();
    resMap.put("status", "success");
    try {
      if (form.getPdwhPubId() != null && form.getPdwhPubId() > 0L && StringUtils.isNoneBlank(form.getDes3PsnId())) {
        Long currentUserId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
        List<Long> inviteFriendIds = friendService.getInviteFriendIds(form.getPdwhPubId(), currentUserId);
        Personal personalform = new Personal();
        personalform.setPdwhPubId(form.getPdwhPubId());
        personalform.setPsnId(currentUserId);
        if (CollectionUtils.isNotEmpty(inviteFriendIds)) {
          for (Long psnId : inviteFriendIds) {
            friendService.addFriendReq(psnId, domainscm, personalform);
          }
        }
      } else {
        resMap.put("status", "参数校验不通过");
      }
    } catch (Exception e) {
      resMap.put("status", "error");
      logger.error("成果认领-邀请合作者成为联系人出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    Struts2Utils.renderJson(resMap, "encoding:UTF-8");
    return null;
  }

  // 关键词认同
  @Action("/psndata/keyword/identifickeyword")
  public void identificKeyword() throws Exception {
    Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3CurrentPsnId()));// 操作人
    Long keyWordPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));// 关键词拥有者
    Long oneKeyWordId = form.getOneKeyWordId();// 关键词id
    if (checkIdentificKeywordParam(currentPsnId, keyWordPsnId, oneKeyWordId)) {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      try {
        if (!currentPsnId.equals(keyWordPsnId)) {
          // 认同关键词
          identificationService.identificKeyword(keyWordPsnId, oneKeyWordId, currentPsnId);
          Person person = personManager.getPsnNameAndAvatars(currentPsnId);// 获取头像用
          // 获取认同数
          resultMap.put("sum", identificationService.countOneIdentification(keyWordPsnId, oneKeyWordId));
          resultMap.put("avatar", person.getAvatars());
          resultMap.put("result", "success");
          Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
        } else {
          resultMap.put("result", "fail");
          Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
        }
      } catch (Exception e) {
        logger.error("认同关键词出错：认同人psnid=" + form.getPsnId() + "keywordId" + form.getOneKeyWordId(), e);
        resultMap.put("result", "fail");
        Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
      }
    }
  }

  private boolean checkIdentificKeywordParam(Long currentPsnId, Long keyWordPsnId, Long oneKeyWordId) {
    return NumberUtils.isNotNullOrZero(currentPsnId) && NumberUtils.isNotNullOrZero(keyWordPsnId)
        && NumberUtils.isNotNullOrZero(oneKeyWordId);
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnHomepageMobileForm();
    }
  }

  @Override
  public PsnHomepageMobileForm getModel() {
    return form;
  }
}
