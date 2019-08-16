package com.smate.web.psn.action.mobile.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.dynamic.DynamicConstant;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.service.mobile.homepage.PersonHomepageMobileService;
import com.smate.web.psn.service.profile.PersonalManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.representprj.RepresentPrjService;
import com.smate.web.psn.service.statistics.VistStatisticsService;

/**
 * SCM APP 个人首页接口
 * 
 * @author LJ
 *
 *         2017年7月14日
 */
public class APPPersonHomepageAction extends ActionSupport implements ModelDriven<PsnHomepageMobileForm>, Preparable {
  private static final long serialVersionUID = -1981406713017046661L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private PsnHomepageMobileForm form;
  @Autowired
  private PersonHomepageMobileService personHomepageMobileService;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private RepresentPrjService representPrjService;
  @Value("${domainoauth}")
  private String domainoauth;
  @Value("${domainscm}")
  private String domainscm;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Autowired
  private VistStatisticsService vistStatisticsService;
  @Autowired
  private PersonalManager personalManager;

  @Action("/app/psnweb/outside/mobile/ajaxrepresentprj")
  public void showRepresentPrj() {
    List<Project> list = new ArrayList<Project>();
    status = IOSHttpStatus.OK;
    try {
      boolean isMyself = false;
      Long currentUserId = SecurityUtils.getCurrentUserId();
      String des3OtherPsnId = form.getDes3OtherPsnId();
      Long otherPsnId = 0L;
      if (StringUtils.isNotBlank(des3OtherPsnId)) {
        otherPsnId = Long.parseLong(Des3Utils.decodeFromDes3(des3OtherPsnId));
      }
      if (currentUserId.equals(otherPsnId)) {
        isMyself = true;
      }
      if (otherPsnId != 0L) {
        form.setPsnId(otherPsnId);
        list = representPrjService.buildMobilePsnRepresentPrjInfo(form.getPsnId(), isMyself);
      }
    } catch (Exception e) {
      logger.error("移动端显示人员代表性项目出错， psnId = " + form.getPsnId(), e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }
    AppActionUtils.renderAPPReturnJson(list, list.size(), status);
  }

  /**
   * 进入个人主页
   * 
   * @return
   * @throws Exception
   */
  @Action("/app/psnweb/homepage")
  public String myHomepage() {

    HashMap<String, Object> map = new HashMap<String, Object>();
    try {

      form.setIsMyself(true);
      personHomepageMobileService.buildPsnHomepageData(form);
      status = IOSHttpStatus.OK;
      map.put("person", form.getPerson());
      map.put("psnWorkList", form.getPsnWorkList());
      map.put("psnEduList", form.getPsnEduList());
      map.put("psnStatistics", form.getPsnStatistics());
      List<PsnScienceArea> PsnScienceAreaList = personHomepageMobileService.findPsnScienceAreaList(form.getPsnId());
      map.put("psnScienceArea", PsnScienceAreaList);

      String personUrl = null;
      try {
        personUrl = personHomepageMobileService.getPersonUrl(form.getPsnId().toString());
      } catch (Exception e) {
        logger.error("获取个人主页短地址出错，psnId:" + form.getPsnId());
      }
      if (personUrl == null) {
        map.put("QRCodeMsg", "");
      } else {
        map.put("QRCodeMsg", domainscm + "/P/" + personUrl);
      }
      map.put("QRCodeImg", domainscm + "/resmod/images/wechat/iconsmate.jpg");

    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("进入个人主页页面异常！", e);
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);

    return null;
  }

  /**
   * 显示关键词信息
   * 
   * @return
   */
  @Action("/app/psnweb/outside/mobile/ajaxkeywords")
  public String showPsnKeyWords() {
    try {
      // 先看下是否有传人员ID
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId())));
      }
      // 没有传人员ID，则认为是本人
      if (NumberUtils.isNullOrZero(form.getPsnId())) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
      // 获取认同记录的人员头像
      if (CollectionUtils.isNotEmpty(keyList)) {
        for (PsnDisciplineKey key : keyList) {
          psnDisciplineKeyService.findSomeIdentifyKwPsnIds(form.getPsnId(), key);
        }
      }
      status = IOSHttpStatus.OK;
      form.setKeywords(keyList);
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("移动端获取人员关键词出错， psnId = " + form.getPsnId(), e);
    }
    AppActionUtils.renderAPPReturnJson(form.getKeywords(), total, status);

    return null;
  }

  /**
   * 进入他人主页
   * 
   * @return
   * @throws Exception
   */
  @Action("/app/psnweb/otherhomepage")
  public String otherHomepage() {
    HashMap<String, Object> map = new HashMap<String, Object>();

    if (StringUtils.isNotBlank(form.getDes3ViewPsnId())) {
      try {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3ViewPsnId())));
        Long currentPsnId = SecurityUtils.getCurrentUserId();
        form.setIsMyself(true);
        personHomepageMobileService.buildPsnHomepageData(form);
        boolean isfriend = personHomepageMobileService.isFriend(currentPsnId, form.getPsnId());
        if (isfriend == true) {
          form.getPerson().setIsMyFriend(2);// 是我的好友
        } else {
          form.getPerson().setIsMyFriend(0);// 不是我的好友
        }
        status = IOSHttpStatus.OK;
        map.put("person", form.getPerson());
        map.put("psnWorkList", form.getPsnWorkList() == null ? new ArrayList<WorkHistory>() : form.getPsnWorkList());
        map.put("psnEduList", form.getPsnEduList() == null ? new ArrayList<EducationHistory>() : form.getPsnEduList());
        map.put("psnStatistics", form.getPsnStatistics());
        List<PsnScienceArea> PsnScienceAreaList = personHomepageMobileService.findPsnScienceAreaList(form.getPsnId());
        map.put("psnScienceArea", PsnScienceAreaList == null ? new ArrayList<PsnScienceArea>() : PsnScienceAreaList);

        String personUrl = null;
        try {
          personUrl = personHomepageMobileService.getPersonUrl(form.getPsnId().toString());
        } catch (Exception e) {
          logger.error("获取个人主页短地址出错！，psnId:" + form.getPsnId());
        }
        if (personUrl == null) {
          map.put("QRCodeMsg", "");
        } else {
          map.put("QRCodeMsg", domainscm + "/P/" + personUrl);
        }
        map.put("QRCodeImg", domainscm + "/resmod/images/wechat/iconsmate.jpg");
        // 构造他人模块权限
        personHomepageMobileService.buildPsnInfoConfig(map, form);
        // 添加访问统计数
        if (!currentPsnId.equals(form.getPsnId()) && form.getPsnId() != null) {
          String ip = Struts2Utils.getRemoteAddr();
          vistStatisticsService.addVistRecord(currentPsnId, form.getPsnId(), form.getPsnId(),
              DynamicConstant.RES_TYPE_PSNRESUME, ip);
        }
      } catch (PsnCnfException e) {
        personalManager.initPsnConfigInfoByTask(form.getPsnId());
        logger.error("个人模块配置为空，进行初始化，psnId = " + form.getPsnId(), e);
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("进入个人主页页面异常！", e);
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;
    }

    AppActionUtils.renderAPPReturnJson(map, total, status);

    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PsnHomepageMobileForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public PsnHomepageMobileForm getModel() {
    return form;
  }
}
