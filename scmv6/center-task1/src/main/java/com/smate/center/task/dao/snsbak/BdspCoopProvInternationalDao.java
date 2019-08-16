package com.smate.center.task.dao.snsbak;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.BdspCoopProvInternational;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspCoopProvInternationalDao extends SnsbakHibernateDao<BdspCoopProvInternational, Long> {

  public void saveRs(PubProvinceInteriorKey key, Integer count, Integer indexId) {
    Integer pubYear = key.getPubYear();
    Integer categoryId = key.getCategoryId();
    Integer provinceId = key.getProvinceId();
    Integer countryId = key.getCoopProvinceId();
    if (pubYear == null || categoryId == null || provinceId == null || countryId == null) {
      return;
    }
    String hql =
        "from BdspCoopProvInternational t where t.pubYear=:pubYear and t.categoryId=:categoryId and t.provinceId=:provinceId and t.countryId=:countryId and t.indexId=:indexId order by t.id desc";
    List<BdspCoopProvInternational> list = (List<BdspCoopProvInternational>) super.createQuery(hql)
        .setParameter("pubYear", pubYear).setParameter("categoryId", categoryId).setParameter("provinceId", provinceId)
        .setParameter("countryId", countryId).setParameter("indexId", indexId).list();
    if (list != null && list.size() != 0) {
      BdspCoopProvInternational bcpi = list.get(0);
      count = count + bcpi.getCount();
      bcpi.setCount(count);
      bcpi.setUpdateTime(new Date());
      super.save(bcpi);
    } else {
      BdspCoopProvInternational bcpi =
          new BdspCoopProvInternational(indexId, pubYear, categoryId, provinceId, countryId, count, new Date());
      super.save(bcpi);
    }

  }

}
