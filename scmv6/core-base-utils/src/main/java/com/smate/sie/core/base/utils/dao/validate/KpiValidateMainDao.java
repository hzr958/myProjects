package com.smate.sie.core.base.utils.dao.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMain;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMainUpload;

/**
 * 科研验证主表 Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiValidateMainDao extends SieHibernateDao<KpiValidateMain, String> {

  public Long countNeedHandleKeyId() {
    String hql = "select count(uuId) from KpiValidateMain where status=0";
    return findUnique(hql);
  }

  @SuppressWarnings("unchecked")
  public List<KpiValidateMain> loadNeedHandleKeyId(int maxSize) {
    // String hql = "from KpiValidateMain k where k.status = 0 order by k.smDate desc";
    String hql = "from KpiValidateMain k where k.status = 0 ";// order by k.priority asc nulls last
    Query queryResult = super.createQuery(hql);
    queryResult.setMaxResults(maxSize);
    return queryResult.list();
  }

  /**
   * 根据 key_type+key_code+version_no 查重，则否 key_type+key_code 查重。
   * 
   * @param keyCode
   * @param verNo
   * @param keyType
   * @return
   */

  @SuppressWarnings("unchecked")
  public List<KpiValidateMain> getByKeyCodeAndType(String keyCode, String verNo, String keyType, String datafrom) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer("from KpiValidateMain k where k.keyCode= ? and k.keyType= ? and k.dataFrom=?");
    params.add(keyCode);
    params.add(keyType);
    params.add(datafrom);
    if (StringUtils.isNotBlank(verNo)) {
      hql.append(" and k.versionNo= ?");
      params.add(verNo);
    }
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    return queryResult.list();
  }

  public Integer getContKeyCodeAndType(String keyCode, String verNo, String keyType, String datafrom) {
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer(
        "select count(k.uuId) from KpiValidateMain k where k.keyCode= ?  and k.keyType= ? and k.dataFrom=?");
    params.add(keyCode.trim());
    params.add(keyType.trim());
    params.add(datafrom.trim());
    if (StringUtils.isNotBlank(verNo)) {
      hql.append(" and k.versionNo= ?");
      params.add(verNo.trim());
    }
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    return ((Long) queryResult.uniqueResult()).intValue();
  }



  // 左侧申请年份
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getYearList(Long userId) {
    List<Map<String, Object>> yesrList = new ArrayList<Map<String, Object>>();
    String hql =
        "select new Map(nvl(p.prpYear,99) as itemName,p.prpYear as itemId) from KpiValidateMainUpload up,KpiValidateMain p where up.uuId = p.uuId and up.isDel=0 and up.psnId=? and p.prpYear is not null group by p.prpYear order by itemId desc";
    List<Object> params = new ArrayList<Object>();
    params.add(userId);
    Query query = super.createQuery(hql, params.toArray());
    yesrList = query.list();
    return yesrList;
  }

  // 左侧查询验证类型(目前没常量表，写死) 左侧查询以去掉改条件
  @Deprecated
  public List<Map<String, String>> getTypeList() {
    List<Map<String, String>> pubList = new ArrayList<Map<String, String>>();
    Map<String, String> map1 = new HashMap<String, String>();
    map1.put("itemId", "1");
    map1.put("itemName", "项目人员");
    pubList.add(map1);
    Map<String, String> map2 = new HashMap<String, String>();
    map2.put("itemId", "2");
    map2.put("itemName", "项目单位");
    pubList.add(map2);
    Map<String, String> map3 = new HashMap<String, String>();
    map3.put("itemId", "3");
    map3.put("itemName", "项目成果");
    pubList.add(map3);
    Map<String, String> map4 = new HashMap<String, String>();
    map4.put("itemId", "4");
    map4.put("itemName", "人员成果");
    pubList.add(map4);
    return pubList;
  }


  // 类型 对应 kpi_validate_main.key_type
  public List<Map<String, String>> getKeyTypeList() {
    List<Map<String, String>> pubList = new ArrayList<Map<String, String>>();
    Map<String, String> map1 = new HashMap<String, String>();
    map1.put("itemId", "1");
    map1.put("itemName", "申请书");
    pubList.add(map1);
    Map<String, String> map2 = new HashMap<String, String>();
    map2.put("itemId", "2");
    map2.put("itemName", "进展报告");
    pubList.add(map2);
    Map<String, String> map3 = new HashMap<String, String>();
    map3.put("itemId", "3");
    map3.put("itemName", "结题报告");
    pubList.add(map3);
    return pubList;
  }

  @SuppressWarnings({"unchecked"})
  public void queryKpiValidateList(Long userId, String searchKey, String typeId, String PrpYear,
      Page<KpiValidateMainUpload> page) {
    String listHql = "select new KpiValidateMainUpload(up.id, up.psnId,up.submitTime,up.fileId,up.uuId)";
    String countHql = "select count(up.uuId) ";
    String orderHql = " order by up.submitTime desc ";
    StringBuffer hql = new StringBuffer(" from KpiValidateMainUpload up,KpiValidateMain t ");
    hql.append("where up.isDel=0 and up.psnId = ? and t.uuId = up.uuId ");
    String hql2 = "", hql3 = "";
    List<Object> params = new ArrayList<Object>();
    params.add(userId);
    if (StringUtils.isNotEmpty(searchKey)) {
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey).toUpperCase().trim();
      hql.append(" and ");
      hql.append("( ");
      hql.append("instr(upper(t.title),?)>0");// 标题
      params.add(searchKey);
      hql.append(" ) ");
    }
    // 问题类型
    if (StringUtils.isNotBlank(typeId)) {
      hql.append(" and t.keyType in(" + typeId + ")");
    }
    // 申请年份
    if (StringUtils.isNotEmpty(PrpYear)) {
      if ("99".equals(PrpYear)) {
        hql.append(" and t.prpYear is null ");
      } else {
        hql.append(" and t.prpYear in(" + PrpYear + ") ");
      }
    }
    if (StringUtils.isNotBlank(hql2)) {
      hql3 = hql2;
    } else {
      hql3 = hql.toString();
    }
    Query queryCount = super.createQuery(countHql + hql3, params.toArray());
    Query query = super.createQuery(listHql + hql3 + orderHql, params.toArray());
    Long totalCount = (Long) queryCount.uniqueResult();
    page.setTotalCount(totalCount);
    if (totalCount <= 0) {
      return;
    }
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    page.setResult(query.list());
  }



  @SuppressWarnings({"unchecked"})
  public List<Map<String, Object>> getValidateCount(Long userId, String searchKey, String typeId, String PrpYear,
      int type) throws SysServiceException {
    String typeCount = " select new Map(nvl(t.keyType,99) as typeId,count(up.uuId) as count) ";
    String prpYearCount = " select new Map(nvl(t.prpYear,99) as prpYear,count(up.uuId) as count) ";
    List<Object> params = new ArrayList<Object>();
    StringBuilder hql = new StringBuilder();
    hql.append(" from KpiValidateMain t,KpiValidateMainUpload up ");
    hql.append(" where up.isDel=0 and up.psnId = ? and t.uuId = up.uuId ");
    params.add(userId);

    if (StringUtils.isNotEmpty(searchKey)) {
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey).toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(t.title),?)>0");// 标题
      params.add(searchKey);
      hql.append(" ) ");
    }
    if (StringUtils.isNotEmpty(typeId) && type != 1) {
      hql.append(" and t.keyType in(" + typeId + ") ");
    }
    if (StringUtils.isNotEmpty(PrpYear) && type != 2) {
      if ("99".equals(PrpYear)) {
        hql.append(" and t.prpYear is null ");
      } else {
        hql.append(" and t.prpYear in(" + PrpYear + ") ");
      }
    }
    if (type == 1) {// 问题类型统计数
      hql.append(" group by nvl(t.keyType,99) ");
      return this.createQuery(typeCount + hql.toString(), params.toArray()).list();
    } else if (type == 2) {// 申请年份统计数
      hql.append(" group by nvl(t.prpYear,99) ");
      return this.createQuery(prpYearCount + hql.toString(), params.toArray()).list();
    }
    return null;
  }

  public int getValidateCountByPsnId(Long userId) {
    String hql = "select count(1) from KpiValidateMainUpload t where t.psnId = ?";
    Long count = super.findUnique(hql, userId);
    if (count > 0) {
      return count.intValue();
    }
    return 0;
  }
}
