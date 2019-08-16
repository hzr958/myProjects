package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatKeywords;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * CnkiPat成果关键词DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCnkiPatKeywordsDao extends RolHibernateDao<PubAssignCnkiPatKeywords, Long> {

  /**
   * 判断成果关键词是否已经存在.
   * 
   * @param pubId
   * @param keyHash
   * @return
   */
  public boolean isExitKeyWords(Long pubId, Integer keyHash) {

    String hql = "select count(id) from PubAssignCnkiPatKeywords where pubId = ? and keyHash = ? ";
    Long count = super.findUnique(hql, pubId, keyHash);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取单位用户关键词匹配上成果关键词的关键词列表.
   * 
   * @param pubId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) {
    Map<Long, Long> map = new HashMap<Long, Long>();
    String kwhql = "select pa.keyHash from PubAssignCnkiPatKeywords pa where pa.pubId = ? ";
    List<Integer> kwList = super.createQuery(kwhql, pubId).list();
    if (CollectionUtils.isEmpty(kwList)) {
      return map;
    }
    // 关键词太多，切断
    if (kwList.size() > 60) {
      kwList = kwList.subList(0, 60);
    }
    // 一次查询的参数个数限制
    int psnIdBatchSize = 90 - kwList.size();

    String hql =
        "select pkw.psnId,count(pkw.id) from PsnPmKeyWord pkw where pkw.psnId in(:psnIds) and pkw.kwhash in(:kwhash) group by pkw.psnId ";
    Collection<Collection<Long>> container = ServiceUtil.splitList(psnIds, psnIdBatchSize);
    List<Object[]> listResult = new ArrayList<Object[]>();
    for (Collection<Long> item : container) {
      listResult
          .addAll(super.createQuery(hql).setParameterList("psnIds", item).setParameterList("kwhash", kwList).list());
    }

    for (Object[] objs : listResult) {
      Long psnId = (Long) objs[0];
      Long count = (Long) objs[1];
      if (count == 0) {
        continue;
      }
      map.put(psnId, count);
    }
    return map;
  }

  /**
   * 获取成果关键词.
   * 
   * @param pubId
   * @return
   */
  public List<PubAssignCnkiPatKeywords> getKwByPubId(Long pubId) {
    String hql = "from PubAssignCnkiPatKeywords where pubId = ? ";
    return super.find(hql, pubId);
  }
}
