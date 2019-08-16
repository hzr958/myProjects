package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.ConstDictionaryCbDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.ConstDictionaryForm;
import com.smate.core.base.utils.constant.ConstCategory;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.constant.ConstDictionaryKey;

/**
 * @author Administrator
 * 
 */
@Service("constDictionaryCbManage")
@Transactional(rollbackFor = Exception.class)
public class ConstDictionaryManageCbImpl implements ConstDictionaryManageCb, Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 8515313172339801578L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   */
  @Autowired
  private ConstDictionaryCbDao constDictionaryDao;
  /*
   * @Autowired private ConstCategoryService constCategoryService;
   */

  /**
   * @param constDictionaryDao the constDictionaryDao to set
   */
  public void setConstDictionaryDao(ConstDictionaryCbDao constDictionaryDao) {
    this.constDictionaryDao = constDictionaryDao;
  }

  /**
   * @return the constDictionaryDao
   */
  public ConstDictionaryCbDao getConstDictionaryDao() {
    return constDictionaryDao;
  }

  @Override
  public List<ConstDictionary> getConstByGategory(String gategory) throws ServiceException {
    return constDictionaryDao.findConstByCategory(gategory);
  }

  @Override
  public Map<Long, String> getConstGategory(String gategory) throws ServiceException {
    Map<Long, String> constStatus = new HashMap<Long, String>();
    Locale local = LocaleContextHolder.getLocale();
    List<ConstDictionary> constdict = constDictionaryDao.findConstByCategory(gategory);
    for (ConstDictionary cons : constdict) {
      if (local.equals(Locale.US)) {
        constStatus.put(Long.parseLong(cons.getKey().getCode()), cons.getEnUsName());
      } else if (local.equals(Locale.TAIWAN)) {
        constStatus.put(Long.parseLong(cons.getKey().getCode()), cons.getZhTwName());
      } else {
        constStatus.put(Long.parseLong(cons.getKey().getCode()), cons.getZhCnName());
      }
    }
    return constStatus;
  }

  @Override
  public Map<String, String> getConstGategoryForFM(String gategory, String codes) throws ServiceException {
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
  public ConstDictionary findConstByCategoryAndCode(String category, String code) throws ServiceException {
    if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(category)) {
      return this.constDictionaryDao.findConstByCategoryAndCode(category, code);
    }
    return null;
  }

  @Override
  public String findConstName(String category, String code, Locale locale) throws ServiceException {
    ConstDictionary cd = this.findConstByCategoryAndCode(category, code);
    if (cd != null) {
      if (Locale.US.equals(locale)) {
        return cd.getEnUsName();
      } else {
        return cd.getZhCnName();
      }
    }
    return null;
  }

  @Override
  public ConstDictionary saveConstDictionary(ConstDictionary constDictionary) {

    constDictionaryDao.save(constDictionary);
    return constDictionary;
  }

  /*	*//**
         * 保存添加常量到数据库.
         * 
         * @param form
         * @return
         * @throws ServiceException
         *//*
            * @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
            * 
            * @SuppressWarnings("unchecked") private List<ConstDictionary>
            * doSaveAddConstDictionary(ConstDictionaryForm form) throws ServiceException {
            * 
            * try {
            * 
            * String category = form.getCategory(); String description = form.getDescription(); // 保存常量类别
            * ConstCategory constCategory = constCategoryService.saveConstCategory(new ConstCategory(category,
            * description)); String strDictionary = form.getDictionary(); String[] strDictionarys =
            * strDictionary.split("_\\|_"); List<ConstDictionary> list = new ArrayList<ConstDictionary>(); for
            * (String dic : strDictionarys) { // 解析 JSON数据 HashMap<String, String> mapDic = (HashMap<String,
            * String>) JSONObject.toBean( JSONObject.fromObject(dic), HashMap.class); String code =
            * mapDic.get("code"); String enDesc = mapDic.get("enDesc"); String cnDesc = mapDic.get("cnDesc");
            * String twDesc = mapDic.get("twDesc"); String seqNo = mapDic.get("seqNo"); ConstDictionary
            * dictionary = new ConstDictionary(category, code, enDesc, cnDesc, twDesc, seqNo); // 保存常量
            * this.constDictionaryDao.save(dictionary); dictionary.setConstCategory(constCategory);
            * list.add(dictionary); } return list; } catch (Exception e) { String jobj =
            * JSONObject.fromObject(form).toString(); logger.error("保存新添加常量错误form:{}", jobj, e); throw new
            * ServiceException(e); } }
            * 
            * @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) private
            * ConstDictionary doSaveEditConstDictionary(ConstDictionaryForm form) throws ServiceException {
            * 
            * String category = form.getCategory(); ConstCategory constCategory =
            * constCategoryService.getConstCategory(category); if (constCategory == null) { throw new
            * ServiceException( String.format("常量不存在code:%s,category:%s", form.getCode(), form.getCategory()));
            * } ConstDictionary dictionary = this.constDictionaryDao.findConstByCategoryAndCode(category,
            * form.getCode()); if (dictionary == null) { dictionary = new ConstDictionary(); ConstDictionaryKey
            * key = new ConstDictionaryKey(category, form.getCode()); dictionary.setKey(key);
            * dictionary.setEditFlag(false); } else { dictionary.setEditFlag(true); }
            * dictionary.setEnUsName(form.getEnName()); dictionary.setZhCnName(form.getCnName());
            * dictionary.setZhTwName(form.getTwName()); dictionary.setSeqNo(form.getSeqNo());
            * this.constDictionaryDao.save(dictionary); dictionary.setConstCategory(constCategory); return
            * dictionary; }
            */
  @Override
  public void removeConstDictionary(String category, String code) throws ServiceException {

    this.constDictionaryDao.removeConstDictionary(category, code);
  }

  @Override
  public void pullConstDictionarySyn(List<ConstDictionary> list) throws ServiceException {

    this.constDictionaryDao.removeAll();
    for (ConstDictionary constDictionary : list) {
      this.constDictionaryDao.save(constDictionary);
    }
  }

  @Override
  public ConstDictionary findConstByCategoryAndName(String category, String name) throws ServiceException {

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
  public String findCodeByName(String category, String name) throws ServiceException {
    try {

      if (StringUtils.isBlank(name))
        return null;
      return this.constDictionaryDao.findCodeByName(category, name);
    } catch (Exception e) {
      logger.error("查找code通过名称.", e);
      throw new ServiceException("查找code通过名称.", e);
    }
  }

  @Override
  public Map<String, ConstDictionary> findConstByCategoryAndCodes(String category, String codes)
      throws ServiceException {
    List<ConstDictionary> constDictionaryList = null;
    Map<String, ConstDictionary> constDictionaryMap = new HashMap<String, ConstDictionary>();
    constDictionaryList = this.constDictionaryDao.findConstByCategoryAndCodes(category, codes);
    for (ConstDictionary cd : constDictionaryList) {
      constDictionaryMap.put(cd.getKey().getCode(), cd);
    }
    return constDictionaryMap;
  }
}
