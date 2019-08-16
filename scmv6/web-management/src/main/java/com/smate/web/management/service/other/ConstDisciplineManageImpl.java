package com.smate.web.management.service.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.ConstDisciplineDao;
import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.management.dao.other.fund.CategoryMapBaseDao;
import com.smate.web.management.dao.other.fund.ConstDisciplineKeyDao;
import com.smate.web.management.model.other.fund.CategoryMapBase;
import com.smate.web.management.model.other.fund.ConstDisciplineKey;

/**
 * 
 * @author liqinghua
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service(value = "constDisciplineManage")
public class ConstDisciplineManageImpl implements ConstDisciplineManage {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstDisciplineDao constDisciplineDao;

  @Autowired
  private ConstDisciplineKeyDao constDisciplineKeyDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;

  private List<Long> disIds = null;// 所有的子节点id

  /**
   * 获取所有学科列表.
   * 
   * @param lang
   * @return
   * @throws ServiceException
   */
  public List<ConstDiscipline> findAll() throws ServiceException {
    try {
      List<ConstDiscipline> superList = constDisciplineDao.getSuperConstDiscipline();
      // for (ConstDiscipline sp : superList) {
      // sp.setConstDisciplineSub(constDisciplineDao.getSubConstDiscipline(sp.getId()));
      // }
      return superList;
    } catch (DAOException e) {
      logger.error("读取专长学科出现错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取学科下拉树JSON，一级.
   * 
   * @return
   * @throws ServiceException
   */
  /*
   * @SuppressWarnings("unchecked") public String getDiscTreeJson(Locale... locales) throws
   * ServiceException {
   * 
   * try { Locale locale = null; if (locales.length == 1) { locale = locales[0]; } else { locale =
   * LocaleContextHolder.getLocale(); }
   * 
   * List<ConstDiscipline> all = this.findAll(); List<Map> node = this.covertInsUnitTree(all, true,
   * locale); JSONArray jsonstr = JSONArray.fromObject(node); return jsonstr.toString(); } catch
   * (Exception e) { logger.error("获取学科下拉树出现错误.", e); throw new ServiceException(e); } }
   */

  /**
   * 获取学科下拉树JSON，二级,三级...，需要通过id获取.
   * 
   * @return
   * @throws ServiceException
   */
  /*
   * @SuppressWarnings("unchecked") public String getDiscTreeJsonBySub(Long id, Locale... locales)
   * throws ServiceException {
   * 
   * try { Locale locale = null; if (locales.length == 1) { locale = locales[0]; } else { locale =
   * LocaleContextHolder.getLocale(); }
   * 
   * List<ConstDiscipline> all = constDisciplineDao.getSubConstDiscipline(id); List<Map> node =
   * this.covertInsUnitTree(all, false, locale); JSONArray jsonstr = JSONArray.fromObject(node);
   * return jsonstr.toString(); } catch (Exception e) { logger.error("获取学科下拉树出现错误.", e); throw new
   * ServiceException(e); } }
   */

  /**
   * 将课题数据转换成JSON下拉树需要的格式.
   * 
   * @param list
   * @param locale
   * @return
   */
  /*
   * @SuppressWarnings("unchecked") public List<Map> covertInsUnitTree(List<ConstDiscipline> list,
   * boolean needTop, Locale locale) { if (list != null && list.size() > 0) {
   * 
   * List<Map> lmap = new ArrayList<Map>();
   * 
   * if (needTop) { // 构造选择学科 Map<String, Object> selnode = new HashMap<String, Object>(); Map<String,
   * Object> selnodeAttributes = new HashMap<String, Object>(); Map<String, Object> selnodeData = new
   * HashMap<String, Object>(); // 顶层属性attributes selnodeAttributes.put("id", ""); // data属性 if
   * (locale.equals(Locale.US)) { // selnodeData.put("title", "Select a Discipline");
   * selnodeData.put("title", ""); } else { // ljj 去掉学科代码选择列表中显示的“选择学科”四个字 // selnodeData.put("title",
   * "选择学科"); selnodeData.put("title", ""); } selnodeData.put("attributes", selnodeAttributes);
   * selnode.put("attributes", selnodeAttributes); selnode.put("data", selnodeData);
   * lmap.add(selnode); }
   * 
   * for (ConstDiscipline disc : list) { Map<String, Object> node = new HashMap<String, Object>();
   * Map<String, Object> nodeAttributes = new HashMap<String, Object>(); Map<String, Object> nodeData
   * = new HashMap<String, Object>(); // 顶层属性attributes nodeAttributes.put("id", disc.getId()); //
   * nodeAttributes.put("isSubItem", disc.getIsSubItem()); // data属性 if (locale.equals(Locale.US)) {
   * nodeData.put("title", disc.getDiscCode() + "-" + disc.getEnName()); } else {
   * nodeData.put("title", disc.getDiscCode() + "-" + disc.getZhName()); } nodeData.put("attributes",
   * nodeAttributes); node.put("attributes", nodeAttributes); node.put("data", nodeData); if
   * ("1".equals(disc.getIsSubItem())) { node.put("state", "closed"); }
   * 
   * // 子节点获取 // List<ConstDiscipline> subList = disc.getConstDisciplineSub(); // if (subList != null
   * && subList.size() > 0) { // node.put("children", covertInsUnitTree(subList, false, // locale));
   * // } lmap.add(node); } return lmap; } return null;
   * 
   * }
   */

  @Override
  public void pullConstDisciplineSyn(List<ConstDiscipline> list) throws ServiceException {

    try {
      this.constDisciplineDao.removeAll();
      for (ConstDiscipline constDiscipline : list) {
        this.constDisciplineDao.save(constDiscipline);
      }
    } catch (DAOException e) {
      logger.error("pullConstDisciplineSyn接收个人专长学科同步错误.size:{}", list.size(), e);
      throw new ServiceException("pullConstDisciplineSyn接收个人专长学科同步错误.", e);
    }

  }

  @Override
  public List<ConstDisciplineKey> findAutoCdKey(String keyWords, String exclude, int size) throws ServiceException {
    try {
      return constDisciplineKeyDao.findAutoCdKey(keyWords, exclude, size);
    } catch (Exception e) {
      logger.error("关键字匹配关键字列表错误.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public ConstDisciplineKey findCdKeyByKeyWords(String keyWords) throws ServiceException {
    try {
      return constDisciplineKeyDao.findCdKeyByKeyWords(keyWords);
    } catch (Exception e) {
      logger.error("获取关键字错误.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getDisciplineName(Long id, Locale locale) throws ServiceException {
    try {
      ConstDiscipline cd = this.constDisciplineDao.getConstDisciplineById(id);
      if (cd != null) {
        if (Locale.US.equals(locale)) {
          return cd.getDiscCode() + "-" + cd.getEnName();
        } else {
          return cd.getDiscCode() + "-" + cd.getZhName();
        }
      }
      return null;
    } catch (Exception e) {
      logger.error("获取学科领域名称错误.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getIdByCode(String code) throws ServiceException {
    try {
      return this.constDisciplineDao.getIdByCode(code);
    } catch (Exception e) {
      logger.error("通过CODE获取ID错误.", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 学科代码修改同步处理.
   */
  @Override
  public void syncUpdateDiscipline(Long disId, String name, int zhOrEn) throws ServiceException {
    this.constDisciplineDao.updateConstDisciplineById(disId, name, zhOrEn);
  }

  /**
   * 根据superId获取它所有的子节点Id.这是个递归方法
   */

  public List<Long> getChildId(Long superId) throws ServiceException {
    List<ConstDiscipline> constDisciplines = this.constDisciplineDao.getSubConstDiscipline(superId);
    if (constDisciplines != null) {
      for (ConstDiscipline dis : constDisciplines) {
        disIds.add(dis.getId());
        getChildId(dis.getId());
      }
    }

    return disIds;
  }

  /**
   * 根据superId获取它所有的子节点Id.
   */
  @Override
  public List<Long> getAllChild(Long superId) throws ServiceException {
    disIds = new ArrayList<Long>();
    disIds = getChildId(superId);
    return disIds;
  }

  /**
   * 根据学科Id得到相关的学科
   */
  @Override
  public ConstDiscipline findDisciplineById(Long id) throws ServiceException {

    return this.constDisciplineDao.get(id);
  }

  @Override
  public Long getIdByDiscCode(String discCode) throws ServiceException {
    try {
      return this.constDisciplineDao.getIdByDiscCode(discCode);
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getDisCodeById(Long id) throws ServiceException {

    return this.constDisciplineDao.getDisCodeById(id);
  }

  // 通过学科代码查询学科领域JSON数据.
  @Override
  public String findDiscJsonData(String discCode) throws ServiceException {
    try {
      String splitDiscCode = ObjectUtils.toString(discCode);

      List<String> searchKey = new ArrayList<String>();
      // 根据学科代码长度进行拆分
      switch (splitDiscCode.length()) {
        case 1:// 当前为1级学科代码，需要查询1级和2级的学科代码
          searchKey.add(null);
          searchKey.add(splitDiscCode.substring(0, 1));
          break;
        case 3:// 当前为2级学科代码，需要查询1级、2级和3级的学科代码
          searchKey.add(null);
          searchKey.add(splitDiscCode.substring(0, 1));
          searchKey.add(splitDiscCode.substring(0, 3));
          break;
        case 5:// 当前为3级学科代码，需要查询1级、2级、3级和4级的学科代码
          searchKey.add(null);
          searchKey.add(splitDiscCode.substring(0, 1));
          searchKey.add(splitDiscCode.substring(0, 3));
          searchKey.add(splitDiscCode.substring(0, 5));
          break;
        case 7:// 当前为4级学科代码，无需查询
          break;
        default:// 查询1级学科代码
          searchKey.add(null);
          break;
      }

      //
      Map<String, List<Map<String, String>>> allData = new HashMap<String, List<Map<String, String>>>();

      for (String key : searchKey) {
        List<ConstDiscipline> list = constDisciplineDao.findDiscData(key);
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (ConstDiscipline cd : list) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("id", ObjectUtils.toString(cd.getId()));
          map.put("zh_CN_name", cd.getZhName());
          map.put("en_US_name", cd.getEnName());
          map.put("disc_code", cd.getDiscCode());
          resultList.add(map);
        }
        allData.put("discipline_code_" + (key == null ? 1 : (key.length() + 3) / 2), resultList);
      }

      // 返回json数据，格式
      return JacksonUtils.mapToJsonStr(allData);
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }

  }

  // 通过学科代码查询学科领域JSON数据.
  @Override
  public String bpoFindDiscJsonData(String discCode) throws ServiceException {
    try {
      String splitDiscCode = ObjectUtils.toString(discCode);

      List<String> searchKey = new ArrayList<String>();
      // 根据学科代码长度进行拆分
      switch (splitDiscCode.length()) {
        case 1:// 当前为1级学科代码，需要查询1级和2级的学科代码
          searchKey.add(null);
          searchKey.add(splitDiscCode.substring(0, 1));
          break;
        case 3:// 当前为2级学科代码，需要查询1级、2级和3级的学科代码
          searchKey.add(null);
          searchKey.add(splitDiscCode.substring(0, 1));
          searchKey.add(splitDiscCode.substring(0, 3));
          break;
        /*
         * case 5:// 当前为3级学科代码，需要查询1级、2级、3级和4级的学科代码 searchKey.add(null);
         * searchKey.add(splitDiscCode.substring(0, 1)); searchKey.add(splitDiscCode.substring(0, 3));
         * searchKey.add(splitDiscCode.substring(0, 5)); break;
         */
        case 7:// 当前为4级学科代码，无需查询
          break;
        default:// 查询1级学科代码
          searchKey.add(null);
          break;
      }

      //
      Map<String, List<Map<String, String>>> allData = new HashMap<String, List<Map<String, String>>>();

      for (String key : searchKey) {
        List<CategoryMapBase> list = categoryMapBaseDao.getCategoryMapBaseList(key);
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (CategoryMapBase cmb : list) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("id", ObjectUtils.toString(cmb.getCategryId()));
          map.put("zh_CN_name", cmb.getCategoryZh());
          map.put("en_US_name", cmb.getCategoryEn());
          map.put("disc_code", ObjectUtils.toString(cmb.getCategryId()));
          resultList.add(map);
        }
        allData.put("discipline_code_" + (key == null ? 1 : (key.length() + 3) / 2), resultList);
      }

      // 返回json数据，格式
      return JacksonUtils.mapToJsonStr(allData);
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<ConstDiscipline> findConstDiscByList(List<Long> discIdList) throws ServiceException {
    return constDisciplineDao.findByIds(discIdList);
  }
}
