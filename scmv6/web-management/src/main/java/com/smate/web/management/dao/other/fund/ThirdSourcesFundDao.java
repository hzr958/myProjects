package com.smate.web.management.dao.other.fund;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.other.fund.ThirdSourcesFund;



/**
 * 第三方，基金机会Dao
 */
@Repository
public class ThirdSourcesFundDao extends SnsHibernateDao<ThirdSourcesFund, Long> {
  public List<ThirdSourcesFund> findFundThirdList(ThirdSourcesFund form, Page<ThirdSourcesFund> page) {
    String count = "select count(1) ";
    String hql = "from ThirdSourcesFund t where t.auditStatus=0 ";
    String order = "order by t.id desc";
    StringBuffer sb = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      sb.append(" and (instr(upper(fundTitleCn),?)>0 or instr(upper(fundTitleEn),?)>0) ");// 中文标题
      String searchKey = HtmlUtils.htmlEscape(form.getSearchKey());
      searchKey = searchKey.toUpperCase().trim();
      params.add(searchKey);
      params.add(searchKey);
    }
    Long totalCount = super.findUnique(count + hql + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    List<ThirdSourcesFund> fundList = super.createQuery(hql + sb.toString() + order, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(fundList);
    return fundList;
  }
}
