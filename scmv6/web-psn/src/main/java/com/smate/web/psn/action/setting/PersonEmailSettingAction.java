package com.smate.web.psn.action.setting;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.setting.ConstMailType;
import com.smate.web.psn.model.setting.PsnMailSet;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.setting.PsnMailSetService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 人员邮件设置
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "attention_set", location = "/WEB-INF/jsp/psnsetting/email/email_set.jsp"),
    @Result(name = "success", location = "/WEB-INF/jsp/psnsetting/email/mailUnsubscribeSuccess.jsp"),
    @Result(name = "successlogin", type = "redirect", location = "/psnweb/cancle/middlepage"),
    @Result(name = "successlogout", type = "redirect", location = "/psnweb/cancle/success/logout"),
    @Result(name = "middlePage", location = "/WEB-INF/jsp/psnsetting/email/mailUnsubscribeMiddlePage.jsp.jsp"),
    @Result(name = "error", location = "/WEB-INF/jsp/psnsetting/email/error.jsp"),
    @Result(name = "unsubscribeConfirm", location = "/WEB-INF/jsp/psnsetting/email/unsubscribeTip.jsp")})
public class PersonEmailSettingAction extends ActionSupport implements ModelDriven<PsnSettingForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected static final String CASLOGIN_COOKIE_NAME = "CasAuth";
  @Value("${domainscm}")
  private String domainscm;
  private PsnSettingForm form;
  private Long typeId;

  private static final long serialVersionUID = -7496914505522794654L;

  @Autowired
  private PsnMailSetService psnMailSetService;

  @Autowired
  private PersonManager personManager;

  /**
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxgetMailTypeList")
  public String getMailTypeList() {
    form.setConstMailTypeList(psnMailSetService.getMailTypeList());
    String des3TypeId = Struts2Utils.getRequest().getParameter("typeid");
    if (des3TypeId != null && !des3TypeId.equals("")) {
      typeId = Long.valueOf(ServiceUtil.decodeFromDes3(des3TypeId));
    }
    HttpSession session = Struts2Utils.getSession();
    session.setAttribute("typeId", typeId);
    return "attention_set";
  }

  /**
   * 根据当前用户Id取其邮件设置
   * 
   * @return
   * @throws ServiceException
   */
  @Action("/psnweb/psnsetting/ajaxListPsnMailSet")
  public String ajaxListPsnMailSet() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    // 获取已经设置接收的邮件列表
    List lst = psnMailSetService.list(psnId);
    Object str = Struts2Utils.getSession().getAttribute("typeId");
    if (str != null && !"".equals(str)) {
      typeId = Long.valueOf(str.toString());
      for (int i = 0; i < lst.size(); i++) {
        Object o = lst.get(i);
        PsnMailSet pms = (PsnMailSet) o;
        Long id = pms.getMailTypeId();
        if (id.equals(typeId)) {
          lst.remove(i);
        }
      }
    }

    // 获取个人基本信息，以便获取邮件语言版本
    Person psn = personManager.getPersonBaseInfo(psnId);

    Map lanMap = new HashMap();
    // 设置邮件语言版本
    if (psn.getEmailLanguageVersion() == null) {// 如果个人邮件接收语言设置为null，则将个人邮件接收语言设置为当前系统语言版本，同时更新到数据库
      lanMap.put("emailLanguageVersion", this.getLocale().toString());
      psn.setEmailLanguageVersion(this.getLocale().toString());
      personManager.saveCurrPsnEmailLanguageVersion(this.getLocale().toString());
    } else {
      lanMap.put("emailLanguageVersion", psn.getEmailLanguageVersion());
    }
    lst.add(lanMap);
    JsonConfig config = new JsonConfig();
    String[] EXCLUDES = new String[] {"psnId"};
    config.setExcludes(EXCLUDES);
    String strPsnMailSet = JSONArray.fromObject(lst, config).toString();
    Struts2Utils.renderJson(strPsnMailSet, "encoding:UTF-8");
    return null;
  }

  /**
   * 新增或修改当前用户的邮件设置
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxAddPsnMailSet")
  public String ajaxAddPsnMailSet() throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();

    // 删除,界面上原来表中的配置
    this.psnMailSetService.removes(psnId);
    // 得到这次未被勾选的配置的配置 tsz_scm-5304_2014-6-18
    String ids = form.getPsnMailSet().getIds();
    // 邮件语言版本
    String languageVersion = form.getPsnMailSet().getLanguageVersion();
    try {
      Person person = personManager.getPerson(psnId);
      String emailLanguageVersion = person.getEmailLanguageVersion();
      // 如果语言版本为空或与当前要保存的语言版本不一样，则重新保存邮件语言版本
      if (StringUtils.isEmpty(emailLanguageVersion) || !emailLanguageVersion.equals(languageVersion)) {
        personManager.saveCurrPsnEmailLanguageVersion(languageVersion);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 修改逻辑。现在不需要删除原来的配置，只需要修改配置信息，跟改了的信息有哪些呢 先得到所有的 在改这里又的 改为0没有的全为1
    // 得到所有邮件类型id
    List<ConstMailType> types = this.psnMailSetService.getMailTypeList();
    if (ids != null && ids.trim().length() > 0) {
      String[] idsColl = ids.split(",");
      // 选中的设置为1
      for (ConstMailType type : types) {
        for (String strId : idsColl) {
          Long id = Long.valueOf(strId);
          // 根据id修改值 tsz_scm-5304_2014-6-18
          // PsnMailSet pms=new PsnMailSet(psnId,id,0L);
          if (id.equals(type.getMailTypeId())) {
            PsnMailSet pms = new PsnMailSet(psnId, id, 1l);
            this.psnMailSetService.addOrMidify(pms);
            type.setMailTypeId(null);
          }

        }
      }

    }
    // 没有选中的设置为0
    for (ConstMailType type : types) {
      if (type.getMailTypeId() != null) {
        PsnMailSet pms = new PsnMailSet(psnId, type.getMailTypeId(), 0l);
        this.psnMailSetService.addOrMidify(pms);
      }
    }
    Struts2Utils.renderJson("{\"result\":\"success\"}", "encoding:UTF-8");
    return null;
  }

  /**
   * 确认是否退订邮件
   * 
   * @throws ServiceException
   * @throws UnsupportedEncodingException
   */
  @Action("/psnweb/unsubscribe/mail")
  public String isUnsubscribeMail() throws Exception {
    if (StringUtils.isNotBlank(form.getPsnid()) && StringUtils.isNotBlank(form.getTypeid())) {

      form.setUnsubscribeMailUrl(
          domainscm + "/psnweb/unsubscribe/mail/continue?psnid=" + form.getPsnid() + "&typeid=" + form.getTypeid());

      return "unsubscribeConfirm";
    } else {
      return "error";
    }

  }

  /**
   * 退订邮件
   * 
   * @throws ServiceException
   * @throws UnsupportedEncodingException
   */
  @Action("/psnweb/unsubscribe/mail/continue")
  public String unsubscribeMail() throws Exception {

    if (StringUtils.isNotBlank(form.getPsnid()) && StringUtils.isNotBlank(form.getTypeid())) {
      // 解密敏感信息
      String newpsnId = ServiceUtil.decodeFromDes3(java.net.URLEncoder.encode(form.getPsnid(), "utf-8"));
      String newtypeid = ServiceUtil.decodeFromDes3(java.net.URLEncoder.encode(form.getTypeid(), "utf-8"));

      String cancleMark = psnMailSetService.psnMailSet(newpsnId, newtypeid);
      String mail = psnMailSetService.getMailById(Long.valueOf(newpsnId));
      form.setCancleMark(cancleMark);// 0=失败；1=成功
      form.setMail(mail);
      // SCM-21076 现改为不管怎样都显示退订成功
      return "success";
    } else {
      return "error";
    }

  }

  @Action("/psnweb/cancle/middlepage")
  public String middlePage() {
    return "middlePage";
  }

  /**
   * 
   * @return 无登录帐号
   */
  @Actions({@Action("/psnweb/cancle/success/logout")})
  public String subscribeMailSuccess() {
    return "success";
  }

  /**
   * 
   * @return 有登录帐号
   */
  @Actions({@Action("/psnweb/cancle/success/login")})
  public String subscribeMailAlready() {
    return "success";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnSettingForm();
    }

  }

  @Override
  public PsnSettingForm getModel() {
    return form;
  }

}
