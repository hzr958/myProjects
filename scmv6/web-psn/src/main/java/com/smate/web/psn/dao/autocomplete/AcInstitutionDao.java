package com.smate.web.psn.dao.autocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcInstitution;

/**
 * 单位自动提示DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class AcInstitutionDao extends SnsHibernateDao<AcInstitution, Long> {

  public List<AcInstitution> getAcInstitution(String searchKey, String exclues, Integer size, Integer nature) {
    if (StringUtils.isBlank(searchKey)) {
      return getSession().createQuery(
          "select new AcInstitution(t1.code,t1.zhName,t1.enName,t1.regionId,t1.status,t1.enabled,t2.pinYin,t2.firstLetter) "
              + "from AcInstitution t1,InsPsnCount t2 where t1.code = t2.insId")
          .setMaxResults(size).list();
    }
    Locale locale = LocaleContextHolder.getLocale();
    // 优先显示当前环境语言
    String order = locale.US.equals(locale) ? "enName" : "zhName";
    StringBuffer HQL = new StringBuffer();
    HQL.append(
        "select new AcInstitution(t1.code,t1.zhName,t1.enName,t1.regionId,t1.status,t1.enabled,t2.pinYin,t2.firstLetter) "
            + "from AcInstitution t1,InsPsnCount t2 where t1.code = t2.insId and ");
    if ("enName".equals(order)) {
      HQL.append("lower(t1.enName) like lower(:searchKey || '%') ");
    } else {
      HQL.append("(lower(t2.pinYin) like lower(:searchKey || '%') ");// 利用字符串拼接,否则hibernate识别不了参数
      HQL.append("or lower(t2.firstLetter) like lower(:searchKey || '%') ");
      HQL.append("or instr(lower(t1.zhName),lower(:searchKey))>0) ");
    }
    if (nature != null && nature > 0 && nature < 3) {
      HQL.append("and t1.nature=:nature ");
    } else {
      HQL.append("and t1.nature <> 1 and t1.nature <> 2 ");
    }
    if (StringUtils.isNotBlank(exclues) && exclues.matches(ServiceConstants.IDPATTERN)) {
      HQL.append("and t1.insId not in (:exclues) ");
    }
    /**
     * 先按nature(性质)升序排序,再将完全匹配的放在第一位,以参数开头的放在第二位,以参数结尾的放在第三位,参数在数据中间的排在第四位
     */
    HQL.append("order by t1.nature asc");
    // if ("enName".equals(order)) {
    // HQL.append(",nvl(t1.enName,t2.pinYin) asc");
    // } else {
    // HQL.append(",NLSSORT (nvl(t1.zhName,t1.enName),'NLS_SORT = SCHINESE_PINYIN_M' ) asc");
    // }
    // if (nature != null && nature == 2) {
    // HQL.append(",t2.historyPsnCount desc");
    // }
    if ("enName".equals(order)) {
      HQL.append(",(case when t1.enName = :searchKey then 1 when t1.enName like :searchKey || '%' then 2 "
          + "when t1.enName like '%' || :searchKey then 3 "
          + "when t1.enName like '%' || :searchKey || '%' then 4 else 0 end)");
    } else {
      HQL.append(",(case when t2.pinYin = :searchKey then 1 when t2.pinYin like :searchKey || '%' then 2 "
          + "when t2.pinYin like '%' || :searchKey then 3 "
          + "when t2.pinYin like '%' || :searchKey || '%' then 4 else 0 end),"
          + "(case when t2.firstLetter = :searchKey then 1 when t2.firstLetter like :searchKey || '%' then 2 "
          + "when t2.firstLetter like '%' || :searchKey then 3 "
          + "when t2.firstLetter like '%' || :searchKey || '%' then 4 else 0 end),"
          + "(case when t1.zhName = :searchKey then 1 when t1.zhName like :searchKey || '%' then 2 "
          + "when t1.zhName like '%' || :searchKey then 3 "
          + "when t1.zhName like '%' || :searchKey || '%' then 4 else 0 end)");
    }
    Query query = getSession().createQuery(HQL.toString());
    query.setParameter("searchKey", searchKey);
    if (nature != null && nature > 0 && nature < 3) {
      query.setParameter("nature", nature);
    }
    query.setMaxResults(size);
    if (StringUtils.isNotEmpty(exclues) && exclues.matches(ServiceConstants.IDPATTERN)) {
      query.setParameter("exclues", exclues);
    }
    return query.list();
  }

  /**
   * 获取指定单位的自动提示信息.
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("static-access")
  public AcInstitution getAcInstitution(Long insId) throws DaoException {

    AcInstitution ins = super.findUnique("from AcInstitution t where t.code = ? ", new Object[] {insId});
    Locale locale = LocaleContextHolder.getLocale();
    if (ins != null) {
      if (locale.equals(locale.US)) {
        ins.setName(ins.getEnName());
      } else {
        ins.setName(ins.getZhName());
      }
    }
    return ins;

  }

  /**
   * 获取单位信息.
   * 
   * @param name
   * @return
   */
  public AcInstitution getByName(String name) {
    String hql = "from AcInstitution t where t.zhName = ? or t.enName = ? ";
    List<AcInstitution> list = super.find(hql, name, name);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取智能匹配国别列表，只读size条记录.
   * 
   * @param startWith
   * @param prvId
   * @param ignorePriv 忽略权限
   * @return
   * @throws Exception
   */

  @SuppressWarnings("unchecked")
  public List<AcInstitution> getStaAcInstitution(String startWith, Long prvId, Long cyId, Long disId, int ignorePriv,
      int size) throws DaoException {

    String hql = null;
    List<Object> param = new ArrayList<Object>();
    if (ignorePriv > 0) { // 报表系统忽略权限，因此只要单位开通报表，就能对比
      hql = "select new AcInstitution(ins.code, ins.zhName, ins.enName, ins.regionId, ins.status, ins.enabled, 1)"
          + " from AcInstitution ins, StaInsSetting sins"
          + " where (lower(ins.zhName) like ? or lower(ins.enName) like ?) and sins.insId=ins.code and sins.statOpen = 1";
    } else {
      hql =
          "select new AcInstitution(ins.code, ins.zhName, ins.enName, ins.regionId, ins.status, ins.enabled, sins.contOpen)"
              + " from AcInstitution ins, StaInsSetting sins"
              + " where (lower(ins.zhName) like ? or lower(ins.enName) like ?) and sins.insId=ins.code and sins.statOpen = 1 and sins.contOpen = 1";
    }
    startWith = startWith.trim().toLowerCase();
    param.add(startWith + "%");
    param.add(startWith + "%");
    if (disId != null) {
      hql += " and exists(select t1.insId from InsRegion t1 where ins.code = t1.insId and t1.disId = ? )";
      param.add(disId);
    } else if (cyId != null) {
      hql += " and exists(select t1.insId from InsRegion t1 where ins.code = t1.insId and t1.cyId = ? )";
      param.add(cyId);
    } else {
      // 所在省份
      if (prvId != null) {
        hql += " and exists(select t1.insId from InsRegion t1 where ins.code = t1.insId and t1.prvId = ? )";
        param.add(prvId);
      }
    }
    Query query = super.createQuery(hql, param.toArray());
    query.setMaxResults(size);
    List<AcInstitution> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
      for (AcInstitution cs : list) {
        if (isChinese) {
          cs.setName(StringUtils.startsWith(StringUtils.lowerCase(cs.getZhName()), startWith) ? cs.getZhName()
              : cs.getEnName());
        } else {
          cs.setName(StringUtils.startsWith(StringUtils.lowerCase(cs.getEnName()), startWith) ? cs.getEnName()
              : cs.getZhName());
        }
      }
    }
    return list;
  }
}
