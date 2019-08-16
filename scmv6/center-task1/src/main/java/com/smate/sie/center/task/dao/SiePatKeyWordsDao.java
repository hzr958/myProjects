package com.smate.sie.center.task.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PatKeyWords;

/**
 * 专利关键词拆分表.
 * 
 * @author liqinghua
 */
@Repository
public class SiePatKeyWordsDao extends SieHibernateDao<PatKeyWords, Long> {

  /**
   * 保存专利关键词.
   * 
   * @param patId
   * @param psnId
   * @param keywords
   */
  public void savePatKeywords(Long patId, Long insId, List<String> keywords) {
    String hql = "delete from PatKeyWords t where t.patId = ? ";
    super.createQuery(hql, patId).executeUpdate();
    if (CollectionUtils.isEmpty(keywords)) {
      return;
    }
    // 用于查重
    List<String> lkwList = new ArrayList<String>();
    for (String kw : keywords) {
      String lkw = kw.trim().toLowerCase();
      if (StringUtils.isBlank(lkw) || lkwList.contains(lkw)) {
        continue;
      }
      PatKeyWords patKw = new PatKeyWords(patId, insId, kw, lkw);
      super.save(patKw);
    }
  }

  /**
   * 删除专利关键词.
   * 
   * @param patId
   */
  public void delPatKeywords(Long patId) {
    String hql = "delete from PatKeyWords t where t.patId = ? ";
    super.createQuery(hql, patId).executeUpdate();
  }

  /**
   * 获取专利关键词列表.
   * 
   * @param patId
   * @return
   */
  public List<PatKeyWords> getPatKeyWords(Long patId) {

    String hql = "from PatKeyWords t where t.patId = ? ";
    return super.createQuery(hql, patId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PatKeyWords> getListByInsId(Long insId) {
    String hql = " from PatKeyWords t where t.insId= ? order by t.id ";
    return super.createQuery(hql, insId).list();
  }
}
