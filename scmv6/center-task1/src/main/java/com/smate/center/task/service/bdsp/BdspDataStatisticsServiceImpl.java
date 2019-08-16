package com.smate.center.task.service.bdsp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.snsbak.BdspCoopCityIndependentDao;
import com.smate.center.task.dao.snsbak.BdspCoopCityInteriorDao;
import com.smate.center.task.dao.snsbak.BdspCoopCityInternationalDao;
import com.smate.center.task.dao.snsbak.BdspCoopCityProvinceDao;
import com.smate.center.task.dao.snsbak.BdspCoopProvInteriorDao;
import com.smate.center.task.dao.snsbak.BdspCoopProvInternationalDao;
import com.smate.center.task.dao.snsbak.BdspCoopPubIndependentDao;
import com.smate.center.task.dao.snsbak.PubCategorySnsbakDao;
import com.smate.center.task.dao.snsbak.PubPdwhAddrInfoInit1Dao;
import com.smate.center.task.dao.snsbak.PubPdwhAddrInfoInit2Dao;
import com.smate.center.task.dao.snsbak.PubPdwhAddrInfoInitDao;
import com.smate.center.task.dao.snsbak.PubPdwhAddrInfoStandardDao;
import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit;
import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit1;
import com.smate.center.task.model.snsbak.PubPdwhAddrInfoInit2;
import com.smate.center.task.model.snsbak.PubProvinceInteriorKey;

@Service("bdspDataStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class BdspDataStatisticsServiceImpl implements BdspDataStatisticsService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhAddrInfoInitDao pubPdwhAddrInfoInitDao;
  @Autowired
  private PubPdwhAddrInfoStandardDao pubPdwhAddrInfoStandardDao;
  @Autowired
  private PubCategorySnsbakDao pubCategoryDao;
  @Autowired
  private BdspCoopProvInteriorDao bdspCoopProvInteriorDao;
  @Autowired
  private BdspCoopProvInternationalDao bdspCoopProvInternationalDao;
  @Autowired
  private BdspCoopCityInteriorDao bdspCoopCityInteriorDao;
  @Autowired
  private BdspCoopCityInternationalDao bdspCoopCityInternationalDao;
  @Autowired
  private BdspCoopCityProvinceDao bdspCoopCityProvinceDao;
  @Autowired
  private PubPdwhAddrInfoInit1Dao pubPdwhAddrInfoInit1Dao;
  @Autowired
  private PubPdwhAddrInfoInit2Dao pubPdwhAddrInfoInit2Dao;
  @Autowired
  private BdspCoopCityIndependentDao bdspCoopCityIndependentDao;
  @Autowired
  private BdspCoopPubIndependentDao bdspCoopPubIndependentDao;

  @Override
  public List<PubPdwhAddrInfoInit1> getProvInteriorToHandleList() {
    return this.pubPdwhAddrInfoInit1Dao.getProvInteritorToHandleList(500);
  }

  @Override
  public List<PubPdwhAddrInfoInit2> getProvInternationalToHandleList() {
    return this.pubPdwhAddrInfoInit2Dao.getProvInternationalToHandleList(500);
  }

  @Override
  public List<PubPdwhAddrInfoInit> getCityToHandleList() {
    return this.pubPdwhAddrInfoInitDao.getCityToHandleList(1500);
  }

  @Override
  public void pubComparativeAnalysisByAreaProInterior(List<PubPdwhAddrInfoInit1> list) {
    HashMap provinceMap = new HashMap<PubProvinceInteriorKey, Integer>();

    for (PubPdwhAddrInfoInit1 pi : list) {
      Long pubId = pi.getPdwhPubId();
      Integer pubYear = pi.getPubYear();
      Integer indexId = this.getIndexIdByType(pi.getPubType());
      try {
        List<Integer> provinceIdList = this.pubPdwhAddrInfoStandardDao.getProvinceIdsByPubId(pubId);
        List<String> categoryIdList = this.pubCategoryDao.getScmCategory1stLevelByPubId(pubId);
        if (CollectionUtils.isEmpty(categoryIdList) && CollectionUtils.isEmpty(provinceIdList)) {
          pi.setStatus(4);
          this.pubPdwhAddrInfoInit1Dao.save(pi);
          continue;
        } else if (CollectionUtils.isEmpty(provinceIdList)) {
          pi.setStatus(5);
          this.pubPdwhAddrInfoInit1Dao.save(pi);
          continue;
        } else if (CollectionUtils.isEmpty(categoryIdList)) {
          pi.setStatus(6);
          this.pubPdwhAddrInfoInit1Dao.save(pi);
          continue;
        }
        this.calculateProInterior(indexId, pubId, pubYear, provinceIdList, categoryIdList, provinceMap);
        pi.setStatus(1);
        this.pubPdwhAddrInfoInit1Dao.save(pi);
      } catch (Exception e) {
        logger.error("计算省内合作出错，pubId = " + pubId + "===", e);
        pi.setStatus(3);
        this.pubPdwhAddrInfoInit1Dao.save(pi);
      }
    }
    this.saveProInteriorCoopRs(provinceMap);
  }

  private void calculateProInterior(Integer indexId, Long pubId, Integer pubYear, List<Integer> provinceIdList,
      List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap) throws Exception {
    if (provinceIdList == null || provinceIdList.size() == 0 || provinceIdList.size() == 1) {
      return;
    }
    for (Integer provinceId : provinceIdList) {
      for (Integer coopProvinceId : provinceIdList) {
        if (coopProvinceId != provinceId) {
          for (String cId : categoryIdList) {
            PubProvinceInteriorKey pk =
                new PubProvinceInteriorKey(provinceId, coopProvinceId, Integer.parseInt(cId), pubYear, indexId);
            if (rsMap.get(pk) != null) {
              rsMap.put(pk, rsMap.get(pk) + 1);
            } else {
              rsMap.put(pk, 1);
            }
          }
        }
      }
    }
  }

  private void saveProInteriorCoopRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopProvInteriorDao.saveRs(key, count, key.getIndexId());
    }
  }

  @Override
  public void pubComparativeAnalysisByAreaProvInternational(List<PubPdwhAddrInfoInit2> list) {
    HashMap provinceMap = new HashMap<PubProvinceInteriorKey, Integer>();

    for (PubPdwhAddrInfoInit2 pi : list) {
      Long pubId = pi.getPdwhPubId();
      Integer pubYear = pi.getPubYear();
      Integer indexId = this.getIndexIdByType(pi.getPubType());
      try {
        List<Integer> provinceIdList = this.pubPdwhAddrInfoStandardDao.getProvinceIdsByPubId(pubId);
        List<Integer> countryIdList = this.pubPdwhAddrInfoStandardDao.getCountryIdsByPubId(pubId);
        List<String> categoryIdList = this.pubCategoryDao.getScmCategory1stLevelByPubId(pubId);
        if (CollectionUtils.isEmpty(categoryIdList) && CollectionUtils.isEmpty(provinceIdList)) {
          pi.setStatusInternational(4);
          this.pubPdwhAddrInfoInit2Dao.save(pi);
          continue;
        } else if (CollectionUtils.isEmpty(provinceIdList)) {
          pi.setStatusInternational(5);
          this.pubPdwhAddrInfoInit2Dao.save(pi);
          continue;
        } else if (CollectionUtils.isEmpty(categoryIdList)) {
          pi.setStatusInternational(6);
          this.pubPdwhAddrInfoInit2Dao.save(pi);
          continue;
        }
        this.calculateProvInternational(indexId, pubId, pubYear, provinceIdList, countryIdList, categoryIdList,
            provinceMap);
        pi.setStatusInternational(1);
        this.pubPdwhAddrInfoInit2Dao.save(pi);
      } catch (Exception e) {
        logger.error("计算省国际合作出错，pubId = " + pubId + "===", e);
        pi.setStatusInternational(3);
        this.pubPdwhAddrInfoInit2Dao.save(pi);
      }
    }
    this.saveProInternationalCoopRs(provinceMap);
  }

  private void calculateProvInternational(Integer indexId, Long pubId, Integer pubYear, List<Integer> provinceIdList,
      List<Integer> countryIdList, List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap)
      throws Exception {
    if (provinceIdList == null || countryIdList == null || provinceIdList.size() == 0 || countryIdList.size() == 0) {
      return;
    }
    for (Integer provinceId : provinceIdList) {
      for (Integer coopCountry : countryIdList) {
        for (String cId : categoryIdList) {
          PubProvinceInteriorKey pk =
              new PubProvinceInteriorKey(provinceId, coopCountry, Integer.parseInt(cId), pubYear, indexId);
          if (rsMap.get(pk) != null) {
            rsMap.put(pk, rsMap.get(pk) + 1);
          } else {
            rsMap.put(pk, 1);
          }
        }
      }
    }
  }

  private void saveProInternationalCoopRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopProvInternationalDao.saveRs(key, count, key.getIndexId());
    }
  }

  @Override
  public void pubComparativeAnalysisByAreaCity(List<PubPdwhAddrInfoInit> list) {
    HashMap cityMap = new HashMap<PubProvinceInteriorKey, Integer>();
    HashMap provinceMap = new HashMap<PubProvinceInteriorKey, Integer>();
    HashMap countryMap = new HashMap<PubProvinceInteriorKey, Integer>();
    HashMap cityIndependentMap = new HashMap<PubProvinceInteriorKey, Integer>();

    for (PubPdwhAddrInfoInit pi : list) {
      Long pubId = pi.getPdwhPubId();
      Integer pubYear = pi.getPubYear();
      Integer indexId = this.getIndexIdByType(pi.getPubType());
      try {
        List<Integer> cityIdList = this.pubPdwhAddrInfoStandardDao.getCityIdsByPubId(pubId);
        // 排除江西
        List<Integer> provinceIdList = this.pubPdwhAddrInfoStandardDao.getProvinceIdsByPubId(pubId);
        List<Integer> countryIdList = this.pubPdwhAddrInfoStandardDao.getCountryIdsByPubId(pubId);
        List<String> categoryIdList = this.pubCategoryDao.getScmCategory1stLevelByPubId(pubId);
        /*
         * if (CollectionUtils.isEmpty(categoryIdList) && CollectionUtils.isEmpty(provinceIdList)) {
         * pi.setStatusCity(4); this.pubPdwhAddrInfoInitDao.save(pi); continue; } else if
         * (CollectionUtils.isEmpty(provinceIdList)) { pi.setStatusCity(5);
         * this.pubPdwhAddrInfoInitDao.save(pi); continue; } else
         */if (CollectionUtils.isEmpty(categoryIdList)) {
          pi.setStatusCity(6);
          this.pubPdwhAddrInfoInitDao.save(pi);
          continue;
        }
        /*
         * this.calculateCityInterior(indexId, pubId, pubYear, cityIdList, categoryIdList, cityMap);
         * this.calculateCityProvince(indexId, pubId, pubYear, cityIdList, provinceIdList, categoryIdList,
         * provinceMap); this.calculateCityInternational(indexId, pubId, pubYear, cityIdList, countryIdList,
         * categoryIdList, countryMap);
         */
        this.calculateCityIndependent(indexId, pubId, pubYear, cityIdList, provinceIdList, categoryIdList,
            cityIndependentMap);
        pi.setStatusCity(1);
        this.pubPdwhAddrInfoInitDao.save(pi);
      } catch (Exception e) {
        logger.error("计算江西省地级市合作出错，pubId = " + pubId + "===", e);
        pi.setStatusCity(3);
        this.pubPdwhAddrInfoInitDao.save(pi);
      }
    }
    /*
     * this.saveCityInteriorCoopRs(cityMap); this.saveCityInternationalCoopRs(countryMap);
     * this.saveCityProvinceCoopRs(provinceMap);
     */
    this.saveCityIndependentRs(cityIndependentMap);
  }

  // 成果作者只有一个人
  @Override
  public void pubIndependentPrCityAnalysis(List<PubPdwhAddrInfoInit> list) {
    HashMap pubIndependentMap = new HashMap<PubProvinceInteriorKey, Integer>();

    for (PubPdwhAddrInfoInit pi : list) {
      Long pubId = pi.getPdwhPubId();
      Integer pubYear = pi.getPubYear();
      Integer indexId = this.getIndexIdByType(pi.getPubType());
      try {
        List<Integer> cityIdList = this.pubPdwhAddrInfoStandardDao.getCityIdsByPubId(pubId);
        List<Integer> provinceIdList = this.pubPdwhAddrInfoStandardDao.getProvinceIdsByPubId(pubId);
        List<String> categoryIdList = this.pubCategoryDao.getScmCategory1stLevelByPubId(pubId);
        if (CollectionUtils.isEmpty(categoryIdList)) {
          pi.setStatusCity(6);
          this.pubPdwhAddrInfoInitDao.save(pi);
          continue;
        }
        this.calculatePubIndependent(indexId, pubId, pubYear, cityIdList, provinceIdList, categoryIdList,
            pubIndependentMap);
        pi.setStatusCity(1);
        this.pubPdwhAddrInfoInitDao.save(pi);
      } catch (Exception e) {
        logger.error("计算江西省地级市合作出错，pubId = " + pubId + "===", e);
        pi.setStatusCity(3);
        this.pubPdwhAddrInfoInitDao.save(pi);
      }
    }
    this.savePubIndependentRs(pubIndependentMap);
  }

  private void calculateCityIndependent(Integer indexId, Long pubId, Integer pubYear, List<Integer> cityIdList,
      List<Integer> provinceIdList, List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (cityIdList == null || cityIdList.size() != 1) {
      return;
    }
    if (provinceIdList == null || provinceIdList.size() != 1) {
      return;
    }
    Integer cityId = cityIdList.get(0);
    Integer provinceId = provinceIdList.get(0);
    for (String cId : categoryIdList) {
      PubProvinceInteriorKey pk =
          new PubProvinceInteriorKey(cityId, provinceId, Integer.parseInt(cId), pubYear, indexId);
      if (rsMap.get(pk) != null) {
        rsMap.put(pk, rsMap.get(pk) + 1);
      } else {
        rsMap.put(pk, 1);
      }
    }
  }

  private void calculatePubIndependent(Integer indexId, Long pubId, Integer pubYear, List<Integer> cityIdList,
      List<Integer> provinceIdList, List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (cityIdList == null || cityIdList.size() != 1) {
      return;
    }
    if (provinceIdList == null || provinceIdList.size() != 1) {
      return;
    }
    Integer cityId = cityIdList.get(0);
    Integer provinceId = provinceIdList.get(0);
    for (String cId : categoryIdList) {
      PubProvinceInteriorKey pk =
          new PubProvinceInteriorKey(cityId, provinceId, Integer.parseInt(cId), pubYear, indexId);
      if (rsMap.get(pk) != null) {
        rsMap.put(pk, rsMap.get(pk) + 1);
      } else {
        rsMap.put(pk, 1);
      }
    }
  }

  private void calculateCityInterior(Integer indexId, Long pubId, Integer pubYear, List<Integer> cityIdList,
      List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap) throws Exception {
    if (cityIdList == null || cityIdList.size() == 0 || cityIdList.size() == 1) {
      return;
    }
    for (Integer cityId : cityIdList) {
      for (Integer coopCityId : cityIdList) {
        if (coopCityId != cityId) {
          for (String cId : categoryIdList) {
            PubProvinceInteriorKey pk =
                new PubProvinceInteriorKey(cityId, coopCityId, Integer.parseInt(cId), pubYear, indexId);
            if (rsMap.get(pk) != null) {
              rsMap.put(pk, rsMap.get(pk) + 1);
            } else {
              rsMap.put(pk, 1);
            }
          }
        }
      }
    }
  }

  private void calculateCityInternational(Integer indexId, Long pubId, Integer pubYear, List<Integer> cityIdList,
      List<Integer> countryIdList, List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap)
      throws Exception {
    if (cityIdList == null || countryIdList == null || cityIdList.size() == 0 || countryIdList.size() == 0) {
      return;
    }
    for (Integer cityId : cityIdList) {
      for (Integer coopCountry : countryIdList) {
        for (String cId : categoryIdList) {
          PubProvinceInteriorKey pk =
              new PubProvinceInteriorKey(cityId, coopCountry, Integer.parseInt(cId), pubYear, indexId);
          if (rsMap.get(pk) != null) {
            rsMap.put(pk, rsMap.get(pk) + 1);
          } else {
            rsMap.put(pk, 1);
          }
        }
      }
    }
  }

  private void calculateCityProvince(Integer indexId, Long pubId, Integer pubYear, List<Integer> cityIdList,
      List<Integer> provinceIdList, List<String> categoryIdList, HashMap<PubProvinceInteriorKey, Integer> rsMap)
      throws Exception {
    if (cityIdList == null || provinceIdList == null || cityIdList.size() == 0 || provinceIdList.size() == 0) {
      return;
    }
    for (Integer cityId : cityIdList) {
      for (Integer provinceId : provinceIdList) {
        if (provinceId == 36000) {
          continue;
        }

        for (String cId : categoryIdList) {
          PubProvinceInteriorKey pk =
              new PubProvinceInteriorKey(cityId, provinceId, Integer.parseInt(cId), pubYear, indexId);
          if (rsMap.get(pk) != null) {
            rsMap.put(pk, rsMap.get(pk) + 1);
          } else {
            rsMap.put(pk, 1);
          }
        }
      }
    }
  }

  private void saveCityInteriorCoopRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopCityInteriorDao.saveRs(key, count, key.getIndexId());
    }
  }

  private void saveCityProvinceCoopRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopCityProvinceDao.saveRs(key, count, key.getIndexId());
    }
  }

  private void saveCityInternationalCoopRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopCityInternationalDao.saveRs(key, count, key.getIndexId());
    }
  }

  private void saveCityIndependentRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopCityIndependentDao.saveRs(key, count, key.getIndexId());
    }
  }

  private void savePubIndependentRs(HashMap<PubProvinceInteriorKey, Integer> rsMap) {
    if (rsMap == null || rsMap.size() == 0) {
      return;
    }
    Iterator<Entry<PubProvinceInteriorKey, Integer>> it = rsMap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<PubProvinceInteriorKey, Integer> entry = it.next();
      PubProvinceInteriorKey key = entry.getKey();
      Integer count = entry.getValue();
      this.bdspCoopPubIndependentDao.saveRs(key, count, key.getIndexId());
    }
  }

  private Integer getIndexIdByType(Integer type) {
    // 成果
    if (type == 3 || type == 4) {
      return 61;
      // 专利
    } else if (type == 5) {
      return 62;
      // 项目
    } else if (type == 1) {
      return 60;
    } else {
      return 99;
    }
  }

  public static void main(String[] args) {
    PubProvinceInteriorKey pk1 = new PubProvinceInteriorKey(36000, 36023, 3, 2015, 8);
    PubProvinceInteriorKey pk2 = new PubProvinceInteriorKey(36000, 36023, 3, 2015, 6);
    boolean bl = pk1.equals(pk2);
    System.out.println(bl);
    HashMap hm = new HashMap<PubProvinceInteriorKey, Integer>();
    hm.put(pk1, 7);
    System.out.println(hm.get(pk2));
  }
}
