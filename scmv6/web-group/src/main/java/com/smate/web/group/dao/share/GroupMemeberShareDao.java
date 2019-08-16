package com.smate.web.group.dao.share;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hibernate.SQLQuery;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.form.GroupShareForm;
import com.smate.web.group.model.grp.member.GrpMember;

@Repository(value = "groupMemeberShareDao")
public class GroupMemeberShareDao extends SnsHibernateDao<GrpMember, Long> {
  /**
   * 根据人员psnId 和 grpId获取群组下最合适的推荐分享人员 推荐原则是： 1.优先群组成员与关注人员的交集 2.优先群组成员 3.优先关注人员 需要作用grp_member（群组成员表）
   * and att_person（关注表） 两个表 排序的实现：关注表中要关联最近访问表recent_selected实现 群组成员表根据最后访问时间进行排序
   * 不使用下面的sql的原因在于union语句无法实现每一个子句的排序 String sql = "select a.ref_psn_id from att_person a where
   * exists (select 1 from v_grp_member t where a.ref_psn_id = t.psn_id and t.grp_id =:grpId and
   * a.psn_id =:psnId)" + " union select t.psn_id from v_grp_member t where t.grp_id =:grpId and
   * t.psn_id <>:psnId " + " union select a.ref_psn_id from att_person a where a.psn_id =:psnId";
   * 
   * @param psnId
   * @param grpId
   * @return 最合适的推荐分享人员的list
   */
  public List<Object> getRecommendPsnIds(Long psnId, Long grpId) {
    SQLQuery query = null;
    String sql = "";
    List<Object> recommends = new ArrayList<Object>();
    /**
     * 最近三天联系人
     */
    sql = "select r.selected_psn_id from recent_selected r where  ROUND(TO_NUMBER(sysdate-r.SELECTED_DATE))<=3"
        + " and r.psn_id=:psnId  order by r.selected_date desc";
    query = this.getSession().createSQLQuery(sql);
    recommends = (List<Object>) query.setParameter("psnId", psnId).list();
    String hql = "from GrpMember t where t.psnId=:psnId and t.grpId=:grpId and t.status = '01'";
    GrpMember grpMember = (GrpMember) getSession().createQuery(hql).setParameter("grpId", grpId)
        .setParameter("psnId", psnId).uniqueResult();
    if (grpMember != null) {
      /**
       * 群组成员
       */
      sql = "select t.psn_id from v_grp_member t where t.grp_id =:grpId and t.psn_id <>:psnId and t.status = '01' "
          + "order by t.last_visit_date desc";
      query = this.getSession().createSQLQuery(sql);
      recommends.addAll((List<Object>) query.setParameter("grpId", grpId).setParameter("psnId", psnId).list());
    }
    /**
     * 好友
     */
    sql = "select f.friend_psn_id from psn_friend f where f.psn_id =:psnId" + " order by f.create_date desc";
    query = this.getSession().createSQLQuery(sql);
    recommends.addAll((List<Object>) query.setParameter("psnId", psnId).list());
    /**
     * 关注人员
     */
    sql = "select a.ref_psn_id from att_person a where a.psn_id =:psnId";
    query = this.getSession().createSQLQuery(sql);
    recommends.addAll((List<Object>) query.setParameter("psnId", psnId).list());
    // 数组去重处理
    return GroupMemeberShareDao.removeDuplicationForList(recommends);
  }

  /**
   * 群组人员的显示 检索 排序
   * 
   * @param from
   * @return
   */
  public List<Object> getSearchPsnIds(GroupShareForm from) {
    Long data = from.getGroupId();
    StringBuffer hql = new StringBuffer();
    Locale locale = LocaleContextHolder.getLocale();
    if (from.getSearchKey() != null && !from.getSearchKey().equals("")) {
      // 针对于有索引关键字的情况
      if (from.getOrderBy().equals("date")) {
        // 按时间排序
        hql.append("select t.psn_id from v_grp_member t where t.grp_id =:data "
            + "and exists (select 1 from person p where (upper(p.name) like:searchkey or upper(p.ename) like:searchkey or upper(p.first_name||p.last_name) like:searchkey) "
            + "and p.psn_id = t.psn_id and t.psn_id <>:psnId and t.status = '01' ) "
            + "order by t.last_visit_date desc");
      } else if (from.getOrderBy().equals("name")) {
        // 姓名排序
        hql.append("select p.psn_id from person p where "
            + "exists (select 1 from v_grp_member t where t.grp_id =:data and p.psn_id = t.psn_id and t.psn_id <>:psnId and t.status = '01' "
            + "and exists (select 1 from person t1 where (upper(t1.name) like:searchkey or upper(t1.ename) like:searchkey or upper(t1.first_name||t1.last_name) like:searchkey) and t.psn_id = t1.psn_id)) ");
        if (Locale.US.equals(locale)) {
          // 英文
          hql.append(
              "order by  NLSSORT  ( nvl(nvl(p.ename,p.first_Name||p.last_Name),p.name) , 'NLS_SORT = SCHINESE_PINYIN_M' ) asc  nulls last");
        } else {
          // 中文
          hql.append(
              "order by  NLSSORT  ( nvl(nvl(p.name,p.first_Name||p.last_Name),p.ename) , 'NLS_SORT = SCHINESE_PINYIN_M' ) asc nulls last");
        }
      }
      return this.getSession().createSQLQuery(hql.toString())
          .setParameter("searchkey", "%" + from.getSearchKey().toUpperCase().trim() + "%")
          .setParameter("psnId", from.getPsnId()).setParameter("data", data).list();
    } else {
      // 索引值为空时
      if (from.getOrderBy().equals("date")) {
        // 按时间排序
        hql.append(
            "select t.psn_id from v_grp_member t where t.grp_id =:data and t.psn_id <>:psnId and t.status = '01'  order by t.last_visit_date desc");
      } else if (from.getOrderBy().equals("name")) {
        // 姓名排序
        hql.append("select p.psn_id from person p where "
            + "exists (select 1 from v_grp_member t where t.grp_id =:data and p.psn_id = t.psn_id  and t.psn_id <>:psnId and t.status = '01' ) ");
        if (Locale.US.equals(locale)) {
          // 英文
          hql.append(
              "order by   NLSSORT  ( nvl(nvl(p.ename,p.first_Name||p.last_Name),p.name) , 'NLS_SORT = SCHINESE_PINYIN_M' ) asc  nulls last");
        } else {
          // 中文
          hql.append(
              "order by   NLSSORT   ( nvl(nvl(p.name,p.first_Name||p.last_Name),p.ename) , 'NLS_SORT = SCHINESE_PINYIN_M' ) asc nulls last");
        }
      }
      return this.getSession().createSQLQuery(hql.toString()).setParameter("psnId", from.getPsnId())
          .setParameter("data", data).list();
    }
  }

  /**
   * 数组去重
   * 
   * @param val
   * @return
   */
  public static List<Object> removeDuplicationForList(List<Object> val) {
    List<Object> list = new ArrayList<Object>();
    for (int i = 0; i < val.size(); i++) {
      if (!list.contains(val.get(i))) {
        list.add(val.get(i));
      }
    }
    return list;
  }
}
