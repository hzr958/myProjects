package com.smate.web.psn.service.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.psn.dao.profile.PsnAliasDao;
import com.smate.web.psn.dao.pub.ConstRefDbDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.profile.PsnAlias;
import com.smate.web.psn.model.pub.ConstRefDb;

/**
 * @author zk
 * 
 */
@Service("psnAliasService")
@Transactional(rollbackFor = Exception.class)
public class PsnAliasServiceImpl implements PsnAliasService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnAliasDao psnAliasDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;

  @Override
  @SuppressWarnings({"rawtypes"})
  public List<Map> getAllPsnAliasByPsnDb(Long psnId, String psnName, String dbCode) throws ServiceException {
    Long dbId = 2L;// isi三库合并
    if ("SCIE".equalsIgnoreCase(dbCode) || "SSCI".equalsIgnoreCase(dbCode) || "ISTP".equalsIgnoreCase(dbCode)) {
      dbId = 2L;
    } else {
      ConstRefDb constRefDb = this.constRefDbDao.findUniqueBy("code", dbCode.toUpperCase());
      dbId = constRefDb.getId();
    }
    List<Map> listPsnAlias = null;
    try {
      psnName = psnName.replace(" ", "");
      listPsnAlias = this.psnAliasDao.findPsnAliasFiled(psnId, psnName.toLowerCase(), dbId);
    } catch (Exception e) {
      logger.error("PsnAlias数据取出有错");
      throw new ServiceException(e);
    }
    return listPsnAlias;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String getAllPsnAliasToJson(Long psnId, String psnName, String dbCodes) throws ServiceException {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("result", "success");
    String[] arrdbCode = dbCodes.split(",");
    psnName = psnName.replace(" ", "");
    for (String dbCode : arrdbCode) {
      if ("SCIE".equalsIgnoreCase(dbCode)) {
        List<Map> psnDbList = this.getAllPsnAliasByPsnDb(psnId, psnName, dbCode);
        if (psnDbList != null && psnDbList.size() > 0) {
          returnMap.put("SCIE", psnDbList);
        }
        continue;
      } else if ("SSCI".equalsIgnoreCase(dbCode)) {
        List<Map> psnDbList = this.getAllPsnAliasByPsnDb(psnId, psnName, dbCode);
        if (psnDbList != null && psnDbList.size() > 0) {
          returnMap.put("SSCI", psnDbList);
        }
        continue;
      } else if ("ISTP".equalsIgnoreCase(dbCode)) {
        List<Map> psnDbList = this.getAllPsnAliasByPsnDb(psnId, psnName, dbCode);
        if (psnDbList != null && psnDbList.size() > 0) {
          returnMap.put("ISTP", psnDbList);
        }
        continue;
      }

      else {
        continue;
      }
    }
    return JacksonUtils.jsonObjectSerializer(returnMap);
  }

  @Override
  public String saveOrDeletePsnAlias(Long psnId, String psnName, String dbCode, String psnAliasNames)
      throws ServiceException {
    try {
      psnName = psnName.replace(" ", "");
      Long dbId = 2L;// isi三库合并
      if ("SCIE".equalsIgnoreCase(dbCode) || "SCI".equalsIgnoreCase(dbCode) || "SSCI".equalsIgnoreCase(dbCode) || "ISTP".equalsIgnoreCase(dbCode)) {
        dbId = 2L;
      } else {
        ConstRefDb constRefDb = this.constRefDbDao.findUniqueBy("code", dbCode.toUpperCase());
        dbId = constRefDb.getId();
      }
      String name = null;
      Long hashName = null;
      List<Map<String, String>> actionList = JacksonUtils.jsonListUnSerializer(psnAliasNames);
      List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
      Long[] allIsiDbids = getDbIdByDbCode("ISTP", "SSCI", "SCI", "SCIE");
      int psnAliasNum = psnAliasDao.getPsnAliasInDbIdList(psnId, psnName.toLowerCase(), allIsiDbids).size();// 获取该人员在isi所有排除总数
      Map<String, Object> resultMap = null;
      for (Map<String, String> actionMap : actionList) {
        switch (Integer.valueOf(actionMap.get("ACTION"))) {
          case 0:
            if (psnAliasNum < 100) {// 每一个人最多对指定文献库进行排除100条SCM-22355
              name = actionMap.get("NAME");
              hashName = HashUtils.getStrHashCode(name);
              PsnAlias psnAlias = new PsnAlias(psnId, dbId, psnName.toLowerCase(), name, hashName, 1, new Date());
              psnAliasDao.save(psnAlias);
              resultMap = new HashMap<String, Object>();
              resultMap.put("ID", psnAlias.getId());
              resultMap.put("NAME", name);
              resultMap.put("ACTION", 0);
              psnAliasNum++;
            }
            break;
          case 1:
            Object idObj = actionMap.get("ID");
            psnAliasDao.delete(Long.valueOf(idObj.toString()));
            resultMap = new HashMap<String, Object>();
            resultMap.put("ID", actionMap.get("ID"));
            resultMap.put("NAME", actionMap.get("NAME"));
            resultMap.put("ACTION", 1);
            psnAliasNum--;
            break;
          default:
            break;
        }
        resultList.add(resultMap);
      }
      return JacksonUtils.listToJsonStr(resultList);
    } catch (Exception e) {
      logger.error("别名保存出错了", e);
      return "";
    }
  }

  /**
   * 通过Dbcode获取dbId
   * 
   * @param dbCode
   * @return
   */
  private Long[] getDbIdByDbCode(String... dbCodes) {
    Long[] dbIds = new Long[dbCodes.length];
    for (int i = 0; i < dbCodes.length; i++) {
      Long dbId = 2L;// isi三库合并
      if ("SCIE".equalsIgnoreCase(dbCodes[i]) || "SCI".equalsIgnoreCase(dbCodes[i])
          || "SSCI".equalsIgnoreCase(dbCodes[i]) || "ISTP".equalsIgnoreCase(dbCodes[i])) {
        dbId = 2L;
      } else {
        ConstRefDb constRefDb = this.constRefDbDao.findUniqueBy("code", dbCodes[i].toUpperCase());
        dbId = constRefDb.getId();
      }
      dbIds[i] = dbId;
    }
    return dbIds;
  }
}
