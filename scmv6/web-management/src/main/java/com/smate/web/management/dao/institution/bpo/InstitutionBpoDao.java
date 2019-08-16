package com.smate.web.management.dao.institution.bpo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.institution.bpo.InstitutionBpo;

/**
 * 机构管理Dao
 * 
 * @author zjh
 * 
 */
@Repository
public class InstitutionBpoDao extends BpoHibernateDao<InstitutionBpo, Long> {
  /**
   * 分页查询单位信息
   * 
   * @param insName
   * @param insSource
   * @param page
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<InstitutionBpo> findInstitutionByPage(String insName, Integer insSource, Page<InstitutionBpo> page)
      throws DaoException {
    List<Object> params = new ArrayList<Object>();
    String countHql = "select count(ins.id) ";
    String orderHql = " order by ins.id desc";

    StringBuilder hql = new StringBuilder();
    hql.append("from InstitutionBpo ins where ins.status <> 9");
    if (StringUtils.isNotBlank(insName)) {
      hql.append(" and lower(ins.zhName) like ?");
      params.add("%" + insName.toLowerCase() + "%");
    }

    if (insSource != null && insSource != -1) {
      hql.append(" and exists (select 1 from InsSource ss where ins.id=ss.insId");
      switch (insSource) {
        case 0:
          hql.append(" and ss.onlineReg=1)");
          break;
        case 1:
          hql.append(" and ss.inviteReg=1)");
          break;
        case 2:
          hql.append(" and ss.isisLink=1)");
          break;
        case 3:
          hql.append(" and ss.isisSync=1)");
          break;
        default:
          hql.append(")");
      }
    }
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    long pageCount = totalCount % 10;
    long pageCounts = totalCount / 10;
    if (pageCount != 0) {
      pageCounts = pageCounts + 1;
    }
    page.setTotalPages(pageCounts);


    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  /**
   * 判断单位名称是否被其他单位使用
   * 
   * @param insId
   * @param insName
   * @return
   * @throws DaoException
   */
  public InstitutionBpo findInstitutionByIdAndName(Long insId, String insName) throws DaoException {
    if (insId == null) {
      String hql =
          "from InstitutionBpo t where (lower(t.zhName)=lower(?) or lower(t.enName)=lower(?)) and t.status <> 9";
      return this.findUnique(hql, StringUtils.lowerCase(insName), StringUtils.lowerCase(insName));
    } else {
      String hql =
          "from InstitutionBpo t where (lower(t.zhName)=lower(?) or lower(t.enName)=lower(?)) and t.status <> 9 and t.id<>?";
      return this.findUnique(hql, StringUtils.lowerCase(insName), StringUtils.lowerCase(insName), insId);
    }
  }

}
