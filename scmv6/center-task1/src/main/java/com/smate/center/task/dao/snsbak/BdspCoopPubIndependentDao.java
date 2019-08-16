package com.smate.center.task.dao.snsbak;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.BdspCoopPubIndependent;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspCoopPubIndependentDao extends SnsbakHibernateDao<BdspCoopPubIndependent, Long> {

  public void saveRs(PubProvinceInteriorKey key, Integer count, Integer indexId) {
    Integer pubYear = key.getPubYear();
    Integer categoryId = key.getCategoryId();
    Integer cityId = key.getProvinceId();
    Integer provinceId = key.getCoopProvinceId();
    if (pubYear == null || categoryId == null || cityId == null || provinceId == null) {
      return;
    }
    String hql =
        "from BdspCoopPubIndependent t where t.pubYear=:pubYear and t.categoryId=:categoryId and t.cityId=:cityId and t.provinceId=:provinceId and t.indexId=:indexId order by t.id desc";
    List<BdspCoopPubIndependent> list = (List<BdspCoopPubIndependent>) super.createQuery(hql)
        .setParameter("pubYear", pubYear).setParameter("categoryId", categoryId).setParameter("cityId", cityId)
        .setParameter("provinceId", provinceId).setParameter("indexId", indexId).list();
    if (list != null && list.size() != 0) {
      BdspCoopPubIndependent bcpi = list.get(0);
      count = count + bcpi.getCount();
      bcpi.setCount(count);
      bcpi.setUpdateTime(new Date());
      super.save(bcpi);
    } else {
      BdspCoopPubIndependent bcpi =
          new BdspCoopPubIndependent(indexId, pubYear, categoryId, cityId, provinceId, count, new Date());
      super.save(bcpi);
    }

  }

}
