package com.smate.sie.center.task.pdwh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.model.PrjDupParam;
import com.smate.sie.core.base.utils.dao.prj.SiePrjDupFieldsDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjFromConstDao;
import com.smate.sie.core.base.utils.dao.prj.SiePrjSchemeConstDao;
import com.smate.sie.core.base.utils.json.prj.ProjectJsonDTO;
import com.smate.sie.core.base.utils.model.prj.SiePrjFromConst;
import com.smate.sie.core.base.utils.model.prj.SiePrjSchemeConst;

/**
 * 
 * @author yxs
 * @descript 查重接口
 */
@Service("sieProjectDupService")
@Transactional(rollbackFor = Exception.class)
public class SieProjectDupServiceImpl implements SieProjectDupService {

  @Autowired
  private SiePrjDupFieldsDao sieProjectDupDao;
  @Autowired
  private SiePrjSchemeConstDao prjSchemeConstDao;
  @Autowired
  private SiePrjFromConstDao prjFromConstDao;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private List<Map<String, Object>> cache = new ArrayList<Map<String, Object>>();

  /**
   * 列出有相同标题的一个项目.
   * 
   * @param zhTitle
   * @param enTitle
   * @return
   */
  @Override
  public List<Long> getDupPrjId(String zhTitle, String enTitle, Long ownerInsId, String externalNo,
      String prjFromName) {
    Integer zh_ext_Hash = getUniHash(zhTitle, externalNo);
    Integer en_ext_Hash = getUniHash(enTitle, externalNo);
    if (zh_ext_Hash != null || en_ext_Hash != null) {
      return sieProjectDupDao.getDupPrjId(zh_ext_Hash, en_ext_Hash, ownerInsId);
    }
    Integer zh_fro_Hash = getUniHash(zhTitle, prjFromName);
    Integer en_fro_Hash = getUniHash(enTitle, prjFromName);
    if (zh_fro_Hash != null || en_fro_Hash != null) {
      return sieProjectDupDao.getDupPrjId2(zh_fro_Hash, en_fro_Hash, ownerInsId);
    }
    return null;
  }

  private Integer getUniHash(String name, String other) {
    if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(other)) {
      return SieProjectHash.dirtyTitleHash(name + other);
    }
    return null;
  }

  /**
   * 根据项目数据和单位查出重复的项目主键
   */
  @Override
  public Map<Integer, List<Long>> getDupPrjByImportPrj(ProjectJsonDTO prj, Long ownerId) throws SysServiceException {
    try {
      PrjDupParam param = buildImportPrjDupParam(prj);
      return this.getDupPrj(param, ownerId);
    } catch (Exception e) {
      logger.error("getDupPrjByImportPrj项目查重", e);
      throw new SysServiceException("getDupPrjByImportPrj项目查重", e);
    }
  }

  /**
   * 构建需要查重的内容
   */
  private PrjDupParam buildImportPrjDupParam(ProjectJsonDTO prj) {
    String zhTitle = prj.title;
    String enTitle = prj.title;
    String externalNo = StringUtils.trimToEmpty(prj.prjExterNo);
    String prjFromName = "";
    if (prj.dataFrom == 2) {
      prj.sourceDbCode = "SCMISIS";
      prjFromName = prj.sourceDbCode;
    } else if (prj.dataFrom == 1) {
      prj.sourceDbCode = "SCMEXCEL";
      prjFromName = buildSchemeCacheData(prj);
    }
    PrjDupParam param = new PrjDupParam();
    param.setZhTitle(zhTitle);
    param.setEnTitle(enTitle);
    param.setExternalNo(externalNo);
    param.setPrjFromName(prjFromName);
    return param;
  }

  /**
   * 匹配excel导入的项目类别和项目来源
   * 
   * @param schemeName
   * @return
   */
  private String buildSchemeCacheData(ProjectJsonDTO prj) { // 民口973(国家973计划)
    Map<String, Object> map = new HashMap<String, Object>();
    String schemeName = prj.schemeName;
    String schemeId = "";
    List<Map> list = prjSchemeConstDao.getSchemeAndFrom();
    if (CollectionUtils.isEmpty(cache)) {
      for (Map m : list) {
        String uniStr = m.get("SCHEME_NAME") + "(" + m.get("NAME_ZH") + ")";
        if (schemeName.equals(uniStr)) {
          schemeId = m.get("SCHEME_ID").toString();
        }
        map.put(uniStr, m.get("SCHEME_ID"));
        cache.add(m);
      }

    } else {
      List<Map<String, Object>> temp = (List<Map<String, Object>>) cache;
      for (Map<String, Object> map2 : temp) {
        if (schemeName.equals(map2.get("SCHEME_NAME") + "(" + map2.get("NAME_ZH") + ")")) {
          schemeId = map2.get("SCHEME_ID").toString();
          break;
        }
      }
    }
    if ("".equals(schemeId)) {
      return "";
    }
    SiePrjSchemeConst schemeConst = prjSchemeConstDao.get(Long.valueOf(schemeId));
    SiePrjFromConst fromConst = prjFromConstDao.get(schemeConst.getPrjFromId());
    return fromConst.getZhName();
  }

  /**
   * 根据bean和insid获取重复项目Id
   */
  private Map<Integer, List<Long>> getDupPrj(PrjDupParam param, Long insId) throws SysServiceException {
    String zhTitle = param.getZhTitle();
    String enTitle = param.getEnTitle();
    String externalNo = param.getExternalNo();
    String prjFromName = param.getPrjFromName();
    Integer zh_ext_Hash = getUniHash(zhTitle, externalNo);
    Integer en_ext_Hash = getUniHash(enTitle, externalNo);
    Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
    if (zh_ext_Hash != null || en_ext_Hash != null) {
      map.put(1, sieProjectDupDao.getDupPrjId(zh_ext_Hash, en_ext_Hash, insId));
    }
    Integer zh_fro_Hash = getUniHash(zhTitle, prjFromName);
    Integer en_fro_Hash = getUniHash(enTitle, prjFromName);
    if (zh_fro_Hash != null || en_fro_Hash != null) {
      map.put(1, sieProjectDupDao.getDupPrjId2(zh_fro_Hash, en_fro_Hash, insId));
    }
    return map;
  }

  public List<Map<String, Object>> getCache() {
    return cache;
  }

  public void setCache(List<Map<String, Object>> cache) {
    this.cache = cache;
  }
}
