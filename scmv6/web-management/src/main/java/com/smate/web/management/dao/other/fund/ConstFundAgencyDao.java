package com.smate.web.management.dao.other.fund;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.other.fund.ConstFundAgency;
import com.smate.web.management.model.other.fund.FundForm;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundAgencyDao extends RcmdHibernateDao<ConstFundAgency, Long> {

  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> findConstFundAgencyByRegionId(Long regionId) {
    String hql = "from ConstFundAgency where regionId = ?";
    return super.createQuery(hql, regionId).list();
  }

  public Page<ConstFundAgency> findInsFundAgency(Page<ConstFundAgency> page, FundForm form) {
    String hql = "select distinct new ConstFundAgency(t1.id,t1.nameZh,t1.nameEn,t1.flag,t1.type,"
        + "t1.regionId,t1.code,t1.abbr,t1.address,t1.url,t1.contact,t1.logoUrl,t1.insId,t1.status,"
        + "t1.psnId,t1.createDate,t1.useDate) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    params.add(form.getPsnId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ?) ";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    } else {
      if (StringUtils.isNotBlank(form.getSearchCounId())) {// 高级检索 地区 省
        hql += " and t1.addrCoun = ? ";
        params.add(Long.parseLong(form.getSearchCounId()));
      }
      if (StringUtils.isNotBlank(form.getSearchProId())) {// 高级检索 地区 省
        hql += " and t1.addrPrv = ? ";
        params.add(Long.parseLong(form.getSearchProId()));
      }
      if (StringUtils.isNotBlank(form.getSearchCityId())) {// 高级检索 地区 市
        hql += " and t1.addrCity = ? ";
        params.add(Long.parseLong(form.getSearchCityId()));
      }
      if (StringUtils.isNotBlank(form.getSearchType())) {// 高级检索 机构类型
        hql += " and t1.type = ? ";
        params.add(Long.parseLong(form.getSearchType()));
      }
      if (StringUtils.isNotBlank(form.getSearchTypeRegionId())) {// 高级检索 机构类型 省市
        hql += " and t1.regionId = ? ";
        params.add(Long.parseLong(form.getSearchTypeRegionId()));
      }
      if (StringUtils.isNotBlank(form.getSearchAgency())) {// 高级检索 资助机构
        hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ? ) ";
        params.add("%" + form.getSearchAgency().trim() + "%");
        params.add("%" + form.getSearchAgency().trim().toUpperCase() + "%");
      }
    }
    if (form.getOrderType().equals("agencyOrder")) {
      Locale locale = LocaleContextHolder.getLocale();
      if (locale.equals(Locale.US)) {
        hql += " order by nlssort(nvl(t1.nameEn,t1.nameZh),'NLS_SORT=SCHINESE_PINYIN_M') " + form.getOrderTypeBy()
            + ", t1.id " + form.getOrderTypeBy();
      } else {
        hql += " order by nlssort(nvl(t1.nameZh,t1.nameEn),'NLS_SORT=SCHINESE_PINYIN_M') " + form.getOrderTypeBy()
            + ", t1.id " + form.getOrderTypeBy();
      }
    }
    return super.findPage(page, hql, params.toArray());
  }

  public int findInsFundAgencyCount(FundForm form) {
    String hql =
        "select count(distinct t1.id) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=?";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    params.add(form.getPsnId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ?) ";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    } else {
      if (StringUtils.isNotBlank(form.getSearchCounId())) {// 高级检索 地区 省
        hql += " and t1.addrCoun = ? ";
        params.add(Long.parseLong(form.getSearchCounId()));
      }
      if (StringUtils.isNotBlank(form.getSearchProId())) {// 高级检索 地区 省
        hql += " and t1.addrPrv = ? ";
        params.add(Long.parseLong(form.getSearchProId()));
      }
      if (StringUtils.isNotBlank(form.getSearchCityId())) {// 高级检索 地区 市
        hql += " and t1.addrCity = ? ";
        params.add(Long.parseLong(form.getSearchCityId()));
      }
      if (StringUtils.isNotBlank(form.getSearchType())) {// 高级检索 机构类型
        hql += " and t1.type = ? ";
        params.add(Long.parseLong(form.getSearchType()));
      }
      if (StringUtils.isNotBlank(form.getSearchTypeRegionId())) {// 高级检索 机构类型 省市
        hql += " and t1.regionId = ? ";
        params.add(Long.parseLong(form.getSearchTypeRegionId()));
      }
      if (StringUtils.isNotBlank(form.getSearchAgency())) {// 高级检索 资助机构
        hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ? ) ";
        params.add("%" + form.getSearchAgency().trim() + "%");
        params.add("%" + form.getSearchAgency().trim().toUpperCase() + "%");
      }
    }
    return ((Long) super.findUnique(hql, params.toArray())).intValue();
  }

  /**
   * 获取单位提交的基金机构给bpo审核
   * 
   * @param page
   * @return
   */
  public Page<ConstFundAgency> findFundAgencyAudit(Page<ConstFundAgency> page, FundForm form) {
    String hql = "from ConstFundAgency t where t.status=? and t.insId>?";
    return super.findPage(page, hql, 0, 0L);
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getAgencys(String startWith, int size, Long insId) {
    String hql =
        "select new ConstFundAgency(id,nameZh,nameEn,code) from ConstFundAgency where (insId=:insId or insId=:curInsId) and (instr(lower(nameZh),:nameZh)>0 or instr(lower(nameEn),:nameEn)>0)";
    return super.createQuery(hql).setParameter("insId", 0L).setParameter("curInsId", insId)
        .setParameter("nameZh", startWith).setParameter("nameEn", startWith).setMaxResults(size).list();
  }

  public List<ConstFundAgency> getFundAgencyAll() {
    String hql = "select new ConstFundAgency(id,nameZh,nameEn,code) from ConstFundAgency where insId=? order by type";
    return super.find(hql, 0L);
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundAgency> getInsFundAgencyAll(Long insId) {
    // String hql =
    // "select new ConstFundAgency(id,nameZh,nameEn,code) from ConstFundAgency where insId in(:insIds)
    // and status=:status order by useDate desc";
    String hql =
        "select t.id,t.name_zh,t.name_en,t.code from const_fund_agency t where t.ins_id in(:insIds) and status=0 order by t.use_date desc nulls last";
    List<Long> insIds = new ArrayList<Long>();
    insIds.add(0L);
    insIds.add(insId);
    SQLQuery query = super.getSession().createSQLQuery(hql);
    query.setParameterList("insIds", insIds);
    List<Object[]> listMap = query.list();
    List<ConstFundAgency> list = new ArrayList<ConstFundAgency>(listMap.size());
    for (Iterator<Object[]> iterator = listMap.iterator(); iterator.hasNext();) {
      Object[] obj = iterator.next();
      ConstFundAgency constFundAgency = new ConstFundAgency();
      constFundAgency.setId(Long.valueOf(obj[0].toString()));
      constFundAgency.setNameZh(obj[1] == null ? null : obj[1].toString());
      constFundAgency.setNameEn(obj[2] == null ? null : obj[2].toString());
      constFundAgency.setCode(obj[3] == null ? null : obj[3].toString());
      list.add(constFundAgency);
    }
    return list;
    // Query query = super.createQuery(hql);
    // query.setParameterList("insIds", insIds);
    // query.setParameter("status", 0);
    // return query.list();
  }

  public Long getConstFundAgencyByName(ConstFundAgency cfa) {
    String hql = "select id from ConstFundAgency where insId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(0L);
    if (StringUtils.isNotBlank(cfa.getNameZh())) {
      hql += " and nameZh=? ";
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()));
    }
    if (StringUtils.isNotBlank(cfa.getNameEn())) {
      hql += " and upper(nameEn)=? ";
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
    }
    return super.findUnique(hql, params.toArray());
  }

  public List<Long> getConstFundAgency(ConstFundAgency cfa) {
    String hql = "select id from ConstFundAgency where insId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(0L);
    if (cfa.getType() != null) {
      hql += " and type=? ";
      params.add(cfa.getType());
    }
    if (cfa.getRegionId() != null) {
      hql += " and regionId=? ";
      params.add(cfa.getRegionId());
    }
    if (StringUtils.isNotBlank(cfa.getNameZh())) {

      if (StringUtils.isNotBlank(cfa.getNameEn())) {
        hql += " and ( (nameZh=? or upper(nameZh)=? or upper(nameEn)=?) ";
        params.add(StringUtils.trimToEmpty(cfa.getNameZh()));
        params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
        params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
        hql += " or (nameZh=? or upper(nameZh)=? or upper(nameEn)=?)) ";
        params.add(StringUtils.trimToEmpty(cfa.getNameEn()));
        params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
        params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
      } else {
        hql += " and (nameZh=? or upper(nameZh)=? or upper(nameEn)=?) ";
        params.add(StringUtils.trimToEmpty(cfa.getNameZh()));
        params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
        params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
      }
    } else if (StringUtils.isNotBlank(cfa.getNameEn())) {
      hql += " and (nameZh=? or upper(nameZh)=?  or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()));
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
    }
    return super.find(hql, params.toArray());
  }

  public List<Long> getConstFundAgency2(ConstFundAgency cfa, Long insId) {
    String hql = "select id from ConstFundAgency where (insId=? or insId=?) ";
    List<Object> params = new ArrayList<Object>();
    params.add(0L);
    params.add(insId);
    if (cfa.getType() != null) {
      hql += " and type=? ";
      params.add(cfa.getType());
    }
    if (cfa.getRegionId() != null) {
      hql += " and regionId=? ";
      params.add(cfa.getRegionId());
    }
    if (StringUtils.isNotBlank(cfa.getNameZh()) && StringUtils.isBlank(cfa.getNameEn())) {// rol-1555
      // 机构名查重
      hql += " and (nameZh=? or upper(nameZh)=? or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()));
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
    } else if (StringUtils.isNotBlank(cfa.getNameZh()) && StringUtils.isNotBlank(cfa.getNameEn())) {
      hql += " and (nameZh=? or upper(nameZh)=? or upper(nameEn)=? ";
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()));
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
      hql += " or (nameZh=? or upper(nameZh)=? or upper(nameEn)=?) )  ";
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()));
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
    } else if (StringUtils.isBlank(cfa.getNameZh()) && StringUtils.isNotBlank(cfa.getNameEn())) {
      hql += " and (nameZh=? or upper(nameZh)=? or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()));
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
    }
    return super.find(hql, params.toArray());
  }

  public List<Long> getByConstFundAgency(ConstFundAgency cfa) {
    String hql = "select id from ConstFundAgency where insId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(cfa.getInsId());
    if (StringUtils.isNotBlank(cfa.getNameZh())) {
      hql += " and (nameZh=? or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()));
      params.add(StringUtils.trimToEmpty(cfa.getNameZh()).toUpperCase());
    }
    if (StringUtils.isNotBlank(cfa.getNameEn())) {
      hql += " and (nameZh=? or upper(nameEn)=?) ";
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()));
      params.add(StringUtils.trimToEmpty(cfa.getNameEn()).toUpperCase());
    }
    return super.find(hql, params.toArray());
  }

  public ConstFundAgency getFundAgency(Long agencyId) {
    String hql = "from ConstFundAgency where id=?";
    return super.findUnique(hql, agencyId);
  }

  public Integer getConstFundAgencyCount(Long typeId) {
    String hql = "select count(t1.id) from ConstFundAgency t1 where t1.type=? and t1.insId=?";
    return ((Long) super.findUnique(hql, typeId, 0L)).intValue();
  }

  public Integer getConstFundAgencyByCategoryCount(Long typeId) {
    String hql =
        "select count(t1.id) from ConstFundAgency t1,ConstFundCategory t2 where t1.id=t2.agencyId and t1.type=? and t1.insId=0";
    return ((Long) super.findUnique(hql, typeId)).intValue();
  }

  public Integer getInsFundAgencyCount(Long insId, Long psnId, Long typeId) {
    String hql =
        "select count(distinct t1.id) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.categoryId is not null and t2.insId=? and t2.psnId=? and t1.type=?";
    return ((Long) super.findUnique(hql, insId, psnId, typeId)).intValue();
  }

  public Integer getInsFundAgencyCount2(Long insId, Long psnId, Long typeId) {
    String hql =
        "select count(distinct t1.id) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=? and t1.type=?";
    return ((Long) super.findUnique(hql, insId, psnId, typeId)).intValue();
  }

  public Integer getConstFundAgencyCountNotIns(Long insId, Long typeId) {
    String hql =
        "select count(t1.id) from ConstFundAgency t1 where t1.insId=? and not exists (select 1 from InsFund t2 where t1.id=t2.agencyId and t2.insId=? ) and t1.type=?"
            + " and not exists(select 1 from ConstFundAgency t3 where t3.insId=? and t3.parentAgencyId=t1.id)";
    return ((Long) super.findUnique(hql, 0L, insId, typeId, insId)).intValue();
  }

  public List<ConstFundAgency> findConstFundAgency(Long typeId) {
    String hql =
        "select distinct new ConstFundAgency(t1.id,t1.nameZh,t1.nameEn,t1.code) from ConstFundAgency t1,ConstFundCategory t2 where t1.id=t2.agencyId  and t1.type=?  and t2.insId=0  order by nlssort(t1.nameZh,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, typeId);
  }

  public List<ConstFundAgency> findInsFundAgency(Long insId, Long psnId, Long typeId) {
    String hql =
        "select distinct new ConstFundAgency(t1.id,t1.nameZh,t1.nameEn,t1.code) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=? and t1.type=?";
    return super.find(hql, insId, psnId, typeId);
  }

  public List<ConstFundAgency> findConstFundAgencyNotIns(Long insId, Long typeId) {
    String hql =
        "select distinct new ConstFundAgency(t1.id,t1.nameZh,t1.nameEn,t1.code) from ConstFundAgency t1 where t1.insId=0 and not exists(select 1 from InsFund t3 where t3.agencyId=t1.id and t3.insId=?) and t1.type=?";
    return super.find(hql, insId, typeId);
  }

  public List<ConstFundAgency> findConstFundAgencyNotIns2(Long insId, Long typeId) {
    String hql =
        "select distinct new ConstFundAgency(t1.id,t1.nameZh,t1.nameEn,t1.code) from ConstFundAgency t1,ConstFundCategory t2 where t1.id=t2.agencyId  and not exists(select 1 from InsFund t3 where t3.categoryId=t2.id and t3.insId=?) and t1.type=?  and t2.insId=0";
    return super.find(hql, insId, typeId);
  }

  public List<ConstRegion> findInsFundRegion(Long insId, Long psnId, Long typeId) {
    String hql =
        "select distinct new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstFundAgency t2,InsFundSearch t3 where t1.id=t2.regionId and t2.id=t3.agencyId and t3.insId=? and t3.psnId=? and t2.type=? order by nlssort(t1.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, new Object[] {insId, psnId, typeId});
  }

  public List<ConstRegion> findConstFundRegionByAgency(Long typeId) {
    String hql =
        "select distinct new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstFundAgency t2 where t1.id=t2.regionId and t2.type=? and t2.insId=0  order by nlssort(t1.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, new Object[] {typeId});
  }

  public List<ConstRegion> findConstFundRegionByCategory(Long typeId) {
    String hql =
        "select distinct new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstFundAgency t2,ConstFundCategory t3 where t1.id=t2.regionId and t2.id=t3.agencyId and t2.type=? and t3.insId=0 order by nlssort(t1.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, new Object[] {typeId});
  }

  public List<ConstRegion> findConstFundRegionNotIns(Long insId, Long typeId) {
    String hql =
        "select distinct new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstFundAgency t2 where t2.insId=0 and not exists(select 1 from InsFund t3 where t3.agencyId=t2.id and t3.insId=? ) and t1.id=t2.regionId and t2.type=? order by nlssort(t1.zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, new Object[] {insId, typeId});
  }

  public ConstRegion getRegion(Long regionId) {
    String hql = "select new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1 where t1.id=?";
    return super.findUnique(hql, new Object[] {regionId});
  }

  public ConstRegion getRegionBySuper(Long regionId) {
    String hql =
        "select new ConstRegion(t1.id,t1.zhName,t1.enName) from ConstRegion t1,ConstRegion t2 where t1.id=t2.superRegionId and t2.id=? and t1.superRegionId=?";
    return super.findUnique(hql, new Object[] {regionId, 156L});
  }

  public Page<ConstFundAgency> findFundAgency(Page<ConstFundAgency> page, FundForm form) {
    StringBuilder hql = new StringBuilder();
    hql.append("from ConstFundAgency where insId=? ");
    List<Object> params = new ArrayList<Object>();
    params.add(0L);
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql.append(" and type=? ");
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql.append(" and id=? ");
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql.append(" and regionId=? ");
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql.append(" and (instr(upper(nameZh),?)>0 or instr(upper(nameEn),?)>0) ");// 中文标题
      String searchKey = HtmlUtils.htmlEscape(form.getSearchKey());
      searchKey = searchKey.toUpperCase().trim();
      params.add(searchKey);
      params.add(searchKey);
    }
    hql.append(" order by useDate desc nulls last,nlssort(nameZh,'NLS_SORT=SCHINESE_PINYIN_M') ");
    return super.findPage(page, hql.toString(), params.toArray());
  }

  public Page<ConstFundAgency> findInsFundAgencyByLeft(Page<ConstFundAgency> page, FundForm form) {
    String hql =
        "select distinct t1 from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=?";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    params.add(form.getPsnId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ?)";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql += " and t1.type=? ";
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t1.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t1.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    Locale locale = LocaleContextHolder.getLocale();
    if (locale.equals(Locale.US)) {
      hql += " order by nlssort(nvl(t1.nameEn,t1.nameZh),'NLS_SORT=SCHINESE_PINYIN_M') " + form.getAgencyOrder()
          + ", t1.id " + form.getAgencyOrder();
    } else {
      hql += " order by nlssort(nvl(t1.nameZh,t1.nameEn),'NLS_SORT=SCHINESE_PINYIN_M') " + form.getAgencyOrder()
          + ", t1.id " + form.getAgencyOrder();
    }
    return super.findPage(page, hql, params.toArray());
  }

  public Page<ConstFundAgency> findAllInsFundAgencyByLeft(Page<ConstFundAgency> page, FundForm form) {
    String hql = "from ConstFundAgency t1 where t1.insId=? "
        + "and not exists(select 1 from InsFund t2 where t2.agencyId=t1.id and t2.insId=? ) "
        + " and not exists(select 1 from ConstFundAgency t3 where t3.insId=? and t3.parentAgencyId=t1.id) ";
    List<Object> params = new ArrayList<Object>();
    params.add(0L);
    params.add(form.getInsId());
    params.add(form.getInsId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ?)";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql += " and t1.type=? ";
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t1.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t1.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    return super.findPage(page, hql, params.toArray());
  }

  public int findInsFundAgencyLeftCount(FundForm form) {
    String hql =
        "select count(distinct t1.id) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=?";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getInsId());
    params.add(form.getPsnId());
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      hql += " and (t1.nameZh like ? or upper(t1.nameEn) like ?)";
      params.add("%" + form.getSearchKey().trim() + "%");
      params.add("%" + form.getSearchKey().trim().toUpperCase() + "%");
    }
    if (StringUtils.isNotBlank(form.getAgencyTypeId())) {
      hql += " and t1.type=? ";
      params.add(NumberUtils.toLong(form.getAgencyTypeId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyId())) {
      hql += " and t1.id=? ";
      params.add(NumberUtils.toLong(form.getAgencyId()));
    }
    if (StringUtils.isNotBlank(form.getAgencyRegionId())) {
      hql += " and t1.regionId=? ";
      params.add(NumberUtils.toLong(form.getAgencyRegionId()));
    }
    return ((Long) super.findUnique(hql, params.toArray())).intValue();
  }

  public int agencySecondMenuCount(Long insId, Long psnId, Long TypeId) {
    String hql =
        "select count(distinct t1.id) from ConstFundAgency t1,InsFundSearch t2 where t1.id=t2.agencyId and t2.insId=? and t2.psnId=? and t1.regionId=? ";
    return ((Long) super.findUnique(hql, insId, psnId, TypeId)).intValue();
  }

  public void remvoeInsFundAgency(Long insId, Long agencyId) {
    String hql = "delete from ConstFundAgency t where t.insId=? and t.id=?";
    super.createQuery(hql, insId, agencyId).executeUpdate();
  }

  public void auditInsFundAgency(Long id, int status) {
    String hql = "update ConstFundAgency t set t.status=? where t.id=?";
    super.createQuery(hql, status, id).executeUpdate();
  }

  public Page<ConstFundAgency> findAllInsFundAgency(Page<ConstFundAgency> page, FundForm form) {
    String hql =
        "from ConstFundAgency t1 where t1.insId=? and not exists(select 1 from InsFund t2 where t2.agencyId=t1.id and t2.insId=?) "
            + " and not exists(select 1 from ConstFundAgency t3 where t3.parentAgencyId=t1.id and t3.insId=?) ";
    return super.findPage(page, hql, 0L, form.getInsId(), form.getInsId());
  }

  public int findAllInsFundAgencyCount() {
    String hql =
        "select count(t1.id) from ConstFundAgency t1 where t1.insId=? and not exists(select 1 from InsFund t2 where t2.agencyId=t1.id )";
    return ((Long) super.findUnique(hql, 0L)).intValue();
  }

  public List<ConstFundAgency> getInsFundAgencyLeft(Long insId, Long psnId) {
    String hql =
        "select distinct new ConstFundAgency(t3.id,t3.nameZh,t3.nameEn,t3.code) from ConstFundCategory t1,InsFundSearch t2,ConstFundAgency t3 where t1.id=t2.categoryId and t1.agencyId=t3.id and t2.insId=? and t2.psnId=? ";
    return super.createQuery(hql, insId, psnId).list();
  }

  /**
   * 临时处理 根据资助机构名称获取机构信息.
   * 
   * @param insId
   * @param agencyName
   * @return
   */
  @SuppressWarnings("unchecked")
  public ConstFundAgency getFundAgencyByName(Long insId, String agencyName) {
    String hql =
        "select new ConstFundAgency(t.id,t.logoUrl) from ConstFundAgency t where t.insId in(?,0) and (t.nameZh=? or lower(t.nameEn)=?) order by t.insId desc";
    List<ConstFundAgency> list =
        super.createQuery(hql, insId, agencyName.trim(), agencyName.trim().toLowerCase()).setMaxResults(1).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    return list.get(0);
  }

  public List<ConstFundAgency> getAgencyIdLikeKey(String key) {
    String hql =
        "select new ConstFundAgency(t.id,t.nameZh,t.nameEn,t.code)  FROM ConstFundAgency t where t.insId =:insId and  t.nameZh like :key or upper(t.nameEn) like :key";

    return super.createQuery(hql).setParameter("key", "%" + key + "%").setParameter("insId", 0L).list();
  }

  public ConstFundAgency getFundAgencyByName(String key) {
    String hql =
        "select new ConstFundAgency(t.id,t.nameZh,t.nameEn,t.code)  FROM ConstFundAgency t where t.insId =:insId and  t.nameZh = :key or upper(t.nameEn) = :key";
    List<ConstFundAgency> list =
        super.createQuery(hql).setParameter("key", key).setParameter("insId", 0L).setMaxResults(1).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    return list.get(0);
  }

}
