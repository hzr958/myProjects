package com.smate.center.batch.dao.rol.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.KpiPubUnit;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 成果院系表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class KpiPubUnitDao extends RolHibernateDao<KpiPubUnit, Long> {

  /**
   * 删除指定部门之外的成果关联关系.
   * 
   * @param pubId
   * @param remainIds
   */
  public void removeExtPubUnit(Long pubId, Set<Long> remainIds) {

    // 如果没有保留的部门，则删除全部
    if (remainIds == null || remainIds.size() == 0) {
      this.removeKpiPubUnit(pubId);
    } else {
      String hql = "delete from KpiPubUnit where pubId = :pubId and unitId not in(:unitIds) ";
      super.createQuery(hql).setParameter("pubId", pubId).setParameterList("unitIds", remainIds).executeUpdate();
    }
  }

  /**
   * 获取成果关联的部门冗余列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KpiPubUnit> getKpiPubUnitByPubId(Long pubId) {

    String hql = "from KpiPubUnit where pubId = ? ";
    return super.createQuery(hql, pubId).list();
  }

  /**
   * 删除成果部门关系冗余.
   * 
   * @param pubId
   */
  public void removeKpiPubUnit(Long pubId) {
    String hql = "delete from KpiPubUnit where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 部门质量统计_按数量计算.<br/>
   * 
   * @param listEi
   * @param listSci
   * @param listIstp
   * @param listSsci
   * @param startYear
   * @param endYear
   * @param endMonth
   * @param selectedPubType
   * @param selectedInsIds
   * @param onlyConfirm
   * @param ofset
   * @return
   */
  public List statKpiPubUnitByQuantity(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer startYear, Integer endYear, int endMonth, String selectedPubType, String selectedInsIds,
      Integer onlyConfirm, int ofset) throws DaoException {
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    sb.append("select count(id) TOTAL,t.PUBLISH_YEAR,t.UNIT_ID from (select ");
    sb.append(" id,t.PUBLISH_MONTH,t.LIST_EI,t.LIST_SCI,t.LIST_ISTP,t.LIST_SSCI,t.PUB_TYPE,t.UNIT_ID,t.PUB_ID,");
    if (ofset > 0) {
      sb.append(
          " case when  decode(t.publish_month,null,1,t.publish_month)<=? then t.publish_year-1 else t.publish_year end PUBLISH_YEAR  ");
      params.add(endMonth);
    } else {
      sb.append(" t.PUBLISH_YEAR");
    }
    sb.append(" from KPI_PUB_UNIT t   where t.pub_type in(4,3,5)");
    if (onlyConfirm != null && onlyConfirm == 1) {
      sb.append(" and IS_CONFIRM=1");
    }
    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("t.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SCI=1");
      else
        storeStr.append("t.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_ISTP=1");
      else
        storeStr.append("t.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SSCI=1");
      else
        storeStr.append("t.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }
    // 类别
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and t.PUB_TYPE in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and t.PUB_TYPE=?");
      params.add(selectedPubType);
    }
    // 单位
    sb.append(" and t.INS_ID =?");
    params.add(selectedInsIds);
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);
    sb.append(" group by t.PUBLISH_YEAR,t.UNIT_ID order by PUBLISH_YEAR");

    return this.queryForList(sb.toString(), params.toArray());
  }

  /**
   * 部门质量报表统计_按作者人数平均统计.
   * 
   * @param listEi
   * @param listSci
   * @param listIstp
   * @param listSsci
   * @param startYear
   * @param endYear
   * @param endMonth
   * @param selectedPubType
   * @param selectedInsIds
   * @param onlyConfirm
   * @param ofset
   * @return
   * @throws DaoException
   */
  public List statKpiPubUnitByAuthor(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer startYear, Integer endYear, int endMonth, String selectedPubType, String selectedInsIds,
      Integer onlyConfirm, int ofset) throws DaoException {
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    sb.append(" select sum(t.percent) TOTAL,t.PUBLISH_YEAR,t.UNIT_ID  from (select ");
    sb.append(" t.PUBLISH_MONTH,t.LIST_EI,t.LIST_SCI,t.LIST_ISTP,t.LIST_SSCI,t.PUB_TYPE,t.UNIT_ID,t.PUB_ID,t.percent,");
    if (ofset > 0) {
      sb.append(
          " case when  decode(t.publish_month,null,1,t.publish_month)<=? then t.publish_year-1 else t.publish_year end PUBLISH_YEAR  ");
      params.add(endMonth);
    } else {
      sb.append(" t.PUBLISH_YEAR");
    }
    sb.append(" from KPI_PUB_UNIT t   where t.pub_type in(4,3,5)");
    if (onlyConfirm != null && onlyConfirm == 1) {
      sb.append(" and IS_CONFIRM=1");
    }
    // 引用情况.
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("t.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SCI=1");
      else
        storeStr.append("t.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_ISTP=1");
      else
        storeStr.append("t.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SSCI=1");
      else
        storeStr.append("t.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and t.PUB_TYPE in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and t.PUB_TYPE=?");
      params.add(selectedPubType);
    }
    sb.append(" and t.INS_ID =?");
    params.add(selectedInsIds);
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);
    sb.append(" group by t.PUBLISH_YEAR,t.UNIT_ID order by PUBLISH_YEAR");
    return this.queryForList(sb.toString(), params.toArray());
  }

  /**
   * 部门合作度统计.
   * 
   * @param listEi
   * @param listSci
   * @param listIstp
   * @param listSsci
   * @param startYear
   * @param endYear
   * @param endMonth
   * @param selectedPubType
   * @param selectedInsIds
   * @param onlyConfirm
   * @param ofset
   * @return
   */
  public List statKpiCoPubUnit(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci, Integer startYear,
      Integer endYear, int endMonth, String selectedPubType, String selectedInsIds, Integer onlyConfirm, int ofset)
      throws DaoException {
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    sb.append(" select sum(t.copub) TOTAL,t.PUBLISH_YEAR,t.UNIT_ID  from (select ");
    sb.append(" t.PUBLISH_MONTH,t.LIST_EI,t.LIST_SCI,t.LIST_ISTP,t.LIST_SSCI,t.PUB_TYPE,t.UNIT_ID,t.PUB_ID,t.copub,");
    if (ofset > 0) {
      sb.append(
          " case when  decode(t.publish_month,null,1,t.publish_month)<=? then t.publish_year-1 else t.publish_year end PUBLISH_YEAR  ");
      params.add(endMonth);
    } else {
      sb.append(" t.PUBLISH_YEAR");
    }
    sb.append(" from KPI_PUB_UNIT t   where t.pub_type in(4,3,5) and t.COPUB=1");
    if (onlyConfirm != null && onlyConfirm == 1) {
      sb.append(" and IS_CONFIRM=1");
    }
    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("t.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SCI=1");
      else
        storeStr.append("t.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_ISTP=1");
      else
        storeStr.append("t.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SSCI=1");
      else
        storeStr.append("t.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and t.PUB_TYPE in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and t.PUB_TYPE=?");
      params.add(selectedPubType);
    }
    sb.append(" and t.INS_ID =?");
    params.add(selectedInsIds);
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);
    sb.append(" group by t.PUBLISH_YEAR,t.UNIT_ID order by PUBLISH_YEAR");
    return this.queryForList(sb.toString(), params.toArray());
  }

  /**
   * 合作度对比.
   * 
   * @param listEi
   * @param listSci
   * @param listIstp
   * @param listSsci
   * @param startYear
   * @param endYear
   * @param endMonth
   * @param selectedPubType
   * @param selectedInsIds
   * @param onlyConfirm
   * @param ofset
   * @return
   * @throws DaoException
   */
  public List statKpiCoPubSelectUnit(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer startYear, Integer endYear, int endMonth, String selectedPubType, String selectedUnitIds,
      Integer onlyConfirm, int ofset) throws DaoException {
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    sb.append(" select sum(t.copub) TOTAL,t.PUBLISH_YEAR,t.UNIT_ID  from (select ");
    sb.append(" t.PUBLISH_MONTH,t.LIST_EI,t.LIST_SCI,t.LIST_ISTP,t.LIST_SSCI,t.PUB_TYPE,t.UNIT_ID,t.PUB_ID,t.copub,");
    if (ofset > 0) {
      sb.append(
          " case when  decode(t.publish_month,null,1,t.publish_month)<=? then t.publish_year-1 else t.publish_year end PUBLISH_YEAR  ");
      params.add(endMonth);
    } else {
      sb.append(" t.PUBLISH_YEAR");
    }
    sb.append(" from KPI_PUB_UNIT t   where t.pub_type in(4,3,5) and t.COPUB=1");
    if (onlyConfirm != null && onlyConfirm == 1) {
      sb.append(" and IS_CONFIRM=1");
    }
    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("t.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SCI=1");
      else
        storeStr.append("t.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_ISTP=1");
      else
        storeStr.append("t.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SSCI=1");
      else
        storeStr.append("t.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and t.PUB_TYPE in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and t.PUB_TYPE=?");
      params.add(selectedPubType);
    }
    sb.append(" and t.UNIT_ID in(" + selectedUnitIds + ")");
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);
    sb.append(" group by t.PUBLISH_YEAR,t.UNIT_ID order by PUBLISH_YEAR");
    return this.queryForList(sb.toString(), params.toArray());
  }

  /**
   * 部门质量对比-按数量.
   * 
   * @param listEi
   * @param listSci
   * @param listIstp
   * @param listSsci
   * @param startYear
   * @param endYear
   * @param endMonth
   * @param selectedPubType
   * @param selectedUnitIds
   * @param onlyConfirm
   * @param ofset
   * @return
   * @throws DaoException
   */
  public List statKpiPubSelectUnitByQuantity(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer startYear, Integer endYear, int endMonth, String selectedPubType, String selectedUnitIds,
      Integer onlyConfirm, int ofset) throws DaoException {
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    sb.append("select count(id) TOTAL,t.PUBLISH_YEAR,t.UNIT_ID from (select ");
    sb.append(" id,t.PUBLISH_MONTH,t.LIST_EI,t.LIST_SCI,t.LIST_ISTP,t.LIST_SSCI,t.PUB_TYPE,t.UNIT_ID,t.PUB_ID,");
    if (ofset > 0) {
      sb.append(
          " case when  decode(t.publish_month,null,1,t.publish_month)<=? then t.publish_year-1 else t.publish_year end PUBLISH_YEAR  ");
      params.add(endMonth);
    } else {
      sb.append(" t.PUBLISH_YEAR");
    }
    sb.append(" from KPI_PUB_UNIT t   where t.pub_type in(4,3,5)");
    if (onlyConfirm != null && onlyConfirm == 1) {
      sb.append(" and IS_CONFIRM=1");
    }
    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("t.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SCI=1");
      else
        storeStr.append("t.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_ISTP=1");
      else
        storeStr.append("t.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SSCI=1");
      else
        storeStr.append("t.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }
    // 类别
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and t.PUB_TYPE in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and t.PUB_TYPE=?");
      params.add(selectedPubType);
    }
    // 单位
    sb.append(" and t.UNIT_ID in(" + selectedUnitIds + ")");
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);
    sb.append(" group by t.PUBLISH_YEAR,t.UNIT_ID order by PUBLISH_YEAR");

    return this.queryForList(sb.toString(), params.toArray());
  }

  /**
   * 部门质量对比报表-按作者人数平均统计.
   * 
   * @param listEi
   * @param listSci
   * @param listIstp
   * @param listSsci
   * @param startYear
   * @param endYear
   * @param endMonth
   * @param selectedPubType
   * @param selectedUnitIds
   * @param onlyConfirm
   * @param ofset
   * @return
   * @throws DaoException
   */
  public List statKpiPubSelectUnitByAuthor(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer startYear, Integer endYear, int endMonth, String selectedPubType, String selectedUnitIds,
      Integer onlyConfirm, int ofset) throws DaoException {
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    sb.append(" select sum(t.percent) TOTAL,t.PUBLISH_YEAR,t.UNIT_ID  from (select ");
    sb.append(" t.PUBLISH_MONTH,t.LIST_EI,t.LIST_SCI,t.LIST_ISTP,t.LIST_SSCI,t.PUB_TYPE,t.UNIT_ID,t.PUB_ID,t.percent,");
    if (ofset > 0) {
      sb.append(
          " case when  decode(t.publish_month,null,1,t.publish_month)<=? then t.publish_year-1 else t.publish_year end PUBLISH_YEAR  ");
      params.add(endMonth);
    } else {
      sb.append(" t.PUBLISH_YEAR");
    }
    sb.append(" from KPI_PUB_UNIT t   where t.pub_type in(4,3,5)");
    if (onlyConfirm != null && onlyConfirm == 1) {
      sb.append(" and IS_CONFIRM=1");
    }
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("t.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SCI=1");
      else
        storeStr.append("t.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_ISTP=1");
      else
        storeStr.append("t.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or t.LIST_SSCI=1");
      else
        storeStr.append("t.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and t.PUB_TYPE in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and t.PUB_TYPE=?");
      params.add(selectedPubType);
    }
    sb.append(" and t.UNIT_ID in(" + selectedUnitIds + ")");
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);
    sb.append(" group by t.PUBLISH_YEAR,t.UNIT_ID order by PUBLISH_YEAR");
    return this.queryForList(sb.toString(), params.toArray());
  }

  /**
   * 删除部门kpi记录.
   * 
   * @param unitId
   * @throws DaoException
   */
  public void deleteKpiRecord(Long unitId) throws DaoException {
    String hql = "delete from KpiPubUnit t where t.unitId=?";
    super.createQuery(hql, new Object[] {unitId}).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> staKpiUnitPubByPage(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer publishYear, int endMonth, String selectedPubType, Long insId, Long unitId, Integer isConfirm,
      Integer isCoPub, Page<PublicationRol> page) throws DaoException {

    String countHql = "select count(pub_id) from ";
    String orderHql = "order by publish_year desc,pub_id desc ";

    StringBuilder sb = new StringBuilder();
    List<Object> params = new ArrayList<Object>();

    sb.append(" select * from (");
    sb.append(" select kpu.pub_type,kpu.pub_id");
    if (endMonth > 1) {
      sb.append(
          " ,case when decode(kpu.publish_month,null,1,kpu.publish_month)<? then kpu.publish_year-1 else kpu.publish_year end publish_year");
      params.add(endMonth);
    } else {
      sb.append(" ,kpu.publish_year");
    }
    sb.append(" from KPI_PUB_UNIT kpu ");
    if (insId != null) {
      sb.append(" where kpu.unit_id=? and kpu.ins_id=? and kpu.pub_type in(3,4,5) ");
      params.add(unitId);
      params.add(insId);
    } else {
      sb.append(" where kpu.unit_id=? and kpu.pub_type in(3,4,5) ");
      params.add(unitId);
    }

    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("kpu.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SCI=1");
      else
        storeStr.append("kpu.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_ISTP=1");
      else
        storeStr.append("kpu.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SSCI=1");
      else
        storeStr.append("kpu.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }

    if (isConfirm != null && isConfirm == 1) {
      sb.append(" and kpu.IS_CONFIRM=1");
    }
    if (isCoPub != null && isCoPub == 1) {
      sb.append(" and kpu.COPUB=1");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and kpu.pub_type in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and kpu.pub_type=?");
      params.add(selectedPubType);
    }

    sb.append(" ) t where t.publish_year=? ");
    params.add(publishYear);

    // 记录数
    Long totalCount = this.queryForLong(countHql + "(" + sb.toString() + ")", params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    List<Map<String, BigDecimal>> staIdList = this.queryForList("select pub_id from(" + sb.toString() + orderHql + ")",
        params.toArray(), page.getPageSize(), page.getFirst() - 1);
    List<Long> pubIdList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(staIdList)) {
      for (Map<String, BigDecimal> map : staIdList) {
        pubIdList.add(map.get("PUB_ID").longValue());
      }
    }

    return pubIdList;
  }

  @SuppressWarnings("unchecked")
  public List<Long> staKpiUnitStatYearPub(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer startYear, Integer endYear, int endMonth, String selectedPubType, Long insId, Long unitId,
      Integer isConfirm, Integer isCoPub, Page<PublicationRol> page) throws DaoException {

    String countHql = "select count(pub_id) from ";
    String orderHql = "order by publish_year desc,pub_id desc ";

    StringBuilder sb = new StringBuilder();
    List<Object> params = new ArrayList<Object>();

    sb.append(" select * from (");
    sb.append(" select kpu.pub_type,kpu.pub_id");
    if (endMonth > 1) {
      sb.append(
          " ,case when decode(kpu.publish_month,null,1,kpu.publish_month)<? then kpu.publish_year-1 else kpu.publish_year end publish_year");
      params.add(endMonth);
    } else {
      sb.append(" ,kpu.publish_year");
    }
    sb.append(" from KPI_PUB_UNIT kpu ");
    if (insId != null) {
      sb.append(" where kpu.unit_id=? and kpu.ins_id=? and kpu.pub_type in(3,4,5) ");
      params.add(unitId);
      params.add(insId);
    } else {
      sb.append(" where kpu.unit_id=? and kpu.pub_type in(3,4,5) ");
      params.add(unitId);
    }

    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("kpu.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SCI=1");
      else
        storeStr.append("kpu.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_ISTP=1");
      else
        storeStr.append("kpu.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SSCI=1");
      else
        storeStr.append("kpu.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }

    if (isConfirm != null && isConfirm == 1) {
      sb.append(" and kpu.IS_CONFIRM=1");
    }
    if (isCoPub != null && isCoPub == 1) {
      sb.append(" and kpu.COPUB=1");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and kpu.pub_type in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and kpu.pub_type=?");
      params.add(selectedPubType);
    }
    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);

    // 记录数
    Long totalCount = this.queryForLong(countHql + "(" + sb.toString() + ")", params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    List<Map<String, BigDecimal>> staIdList = this.queryForList("select pub_id from(" + sb.toString() + orderHql + ")",
        params.toArray(), page.getPageSize(), page.getFirst() - 1);
    List<Long> pubIdList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(staIdList)) {
      for (Map<String, BigDecimal> map : staIdList) {
        pubIdList.add(map.get("PUB_ID").longValue());
      }
    }

    return pubIdList;
  }

  @SuppressWarnings("unchecked")
  public List<Long> staKpiUnitPub(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci,
      Integer publishYear, int endMonth, String selectedPubType, Long insId, Long unitId, Integer isConfirm,
      Integer isCoPub) throws DaoException {

    String orderHql = "order by publish_year desc,pub_id desc ";

    StringBuilder sb = new StringBuilder();
    List<Object> params = new ArrayList<Object>();

    sb.append(" select * from (");
    sb.append(" select kpu.pub_type,kpu.pub_id");
    if (endMonth > 1) {
      sb.append(
          " ,case when decode(kpu.publish_month,null,1,kpu.publish_month)<? then kpu.publish_year-1 else kpu.publish_year end publish_year");
      params.add(endMonth);
    } else {
      sb.append(" ,kpu.publish_year");
    }
    sb.append(" from KPI_PUB_UNIT kpu ");
    if (insId != null) {
      sb.append(" where kpu.unit_id=? and kpu.ins_id=? and kpu.pub_type in(3,4,5) ");
      params.add(unitId);
      params.add(insId);
    } else {
      sb.append(" where kpu.unit_id=? and kpu.pub_type in(3,4,5) ");
      params.add(unitId);
    }

    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("kpu.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SCI=1");
      else
        storeStr.append("kpu.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_ISTP=1");
      else
        storeStr.append("kpu.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SSCI=1");
      else
        storeStr.append("kpu.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }

    if (isConfirm != null && isConfirm == 1) {
      sb.append(" and kpu.IS_CONFIRM=1");
    }
    if (isCoPub != null && isCoPub == 1) {
      sb.append(" and kpu.COPUB=1");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and kpu.pub_type in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and kpu.pub_type=?");
      params.add(selectedPubType);
    }

    sb.append(" ) t where t.publish_year=? ");
    params.add(publishYear);

    // 查询数据实体
    List<Map<String, BigDecimal>> staIdList =
        this.queryForList("select pub_id from(" + sb.toString() + orderHql + ")", params.toArray());
    List<Long> pubIdList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(staIdList)) {
      for (Map<String, BigDecimal> map : staIdList) {
        pubIdList.add(map.get("PUB_ID").longValue());
      }
    }

    return pubIdList;
  }

  public Long staKpiPubCount(Integer listEi, Integer listSci, Integer listIstp, Integer listSsci, Integer startYear,
      Integer endYear, int endMonth, String selectedPubType, Long insId, String unitIds, Integer isConfirm,
      Integer isCoPub) throws DaoException {

    String countHql = "select count(pub_id) from ";

    StringBuilder sb = new StringBuilder();
    List<Object> params = new ArrayList<Object>();

    sb.append(" select * from (");
    sb.append(" select kpu.pub_type,kpu.pub_id");
    if (endMonth > 1) {
      sb.append(
          " ,case when decode(kpu.publish_month,null,1,kpu.publish_month)<? then kpu.publish_year-1 else kpu.publish_year end publish_year");
      params.add(endMonth);
    } else {
      sb.append(" ,kpu.publish_year");
    }
    sb.append(" from KPI_PUB_UNIT kpu ");
    if (insId != null) {
      sb.append(" where kpu.unit_id in(" + unitIds + ") and kpu.ins_id=? and kpu.pub_type in(3,4,5) ");
      params.add(insId);
    } else {
      sb.append(" where kpu.unit_id in (" + unitIds + ") and kpu.pub_type in(3,4,5) ");
    }

    // 引用情况
    int storeFlag = 0;
    StringBuffer storeStr = new StringBuffer();
    if (listEi != null && listEi == 1) {
      storeStr.append("kpu.LIST_EI=1");
      storeFlag = 1;
    }
    if (listSci != null && listSci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SCI=1");
      else
        storeStr.append("kpu.LIST_SCI=1");
      storeFlag = 1;
    }
    if (listIstp != null && listIstp == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_ISTP=1");
      else
        storeStr.append("kpu.LIST_ISTP=1");
      storeFlag = 1;
    }
    if (listSsci != null && listSsci == 1) {
      if (storeFlag != 0)
        storeStr.append(" or kpu.LIST_SSCI=1");
      else
        storeStr.append("kpu.LIST_SSCI=1");
      storeFlag = 1;
    }

    if (storeFlag == 1) {
      sb.append(" and(" + storeStr + ")");
    }

    if (isConfirm != null && isConfirm == 1) {
      sb.append(" and kpu.IS_CONFIRM=1");
    }
    if (isCoPub != null && isCoPub == 1) {
      sb.append(" and kpu.COPUB=1");
    }
    if (StringUtils.isNotBlank(selectedPubType) && selectedPubType.indexOf(",") > 0) {
      sb.append(" and kpu.pub_type in(" + selectedPubType + ")");
    } else if (StringUtils.isNotBlank(selectedPubType)) {
      sb.append(" and kpu.pub_type=?");
      params.add(selectedPubType);
    }

    sb.append(" ) t where t.PUBLISH_YEAR>=? and t.PUBLISH_YEAR<=? ");
    params.add(startYear);
    params.add(endYear);

    // 记录数
    Long totalCount = this.queryForLong(countHql + "(" + sb.toString() + ")", params.toArray());

    return totalCount;
  }
}
