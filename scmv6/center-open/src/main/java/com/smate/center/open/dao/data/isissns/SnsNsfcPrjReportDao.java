package com.smate.center.open.dao.data.isissns;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isissns.SnsNsfcPrjReport;

import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * @author hp
 * @date 2016-02-16
 */
@Repository
public class SnsNsfcPrjReportDao extends SnsHibernateDao<SnsNsfcPrjReport, Long> {

  @SuppressWarnings("unchecked")
  public List<SnsNsfcPrjReport> getNsfcPrjReportList(Map<String, Object> dataMap) {
    String querystring = "from SnsNsfcPrjReport npr" + "            where exists (  select 1 from SnsNsfcProject np "
        + "                    where npr.prjId=np.prjId   and  np.insName=:ins_name" + "                 ) "
        + "              and rptYear=:rpt_year and status=1 and rptType=1";
    return super.getSession().createQuery(querystring)
        .setParameter("rpt_year", Long.valueOf(dataMap.get("rpt_year").toString()))
        .setParameter("ins_name", dataMap.get("ins_name").toString()).list();
  }

  @SuppressWarnings("unchecked")
  public Map<String, Object> getPubStatByIns(Map<String, Object> dataMap) {
    String queryString = "select   award_nat_zrkx1," + "award_nat_zrkx2," + "award_nat_kjjb1," + "award_nat_kjjb2,"
        + "award_nat_fm1," + "award_nat_fm2," + "award_prv_zrkx1," + "award_prv_zrkx2 ," + "award_prv_kjjb1 ,"
        + "award_prv_kjjb2 ," + "award_int_xs    ," + "award_other     ," + "report_int_ty," + "report_int_fz,"
        + "report_nat_ty," + "report_nat_fz   ," + "journal_home_yb  ," + "journal_home_hx ," + "journal_int    ,"
        + "journal_idx_ei  ," + "journal_idx_istp," + "journal_idx_sci ," + "journal_idx_isr ," + "zh_ycb,"
        + "zh_dcb           ," + "en_ycb          ," + "en_dcb         ," + "home_app   ," + "home_auth ,"
        + "abroad_app ," + "abroad_auth" + "   from isis_rpt_pub_stat t"
        + " where t.ins_Name=:ins_name and t.rpt_year=:rpt_year";

    Object map = super.getSession().createSQLQuery(queryString)
        .setParameter("rpt_year", Long.valueOf(dataMap.get("rpt_year").toString()))
        .setParameter("ins_name", dataMap.get("ins_name").toString())
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).uniqueResult();
    if (map == null)
      map = new HashMap<String, Object>();
    return (Map<String, Object>) map;
  }


}
