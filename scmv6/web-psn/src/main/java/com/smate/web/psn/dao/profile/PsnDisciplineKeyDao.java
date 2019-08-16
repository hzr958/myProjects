package com.smate.web.psn.dao.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.action.search.PersonSearchForm;

/**
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PsnDisciplineKeyDao extends SnsHibernateDao<PsnDisciplineKey, Long> {

  public Integer saveKeys(String keyWords, Long psnId, Integer permission) {

    String hql = "from PsnDisciplineKey t where t.psnId = ? and t.keyWords = ?";
    PsnDisciplineKey pdk = (PsnDisciplineKey) super.createQuery(hql, psnId, keyWords).uniqueResult();
    if (pdk == null) {
      pdk = new PsnDisciplineKey(keyWords, psnId, 1, new Date());
    } else if (pdk.getStatus() == 0) {// 关键词已经存在，但是无效，则将该关键词状态设置为有效，同时将更新时间进行修改
      pdk.setStatus(1);
      pdk.setUpdateDate(new Date());
    }
    this.save(pdk);
    return 1;
  }

  public void updateKwStatusByPsnId(Long psnId, Integer status) {
    String hql = "update PsnDisciplineKey t set t.status= ? where t.psnId=? ";
    super.createQuery(hql, status, psnId).executeUpdate();
  }

  /**
   * 更新指定人员的关键词，关键词不在指定的关键词列表的状态
   * 
   * @param psnId
   * @param status
   * @param kwList
   */
  public void updateKwStatusNotInKwList(Long psnId, Integer status, List<String> kwList) {
    String hql = "update PsnDisciplineKey t set t.status=:status where t.psnId=:psnId and t.keyWords not in (:kwList)";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status)
        .setParameterList("kwList", kwList).executeUpdate();
  }

  /**
   * 查找psnId的有效关键词
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPsnDiscKeyId(Long psnId, Integer status) {

    String hql = "select t.id from PsnDisciplineKey t where t.psnId=? and t.status=?";
    return super.createQuery(hql, psnId, status).list();

  }

  public List<PsnDisciplineKey> findPsnDisciplineKey(Long psnId, List<Long> idList, Integer status) {
    StringBuilder hql = null;
    Map<String, Object> values = new HashMap<String, Object>();
    values.put("psnId", psnId);

    if (CollectionUtils.isNotEmpty(idList)) {
      hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = :psnId and t.id in(:idList) and t.status=1");
      values.put("idList", idList);
    } else {
      hql = new StringBuilder("from PsnDisciplineKey t where t.psnId = :psnId and t.status=1");
    }

    values.put("status", status);
    hql.append(" and t.status=:status order by t.id");

    return super.find(hql.toString(), values);
  }

  /**
   * 通过人员姓名及关键词检索
   *
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> findPsnIdsForSearch(PersonSearchForm form) {

    if (StringUtils.isBlank(form.getSearchKey()) && StringUtils.isBlank(form.getSearchName())) {
      return null;
    }
    List<Object> pramsList = new ArrayList<Object>();
    StringBuffer sb = new StringBuffer();
    sb.append(" select  ps2.psn_id from ( ");
    sb.append(" select distinct ps.psn_id from person ps ");
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      sb.append(" left join psn_discipline_key pk on ps.psn_id=pk.psn_id ");
    }
    sb.append(" where not exists(select pp.psn_id from psn_private pp where pp.psn_id =ps.psn_id) ");
    if (StringUtils.isNotBlank(form.getSearchName())) {
      sb.append(" and ( lower(ps.name)= ? or lower(ps.first_name)=? or lower(ps.last_name) =? ");
      sb.append(" or lower(ps.first_name||' '||ps.last_name) =? or  lower(ps.last_name||' '||ps.first_name) =? )");
      pramsList.add(form.getSearchName().trim().toLowerCase());
      pramsList.add(form.getSearchName().trim().toLowerCase());
      pramsList.add(form.getSearchName().trim().toLowerCase());
      pramsList.add(form.getSearchName().trim().toLowerCase());
      pramsList.add(form.getSearchName().trim().toLowerCase());
    }
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      sb.append(" and lower(pk.key_words) = ? and pk.status=1 ");
      pramsList.add(form.getSearchKey().trim().toLowerCase());
    }
    sb.append(" ) ps2 ");
    sb.append(
        " left join psn_statistics pss on ps2.psn_id=pss.psn_id  order by nvl(pss.pub_sum,0) desc,ps2.psn_id desc ");
    List<Map> queryList = super.queryForList(sb.toString(), pramsList.toArray(), form.getPageSize(),
        (form.getPageNo() - 1) * form.getPageSize());
    if (CollectionUtils.isNotEmpty(queryList)) {
      List<Long> resultList = new ArrayList<Long>();
      for (Map map : queryList) {
        resultList.add(map.get("PSN_ID") != null ? Long.valueOf(map.get("PSN_ID").toString()) : 0L);
      }
      return resultList;
    }
    return null;
  }

  /**
   * 查找人员关键词List
   * 
   * @param psnId
   * @param status
   * @return
   */
  public List<PsnDisciplineKey> findPsnDisciplineKeyByPsnId(Long psnId, Integer status) {
    String hql =
        "select new PsnDisciplineKey(id, keyWords, psnId) from PsnDisciplineKey t where t.psnId=:psnId and t.status=:status order by updateDate asc,id asc";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("status", status).list();
  }

  /**
   * 统计人员有效关键词数量
   * 
   * @param psnId
   * @param status
   * @return
   */
  public Long countPsnDisciplineKey(Long psnId) {
    String hql = "select count(1) from PsnDisciplineKey t where t.psnId=:psnId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public String getPsnKeywordsStr(Long psnId) {
    String hql = "select t.keyWords from PsnDisciplineKey t where t.psnId=:psnId and t.status=1";
    List<String> kws = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (kws == null || kws.size() == 0) {
      return null;
    } else {
      StringBuffer sb = new StringBuffer();
      for (String kw : kws) {
        sb.append(String.valueOf(kw));
        sb.append(" ");
      }
      return sb.toString().trim();
    }

  }

  /**
   * 更新指定ID的关键词状态
   * 
   * @param ids
   * @param status
   */
  public void updateKwStatusById(List<Long> ids, Integer status) {
    String hql = "update PsnDisciplineKey t set t.status= :status where t.id in (:ids) ";
    super.createQuery(hql).setParameter("status", status).setParameterList("ids", ids).executeUpdate();
  }
}
