package com.smate.web.psn.action.mobile.homepage;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.mobile.homepage.PersonHomepageMobileService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.representprj.RepresentPrjService;

/**
 * 移动端 个人主页
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "homepage", location = "/WEB-INF/jsp/mobile/homepage/mobile_homepage_main.jsp"),
    @Result(name = "myhome", location = "/WEB-INF/jsp/mobile/myhome/mobileMyhome.jsp"),
    @Result(name = "mobile_represent_prj", location = "/WEB-INF/jsp/mobile/homepage/mobile_represent_prj.jsp"),
    @Result(name = "keywords", location = "/WEB-INF/jsp/mobile/homepage/mobile_homepage_keywords.jsp")})
public class PersonHompageMobileAction extends WechatBaseAction
    implements ModelDriven<PsnHomepageMobileForm>, Preparable {

  private static final long serialVersionUID = -2700156214728128232L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PsnHomepageMobileForm form;
  @Autowired
  private PersonHomepageMobileService personHomepageMobileService;

  @Value("${domainoauth}")
  private String domainoauth;

  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  private String realUrlParamet; // 短地址跳转用
  @Autowired
  private RepresentPrjService representPrjService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;

  /**
   * 进入个人主页
   * 
   * @return
   * @throws Exception
   */
  @Actions({@Action("/psnweb/mobile/homepage")})
  public String hompage() {
    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      try {
        if (this.isWechatBrowser()) {
          form.setWechatBrowser(true);
          // 改为ajax请求处理，不然open系统有问题的话访问页面会有问题
          // this.handleWxJsApiTicket(this.getDomain() +
          // "/psnweb/mobile/homepage" + this.handleRequestParams());
        }
        if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
          this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        }
      } catch (Exception e) {
        logger.error("移动端处理微信签名出错, psnId = " + currentPsnId, e);
      }
      // 有限重线程变量中获取 没有 在到 参数中获取 还没有就直接回到登陆页面
      // 个人主页，查看好友信息 des3PsnId
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        String psnId = ServiceUtil.decodeFromDes3(form.getDes3PsnId());
        form.setPsnId(NumberUtils.toLong(psnId));
      } else {
        form.setPsnId(currentPsnId);
        form.setDes3PsnId(ServiceUtil.encodeToDes3(form.getPsnId().toString()));
        form.setIsMyself(true);
        logger.info(currentPsnId + "进入个人主页");
      }
      // 需要判断是否为好友和是否已关注过
      if (!this.isMySelf()) {
        form.setIsFriend(friendService.isFriend(currentPsnId, form.getPsnId()) ? 1 : 0);
        AttPerson attPerson = personManager.payAttention(currentPsnId, form.getPsnId());
        if (attPerson != null) {
          form.setPayAttention(1);
          form.setAttentionId(attPerson.getId());
        } else {
          form.setPayAttention(0);
        }
      }
      form.setHasLogin(1);
      // 不正确 重向到登录页面
      personHomepageMobileService.buildPsnHomepageData(form);
    } catch (Exception e) {
      logger.error("进入个人主页页面异常！", e);
    }
    return "homepage";
  }

  /**
   * 个人主页目录
   * 
   * @return
   */
  @Action("/psnweb/mobile/myhome")
  public String myhomepage() {
    try {
      // if (this.isWechatBrowser()) {
      // form.setWechatBrowser(true);
      // this.handleWxJsApiTicket(this.getDomain() +
      // "/psnweb/mobile/myhome" + this.handleRequestParams());
      // }
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      // 有限重线程变量中获取 没有 在到 参数中获取 还没有就直接回到登陆页面
      // 个人主页，查看好友信息 des3PsnId
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setDes3PsnId(ServiceUtil.encodeToDes3(form.getPsnId().toString()));
      form.setIsMyself(true);
      personHomepageMobileService.bulidMyHome(form);
      /* logger.info(SecurityUtils.getCurrentUserId() + "进入个人主页"); */

    } catch (Exception e) {
      logger.error("进入个人主页页面目录异常！", e);
    }
    return "myhome";
  }

  /**
   * 个人主页站外地址
   * 
   * @return
   * @throws Exception
   */
  @Actions({@Action("/psnweb/mobile/outhome"), @Action("/psnweb/mobile/otherhome")})
  public String outhomepage() {
    try {
      // 短地址跳转
      if (StringUtils.isNotBlank(this.getRealUrlParamet())) {
        PsnHomepageMobileForm obj = JacksonUtils.jsonObject(this.getRealUrlParamet(), PsnHomepageMobileForm.class);
        if (obj != null) {
          form.setDes3PsnId(obj.getDes3PsnId());
        }
      }
      if (form.getPsnId() == null || form.getPsnId() == 0) {
        String des3PsnId = Des3Utils.decodeFromDes3(form.getDes3PsnId());
        // 有限重线程变量中获取 没有 在到 参数中获取 还没有就直接回到登陆页面
        if ((StringUtils.isBlank(des3PsnId) || "0".equals(des3PsnId))
            && StringUtils.isNotBlank(form.getDes3ViewPsnId())) {
          if (form.getDes3ViewPsnId().contains(" ")) {
            form.setDes3ViewPsnId(form.getDes3ViewPsnId().replace(" ", "+"));
          }
          des3PsnId = ServiceUtil.decodeFromDes3(form.getDes3ViewPsnId());
        }
        form.setPsnId(NumberUtils.toLong(des3PsnId));
      }
      if (this.isWechatBrowser()) {
        form.setWechatBrowser(true);
        // 改为ajax请求处理，不然open系统有问题的话访问页面会有问题
        // this.handleWxJsApiTicket(
        // this.getDomain() + Struts2Utils.getRequest().getServletPath()
        // + this.handleRequestParams());
      }
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
      }
      Long loginPsnId = SecurityUtils.getCurrentUserId();
      if (loginPsnId > 0) {
        form.setHasLogin(1);
      }
      Long viewPsnId = form.getPsnId();// 查看的psnId
      // 如果是自己查看自己 就进入自己的个人主页
      if (form.getPsnId() != null && form.getPsnId().equals(loginPsnId)) {
        Struts2Utils.getResponse().sendRedirect(domainMobile + "/psnweb/mobile/homepage");
        return null;
      }
      initPsnPageData(form, loginPsnId);
      // 若为被合并账户，此处需再重新初始化一次数据
      if (!viewPsnId.equals(form.getPsnId())) {
        if (form.getPsnId() != null && form.getPsnId().equals(loginPsnId)) {
          Struts2Utils.getResponse().sendRedirect(domainMobile + "/psnweb/mobile/homepage");
          return null;
        }
        initPsnPageData(form, loginPsnId);
      }
      form.setDes3PsnId(ServiceUtil.encodeToDes3(String.valueOf(form.getPsnId())));
      String param = this.handleRequestParams();
      if (StringUtils.isBlank(param)) {
        param = "?des3PsnId=" + form.getDes3PsnId();
      }
      form.setLoginTargetUrl(Des3Utils.encodeToDes3("/psnweb/mobile/outhome" + param));
    } catch (Exception e) {
      logger.error("个人主页站外地址异常！", e);
      form.setPsnId(0L);
    }
    return "homepage";
  }

  private void initPsnPageData(PsnHomepageMobileForm form, Long loginPsnId) throws Exception {
    // 需要判断是否为好友和是否已关注过
    if (!this.isMySelf()) {
      form.setIsFriend(friendService.isFriend(loginPsnId, form.getPsnId()) ? 1 : 0);
      AttPerson attPerson = personManager.payAttention(loginPsnId, form.getPsnId());
      if (attPerson != null) {
        form.setPayAttention(1);
        form.setAttentionId(attPerson.getId());
      } else {
        form.setPayAttention(0);
      }
    }
    form.setOutHomePage("true"); // 设置他人主页
    personHomepageMobileService.buildPsnHomepageData(form);
  }

  @Action("/psnweb/outside/mobile/ajaxrepresentprj")
  public String showRepresentPrj() {
    try {
      // 先看下是否有传人员ID
      if ((form.getPsnId() == null || form.getPsnId() == 0) && StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
      }
      // 没有传人员ID，则认为是本人
      if (form.getPsnId() == null || form.getPsnId() == 0) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      Long currentUserId = SecurityUtils.getCurrentUserId();
      if (com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(currentUserId))
        form.setHasLogin(1);
      form.setPrjList(representPrjService.buildMobilePsnRepresentPrjInfo(form.getPsnId(), this.isMySelf()));
    } catch (Exception e) {
      logger.error("移动端显示人员代表性项目出错， psnId = " + form.getPsnId(), e);
    }
    return "mobile_represent_prj";
  }

  /**
   * 显示关键词信息
   * 
   * @return
   */
  @Action("/psnweb/outside/mobile/ajaxkeywords")
  public String showPsnKeyWords() {
    try {
      // 先看下是否有传人员ID
      if ((form.getPsnId() == null || form.getPsnId() == 0) && StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
      }
      // 没有传人员ID，则认为是本人
      if (form.getPsnId() == null || form.getPsnId() == 0) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
        form.setIsMyself(true);
      } else if (form.getPsnId().equals(SecurityUtils.getCurrentUserId())) {
        form.setIsMyself(true);
      } else {
        form.setIsMyself(false);
      }
      List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
      // 获取认同记录的人员头像
      if (CollectionUtils.isNotEmpty(keyList)) {
        for (PsnDisciplineKey key : keyList) {
          psnDisciplineKeyService.findSomeIdentifyKwPsnIds(form.getPsnId(), key);
        }
      }
      form.setKeywords(keyList);
    } catch (Exception e) {
      logger.error("移动端获取人员关键词出错， psnId = " + form.getPsnId(), e);
    }
    return "keywords";
  }

  /**
   * 判断是否是本人
   * 
   * @return
   */
  private boolean isMySelf() {
    boolean isSelf = false;
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    if ((form.getPsnId() == null || form.getPsnId() == 0) && StringUtils.isNotBlank(form.getDes3PsnId())) {
      form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      if (form.getPsnId() == null) {
        return isSelf;
      }
    }
    if (currentPsnId != null && currentPsnId != 0) {
      if (currentPsnId.longValue() == form.getPsnId().longValue()) {
        isSelf = true;
      } else {
        isSelf = false;
      }
    }
    form.setIsMyself(isSelf);
    return isSelf;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnHomepageMobileForm();
    }
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    form.setCurrentPsnId(currentPsnId);
    if (currentPsnId != null) {
      form.setDes3CurrentPsnId(Des3Utils.encodeToDes3(currentPsnId.toString()));
    }
  }

  @Override
  public PsnHomepageMobileForm getModel() {
    return form;
  }

  public String getRealUrlParamet() {
    return realUrlParamet;
  }

  public void setRealUrlParamet(String realUrlParamet) {
    this.realUrlParamet = realUrlParamet;
  }

}
