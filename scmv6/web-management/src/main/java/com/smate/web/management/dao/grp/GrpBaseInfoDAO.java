package com.smate.web.management.dao.grp;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.management.model.grp.GrpBaseinfo;
import com.smate.web.management.model.grp.GrpManageForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组基础信息dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class GrpBaseInfoDAO extends SnsHibernateDao<GrpBaseinfo, Long> {

  /**
   * 获取项目编号
   * 
   * @param grpId
   * @return
   */
  public String getProjectNo(Long grpId) {
    String hql = "select g.projectNo from GrpBaseinfo g where g.grpId =:grpId";
    return (String) super.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
  }

  public void findGrpList(GrpManageForm form){
    String countHql = "select count(1) " ;
    StringBuilder  sb =  new StringBuilder();
    sb.append(" from GrpBaseinfo g where g.status = '01' ");
    List<Object> params = new ArrayList<>();
    ;
    if(StringUtils.isNotBlank(form.getSearchKey())){
      sb.append("and  upper(g.grpName) like ? ");// 标题
      params.add("%" + form.getSearchKey() + "%");
    }
    sb.append(" order by g.createDate desc ") ;
    Long totalCount = super.findUnique(countHql.toString() + sb.toString(), params.toArray());
    form.getPage().setTotalCount(totalCount);
    List list = super.createQuery(sb.toString(), params.toArray()).setFirstResult(form.getPage().getFirst()-1)
        .setMaxResults(form.getPage().getPageSize()).list();
    form.getPage().setResult(list);
  }
}
