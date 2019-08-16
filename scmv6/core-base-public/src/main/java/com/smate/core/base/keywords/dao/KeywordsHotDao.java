package com.smate.core.base.keywords.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 热词Dao
 * 
 * @author zk
 *
 */
@Repository
public class KeywordsHotDao extends SnsHibernateDao<KeywordsHot, Long> {

  /**
   * 批量获取kwtxt
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findKwtxt(Integer pageNo, Integer pageSize) {
    String hql = "select kh.kwTxt from KeywordsHot kh order by kh.id asc ";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 查找热词关键词.
   * 
   * @param keywords
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KeywordsHot> getKeywordsHot(List<String> kwtxtList) {
    if (CollectionUtils.isEmpty(kwtxtList)) {
      return null;
    }
    Collection<Collection<String>> container = ServiceUtil.splitStrList(kwtxtList, 40);
    List<KeywordsHot> listResult = new ArrayList<KeywordsHot>();
    String hql = "from KeywordsHot t where t.kwTxt in(:kwTxt) or t.ekwTxt in(:ekwTxt) ";
    for (Collection<String> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("kwTxt", item).setParameterList("ekwTxt", item).list());
    }
    return listResult;
  }

  /**
   * 根据关键词ID获取热词列表.
   * 
   * @param kwIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KeywordsHot> loadKeywordsHotByIds(List<Long> kwIds) {

    if (CollectionUtils.isEmpty(kwIds)) {
      return null;
    }
    Collection<Collection<Long>> container = ServiceUtil.splitList(kwIds, 40);
    List<KeywordsHot> listResult = new ArrayList<KeywordsHot>();
    String hql = "from KeywordsHot t where t.id in(:ids)";
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("ids", item).list());
    }
    return listResult;
  }
}
