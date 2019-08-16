package com.smate.center.task.dao.snsbak;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.BdspCoopCityInterior;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspCoopCityInteriorDao extends SnsbakHibernateDao<BdspCoopCityInterior, Long> {

  public void saveRs(PubProvinceInteriorKey key, Integer count, Integer indexId) {
    Integer pubYear = key.getPubYear();
    Integer categoryId = key.getCategoryId();
    Integer cityId = key.getProvinceId();
    Integer coopcityId = key.getCoopProvinceId();
    if (pubYear == null || categoryId == null || cityId == null || coopcityId == null) {
      return;
    }
    String hql =
        "from BdspCoopCityInterior t where t.pubYear=:pubYear and t.categoryId=:categoryId and t.cityId=:cityId and t.coopcityId=:coopcityId and t.indexId=:indexId order by t.id desc";
    List<BdspCoopCityInterior> list = (List<BdspCoopCityInterior>) super.createQuery(hql)
        .setParameter("pubYear", pubYear).setParameter("categoryId", categoryId).setParameter("cityId", cityId)
        .setParameter("coopcityId", coopcityId).setParameter("indexId", indexId).list();
    if (list != null && list.size() != 0) {
      BdspCoopCityInterior bcpi = list.get(0);
      count = count + bcpi.getCount();
      bcpi.setCount(count);
      bcpi.setUpdateTime(new Date());
      super.save(bcpi);
    } else {
      BdspCoopCityInterior bcpi =
          new BdspCoopCityInterior(indexId, pubYear, categoryId, cityId, coopcityId, count, new Date());
      super.save(bcpi);
    }

  }

}
