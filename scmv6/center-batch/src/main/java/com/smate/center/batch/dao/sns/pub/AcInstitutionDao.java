package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcInstitution;
import com.smate.center.batch.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 单位自动提示DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository(value = "acInstitutionDao")
public class AcInstitutionDao extends SnsHibernateDao<AcInstitution, Long> {

  /**
   * 获取智能匹配国别列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */

  @SuppressWarnings("unchecked")
  public List<AcInstitution> getAcInstitution(String startWith, String excludes, int size) throws DaoException {

    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    String hql = null;
    String fetchHql = null;
    // 判断是否是非英文
    if (isChinese) {
      hql = "from AcInstitution t where lower(t.zhName) like ? ";
      fetchHql = "from AcInstitution t where lower(t.zhName) not like ?  and  lower(t.zhName) like ?  ";

    } else {
      hql = "from AcInstitution t where lower(t.enName) like ? ";
      fetchHql = "from AcInstitution t where lower(t.enName) not like ?  and  lower(t.enName) like ?  ";

    }
    hql = hql + "and t.enabled=1 ";
    fetchHql = fetchHql + "and t.enabled=1 ";
    if (StringUtils.isNotBlank(excludes) && excludes.matches(ServiceConstants.IDPATTERN)) {

      hql = hql + "and t.id not in( " + excludes + " )";
      fetchHql = fetchHql + "and t.id not in( " + excludes + " )";
    }

    Query query = super.createQuery(hql, new Object[] {startWith.trim().toLowerCase() + "%"});
    query.setMaxResults(size);

    List<AcInstitution> list = query.list();

    if (list != null && list.size() < size) { // 拼接剩余的部分

      int fetchSize = size - list.size();
      query = super.createQuery(fetchHql,
          new Object[] {startWith.trim().toLowerCase() + "%", "_%" + startWith.trim().toLowerCase() + "%"});
      query.setMaxResults(fetchSize);

      List<AcInstitution> fetchList = query.list();
      for (AcInstitution ins : fetchList) {

        list.add(ins);

      }
    }

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (AcInstitution cs : list) {

        if (isChinese) {
          cs.setName(cs.getZhName());
        } else
          cs.setName(cs.getEnName());

        if (cs.getRegionId() != null) {
          ConstRegion region = super.findUnique("from ConstRegion t where t.id = ? ", cs.getRegionId());

          if (isChinese) {
            if (region != null) {
              if (region.getSuperRegionId() == null) {
                cs.setCountry(region.getZhName());
              } else {
                // 获取国别
                ConstRegion superRegion =
                    super.findUnique("from ConstRegion t where t.id = ? ", region.getSuperRegionId());
                cs.setCountry(superRegion.getZhName());
              }
            }

          } else {
            if (region != null) {
              if (region.getSuperRegionId() == null) {
                cs.setCountry(region.getEnName());
              } else {
                // 获取国别
                ConstRegion superRegion =
                    super.findUnique("from ConstRegion t where t.id = ? ", region.getSuperRegionId());
                cs.setCountry(superRegion.getEnName());
              }
            }
          }
        }
      }
    }
    return list;
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
