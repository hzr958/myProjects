package com.smate.web.psn.service.psnwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("psnConstRegionService")
@Transactional(rollbackFor = Exception.class)
public class PsnConstRegionServiceImpl implements PsnConstRegionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstRegionDao constRegionDao;

  /**
   * 获取国家地区
   */
  // @Override
  public String findRegionJsonData(Long superRegionId) throws ServiceException {

    try {

      List<ConstRegion> list = constRegionDao.findRegionData(superRegionId);
      List<ConstRegion> secondlist = new ArrayList<ConstRegion>();
      List<Long> superRegionIdList = new ArrayList<Long>();
      if (list != null) {
        for (ConstRegion cr : list) {
          superRegionIdList.add(cr.getId());
        }
        secondlist = constRegionDao.findBitchRegionData(superRegionIdList);
      }
      List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
      for (ConstRegion cr : list) {
        Map<String, String> map = new HashMap<String, String>();
        if (secondlist != null) {
          for (ConstRegion secondcr : secondlist) {
            if (cr.getId().equals(secondcr.getSuperRegionId())) {
              map.put("nextlevel", "true");
            }
          }
        }
        map.put("code", ObjectUtils.toString(cr.getId()));
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          map.put("name", StringUtils.isNotBlank(cr.getEnName()) ? cr.getEnName() : cr.getZhName());
        } else {
          map.put("name", StringUtils.isNotBlank(cr.getZhName()) ? cr.getZhName() : cr.getEnName());
        }
        /*
         * map.put("en_US_name", cr.getEnName()); map.put("zh_CN_seq", ObjectUtils.toString(cr.getZhSeq()));
         * map.put("en_US_seq", ObjectUtils.toString(cr.getEnSeq()));
         */
        resultList.add(map);
      }
      // 返回json数据，格式
      return JacksonUtils.listToJsonStr(resultList);
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Map<String, String>> findRegionList(Long superRegionId) throws Exception {
    List<ConstRegion> list = constRegionDao.findRegionData(superRegionId);
    List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
    for (ConstRegion constRegion : list) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("code", ObjectUtils.toString(constRegion.getId()));
      map.put("name", constRegion.getZhName());
      resultList.add(map);
    }
    return resultList;
  }

  // 通过region ID反过来获取地区
  @Override
  public Map<String, String> findDataByRegionId(Long superRegionId) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    ConstRegion constRegion = constRegionDao.findRegionNameById(superRegionId);
    if (constRegion != null) {
      map.put("firstRegionName", buildRegionNameByLanguage(constRegion));
      if (constRegion.getSuperRegionId() != null) {
        map.put("firstCode", String.valueOf(constRegion.getSuperRegionId()));
        constRegion = constRegionDao.findRegionNameById(constRegion.getSuperRegionId());
      } else {
        constRegion = null;
        map.put("firstRegionCode", String.valueOf(superRegionId));
      }
      if (constRegion != null) {
        map.put("secondRegionName", buildRegionNameByLanguage(constRegion));
        if (constRegion.getSuperRegionId() != null) {
          map.put("secondCode", String.valueOf(constRegion.getSuperRegionId()));
          constRegion = constRegionDao.findRegionNameById(constRegion.getSuperRegionId());
        } else {
          constRegion = null;
        }
        if (constRegion != null) {
          map.put("thirdRegionName", buildRegionNameByLanguage(constRegion));
        } else {
          map.put("thirdRegionName", null);
        }
      }

    } else {
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        map.put("firstRegionName", "China");
        map.put("firstRegionCode", "156");
      } else {
        map.put("firstRegionName", "中国");
        map.put("firstRegionCode", "156");
      }
    }
    return map;
  }

  /**
   * 地区国际化显示
   * 
   * @param reg
   * @return
   */
  private String buildRegionNameByLanguage(ConstRegion reg) {
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      return StringUtils.isNotBlank(reg.getEnName()) ? reg.getEnName() : reg.getZhName();
    } else {
      return StringUtils.isNotBlank(reg.getZhName()) ? reg.getZhName() : reg.getEnName();
    }
  }

  @Override
  public void autoRegionPrompt(PersonProfileForm form) throws Exception {
    List<Map<String, Object>> RegionNames =
        constRegionDao.getRegionNameList(form.getSearchKey(), form.getSuperRegionId());
    if (RegionNames != null && RegionNames.size() > 0) {
      JSONArray jsonArray = new JSONArray();
      if (XmlUtil.containZhChar(form.getSearchKey())) {
        for (Map<String, Object> name : RegionNames) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", name.get("code"));
          jsonObj.put("name", name.get("name"));
          jsonArray.add(jsonObj);
        }
      } else {
        for (Map<String, Object> name : RegionNames) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", name.get("code"));
          jsonObj.put("name", name.get("ename"));
          jsonArray.add(jsonObj);
        }
      }
      form.setData(jsonArray.toString());
    }
  }

  /**
   * 获取下一级是否存在
   */
  @Override
  public Map<String, String> findNextLevelRegionId(Long superRegionId) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    List<ConstRegion> constRegion = constRegionDao.findNextLevelRegion(superRegionId);
    if (CollectionUtils.isNotEmpty(constRegion)) {
      map.put("nextLevel", "true");
    } else {
      map.put("nextLevel", "false");
    }
    return map;
  }



}
