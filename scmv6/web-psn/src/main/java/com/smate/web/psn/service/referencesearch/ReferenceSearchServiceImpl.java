package com.smate.web.psn.service.referencesearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.psn.dao.pub.ConstRefDbDao;
import com.smate.web.psn.dao.pub.InsRefDbDao;
import com.smate.web.psn.model.pub.ConstRefDb;
import com.smate.web.psn.model.pub.ConstRefDbFrom;
import com.smate.web.psn.model.pub.InsRefDb;

/**
 * 更新引用用----------其他成果相关操作请去pub项目
 * 
 * @author Administrator
 *
 */
@Service("referenceSearchService")
@Transactional(rollbackFor = Exception.class)
public class ReferenceSearchServiceImpl implements ReferenceSearchService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private InsRefDbDao insRefDbDao;

  @Override
  public List<ConstRefDbFrom> getDbList(String dbType, List<Long> insIdList) {
    try {
      String allInsIds = ArrayUtils.toString(insIdList);
      logger.debug("====检索资源类型:{},人员所有单位:{}", dbType, allInsIds.substring(1, allInsIds.length() - 1));
      Locale locale = LocaleContextHolder.getLocale();
      List<ConstRefDbFrom> from2List = null;
      if (CollectionUtils.isEmpty(insIdList)) {
        List<ConstRefDb> list = new ArrayList<ConstRefDb>();
        List<ConstRefDb> constList = constRefDbDao.getPubRefDb(locale);
        if (CollectionUtils.isNotEmpty(constList)) {
          for (int i = 0; i < constList.size(); i++) {
            if (constList.get(i).getDbType().indexOf(dbType) >= 0)
              list.add(constList.get(i));
          }
        }
        logger.debug("====没有找到用户可用的单位insIdList，直接返回ConstRefDb表中的数据");
        return copyConstRefDbByFrom(list);
      } else {
        List<ConstRefDb> constRefDbList = constRefDbDao.getAllConstRefDb(locale);
        // copy
        List<ConstRefDbFrom> constRefDBFromList = copyConstRefDbByFrom(constRefDbList);

        List<InsRefDb> insRefDbList = insRefDbDao.getDbByIns(insIdList);

        if (CollectionUtils.isNotEmpty(constRefDBFromList) && CollectionUtils.isNotEmpty(insRefDbList)) {
          from2List = new ArrayList<ConstRefDbFrom>();
          int dbListLen = constRefDBFromList.size();
          int insDbListLen = insRefDbList.size();
          for (int i = 0; i < dbListLen; i++) {
            ConstRefDbFrom from = constRefDBFromList.get(i);
            for (int j = 0; j < insDbListLen; j++) {
              InsRefDb insRefDb = insRefDbList.get(j);
              if (from.getId().equals(insRefDb.getInsRefDbId().getDbId())) {
                from.setLoginUrl(insRefDb.getLoginUrl() == null ? "" : insRefDb.getLoginUrl());// 用于校外登录的url
                if (StringUtils.isNotBlank(insRefDb.getActionUrl()))
                  from.setActionUrl(insRefDb.getActionUrl());// 用于校外查询的url
                if (StringUtils.isNotBlank(insRefDb.getActionUrlInside()))
                  from.setActionUrlInside(insRefDb.getActionUrlInside());// 用于校内查询的url
                if (StringUtils.isNotBlank(insRefDb.getLoginUrlInside()))
                  from.setLoginUrlInside(insRefDb.getLoginUrlInside());// 用户进行登录验证的URL
                if (StringUtils.isNotBlank(insRefDb.getFulltextUrl()))
                  from.setFulltextUrl(insRefDb.getFulltextUrl());// 校外全文url的域名
                if (StringUtils.isNotBlank(insRefDb.getFulltextUrlInside()))
                  from.setFulltextUrlInside(insRefDb.getFulltextUrlInside());// 校内全文url的域名
                if (insRefDb.getEnabled() != null)
                  from.setIsPublic(insRefDb.getEnabled());// 是否公用
                // 0:不公用
                // 1:
                // 公用
                logger.debug("====根据用户单位配制的url对ConstRefDb进行重设,dbcode:{}", from.getCode());
                break;
              }
            }
            from2List.add(from);
          }
        }

        if (CollectionUtils.isEmpty(from2List)) {
          from2List = constRefDBFromList;
          logger.debug("====根据用户的所有单位insIdList:{},没有在InsRefDb表中找到数据,直接返回ConstRefDb表数据",
              allInsIds.substring(1, allInsIds.length() - 1));
        }

        if (StringUtils.isNotBlank(dbType) && CollectionUtils.isNotEmpty(from2List)) {
          for (int i = from2List.size() - 1; i >= 0; i--) {
            if (from2List.get(i).getIsPublic() != 1 || from2List.get(i).getDbType().indexOf(dbType) < 0)
              from2List.remove(i);
          }
        }
      }
      return from2List;
    } catch (SysServiceException e) {
      logger.error("获取可用查询数据列表时出错", e);
      return null;
    }
  }

  private List<ConstRefDbFrom> copyConstRefDbByFrom(List<ConstRefDb> constRefDbList) {
    if (CollectionUtils.isEmpty(constRefDbList))
      return null;
    List<ConstRefDbFrom> constRefDBFromList = new ArrayList<ConstRefDbFrom>();
    for (ConstRefDb constRefDb : constRefDbList) {
      ConstRefDbFrom from = new ConstRefDbFrom();
      from.setId(constRefDb.getId());
      from.setActionUrl(constRefDb.getActionUrl() == null ? "" : constRefDb.getActionUrl());
      from.setActionUrlInside(constRefDb.getActionUrlInside() == null ? "" : constRefDb.getActionUrlInside());
      from.setCode(constRefDb.getCode());
      from.setDbBitCode(constRefDb.getDbBitCode());
      from.setEnSortKey(constRefDb.getEnSortKey());
      from.setIsPublic(constRefDb.getIsPublic());
      from.setDbName(constRefDb.getDbName() == null ? "" : constRefDb.getDbName());
      from.setDbType(constRefDb.getDbType() == null ? "" : constRefDb.getDbType());
      from.setEnAbbrName(constRefDb.getEnAbbrName() == null ? "" : constRefDb.getEnAbbrName());
      from.setEnUsName(constRefDb.getEnUsName() == null ? "" : constRefDb.getEnUsName());
      from.setFulltextUrl(constRefDb.getFulltextUrl() == null ? "" : constRefDb.getFulltextUrl());
      from.setFulltextUrlInside(constRefDb.getFulltextUrlInside() == null ? "" : constRefDb.getFulltextUrlInside());
      from.setLoginUrl(constRefDb.getLoginUrl() == null ? "" : constRefDb.getLoginUrl());
      from.setLoginUrlInside(constRefDb.getLoginUrlInside() == null ? "" : constRefDb.getLoginUrlInside());
      from.setSuportLang(constRefDb.getSuportLang() == null ? "" : constRefDb.getSuportLang());
      from.setZhAbbrName(constRefDb.getZhAbbrName() == null ? "" : constRefDb.getZhAbbrName());
      from.setZhCnName(constRefDb.getZhCnName() == null ? "" : constRefDb.getZhCnName());
      from.setZhSortKey(constRefDb.getZhSortKey());
      from.setBatchQuery(constRefDb.getBatchQuery() == null ? "" : constRefDb.getBatchQuery());
      constRefDBFromList.add(from);
    }
    return constRefDBFromList;
  }

  /**
   * 以json格式字符串返回文献库信息列表.
   */
  @Override
  public String getDbUrl(List<ConstRefDbFrom> dbList) {

    Locale local = LocaleContextHolder.getLocale();
    if (CollectionUtils.isNotEmpty(dbList)) {
      for (ConstRefDbFrom constRefDb : dbList) {
        if (local.equals(Locale.US)) {
          constRefDb.setDbName(constRefDb.getEnAbbrName());
        } else {
          constRefDb.setDbName(constRefDb.getZhAbbrName());
        }
      }
    }
    StringBuilder sb = new StringBuilder("{'default':''");
    if (dbList != null && dbList.size() > 0) {
      int dbListLen = dbList.size();
      for (int i = 0; i < dbListLen; i++) {
        sb.append(",'" + dbList.get(i).getCode() + "':");
        sb.append(JacksonUtils.jsonObjectSerializer(dbList.get(i)).toString());

      }
    }
    sb.append("}");
    return sb.toString();

  }

}
