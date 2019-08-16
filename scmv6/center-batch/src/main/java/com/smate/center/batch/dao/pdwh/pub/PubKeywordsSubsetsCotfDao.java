package com.smate.center.batch.dao.pdwh.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetsCotf;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubKeywordsSubsetsCotfDao extends PdwhHibernateDao<PubKeywordsSubsetsCotf, Long> {

  @SuppressWarnings("unchecked")
  /*
   * public List<String> getPubKeywordsSubsetsCotfByContentMd5(List<String> md5List) {
   * if(md5List.size()>999) { md5List = md5List.subList(0, 999); } String hql =
   * "select t.discode from PubKeywordsSubsetsCotf t where t.contentMd5Code in (:md5List) " +
   * "order by t.size desc, t.counts desc"; return super.createQuery(hql).setParameterList("md5List",
   * md5List).list(); }
   */
  public List<String> getPubKeywordsSubsetsCotfByContentMd5(List<String> md5List) {
    if (md5List != null && md5List.size() > 0) {
      if (md5List.size() <= 1000) {
        String hql =
            "select t.discode from PubKeywordsSubsetsCotf t where t.contentMd5Code in (:md5List) order by t.size desc, t.counts desc";
        return super.createQuery(hql).setParameterList("md5List", md5List).list();
      } else if (md5List.size() > 1000) {
        Integer times = md5List.size() / 1000;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String hql1 = "select t.discode from PubKeywordsSubsetsCotf t where t.contentMd5Code in (:md5List0)";
        List<String> md5List0 = new ArrayList<String>(md5List.subList(0, 1000));
        parameterMap.put("md5List0", md5List0);
        for (int i = 1; i <= times; i++) {
          hql1 = hql1 + " or t.contentMd5Code in (:md5List" + i + ")";
          if (i < times) {
            List<String> valueList = new ArrayList<String>(md5List0.subList(i * 1000, i * 1000 + 1000));
            parameterMap.put("contentMd5Code" + i, valueList);
          } else if (i == times) {
            List<String> valueList = new ArrayList<String>(md5List0.subList(i * 1000, md5List0.size()));
            parameterMap.put("contentMd5Code" + i, valueList);
          }
        }
        hql1 = hql1 + " order by t.size desc, t.counts desc";
        return super.createQuery(hql1).setProperties(parameterMap).list();
      }
    }
    return null;
  }
}
