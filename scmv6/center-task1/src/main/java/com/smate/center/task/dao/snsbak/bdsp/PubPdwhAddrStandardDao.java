package com.smate.center.task.dao.snsbak.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.bdsp.PubPdwhAddrStandard;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class PubPdwhAddrStandardDao extends SnsbakHibernateDao<PubPdwhAddrStandard, Long> {
  public List<PubPdwhAddrStandard> findlist(int batchSize) {
    String hql =
        "from PubPdwhAddrStandard t1 where t1.pubType in(3,4,5) and t1.provinceId<>0 and t1.cityId <>0 and t1.insId <>0 and not exists(select 1 from PubPdwhAddrStandardTemp t2 where t1.id=t2.id)";
    return super.createQuery(hql).setMaxResults(batchSize).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubPdwhAddrStandard(Long pubId) {
    String hql = "select t.provinceId from PubPdwhAddrStandard t where t.pdwhPubId = :pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }


  /**
   * 删除PUB_PDWH_ADDR_STANDARD_TEMP中pubId重复的数据
   */
  public void delRepeatData() {
    String sql = " delete PUB_PDWH_ADDR_STANDARD_TEMP t1 where t1.id not in ( " + " select  m.id from( "
        + " select max(t.id) as id,count(1) as count,t.pub_id from PUB_PDWH_ADDR_STANDARD_TEMP t  group by t.pub_id "
        + " ) m " + " ) ";
    this.getSession().createSQLQuery(sql).executeUpdate();
  }

  /**
   * 删除统计分析和对比分析表的数据
   */
  public void delOldData() {

    String sql1 = " delete BDSP_STAT_SUM ";
    String sql2 = " delete BDSP_STAT_AREA ";
    String sql3 = " delete BDSP_COMPARE_ORGANIZATION ";
    String sql4 = " delete BDSP_COMPARE_province ";
    String sql5 = " delete bdsp_stat_organization ";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();
    this.getSession().createSQLQuery(sql4).executeUpdate();
    this.getSession().createSQLQuery(sql5).executeUpdate();
  }

  /**
   * 项目数据-分组到统计对比分析表
   */
  public void dealPrjData() {

    String sql1 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time)  "
        + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from ("
        + " select 3,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from BDSP_PROJECT_TEMP t "
        + " group by t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql2 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval ,m.* from ("
            + " select 3,t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from BDSP_PROJECT_TEMP t "
            + " group by t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql3 =
        " insert into BDSP_COMPARE_ORGANIZATION(id, index_id, org_id,year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_COMPARE_ORGANIZATION.nextval,m.*  from ("
            + " select 60,t.org_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from BDSP_PROJECT_TEMP t "
            + " group by t.org_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql4 =
        " insert into BDSP_COMPARE_province(id, index_id,prov_id, year_id,grant_type_id,techfiled_id,result,update_time)  "
            + " select  SEQ_BDSP_COMPARE_province.nextval ,m.* from ("
            + " select 60,t.prov_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from BDSP_PROJECT_TEMP t "
            + " group by t.prov_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql5 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id) "
            + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from ("
            + " select 3,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate,t.org_id from BDSP_PROJECT_TEMP t "
            + " group by t.stat_year,t.grant_type_id,t.techfiled_id,t.org_id) m ";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();
    this.getSession().createSQLQuery(sql4).executeUpdate();
    this.getSession().createSQLQuery(sql5).executeUpdate();
  }

  /**
   * 申请数据-分组到统计对比分析表
   */
  public void dealPrpData() {

    String sql1 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time) "
        + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 1,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from BDSP_PROPOSAL_TEMP t "
        + " group by t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql2 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval ,m.* from (select 1,t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from BDSP_PROPOSAL_TEMP t "
            + " group by t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql3 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id)"
            + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from ("
            + " select 1,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate,t.org_id from BDSP_PROPOSAL_TEMP t "
            + " group by t.stat_year,t.grant_type_id,t.techfiled_id,t.org_id) m";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();
  }

  /**
   * 资助率-分组到统计对比分析表
   */
  public void dealFundRateData() {

    String sql1 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval,m.*  from ("
            + " select 5,t1.city_id, t1.year_id,t1.grant_type_id,t1.techfiled_id,t1.result/t2.result,sysdate from ("
            + " select t.city_id, t.year_id,t.grant_type_id,t.techfiled_id,t.result from BDSP_STAT_AREA t where t.index_id=3"
            + " ) t1,("
            + " select t0.city_id,  t0.year_id,t0.grant_type_id,t0.techfiled_id,t0.result from BDSP_STAT_AREA t0 where t0.index_id=1"
            + "	) t2 where t1.city_id=t2.city_id and t1.year_id=t2.year_id and t1.grant_type_id=t2.grant_type_id and t1.techfiled_id=t2.techfiled_id "
            + " ) m";
    String sql2 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time) "
        + " select  SEQ_BDSP_STAT_SUM.nextval,m.*  from ("
        + " select 5, t1.year_id,t1.grant_type_id,t1.techfiled_id,t1.result/t2.result,sysdate from ("
        + " select t.year_id,t.grant_type_id,t.techfiled_id,t.result from BDSP_STAT_SUM t where t.index_id=3"
        + " ) t1,("
        + " select  t0.year_id,t0.grant_type_id,t0.techfiled_id,t0.result from BDSP_STAT_SUM t0 where t0.index_id=1"
        + " ) t2 where t1.year_id=t2.year_id and t1.grant_type_id=t2.grant_type_id and t1.techfiled_id=t2.techfiled_id  "
        + " ) m";
    String sql3 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id) "
            + " select  SEQ_BDSP_STAT_SUM.nextval,m.*  from ("
            + " select 5, t1.year_id,t1.grant_type_id,t1.techfiled_id,t1.result/t2.result,sysdate,t1.org_id from ("
            + " select t.year_id,t.grant_type_id,t.techfiled_id,t.result,t.org_id from bdsp_stat_organization t where t.index_id=3"
            + " ) t1,("
            + "   select  t0.year_id,t0.grant_type_id,t0.techfiled_id,t0.result,t0.org_id from bdsp_stat_organization t0 where t0.index_id=1"
            + " ) t2 where t1.year_id=t2.year_id and t1.grant_type_id=t2.grant_type_id and t1.techfiled_id=t2.techfiled_id and t1.org_id=t2.org_id"
            + " ) m";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();

  }

  /**
   * 论文数据-分组到统计对比分析表
   */
  public void dealPaperData() {

    String sql1 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time) "
        + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 6,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP t "
        + " where t.pub_type in(4,3) group by t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql2 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval ,m.* from (select 6,t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP t "
            + " where t.pub_type in (4,3) group by t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql3 =
        " insert into BDSP_COMPARE_ORGANIZATION(id, index_id,org_id, year_id,grant_type_id,techfiled_id,result,update_time)"
            + " select  SEQ_BDSP_COMPARE_ORGANIZATION.nextval ,m.* from (select  21,t.org_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP t "
            + " where t.pub_type in(4,3) group by t.org_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql4 =
        " insert into BDSP_COMPARE_province(id, index_id,prov_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_COMPARE_province.nextval ,m.* from (select 21,t.prov_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP  t "
            + " where t.pub_type in(4,3) group by t.prov_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql5 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id) "
            + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 6,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate,t.org_id from PUB_PDWH_ADDR_STANDARD_TEMP t "
            + " where t.pub_type in(4,3) group by t.stat_year,t.grant_type_id,t.techfiled_id,t.org_id) m";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();
    this.getSession().createSQLQuery(sql4).executeUpdate();
    this.getSession().createSQLQuery(sql5).executeUpdate();

  }

  /**
   * 专利数据-分组到统计对比分析表
   */
  public void dealPatentData() {

    String sql1 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time) "
        + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 8,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP t "
        + " where t.pub_type =5 group by t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql2 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval ,m.* from (select 8,t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP t"
            + " where t.pub_type =5 group by t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql3 =
        " insert into BDSP_COMPARE_ORGANIZATION(id, index_id,org_id, year_id,grant_type_id,techfiled_id,result,update_time)"
            + " select  SEQ_BDSP_COMPARE_ORGANIZATION.nextval ,m.* from (select  23,t.org_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP t"
            + "	where t.pub_type =5 group by t.org_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql4 =
        " insert into BDSP_COMPARE_province(id, index_id,prov_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_COMPARE_province.nextval ,m.* from (select 23,t.prov_id,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate from PUB_PDWH_ADDR_STANDARD_TEMP  t"
            + " where t.pub_type =5 group by t.prov_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql5 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id) "
            + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 8,t.stat_year,t.grant_type_id,t.techfiled_id,count(1),sysdate,t.org_id from PUB_PDWH_ADDR_STANDARD_TEMP t "
            + " where t.pub_type =5 group by t.stat_year,t.grant_type_id,t.techfiled_id,t.org_id) m";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();
    this.getSession().createSQLQuery(sql4).executeUpdate();
    this.getSession().createSQLQuery(sql5).executeUpdate();

  }

  /**
   * 项目金额-分组到统计对比分析表
   */
  public void dealPrjAmt() {

    String sql1 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time) "
        + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 4,t.stat_year,t.grant_type_id,t.techfiled_id,sum(t.req_amt),sysdate from BDSP_PROJECT_TEMP t "
        + " group by t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql2 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval ,m.* from (select 4,t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id,sum(t.req_amt),sysdate from BDSP_PROJECT_TEMP t "
            + " group by t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql3 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id) "
            + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 4,t.stat_year,t.grant_type_id,t.techfiled_id,sum(t.req_amt),sysdate,t.org_id from BDSP_PROJECT_TEMP t "
            + " group by t.stat_year,t.grant_type_id,t.techfiled_id,t.org_id) m";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();

  }

  /**
   * 申请金额-分组到统计对比分析表
   */
  public void dealPrpAmt() {

    String sql1 = " insert into BDSP_STAT_SUM(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time) "
        + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 2,t.stat_year,t.grant_type_id,t.techfiled_id,sum(t.req_amt),sysdate from BDSP_PROPOSAL_TEMP t"
        + " group by t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql2 =
        " insert into BDSP_STAT_AREA(id, index_id,city_id, year_id,grant_type_id,techfiled_id,result,update_time) "
            + " select  SEQ_BDSP_STAT_AREA.nextval ,m.* from (select 2,t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id,sum(t.req_amt),sysdate from BDSP_PROPOSAL_TEMP t "
            + " group by t.city_id,t.stat_year,t.grant_type_id,t.techfiled_id) m";
    String sql3 =
        " insert into bdsp_stat_organization(id, index_id, year_id,grant_type_id,techfiled_id,result,update_time,org_id) "
            + " select  SEQ_BDSP_STAT_SUM.nextval ,m.* from (select 2,t.stat_year,t.grant_type_id,t.techfiled_id,sum(t.req_amt),sysdate,t.org_id from BDSP_PROPOSAL_TEMP t "
            + " group by t.stat_year,t.grant_type_id,t.techfiled_id,t.org_id) m";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();
  }

  /**
   * 清空临时数据 （该数据是从各个原数据跑出来的有所有分组信息的数据，用来直接分组到统计表的）
   */
  public void delTempData() {
    String sql1 = " delete BDSP_PROJECT_TEMP ";
    String sql2 = " delete BDSP_PROPOSAL_TEMP ";
    String sql3 = " delete PUB_PDWH_ADDR_STANDARD_TEMP ";

    this.getSession().createSQLQuery(sql1).executeUpdate();
    this.getSession().createSQLQuery(sql2).executeUpdate();
    this.getSession().createSQLQuery(sql3).executeUpdate();

  }

}
