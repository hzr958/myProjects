package com.smate.center.open.dao.iris;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.SiePublicationList;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 合肥工业大学期刊、论文被收录统计Dao
 * 
 * @author zll
 *
 */
@Repository
public class HfutPubListStatsDao extends RolHibernateDao<SiePublicationList, Long> {

  @SuppressWarnings("unchecked")
  public Map<String, Object> getPubListStats(Map<String, Object> pubListStats, Map<String, Object> dataMap) {
    String countEi = "select count(p.list_ei)";
    String countSci = "select count(p.list_sci)";
    String countSsci = "select count(p.list_ssci)";
    String countIstp = "select count(p.list_istp)";
    StringBuilder sql = new StringBuilder();
    sql.append(
        "from pub_list p where exists(select 1  from publication t where p.pub_id=t.pub_id and t.ins_id=1392 and t.status=2 "
            + "and to_date(nvl(t.publish_year,1900)||'-'||nvl(t.publish_month,1)||'-'||nvl(t.publish_day,1),'yyyy-MM-dd')"
            + "between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')");
    Integer typeId = Integer.valueOf(dataMap.get("typeId").toString());
    if (typeId == 3) {
      sql.append("and t.pub_type=3");
    } else {
      sql.append("and t.pub_type=4");
    }
    sql.append(")");
    String hqlEi = "and p.list_ei=1";
    String hqlSci = "and p.list_sci=1";
    String hqlSsci = "and p.list_ssci=1";
    String hqlIstp = "and p.list_istp=1";
    String startDate = dataMap.get("startDate").toString();
    String endDate = dataMap.get("endDate").toString();
    // 统计数
    long totalCountEi = super.queryForLong(countEi + sql + hqlEi, new Object[] {startDate, endDate});
    long totalCountSci = super.queryForLong(countSci + sql + hqlSci, new Object[] {startDate, endDate});
    long totalCountSsci = super.queryForLong(countSsci + sql + hqlSsci, new Object[] {startDate, endDate});
    long totalCountIstp = super.queryForLong(countIstp + sql + hqlIstp, new Object[] {startDate, endDate});

    pubListStats.put("totalCountEi", totalCountEi);
    pubListStats.put("totalCountSci", totalCountSci);
    pubListStats.put("totalCountSsci", totalCountSsci);
    pubListStats.put("totalCountIstp", totalCountIstp);

    return pubListStats;
  }


}
