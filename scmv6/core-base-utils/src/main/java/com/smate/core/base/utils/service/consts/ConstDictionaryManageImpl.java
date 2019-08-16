package com.smate.core.base.utils.service.consts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.dao.consts.ConstDictionaryDao;

/**
 * @author Administrator
 * 
 */
@Service("constDictionaryManage")
@Transactional(rollbackFor = Exception.class)
public class ConstDictionaryManageImpl implements ConstDictionaryManage, Serializable {

  private static final long serialVersionUID = 6741081689110236328L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstDictionaryDao constDictionaryDao;

  @Override
  public Map<String, ConstDictionary> findConstByCategoryAndCodes(String category, String codes) throws Exception {
    List<ConstDictionary> constDictionaryList = null;
    Map<String, ConstDictionary> constDictionaryMap = new HashMap<String, ConstDictionary>();
    try {
      constDictionaryList = this.constDictionaryDao.findConstByCategoryAndCodes(category, codes);
    } catch (Exception e) {
      logger.error("查找code通过名称.", e);
      throw new Exception("查找code通过名称.", e);
    }
    for (ConstDictionary cd : constDictionaryList) {
      constDictionaryMap.put(cd.getKey().getCode(), cd);
    }
    return constDictionaryMap;
  }

  @Override
  public ConstDictionary findConstByCategoryAndCode(String category, String code) throws Exception {
    try {
      if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(category)) {
        return this.constDictionaryDao.findConstByCategoryAndCode(category, code);
      }
      return null;
    } catch (Exception e) {
      logger.error("读取常量出错:category={},code={}", new Object[] {category, code, e});
      throw new Exception(e);
    }
  }

  @Override
  public ConstDictionary findConstByCategoryAndName(String category, String name) throws Exception {

    if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(category)) {
      List<ConstDictionary> list = this.getConstByGategory(category);
      if (list != null && list.size() > 0) {
        for (ConstDictionary cd : list) {
          String zhName = cd.getZhCnName();
          if (name.equalsIgnoreCase(zhName)) {
            return cd;
          }
          String twName = cd.getZhTwName();
          if (name.equalsIgnoreCase(twName)) {
            return cd;
          }
          String enName = cd.getEnUsName();
          if (name.equalsIgnoreCase(enName)) {
            return cd;
          }
        }
      }
    }

    return null;
  }

  @Override
  public List<ConstDictionary> getConstByGategory(String gategory) {
    return constDictionaryDao.findConstByCategory(gategory);
  }

  @Override
  public Map<String, String> getConstGategoryForFM(String gategory, String codes) throws Exception {
    Map<String, String> constStatus = new LinkedHashMap<String, String>();
    Locale local = LocaleContextHolder.getLocale();
    List<ConstDictionary> constdict = constDictionaryDao.findConstByCategoryAndCodes(gategory, codes);
    for (ConstDictionary cons : constdict) {
      if (local.equals(Locale.US)) {
        constStatus.put(cons.getKey().getCode(), cons.getEnUsName());
      } else if (local.equals(Locale.TAIWAN)) {
        constStatus.put(cons.getKey().getCode(), cons.getZhTwName());
      } else {
        constStatus.put(cons.getKey().getCode(), cons.getZhCnName());
      }
    }
    return constStatus;
  }

  @Override
  public List<ConstDictionary> getConstByCategory(String category) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
