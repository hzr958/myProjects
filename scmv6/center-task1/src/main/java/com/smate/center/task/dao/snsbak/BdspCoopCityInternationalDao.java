package com.smate.center.task.dao.snsbak;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.BdspCoopCityInternational;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspCoopCityInternationalDao extends SnsbakHibernateDao<BdspCoopCityInternational, Long> {

  public void saveRs(PubProvinceInteriorKey key, Integer count, Integer indexId) {
    Integer pubYear = key.getPubYear();
    Integer categoryId = key.getCategoryId();
    Integer cityId = key.getProvinceId();
    Integer countryId = key.getCoopProvinceId();
    if (pubYear == null || categoryId == null || cityId == null || countryId == null) {
      return;
    }
    String hql =
        "from BdspCoopCityInternational t where t.pubYear=:pubYear and t.categoryId=:categoryId and t.cityId=:cityId and t.countryId=:countryId and t.indexId=:indexId order by t.id desc";
    List<BdspCoopCityInternational> list = (List<BdspCoopCityInternational>) super.createQuery(hql)
        .setParameter("pubYear", pubYear).setParameter("categoryId", categoryId).setParameter("cityId", cityId)
        .setParameter("countryId", countryId).setParameter("indexId", indexId).list();
    if (list != null && list.size() != 0) {
      BdspCoopCityInternational bcpi = list.get(0);
      count = count + bcpi.getCount();
      bcpi.setCount(count);
      bcpi.setUpdateTime(new Date());
      super.save(bcpi);
    } else {
      BdspCoopCityInternational bcpi =
          new BdspCoopCityInternational(indexId, pubYear, categoryId, cityId, countryId, count, new Date());
      super.save(bcpi);
    }

  }

}
