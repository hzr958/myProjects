package com.smate.web.psn.dao.keywork;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.keyword.RermcPsnKwHot;

/**
 * 反推热词dao
 * 
 * @author zk
 * 
 */
@Repository
public class RermcPsnKwHotDao extends SnsHibernateDao<RermcPsnKwHot, Long> {

  // RermcPsnKwHot.id越小，推荐度越大
  @SuppressWarnings("unchecked")
  public List<String> getRermcHotByPsnId(Long psnId, Integer limitSize) {
    List<String> returnKwList = null;
    String hql = "select keyword from RermcPsnKwHot where psnId=:psnId order by id ";
    List<String> hotKwList = super.createQuery(hql).setParameter("psnId", psnId).list();

    if (CollectionUtils.isNotEmpty(hotKwList)) {
      returnKwList = new ArrayList<String>();
      for (String hotKw : hotKwList) {
        if (!returnKwList.contains(hotKw)) {
          returnKwList.add(hotKw);
        }
        if (returnKwList.size() >= limitSize) {
          break;
        }
      }
    }
    return returnKwList;
  }

  // 删除psnId的反推热词
  public void delRermcHotByPsnId(Long psnId) {
    String hql = "delete from RermcPsnKwHot where psnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }
}
