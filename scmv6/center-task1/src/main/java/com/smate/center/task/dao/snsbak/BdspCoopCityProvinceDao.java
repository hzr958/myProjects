package com.smate.center.task.dao.snsbak;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.BdspCoopCityProvince;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspCoopCityProvinceDao extends SnsbakHibernateDao<BdspCoopCityProvince, Long> {

  public void saveRs(PubProvinceInteriorKey key, Integer count, Integer indexId) {
    Integer pubYear = key.getPubYear();
    Integer categoryId = key.getCategoryId();
    Integer cityId = key.getProvinceId();
    Integer coopProvinceId = key.getCoopProvinceId();
    if (pubYear == null || categoryId == null || cityId == null || coopProvinceId == null) {
      return;
    }
    String hql =
        "from BdspCoopCityProvince t where t.pubYear=:pubYear and t.categoryId=:categoryId and t.cityId=:cityId and t.coopProvinceId=:coopProvinceId and t.indexId=:indexId order by t.id desc";
    List<BdspCoopCityProvince> list = (List<BdspCoopCityProvince>) super.createQuery(hql)
        .setParameter("pubYear", pubYear).setParameter("categoryId", categoryId).setParameter("cityId", cityId)
        .setParameter("coopProvinceId", coopProvinceId).setParameter("indexId", indexId).list();
    if (list != null && list.size() != 0) {
      BdspCoopCityProvince bcpi = list.get(0);
      count = count + bcpi.getCount();
      bcpi.setCount(count);
      bcpi.setUpdateTime(new Date());
      super.save(bcpi);
    } else {
      BdspCoopCityProvince bcpi =
          new BdspCoopCityProvince(indexId, pubYear, categoryId, cityId, coopProvinceId, count, new Date());
      super.save(bcpi);
    }

  }

}
