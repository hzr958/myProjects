package com.smate.center.batch.dao.pdwh.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetsCotfHnt;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubKeywordsSubsetsCotfHntDao extends PdwhHibernateDao<PubKeywordsSubsetsCotfHnt, Long> {

  @SuppressWarnings("unchecked")
  public List<String> getPubKeywordsSubsetsCotfHntByContentHash(List<Long> contentHashValueList) {
    if (contentHashValueList != null && contentHashValueList.size() > 0) {
      if (contentHashValueList.size() <= 1000) {
        String hql =
            "select t.discode from PubKeywordsSubsetsCotfHnt t where t.contentHashValue in (:contentHashValueList) order by t.size desc, t.counts desc";
        return super.createQuery(hql).setParameterList("contentHashValueList", contentHashValueList).list();
      } else if (contentHashValueList.size() > 1000) {
        Integer times = contentHashValueList.size() / 1000;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String hql1 =
            "select t.discode from PubKeywordsSubsetsCotfHnt t where t.contentHashValue in (:contentHashValueList0)";
        List<Long> contentHashValueList0 = new ArrayList<Long>(contentHashValueList.subList(0, 1000));
        parameterMap.put("contentHashValueList0", contentHashValueList0);
        for (int i = 1; i <= times; i++) {
          hql1 = hql1 + " or t.contentHashValue in (:contentHashValueList" + i + ")";
          if (i < times) {
            List<Long> valueList = new ArrayList<Long>(contentHashValueList.subList(i * 1000, i * 1000 + 1000));
            parameterMap.put("contentHashValueList" + i, valueList);
          } else if (i == times) {
            List<Long> valueList =
                new ArrayList<Long>(contentHashValueList.subList(i * 1000, contentHashValueList.size()));
            parameterMap.put("contentHashValueList" + i, valueList);
          }
        }
        hql1 = hql1 + " order by t.size desc, t.counts desc";
        return super.createQuery(hql1).setProperties(parameterMap).list();
      }
    }
    return null;
  }

}
