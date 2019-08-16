package com.smate.web.management.dao.other.fund;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.ibm.icu.util.Calendar;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.FundForm;
import com.smate.web.management.model.other.fund.InsFundSearch;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryDao extends RcmdHibernateDao<ConstFundCategory, Long> {

  /**
   * @return
   */
  public Page<ConstFundCategory> findFundCategory(Page<ConstFundCategory> page, FundForm form) {
    String hql = "select t1 from ConstFundCategory t1,ConstFundAgency t3 where t1.agencyId=t3.id and t3.insId=?";
    List<Object> params = new ArrayList<Object>();
    params.add(0L);
    if (form.getLanguage() != null) {
      hql += " and t1.language=? ";
      params.add(NumberUtils.toLong(ObjectUtils.toString(form.getLanguage())));
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql += " and t3.type=? ";
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t3.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t3.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    if (form.getNewMonth() != null) {
      hql += " and t1.endDate>=? and t1.endDate<=?";
      Date currDate = new Date();
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      df.format(currDate);
      try {
        currDate = df.parse(df.format(currDate));
      } catch (ParseException e) {
      }
      params.add(currDate);
      params.add(DateUtils.afterOneMonth(form.getNewMonth()));
    }
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      if (hql.indexOf("=") != -1)
        hql += " and ";
      hql += " (instr(upper(t1.nameZh),?)>0 or instr(upper(t1.nameEn),?)>0) ";
      String searchKey = HtmlUtils.htmlEscape(form.getSearchKey());
      searchKey = searchKey.toUpperCase().trim();
      params.add(searchKey);
      params.add(searchKey);
    }
    hql += " order by t1.updateDate desc nulls last,nlssort(t1.nameZh,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.findPage(page, hql, params.toArray());
  }

  public Page<ConstFundCategory> findFundCategoryAudit(Page<ConstFundCategory> page, FundForm form) {
    String hql = "from ConstFundCategory t where t.status=? and t.insId>?";
    return super.findPage(page, hql, 0, 0L);
  }

  public Page<ConstFundCategory> getFundCategoryAllNotIns(Page<ConstFundCategory> page, Long insId) {
    String hql = "from ConstFundCategory t1 where not exists(select 1 from InsFund t2 where "
        + "t2.categoryId=t1.id and t2.insId=? and t2.categoryId is not null) and not exists(select 1 from ConstFundCategory t3 where  t3.insId=? and t3.parentCategoryId=t1.id ) "
        + "and t1.insId=? order by nvl(t1.endDate,sysdate-99999) desc";
    return super.findPage(page, hql, insId, insId, 0L);
  }

  public Integer getConstFundCategoryCountNotIns(Long insId, Long typeId) {
    String hql =
        "select count(t1.id) from ConstFundCategory t1,ConstFundAgency t3 where t1.insId=? and t1.agencyId=t3.id and not exists (select 1 from InsFund t2 where t1.id=t2.categoryId and t2.insId=? ) and t3.type=? "
            + "and not exists(select 1 from ConstFundCategory t2 where  t2.insId=? and t2.parentCategoryId=t1.id )";
    return ((Long) super.findUnique(hql, 0L, insId, typeId, insId)).intValue();
  }

  public List<ConstFundCategory> findConstFundCategoryNotIns(Long insId, Long typeId) {
    String hql =
        "select distinct new ConstFundCategory(t1.id,t1.nameZh,t1.nameEn,t1.code) from ConstFundCategory t1,ConstFundAgency t2 where t1.insId=0 and t1.agencyId=t2.id and not exists(select 1 from InsFund t3 where t3.categoryId=t1.id and t3.insId=?) and t2.type=?";
    return super.find(hql, insId, typeId);
  }

  public List<ConstRegion> findConstFundRegionNotIns(Long insId, Long typeId) {
    String hql =
        "select distinct new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstFundCategory t2,ConstFundAgency t4 where t2.insId=0 and t2.agencyId=t4.id and not exists(select 1 from InsFund t3 where t3.categoryId=t2.id and t3.insId=?) and t1.id=t4.regionId and t4.type=? order by nlssort(t1.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, new Object[] {insId, typeId});
  }

  public Integer getInsFundCategoryCount(Long insId, Long psnId, Long typeId) {
    String hql =
        "select count(distinct t1.id) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3 where t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=? and t3.type=?";
    return ((Long) super.findUnique(hql, insId, psnId, typeId)).intValue();
  }

  public List<ConstFundCategory> findInsFundCategory(Long insId, Long psnId, Long typeId) {
    String hql =
        "select distinct new ConstFundCategory(t1.id,t1.nameZh,t1.nameEn,t1.code) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3 where t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=? and t3.type=?";
    return super.find(hql, insId, psnId, typeId);
  }

  public List<ConstRegion> findInsFundRegion(Long insId, Long psnId, Long typeId) {
    String hql =
        "select distinct new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstFundCategory t2,InsFundSearch t3,ConstFundAgency t4 where t1.id=t4.regionId and t2.agencyId=t4.id and t2.id=t3.categoryId and t3.insId=? and t3.psnId=? and t4.type=? order by nlssort(t1.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, new Object[] {insId, psnId, typeId});
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> findFundCategory(List<Long> catIds) {
    String hql = "from ConstFundCategory where id in(:ids) order by nvl(endDate,sysdate-99999) desc";
    return super.createQuery(hql).setParameterList("ids", catIds).list();
  }

  /**
   * @param agencyId
   * @param category
   * @param flag
   * @return
   */
  public Page<ConstFundCategory> findFundCategory(Page<ConstFundCategory> page, Long agencyId, String category,
      boolean flag) {
    String hql = "from ConstFundCategory t where 1=1 ";
    List<Object> params = new ArrayList<Object>();
    if (agencyId != null && agencyId != -1) {
      hql += " and t.agencyId=? ";
      params.add(agencyId);
    }
    if (StringUtils.isNotBlank(category)) {
      hql += "and (t.nameZh like ? or upper(t.nameEn) like ?) ";
      params.add("%" + category.trim() + "%");
      params.add("%" + category.trim().toUpperCase() + "%");
    }
    // 不显示已过期的基金类别
    if (flag) {
      hql += "and nvl(t.endDate,sysdate)>=sysdate";
    }
    return findPage(page, hql, params.toArray());
  }

  /**
   * 转换Hash值列表.
   * 
   * @param keywordHashList
   * @return
   */
  private List<String> transferListToStr(List<String> keywordHashList) {
    List<String> result = new ArrayList<String>();
    for (int i = 0; i < keywordHashList.size(); i++) {
      String keywordHash = String.valueOf(keywordHashList.get(i));
      result.add(new BigDecimal(keywordHash).toPlainString());
    }
    return result;
  }

  /**
   * 根据基金关键词获取基金ID列表.
   * 
   * @param keywordHashList 关键词Hash值列表.
   * @return 基金ID列表.
   */
  @SuppressWarnings({"rawtypes"})
  public List getFundIdListByKeyHash(List<String> keywordHashList) {
    List fundIdList = new ArrayList();
    if (keywordHashList != null && keywordHashList.size() > 0) {
      keywordHashList = this.transferListToStr(keywordHashList);
      String hql =
          "select t.categoryId,count(*) from ConstFundCategorykeywords t where to_char(t.keywordHash) in (:hash) group by t.categoryId ";
      fundIdList = super.createQuery(hql).setParameterList("hash", keywordHashList).list();
    }
    return fundIdList;
  }

  /**
   * 根据基金ID获取对应的学科代码.
   * 
   * @param fundIdList 基金ID列表.
   * @return (学科代码ID，基金ID，学科代码ID对应的基金数).
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List getDiscIdByFunds(List<Long> fundIdList) {
    List discList = new ArrayList();
    if (fundIdList != null && fundIdList.size() > 0) {
      StringBuffer hql = new StringBuffer();
      hql.append(
          "select b.disId,b.categoryId,b.superDisId from ConstFundCategoryDis b where b.categoryId in (:fundId)");
      discList = super.createQuery(hql.toString()).setParameterList("fundId", fundIdList).list();
    }
    if (CollectionUtils.isNotEmpty(discList)) {
      List tempList = new ArrayList();
      // 重新封装基金列表.
      for (int i = 0; i < discList.size(); i++) {
        Object[] objArr = (Object[]) discList.get(i);
        Object[] resultArr = new Object[4];
        int count = 0;
        String disId = ObjectUtils.toString(objArr[0]);// 学科代码ID.
        // 统计学科代码对应的基金数.
        for (int j = 0; j < discList.size(); j++) {
          Object[] jObjArr = (Object[]) discList.get(j);
          String jDisId = ObjectUtils.toString(jObjArr[0]);
          if (Long.valueOf(disId).longValue() == Long.valueOf(jDisId).longValue()) {
            count++;
          }
        }
        resultArr[0] = disId;
        resultArr[1] = objArr[1];
        resultArr[2] = count;
        resultArr[3] = objArr[2];
        tempList.add(resultArr);
      }
      discList = tempList;
    }
    return discList;
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> queryFundCategory(int psnInsType, int maxSize) {
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select new ConstFundCategory(t1.id,t1.agencyId,t1.nameZh,t1.nameEn,t1.guideUrl,t1.declareUrl,t1.startDate,t1.endDate,t1.condition,t2.starNum) ");
    hql.append("from ConstFundCategory t1,ConstFundCategoryOrder t2");
    hql.append(" where t1.id=t2.categoryId and t2.psnInsType=?");
    hql.append(" order by t2.orderNum,t1.startDate,t1.endDate");
    return super.createQuery(hql.toString(), psnInsType).setMaxResults(maxSize).list();
  }

  public Long getFundCatCount(Long agencyId) {
    String hql = "select count(id) from ConstFundCategory where agencyId=?";
    return super.findUnique(hql, agencyId);
  }

  public Long getFundCateCountByAgency(Long insId, Long psnId, Long agencyId) {
    String hql =
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3 where t1.id=t2.categoryId and "
            + "t1.agencyId=t3.id and t2.insId=? and t2.psnId=? and t3.id=?";
    return super.findUnique(hql, insId, psnId, agencyId);
  }

  public Page<ConstFundCategory> findInsFund(Page<ConstFundCategory> page, FundForm form) {

    String hql = "select new ConstFundCategory(t1.id,t2.agencyId,t1.nameZh,t1.nameEn,t1.code,t1.abbr,t1.language,"
        + "t1.description,t1.guideUrl,t1.declareUrl,t1.startDate,t1.endDate,t1.titleRequire1,t1.degreeRequire1,"
        + "t1.titleRequire2,t1.degreeRequire2,t1.titleBest,t1.degreeBest,t1.birthLeast,t1.birthMax,"
        + "t1.ageLeast,t1.ageMax,t1.condition,t1.contact,t1.relationship,t1.deadline,t1.strength,t1.insId,"
        + "t1.status,t1.grantNameZh,t1.grantNameEn,t1.remark,t1.year,t1.psnId,t1.createDate,t2.editDate)  from ConstFundCategory t1,InsFundSearch t2 where t1.id=t2.categoryId and t2.insId=? and t2.psnId=? ";
    String orderHql = "";
    if (StringUtils.isNotBlank(form.getOrderType())) {
      if (form.getOrderType().equals("agencyOrder")) {
        if (form.getOrderTypeBy().equals("desc")) {
          orderHql += " order by t1.agencyId desc";
        } else if (form.getOrderTypeBy().equals("asc")) {
          orderHql += " order by t1.agencyId asc";
        }
      } else if (form.getOrderType().equals("categoryOrder")) {
        if (form.getOrderTypeBy().equals("desc")) {
          orderHql += " order by t1.id desc";
        } else if (form.getOrderTypeBy().equals("asc")) {
          orderHql += " order by t1.id asc";
        }
      } else if (form.getOrderType().equals("updateDateOrder")) {
        if (form.getOrderTypeBy().equals("desc")) {
          orderHql += " order by nvl(t2.editDate,to_date('1900/01/01','yyyy/MM/dd')) desc, t1.id desc ";
        } else if (form.getOrderTypeBy().equals("asc")) {
          orderHql += " order by nvl(t2.editDate,to_date('1900/01/01','yyyy/MM/dd')) asc, t1.id asc ";
        }
      }
    }
    return super.findPage(page, hql + orderHql, form.getInsId(), form.getPsnId());
  }

  public Integer getInsFundLanguageCount(Long language, Long insId, Long psnId) {
    String hql =
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2 where t1.id=t2.categoryId and t1.language=? and t2.insId=? and t2.psnId=?";
    return ((Long) super.findUnique(hql, language, insId, psnId)).intValue();
  }

  public List<Long> getInsFundYearList(Long insId, Long psnId) {
    String hql =
        "select distinct t1.year from ConstFundCategory t1,InsFundSearch t2 where t1.id=t2.categoryId and t1.year is not null and t2.insId=? and t2.psnId=?";
    return super.find(hql, insId, psnId);
  }

  public Integer getConstFundLanguageCountNotIns(Long language, Long insId) {
    String hql =
        "select count(t1.id) from ConstFundCategory t1 where t1.language=? and not exists(select 1 from InsFund t2 where t1.id=t2.categoryId and t2.insId=?)";
    return ((Long) super.findUnique(hql, language, insId)).intValue();
  }

  public Integer getConstFundLanguageCount(Long language) {
    String hql = "select count(t1.id) from ConstFundCategory t1 where t1.language=?";
    return ((Long) super.findUnique(hql, language)).intValue();
  }

  public Integer getInsFundEndDateCount(Date currDate, Date newDate, Long insId, Long psnId) throws DaoException {
    String hql =
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2 where t1.id=t2.categoryId and t1.endDate>=? and t1.endDate<=? and t2.insId=? and t2.psnId=?";
    return ((Long) super.findUnique(hql, currDate, newDate, insId, psnId)).intValue();
  }

  public Integer getConstFundEndDateCountNotIns(Date currDate, Date newDate, Long insId) throws DaoException {
    String hql =
        "select count(t1.id) from ConstFundCategory t1 where t1.insId=? and t1.endDate>=? and t1.endDate<=? and not exists(select 1 from InsFund t2 where t1.id=t2.categoryId and t2.insId=?) "
            + " and not exists(select 1 from ConstFundCategory t4 where  t4.insId=? and t4.parentCategoryId=t1.id )";
    return ((Long) super.findUnique(hql, 0L, currDate, newDate, insId, insId)).intValue();
  }

  public Integer getConstFundEndDateCount(Date currDate, Date newDate) throws DaoException {
    String hql = "select count(t1.id) from ConstFundCategory t1 where t1.endDate>=? and t1.endDate<=?";
    return ((Long) super.findUnique(hql, currDate, newDate)).intValue();
  }

  @SuppressWarnings("unchecked")
  public List<InsFundSearch> findInsFundAgencyBySearch(FundForm form) {
    String hql =
        "select distinct new InsFundSearch(t2.id,t2.useDate) from ConstFundAgency t2,InsFund t3 where t2.id=t3.agencyId and t3.insId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t2.nameZh like ? or upper(t2.nameEn) like ?) ";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    } else {
      /*
       * if (StringUtils.isNotBlank(form.getAgencyName())) { hql +=
       * " and (t2.nameZh like ? or upper(t2.nameEn) like ?) "; params.add("%" +
       * form.getAgencyName().trim() + "%"); params.add("%" + form.getAgencyName().trim().toUpperCase() +
       * "%"); } if (StringUtils.isNotBlank(form.getAgencyTypeId())) { hql += " and t2.type=? ";
       * params.add(NumberUtils.toLong(form.getAgencyTypeId())); } if
       * (StringUtils.isNotBlank(form.getAgencyRegionId())) { hql += " and t2.regionId=? ";
       * params.add(NumberUtils.toLong(form.getAgencyRegionId())); }
       */
      if (StringUtils.isNotBlank(form.getSearchCounId())) {// 高级检索 地区 省
        hql += " and t2.addrCoun = ? ";
        params.add(Long.parseLong(form.getSearchCounId()));
      }
      if (StringUtils.isNotBlank(form.getSearchProId())) {// 高级检索 地区 省
        hql += " and t2.addrPrv = ? ";
        params.add(Long.parseLong(form.getSearchProId()));
      }
      if (StringUtils.isNotBlank(form.getSearchCityId())) {// 高级检索 地区 市
        hql += " and t2.addrCity = ? ";
        params.add(Long.parseLong(form.getSearchCityId()));
      }
      if (StringUtils.isNotBlank(form.getSearchType())) {// 高级检索 机构类型
        hql += " and t2.type = ? ";
        params.add(Long.parseLong(form.getSearchType()));
      }
      if (StringUtils.isNotBlank(form.getSearchTypeRegionId())) {// 高级检索
        // 机构类型
        // 省市
        hql += " and t2.regionId = ? ";
        params.add(Long.parseLong(form.getSearchTypeRegionId()));
      }
      if (StringUtils.isNotBlank(form.getSearchAgency())) {// 高级检索 资助机构
        hql += " and (t2.nameZh like ? or upper(t2.nameEn) like ? ) ";
        params.add("%" + form.getSearchAgency().trim() + "%");
        params.add("%" + form.getSearchAgency().trim().toUpperCase() + "%");
      }

    }
    return super.createQuery(hql, params.toArray()).list();
  }

  @SuppressWarnings("unchecked")
  public List<InsFundSearch> findInsFundBySearch(Page<ConstFundCategory> page, FundForm form) {
    String hql =
        "select distinct new InsFundSearch(t1.id,t1.agencyId,t1.updateDate) from ConstFundCategory t1,ConstFundAgency t2,InsFund t3";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " where t1.id=t3.categoryId and t1.agencyId=t2.id and t3.insId=? ";
      hql += " and (t2.nameZh like ? or upper(t2.nameEn) like ? or t1.nameZh like ? or upper(t1.nameEn) like ?) ";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    } else {
      /*
       * if (StringUtils.isNotBlank(form.getAgencyName())) { hql +=
       * " and (t2.nameZh like ? or upper(t2.nameEn) like ?) "; params.add("%" +
       * form.getAgencyName().trim() + "%"); params.add("%" + form.getAgencyName().trim().toUpperCase() +
       * "%"); } if (StringUtils.isNotBlank(form.getAgencyTypeId())) { hql += " and t2.type=? ";
       * params.add(NumberUtils.toLong(form.getAgencyTypeId())); } if
       * (StringUtils.isNotBlank(form.getAgencyRegionId())) { hql += " and t2.regionId=? ";
       * params.add(NumberUtils.toLong(form.getAgencyRegionId())); } if
       * (StringUtils.isNotBlank(form.getCategoryName())) { hql +=
       * " and (t1.nameZh like ? or upper(t1.nameEn) like ?) "; params.add("%" +
       * form.getCategoryName().trim() + "%"); params.add("%" +
       * form.getCategoryName().trim().toUpperCase() + "%"); } if (form.getStartDate() != null) { hql +=
       * " and t1.startDate>=? "; params.add(form.getStartDate()); } if (form.getEndDate() != null) { hql
       * += " and t1.endDate<=? "; params.add(form.getEndDate()); }
       */
      if (StringUtils.isNotBlank(form.getRegionId())) {// 高级检索 地区id
        hql += ",ConstFundCategoryRegion t4 where t1.id=t3.categoryId and t1.agencyId=t2.id and t3.insId=? ";
        if (form.getRegionId().endsWith("0000")) {
          String cityidStr = form.getRegionId().substring(0, 2);
          hql += " and t1.id=t4.categoryId and (t4.regId = ? or to_char(t4.regId) like ?)";
          params.add(Long.parseLong(form.getRegionId()));
          params.add(cityidStr + "%00");
        } else {
          hql += " and t1.id=t4.categoryId and t4.regId = ? ";
          params.add(Long.parseLong(form.getRegionId()));
        }
      } else {
        hql += " where t1.id=t3.categoryId and t1.agencyId=t2.id and t3.insId=? ";
      }
      if (StringUtils.isNotBlank(form.getSearchAgency())) {// 高级检索 资助机构
        hql += " and (t2.nameZh like ? or upper(t2.nameEn) like ? ) ";
        params.add("%" + form.getSearchAgency().trim() + "%");
        params.add("%" + form.getSearchAgency().trim().toUpperCase() + "%");
      }
      if (StringUtils.isNotBlank(form.getSearchScheme())) {// 高级检索 机构类别
        hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ?) ";
        params.add("%" + form.getSearchScheme().trim() + "%");
        params.add("%" + form.getSearchScheme().trim().toUpperCase() + "%");
      }
      try {
        if (StringUtils.isNotBlank(form.getSearchStartDate())) {// 高级检索
          // 开始时间
          hql += " and t1.startDate >= to_date(?,'yyyy/MM/dd') ";
          params.add(form.getSearchStartDate());
        }
        if (StringUtils.isNotBlank(form.getSearchEndDate())) {// 高级检索
          // 结束时间
          hql += " and t1.endDate <= to_date(?,'yyyy/MM/dd') ";
          params.add(form.getSearchEndDate());
        }
      } catch (Exception e) {
      }
    }
    return super.createQuery(hql, params.toArray()).list();
  }

  public Page<ConstFundCategory> findInsFundByLeft(Page<ConstFundCategory> page, FundForm form) {
    String hql = "select t1 from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3";
    Date currDate = new Date();
    Calendar cal = Calendar.getInstance();
    String currDateStr = String.valueOf(cal.get(Calendar.YEAR)) + "/" + String.valueOf(cal.get(Calendar.MONTH) + 1)
        + "/" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    String orderHql = "";
    List<Object> params = new ArrayList<Object>();
    if (StringUtils.isNotBlank(form.getLeftMenuType()) && "region".equals(form.getLeftMenuType())
        && StringUtils.isNotBlank(form.getLeftMenuId()) || StringUtils.isNotBlank(form.getRegionId())) {
      hql +=
          ", ConstFundCategoryRegion t4 where t4.categoryId=t1.id and t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=?";
    } else {
      hql += " where t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=?";
    }
    params.add(form.getInsId());
    params.add(form.getPsnId());
    if (StringUtils.isNotBlank(form.getLeftMenuType())) {
      if ("region".equals(form.getLeftMenuType())) {// 左边栏 地区
        if (StringUtils.isNotBlank(form.getLeftMenuId())) {
          hql += " and t4.regId = ?";
          params.add(Long.parseLong(form.getLeftMenuId()));
        } else {// 其他
          hql += " and  not exists(select 1 from ConstFundCategoryRegion tt where tt.categoryId=t1.id) ";
        }
      } else if ("agency".equals(form.getLeftMenuType())) {// 左边栏菜单 资助机构
        hql += " and t3.id = ?";
        params.add(Long.parseLong(form.getLeftMenuId()));
      } else if ("status".equals(form.getLeftMenuType())) {// 左边栏菜单 状态
        if ("1".equals(form.getLeftMenuId())) {// 申请中
          hql += " and ( ";
          // 开始日期和结束日期都不为空，且开始日期<=当前日期<=结束日期
          hql +=
              "(t1.startDate is not null and t1.startDate <= ? and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') >= to_date(?,'yyyy/mm/dd'))";
          // 开始日期和结束日期都为空
          hql += " or (t1.startDate is null and t1.endDate is null)";
          // 开始日期为空，结束日期不为空，且结束日期>=当前日期
          hql +=
              " or (t1.startDate is null and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') >= to_date(?,'yyyy/mm/dd'))";
          // 开始日期不为空，结束日期为空，且开始日期<=当前日期
          hql += " or (t1.startDate is not null and t1.startDate<= ? and t1.endDate is null)";
          hql += ")";
          params.add(currDate);
          params.add(currDateStr);
          params.add(currDateStr);
          params.add(currDate);
        } else if ("0".equals(form.getLeftMenuId())) {// 已结束
          hql += " and ( ";
          // 开始日期为空，结束日期不为空，且结束日期<当前日期
          hql +=
              " (t1.startDate is null and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') < to_date(?,'yyyy/mm/dd')) ";
          // 开始日期和结束日期都不为空，且开始日期<当前日期，结束日期<当前日期
          hql +=
              " or (t1.startDate is not null and t1.startDate < ? and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') < to_date(?,'yyyy/mm/dd'))";
          hql += ")";
          params.add(currDateStr);
          params.add(currDate);
          params.add(currDateStr);
        } else if ("2".equals(form.getLeftMenuId())) {// 未开始
          // 开始日期不为空，且开始日期>当前日期
          hql += " and (t1.startDate is not null and t1.startDate > ?) ";
          params.add(currDate);
        }
      }
    }

    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ? or t3.nameZh like ? or upper(t3.nameEn) like ?) ";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    }
    if (form.getLanguage() != null) {
      hql += " and t1.language=? ";
      params.add(NumberUtils.toLong(ObjectUtils.toString(form.getLanguage())));
    }
    if (form.getYear() != null) {
      hql += " and t1.year=? ";
      params.add(form.getYear());
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      if ("190".equals(form.getAgencyTypeId())) {
        hql += " and t1.year is not null";
      } else {
        hql += " and t3.type=? ";
        params.add(NumberUtils.toLong(form.getAgencyTypeId()));
      }
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t3.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t3.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    if (form.getNewMonth() != null) {
      hql += " and t1.endDate>=? and t1.endDate<=?";
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      df.format(currDate);
      try {
        currDate = df.parse(df.format(currDate));
      } catch (ParseException e) {
      }
      params.add(currDate);
      params.add(DateUtils.afterOneMonth(form.getNewMonth()));
    }
    if (StringUtils.isNotBlank(form.getRegionId())) {// 高级检索 地区
      hql += " and t4.regId = ?";
      params.add(Long.parseLong(form.getLeftMenuId()));
    }

    if (StringUtils.isNotBlank(form.getOrderType())) {
      if (form.getOrderType().equals("agencyOrder")) {
        if (form.getOrderTypeBy().equals("desc")) {
          orderHql += " order by t3.id desc";
        } else if (form.getOrderTypeBy().equals("asc")) {
          orderHql += " order by t3.id asc";
        }
      } else if (form.getOrderType().equals("categoryOrder")) {
        if (form.getOrderTypeBy().equals("desc")) {
          orderHql += " order by t1.id desc";
        } else if (form.getOrderTypeBy().equals("asc")) {
          orderHql += " order by t1.id asc";
        }
      } else if (form.getOrderType().equals("updateDateOrder")) {
        if (form.getOrderTypeBy().equals("desc")) {
          orderHql += " order by nvl(t1.updateDate,to_date('1900/01/01','yyyy/MM/dd')) desc, t1.id desc ";
        } else if (form.getOrderTypeBy().equals("asc")) {
          orderHql += " order by nvl(t1.updateDate,to_date('1900/01/01','yyyy/MM/dd')) asc, t1.id asc ";
        }
      }
    }
    return super.findPage(page, hql + orderHql, params.toArray());
  }

  public Page<ConstFundCategory> findConstFundAllByLeft(Page<ConstFundCategory> page, FundForm form) {
    String hql = "select t1 from ConstFundCategory t1,ConstFundAgency t3 where t1.agencyId=t3.id and t1.insId=0 ";
    List<Object> params = new ArrayList<Object>();
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ? or t3.nameZh like ? or upper(t3.nameEn) like ?) ";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    }
    if (form.getLanguage() != null) {
      hql += " and t1.language=? ";
      params.add(NumberUtils.toLong(ObjectUtils.toString(form.getLanguage())));
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql += " and t3.type=? ";
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t3.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t3.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    if (form.getNewMonth() != null) {
      hql += " and t1.endDate>=? and t1.endDate<=?";
      Date currDate = new Date();
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      df.format(currDate);
      try {
        currDate = df.parse(df.format(currDate));
      } catch (ParseException e) {
      }
      params.add(currDate);
      params.add(DateUtils.afterOneMonth(form.getNewMonth()));
    }
    return super.findPage(page, hql, params.toArray());
  }

  public Page<ConstFundCategory> findConstFundAllByLeftNotIns(Page<ConstFundCategory> page, FundForm form) {
    String hql = "select t1 from ConstFundCategory t1,ConstFundAgency t3 where t1.agencyId=t3.id and "
        + " not exists (select 1 from InsFund t2 where t1.id=t2.categoryId and t2.insId=?) "
        + " and not exists(select 1 from ConstFundCategory t4 where  t4.insId=? and t4.parentCategoryId=t1.id ) and t1.insId=0 ";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    params.add(form.getInsId());
    if (form.getLanguage() != null) {
      hql += " and t1.language=? ";
      params.add(NumberUtils.toLong(ObjectUtils.toString(form.getLanguage())));
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql += " and t3.type=? ";
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t3.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t3.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    if (form.getNewMonth() != null) {
      hql += " and t1.endDate>=? and t1.endDate<=?";
      Date currDate = new Date();
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      df.format(currDate);
      try {
        currDate = df.parse(df.format(currDate));
      } catch (ParseException e) {
      }
      params.add(currDate);
      params.add(DateUtils.afterOneMonth(form.getNewMonth()));
    }
    return super.findPage(page, hql, params.toArray());
  }

  public List<ConstFundCategory> findConstFundCategoryByAgencyId(Long agencyId) {
    String hql = "from ConstFundCategory t where t.agencyId=?";
    return super.find(hql, agencyId);
  }

  public List<Long> getConstFundCategory(ConstFundCategory cfc, String type) {
    String hql = "select t.id from ConstFundCategory t where t.agencyId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(cfc.getAgencyId());
    if ("bpo".equals(type)) {
      hql += " and insId=0 ";
    }
    if ("sie".equals(type)) {// rol-1893 机构添加、修改资助类别时，只与当前机构和bpo查重
      hql += " and (insId=? or insId=0) ";
      params.add(SecurityUtils.getCurrentInsId());
    }
    if (StringUtils.isNotBlank(cfc.getNameZh()) && StringUtils.isBlank(cfc.getNameEn())) {
      hql += " and (upper(nameZh)=? or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfc.getNameZh()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfc.getNameZh()).toUpperCase());
    } else if (StringUtils.isNotBlank(cfc.getNameEn()) && StringUtils.isBlank(cfc.getNameZh())) {
      hql += " and (upper(nameZh)=? or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfc.getNameEn()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfc.getNameEn()).toUpperCase());
    } else if (StringUtils.isNotBlank(cfc.getNameZh()) && StringUtils.isNotBlank(cfc.getNameEn())) {
      hql += " and (upper(nameZh)=? or upper(nameEn)=? ) ";
      params.add(StringUtils.trimToEmpty(cfc.getNameZh()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfc.getNameEn()).toUpperCase());
    }
    return super.find(hql, params.toArray());
  }

  /**
   * 获取所有基金类别.
   * 
   * @return
   */
  public List<ConstFundCategory> getFundCategoryAll() {
    String hql = "from ConstFundCategory where insId=? ";
    return super.find(hql, 0L);
  }

  /**
   * 获取可推荐的基金类别.
   * 
   * @return
   */
  public List<ConstFundCategory> getFundCategoryRecommend() {
    String hql =
        "select t1 from ConstFundCategory t1,ConstFundAgency t2 where t1.agencyId=t2.id and t1.insId=? and t2.insId=0";
    return super.find(hql, 0L);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findInsFundAgencyByCatIds(Long insId, Long agencyId) {
    String hql = "select t.id from ConstFundCategory t where t.insId=? and t.agencyId=?";
    return super.createQuery(hql, insId, agencyId).list();
  }

  public Long getInsFundCategory(Long insId, Long categoryId) {
    String hql = "select id from ConstFundCategory where insId=? and id=?";
    return findUnique(hql, insId, categoryId);
  }

  public void deleteConstFundCategory(Long categoryId) {
    String hql = "delete from ConstFundCategory where id=?";
    super.createQuery(hql, categoryId).executeUpdate();
  }

  public void auditInsFundCategory(Long id, int status) {
    String hql = "update ConstFundCategory t set t.status=? where id=?";
    super.createQuery(hql, status, id).executeUpdate();
  }

  public Integer getStatusLeftMenuCount(Long insId, Long psnId, String statusType) {
    StringBuffer hql = new StringBuffer();
    Date nowDate = new Date();
    Calendar cal = Calendar.getInstance();
    String currDateStr = String.valueOf(cal.get(Calendar.YEAR)) + "/" + String.valueOf(cal.get(Calendar.MONTH) + 1)
        + "/" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    hql.append(
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3 where t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=? ");
    if ("active".equals(statusType)) {// 申请中
      hql.append(" and ( ");
      // 开始日期和结束日期都不为空，且开始日期<=当前日期<=结束日期
      hql.append(
          "(t1.startDate is not null and t1.startDate <= ? and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') >= to_date(?,'yyyy/mm/dd'))");
      // 开始日期和结束日期都为空
      hql.append(" or (t1.startDate is null and t1.endDate is null)");
      // 开始日期为空，结束日期不为空，且结束日期>=当前日期
      hql.append(
          " or (t1.startDate is null and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') >= to_date(?,'yyyy/mm/dd'))");
      // 开始日期不为空，结束日期为空，且开始日期<=当前日期
      hql.append(" or (t1.startDate is not null and t1.startDate<= ? and t1.endDate is null)");
      hql.append(")");
    } else if ("inactive".equals(statusType)) {// 已结束
      hql.append(" and ( ");
      // 开始日期为空，结束日期不为空，且结束日期<当前日期
      hql.append(
          " (t1.startDate is null and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') < to_date(?,'yyyy/mm/dd')) ");
      // 开始日期和结束日期都不为空，且开始日期<当前日期，结束日期<当前日期
      hql.append(
          " or (t1.startDate is not null and t1.startDate < ? and t1.endDate is not null and to_date(to_char(t1.endDate,'yyyy/mm/dd'),'yyyy/mm/dd') < to_date(?,'yyyy/mm/dd'))");
      hql.append(")");
    } else if ("notStart".equals(statusType)) {// 未开始
      // 开始日期不为空，且开始日期>当前日期
      hql.append(" and (t1.startDate is not null and t1.startDate > ?) ");
    }
    // hql.append("and not exists(select 1 from InsFund t2 where
    // t1.id=t2.categoryId and t2.insId=?)");
    if ("active".equals(statusType)) {
      return ((Long) super.findUnique(hql.toString(), insId, psnId, nowDate, currDateStr, currDateStr, nowDate))
          .intValue();
    } else if ("inactive".equals(statusType)) {
      return ((Long) super.findUnique(hql.toString(), insId, psnId, currDateStr, nowDate, currDateStr)).intValue();
    } else if ("notStart".equals(statusType)) {
      return ((Long) super.findUnique(hql.toString(), insId, psnId, nowDate)).intValue();
    } else {
      return 0;
    }
  }

  public int getInsFundAgencyMenuCount(Long insId, Long psnId, Long agencyId) {
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3 where t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=? and t3.id=? ");
    return ((Long) super.findUnique(hql.toString(), insId, psnId, agencyId)).intValue();
  }

  public int getCategoryRegionMenuCount(Long insId, Long psnId, Long regionId) {
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2,ConstFundCategoryRegion t3,ConstFundAgency t4 where t1.id=t2.categoryId and t1.agencyId=t4.id and t1.id=t3.categoryId and t2.insId=? and t2.psnId=? and t3.regId=? ");
    return ((Long) super.findUnique(hql.toString(), insId, psnId, regionId)).intValue();
  }

  public int getOtherRegionCount(Long insId, Long psnId) {
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select count(t1.id) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t4 where t1.id=t2.categoryId and t1.agencyId=t4.id and t2.insId=? and t2.psnId=? and not exists(select 1 from ConstFundCategoryRegion tt where tt.categoryId=t1.id)");
    return ((Long) super.findUnique(hql.toString(), insId, psnId)).intValue();
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> getConstFundCategoryAll(Long id) {
    String hql = "from ConstFundCategory t where t.agencyId = :id";
    return super.createQuery(hql).setParameter("id", id).list();
  }
}
