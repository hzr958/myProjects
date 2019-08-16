package com.smate.center.task.service.fund;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.fund.rcmd.ConstFundAgencyDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryDao;
import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryRegionDao;
import com.smate.center.task.dao.fund.sns.FundAgencyInterestDao;
import com.smate.center.task.dao.fund.sns.FundRecommendAreaDao;
import com.smate.center.task.dao.fund.sns.FundRecommendRegionDao;
import com.smate.center.task.dao.fund.sns.FundRecommendSeniorityDao;
import com.smate.center.task.dao.fund.sns.PsnFundRecommendDao;
import com.smate.center.task.dao.sns.psn.PsnScienceAreaDao;
import com.smate.center.task.dao.sns.psn.UserUnionLoginLogDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapBaseDao;
import com.smate.center.task.dao.sns.quartz.CategoryScmDao;
import com.smate.center.task.dao.sns.quartz.ConstPositionDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.fund.rcmd.ConstFundAgency;
import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.fund.rcmd.FundRecommendContext;
import com.smate.center.task.model.fund.sns.FundAgencyInterest;
import com.smate.center.task.model.fund.sns.FundRecommendSeniority;
import com.smate.center.task.model.fund.sns.PsnFundRecommend;
import com.smate.center.task.model.sns.pub.CategoryScm;
import com.smate.center.task.model.sns.pub.ConstPosition;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.model.security.Person;

@Service("psnFundRecommendService")
@Transactional(rollbackOn = Exception.class)
public class PsnFundRecommendServiceImpl implements PsnFundRecommendService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnFundRecommendDao psnFundRecommendDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private ConstFundAgencyDao constfundAgencyDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private ConstPositionDao constPositionDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;
  @Autowired
  private FundRecommendRegionDao fundRecommendRegionDao;
  @Autowired
  private FundRecommendAreaDao fundRecommendAreaDao;
  @Autowired
  private CategoryScmDao categoryScmDao;
  @Autowired
  private FundRecommendSeniorityDao fundRecommendSeniorityDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private UserUnionLoginLogDao userUnionLoginLogDao;

  @Override
  public List<ConstFundCategory> initFundList() {
    return constFundCategoryDao.getFundListInfo();
  }

  /**
   * 获取人员列表.
   *
   * @param appValue
   * @param lastPsnId
   * @return
   * @throws ServiceException
   */
  @Override
  public List<Long> getTaskPsnList(Long lastPsnId, Integer type) throws ServiceException {
    return userUnionLoginLogDao.getTaskPsnList(lastPsnId, type);
  }

  /**
   * 构建必要条件所需参数.
   */
  @Override
  public void buildPsnInfo(Long psnId, FundRecommendContext context) {
    // 查询人员身份
    Integer grade = this.findPsnGrades(psnId);
    String areaIds = "";
    String agencyIds = "";
    Integer seniority = null;
    areaIds = getPsnSetAreaId(psnId);
    seniority = getPsnSeniority(psnId);
    agencyIds = this.getPsnFundAgencyIdsList(psnId);
    context.setAgencyIds(agencyIds);
    context.setScienceAreaIds(areaIds);
    context.setGrade(grade);
    context.setSeniority(Objects.toString(seniority));
  }

  private String getInitFundAgencyIdsList(Long psnId) {
    List<Long> fundAgencyIdList = new ArrayList<Long>();
    // 用人员自身信息作为推荐条件
    Long regionId = personProfileDao.findPsnRegionId(psnId);
    if (regionId == null) {
      Person psn = personProfileDao.getPsnInsData(psnId);
      regionId = this.getInsRegion(psn);
    }
    List<ConstFundAgency> countryFundAgencyList = constfundAgencyDao.getCountryFundAgencyList();// 设置国家级的
    if (CollectionUtils.isNotEmpty(countryFundAgencyList) && fundAgencyIdList.size() < 10) {
      for (ConstFundAgency agency : countryFundAgencyList) {
        Long agencyId = Optional.of(agency).map(ConstFundAgency::getId).orElse(null);
        if (agencyId != null) {
          fundAgencyIdList.add(agencyId);
        }
      }
    }
    if (fundAgencyIdList.size() < 10) {
      if (regionId != null) {
        fundAgencyIdList.add(regionId);
      }
    }
    return StringUtils.join(fundAgencyIdList, ",");
  }

  private String getPsnFundAgencyIdsList(Long psnId) {
    List<FundAgencyInterest> fundAgencyInterestList = fundAgencyInterestDao.fundAgencyInteresByPsnId(psnId);
    return Optional.ofNullable(fundAgencyInterestList).map(
        list -> list.stream().map(agency -> Objects.toString(agency.getAgencyId())).collect(Collectors.joining(",")))
        .orElseGet(() -> "");
  }

  /**
   * 按默认的
   *
   * @param psnId
   * @return
   */
  private String getInitRegionNames(Long psnId) {
    // 用人员自身信息作为推荐条件
    Long regionId = personProfileDao.findPsnRegionId(psnId);
    String zhRegionNames = "";
    Locale locale = LocaleContextHolder.getLocale();
    if (regionId == null) {
      Person psn = personProfileDao.getPsnInsData(psnId);
      regionId = this.getInsRegion(psn);
    }
    if (regionId != null) {
      ConstRegion constRegion = constRegionDao.findRegionNameById(regionId);
      if (constRegion != null) {
        // 获取所有父级id
        List<Long> superRegionIds = new ArrayList<Long>();
        if (regionId == 158 && regionId == 344 && regionId == 446) {// 中国香港、澳门、台湾要显示
          superRegionIds = constRegionDao.getSuperRegionList(regionId, false);
        } else {
          superRegionIds = constRegionDao.getSuperRegionList(regionId, true);
          if (regionId == 156 || !superRegionIds.contains(156L)) {// 中国或其他外国地区就排除
            return "";
          } else {
            superRegionIds.remove(156L);
          }
        }
        superRegionIds.add(regionId);// 把原本要插入的regionID也放进去，可能重复
        Set<Long> set = new HashSet<Long>(superRegionIds);
        zhRegionNames = getRegionName(locale, psnId, StringUtils.strip(set.toString(), "[]"));
      }
    }
    return zhRegionNames;
  }

  /**
   * 按默认的
   *
   * @param psnId
   * @return
   */
  private String getInitAreaName(Long psnId) {
    List<Long> scienceAreaIds = psnScienceAreaDao.findPsnScienceAreaIds(psnId);
    Locale locale = LocaleContextHolder.getLocale();
    String areaName = "";
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {
      List<Long> areaIdList = new ArrayList<Long>();
      for (int i = 0; i < (scienceAreaIds.size() > 3 ? 3 : scienceAreaIds.size()); i++) {// 保存最多3个科技领域
        Long areaId = NumberUtils.toLong(scienceAreaIds.get(i) + "");
        if (areaId != null) {
          areaIdList.add(areaId);
        }
      }
      areaName = getAreaName(locale, psnId, StringUtils.strip(areaIdList.toString(), "[]"));
    }
    return areaName;
  }

  /**
   * 按默认的
   *
   * @param psnId
   * @return
   */
  private Integer getInitSeniorityName(Long psnId) {
    Long insId = personProfileDao.getPsnInsId(psnId);
    Integer nature = 0;
    if (insId != null) {
      Long insNature = institutionDao.getInsNatureByInsId(insId);
      if (insNature == null) {// 不是企业或科研单位设置为其他
        nature = 0;
      } else if (insNature == 4) {// 生产机4是表示企业
        nature = 1;
      } else {// 其他算是科研机构
        nature = 2;
      }
    }
    return nature;
  }

  /**
   * 获取用户设置的地区名称
   *
   * @param locale
   * @param psnId
   * @return
   */
  private String getPsnSetRegionName(Locale locale, Long psnId) {
    List<Long> psnSetRegionId = fundRecommendRegionDao.getPsnSetRegionId(psnId);
    return getRegionName(locale, psnId, StringUtils.strip(psnSetRegionId.toString(), "[]"));
  }

  /**
   * 获取用户选中的地区名称
   *
   * @param locale
   * @param psnId
   * @param regionCodeSelect
   * @return
   */
  private String getRegionName(Locale locale, Long psnId, String regionCodeSelect) {
    List<String> namesAllList = new ArrayList<String>();
    if (StringUtils.isNotBlank(regionCodeSelect)) {
      String[] codeList = StringUtils.split(regionCodeSelect, ",");
      if (codeList.length > 0) {
        for (int i = 0; i < codeList.length; i++) {
          Long regionId = NumberUtils.toLong(StringUtils.trim(codeList[i]));
          if (regionId != 0) {
            ConstRegion constRegion = constRegionDao.findRegionNameById(regionId);
            if (constRegion != null) {
              namesAllList.add(LocaleTextUtils.getLocaleText(locale, constRegion.getZhName(), constRegion.getEnName()));
            }
          }
        }
      }
    }
    namesAllList = new ArrayList<String>(new HashSet<String>(namesAllList)); // 去重复
    return StringUtils.strip(namesAllList.toString(), "[]");
  }

  /**
   * 获取用户设置的科技领域ID
   * 
   * @param locale
   * @param psnId
   * @return
   */
  private String getPsnSetAreaId(Long psnId) {
    List<Long> psnSetAreaId = fundRecommendAreaDao.getPsnSetAreaId(psnId);
    if (psnSetAreaId != null && psnSetAreaId.size() > 0) {
      return psnSetAreaId.stream().map(a -> a.toString()).collect(Collectors.joining(" "));
    }
    return "";
  }

  /**
   * 获取用户选中的科技领域名称
   *
   * @param locale
   * @param psnId
   * @param scienceCodeSelect
   * @return
   */
  private String getAreaName(Locale locale, Long psnId, String scienceCodeSelect) {
    List<String> namesList = new ArrayList<String>();
    if (StringUtils.isNotBlank(scienceCodeSelect)) {
      String[] codeList = StringUtils.split(scienceCodeSelect, ",");
      if (codeList.length > 0) {
        for (int i = 0; i < codeList.length; i++) {
          Long areaId = NumberUtils.toLong(StringUtils.trim(codeList[i]));
          if (areaId != 0) {
            CategoryScm categoryScm = categoryScmDao.get(areaId);
            if (categoryScm != null) {
              namesList
                  .add(LocaleTextUtils.getLocaleText(locale, categoryScm.getCategoryZh(), categoryScm.getCategoryEn()));
            }
            /*
             * Long parentRegion = categoryScm.getParentCategroyId(); if (parentRegion != null) { CategoryScm
             * parentcategoryScm = categoryScmDao.get(parentRegion); if (parentcategoryScm != null) {
             * namesList.add(LocaleTextUtils.getLocaleText(locale, parentcategoryScm.getCategoryZh(),
             * parentcategoryScm.getCategoryEn())); } }
             */
          }
        }
      }
    }
    namesList = new ArrayList<String>(new HashSet<String>(namesList)); // 去重复
    return StringUtils.strip(namesList.toString(), "[]");
  }

  /**
   * 获取用户设置单位性质
   *
   * @param locale
   * @param psnId
   * @return
   */
  private Integer getPsnSeniority(Long psnId) {
    FundRecommendSeniority psnSeniority = fundRecommendSeniorityDao.getSeniorityByPsnId(psnId);
    return psnSeniority != null ? psnSeniority.getCode() : null;
  }

  /**
   * 人员最高学历
   *
   * @param personId
   * @return
   */
  private Integer findPsnGrades(Long personId) {
    Person psn = this.personProfileDao.findPsnDegreeAndPosition(personId);
    Integer degree = -1;
    if (psn != null) {
      if (StringUtils.isNotBlank(psn.getPosition())) {
        ConstPosition pos = constPositionDao.getPosByName(psn.getPosition());
        if (pos != null) {
          degree = pos.getGrades();
        }
      }
      if (degree == 0 && StringUtils.isNotBlank(psn.getDegreeName())) {
        ConstPosition pos = constPositionDao.getPosByName(psn.getPosition());
        if (pos != null) {
          degree = pos.getGrades();
        }
      }
    }
    return degree;
  }

  /**
   * 保存更新人员推荐的基金(新的实现方法)_MJG_SCM_5444_2014-07-17.
   *
   * @param reFundList
   * @param psnId
   * @throws ServiceException
   */
  @Override
  public void saveReFundList(PsnFundRecommend reFund) throws ServiceException {
    PsnFundRecommend oldReFund = psnFundRecommendDao.findPsnFundRecommend(reFund.getFundId(), reFund.getPsnId());
    // 保存之前没有推荐的基金记录
    if (oldReFund == null) {
      psnFundRecommendDao.save(reFund);
    }
  }

  /**
   * 获取人员首要工作单位的region_id .
   *
   * @param person
   * @return
   */
  private Long getInsRegion(Person person) {
    Long insRegionId = null;
    try {
      Institution institute = null;
      if (person.getInsId() != null) {
        institute = institutionDao.findById(person.getInsId());
      } else if (StringUtils.isNotBlank(person.getInsName())) {
        institute = institutionDao.findByName(person.getInsName());
      }
      if (institute != null) {
        insRegionId = institute.getRegionId();
      }
    } catch (Exception e) {
      logger.error("获取人员首要单位的regionId出错, psnId=" + (person != null ? person.getPersonId() : "null"));
    }
    return insRegionId;
  }

  @Override
  public List<Long> getFundRegionId(Long fundId) {
    return constFundCategoryRegionDao.findFundRegionId(fundId);
  }


  @Override
  public void deletePsnFundRecommend(Long fundId, Date updateDate) {
    psnFundRecommendDao.deletePsnFundRecommend(fundId, updateDate);
  }

  @Override
  public void deletePsnFundRecommend(Long psnId) {
    psnFundRecommendDao.deletePsnFundRecommend(psnId);
  }


}
