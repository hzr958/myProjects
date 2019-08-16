package com.smate.center.task.dao.snsbak;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.BdspCoopProvInterior;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspCoopProvInteriorDao extends SnsbakHibernateDao<BdspCoopProvInterior, Long> {

  public void saveRs(PubProvinceInteriorKey key, Integer count, Integer indexId) {
    Integer pubYear = key.getPubYear();
    Integer categoryId = key.getCategoryId();
    Integer provinceId = key.getProvinceId();
    Integer coopProvinceId = key.getCoopProvinceId();
    if (pubYear == null || categoryId == null || provinceId == null || coopProvinceId == null) {
      return;
    }
    String hql =
        "from BdspCoopProvInterior t where t.pubYear=:pubYear and t.categoryId=:categoryId and t.provinceId=:provinceId and t.coopProvinceId=:coopProvinceId and t.indexId=:indexId order by t.id desc";
    List<BdspCoopProvInterior> list = (List<BdspCoopProvInterior>) super.createQuery(hql)
        .setParameter("pubYear", pubYear).setParameter("categoryId", categoryId).setParameter("provinceId", provinceId)
        .setParameter("coopProvinceId", coopProvinceId).setParameter("indexId", indexId).list();
    if (list != null && list.size() != 0) {
      BdspCoopProvInterior bcpi = list.get(0);
      count = count + bcpi.getCount();
      bcpi.setCount(count);
      bcpi.setUpdateTime(new Date());
      super.save(bcpi);
    } else {
      BdspCoopProvInterior bcpi =
          new BdspCoopProvInterior(indexId, pubYear, categoryId, provinceId, coopProvinceId, count, new Date());
      super.save(bcpi);
    }

  }

}
