package com.smate.web.psn.service.mobile.homepage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.dao.keywork.PsnScienceAreaDao;
import com.smate.web.psn.dao.keywork.ScienceAreaIdentificationDao;
import com.smate.web.psn.dao.merge.AccountsMergeDao;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.service.brief.PsnBriefService;
import com.smate.web.psn.service.edu.PsnEduHistoryInsInfoService;
import com.smate.web.psn.service.friend.FriendService;
import com.smate.web.psn.service.keyword.PsnKeywordService;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.psncnf.build.PsnCnfBuildService;
import com.smate.web.psn.service.psnwork.PsnWorkHistoryInsInfoService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;
import com.smate.web.psn.service.statistics.PsnStatisticsService;

/**
 * 移动端 个人主页服务
 * 
 * @author tsz
 *
 */

@Service("personHomepageMobileService")
@Transactional(rollbackOn = Exception.class)
public class PersonHomepageMobileServiceImpl implements PersonHomepageMobileService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnWorkHistoryInsInfoService psnWorkHistoryInsInfoService;
  @Autowired
  private PsnEduHistoryInsInfoService psnEduHistoryInsInfoService;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private PsnBriefService psnBriefService;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PsnCnfBuildService cnfBuildService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private ScienceAreaIdentificationDao scienceAreaIdentificationDao;
  @Autowired
  private PsnKeywordService psnKeywordService;
  @Autowired
  private AccountsMergeDao accountsMergeDao;
  @Autowired
  private PersonDao personDao;

  /**
   * 构建个人主页需要的数据
   */
  @Override
  public void buildPsnHomepageData(PsnHomepageMobileForm form) throws PsnException {
    personManager.getBaseInfoForMobilHomepage(form);
    checkBaseInfo(form);
    form.getPerson().setBrief(psnBriefService.getPsnBrief(form.getPsnId()));
    if (StringUtils.isNoneBlank(form.getPerson().getName())) {
      form.setShowName(form.getPerson().getName());
    } else {
      // 查出来的数据没有ename字段，需要进行拼接
      form.setShowName(form.getPerson().getFirstName() + " " + form.getPerson().getLastName());
    }
    if (!form.getIsMyself() && form.getIsFriend() == 0) {
      buildShowTelEmail(form);
    }
    form.setPsnEduList(psnEduHistoryInsInfoService.getPsnEduHistory(form.getPsnId()));
    form.setPsnWorkList(psnWorkHistoryInsInfoService.getPsnWorkHistory(form.getPsnId()));
    form.setKeywordIdentificationForm(psnKeywordService.getPsnKeyWord(form.getPsnId(), 3));
    //
    form.setPsnScienceAreaFormList(scienceAreaService.getPsnScienceAreaFormList(form.getPsnId()));// 获取个人有效的研究领域
    form.setDomain(personManager.getDomain());
    form.setPsnStatistics(psnStatisticsService.getPsnStatistics(form.getPsnId()));
    buildHomPageOauth(form);
    // 重新构建数据 根据权限信息
    // SCM-16767 页面显示内容与否通过anymod和页面上CNF_XXX与运算决定
    // rebuildFormDate(form);
  }

  public String checkBaseInfo(PsnHomepageMobileForm form) throws PsnException {
    // 若psnId不存在，再判断是否是被合并账号 被合并账号的个人主页显示合并账号的个人主页
    if (form.getPerson() == null) {
      Long saveId = accountsMergeDao.findPsnIdByMergePsnId(form.getPsnId());
      if (saveId != null && saveId > 0L) {
        while (personDao.get(saveId) == null && saveId != null) {// 判断是否有多次合并
          saveId = accountsMergeDao.findPsnIdByMergePsnId(saveId);
        }
      }
      if (com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(saveId)) {
        form.setPsnId(saveId);
        personManager.getBaseInfoForMobilHomepage(form);
        return "RESET";
      }
      throw new PsnException("psnid=" + form.getPsnId() + "的人员获取person对象为空");
    } else {
      return "CONTINUE";
    }

  }

  @Override
  public void bulidMyHome(PsnHomepageMobileForm form) throws PsnException {
    personManager.getBaseInfoForMobilHomepage(form);
    if (form.getPerson() == null) {
      throw new PsnException("psnid=" + form.getPsnId() + "的人员获取person对象为空");
    }
    if (StringUtils.isNotBlank(form.getPerson().getName())) {
      form.setShowName(form.getPerson().getName());
    } else {
      // 查出来的数据没有ename字段，需要进行拼接
      form.setShowName(form.getPerson().getFirstName() + " " + form.getPerson().getLastName());
    }
    if (StringUtils.isNotBlank(form.getPerson().getPosition())
        && StringUtils.isNotBlank(form.getPerson().getInsName())) {
      form.setPositionAndInsName(form.getPerson().getInsName() + ',' + ' ' + form.getPerson().getPosition());
    } else if (StringUtils.isNotBlank(form.getPerson().getPosition())) {
      form.setPositionAndInsName(form.getPerson().getPosition());
    } else if (StringUtils.isNotBlank(form.getPerson().getInsName())) {
      form.setPositionAndInsName(form.getPerson().getInsName());
    }

  }

  /**
   * 根据权限信息 重新构建数据
   * 
   * @param form
   */
  private void rebuildFormDate(PsnHomepageMobileForm form) {
    if ("true".equals(form.getOutHomePage())) {
      if (form.getPsnCnfBuild() == null) {
        form.getPerson().setEmail(null);
        form.getPerson().setTel(null);
        form.getPerson().setBrief(null);
        form.setKeywordIdentificationForm(null);
        form.setPsnEduList(null);
        form.setPsnWorkList(null);
        return;
      }
      if (!(form.getPsnCnfBuild().getCnfContact().getAnyUserEmail() >= form.getAnyUser())) {
        form.getPerson().setEmail(null);
      }
      if (!(form.getPsnCnfBuild().getCnfContact().getAnyUserTel() >= form.getAnyUser())) {
        form.getPerson().setTel(null);
      }
      if (!(form.getPsnCnfBuild().getCnfBrief().getAnyUser() >= form.getAnyUser())) {
        form.getPerson().setBrief(null);
      }
      if (!(form.getPsnCnfBuild().getCnfExpertise().getAnyUser() >= form.getAnyUser())) {
        form.setKeywordIdentificationForm(null);
      }
      Iterator<EducationHistory> eduI = form.getPsnEduList().iterator();
      while (eduI.hasNext()) {
        EducationHistory edu = eduI.next();
        PsnConfigEdu psnConfEdu = form.getPsnCnfBuild().getCnfEdu().get(edu.getEduId());
        if (psnConfEdu != null && !(psnConfEdu.getAnyUser() >= form.getAnyUser())) {
          eduI.remove();
        }
      }

      Iterator<WorkHistory> workI = form.getPsnWorkList().iterator();
      while (workI.hasNext()) {
        WorkHistory work = workI.next();
        PsnConfigWork psnConfWork = form.getPsnCnfBuild().getCnfWork().get(work.getWorkId());
        if (psnConfWork != null && !(psnConfWork.getAnyUser() >= form.getAnyUser())) {
          workI.remove();
        }
      }
    }
  }

  /**
   * 构建当前被查看个人主页权限 ,以及 查看人 与被查看人的关系
   * 
   * @param form
   */
  private void buildHomPageOauth(PsnHomepageMobileForm form) {
    // 获取权限配置
    try {
      PsnConfig cnf = psnConfigDao.getByPsn(form.getPsnId());
      if (cnf == null) {
        form.setPsnCnfBuild(null);
      } else {
        form.setPsnCnfBuild(cnfBuildService.get(form.getPsnId()));
      }
    } catch (Exception e) {
      // 获取权限信息失败
      logger.error("获取权限信息失败", e);
      form.setPsnCnfBuild(null);
      // throw new PsnException("获取权限信息失败", e);
    }

    // 获取 内容对查看人的权限 (针对于 查看他人主页 (进入他人主页 需要获取的))
    // 先判断 查看人 与被查看人 是否为好友
    if ("true".equals(form.getOutHomePage())) {
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      if (currentPsnId == null || currentPsnId == 0L) {
        form.setAnyUser(PsnCnfConst.ALLOWS); // 站外请求(匿名用户)
      } else if (friendService.isFriend(currentPsnId, form.getPsnId())) {
        // 好友请求
        form.setAnyUser(PsnCnfConst.ALLOWS_FRIEND | PsnCnfConst.ALLOWS_SELF);
      } else {
        // 科研之友用户 (登陆用户)
        form.setAnyUser(PsnCnfConst.ALLOWS);
      }
    }
  }

  @Override
  public List<PsnScienceArea> findPsnScienceAreaList(Long psnId) throws PsnException {
    List<PsnScienceArea> areaList = psnScienceAreaDao.findPsnScienceAreaList(psnId, 1);
    // 获取认同的人员ID列表
    for (PsnScienceArea area : areaList) {
      List<Long> identifyPsnIds = this.scienceAreaIdentificationDao.findIdentifyPsnIds(psnId, area.getScienceAreaId());
      if (identifyPsnIds != null) {
        area.setIdentifyPsnIds(identifyPsnIds.toString());
      }
      // 不是自己的科技领域要判断是否认同过
      if (!SecurityUtils.getCurrentUserId().equals(psnId) && identifyPsnIds.contains(psnId)) {
        area.setHasIdentified(true);
      }
    }
    return areaList;
  }

  private void buildShowTelEmail(PsnHomepageMobileForm form) {
    String tel = form.getPerson().getTel();
    String email = form.getPerson().getEmail();
    int hidLen = 0;
    if (StringUtils.isNotBlank(tel)) {
      String showTel = "";
      if (tel.length() % 3 == 0) {
        hidLen = tel.length() / 3;
      } else {
        hidLen = tel.length() / 3 + 1;
      }
      int start = (tel.length() - hidLen) / 2;
      int end = start + hidLen;
      for (int i = 0; i < tel.length(); i++) {
        if (i > start && i <= end) {
          showTel += "*";
        } else {
          showTel += tel.charAt(i);
        }
      }
      form.setTel(showTel);
    }
    if (StringUtils.isNotBlank(email) && email.indexOf("@") > 0) {
      String pre = email.split("@")[0];
      String showEmail = "";
      if (pre.length() % 3 == 0) {
        hidLen = pre.length() / 3;
      } else {
        hidLen = pre.length() / 3 + 1;
      }
      for (int i = 0; i < pre.length(); i++) {
        if (i < (pre.length() - hidLen)) {
          showEmail += pre.charAt(i);
        } else {
          showEmail += "*";
        }

      }
      showEmail = showEmail + "@" + email.split("@")[1];
      form.setEmail(showEmail);
    }
  }

  @Override
  public String getPersonUrl(String psnId) {

    return psnProfileUrlDao.get(NumberUtils.toLong(psnId)).getPsnIndexUrl();
  }

  @Override
  public boolean isFriend(Long currentUserId, Long psnId) {
    return friendService.isFriend(currentUserId, psnId);

  }

  @Override
  public void buildPsnInfoConfig(HashMap<String, Object> map, PsnHomepageMobileForm form) throws PsnCnfException {
    PersonProfileForm f = new PersonProfileForm();
    f.setPsnId(form.getPsnId());
    personManager.buildPsnInfoConfig(f);
    Long anyMode = f.getCnfAnyMode();
    if (anyMode != null && anyMode != 0L) {
      int anymode = anyMode.intValue();
      if ((anymode & CNF_PRJ) == CNF_PRJ) {
        map.put("representPrjConf", 1);
      }
      if ((anymode & CNF_PUB) == CNF_PUB) {
        map.put("representPubConf", 1);
      }
      if ((anymode & CNF_WORK) == CNF_WORK) {
        map.put("workHistoryConf", 1);
      }
      if ((anymode & CNF_EDU) == CNF_EDU) {
        map.put("eduHistoryConf", 1);
      }
      if ((anymode & CNF_EXPERTISE) == CNF_EXPERTISE) {
        map.put("keywordsConf", 1);
      }
      if ((anymode & CNF_BRIEF) == CNF_BRIEF) {
        map.put("briefConf", 1);
      }
      if ((anymode & CNF_CONTACT) == CNF_CONTACT) {
        map.put("contactInfoConf", 1);
      }
    }
  }

  public static final int CNF_PRJ = 1 << 6;// 默认状态
  public static final int CNF_PUB = 1 << 7;// 默认状态
  public static final int CNF_WORK = 1 << 8;// 默认状态
  public static final int CNF_EDU = 1 << 9;// 默认状态
  public static final int CNF_EXPERTISE = 1 << 10;// 默认状态
  public static final int CNF_TAUGHT = 1 << 11;// 默认状态
  public static final int CNF_BRIEF = 1 << 12;// 默认状态
  public static final int CNF_CONTACT = 1 << 13;// 默认状态
  public static final int CNF_POSITION = 1 << 14;// 默认状态
  public static final int CNF_SSI = 1 << 15;// 默认状态
  public static final int CNF_HINDEX = 1 << 16;// 默认状态
  public static final int CNF_ALL =
      1 << 6 | 1 << 7 | 1 << 8 | 1 << 9 | 1 << 10 | 1 << 11 | 1 << 12 | 1 << 13 | 1 << 14 | 1 << 15 | 1 << 16;// 默认状态

}
