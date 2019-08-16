package com.smate.center.open.dao.data.isis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.CriteriaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 结题报告成果Dao
 * 
 * @author hp
 */
@Repository
public class NsfcPrjPubReportDao extends RolHibernateDao<NsfcPrjPubReport, Long> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   * @param insName 机构名称
   * @param rptYear 报告年度
   * @return NsfcPrjPubReport list
   */
  @SuppressWarnings("unchecked")
  public List<NsfcPrjPubReport> getNsfcPrjPubReportList(String insName, Long rptYear) {
    String hql = "from NsfcPrjPubReport n where n.insName=:insName and n.rptYear=:rptYear and n.status=1"
        + " and n.rowid in ( select min(t2.rowid) from  NsfcPrjPubReport t2 where t2.insName=:insName and t2.rptYear=:rptYear and t2.status=1  group by  t2.pubTitle )";
    return super.createQuery(hql).setParameter("insName", insName).setParameter("rptYear", rptYear).list();
  }

  public void deleteByDeliverDate(Date date) {
    String hql = "delete from  NsfcPrjPubReport n where trunc(n.deliverDate)=trunc(?)";
    super.createQuery(hql, date).executeUpdate();
  }



  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getStatData2(Map<String, Object> params) {
    String sqlString = "select sum(sci) sci,sum(ei) ei,sum(istp) istp from NSFC_PRJ_PUB_REPORT t " + " where t.status=1"
        + " and t.rpt_year>=:start_year and t.rpt_year<=:end_year " + " and t.ins_name=:ins_name"
        + " and t.rowid in ( select min(t2.rowid) from  NSFC_PRJ_PUB_REPORT t2 where t2.rpt_year>=:start_year and t2.rpt_year<=:end_year and t2.status=1 and t2.ins_name=:ins_name  group by  t2.pub_title )";

    return super.getSession().createSQLQuery(sqlString)
        .setParameter("start_year", Long.valueOf(params.get("start_year").toString()))
        .setParameter("end_year", Long.valueOf(params.get("end_year").toString()))
        .setParameter("ins_name", params.get("ins_name").toString())
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getStatData3(Map<String, Object> params) {
    String sqlString = "select count(distinct prj_id) prjcou,sum(greatest(ssci,sci)) ssci,sum(ei) ei,sum(hxj) hxj"
        + ",sum(case when t.pub_type=2 then 1 else 0 end ) book,"
        + " sum(case when t.pub_type=7 then 1 else 0 end ) other," + " count(1) thesis," + "  sum(is_tag) tag,"
        + "  round(sum(is_tag)/sum(1),4)   tagPercent,"
        + "  sum(case when lower(romeo_colour) in ('yellow','blue','green') then 1 else 0 end ) open,"
        + "  round(sum(case when lower(romeo_colour) in ('yellow','blue','green') then 1 else 0 end )/sum(1),4) openPercent"
        + " from NSFC_PRJ_PUB_REPORT t  " + " where t.status=1 " + "  and t.rpt_year=:rpt_year"
        + "  and t.ins_name=:ins_name"
        + " and t.rowid in ( select min(t2.rowid) from  NSFC_PRJ_PUB_REPORT t2 where   t2.rpt_year=:rpt_year and t2.status=1 and t2.ins_name=:ins_name  group by  t2.pub_title )";

    return super.getSession().createSQLQuery(sqlString)
        .setParameter("rpt_year", Long.valueOf(params.get("rpt_year").toString()))
        .setParameter("ins_name", params.get("ins_name").toString())
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getStatData4(Map<String, Object> params) {
    String sqlString =
        "select np.depno,np.depname ,1 prjtype,count(distinct t.prj_id) prjcou,sum(greatest(ssci,sci)) ssci,sum(ei) ei"
            + ",sum(hxj) hxj,sum(case when t.pub_type=2 then 1 else 0 end ) book,"
            + " sum(case when t.pub_type=7 then 1 else 0 end ) other," + " count(1) thesis," + " sum(is_tag) tag,"
            + " round(sum(is_tag)/sum(1),4)   tagPercent,"
            + " sum(case when lower(romeo_colour) in ('yellow','blue','green') then 1 else 0 end ) open,"
            + " round(sum(case when lower(romeo_colour) in ('yellow','blue','green') then 1 else 0 end )/sum(1),4) openPercent"
            + " from NSFC_PRJ_PUB_REPORT t  left join  nsfc_project np on np.prj_id=t.prj_id" + " where t.status=1 "
            + "  and t.rpt_year=:rpt_year" + "  and t.ins_name=:ins_name"
            + " and t.rowid in ( select min(t2.rowid) from  NSFC_PRJ_PUB_REPORT t2 left join  nsfc_project np on np.prj_id=t2.prj_id  where  t2.rpt_year=:rpt_year and t2.status=1  and t2.ins_name=:ins_name  group by   t2.pub_title,np.depno,np.depname )"

            + "group by np.depno,np.depname";

    return super.getSession().createSQLQuery(sqlString)
        .setParameter("rpt_year", Long.valueOf(params.get("rpt_year").toString()))
        .setParameter("ins_name", params.get("ins_name").toString())
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getStatData5(Map<String, Object> params) {
    String sqlString =
        "select np.granttypeid,np.granttypename ,1 prjtype,count(distinct t.prj_id) prjcou,sum(greatest(ssci,sci)) ssci,sum(ei) ei"
            + ",sum(hxj) hxj,sum(case when t.pub_type=2 then 1 else 0 end ) book,"
            + " sum(case when t.pub_type=7 then 1 else 0 end ) other," + " count(1) thesis," + " sum(is_tag) tag,"
            + " round(sum(is_tag)/sum(1),4)   tagPercent,"
            + " sum(case when lower(romeo_colour) in ('yellow','blue','green') then 1 else 0 end ) open,"
            + " round(sum(case when lower(romeo_colour) in ('yellow','blue','green') then 1 else 0 end )/sum(1),4) openPercent"
            + " from NSFC_PRJ_PUB_REPORT t  left join  nsfc_project np on np.prj_id=t.prj_id" + " where t.status=1 "
            + "  and t.rpt_year=:rpt_year" + "  and t.ins_name=:ins_name"
            + " and t.rowid in ( select min(t2.rowid) from  NSFC_PRJ_PUB_REPORT t2 left join  nsfc_project np on np.prj_id=t2.prj_id  where  t2.rpt_year=:rpt_year and t2.status=1  and t2.ins_name=:ins_name  group by   t2.pub_title,np.granttypeid,np.granttypename )"
            + "group by np.granttypeid,np.granttypename ";

    return super.getSession().createSQLQuery(sqlString)
        .setParameter("rpt_year", Long.valueOf(params.get("rpt_year").toString()))
        .setParameter("ins_name", params.get("ins_name").toString())
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
  }
}
