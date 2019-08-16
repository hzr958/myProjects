package com.smate.web.management.service.other;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.dao.other.fund.CategoryMapBaseDao;
import com.smate.web.management.dao.other.fund.ConstFundAgencyDao;
import com.smate.web.management.dao.other.fund.ConstFundCategoryDao;
import com.smate.web.management.dao.other.fund.ConstFundCategoryDisDao;
import com.smate.web.management.dao.other.fund.ConstFundCategoryFileDao;
import com.smate.web.management.dao.other.fund.ConstFundCategoryKeywordsDao;
import com.smate.web.management.dao.other.fund.ConstFundCategoryRegionDao;
import com.smate.web.management.dao.other.fund.ConstFundPositionDao;
import com.smate.web.management.model.other.fund.CategoryMapBase;
import com.smate.web.management.model.other.fund.ConstFundAgency;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.ConstFundCategoryDis;
import com.smate.web.management.model.other.fund.ConstFundCategoryFile;
import com.smate.web.management.model.other.fund.ConstFundCategoryRegion;
import com.smate.web.management.model.other.fund.ConstFundCategorykeywords;
import com.smate.web.management.model.other.fund.ConstFundPosition;
import com.smate.web.management.model.other.fund.FundForm;
import com.smate.web.management.model.other.fund.FundLeftMenu;

@Service("bpoFundService")
@Transactional(rollbackFor = Exception.class)
public class BpoFundServiceImpl implements BpoFundService {
  /**
   * 
   */
  private static final long serialVersionUID = 7603977418174465406L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundPositionDao constFundPositionDao;
  @Autowired
  private ConstFundCategoryKeywordsDao constFundCategoryKeywordsDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private ConstFundCategoryFileDao constFundCategoryFileDao;
  @Autowired
  private ConstFundCategoryRegionDao constFundCategoryRegionDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;

  private String getLocaleName(String nameZh, String nameEn) {
    String name = "";
    Locale local = LocaleContextHolder.getLocale();
    if (local.equals(Locale.US)) {
      name = StringUtils.isBlank(nameEn) ? nameZh : nameEn;
    } else {
      name = StringUtils.isBlank(nameZh) ? nameEn : nameZh;
    }
    return name;
  }

  @Override
  public List<FundLeftMenu> getConstFundAgencyLeftMenu() {
    List<FundLeftMenu> newList = new ArrayList<FundLeftMenu>();
    try {
      List<ConstFundPosition> List = constFundPositionDao.getFundLeftMenu();
      for (ConstFundPosition pos : List) {
        FundLeftMenu menu = null;
        List<FundLeftMenu> nextList = null;
        int count = 0;
        switch (pos.getId().intValue()) {
          case 110:// 国际
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 120:// 国家级
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {// bpo-9 资助机构左边栏的国家级，不用出现二级选项
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 130:// 省级
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              List<ConstRegion> prvList = constFundAgencyDao.findConstFundRegionByAgency(pos.getId());
              nextList = new ArrayList<FundLeftMenu>();
              for (ConstRegion reg : prvList) {
                FundLeftMenu prvLeft = new FundLeftMenu();
                prvLeft.setId(reg.getId());
                prvLeft.setName(this.getLocaleName(reg.getZhName(), reg.getEnName()));
                nextList.add(prvLeft);
              }
              menu.setList(nextList);
              newList.add(menu);
            }
            break;
          case 140:// 地市级
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              List<ConstRegion> prvList = constFundAgencyDao.findConstFundRegionByAgency(pos.getId());
              nextList = new ArrayList<FundLeftMenu>();
              for (ConstRegion reg : prvList) {
                FundLeftMenu prvLeft = new FundLeftMenu();
                prvLeft.setId(reg.getId());
                prvLeft.setName(this.getLocaleName(reg.getZhName(), reg.getEnName()));
                nextList.add(prvLeft);
              }
              menu.setList(nextList);
              newList.add(menu);
            }
            break;
          case 141:// 区县级
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 150:// 企业类
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 160:// 其它
            count = constFundAgencyDao.getConstFundAgencyCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          default:
            break;
        }
      }
    } catch (Exception e) {
      logger.error("获取所有基准基金类别左菜单出错", e);
    }
    return newList;
  }

  @Override
  public List<FundLeftMenu> getConstFundCategoryLeftMenu() {
    List<FundLeftMenu> newList = new ArrayList<FundLeftMenu>();
    try {
      List<ConstFundPosition> List = constFundPositionDao.getFundLeftMenu();
      for (ConstFundPosition pos : List) {
        FundLeftMenu menu = null;
        List<FundLeftMenu> nextList = null;
        int count = 0;
        switch (pos.getId().intValue()) {
          case 110:// 国际
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 120:// 国家级
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            menu = new FundLeftMenu();
            menu.setId(pos.getId());
            menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
            List<ConstFundAgency> agencyList = constFundAgencyDao.findConstFundAgency(pos.getId());
            nextList = new ArrayList<FundLeftMenu>();
            for (ConstFundAgency ag : agencyList) {
              FundLeftMenu regLeft = new FundLeftMenu();
              regLeft.setId(ag.getId());
              regLeft.setName(this.getLocaleName(ag.getNameZh(), ag.getNameEn()));
              nextList.add(regLeft);
            }
            menu.setList(nextList);
            if (CollectionUtils.isNotEmpty(nextList))
              newList.add(menu);
            break;
          case 130:// 省级
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              List<ConstRegion> prvList = constFundAgencyDao.findConstFundRegionByCategory(pos.getId());
              nextList = new ArrayList<FundLeftMenu>();
              for (ConstRegion reg : prvList) {
                FundLeftMenu prvLeft = new FundLeftMenu();
                prvLeft.setId(reg.getId());
                prvLeft.setName(this.getLocaleName(reg.getZhName(), reg.getEnName()));
                nextList.add(prvLeft);
              }
              menu.setList(nextList);
              newList.add(menu);
            }
            break;
          case 140:// 地市级
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              List<ConstRegion> prvList = constFundAgencyDao.findConstFundRegionByCategory(pos.getId());
              nextList = new ArrayList<FundLeftMenu>();
              for (ConstRegion reg : prvList) {
                FundLeftMenu prvLeft = new FundLeftMenu();
                prvLeft.setId(reg.getId());
                prvLeft.setName(this.getLocaleName(reg.getZhName(), reg.getEnName()));
                nextList.add(prvLeft);
              }
              menu.setList(nextList);
              newList.add(menu);
            }
            break;
          case 141:// 区县级
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 150:// 企业类
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 160:// 其它
            count = constFundAgencyDao.getConstFundAgencyByCategoryCount(pos.getId());
            if (count > 0) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              newList.add(menu);
            }
            break;
          case 170:// 截止日期
            List<ConstFundPosition> posList = constFundPositionDao.findFundPositionByParantId(pos.getId());
            nextList = new ArrayList<FundLeftMenu>();
            Date currDate = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.format(currDate);
            currDate = df.parse(df.format(currDate));
            for (ConstFundPosition position : posList) {
              Date nextDate = DateUtils.afterOneMonth(position.getSeqNo());
              count = constFundCategoryDao.getConstFundEndDateCount(currDate, nextDate);
              if (count > 0) {
                FundLeftMenu nextLeft = new FundLeftMenu();
                nextLeft.setId(NumberUtils.toLong(ObjectUtils.toString(position.getSeqNo())));
                nextLeft.setName(this.getLocaleName(position.getNameZh(), position.getNameEn()));
                nextList.add(nextLeft);
              }
            }
            if (CollectionUtils.isNotEmpty(nextList)) {
              menu = new FundLeftMenu();
              menu.setId(pos.getId());
              menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
              menu.setList(nextList);
              newList.add(menu);
            }
            break;
          // bpo-21 去掉“语言类别”
          /*
           * case 180:// 语言类别 menu = new FundLeftMenu(); menu.setId(pos.getId());
           * menu.setName(this.getLocaleName(pos.getNameZh(), pos.getNameEn())); List<ConstFundPosition>
           * langList = constFundPositionDao.findFundPositionByParantId(pos.getId()); nextList = new
           * ArrayList<FundLeftMenu>(); for (ConstFundPosition position : langList) { count =
           * constFundCategoryDao.getConstFundLanguageCount(position.getId()); if (count > 0) { FundLeftMenu
           * nextLeft = new FundLeftMenu(); nextLeft.setId(position.getId());
           * nextLeft.setName(this.getLocaleName(position.getNameZh(), position.getNameEn()));
           * nextList.add(nextLeft); } } menu.setList(nextList); if (CollectionUtils.isNotEmpty(nextList))
           * newList.add(menu); break;
           */
          default:
            break;
        }
      }
    } catch (Exception e) {
      logger.error("获取所有基准基金类别左菜单出错", e);
    }
    return newList;
  }

  @Override
  public Page<ConstFundAgency> findFundAgency(Page<ConstFundAgency> page, FundForm form) {
    try {
      page = constFundAgencyDao.findFundAgency(page, form);
      for (ConstFundAgency agency : page.getResult()) {
        agency.setNameView(this.getLocaleName(agency.getNameZh(), agency.getNameEn()));
        if (agency.getType() != null) {
          ConstFundPosition pos = constFundPositionDao.get(agency.getType());
          agency.setTypeView(this.getLocaleName(pos.getNameZh(), pos.getNameEn()));
        }
        Long regionId = agency.getRegionId();
        if (regionId != null) {
          ConstRegion region = constFundAgencyDao.getRegion(regionId);
          ConstRegion regionSuper = constFundAgencyDao.getRegionBySuper(regionId);
          if (regionSuper != null) {
            agency.setRegionName(this.getLocaleName(regionSuper.getZhName(), regionSuper.getEnName()) + "-"
                + this.getLocaleName(region.getZhName(), region.getEnName()));
          } else {
            agency.setRegionName(this.getLocaleName(region.getZhName(), region.getEnName()));
          }
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return page;
  }

  @Override
  public Page<ConstFundAgency> findFundAgencyAudit(Page<ConstFundAgency> page, FundForm form) {
    try {
      page = constFundAgencyDao.findFundAgencyAudit(page, form);
      for (ConstFundAgency agency : page.getResult()) {
        agency.setNameView(this.getLocaleName(agency.getNameZh(), agency.getNameEn()));
      }
    } catch (Exception e) {
      logger.error("获取全部待审核基金机构出错", e);
    }
    return page;
  }

  @Override
  public Page<ConstFundCategory> findFundCategory(Page<ConstFundCategory> page, FundForm form) {
    try {
      page = constFundCategoryDao.findFundCategory(page, form);
      for (ConstFundCategory cat : page.getResult()) {
        ConstFundAgency agency = constFundAgencyDao.getFundAgency(cat.getAgencyId());
        cat.setAgencyViewName(this.getLocaleName(agency.getNameZh(), agency.getNameEn()));
        cat.setCategoryViewName(this.getLocaleName(cat.getNameZh(), cat.getNameEn()));
        cat.setLogoUrl(agency.getLogoUrl());
        cat.setAgencyUrl(agency.getUrl());
      }
    } catch (Exception e) {
      logger.error("获取全部基金机构及类别出错", e);
    }
    return page;
  }

  @Override
  public Page<ConstFundCategory> findFundCategoryAudit(Page<ConstFundCategory> page, FundForm form) {
    try {
      page = constFundCategoryDao.findFundCategoryAudit(page, form);
      for (ConstFundCategory cat : page.getResult()) {
        if (cat.getAgencyId() != null) {
          ConstFundAgency agency = constFundAgencyDao.getFundAgency(cat.getAgencyId());
          cat.setAgencyViewName(this.getLocaleName(agency.getNameZh(), agency.getNameEn()));
        }
        cat.setCategoryViewName(this.getLocaleName(cat.getNameZh(), cat.getNameEn()));
      }
    } catch (Exception e) {
      logger.error("获取全部待审核基金机构及类别出错", e);
    }
    return page;
  }

  @Override
  public void saveConstFundAgency(ConstFundAgency fundAgency) {
    try {
      Long auditAgencyId = fundAgency.getAuditAgencyId();
      Date createDate = new Date();
      fundAgency.setCreateDate(createDate);
      fundAgency.setUseDate(createDate);
      fundAgency.setParentAgencyId(null);
      constFundAgencyDao.save(fundAgency);
      // 回写
      if (auditAgencyId != null && auditAgencyId > 0L) {
        ConstFundAgency obj = constFundAgencyDao.get(auditAgencyId);
        if (obj != null && obj.getInsId() > 0L) {
          obj.setParentAgencyId(fundAgency.getId());
          constFundAgencyDao.save(obj);
        } else {
          obj.setParentAgencyId(null);
          constFundAgencyDao.save(obj);
        }
      }
    } catch (Exception e) {
      logger.error("新增单位基金机构出错", e);
    }
  }

  @Override
  public void updateConstFundAgency(ConstFundAgency fundAgency) {
    try {
      ConstFundAgency constFundAgency = constFundAgencyDao.get(fundAgency.getId());
      fundAgency.setUseDate(new Date());
      fundAgency.setCreateDate(constFundAgency.getCreateDate());
      BeanUtils.copyProperties(fundAgency, constFundAgency);// 左边复制到右边
      constFundAgencyDao.saveOrUpdate(fundAgency);
    } catch (Exception e) {
      logger.error("更新单位基金机构出错", e);
    }
  }

  @Override
  public void auditInsFundAgency(Long id, int status) {
    constFundAgencyDao.auditInsFundAgency(id, status);
  }

  @Override
  public void auditInsFundCategory(Long id, int status) {
    constFundCategoryDao.auditInsFundCategory(id, status);
  }

  @Override
  public ConstFundAgency getConstFundAgency(Long id) {
    ConstFundAgency constFundAgency = constFundAgencyDao.get(id);
    if (constFundAgency != null) {
      ConstRegion region = null;
      if (constFundAgency.getRegionId() != null) {
        region = constFundAgencyDao.getRegion(constFundAgency.getRegionId());
      }
      if (constFundAgency.getType() != null && region != null) {
        ConstFundPosition pos = constFundPositionDao.get(constFundAgency.getType());
        String typeView = this.getLocaleName(pos.getNameZh(), pos.getNameEn());
        // 省级
        if (constFundAgency.getType().intValue() == 130) {
          typeView = typeView + " - " + this.getLocaleName(region.getZhName(), region.getEnName());
        }
        // 地市级
        if (constFundAgency.getType().intValue() == 140) {
          ConstRegion regionSuper = constFundAgencyDao.getRegionBySuper(constFundAgency.getRegionId());
          if (regionSuper != null) {
            typeView = typeView + " - " + this.getLocaleName(regionSuper.getZhName(), regionSuper.getEnName()) + " - "
                + this.getLocaleName(region.getZhName(), region.getEnName());
            constFundAgency.setSuperRegionId(regionSuper.getId());
          }
        }
        constFundAgency.setTypeView(typeView);
      }
      if (constFundAgency.getAddrCoun() != null && !"".equals(constFundAgency.getAddrCoun())) {
        ConstRegion addrCounRegion = constRegionDao.findRegionNameById(constFundAgency.getAddrCoun());
        constFundAgency.setAddrCounName(addrCounRegion.getZhName());
      }
      if (constFundAgency.getAddrPrv() != null && !"".equals(constFundAgency.getAddrPrv())) {
        ConstRegion addrPrvRegion = constRegionDao.findRegionNameById(constFundAgency.getAddrPrv());
        constFundAgency.setAddrPrvName(addrPrvRegion.getZhName());
      }
      if (constFundAgency.getAddrCity() != null && !"".equals(constFundAgency.getAddrCity())) {// 机构地址-市
        ConstRegion addrCityRegion = constRegionDao.findRegionNameById(constFundAgency.getAddrCity());
        constFundAgency.setAddrCityName(addrCityRegion.getZhName());
      }
    }
    return constFundAgency;
  }

  @Override
  public void deleteConstFundAgency(Long id) {
    try {
      List<Long> categoryIds = constFundCategoryDao.findInsFundAgencyByCatIds(0L, id);
      if (CollectionUtils.isNotEmpty(categoryIds)) {
        for (Long constCategoryId : categoryIds) {
          constFundCategoryDisDao.deleteFundDisByCategoryId(constCategoryId);
          constFundCategoryKeywordsDao.deleteFundKeywordByCategoryId(constCategoryId);
          constFundCategoryFileDao.deleteFundFileByCategoryId(constCategoryId);
          constFundCategoryRegionDao.deleteFundRegionByCategoryId(constCategoryId);
          constFundCategoryDao.deleteConstFundCategory(constCategoryId);
        }
      }
      constFundAgencyDao.remvoeInsFundAgency(0L, id);
    } catch (Exception e) {
      logger.error("删除单位基金机构出错", e);
    }
  }

  @Override
  public ConstFundCategory saveConstFundCategory(ConstFundCategory constFundCategory) {
    Long insCategoryId = constFundCategory.getInsCategoryId();
    constFundCategory.setParentCategoryId(null);
    constFundCategory.setUpdateDate(new Date());
    constFundCategoryDao.save(constFundCategory);
    // 删除原数据
    constFundCategoryDisDao.deleteFundDisByCategoryId(constFundCategory.getId());
    constFundCategoryRegionDao.deleteFundRegionByCategoryId(constFundCategory.getId());
    constFundCategoryFileDao.deleteFundFileByCategoryId(constFundCategory.getId());
    constFundCategoryKeywordsDao.deleteFundKeywordByCategoryId(constFundCategory.getId());
    // 保存新数据
    this.saveFundCategoryDis(constFundCategory);
    this.saveFundCategoryRegion(constFundCategory);
    this.saveFundCategoryFile(constFundCategory);
    this.saveFundCategoryKeyword(constFundCategory);
    if (insCategoryId != null && insCategoryId > 0L) {
      // 回写parent_category_id
      ConstFundCategory cat = constFundCategoryDao.get(insCategoryId);
      if (cat != null && cat.getInsId() > 0L) {
        cat.setParentCategoryId(constFundCategory.getId());
        constFundCategoryDao.save(cat);
      } else {
        cat.setParentCategoryId(null);
        constFundCategoryDao.save(cat);
      }
    }
    return constFundCategory;

  }

  /**
   * 获取解析职称或学位要求列表.
   * 
   * @param requireStr
   * @return
   */
  private List<Long> getSplitRequireIdList(String requireStr) {
    List<Long> requireIdList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(requireStr)) {
      String[] requireArr = requireStr.split(",");
      for (int i = 0; i < requireArr.length; i++) {
        requireIdList.add(Long.valueOf(requireArr[i]));
      }
    }
    return requireIdList;
  }

  @Override
  public ConstFundCategory updateConstFundCategory(ConstFundCategory constFundCategory) {
    Long parentCategoryId = constFundCategory.getId();
    constFundCategory.setParentCategoryId(null);
    ConstFundCategory cat = constFundCategoryDao.get(parentCategoryId);
    constFundCategory.setId(parentCategoryId);
    constFundCategory.setCreateDate(cat.getCreateDate());
    // //复制对象
    try {
      BeanUtils.copyProperties(constFundCategory, cat);// 左边复制到右边
      cat.setUpdateDate(new Date());
    } catch (Exception e) {
      e.printStackTrace();
    }
    constFundCategoryDao.save(cat);
    // 删除原数据
    constFundCategoryDisDao.deleteFundDisByCategoryId(cat.getId());
    constFundCategoryRegionDao.deleteFundRegionByCategoryId(cat.getId());
    constFundCategoryFileDao.deleteFundFileByCategoryId(cat.getId());
    constFundCategoryKeywordsDao.deleteFundKeywordByCategoryId(cat.getId());
    // 保存新数据
    this.saveFundCategoryDis(cat);
    this.saveFundCategoryRegion(cat);
    this.saveFundCategoryFile(cat);
    this.saveFundCategoryKeyword(cat);
    return cat;

  }

  private void saveFundCategoryDis(ConstFundCategory constFundCategory) {
    String disList = constFundCategory.getDisList();
    if (StringUtils.isNotBlank(disList)) {
      String[] arr = disList.split(",");
      for (String str : arr) {
        if (StringUtils.isBlank(str))
          continue;
        ConstFundCategoryDis fundDis = new ConstFundCategoryDis();
        fundDis.setCategoryId(constFundCategory.getId());
        fundDis.setDisId(NumberUtils.toLong(str));
        fundDis.setSuperDisId(categoryMapBaseDao.getDisSuperId(str));
        constFundCategoryDisDao.save(fundDis);
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void saveFundCategoryRegion(ConstFundCategory constFundCategory) {
    String regionList = constFundCategory.getRegionList();
    if (StringUtils.isNotBlank(regionList)) {
      List jsonArr = JacksonUtils.jsonToList(regionList);
      for (Object object : jsonArr) {
        Map map = (Map) object;
        Long regId = NumberUtils.toLong(ObjectUtils.toString(map.get("val")));
        String viewName = ObjectUtils.toString(map.get("text"));
        ConstFundCategoryRegion fundRegion = new ConstFundCategoryRegion();
        fundRegion.setCategoryId(constFundCategory.getId());
        fundRegion.setRegId(regId);
        fundRegion.setViewName(viewName);
        constFundCategoryRegionDao.save(fundRegion);
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private void saveFundCategoryFile(ConstFundCategory constFundCategory) {
    String fileList = constFundCategory.getFileList();
    if (StringUtils.isNotBlank(fileList)) {
      List jsonArr = JacksonUtils.jsonToList(fileList);
      for (Object object : jsonArr) {
        Map map = (Map) object;
        String filePath = ObjectUtils.toString(map.get("filePath"));
        String fileName = ObjectUtils.toString(map.get("fileName"));
        ConstFundCategoryFile entity =
            constFundCategoryFileDao.getConstFundCategoryFile(constFundCategory.getId(), filePath);
        if (entity == null) {
          entity = new ConstFundCategoryFile();
          entity.setCategoryId(constFundCategory.getId());
          entity.setFilePath(filePath);
          entity.setFileName(fileName);
          constFundCategoryFileDao.save(entity);
        }
      }
    }
  }

  private void saveFundCategoryKeyword(ConstFundCategory constFundCategory) {
    String keywords = constFundCategory.getKeywordList();
    if (StringUtils.isNotBlank(keywords)) {
      String[] arr = keywords.split(",");
      for (String str : arr) {
        if (StringUtils.isBlank(str))
          continue;
        ConstFundCategorykeywords entity = new ConstFundCategorykeywords();
        entity.setCategoryId(constFundCategory.getId());
        entity.setKeyword(str);
        entity.setKeywordText(str.toLowerCase());
        entity.setKeywordHash(NumberUtils.toLong(ObjectUtils.toString(str.toLowerCase().hashCode())));
        constFundCategoryKeywordsDao.save(entity);
      }
    }
  }

  @Override
  public ConstFundCategory getConstFundCategory(Long categoryId) {
    ConstFundCategory constFundCategory = null;
    try {
      constFundCategory = constFundCategoryDao.get(categoryId);
      if (constFundCategory != null) {
        String description = constFundCategory.getDescription();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(description)) {
          constFundCategory.setDescriptionHtml(description.replaceAll("&lt;br/&gt;", "\n"));
        }
        if (constFundCategory.getAgencyId() != null) {
          ConstFundAgency agency = constFundAgencyDao.getFundAgency(constFundCategory.getAgencyId());
          constFundCategory.setAgencyViewName(this.getLocaleName(agency.getNameZh(), agency.getNameEn()));
          constFundCategory.setLogoUrl(agency.getLogoUrl());
        }
        // 修正增加资助类别的显示名_MJG_SCM-4254.
        constFundCategory
            .setCategoryViewName(this.getLocaleName(constFundCategory.getNameZh(), constFundCategory.getNameEn()));
        this.setFundDisList(categoryId, constFundCategory);
        this.setFundCategoryList(categoryId, constFundCategory);
        this.setFundRegionList(categoryId, constFundCategory);
        this.setFundKeywordList(categoryId, constFundCategory);
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return constFundCategory;
  }

  @Override
  public void deleteConstFundCategory(Long id) {
    Long constCategoryId = constFundCategoryDao.getInsFundCategory(0L, id);
    if (constCategoryId != null) {
      constFundCategoryDisDao.deleteFundDisByCategoryId(constCategoryId);
      constFundCategoryKeywordsDao.deleteFundKeywordByCategoryId(constCategoryId);
      constFundCategoryFileDao.deleteFundFileByCategoryId(constCategoryId);
      constFundCategoryRegionDao.deleteFundRegionByCategoryId(constCategoryId);
      constFundCategoryDao.deleteConstFundCategory(constCategoryId);
    }

  }

  @Override
  public Long checkInsFundCategoryByAgency(Long id) {
    try {
      ConstFundAgency fundAgency = constFundAgencyDao.get(id);
      if (fundAgency != null) {
        return constFundAgencyDao.getConstFundAgencyByName(fundAgency);
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  @Override
  public boolean getConstFundAgency(ConstFundAgency cfa) {
    List<Long> ids = constFundAgencyDao.getConstFundAgency(cfa);
    if (CollectionUtils.isEmpty(ids))
      return false;
    if (cfa.getId() == null && ids.size() > 0)
      return true;
    boolean result = true;
    for (Long id : ids) {
      if (id.equals(cfa.getId())) {
        result = false;
        break;
      }
    }
    return result;
  }

  @Override
  public boolean getConstFundCategory(ConstFundCategory cfc) {
    List<Long> ids = constFundCategoryDao.getConstFundCategory(cfc, "bpo");
    if (CollectionUtils.isEmpty(ids))
      return false;
    if (cfc.getId() == null && ids.size() > 0)
      return true;
    boolean result = true;
    for (Long id : ids) {
      if (id.equals(cfc.getId())) {
        result = false;
        break;
      }
    }
    return result;
  }

  @Override
  public List<Map<String, Object>> getFundAgencyTypeList() {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<ConstFundPosition> list = constFundPositionDao.findFundPositionByParantId(ConstFundPosition.AGENCY_TYPE);
      for (ConstFundPosition cfp : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", cfp.getId());
        map.put("name", this.getLocaleName(cfp.getNameZh(), cfp.getNameEn()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取基金机构类别出错", e);
    }
    return mapList;
  }

  @Override
  public List<Map<String, Object>> getConstRegionList(Long regionId) {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      regionId = regionId == null ? 156L : regionId;// 默认：156代表中国
      List<ConstRegion> list = constRegionDao.findRegionData(regionId);
      for (ConstRegion reg : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", reg.getId());
        map.put("name", this.getLocaleName(reg.getZhName(), reg.getEnName()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取基金机构所在地区出错", e);
    }
    return mapList;
  }

  @Override
  public List<Map<String, Object>> getAllCountryAndRegion() {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<ConstRegion> list = constRegionDao.getAllConstRegion(LocaleContextHolder.getLocale());
      for (ConstRegion reg : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", reg.getId());
        map.put("name", this.getLocaleName(reg.getZhName(), reg.getEnName()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取所有国家和地区出错", e);
    }
    return mapList;
  }

  @Override
  public List<Map<String, Object>> getFundAgencyAll() {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<ConstFundAgency> list = constFundAgencyDao.getFundAgencyAll();
      for (ConstFundAgency agency : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", agency.getId());
        map.put("name", this.getLocaleName(agency.getNameZh(), agency.getNameEn()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取全部基金机构出错", e);
    }
    return mapList;
  }

  @Override
  public List<Map<String, Object>> getLanguageList() {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<ConstFundPosition> list = constFundPositionDao.findFundPositionByParantId(ConstFundPosition.LANGUAGE);
      for (ConstFundPosition cfp : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", cfp.getId());
        map.put("name", this.getLocaleName(cfp.getNameZh(), cfp.getNameEn()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取语言类别出错", e);
    }
    return mapList;
  }

  @Override
  public List<Map<String, Object>> getDegreeList() {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<ConstFundPosition> list = constFundPositionDao.findFundPositionByParantId(ConstFundPosition.DEGREE);
      for (ConstFundPosition cfp : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", cfp.getId());
        map.put("name", this.getLocaleName(cfp.getNameZh(), cfp.getNameEn()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取学位出错", e);
    }
    return mapList;
  }

  @Override
  public List<Map<String, Object>> getTitleList() {
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    try {
      List<ConstFundPosition> list = constFundPositionDao.findFundPositionByParantId(ConstFundPosition.TITLE);
      for (ConstFundPosition cfp : list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", cfp.getId());
        map.put("name", this.getLocaleName(cfp.getNameZh(), cfp.getNameEn()));
        mapList.add(map);
      }
    } catch (Exception e) {
      logger.error("获取职称出错", e);
    }
    return mapList;
  }

  private void setFundRegionList(Long categoryId, ConstFundCategory constFundCategory) {
    List<ConstFundCategoryRegion> regionList = constFundCategoryRegionDao.findFundRegionByCategoryId(categoryId);
    if (CollectionUtils.isNotEmpty(regionList)) {
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      for (ConstFundCategoryRegion freg : regionList) {
        Map<String, Object> map = new HashMap<String, Object>();
        Long regionId = freg.getRegId();
        map.put("val", regionId);
        if (regionId == null) {
          map.put("text", freg.getViewName());
        } else {
          map.put("text", constRegionDao.getRegionNameById(regionId));
        }
        mapList.add(map);
      }
      constFundCategory.setRegionList(JacksonUtils.listToJsonStr(mapList));
    }
  }

  private void setFundCategoryList(Long categoryId, ConstFundCategory constFundCategory) {
    List<ConstFundCategoryFile> catFiles = constFundCategoryFileDao.findFundFileByCategoryId(categoryId);
    String fileFdesIdStr = "";
    if (CollectionUtils.isNotEmpty(catFiles)) {
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      for (ConstFundCategoryFile file : catFiles) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileName", file.getFileName());
        map.put("filePath", file.getFilePath());
        fileFdesIdStr += (file.getFilePath() != null && file.getFilePath().contains("fdesId=")
            && file.getFilePath().contains("&nodeId"))
                ? file.getFilePath().substring(file.getFilePath().indexOf("fdesId=") + 7,
                    file.getFilePath().indexOf("&nodeId"))
                : "";
        mapList.add(map);
      }
      constFundCategory.setFileList(JacksonUtils.listToJsonStr(mapList));
      constFundCategory.setFileFdesIdStr(fileFdesIdStr);
    }
  }

  private void setFundKeywordList(Long categoryId, ConstFundCategory constFundCategory) {
    List<ConstFundCategorykeywords> keywords = constFundCategoryKeywordsDao.findFundKeywordByCategoryId(categoryId);
    if (CollectionUtils.isNotEmpty(keywords)) {
      String keywordstr = "";
      for (ConstFundCategorykeywords key : keywords) {
        keywordstr += "," + key.getKeyword();
      }
      constFundCategory.setKeywordList(keywordstr.substring(1));
    }
  }

  private void setFundDisList(Long categoryId, ConstFundCategory constFundCategory) {
    List<ConstFundCategoryDis> disList = constFundCategoryDisDao.findFundDisByCategoryId(categoryId);
    if (CollectionUtils.isNotEmpty(disList)) {
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      for (ConstFundCategoryDis dis : disList) {
        CategoryMapBase constDis = categoryMapBaseDao.getCategoryDisByDisId(dis.getDisId());
        if (constDis != null) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("id", constDis.getCategryId());
          map.put("code", constDis.getCategryId());
          map.put("name", this.getLocaleName(constDis.getCategoryZh(), constDis.getCategoryEn()));
          mapList.add(map);
        }
      }
      constFundCategory.setDisJson(JacksonUtils.listToJsonStr(mapList));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public String getAcFundKeywords(String startWith, int size) {
    String newjson = "";
    try {
      List<ConstFundCategorykeywords> list = constFundCategoryKeywordsDao.getAcFundKeywords(startWith, size);
      List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
      if (CollectionUtils.isNotEmpty(list)) {
        for (ConstFundCategorykeywords cfk : list) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("code", cfk.getKeyword());
          map.put("name", cfk.getKeyword());
          mapList.add(map);
        }
      }
      mapList = HqlUtils.removeDuplicateWithOrder(mapList);
      newjson = ObjectUtils.toString(JacksonUtils.listToJsonStr(mapList));
    } catch (Exception e) {
      logger.error("自动提示基金关键词出错", e);
    }
    return newjson;
  }

  @Override
  public String[] compareConstFundAgency(ConstFundAgency agency, ConstFundAgency parentAgency) {
    try {
      // 名称 属性 下标
      // 1 机构名称(中文) nameZh 0
      // 2 机构名称(英文) nameEn 1
      // 3 机构类别 typeView 2
      // 4 机构代码 code 3
      // 5 机构缩写 abbr 4
      // 6 机构地址 address 5
      // 7 URL网址 url 6
      // 8 联系方式 contact 7
      // 9 资助机构Logo logoUrl 8
      // 10机构地址-省 addrPrvName 9
      // 11机构地址-市 addrCityName 10
      // 12机构地址-国家 addrCounName 11
      String[] compareResult =
          {"false", "false", "false", "false", "false", "false", "false", "false", "false", "false", "false", "false"};
      if (StringUtils.equals(agency.getNameZh(), parentAgency.getNameZh())) {
        compareResult[0] = "true";
      }
      if (StringUtils.equals(agency.getNameEn(), parentAgency.getNameEn())) {
        compareResult[1] = "true";
      }
      if (StringUtils.equals("" + agency.getType(), "" + parentAgency.getType())
          && StringUtils.equals("" + agency.getRegionId(), "" + agency.getRegionId())) {
        compareResult[2] = "true";
      }
      if (StringUtils.equals(agency.getCode(), parentAgency.getCode())) {
        compareResult[3] = "true";
      }
      if (StringUtils.equals(agency.getAbbr(), parentAgency.getAbbr())) {
        compareResult[4] = "true";
      }
      if (StringUtils.equals(agency.getAddress(), parentAgency.getAddress())) {
        compareResult[5] = "true";
      }
      if (StringUtils.equals(agency.getUrl(), parentAgency.getUrl())) {
        compareResult[6] = "true";
      }
      if (StringUtils.equals(agency.getContact(), parentAgency.getContact())) {
        compareResult[7] = "true";
      }
      if (StringUtils.equals(agency.getLogoUrl(), parentAgency.getLogoUrl())) {
        compareResult[8] = "true";
      }
      if (StringUtils.equals(agency.getAddrPrvName(), parentAgency.getAddrPrvName())) {
        compareResult[9] = "true";
      }
      if (StringUtils.equals(agency.getAddrCityName(), parentAgency.getAddrCityName())) {
        compareResult[10] = "true";
      }
      if (StringUtils.equals(agency.getAddrCounName(), parentAgency.getAddrCounName())) {
        compareResult[11] = "true";
      }
      return compareResult;

    } catch (Exception e) {
      logger.error("BPO:比较单位基金机构出错", e);
    }
    return null;
  }

  @Override
  public String[] compareConstFundCategory(ConstFundCategory category, ConstFundCategory parentCategory) {
    try {
      // 名称 属性 下标(-1)
      // 1 基金机构 agencyId 0
      // 2 类别名称(中文) nameZh 1
      // 3 类别名称(英文) nameEn 2
      // 4 类别代码 code 3
      // 5 类别缩写 abbr 4
      // 6 类别语言 language 5
      // 7 类别描述 description 6
      // 8 申报指南网址 guideUrl 7
      // 9 申报网址 declareUrl 8
      // 10 适合领域1 disJson 9
      // 11 适合领域2
      // 12 适合领域3
      // 13 适合领域4
      // 14 地区 regionList 10
      // 15 关键词 keywordList 11
      // 16 项目期限 deadline 12
      // 17 资助强度 strength 13
      // 18 开始日期 startDate 14
      // 19 截止日期 endDate 15
      // 20 职称要求1 titleRequire1 16
      // 21 学位要求1 degreeRequire1 17
      // 22 关系运算符 relationship 18
      // 23 职称要求2 titleRequire2 19
      // 24 学位要求2 degreeRequire2 20
      // 25 最佳职称 titleBest 21
      // 26 最佳学位 degreeBest 22
      // 27 出生年月要求 1 2 birthLeast-----birthMax 23 24
      // 28 最佳年龄 1 2 ageLeast-----ageMax 25 26
      // 29 附件 fileList 27
      // 30 联系方式 contact 28
      // 31是否匹配 isMatch 29
      // 32比例 percentage 30
      String[] compareResult = new String[31];
      // 初始化为false (不相同)
      for (int i = 0; i < compareResult.length; i++) {
        compareResult[i] = "false";
      }
      // 有相同的标记为true
      if (StringUtils.equals("" + category.getAgencyId(), "" + parentCategory.getAgencyId())) {
        compareResult[0] = "true";
      }
      if (StringUtils.equals("" + category.getNameZh(), "" + parentCategory.getNameZh())) {
        compareResult[1] = "true";
      }
      if (StringUtils.equals("" + category.getNameEn(), "" + parentCategory.getNameEn())) {
        compareResult[2] = "true";
      }
      if (StringUtils.equals("" + category.getCode(), "" + parentCategory.getCode())) {
        compareResult[3] = "true";
      }
      if (StringUtils.equals("" + category.getAbbr(), "" + parentCategory.getAbbr())) {
        compareResult[4] = "true";
      }
      if (StringUtils.equals("" + category.getLanguage(), "" + parentCategory.getLanguage())) {
        compareResult[5] = "true";
      }
      if (StringUtils.equals("" + category.getDescription(), "" + parentCategory.getDescription())) {
        compareResult[6] = "true";
      }
      if (StringUtils.equals("" + category.getGuideUrl(), "" + parentCategory.getGuideUrl())) {
        compareResult[7] = "true";
      }
      if (StringUtils.equals("" + category.getDeclareUrl(), "" + parentCategory.getDeclareUrl())) {
        compareResult[8] = "true";
      }
      if (StringUtils.equals("" + category.getDisJson(), "" + parentCategory.getDisJson())) {
        compareResult[9] = "true";
      }
      if (StringUtils.equals("" + category.getRegionList(), "" + parentCategory.getRegionList())) {
        compareResult[10] = "true";
      }
      if (StringUtils.equals("" + category.getKeywordList(), "" + parentCategory.getKeywordList())) {
        compareResult[11] = "true";
      }
      if (StringUtils.equals("" + category.getDeadline(), "" + parentCategory.getDeadline())) {
        compareResult[12] = "true";
      }
      if (StringUtils.equals("" + category.getStrength(), "" + parentCategory.getStrength())) {
        compareResult[13] = "true";
      }
      if (StringUtils.equals("" + category.getStartDate(), "" + parentCategory.getStartDate())) {
        compareResult[14] = "true";
      }
      if (StringUtils.equals("" + category.getEndDate(), "" + parentCategory.getEndDate())) {
        compareResult[15] = "true";
      }
      if (StringUtils.equals("" + category.getTitleRequire1(), "" + parentCategory.getTitleRequire1())) {
        compareResult[16] = "true";
      }
      if (StringUtils.equals("" + category.getDegreeRequire1(), "" + parentCategory.getDegreeRequire1())) {
        compareResult[17] = "true";
      }
      if (StringUtils.equals("" + category.getRelationship(), "" + parentCategory.getRelationship())) {
        compareResult[18] = "true";
      }
      if (StringUtils.equals("" + category.getTitleRequire2(), "" + parentCategory.getTitleRequire2())) {
        compareResult[19] = "true";
      }
      if (StringUtils.equals("" + category.getDegreeRequire2(), "" + parentCategory.getDegreeRequire2())) {
        compareResult[20] = "true";
      }
      if (StringUtils.equals("" + category.getTitleBest(), "" + parentCategory.getTitleBest())) {
        compareResult[21] = "true";
      }
      if (StringUtils.equals("" + category.getDegreeBest(), "" + parentCategory.getDegreeBest())) {
        compareResult[22] = "true";
      }
      if (StringUtils.equals("" + category.getBirthLeast(), "" + parentCategory.getBirthLeast())) {
        compareResult[23] = "true";
      }
      if (StringUtils.equals("" + category.getBirthMax(), "" + parentCategory.getBirthMax())) {
        compareResult[24] = "true";
      }
      if (StringUtils.equals("" + category.getAgeLeast(), "" + parentCategory.getAgeLeast())) {
        compareResult[25] = "true";
      }
      if (StringUtils.equals("" + category.getAgeMax(), "" + parentCategory.getAgeMax())) {
        compareResult[26] = "true";
      }
      if (StringUtils.equals("" + category.getFileFdesIdStr(), "" + parentCategory.getFileFdesIdStr())) {
        compareResult[27] = "true";
      }
      if (StringUtils.equals("" + category.getContact(), "" + parentCategory.getContact())) {
        compareResult[28] = "true";
      }
      if (StringUtils.equals("" + category.getIsMatch(), "" + parentCategory.getIsMatch())) {
        compareResult[29] = "true";
      }
      if (StringUtils.equals("" + category.getPercentage(), "" + parentCategory.getPercentage())) {
        compareResult[30] = "true";
      }
      return compareResult;

    } catch (Exception e) {
      logger.error("BPO:比较单位基金机构出错", e);
    }
    return null;
  }

  @Override
  public List<ConstFundCategory> getConstFundCategoryAll(Long id) {
    return constFundCategoryDao.getConstFundCategoryAll(id);
  }

  @Override
  public List<ConstFundAgency> getMatchAgencyByKey(String key) {

    return constFundAgencyDao.getAgencyIdLikeKey(key.toUpperCase());
  }

  @Override
  public ConstFundAgency getFundAgencyByName(String key) {
    return constFundAgencyDao.getFundAgencyByName(key);
  }
}
