package com.smate.web.group.service.grp.manage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.center.mail.connector.model.MailLinkInfo;
import com.smate.center.mail.connector.model.MailOriginalDataInfo;
import com.smate.core.base.consts.dao.ConstKeyDiscDao;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpDiscussForm;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.action.grp.form.GrpShowInfo;
import com.smate.web.group.action.grp.form.PrjInfo;
import com.smate.web.group.dao.group.DisciplineDao;
import com.smate.web.group.dao.group.OpenGroupUnionDao;
import com.smate.web.group.dao.group.psn.GrpViewDao;
import com.smate.web.group.dao.group.unread.GroupUnreadMsgDao;
import com.smate.web.group.dao.grp.file.GrpFileDao;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpControlDao;
import com.smate.web.group.dao.grp.grpbase.GrpIndexUrlDao;
import com.smate.web.group.dao.grp.grpbase.GrpKwDiscDao;
import com.smate.web.group.dao.grp.grpbase.GrpStatisticsDao;
import com.smate.web.group.dao.grp.grpbase.PrjGroupRelationDao;
import com.smate.web.group.dao.grp.job.TmpTaskInfoRecordDao;
import com.smate.web.group.dao.grp.member.CategoryMapBaseDao;
import com.smate.web.group.dao.grp.member.GrpMemberDao;
import com.smate.web.group.dao.grp.member.GrpProposerDao;
import com.smate.web.group.dao.grp.pub.GrpPubRcmdDao;
import com.smate.web.group.dao.grp.pub.GrpPubsDao;
import com.smate.web.group.model.group.psn.GrpView;
import com.smate.web.group.model.group.pub.Discipline;
import com.smate.web.group.model.group.pub.PubInfo;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.model.grp.grpbase.GrpIndexUrl;
import com.smate.web.group.model.grp.grpbase.GrpKwDisc;
import com.smate.web.group.model.grp.job.TmpTaskInfoRecord;
import com.smate.web.group.model.grp.member.CategoryMapBase;
import com.smate.web.group.model.grp.member.GrpMember;
import com.smate.web.group.model.grp.member.GrpProposer;
import com.smate.web.group.service.grp.keywords.GrpKeywordsHotService;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.pub.GrpPubRcmdService;


/**
 * 群组信息业务处理service实现类
 * 
 * @author zzx
 */
@Service("grpBaseService")
@Transactional(rollbackFor = Exception.class)
public class GrpBaseServiceImpl implements GrpBaseService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private GrpProposerDao grpProposerDao;
  @Autowired
  private GrpPubRcmdService grpPubRcmdService;
  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private GrpControlDao grpControlDao;
  @Autowired
  private DisciplineDao disciplineDao;
  @Autowired
  private ConstKeyDiscDao constKeyDiscDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private OpenShortUrlDao openShortUrlDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private GrpIndexUrlDao grpIndexUrlDao;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private GrpKeywordsHotService grpKeywordsHotService;
  @Autowired
  private GroupUnreadMsgDao groupUnreadMsgDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;
  @Value(value = "${sendEmail.restful.url}")
  private String sendRestfulUrl;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PrjGroupRelationDao prjGroupRelationDao;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private GrpViewDao grpViewDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;

  @SuppressWarnings("unchecked")
  @Override
  public void getGrpInfo(GrpMainForm form) {
    if (form.getGrpId() != null && form.getGrpId() != 0L) {
      GrpShowInfo gsi = new GrpShowInfo();
      GrpBaseinfo baseinfo = grpBaseInfoDao.get(form.getGrpId());
      gsi.setGrpBaseInfo(baseinfo);
      // 判断是否是基金委的项目
      gsi.setIsNsfcPrj(this.grpBaseInfoDao.isNsfcProject(form.getGrpId()) == false ? 0 : 1);
      GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
      if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())
          && !"P".equals(baseinfo.getOpenType())) { // 隐私群组不显示短地址
        gsi.setGrpIndexUrl(domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
      }
      GrpKwDisc grpKwDisc = grpKwDiscDao.get(form.getGrpId());
      if (grpKwDisc != null) {
        gsi.setGrpKwDisc(grpKwDisc);
        String keywords = grpKwDisc.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
          String[] str = keywords.split(";");
          if (str != null && str.length > 0) {
            List<String> grpKwList = new ArrayList<String>();
            for (String s : str) {
              if (StringUtils.isNotBlank(s)) {
                grpKwList.add(s.replaceAll("<[^<]*>", "").replaceAll("77&", ";"));
              }
            }
            gsi.setGrpKeywordList(grpKwList);
          }
        }
        boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
        if (grpKwDisc.getFirstCategoryId() != null) {
          CategoryMapBase c1 = categoryMapBaseDao.get(grpKwDisc.getFirstCategoryId());
          if (c1 != null) {
            form.setFirstDisciplinetName(iszhCN ? c1.getCategoryZh() : c1.getCategoryEn());
            gsi.setFirstDisciplinetName(iszhCN ? c1.getCategoryZh() : c1.getCategoryEn());
          }
        }
        if (grpKwDisc.getSecondCategoryId() != null) {
          CategoryMapBase c2 = categoryMapBaseDao.get(grpKwDisc.getSecondCategoryId());
          if (c2 != null) {
            form.setSecondDisciplinetName(iszhCN ? c2.getCategoryZh() : c2.getCategoryEn());
            gsi.setSecondDisciplinetName(iszhCN ? c2.getCategoryZh() : c2.getCategoryEn());
          }
        }
      }
      Long toConfirmPubCounts = 0L;
      toConfirmPubCounts = this.grpPubRcmdDao.getPendingConfirmedCount(form.getGrpId());
      gsi.setGrpPendingConfirmPubs(toConfirmPubCounts);
      gsi.setGrpStatistic(grpStatisticsDao.get(form.getGrpId()));
      gsi.setGrpId(form.getGrpId());
      buildGrpStatistic(gsi);
      gsi.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
      gsi.setGrpControl(grpControlDao.get(form.getGrpId()));
      gsi.setIsTop(grpMemberDao.getTopGrp(form.getGrpId(), form.getPsnId()));
      gsi.setGrpListIndex(
          NumberUtils.toInt(Objects.toString(grpMemberDao.findGrpRownum(form.getGrpId(), form.getPsnId()), "0")));
      try {
        // 与项目有关联的群组，显示一些项目的信息
        HashMap<String, Object> resultMap = getGrpPrjInfo(form.getGrpId() + "");
        gsi.setPrjInfo(dealDate(resultMap));
      } catch (Exception e) {
        logger.error("获取群组关联的项目信息异常，grpId={}", form.getGrpId(), e);
      }
      // 增加群组访问记录
      if ("1".equals(form.getIsViewGrpDetail())) {
        insertGrpView(form);
      }

      Long firstPsnId = grpMemberDao.getMembersForOne(form.getGrpId());
      if (firstPsnId != null) {
        form.setDes3FirstPsnId(Des3Utils.encodeToDes3(firstPsnId.toString()));
      }
      boolean isGrpMember = true;
      if (gsi.getRole() == 1 || gsi.getRole() == 2 || gsi.getRole() == 3) {
        isGrpMember = false;
      }
      if (isGrpMember && "H".equals(baseinfo.getOpenType())) {
        gsi.setShowDyn(grpIsShowIndexOpen(baseinfo, gsi.getGrpControl()));// 是否显示动态内容
      } else {
        gsi.setShowDyn("1");
      }
      form.setGrpShowInfo(gsi);
    }
  }

  // 增加群组访问记录
  private void insertGrpView(GrpMainForm form) {
    try {
      long formateDate = DateUtils.getDateTime(new Date());
      String ip = Struts2Utils.getRemoteAddr();
      if (StringUtils.isNotBlank(form.getViewIp())) {
        ip = form.getViewIp();
      }
      GrpView grpView = grpViewDao.findGrpView(form.getPsnId(), form.getGrpId(), formateDate, ip);
      if (grpView == null) {
        grpView = new GrpView();
        grpView.setGrpId(form.getGrpId());
        grpView.setViewPsnId(form.getPsnId());
        grpView.setIp(ip);
        grpView.setGmtCreate(new Date());
        grpView.setFormateDate(formateDate);
        grpView.setTotalCount(1l);
      } else {
        grpView.setGmtCreate(new Date());
        grpView.setFormateDate(formateDate);
        long viewCount = grpView.getTotalCount() == null ? 0 : grpView.getTotalCount();
        grpView.setTotalCount(viewCount + 1);
      }
      grpViewDao.saveOrUpdate(grpView);
    } catch (Exception e) {
      logger.error("群组访问记录插入异常,grpId=" + form.getGrpId() + ",psnId=" + form.getPsnId(), e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public PrjInfo dealDate(HashMap<String, Object> resultMap) {
    PrjInfo prjInfo = null;
    if (MapUtils.isNotEmpty(resultMap) && "success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
      if (StringUtils.isNotBlank(String.valueOf(resultMap.get("prjInfo")))) {
        prjInfo = new PrjInfo();
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) resultMap.get("prjInfo");
        prjInfo.setAgencyName(String.valueOf(map.get("agencyName")));
        prjInfo.setEnAgencyName(String.valueOf(map.get("enAgencyName")));
        Object amount = map.get("amount");
        prjInfo.setAmount(amount == null || amount == "" ? null : new BigDecimal(String.valueOf(amount)));
        if (amount != null && NumberUtils.isDigits(amount.toString())) {
          prjInfo.setAmountFormat(MoneyFormatterUtils.formatterMoney(Long.parseLong(amount.toString())) + ".");
        }
        prjInfo.setAmountUnit(String.valueOf(map.get("amountUnit")));
        prjInfo.setInsName(String.valueOf(map.get("insName")));
        Object startDay = map.get("startDay");
        prjInfo.setStartDay(startDay == null || startDay == "" ? null : (Integer) startDay);
        Object startMonth = map.get("startMonth");
        prjInfo.setStartMonth(startMonth == null || startMonth == "" ? null : (Integer) startMonth);
        Object startYear = map.get("startYear");
        prjInfo.setStartYear(startYear == null || startYear == "" ? null : (Integer) startYear);
        Object endDay = map.get("endDay");
        prjInfo.setEndDay(endDay == null || endDay == "" ? null : (Integer) endDay);
        Object endMonth = map.get("endMonth");
        prjInfo.setEndMonth(endMonth == null || endMonth == "" ? null : (Integer) endMonth);
        Object endYear = map.get("endYear");
        prjInfo.setEndYear(endYear == null || endYear == "" ? null : (Integer) endYear);
        prjInfo.setSchemeName(String.valueOf(map.get("schemeName")));
        prjInfo.setEnSchemeName(String.valueOf(map.get("enSchemeName")));
      }
    }
    return prjInfo;
  }

  @SuppressWarnings("unchecked")
  @Override
  public HashMap<String, Object> getGrpPrjInfo(String des3GrpId) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3GrpId", Des3Utils.encodeToDes3(des3GrpId));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<MultiValueMap<String, String>>(params, headers);
    HashMap<String, Object> resultMap = (HashMap<String, Object>) restTemplate
        .postForObject(domainscm + "/prjdata/project/relationgrpinfo", requestEntity, Map.class);
    return resultMap;
  }

  @Override
  public void getMyGrpInfoList(GrpMainForm form) {
    List<Long> grpIds = null;
    // SearchByRole： 1=所有我的群组， 2=我管理的群组，3=我是普通成员的群组，4=待批准的群组
    // grpCategory： 群组分类 10:兴趣群组 ， 11项目群组
    grpIds = getMyGrpIdsBySearchRole(form);
    // 构建群组列表显示信息
    buildGrpShowInfoList(form, grpIds);
  }

  /**
   * 根据群组权限查询群组Id
   * 
   * @param form
   * @return
   */
  private List<Long> getMyGrpIdsBySearchRole(GrpMainForm form) {
    // SearchByRole： 1=所有我的群组， 2=我管理的群组，3=我是普通成员的群组，4=待批准的群组
    List<Long> grpIds = new ArrayList<Long>();
    List<Long> grpIds1 =
        grpMemberDao.getMyGrpIds(form.getPsnId(), form.getSearchByRole(), form.getGrpCategory(), form.getSearchKey());// 我的群组
    List<Long> grpIds2 = grpProposerDao.getMyGrpIds(form.getPsnId(), form.getGrpCategory(), form.getSearchKey());// 待批准的群组
    if (form.getSearchByRole() == null) {
      form.setSearchByRole(1);
    }
    switch (form.getSearchByRole()) {
      case 1: // 1=所有我的群组
        grpIds.addAll(grpIds1);
        grpIds.addAll(grpIds2);
        break;
      case 2:// 2=我管理的群组
        grpIds.addAll(grpIds1);
        break;
      case 3:// 3=我是普通成员的群组
        grpIds.addAll(grpIds1);
        break;
      case 4:// 4=待批准的群组
        grpIds.addAll(grpIds2);
        break;
      default:
        grpIds.addAll(grpIds1);
        grpIds.addAll(grpIds2);
        break;
    }
    return grpIds;
  }

  /**
   * 构建群组列表显示信息
   * 
   * @param form
   * @param grpIds
   */
  private void buildGrpShowInfoList(GrpMainForm form, List<Long> grpIds) {
    if (grpIds != null && grpIds.size() > 0) {
      List<GrpBaseinfo> baseInfoList = grpBaseInfoDao.getGrpBaseInfoList(grpIds, form);
      if (baseInfoList != null && baseInfoList.size() > 0) {
        List<GrpShowInfo> grpShowInfoList = new ArrayList<GrpShowInfo>();
        GrpShowInfo grpShowInfo = null;
        for (GrpBaseinfo g : baseInfoList) {
          grpShowInfo = new GrpShowInfo();
          GrpProposer grpProposer = grpProposerDao.getByPsnIdAndGrpId(form.getPsnId(), g.getGrpId(), 1, 2);
          if (grpProposer != null) {// 说明是申请群组
            grpShowInfo.setIsTop(0);
            grpShowInfo.setIsApplyGrp(1);// 是否是申请群组， 1=是，0=否
          } else {
            grpShowInfo.setIsApplyGrp(0);
            grpShowInfo.setIsTop(grpMemberDao.getTopGrp(g.getGrpId(), form.getPsnId()));
            grpShowInfo.setRole(grpRoleService.getGrpRole(form.getPsnId(), g.getGrpId()));
          }
          grpShowInfo.setIsGrpUnion(isUnionGrp(g.getGrpId()));
          getPendingCount(g.getGrpId(), form.getPsnId(), grpShowInfo);
          grpShowInfo.setGrpBaseInfo(g);
          GrpKwDisc grpKwDisc = grpKwDiscDao.get(g.getGrpId());
          boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
          if (grpKwDisc != null) {
            grpShowInfo.setGrpKwDisc(grpKwDisc);
            if (grpKwDisc.getFirstCategoryId() != null) {
              CategoryMapBase c1 = categoryMapBaseDao.get(grpKwDisc.getFirstCategoryId());
              if (c1 != null) {
                grpShowInfo.setFirstDisciplinetName(iszhCN ? c1.getCategoryZh() : c1.getCategoryEn());
              }
            }
            if (grpKwDisc.getSecondCategoryId() != null) {
              CategoryMapBase c2 = categoryMapBaseDao.get(grpKwDisc.getSecondCategoryId());
              if (c2 != null) {
                grpShowInfo.setSecondDisciplinetName(iszhCN ? c2.getCategoryZh() : c2.getCategoryEn());
              }
            }
          }
          grpShowInfo.setGrpStatistic(grpStatisticsDao.get(g.getGrpId()));
          grpShowInfo.setGrpId(grpShowInfo.getGrpBaseInfo().getGrpId());
          buildGrpStatistic(grpShowInfo);
          grpShowInfo.setDes3GrpId(Des3Utils.encodeToDes3(g.getGrpId().toString()));
          // 设置未读数
          Long count = groupUnreadMsgDao.getGroupUnreadCount(form.getPsnId(), g.getGrpId());
          grpShowInfo.setGroupUnreadCount(count == null ? 0 : count);
          grpShowInfoList.add(grpShowInfo);
        }
        form.setGrpShowInfoList(grpShowInfoList);
      }
    }
  }

  /**
   * 是否关联群组
   * 
   * @return
   */
  private Integer isUnionGrp(Long grpId) {
    return openGroupUnionDao.IsExist(grpId);
  }

  /**
   * 获取未处理事项的数量
   * 
   * @param grpId
   * @return
   */
  private void getPendingCount(Long grpId, Long psnId, GrpShowInfo grpShowInfo) {
    Integer role = grpRoleService.getGrpRole(psnId, grpId);
    if (role == 1 || role == 2) {
      Long PendingApprovalCount = grpProposerDao.getPendingApproval(grpId);
      Long pendingConfirmedCount = grpPubRcmdService.getPendingConfirmedCount(grpId);
      if (pendingConfirmedCount > 0) {
        grpShowInfo.setPendIngCountType(2);
      } else {
        grpShowInfo.setPendIngCountType(1);
      }
      grpShowInfo.setPendIngCount((PendingApprovalCount.intValue() + pendingConfirmedCount.intValue()));
    }
  }

  @Override
  public boolean checkGrpIsExist(Long grpId) {
    if (grpId != null) {
      return grpBaseInfoDao.isExist(grpId);
    } else {
      return false;
    }
  }

  public GrpBaseinfo getCurrGrp(Long grpId) {
    return grpBaseInfoDao.get(grpId);
  }

  public static void main(String[] args) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("1", "a");
    map.put("2", "b");
    System.out.println(map);
    map.put("2", "c");
    System.out.println(map);
  }

  @Override
  public void getMyGrpInfoListCallBack(GrpMainForm form) {
    List<Long> groupIds = null;
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> grpCategoryMap = new HashMap<String, String>();
    Map<String, String> searchByRoleMap = new HashMap<String, String>();
    // 设置默认值为0
    grpCategoryMap.put("10", "0");
    grpCategoryMap.put("11", "0");
    grpCategoryMap.put("12", "0");
    searchByRoleMap.put("1", "0");
    searchByRoleMap.put("2", "0");
    searchByRoleMap.put("3", "0");
    searchByRoleMap.put("4", "0");// 我的待批准群组数
    Integer grpCategory = form.getGrpCategory();
    form.setGrpCategory(null);
    groupIds = getMyGrpIdsBySearchRole(form);
    if (groupIds != null && groupIds.size() > 0) {
      List<Map<String, Object>> grpCategoryCount = grpBaseInfoDao.getGrpCategoryCount(groupIds);// 兴趣群组、项目群组、课程群组群组数
      for (Map<String, Object> m : grpCategoryCount) {// 遍历放到map
        grpCategoryMap.put(m.get("grpCategory").toString(), m.get("count").toString());
      }
    }
    form.setGrpCategory(grpCategory);
    form.setSearchByRole(1);
    groupIds = getMyGrpIdsBySearchRole(form);
    if (groupIds != null && groupIds.size() > 0) {
      List<Map<String, Object>> roleCount = grpMemberDao.getRoleCount(groupIds, form.getPsnId());// 我加入的群组数（我的管理、普通群组）
      for (Map<String, Object> m : roleCount) {// 遍历放到map、统计所有我加入的群组数量
        searchByRoleMap.put(m.get("grpRole").toString(), m.get("count").toString());
      }
      searchByRoleMap.put("2",
          Integer.parseInt(searchByRoleMap.get("1")) + Integer.parseInt(searchByRoleMap.get("2")) + "");
      searchByRoleMap.put("4",
          (groupIds.size() - Integer.parseInt(searchByRoleMap.get("2")) - Integer.parseInt(searchByRoleMap.get("3")))
              + "");
    }
    map.put("grpCategory", grpCategoryMap);
    map.put("searchByRole", searchByRoleMap);
    form.setResult2Map(map);
  }

  @Override
  public void getGrpDesc(GrpDiscussForm form) {
    form.setGrpDesc(grpBaseInfoDao.getGrpDesc(form.getGrpId()));
  }

  @Override
  public String getGrpDesc(Long grpId) {
    return grpBaseInfoDao.getGrpDesc(grpId);
  }

  @Override
  public Integer getGrpCategory(Long grpId) throws Exception {
    return grpBaseInfoDao.getGrpCatetory(grpId);
  }

  @Override
  public void setGrpTop(GrpMainForm form) {
    GrpMember grpMember = grpMemberDao.getByPsnIdAndGrpId(form.getPsnId(), form.getGrpId());
    if (grpMember != null) {
      grpMember.setTopDate(form.getSetTopOpt().intValue() == 1 ? new Date() : null);
      grpMemberDao.save(grpMember);
    }
  }

  @Override
  public void delMyGrp(GrpMainForm form) throws Exception {
    GrpBaseinfo baseinfo = grpBaseInfoDao.get(form.getGrpId());
    if (baseinfo != null) {
      baseinfo.setStatus("99");
      grpBaseInfoDao.save(baseinfo);
      this.saveTmpTaskInfoRecord(baseinfo.getGrpId());
      PsnStatistics psnStatistics = psnStatisticsDao.get(form.getPsnId());
      if (psnStatistics != null) {
        psnStatistics.setGroupSum(psnStatistics.getGroupSum() == null ? 0 : (psnStatistics.getGroupSum() - 1));
        // 属性为null的保存为0
        PsnStatisticsUtils.buildZero(psnStatistics);
        psnStatisticsDao.save(psnStatistics);
      }
      // SCM-22523 项目-在项目列表创建的群组，删除该群组后，项目列表应变为“创建群组”
      prjGroupRelationDao.delPrjGroupRelation(baseinfo.getGrpId());
      List<Long> memberList = grpMemberDao.getGrpMembers(form.getGrpId());
      Person sender = personDao.get(form.getPsnId());
      for (Long psnId : memberList) {
        if (psnId.longValue() == form.getPsnId().longValue()) {
          continue;
        }
        // 给其他组员发送邮件
        Person receiver = personDao.get(psnId);
        restSendDelGrpEmail(baseinfo, sender, receiver);
      }
    }
  }

  /**
   * 调用接口发送删除群组邮件
   * 
   * @param baseinfo
   * @param sender
   * @param receiver
   * @throws Exception
   */
  public void restSendDelGrpEmail(GrpBaseinfo baseinfo, Person sender, Person receiver) throws Exception {
    // 群组删除
    if (sender == null || receiver == null || baseinfo == null) {
      throw new Exception("构建群组删除回复，邮件对象为空" + this.getClass().getName());
    }
    // 定义接口接收的参数
    Map<String, String> paramData = new HashMap<String, String>();
    // 定义构造邮件模版参数集
    Map<String, Object> mailData = new HashMap<String, Object>();
    String language = "";
    language = receiver.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = LocaleContextHolder.getLocale().toString();
    }
    mailData.put("sender", getPersonName(sender, language));
    mailData.put("receiver", getPersonName(receiver, language));
    mailData.put("groupName", baseinfo.getGrpName());
    // 构造必需的参数
    MailOriginalDataInfo info = new MailOriginalDataInfo();
    Integer tempCode = 10022;
    info.setMailTemplateCode(tempCode);
    info.setMsg("删除群组邮件");
    info.setSenderPsnId(sender.getPersonId());
    info.setReceiver(receiver.getEmail());
    info.setReceiverPsnId(receiver.getPersonId());
    paramData.put("mailOriginalData", JacksonUtils.jsonObjectSerializerNoNull(info));
    // 跟踪链接 根据key放置到模板中 所有的链接不再需要放到mailData中
    List<String> linkList = new ArrayList<String>();
    MailLinkInfo l1 = new MailLinkInfo();
    l1.setKey("domainUrl");
    l1.setUrl(domainscm);
    l1.setUrlDesc("科研之友首页");
    linkList.add(JacksonUtils.jsonObjectSerializer(l1));
    MailLinkInfo l2 = new MailLinkInfo();
    l2.setKey("recvUrl");
    l2.setUrl(this.domainscm + "/groupweb/mygrp/main?model=myGrp");
    l2.setUrlDesc("群组列表链接");
    linkList.add(JacksonUtils.jsonObjectSerializer(l2));
    mailData.put("linkList", JacksonUtils.listToJsonStr(linkList));
    paramData.put("mailData", JacksonUtils.mapToJsonStr(mailData));
    restTemplate.postForObject(this.sendRestfulUrl, paramData, Object.class);
  }


  /**
   * 
   * @param person
   * @param language zh=中文
   * @return
   */
  String getPersonName(Person person, String language) {
    if ("zh".equalsIgnoreCase(language) || "zh_CN".equalsIgnoreCase(language)) {
      if (StringUtils.isNotBlank(person.getName())) {
        return person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getEname();
      }
    } else {
      if (StringUtils.isNotBlank(person.getEname())) {
        return person.getEname();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        return person.getFirstName() + " " + person.getLastName();
      } else {
        return person.getName();
      }
    }
  }

  @Override
  public void modifyGrpPermissions(GrpMainForm form) throws Exception {
    GrpControl control = grpControlDao.get(form.getGrpId());
    if (control != null) {
      control.setIsIndexDiscussOpen(form.getIsIndexDiscussOpen());
      control.setIsIndexFileOpen(form.getIsIndexFileOpen());
      control.setIsIndexMemberOpen(form.getIsIndexMemberOpen());
      control.setIsIndexPubOpen(form.getIsIndexPubOpen());
      control.setIsCurwareFileShow(form.getIsCurwareFileShow());
      control.setIsWorkFileShow(form.getIsWorkFileShow());
      control.setIsPrjPubShow(form.getIsPrjPubShow());
      control.setIsPrjRefShow(form.getIsPrjRefShow());
      grpControlDao.save(control);
    }
  }

  @Override
  public void getAllMyGrp(GrpMainForm form) throws Exception {
    List<GrpBaseinfo> list = grpBaseInfoDao.getAllMyGrpBaseinfos(form);
    form.setGrpBaseInfoList(list);
  }

  @Override
  public void searchMyJoinedGrp(GrpMainForm form) throws ServiceException {
    form.setGrpBaseInfoList(grpBaseInfoDao.searchMyGrpBaseinfos(form));
  }

  @Override
  public void getSecondDisciplineListById(GrpMainForm form) throws Exception {
    List<CategoryMapBase> list = categoryMapBaseDao.getSubCategoryMapBaseList(form.getfCategoryId().intValue());
    List<Discipline> disciplineList = new ArrayList<Discipline>();
    // 判断语言
    Locale locale = LocaleContextHolder.getLocale();
    if (list != null && list.size() > 0) {
      if (Locale.US.equals(locale)) {
        for (CategoryMapBase c : list) {
          Discipline d = new Discipline();
          d.setSecondCategoryZhName(c.getCategoryEn());
          d.setSecondCategoryId(c.getCategryId().longValue());
          disciplineList.add(d);
        }
      } else {
        for (CategoryMapBase c : list) {
          Discipline d = new Discipline();
          d.setSecondCategoryZhName(c.getCategoryZh());
          d.setSecondCategoryId(c.getCategryId().longValue());
          disciplineList.add(d);
        }
      }
    }
    form.setDisciplineList(disciplineList);
  }

  @Override
  public List<GrpBaseinfo> getGroupNames(GrpMainForm form) throws Exception {
    return grpBaseInfoDao.getAutoGrpNames(form);
  }

  @Override
  public List<ConstKeyDisc> getConstKeyDiscs(GrpMainForm form) throws Exception {
    return constKeyDiscDao.findKeys(form.getSearchKey(), form.getKeywordsSize());
  }

  @Override
  public void getHasGrpIviteInfo(GrpMainForm form) throws Exception {
    if (grpProposerDao.isHasIviteGrp(form.getPsnId())) {
      form.setHasIvite("1");
    } else {
      form.setHasIvite("0");
    }
    // 没有群组邀请，查询一下，请求群组的
    List<GrpProposer> queryGrpReq = grpProposerDao.queryGrpReq(form.getPsnId(), null);
    if (queryGrpReq != null && queryGrpReq.size() > 0) {
      form.setHasReqGrp("1");
    }
  }

  @Override
  public void getHasIviteGrpList(GrpMainForm form) throws Exception {
    List<GrpProposer> hasIviteList = grpProposerDao.hasIviteList(form.getPsnId());
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    if (hasIviteList != null && hasIviteList.size() > 0) {
      List<GrpShowInfo> grpShowInfoList = new ArrayList<GrpShowInfo>();
      GrpShowInfo grpShowInfo = null;
      for (GrpProposer g : hasIviteList) {
        grpShowInfo = new GrpShowInfo();
        // 获取群组信息
        GrpBaseinfo grpBaseinfo = grpBaseInfoDao.getGrpBaseinfoForIvite(g.getGrpId());
        if (grpBaseinfo != null) {
          grpShowInfo.setGrpId(g.getGrpId());
          grpShowInfo.setGrpBaseInfo(grpBaseinfo);
          // 获取群组统计信息
          grpShowInfo.setGrpStatistic(grpStatisticsDao.get(g.getGrpId()));
          buildGrpStatistic(grpShowInfo);
          // 关联记录
          grpShowInfo.setIsGrpUnion(isUnionGrp(g.getGrpId()));
          // 获取群组关键词和领域
          GrpKwDisc grpKwDisc = grpKwDiscDao.get(g.getGrpId());
          if (grpKwDisc != null) {
            grpShowInfo.setGrpKwDisc(grpKwDisc);
            if (grpKwDisc.getFirstCategoryId() != null) {
              CategoryMapBase c1 = categoryMapBaseDao.get(grpKwDisc.getFirstCategoryId());
              if (c1 != null) {
                grpShowInfo.setFirstDisciplinetName(iszhCN ? c1.getCategoryZh() : c1.getCategoryEn());
              }
            }
            if (grpKwDisc.getSecondCategoryId() != null) {
              CategoryMapBase c2 = categoryMapBaseDao.get(grpKwDisc.getSecondCategoryId());
              if (c2 != null) {
                grpShowInfo.setSecondDisciplinetName(iszhCN ? c2.getCategoryZh() : c2.getCategoryEn());
              }
            }
          }
          // 获取群组邀请人信息
          Person person = personDao.getPersonNameNotId(g.getInviterId());
          if (person != null) {
            // 人员短地址的获取
            PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(g.getInviterId());
            if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
              grpShowInfo
                  .setGrpInviterUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
            }
            String locale = LocaleContextHolder.getLocale().toString();
            if ("en_US".equals(locale)) {
              grpShowInfo.setGrpInviterName(person.getEname());
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName())
                    + " " + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
              } else if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName(person.getName());
              }
            } else {
              grpShowInfo.setGrpInviterName(person.getName());
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName())
                    + " " + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
              } else if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName(person.getEname());
              }
            }
          }
          GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(g.getGrpId());
          if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
            grpShowInfo.setGrpIndexUrl(domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
          }
          grpShowInfoList.add(grpShowInfo);
        }
      }
      form.setGrpShowInfoList(grpShowInfoList);
    }
  }

  @Override
  public void checkMyGrp(GrpMainForm form) {
    Long count = grpBaseInfoDao.getMyGrpCount(form.getPsnId());
    form.setGrpCount(count);
  }

  @Override
  public Long getGrpIdByGroupCode(String groupCode) throws Exception {
    return openGroupUnionDao.findGroupIdByGroupCode(groupCode);
  }

  @Override
  public void saveGrpShortUrl(GrpMainForm form) throws Exception {
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    if (!openShortUrlDao.isExIst(form.getNewShortUrl())) {
      openShortUrlDao.modifierShortUrl(form.getOldShortUrl(), form.getNewShortUrl());
      GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(form.getGrpId());
      if (grpIndexUrl != null) {
        grpIndexUrl.setGrpIndexUrl(form.getNewShortUrl());
        grpIndexUrl.setUpdateDate(new Date());
        grpIndexUrl.setPsnId(form.getPsnId());
      }
      form.getResultMap().put("result", "success");
      form.getResultMap().put("msg", iszhCN ? "短地址已修改！" : "ShortUrl has been update successfully.");
    } else {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", iszhCN ? "短地址已存在！" : "This shortUrl already exists.");
    }
  }

  @Override
  public void grpListForMain(GrpMainForm form) throws Exception {
    form.setGrpShowInfoList(new ArrayList<GrpShowInfo>());
    List<GrpBaseinfo> grpList = grpBaseInfoDao.getGrpList(SecurityUtils.getCurrentUserId(), form.getPage());
    if (CollectionUtils.isNotEmpty(grpList)) {
      GrpShowInfo grpShowInfo = null;
      for (GrpBaseinfo g : grpList) {
        grpShowInfo = new GrpShowInfo();
        grpShowInfo.setGrpAuatars(g.getGrpAuatars());
        grpShowInfo.setGrpName(g.getGrpName());
        GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(g.getGrpId());
        if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
          grpShowInfo.setGrpIndexUrl(domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
        } else {
          grpShowInfo.setGrpIndexUrl(
              "/groupweb/grpinfo/main?des3GrpId=" + Des3Utils.encodeToDes3(g.getGrpId().toString()) + "&model=discuss");
        }
        // 设置未读数
        Long count = groupUnreadMsgDao.getGroupUnreadCount(form.getPsnId(), g.getGrpId());
        grpShowInfo.setGroupUnreadCount(count == null ? 0 : count);
        form.getGrpShowInfoList().add(grpShowInfo);
      }
    }
  }

  @Override
  public void queryGrpReq(GrpMainForm form) throws Exception {
    List<GrpProposer> grpProposerList = null;
    if (form.getIsAll() == 1) {
      // 请求全部
      grpProposerList = grpProposerDao.queryAllGrpReq(SecurityUtils.getCurrentUserId(), form.getPage());
    } else {
      // 请求两条
      grpProposerList = grpProposerDao.queryGrpReq(SecurityUtils.getCurrentUserId(), form.getPage());
    }
    buildGrpReqInfo(grpProposerList, form);
  }

  private void buildGrpReqInfo(List<GrpProposer> list, GrpMainForm form) {
    form.setGrpShowInfoList(new ArrayList<GrpShowInfo>());
    if (list != null && list.size() > 0) {
      boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
      GrpShowInfo grpShowInfo = null;
      for (GrpProposer g : list) {
        grpShowInfo = new GrpShowInfo();
        // 获取群组信息
        GrpBaseinfo grpBaseinfo = grpBaseInfoDao.getGrpBaseinfoForIvite(g.getGrpId());
        if (grpBaseinfo != null) {
          grpShowInfo.setGrpId(g.getGrpId());
          grpShowInfo.setDes3GrpId(Des3Utils.encodeToDes3(g.getGrpId().toString()));
          grpShowInfo.setGrpBaseInfo(grpBaseinfo);
          grpShowInfo.setGrpName(grpBaseinfo.getGrpName());
          GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(g.getGrpId());
          if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
            grpShowInfo.setGrpIndexUrl(domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
          }
          // 获取请求加入群组的人信息
          Person person = personDao.findPersonInsAndPos(g.getPsnId());
          if (person != null) {
            PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(g.getPsnId());
            if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
              grpShowInfo
                  .setGrpInviterUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
            }
            grpShowInfo.setGrpInviterDes3psnId(Des3Utils.encodeToDes3(g.getPsnId().toString()));
            grpShowInfo.setGrpInviterInsName(person.getInsName());
            grpShowInfo.setGrpInviterAvatars(person.getAvatars());
            grpShowInfo.setGrpInviterDepartment(person.getPosition());
            String locale = LocaleContextHolder.getLocale().toString();
            if ("en_US".equals(locale)) {
              grpShowInfo.setGrpInviterName(person.getEname());
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName())
                    + " " + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
              }
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName(person.getName());
              }
            } else {
              grpShowInfo.setGrpInviterName(person.getName());
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName())
                    + " " + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
              }
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName(person.getEname());
              }
            }
          }
          form.getGrpShowInfoList().add(grpShowInfo);
        }
      }
    }
  }

  @Override
  public void queryGrpInvite(GrpMainForm form) throws Exception {
    List<GrpProposer> grpProposerList = null;
    if (form.getIsAll() == 1) {
      // 查询全部
      grpProposerList = grpProposerDao.queryAllGrpInvite(SecurityUtils.getCurrentUserId(), form.getPage());
    } else {
      // 查询一条
      grpProposerList = grpProposerDao.queryGrpInvite(SecurityUtils.getCurrentUserId(), form.getPage());
    }
    buildGrpInviteInfo(grpProposerList, form);
  }

  private void buildGrpInviteInfo(List<GrpProposer> list, GrpMainForm form) {
    form.setGrpShowInfoList(new ArrayList<GrpShowInfo>());
    if (list != null && list.size() > 0) {
      boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
      GrpShowInfo grpShowInfo = null;
      for (GrpProposer g : list) {
        grpShowInfo = new GrpShowInfo();
        // 获取群组信息
        GrpBaseinfo grpBaseinfo = grpBaseInfoDao.getGrpBaseinfoForIvite(g.getGrpId());
        if (grpBaseinfo != null) {
          grpShowInfo.setGrpId(g.getGrpId());
          grpShowInfo.setDes3GrpId(Des3Utils.encodeToDes3(g.getGrpId().toString()));
          grpShowInfo.setGrpBaseInfo(grpBaseinfo);
          grpShowInfo.setGrpAuatars(grpBaseinfo.getGrpAuatars());
          grpShowInfo.setGrpName(grpBaseinfo.getGrpName());
          // 获取群组统计信息
          grpShowInfo.setGrpStatistic(grpStatisticsDao.getSumPubsAndSumMember(g.getGrpId()));
          // 获取群组关键词和领域
          GrpKwDisc grpKwDisc = grpKwDiscDao.get(g.getGrpId());
          if (grpKwDisc != null) {
            grpShowInfo.setGrpKwDisc(grpKwDisc);
            CategoryMapBase categoryMapBase = categoryMapBaseDao.get(grpKwDisc.getFirstCategoryId());
            if (categoryMapBase != null) {
              grpShowInfo
                  .setFirstDisciplinetName(iszhCN ? categoryMapBase.getCategoryZh() : categoryMapBase.getCategoryEn());
            }
            CategoryMapBase categoryMapBase2 = categoryMapBaseDao.get(grpKwDisc.getSecondCategoryId());
            if (categoryMapBase2 != null) {
              grpShowInfo.setSecondDisciplinetName(
                  iszhCN ? categoryMapBase2.getCategoryZh() : categoryMapBase2.getCategoryEn());
            }
          }
          // 短地址
          GrpIndexUrl grpIndexUrl = grpIndexUrlDao.get(g.getGrpId());
          if (grpIndexUrl != null && StringUtils.isNotBlank(grpIndexUrl.getGrpIndexUrl())) {
            grpShowInfo.setGrpIndexUrl(domainscm + "/" + ShortUrlConst.G_TYPE + "/" + grpIndexUrl.getGrpIndexUrl());
          }
          // 获取邀请加入群组的人信息
          Person person = personDao.findPersonInsAndPos(g.getInviterId());
          if (person != null) {
            PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(person.getPersonId());
            if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
              grpShowInfo
                  .setGrpInviterUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
            }
            grpShowInfo.setGrpInviterDes3psnId(Des3Utils.encodeToDes3(person.getPersonId().toString()));
            String locale = LocaleContextHolder.getLocale().toString();
            if ("en_US".equals(locale)) {
              grpShowInfo.setGrpInviterName(person.getEname());
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName())
                    + " " + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
              }
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName(person.getName());
              }
            } else {
              grpShowInfo.setGrpInviterName(person.getName());
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName((StringUtils.isBlank(person.getFirstName()) ? "" : person.getFirstName())
                    + " " + (StringUtils.isBlank(person.getLastName()) ? "" : person.getLastName()));
              }
              if (StringUtils.isBlank(grpShowInfo.getGrpInviterName())) {
                grpShowInfo.setGrpInviterName(person.getEname());
              }
            }
          }
          form.getGrpShowInfoList().add(grpShowInfo);
        }
      }
    }
  }

  /**
   * 构建群组统计数
   * 
   * @param grpShowInfo
   */
  public void buildGrpStatistic(GrpShowInfo grpShowInfo) {
    if (grpShowInfo.getGrpBaseInfo().getGrpCategory() == 10) {
      // 课程群组 <!-- 成员，文献，课件，作业 -->
      // 课件
      Long courseFileSum = grpFileDao.countGrpFile(grpShowInfo.getGrpId(), 2);
      // 作业
      Long workFileSum = grpFileDao.countGrpFile(grpShowInfo.getGrpId(), 1);
      grpShowInfo.setGrpCourseFileSum(courseFileSum);
      grpShowInfo.setGrpWorkFileSum(workFileSum);
    } else if (grpShowInfo.getGrpBaseInfo().getGrpCategory() == 11) {
      // 项目群组<!-- 成员，成果，文献，文件 -->
      // 文献
      Long projectRefSum = grpPubsDao.countGrpPubsSum(grpShowInfo.getGrpId(), 0);
      // 成果
      Long projectPubSum = grpPubsDao.countGrpPubsSum(grpShowInfo.getGrpId(), 1);
      grpShowInfo.setGrpProjectPubSum(projectPubSum);
      grpShowInfo.setGrpProjectRefSum(projectRefSum);
    }
  }

  @Override
  public Map<String, List<KeywordsHot>> getGroupRcmdKeywords(GrpMainForm form) throws Exception {
    PubInfo info = new PubInfo();
    // 封装获取关键词热词的请求参数.
    String locale = LocaleContextHolder.getLocale().toString();
    info = this.setPubValueInfo(form, info, locale);
    // 获取推荐seo热词
    Map<String, List<KeywordsHot>> mapKeyword = grpKeywordsHotService.queryPubRefHotKeyByLang(info);
    mapKeyword = this.rebuildKwMap(form, mapKeyword);
    return mapKeyword;
  }

  /**
   * 封装获取关键词热词的请求参数<如果群组名称或摘要包含中文内容则只匹配中文关键词>.
   * 
   * @param form
   * @param info
   * @param locale
   * @return
   */
  private PubInfo setPubValueInfo(GrpMainForm form, PubInfo info, String locale) {
    if (ServiceUtil.isChineseStr(form.getGrpName()) || ServiceUtil.isChineseStr(form.getGrpDescription())) {
      info.setZhTitle(form.getGrpName());
      info.setZhAbs(form.getGrpDescription());
    } else {
      info.setEnTitle(form.getGrpName());
      info.setEnAbs(form.getGrpDescription());
    }
    return info;
  }

  /**
   * 重新构建关键词列表(排除重复的关键词).
   * 
   * @param mapKeyword
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, List<KeywordsHot>> rebuildKwMap(GrpMainForm form, Map<String, List<KeywordsHot>> mapKeyword) {
    Map<String, List<KeywordsHot>> result = MapBuilder.getInstance().getMap();
    if (mapKeyword != null && mapKeyword.size() > 0) {
      List<KeywordsHot> zhResultList = new ArrayList<KeywordsHot>();
      List<KeywordsHot> zhKeywordList = mapKeyword.get("zh");
      if (CollectionUtils.isNotEmpty(zhKeywordList)) {
        for (KeywordsHot kwHot : zhKeywordList) {
          if (!zhResultList.contains(kwHot)) {
            zhResultList.add(kwHot);
          }
        }
        zhResultList = rebulidKwList(form, zhResultList);
      }
      result.put(RECOMMEND_KW_KEY_ZH, zhResultList);
      List<KeywordsHot> enResultList = new ArrayList<KeywordsHot>();
      List<KeywordsHot> enKeywordList = mapKeyword.get("en");
      if (CollectionUtils.isNotEmpty(enKeywordList)) {
        for (KeywordsHot kwHot : enKeywordList) {
          if (!enResultList.contains(kwHot)) {
            enResultList.add(kwHot);
          }
        }
        enResultList = rebulidKwList(form, enResultList);
      }
      result.put(RECOMMEND_KW_KEY_EN, enResultList);
    }
    return result;
  }

  /**
   * 过滤群组已添加的关键词
   *
   * @param kwList
   * @return
   */
  private List<KeywordsHot> rebulidKwList(GrpMainForm form, List<KeywordsHot> kwList) {
    Long grpId = form.getGrpId();
    if (grpId == null || grpId == 0L) {
      return kwList;
    }
    GrpKwDisc grpKwDisc = grpKwDiscDao.get(grpId);
    List<String> grpKwList = new ArrayList<String>();
    if (grpKwDisc != null) {
      String keywords = grpKwDisc.getKeywords();
      if (StringUtils.isNotBlank(keywords)) {
        String[] str = keywords.split(";");
        if (str != null && str.length > 0) {
          for (String s : str) {
            if (StringUtils.isNotBlank(s)) {
              grpKwList.add(s.replaceAll("<[^<]*>", "").replaceAll("77&", ";"));
            }
          }
        }
      }
    }
    if (!CollectionUtils.isEmpty(grpKwList)) {
      Iterator<KeywordsHot> it;
      for (String keywords : grpKwList) {
        it = kwList.iterator();
        while (it.hasNext()) {
          KeywordsHot keywordsHot = it.next();
          if (keywordsHot.getEkeywords().equals(keywords) || keywordsHot.getEkwTxt().equals(keywords)
              || keywordsHot.getKeywords().equals(keywords) || keywordsHot.getKwTxt().equals(keywords)) {
            it.remove();
          }
        }
      }
      return kwList;
    } else {
      return kwList;
    }
  }

  @Override
  public void buildCategoryMapBaseInfo(GrpMainForm form) {
    String scienceAreaIds = form.getScienceAreaIds();
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级科技领域
    List<CategoryMapBase> firstLevelList = this.findCategoryMapBaseFirstLevelList();
    List<CategoryMapBase> subLevelList = new ArrayList<CategoryMapBase>();
    List<CategoryMapBase> scienceAreaList = new ArrayList<CategoryMapBase>();
    for (CategoryMapBase cate : firstLevelList) {
      // 二级科技领域
      subLevelList = this.findSubCategoryMapBaseList(cate.getCategryId());
      // 检查人员已选科技领域
      if (StringUtils.isNotBlank(scienceAreaIds)) {
        for (CategoryMapBase ca : subLevelList) {
          if (scienceAreaIds.contains(ca.getCategryId().toString())) {
            ca.setAdded(true);
            cate.setAdded(true);
            scienceAreaList.add(ca);
          }
        }
      }
      allData.put("CategoryMap_sub" + cate.getCategryId().toString(), subLevelList);
    }
    List<CategoryMapBase> sortAreaList = new ArrayList<CategoryMapBase>();
    String[] selectIds = scienceAreaIds.split(",");
    for (String id : selectIds) {// 排序
      for (CategoryMapBase area : scienceAreaList) {
        if (id.equals(area.getCategryId().toString())) {
          sortAreaList.add(area);
        }
      }
    }
    allData.put("CategoryMap_first", firstLevelList);
    if (allData == null || allData.isEmpty()) {
      allData.put("isNull", true);
    } else {
      allData.put("isNull", false);
    }
    form.setCategoryMap(allData);
    form.setCategoryList(sortAreaList);
  }

  public List<CategoryMapBase> findCategoryMapBaseFirstLevelList() {
    List<CategoryMapBase> list = categoryMapBaseDao.getCategoryMapBaseFirstLevelList();
    if (CollectionUtils.isNotEmpty(list)) {
      for (CategoryMapBase ca : list) {
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          ca.setShowCategory(ca.getCategoryEn());
        } else {
          ca.setShowCategory(ca.getCategoryZh());
        }
      }
    }
    return list;
  }

  public List<CategoryMapBase> findSubCategoryMapBaseList(Integer superCategoryId) {
    List<CategoryMapBase> list = categoryMapBaseDao.getSubCategoryMapBaseList(superCategoryId);
    if (CollectionUtils.isNotEmpty(list)) {
      for (CategoryMapBase ca : list) {
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          ca.setShowCategory(ca.getCategoryEn());
        } else {
          ca.setShowCategory(ca.getCategoryZh());
        }
      }
    }
    return list;
  }

  public GrpControl getCurrGrpControl(Long grpId) {
    return grpControlDao.getCurrGrpControl(grpId);
  }

  /**
   * 获取群组详情是否展示首页
   */
  public String grpIsShowIndexOpen(GrpBaseinfo grpBaseinfo, GrpControl currGrpControl) {
    int grpCategory = grpBaseinfo.getGrpCategory();
    String flag = "1";
    if ("0".equals(currGrpControl.getIsIndexMemberOpen())) {
      flag = "0";
    }
    // 课程群组
    if (grpCategory == 10) {
      if ("0".equals(currGrpControl.getIsCurwareFileShow()) || "0".equals(currGrpControl.getIsWorkFileShow())
          || "0".equals(currGrpControl.getIsIndexPubOpen())) {
        flag = "0";
      }
    } else if (grpCategory == 11) {
      // 项目群组
      if ("0".equals(currGrpControl.getIsPrjPubShow()) || "0".equals(currGrpControl.getIsPrjRefShow())
          || "0".equals(currGrpControl.getIsIndexFileOpen())) {
        flag = "0";
      }
    } else {
      if ("0".equals(currGrpControl.getIsIndexPubOpen()) || "0".equals(currGrpControl.getIsIndexFileOpen())) {
        flag = "0";
      }
    }
    return flag;
  }

  @Override
  public Integer getProposerCount(Long grpId) {
    return grpProposerDao.getProposerCount(grpId).intValue();
  }

  @Override
  public void saveTmpTaskInfoRecord(Long grpId) {
    // 保存到TMP_TASK_INFO_RECORD，群组推荐任务
    TmpTaskInfoRecord record = tmpTaskInfoRecordDao.getJobByhandleId(grpId, 24);
    if (record != null) {
      record.setStatus(0);
      record.setHandletime(new Date());
    } else {
      record = new TmpTaskInfoRecord(grpId, 24, new Date());
    }
    tmpTaskInfoRecordDao.saveOrUpdate(record);
  }
}
