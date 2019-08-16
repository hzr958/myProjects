package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ConstRegionDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.json.JacksonUtils;


/**
 * 国家与地区/省份的公共读取业务模块.
 * 
 * @author zb
 * 
 */
@Service("constRegionService")
@Transactional(rollbackFor = Exception.class)
public class ConstRegionServiceImpl extends EntityManagerImpl<ConstRegion, Long> implements ConstRegionService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionDao constRegionDao;

  @Override
  protected ConstRegionDao getEntityDao() {
    return constRegionDao;
  }

  /**
   * 获取所有国家或地区.
   */
  @Override
  public List<ConstRegion> getAllCountryAndRegion() throws ServiceException {
    try {
      return constRegionDao.getAllCountryAndRegion(LocaleContextHolder.getLocale());
    } catch (DaoException e) {
      logger.error("getAllRegion获取所有国家地区错误：", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 获取所有省份.
   */
  @Override
  public List<ConstRegion> getAllProvince() throws ServiceException {
    try {
      return constRegionDao.getAllProvince(LocaleContextHolder.getLocale());
    } catch (DaoException e) {
      logger.error("getAllProvince获取所省份错误：", e);
      throw new ServiceException(e);

    }
  }

  /**
   * 获取所有城市.
   * 
   * @return
   * @throws ServiceException
   */
  @Override
  public List<ConstRegion> getAllCity() throws ServiceException {

    try {
      return constRegionDao.getAllCity(LocaleContextHolder.getLocale());
    } catch (DaoException e) {
      logger.error("getAllProvince获取所省份错误：", e);
      throw new ServiceException(e);

    }
  }

  // 通过superRegionId查询国家和区域数据
  @Override
  public String findRegionJsonData(Long superRegionId) throws ServiceException {
    try {

      List<ConstRegion> list = constRegionDao.findRegionData(superRegionId);
      List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
      for (ConstRegion cr : list) {
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
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取单个数据.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @Override
  public ConstRegion getRegionRolById(Long id) throws ServiceException {

    try {
      return constRegionDao.getConstRegionById(id);
    } catch (DaoException e) {
      logger.error("getAllRegion获取所有国家地区错误：", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取所在国家地区.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @Override
  public String getRegionsById(Long id) throws ServiceException {

    try {
      List<String> result = new ArrayList<String>();
      if (id != null) {
        ConstRegion lastRegion = constRegionDao.getConstRegionById(id);
        if (lastRegion != null) {

          ConstRegion middleRegion = constRegionDao.getConstRegionById(lastRegion.getSuperRegionId());
          if (middleRegion != null) {
            ConstRegion firstRegion = constRegionDao.getConstRegionById(middleRegion.getSuperRegionId());
            if (firstRegion != null) {// 第一级
              result.add(String.valueOf(middleRegion.getSuperRegionId()));
            }
            result.add(String.valueOf(lastRegion.getSuperRegionId()));// 第二级
          }
          result.add(String.valueOf(id));// 第三级
        }
      }

      return StringUtils.join(result, ",");
    } catch (DaoException e) {
      logger.error("获取所在国家地区错误：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public ConstRegion getConstRegionByName(String country) throws ServiceException {
    try {
      return constRegionDao.getConstRegionByName(country);
    } catch (DaoException e) {
      logger.error("getConstRegionByName获取国家地区错误：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<ConstRegion> getAllRegion() throws ServiceException {
    try {
      return constRegionDao.getAllRegion();
    } catch (DaoException e) {
      logger.error("getAllRegion获取所有国家或地区数据错误：", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<ConstRegion> getRegionBySuperId(Long superRegionId) throws ServiceException {
    try {
      return constRegionDao.getRegionBySuperId(superRegionId);
    } catch (DaoException e) {
      logger.error("getRegionBySuperId获取指定国家或地区的省份superRegionId:{}", superRegionId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Boolean isRegionIdExit(Long regionId) throws ServiceException {
    try {
      return constRegionDao.isRegionIdExit(regionId);
    } catch (DaoException e) {
      logger.error("isRegionIdExit查询国家或地区的ID是否存在regionId:{}", regionId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> isRegionIdExit(List<Long> regionIds) throws ServiceException {

    List<Long> dup = new ArrayList<Long>();
    for (Long regionId : regionIds) {
      if (this.isRegionIdExit(regionId)) {
        dup.add(regionId);
      }
    }
    return dup;
  }

  @Override
  public void pullConstRegionSyn(List<ConstRegion> list) throws ServiceException {

    try {

      this.constRegionDao.removeAll();
      for (ConstRegion region : list) {
        this.constRegionDao.save(region);
      }
    } catch (DaoException e) {
      logger.error("pullConstRegionSyn接收国家或地区同步错误", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据传入ID，语言返回所在地
   * 
   * 英文:city+province+country
   * 
   * 中文country+province+city .
   * 
   * @param regionId
   * @param lan
   * @return
   * @throws ServiceException
   */
  @Override
  public String getConstRegionShow(Long regionId, String lan) throws ServiceException {

    try {
      ConstRegion region = this.constRegionDao.get(regionId);
      if (region == null) {
        return null;
      }
      Long parrentId = region.getSuperRegionId();
      ConstRegion perentRegion = null;
      ConstRegion superRegion = null;
      if (parrentId != null) {
        perentRegion = this.constRegionDao.get(parrentId);
        if (perentRegion != null && perentRegion.getSuperRegionId() != null) {
          superRegion = this.constRegionDao.get(perentRegion.getSuperRegionId());
        }
      }
      StringBuilder sb = new StringBuilder();
      // 英文，city+province+country
      if (lan.equalsIgnoreCase("en")) {
        sb.append(region.getEnName());

        if (perentRegion != null) {
          sb.append(" ").append(perentRegion.getEnName());
        }

        if (superRegion != null) {
          sb.append(" ").append(superRegion.getEnName());
        }

      } else {// 中文country+province+city
        if (superRegion != null) {
          sb.append(superRegion.getZhName()).append(" ");
        }
        if (perentRegion != null) {
          sb.append(perentRegion.getZhName()).append(" ");
        }
        sb.append(region.getZhName());
      }
      return sb.toString();
    } catch (Exception e) {
      logger.error("根据传入ID，语言返回所在地.错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getOldMapingId(Integer oldId) throws ServiceException {
    try {

      return this.constRegionDao.getOldMapingId(oldId);
    } catch (Exception e) {
      logger.error("V2.6的ID对应过来的V3的ID错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getRegionIdByCode(String regionCode) throws ServiceException {

    ConstRegion cr = new ConstRegion();
    try {
      cr = this.constRegionDao.getRegionId(regionCode);
    } catch (DaoException e) {

      logger.error("根据regionCode={}读取地区Id失败！", regionCode, e);
    }

    return cr == null ? null : cr.getId();
  }

  @Override
  public List<ConstRegion> findRegionByIds(List<Long> ids) throws ServiceException {
    try {
      return this.constRegionDao.findByIds(ids);
    } catch (Exception e) {
      logger.error("根据codeList读取地区信息失败！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 根据机构ID获取其所有父级单位.
   * 
   * @param regionId
   * @return
   */
  @Override
  public List<Long> getSuperRegionList(Long regionId) {
    return this.constRegionDao.getSuperRegionList(regionId);
  }
}
