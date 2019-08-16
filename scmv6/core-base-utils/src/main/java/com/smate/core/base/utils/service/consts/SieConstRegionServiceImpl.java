package com.smate.core.base.utils.service.consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.exception.SmateException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.SieConstRegion;

/**
 * 国家与地区/省份的公共读取业务模块.
 * 
 * @author hd
 */
@Service("sieConstRegionService")
@Transactional(rollbackFor = Exception.class)
public class SieConstRegionServiceImpl implements SieConstRegionService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieConstRegionDao sieConstRegionDao;

  @Override
  public String findRegionJsonData(Long superRegionId) throws SmateException {
    try {

      List<SieConstRegion> list = sieConstRegionDao.findRegionData(superRegionId);
      List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
      for (SieConstRegion cr : list) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ObjectUtils.toString(cr.getId()));
        map.put("zh_CN_name", cr.getZhName());
        map.put("en_US_name", cr.getEnName());
        map.put("zh_CN_seq", ObjectUtils.toString(cr.getZhSeq()));
        map.put("en_US_seq", ObjectUtils.toString(cr.getEnSeq()));
        resultList.add(map);
      }

      // 返回json数据，格式
      return JacksonUtils.listToJsonStr(resultList);
    } catch (Exception e) {
      logger.error("获取地区数据错误.", e);
      throw new SmateException(e);
    }
  }

  @Override
  public SieConstRegion getRegionByName(String name) throws SmateException {
    SieConstRegion sieConstRegion = null;
    List<SieConstRegion> regionList = sieConstRegionDao.getRegionByName(name);
    if (regionList != null && regionList.size() > 0) {
      sieConstRegion = regionList.get(0);
    } else {
      sieConstRegion = new SieConstRegion();
    }
    return sieConstRegion;
  }

  @Override
  public String findCnJsonData(Long superRegionId) throws SmateException {
    try {

      List<SieConstRegion> list = sieConstRegionDao.findRegionData(superRegionId);
      List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
      for (SieConstRegion cr : list) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ObjectUtils.toString(cr.getId()));
        map.put("zh_CN_name", cr.getZhName());
        map.put("en_US_name", cr.getEnName());
        map.put("zh_CN_seq", ObjectUtils.toString(cr.getZhSeq()));
        map.put("en_US_seq", ObjectUtils.toString(cr.getEnSeq()));
        resultList.add(map);
      }

      // 返回json数据，格式
      return JacksonUtils.listToJsonStr(resultList);
    } catch (Exception e) {
      logger.error("获取地区数据错误", e);
      throw new SmateException(e);
    }
  }

  @Override
  public SieConstRegion getRegionById(Long id) throws SmateException {
    try {
      return sieConstRegionDao.get(id);

    } catch (Exception e) {
      logger.error("根据id获取地区数据错误", e);
      throw new SmateException(e);
    }
  }

  @Override
  public Long findRegionId(String regionName) throws SmateException {
    try {
      if (regionName == null) {
        return null;
      }
      SieConstRegion region = sieConstRegionDao.getConstRegionByName(regionName);
      if (region == null) {
        return null;
      } else {
        return region.getId();
      }
    } catch (Exception e) {
      logger.error("获取region_id失败", e);
      throw new SmateException("获取region_id失败", e);
    }
  }

}
