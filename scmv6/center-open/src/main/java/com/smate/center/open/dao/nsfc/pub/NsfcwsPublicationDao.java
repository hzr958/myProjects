package com.smate.center.open.dao.nsfc.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * @author tsz
 * 
 */
@Repository
public class NsfcwsPublicationDao extends HibernateDao<NsfcwsPublication, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 分页查询指定Google人员的成果数据.
   * 
   * @param psnId
   * @param keywords
   * @param uuid
   * @param sortType
   * @param page
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public Page<NsfcwsPublication> queryPsnPubByPage(Long psnId, String keywords, List<Long> ids, String sortType,
      Page<NsfcwsPublication> page) throws Exception {
    StringBuffer sb = new StringBuffer("from NsfcwsPublication t where t.status=? and t.psnId=?");
    List<Object> params = new ArrayList<Object>();
    params.add(0);
    params.add(psnId);

    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like ? or lower(t.enTitle) like ?)");
      params.add("%" + keywords.toLowerCase() + "%");
      params.add("%" + keywords.toLowerCase() + "%");
    }
    if (CollectionUtils.isNotEmpty(ids)) {
      sb.append(" and t.id not in(:list)");
    }
    Query countQuery = super.createQuery("select count(t.id) " + sb.toString(), params.toArray());
    if (CollectionUtils.isNotEmpty(ids)) {
      countQuery.setParameterList("list", ids);
    }
    Long count = (Long) countQuery.uniqueResult();
    page.setTotalCount(count);

    if ("updateTime".equals(sortType)) {
      sb.append(" order by t.createDate desc, t.id desc");
    } else {
      sb.append(" order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.id desc");
    }
    Query resultQuery =
        super.createQuery(sb.toString()).setParameters(params.toArray(), super.findTypes(params.toArray()))
            .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize());
    if (CollectionUtils.isNotEmpty(ids)) {
      resultQuery.setParameterList("list", ids);
    }
    List<NsfcwsPublication> list = resultQuery.list();
    page.setResult(list);
    return page;
  }


  /**
   * 获取Google人员成果总数.
   * 
   * @param psnId
   * @param keywords
   * @param uuid
   * @return
   * @throws Exception
   */
  public Long queryPsnPubCount(Long psnId, String keywords, String uuid) throws Exception {
    StringBuffer sb = new StringBuffer("select count(t.id) from NsfcwsPublication t where t.status=? and t.psnId=?");
    List<Object> params = new ArrayList<Object>();
    params.add(0);
    params.add(psnId);
    if (uuid != null) {
      sb.append(" and t.id not in(select t2.pubId from IrisExcludedPub t2 where t2.uuid=?)");
      params.add(uuid);
    }
    if (StringUtils.isNotBlank(keywords)) {
      sb.append(" and (lower(t.zhTitle) like ? or lower(t.enTitle) like ?)");
      params.add("%" + keywords.toLowerCase() + "%");
      params.add("%" + keywords.toLowerCase() + "%");
    }
    return super.findUnique(sb.toString(), params.toArray());
  }

}
