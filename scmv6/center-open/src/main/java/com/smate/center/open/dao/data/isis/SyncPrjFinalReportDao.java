package com.smate.center.open.dao.data.isis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.CriteriaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.isis.model.data.isis.NsfcPrjPubReport;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 结题报告成果Dao
 * 
 * @author hp
 */
@Repository
public class SyncPrjFinalReportDao extends SnsHibernateDao<NsfcPrjPubReport, Long> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 为SIE提供报告数据 ds:sns
   * 
   * @param insName deliverDate
   * @return Report list
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPrjFinalReportList(Date deliverDate) {
    String sql =
        " select   npr.rpt_id,npr.nsfc_rpt_id,npr.rpt_year,npr.rpt_type,npr.status,np.ins_id,np.ins_name,np.nsfc_prj_id nsfc_prj_id,np.prj_id prj_id,nprp.pub_id,"
            + "  case when jg.grade is null then 4 "
            + "       else jg.grade end grade,nprp.pub_type,nprp.is_tag,p.jid,null romeo_colour,"
            + "   case when upper(nprp.list_info) like '%,SCI%' or upper(nprp.list_info) like 'SCI%'  then 1 else 0 end sci , "
            + "   case when upper(nprp.list_info) like '%EI%' then 1 else 0 end ei,"
            + "   case when upper(nprp.list_info) like '%ISTP%' then 1 else 0 end istp, "
            + "   case when upper(nprp.list_info) like '%SSCI%' then 1 else 0 end ssci,npr.deliver_date"
            + "  ,p.source_db_id,pf.fundinfo,np.pno prj_external_no,to_char(nprp.title) pub_title"
            + "  from  nsfc_prj_report npr" + "  inner join  nsfc_project  np on npr.prj_id=np.prj_id"
            + "  inner join nsfc_prj_rpt_pub nprp on npr.rpt_id=nprp.rpt_id"
            + "   left join publication p on p.pub_id=nprp.pub_id"
            + "  left join pub_fundinfo pf on p.pub_id=pf.pub_id "
            + "   left join pub_journal pj on pj.pub_id=nprp.pub_id"
            + "   left join journal_grade jg on pj.issn=jg.issn"
            + "     where   npr.rpt_type=1 and trunc(npr.deliver_date)=trunc(?)  ";
    return super.getSession().createSQLQuery(sql).setParameter(0, deliverDate)
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
  }

  /** 获取项目负责人邮箱和姓名 **/
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPrjInfoMap(Set<Long> prjIdSet) {
    String sql =
        "select n.prj_id,p.name,p.email from Nsfc_Project n,person p  where p.psn_id=n.pi_psn_id and  n.prj_Id in (:prjIdSet) ";
    return super.getSession().createSQLQuery(sql).setParameterList("prjIdSet", prjIdSet)
        .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
  }
}
