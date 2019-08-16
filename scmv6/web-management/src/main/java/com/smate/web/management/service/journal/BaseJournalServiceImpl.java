package com.smate.web.management.service.journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.JnlFormateUtils;
import com.smate.web.management.dao.journal.BaseConstCategoryDao;
import com.smate.web.management.dao.journal.BaseJnlCategoryRankDao;
import com.smate.web.management.dao.journal.BaseJournalCategoryDao;
import com.smate.web.management.dao.journal.BaseJournalDao;
import com.smate.web.management.dao.journal.BaseJournalDbDao;
import com.smate.web.management.dao.journal.BaseJournalIfDao;
import com.smate.web.management.dao.journal.BaseJournalLogDao;
import com.smate.web.management.dao.journal.BaseJournalPublisherDao;
import com.smate.web.management.dao.journal.BaseJournalSearchDao;
import com.smate.web.management.dao.journal.BaseJournalTempBatchDao;
import com.smate.web.management.dao.journal.BaseJournalTempIsiIfDao;
import com.smate.web.management.dao.journal.BaseJournalTitleDao;
import com.smate.web.management.dao.journal.ConstRefDbDao;
import com.smate.web.management.dao.journal.JournalDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.journal.BaseConstCategory;
import com.smate.web.management.model.journal.BaseJnlCategoryRank;
import com.smate.web.management.model.journal.BaseJournal;
import com.smate.web.management.model.journal.BaseJournalCategory;
import com.smate.web.management.model.journal.BaseJournalDb;
import com.smate.web.management.model.journal.BaseJournalIfTo;
import com.smate.web.management.model.journal.BaseJournalLog;
import com.smate.web.management.model.journal.BaseJournalPublisher;
import com.smate.web.management.model.journal.BaseJournalTempBatch;
import com.smate.web.management.model.journal.BaseJournalTempCheck;
import com.smate.web.management.model.journal.BaseJournalTempIsiIf;
import com.smate.web.management.model.journal.BaseJournalTitle;
import com.smate.web.management.model.journal.Journal;
import com.smate.web.management.model.journal.JournalForm;

/**
 * @author cwli
 * 
 */
@Service("baseJournalService")
@Transactional(rollbackFor = Exception.class)
public class BaseJournalServiceImpl implements BaseJournalService {
  private static final long serialVersionUID = -3131795568765610598L;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BaseJournalTempBatchDao baseJournalTempBatchDao;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private BaseConstCategoryDao baseConstCategoryDao;
  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;
  @Autowired
  private BaseJournalDbDao baseJournalDbDao;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private BaseJnlCategoryRankDao baseJnlCategoryRankDao;
  @Autowired
  private BaseJournalPublisherDao baseJournalPublisherDao;
  @Autowired
  private BaseJournalSearchDao baseJnlSerchDao;
  @Autowired
  private JournalDao journalDao;
  @Autowired
  private BaseJournalLogDao baseJournalLogDao;
  @Autowired
  private BaseJournalTempIsiIfDao baseJournalTempIsiIfDao;
  @Autowired
  private BaseJournalIfDao baseJournalIfDao;



  @Override
  public JournalImportStatusEnum importJournal(BaseJournalTempBatch baseJournalTempBatch) throws ServiceException {
    if (baseJournalTempBatch == null) {
      return JournalImportStatusEnum.OTHER;
    }
    if (baseJournalTempBatch.getDbId() == null && ((StringUtils.isBlank(baseJournalTempBatch.getTitleXx())
        && StringUtils.isBlank(baseJournalTempBatch.getTitleEn()))
        || StringUtils.isBlank(baseJournalTempBatch.getDbCode()))) {
      return JournalImportStatusEnum.NOTIMPORT;
    }
    JournalImportStatusEnum status = JournalImportStatusEnum.ERROR;
    Long dbid = baseJournalTempBatch.getDbId() == null ? getDbIdByDbCode(baseJournalTempBatch.getDbCode())
        : baseJournalTempBatch.getDbId();
    if (null == dbid) {
      return JournalImportStatusEnum.NODBID;
    }
    baseJournalTempBatch.setDbId(dbid);
    // 判断pissn号是否异常！
    if (issnError(baseJournalTempBatch.getPissn(), 1)) {
      // 只插入临时表。并记录失败状态！
      baseJournalTempBatch.setThrowsCause(3L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
      return JournalImportStatusEnum.MANUAL;
    }
    // 如果eissn不为空并且格式出错
    if (issnError(baseJournalTempBatch.getEissn(), 2)) {
      baseJournalTempBatch.setThrowsCause(4L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
      return JournalImportStatusEnum.MANUAL;
    }
    // 返回值 0：无重复没有异常，1：记录重复，2：一号多刊名,4:其它异常！
    int throwsCause = isExistJournal(baseJournalTempBatch);
    if (logger.isInfoEnabled()) {
      logger.info("查重结果:{}，baseJnlId:{}，查重参数：title_en:{},title_xx:{},db_id:{},pissn:{}",
          new Object[] {throwsCause, baseJournalTempBatch.getJnlId(), baseJournalTempBatch.getTitleEn(),
              baseJournalTempBatch.getTitleXx(), dbid, baseJournalTempBatch.getPissn()});
    }
    switch (throwsCause) {
      case 0:
        // 无重复数据，填充相关表
        // batchCount++;
        this.savaJournalInfo(baseJournalTempBatch);
        status = JournalImportStatusEnum.ADD;
        break;
      case 1:
        // 记录重复。更新数据！
        baseJournalTempBatch.setThrowsCause(1L);
        // 更新相关表数据！
        updateJournalInfo(baseJournalTempBatch);
        if (logger.isDebugEnabled()) {
          logger.info("记录重复，pissn：" + baseJournalTempBatch.getPissn());
        }
        status = JournalImportStatusEnum.UPDATE;
        break;
      case 2:
        // 一号多刊名,更改状态。转收工处理！
        baseJournalTempBatch.setThrowsCause(2L);
        status = JournalImportStatusEnum.MANUAL;
        break;
      case 4:
        baseJournalTempBatch.setThrowsCause(4L);
        status = JournalImportStatusEnum.MANUAL;
        break;
      default:
        break;
    }
    baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
    return status;
  }


  @Override
  public BaseJournalLog impoutJournalIf(List<BaseJournalTempIsiIf> importJournalIfList) {
    // 转手工处理的条数！
    int manualCount = 0;
    // 导入的总条数！
    int impCount = importJournalIfList.size();
    // 成功导入的条数！
    int batchCount = 0;
    // 导入更新的条数！
    int updateCount = 0;
    Long baseJournalId = null;// 基础期刊id
    BaseJournalLog baseJournalLog = new BaseJournalLog();
    if (importJournalIfList == null || importJournalIfList.size() <= 0) {
      return null;
    }
    for (BaseJournalTempIsiIf baseJournalTempIf : importJournalIfList) {
      try {
        Long db_id = getDbIdByDbCode(baseJournalTempIf.getDbCode());
        if (null == db_id) {
          if (logger.isInfoEnabled())
            logger.info("db_coed在库中不存在。不导入！" + baseJournalTempIf.toString());
          continue;
        }
        baseJournalTempIf.setDbId(db_id);
        // 判断pissn号是否异常！
        if (issnError(baseJournalTempIf.getPissn(), 1)) {
          manualCount++;
          baseJournalTempIf.setThrowsCause(1L);// 转手工
          baseJournalTempIsiIfDao.saveOrUpdate(baseJournalTempIf);
          continue;
        }
        // 查重base_journal：titleZh+titleEn+pissn相同
        List<BaseJournal> list1 = baseJournalDao.findBaseJnl(baseJournalTempIf.getTitleXx(),
            baseJournalTempIf.getTitleEn(), baseJournalTempIf.getPissn(), null);
        // 找不到相应的记录！.转手工处理！
        if (CollectionUtils.isEmpty(list1)) {
          List<Long> jnlIdList = baseJournalTitleDao.getJnlID(baseJournalTempIf.getPissn(),
              baseJournalTempIf.getTitleEn(), baseJournalTempIf.getTitleXx());
          if (null == jnlIdList || 0 == jnlIdList.size()) {
            // 找不到相应的记录！.转手工处理！
            manualCount++;
            baseJournalTempIf.setThrowsCause(1L);// 转手工
            baseJournalTempIsiIfDao.saveOrUpdate(baseJournalTempIf);
            logger.info("没有找到相应的journalID,新增期刊.pissn为:" + baseJournalTempIf.getPissn());
            continue;
          }
          if (jnlIdList.size() > 1) {
            // 如果找到的基础大于一条！.转手工处理！
            manualCount++;
            baseJournalTempIf.setThrowsCause(1L);// 转手工
            baseJournalTempIsiIfDao.saveOrUpdate(baseJournalTempIf);
            logger.info("找到的基础大于一条,期刊.pissn为:" + baseJournalTempIf.getPissn());
            continue;
          }
          baseJournalId = jnlIdList.get(0);
        } else {
          baseJournalId = list1.get(0).getJnlId();
        }
        // 保存到临时表
        baseJournalTempIf.setJnlId(baseJournalId);
        baseJournalTempIsiIfDao.saveOrUpdate(baseJournalTempIf);
        // 保存期刊因子！
        // 检查期刊期刊因子表中是否存在该Jnl_id的记录；
        BaseJournalIfTo baseJournalIfTo =
            baseJournalIfDao.getJifIdByJnlId(baseJournalId, db_id, baseJournalTempIf.getIfYear());

        // 如果没有该期刊的期刊样子记录。创建对象并插入！存在则更新记录！
        if (null == baseJournalIfTo) {
          batchCount++;
          baseJournalIfTo = new BaseJournalIfTo();
        } else {
          updateCount++;
        }
        baseJournalIfTo.setPissn(baseJournalTempIf.getPissn());
        baseJournalIfTo.setJnlId(baseJournalId);
        baseJournalIfTo.setDbId(db_id);
        baseJournalIfTo.setJouIf(baseJournalTempIf.getJouIf());
        baseJournalIfTo.setIfYear(baseJournalTempIf.getIfYear());
        baseJournalIfTo.setJouRif(baseJournalTempIf.getJouRif());
        baseJournalIfTo.setImmediacyIndex(baseJournalTempIf.getImmediacyIndex());
        baseJournalIfTo.setArticles(baseJournalTempIf.getArticles());
        baseJournalIfTo.setCites(baseJournalTempIf.getCites());
        baseJournalIfTo.setCitedHalfLife(baseJournalTempIf.getCitedHalfLife());
        baseJournalIfTo.setCitingHalfLife(baseJournalTempIf.getCitingHalfLife());
        baseJournalIfTo.setArticleInfluenceScore(baseJournalTempIf.getArticleInfluenceScore());
        baseJournalIfTo.setEigenfactorScore(baseJournalTempIf.getEigenfactorScore());
        baseJournalIfDao.saveOrUpdate(baseJournalIfTo);
        // 同步新增基础期刊刷新表供期刊统计调用.
        baseJnlSerchDao.delete(baseJournalIfTo.getJnlId());
        baseJournalDao.deleteSyncJournalFlag(baseJournalIfTo.getJnlId());
        baseJournalDao.syncBaseJournalFlag(baseJournalIfTo.getJnlId(), 0);
      } catch (Exception e) {
        logger.error("保存影响因子出错,参数：{}", baseJournalTempIf.toString(), e);
      }
    }
    baseJournalLog.setImpCount(impCount);
    baseJournalLog.setBatchCount(batchCount);
    baseJournalLog.setManualCount(manualCount);
    baseJournalLog.setUpdateCount(updateCount);
    return baseJournalLog;
  }

  private void updateJournalInfo(BaseJournalTempBatch baseJournalTempBatch) {

    if (baseJournalTempBatch.getJnlId() == null)
      return;
    // 根据journalId查询得到持久化对象BaseJournal！
    // BaseJournal baseJournal = baseJournalDao.get(baseJournalTempBatch.getJnlId());
    // 更新BaseJournal。

    saveBaseJournal(baseJournalTempBatch);

    // 更新BaseJournalTitle！
    updateBaseJournalTitle(baseJournalTempBatch);

    // 更新BaseJournalDb！
    updateBaseJournalDb(baseJournalTempBatch);

    // 更新BaseJournalCategory！
    List<BaseJournalCategory> baseJournalCategorys = savaBaseJournalCategory(baseJournalTempBatch);

    // 更新BaseJournalPublisher！
    updateBaseJournalPublisher(baseJournalTempBatch);

    // 保存及更新！
    // baseJournalDao.saveOrUpdate(baseJournal);
    // 同步更新期刊分类排名表
    syncUpdateJnlCatRank(baseJournalTempBatch, baseJournalCategorys);
  }


  private void updateBaseJournalPublisher(BaseJournalTempBatch jnlBatch) {
    Long db_id = jnlBatch.getDbId();
    boolean flag = true;
    if (StringUtils.isNotBlank(jnlBatch.getPublisherName()) || StringUtils.isNotBlank(jnlBatch.getPublisherAddress())) {
      // 如果没关联的记录，直接增加！
      List<BaseJournalPublisher> baseJournalPublishers = baseJournalPublisherDao.findByJnlId(jnlBatch.getJnlId());
      if (baseJournalPublishers == null || baseJournalPublishers.size() == 0) {
        savaBaseJournalPublisher(jnlBatch);
        return;
      }
      // 如果有关联的记录。比较DBID。如果DBID相同则更新此条数据。如果不同则增加新的记录！
      for (BaseJournalPublisher bjp : baseJournalPublishers) {
        if (isEqualsNum(bjp.getDbId().longValue(), db_id.longValue())) {
          bjp.setDbId(jnlBatch.getDbId());
          bjp.setPublisherAddress(jnlBatch.getPublisherAddress());
          bjp.setPublisherName(jnlBatch.getPublisherName());
          bjp.setPublisherUrl(jnlBatch.getPublisherUrl());
          baseJournalPublisherDao.saveOrUpdate(bjp);
          flag = false;
          break;
        }
      }
      if (flag) {
        savaBaseJournalPublisher(jnlBatch);
      }

    }
  }



  private void updateBaseJournalDb(BaseJournalTempBatch jnlBatch) {
    List<BaseJournalDb> baseJournalDbs = baseJournalDbDao.fundByJnlId(jnlBatch.getJnlId());
    if (baseJournalDbs == null || baseJournalDbs.size() == 0) {
      savaBaseJournalDb(jnlBatch);
      return;
    }
    boolean flag = true;
    for (BaseJournalDb bjd : baseJournalDbs) {
      if (isEqualsNum(bjd.getDbId(), jnlBatch.getDbId())
          && StringUtils.equalsIgnoreCase(jnlBatch.getUpdateYear(), bjd.getYear())) {
        flag = false;
        break;
      }
    }
    // 当没有重复记录时！插入新的记录！
    if (flag) {
      savaBaseJournalDb(jnlBatch);
    }
  }


  private void updateBaseJournalTitle(BaseJournalTempBatch jnlBatch) {
    List<BaseJournalTitle> baseJournalTitles = baseJournalTitleDao.findByJnlId(jnlBatch.getJnlId());
    boolean flag = true;
    for (BaseJournalTitle bjt : baseJournalTitles) {
      if (isEqualsNum(bjt.getDbId(), jnlBatch.getDbId())) {
        bjt.setTitleAbbr(jnlBatch.getTitleAbbr());
        bjt.setCn(jnlBatch.getCn());
        bjt.setEissn(jnlBatch.getEissn());
        baseJournalTitleDao.saveOrUpdate(bjt);
        flag = false;
        break;
      }
    }
    // dbid不同说明是pissn和title相同。dbid不同。增加title记录！
    if (flag) {
      this.savaBaseJournalTitle(jnlBatch);
    }
  }

  /**
   * 比较DBID是否相同。对于isi包括5个字库。即2包含15、16、17、18、19！
   * 
   * @param oldValue
   * @param newValue
   * @return
   */
  private boolean isEqualsNum(long oldValue, long newValue) {
    if (2 == newValue) {
      if (oldValue == 2 || oldValue == 15 || oldValue == 16 || oldValue == 17 || oldValue == 18 || oldValue == 19) {
        return true;
      }
    } else {
      if (oldValue == newValue) {
        return true;
      }
    }
    return false;
  }


  /**
   * 保存期刊信息
   * 
   * @param baseJournalTempBatch
   * @return
   */
  private void savaJournalInfo(BaseJournalTempBatch baseJournalTempBatch) {

    // 存储BaseJournal
    this.saveBaseJournal(baseJournalTempBatch);

    // 存储BaseJournalTitle
    this.savaBaseJournalTitle(baseJournalTempBatch);

    // 存储BaseJournalDb！
    this.savaBaseJournalDb(baseJournalTempBatch);

    // 存储BaseJournalCategory
    List<BaseJournalCategory> baseJournalCategorys = savaBaseJournalCategory(baseJournalTempBatch);

    // 存储BaseJournalPublisher！
    this.savaBaseJournalPublisher(baseJournalTempBatch);

    // 同步更新期刊分类排名表
    this.syncUpdateJnlCatRank(baseJournalTempBatch, baseJournalCategorys);

  }


  /**
   * 同步更新期刊分类排名表
   * 
   * @param baseJournal
   * @param baseJournalTempBatch
   */
  private void syncUpdateJnlCatRank(BaseJournalTempBatch JouBatch, List<BaseJournalCategory> baseJournalCategorys) {
    if (CollectionUtils.isEmpty(baseJournalCategorys)) {
      return;
    }
    try {
      String catRankXml = JouBatch.getCatRankXml();
      String ifyear = StringUtils.trimToEmpty(JouBatch.getIfYear());
      if (StringUtils.isBlank(catRankXml) || StringUtils.isBlank(ifyear))
        return;
      if (ifyear.indexOf(".") > -1) {
        ifyear = ifyear.substring(0, ifyear.indexOf("."));
      }
      Integer year = NumberUtils.toInt(ifyear);
      for (BaseJournalCategory baseJournalCategory : baseJournalCategorys) {
        if (baseJournalCategory != null) {
          Long jcId = baseJournalCategory.getId();
          if (!baseJournalCategory.getDbId().equals(JouBatch.getDbId()) || jcId == null)
            return;
          BaseConstCategory constCat = baseConstCategoryDao.get(baseJournalCategory.getCategoryId());
          String catZh = StringUtils.trimToEmpty(constCat.getCategoryXx());
          String catEn = StringUtils.trimToEmpty(constCat.getCategoryEn());
          catRankXml = catRankXml.replace("&", "@");
          Document doc = DocumentHelper.parseText(catRankXml);
          List nodes = doc.selectNodes("/category/rank");
          for (int i = 0; i < nodes.size(); i++) {
            Element ele = (Element) nodes.get(i);
            String cat = StringUtils.trimToEmpty(ele.attributeValue("name"));
            if (StringUtils.isBlank(cat))
              continue;
            cat = cat.replace("@", "&");
            Integer count = NumberUtils.toInt(StringUtils.trim(ele.attributeValue("count")));
            Integer no = NumberUtils.toInt(StringUtils.trim(ele.attributeValue("no")));
            String rank = StringUtils.trimToEmpty(ele.attributeValue("rank"));
            if (StringUtils.equalsIgnoreCase(catEn, cat) || StringUtils.equalsIgnoreCase(catZh, cat)) {
              BaseJnlCategoryRank newjcr = baseJnlCategoryRankDao.getJnlCatRankByJcIdAndYear(jcId, year);
              if (newjcr == null) {
                newjcr = new BaseJnlCategoryRank(jcId, year);
              }
              newjcr.setCount(count);
              newjcr.setNo(no);
              newjcr.setRank(rank);
              baseJnlCategoryRankDao.saveOrUpdate(newjcr);
              break;
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("同步更新期刊分类排名表出错", e);
    }
  }


  /**
   * 存储BaseJournalPublisher！
   * 
   * @param publisher
   * @param baseJournal
   * @param baseJournalTempBatch
   */
  private void savaBaseJournalPublisher(BaseJournalTempBatch jouBatch) {
    BaseJournalPublisher publisher = baseJournalPublisherDao.getJouPubliser(jouBatch.getDbId(), jouBatch.getJnlId());
    if (StringUtils.isNotBlank(jouBatch.getPublisherName()) || StringUtils.isNotBlank(jouBatch.getPublisherAddress())) {
      if (publisher == null) {
        publisher = new BaseJournalPublisher(jouBatch.getJnlId(), jouBatch.getDbId());
      }
      publisher.setPublisherAddress(jouBatch.getPublisherAddress());
      publisher.setPublisherName(jouBatch.getPublisherName());
      publisher.setPublisherUrl(jouBatch.getPublisherUrl());
      baseJournalPublisherDao.saveOrUpdate(publisher);
    }
  }


  /**
   * 存储BaseJournalCategory
   * 
   * @param baseJournal
   * @param jouBatch
   * @return
   */
  private List<BaseJournalCategory> savaBaseJournalCategory(BaseJournalTempBatch jouBatch) {
    Long dbId = jouBatch.getDbId();
    String category = jouBatch.getCategory();
    String catRankXml = jouBatch.getCatRankXml();
    List<BaseJournalCategory> baseJournalCategorys = null;
    if (StringUtils.isNotBlank(catRankXml)) {
      try {
        catRankXml = catRankXml.replace("&", "@");
        Document doc = DocumentHelper.parseText(catRankXml);
        List nodes = doc.selectNodes("/category/rank");
        baseJournalCategorys = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
          Element ele = (Element) nodes.get(i);
          String catName = StringUtils.trimToEmpty(ele.attributeValue("name"));
          if (StringUtils.isBlank(catName))
            continue;
          catName = catName.replace("@", "&");
          baseJournalCategorys.addAll(saveBaseJournalCategory(catName, dbId, jouBatch.getJnlId()));
        }
      } catch (DocumentException e) {
        logger.error("catRankXml:{}转换xml出错,开始使用category字段读取期刊分类", catRankXml, e);
        category = category.replace("|", "/");
        String[] categorys = category.split("/");
        baseJournalCategorys = new ArrayList<>();
        for (String catName : categorys) {
          if (StringUtils.isBlank(catName))
            continue;
          baseJournalCategorys.addAll(saveBaseJournalCategory(catName, dbId, jouBatch.getJnlId()));
        }
      }
    } else if (StringUtils.isNotBlank(category)) {
      category = category.replace("|", "/");
      String[] categorys = category.split("/");
      baseJournalCategorys = new ArrayList<>();
      for (String catName : categorys) {
        if (StringUtils.isBlank(catName))
          continue;
        baseJournalCategorys.addAll(saveBaseJournalCategory(catName, dbId, jouBatch.getJnlId()));
      }
    }
    return baseJournalCategorys;
  }


  /**
   * 查找期刊分类常量表中的分类id，将所有分类进行保存
   * 
   * @param catName
   * @param dbId
   * @param jnlId
   * @return
   */
  private List<BaseJournalCategory> saveBaseJournalCategory(String catName, Long dbId, Long jnlId) {
    List<BaseJournalCategory> categoryList = new ArrayList<>();
    List<Long> categoryIds = baseConstCategoryDao.searchByCategoryName(StringUtils.trimToEmpty(catName), dbId);
    if (CollectionUtils.isNotEmpty(categoryIds)) {
      for (Long categoryId : categoryIds) {
        BaseJournalCategory jouCategory = baseJournalCategoryDao.getJouCategory(categoryId, dbId, jnlId);
        if (jouCategory == null) {
          jouCategory = new BaseJournalCategory(jnlId, categoryId, dbId);
          baseJournalCategoryDao.save(jouCategory);
        }
        categoryList.add(jouCategory);
      }
    }
    return categoryList;
  }

  /**
   * 存储BaseJournalDb！
   * 
   * @param baseJournalDb
   * @param baseJournal
   * @param jouBatch
   */
  private void savaBaseJournalDb(BaseJournalTempBatch jouBatch) {
    BaseJournalDb baseJournalDb = new BaseJournalDb(jouBatch.getJnlId(), jouBatch.getDbId(), jouBatch.getUpdateYear());
    baseJournalDbDao.saveOrUpdate(baseJournalDb);
  }


  /**
   * 存储BaseJournalTitle！
   * 
   * @param baseJournalTitle
   * @param baseJournal
   * @param baseJournalTempBatch
   */
  private void savaBaseJournalTitle(BaseJournalTempBatch baseJournalTempBatch) {
    BaseJournalTitle baseJournalTitle = new BaseJournalTitle();
    BeanUtils.copyProperties(baseJournalTempBatch, baseJournalTitle);
    baseJournalTitleDao.saveOrUpdate(baseJournalTitle);
  }


  /**
   * 保存基础期刊数据
   * 
   * @param JouBatch
   * @return
   * @throws Exception
   */
  private void saveBaseJournal(BaseJournalTempBatch JouBatch) {
    // 新增
    BaseJournal baseJournal = null;
    if (JouBatch.getJnlId() == null) {
      baseJournal = new BaseJournal();
      baseJournal.setTitleEn(JouBatch.getTitleEn());
      baseJournal.setTitleXx(JouBatch.getTitleXx());
      baseJournal.setPissn(JouBatch.getPissn());
    } else {
      baseJournal = baseJournalDao.get(JouBatch.getJnlId());
    }
    baseJournal.setTitleAbbr(JouBatch.getTitleAbbr());
    baseJournal.setEissn(JouBatch.getEissn());
    baseJournal.setCn(JouBatch.getCn());
    baseJournal.setLanguage(JouBatch.getLanguage());
    baseJournal.setStartYear(fmtYear(JouBatch.getStartYear()));
    baseJournal.setEndYear(fmtYear(JouBatch.getEndYear()));
    baseJournal.setOaStatus(JouBatch.getOaStatus());
    baseJournal.setActiveStatus(JouBatch.getActiveStatus());
    baseJournal.setFrequencyEn(JouBatch.getFrequencyEn());
    baseJournal.setFrequencyZh(JouBatch.getFrequencyZh());
    if (StringUtils.isNotEmpty(JouBatch.getRegion())) {
      Long regionId = null;
      try {
        regionId = constRegionDao.getRegionIdByName(JouBatch.getRegion());
      } catch (Exception e) {
        logger.info("从CONST_REGION表中获取regionId失败。regionName=" + JouBatch.getRegion(), e);
      }
      baseJournal.setRegionId(regionId);
    }
    baseJournal.setJournalUrl(JouBatch.getJournalUrl());
    baseJournal.setDescriptionEn(JouBatch.getDescriptionEn());
    baseJournal.setDescriptionXx(JouBatch.getDescriptionXx());
    baseJournal.setSubmissionUrl(JouBatch.getSubmissionUrl());
    baseJournalDao.saveOrUpdate(baseJournal);
    JouBatch.setJnlId(baseJournal.getJnlId());
  }


  /**
   * 格式化年份
   * 
   * @param year
   * @return
   */
  private String fmtYear(String year) {
    if (StringUtils.isBlank(year))
      return "";
    if (year.indexOf(".") != -1)
      year = year.substring(0, year.indexOf("."));
    return year;
  }

  @Override
  public int isExistJournal(BaseJournalTempBatch baseJournalTempBatch) {
    String title_xx = baseJournalTempBatch.getTitleXx();
    String title_en = baseJournalTempBatch.getTitleEn();
    String pissn = baseJournalTempBatch.getPissn();
    String eissn = baseJournalTempBatch.getEissn();
    Long db_id = baseJournalTempBatch.getDbId();
    try {
      // 查重base_journal：titleZh+titleEn+pissn+eissn相同
      List<BaseJournal> list1 = baseJournalDao.findBaseJnl(title_xx, title_en, pissn, eissn);
      if (CollectionUtils.isNotEmpty(list1)) {
        baseJournalTempBatch.setJnlId(list1.get(0).getJnlId());
        return 1;
      }
      String titleXxAlias = JnlFormateUtils.getStrAlias(title_xx);
      String titleEnAlias = JnlFormateUtils.getStrAlias(title_en);
      // 查重base_journal_titele：titleZh+titleEn+pissn相同
      List<BaseJournalTitle> list2 = baseJournalTitleDao.findBaseJnlTitle(titleXxAlias, titleEnAlias, pissn, null);
      if (CollectionUtils.isNotEmpty(list2)) {
        baseJournalTempBatch.setJnlId(list2.get(0).getJnlId());
        return 1;
      }
      // "****_****"该格式不执行一号多刊查重
      if (!"****_****".equals(pissn) && !"****-****".equals(pissn)) {
        // 查重base_journal_titele：pissn相同
        List<BaseJournalTitle> list3 = baseJournalTitleDao.findBaseJnlTitle(null, null, pissn, null);
        if (CollectionUtils.isNotEmpty(list3))
          return 2;
      }
      // 查重base_journal_titele：任意title相同
      List<BaseJournalTitle> list4 = baseJournalTitleDao.jnlMatchBaseJnlTitile(titleXxAlias, titleEnAlias);
      if (CollectionUtils.isNotEmpty(list4))
        return 4;
    } catch (Exception e) {
      logger.error("查重出错,db_id:{},pissn:{},eissn:{},title_en:{},title_xx:{}",
          new Object[] {db_id, pissn, eissn, title_en, title_xx}, e);
      return 4;
    }
    return 0;
  }


  @Override
  // 返回值 0：无重复没有异常，1：记录重复，2：一号多刊名,4:其它异常！
  public int isExistJournal(String pissn, String eissn, String title_en, String title_xx, Long db_id, String cn) {
    /*
     * try { // 查重base_journal：titleZh+titleEn+pissn+eissn相同 List<BaseJournal> list1 =
     * baseJournalDao.findBaseJnl(title_xx, title_en, pissn, eissn); if
     * (CollectionUtils.isNotEmpty(list1)) { return 1; } String titleXxAlias =
     * JnlFormateUtils.getStrAlias(title_xx); String titleEnAlias =
     * JnlFormateUtils.getStrAlias(title_en); // 查重base_journal_titele：titleZh+titleEn+pissn相同
     * List<BaseJournalTitle> list2 = baseJournalTitleDao.findBaseJnlTitle(titleXxAlias, titleEnAlias,
     * pissn, null); if (CollectionUtils.isNotEmpty(list2)) { return 1; } // "****_****"该格式不执行一号多刊查重 if
     * (!"****_****".equals(pissn) && !"****-****".equals(pissn)) { // 查重base_journal_titele：pissn相同
     * List<BaseJournalTitle> list3 = baseJournalTitleDao.findBaseJnlTitle(null, null, pissn, null); if
     * (CollectionUtils.isNotEmpty(list3)) return 2; } // 查重base_journal_titele：任意title相同
     * List<BaseJournalTitle> list4 = baseJournalTitleDao.jnlMatchBaseJnlTitile(titleXxAlias,
     * titleEnAlias); if (CollectionUtils.isNotEmpty(list4)) return 4; } catch (Exception e) {
     * logger.error("查重出错,db_id:{},pissn:{},eissn:{},title_en:{},title_xx:{}", new Object[] {db_id,
     * pissn, eissn, title_en, title_xx}, e); return 4; }
     */
    return 0;
  }


  /**
   * 判断pissi是否异常 校验格式
   * 
   * @param pissn
   * @type 1=pissn 2=eissn
   * @return
   */
  private boolean issnError(String issn, int type) {
    // 如果是pissn
    if (type == 1 && StringUtils.isBlank(issn))
      return true;
    if (type == 1 && ("****_****".equals(issn) || "****-****".equals(issn)))
      return false;
    if (StringUtils.isNotBlank(issn)) {
      Pattern pattern = Pattern.compile("\\d{4}-[A-Za-z0-9]{4}");
      Matcher matcher = pattern.matcher(issn);
      if (!matcher.matches()) {
        return true;
      }
    }
    return false;
  }

  private Long getDbIdByDbCode(String dbCode) {
    // 数据来源常量表使用sns ,同时这边的SCI和SCIE是一一样的，在数据库中都为SCIE
    dbCode = "SCI".equalsIgnoreCase(dbCode) ? "SCIE" : dbCode;
    return constRefDbDao.getDbIdByCode(dbCode);
  }


  @Override
  public void saveJournalTitle(BaseJournalTempBatch baseJournalTempBatch) throws ServiceException {
    BaseJournalTitle baseJournalTitle = new BaseJournalTitle();
    BeanUtils.copyProperties(baseJournalTempBatch, baseJournalTitle);
    baseJournalTitleDao.saveOrUpdate(baseJournalTitle);
  }


  @Override
  public void saveJournalImportLog(BaseJournalLog baseJournalLog) {
    baseJournalLogDao.saveOrUpdate(baseJournalLog);
  }



  @Override
  @Deprecated
  public void getTempCheck(JournalForm journalForm, String isTemp) {
    /*
     * BaseJournalTempCheck check = journalForm.getCheck(); try { if (check == null) { check = new
     * BaseJournalTempCheck(); } if (StringUtils.isNotBlank(isTemp)) { check.setIsTemp(isTemp); }
     * Page<BaseJournalTempCheck> pageCheck =
     * baseJournalTempCheckDao.findByTempCheck(journalForm.getPage(), check); List<BaseJournalTempCheck>
     * checkList = pageCheck.getResult(); if (checkList != null && checkList.size() > 0) { for
     * (BaseJournalTempCheck baseJournalTempCheck : checkList) { String xx =
     * baseJournalTempCheck.getTitleXx(); if (StringUtils.isNotBlank(xx) &&
     * "[object Object]".equals(xx)) baseJournalTempCheck.setTitleXx(null); if
     * (baseJournalTempCheck.getDbId() != null)
     * baseJournalTempCheck.setDbCode(constRefDbDao.getCodeByDbId(baseJournalTempCheck.getDbId())); } }
     * journalForm.setPage(pageCheck); } catch (DaoException e) { logger.error("获取审核表所有数据出错", e); }
     */
  }



  @Override
  public List<BaseJournalTitle> getTempTitleByTempCheck(List<Long> titleIdList) throws ServiceException {
    List<BaseJournalTitle> baseJournalTitleList = null;
    baseJournalTitleList = baseJournalTitleDao.findByIds(titleIdList);
    if (baseJournalTitleList != null && baseJournalTitleList.size() > 0) {
      for (BaseJournalTitle BaseJournalTitle : baseJournalTitleList) {
        if (BaseJournalTitle.getDbId() != null)
          BaseJournalTitle.setDbCode(constRefDbDao.getCodeByDbId(BaseJournalTitle.getDbId()));
      }
    }
    return baseJournalTitleList;
  }

  @Override
  public List<BaseJournalTempBatch> getBatchTitleByTempCheck(List<Long> batchIdList) throws ServiceException {
    List<BaseJournalTempBatch> baseJournalTempBatchList = null;
    baseJournalTempBatchList = baseJournalTempBatchDao.findByIds(batchIdList);
    if (baseJournalTempBatchList != null && baseJournalTempBatchList.size() > 0) {
      for (BaseJournalTempBatch baseJournalTempBatch : baseJournalTempBatchList) {
        String xx = baseJournalTempBatch.getTitleXx();
        if (StringUtils.isNotBlank(xx) && "[object Object]".equals(xx))
          baseJournalTempBatch.setTitleXx(null);
        if (baseJournalTempBatch.getDbId() != null)
          baseJournalTempBatch.setDbCode(constRefDbDao.getCodeByDbId(baseJournalTempBatch.getDbId()));
      }
    }

    return baseJournalTempBatchDao.findByIds(batchIdList);
  }

  @Deprecated
  @Override
  public int passTempCheck(Long tempId, String isTemp) throws ServiceException {
    /*
     * try { if (null == tempId) { return 0; } Long status = 4L;// 0：待处理 /2：待审核/3：已拒绝/4：已通过
     * BaseJournalTempCheck check = baseJournalTempCheckDao.get(tempId); if ("batch".equals(isTemp)) {
     * // this.updateTempBatch(check, status); } if ("isiIf".equals(isTemp)) { updateTempIsIif(check,
     * status); } // 管理员已经处理，相当于删除此条记录 check.setIsActive(1L); // 0L：未审核/1L：批准/2L：拒绝 check.setStatus(1L);
     * // add by liuwei // 增加管理员id及审核时间 check.setAdminPsnId(SecurityUtils.getCurrentUserId());
     * check.setAuditDate(new Date()); baseJournalTempCheckDao.saveOrUpdate(check); return 1; } catch
     * (Exception e) { logger.debug("管理员审核期刊通过出错", e); return 0; }
     */
    return 0;
  }

  /**
   * 期刊审核拒绝
   */
  @Override
  @Deprecated
  public int refuseTempCheck(Long tempId, String temps) {
    /*
     * try { if (null == tempId) { return 0; } Long status = 3L;// 1：待处理/2：待审核/3：已拒绝/4：已通过
     * BaseJournalTempCheck check = baseJournalTempCheckDao.get(tempId); check.setStatus(2L);//
     * 0L：未审核/1L：通过/2L：拒绝 // add by liuwei 保存管理员ID；及审核时间 check.setIsActive(1L);
     * check.setAdminPsnId(SecurityUtils.getCurrentUserId()); check.setAuditDate(new Date());
     * baseJournalTempCheckDao.saveOrUpdate(check); if ("batch".equals(temps.trim())) {
     * BaseJournalTempBatch batch = baseJournalTempBatchDao.get(check.getTempBatchId());
     * batch.setStatus(status); } if ("isiIf".equals(temps.trim())) { BaseJournalTempIsiIf isiIf =
     * baseJournalTempIsiIfDao.get(check.getTempIsiIfId()); isiIf.setStatus(status); } return 1; } catch
     * (Exception e) { if (logger.isDebugEnabled()) { logger.debug("管理员审核期刊拒绝出错", e); } return 0; }
     */
    return 0;
  }

  /**
   * 管理员审核通过__IsI期刊影响因子
   * 
   * @param check
   * @param status
   */
  private void updateTempIsIif(BaseJournalTempCheck check, Long status) {
    try {
      // 更新至选中期刊
      if (check.getHandleMethod() == 1) {
        // check.getTuttiJouId()必须获取的BaseJournalTitle表的jouTitleId
        if (check.getTuttiJouId() != null) {
          BaseJournalTitle baseJournalTitle = baseJournalTitleDao.get(check.getTuttiJouId());
          Long jnlId = baseJournalTitle.getJnlId();
          Long dbid = baseJournalTitle.getDbId();
          BaseJournalTempIsiIf baseJournalTempIf = baseJournalTempIsiIfDao.findById(check.getTempIsiIfId());
          baseJournalTempIf.setJnlId(jnlId);
          // 更新影响因子
          BaseJournalIfTo baseJournalIfTo =
              baseJournalIfDao.getJifIdByJnlId(jnlId, dbid, baseJournalTempIf.getIfYear());
          if (null == baseJournalIfTo) {
            baseJournalIfTo = new BaseJournalIfTo();
          }
          // 复制属性
          BeanUtils.copyProperties(baseJournalTempIf, baseJournalIfTo);
          baseJournalIfTo.setJnlId(jnlId);
          baseJournalIfTo.setDbId(dbid);
          baseJournalIfDao.saveOrUpdate(baseJournalIfTo);
        }
      }
      // (更新至选中期刊)与(保留原样)都需要更改审核状态
      if (check.getHandleMethod() == 1 || check.getHandleMethod() == 3) {
        BaseJournalTempIsiIf isiIf = baseJournalTempIsiIfDao.get(check.getTempIsiIfId());
        isiIf.setStatus(status);
      }
    } catch (Exception e) {
      logger.error("审核手工导入临时表通过时出错", e);
    }
  }


  @Override
  public BaseJournalLog importJournalByPass(BaseJournalTempBatch baseJournalTempBatch) {
    Long jnlId = 0L;
    if (logger.isDebugEnabled()) {
      logger.debug("开始通过审核的期刊！");
    }
    // 转手工处理的条数！
    int manualCount = 0;
    // 导入的总条数！
    int impCount = 1;
    // 成功导入的条数！
    int batchCount = 0;
    // 导入更新的条数！
    int updateCount = 0;
    if (baseJournalTempBatch == null)
      return null;
    Long db_id = baseJournalTempBatch.getDbId() == null ? getDbIdByDbCode(baseJournalTempBatch.getDbCode())
        : baseJournalTempBatch.getDbId();
    if (null == db_id)
      return null;
    if (StringUtils.isBlank(baseJournalTempBatch.getPissn()) && StringUtils.isBlank(baseJournalTempBatch.getEissn()))
      return null;
    baseJournalTempBatch.setDbId(db_id);

    // 判断pissn号是否异常！
    if (issnError(baseJournalTempBatch.getPissn(), 1)) {
      // 只插入临时表。并记录失败状态！
      manualCount++;
      baseJournalTempBatch.setThrowsCause(3L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
    }
    // 如果eissn不为空并且格式出错
    if (issnError(baseJournalTempBatch.getEissn(), 2)) {
      baseJournalTempBatch.setThrowsCause(4L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
    }

    int throws_cause = isExistJournalPass(baseJournalTempBatch.getPissn(), baseJournalTempBatch.getEissn(),
        baseJournalTempBatch.getTitleEn(), baseJournalTempBatch.getTitleXx());
    if (throws_cause == 0) {
      // 无重复数据
      // 填充相关表
      batchCount++;
      savaJournalInfo(baseJournalTempBatch);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
      this.setJournalMatchJid(baseJournalTempBatch, baseJournalTempBatch.getJnlId());
    } else if (throws_cause == 1) {
      // 记录重复。更新数据！
      // 插入临时表
      updateCount++;
      baseJournalTempBatch.setJnlId(jnlId);
      baseJournalTempBatch.setThrowsCause(1L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
      // 更新相关表数据！
      updateJournalInfo(baseJournalTempBatch);
      this.setJournalMatchJid(baseJournalTempBatch, jnlId);
    }

    BaseJournalLog baseJournalLog = new BaseJournalLog();
    baseJournalLog.setImpCount(impCount);
    baseJournalLog.setBatchCount(batchCount);
    baseJournalLog.setManualCount(manualCount);
    baseJournalLog.setUpdateCount(updateCount);
    return baseJournalLog;
  }



  // 管理员审核批准通过-查重
  private int isExistJournalPass(String pissn, String eissn, String title_en, String title_xx) {
    Long jnlId = 0L;
    // 查重base_journal：titleZh+titleEn+pissn+eissn相同
    List<BaseJournal> list1 = baseJournalDao.findBaseJnl(title_xx, title_en, pissn, eissn);
    if (CollectionUtils.isNotEmpty(list1)) {
      jnlId = list1.get(0).getJnlId();
      return 1;
    }
    String titleXxAlias = JnlFormateUtils.getStrAlias(title_xx);
    String titleEnAlias = JnlFormateUtils.getStrAlias(title_en);
    // 查重base_journal_titele：titleZh+titleEn+pissn+eissn相同
    List<BaseJournalTitle> list2 = baseJournalTitleDao.findBaseJnlTitle(titleXxAlias, titleEnAlias, pissn, eissn);
    if (CollectionUtils.isNotEmpty(list2)) {
      jnlId = list2.get(0).getJnlId();
      return 1;
    }
    return 0;
  }


  private void setJournalMatchJid(BaseJournalTempBatch baseJournalTempBatch, Long baseJnlId) {
    if (baseJournalTempBatch.getJid() != null)
      journalDao.setMatchJnlId(baseJournalTempBatch.getJid(), baseJnlId);
  }

  /**
   * 
   * @param batch
   * @param toJnlId 要更新到的期刊对应的id
   */
  private void saveOrUpdateBaseTitle(BaseJournalTempBatch batch, Long toJnlId) {
    List<BaseJournalTitle> bjtList =
        baseJournalTitleDao.findBaseJnlTitle(JnlFormateUtils.getStrAlias(batch.getTitleXx()),
            JnlFormateUtils.getStrAlias(batch.getTitleEn()), batch.getPissn(), null);
    if (CollectionUtils.isEmpty(bjtList)) {
      BaseJournalTitle newJnlTitle = new BaseJournalTitle();
      BeanUtils.copyProperties(batch, newJnlTitle);
      newJnlTitle.setJnlId(toJnlId);
      newJnlTitle.setTitleStatus(0);
      baseJournalTitleDao.save(newJnlTitle);
    } else {
      for (BaseJournalTitle BaseJournalTitle : bjtList) {
        if (StringUtils.isBlank(BaseJournalTitle.getTitleEn()))
          BaseJournalTitle.setTitleEn(batch.getTitleEn());
        if (StringUtils.isBlank(BaseJournalTitle.getTitleXx()))
          BaseJournalTitle.setTitleXx(batch.getTitleXx());
        if (StringUtils.isNotBlank(batch.getEissn()))
          BaseJournalTitle.setEissn(batch.getEissn());
        baseJournalTitleDao.update(BaseJournalTitle);
      }
    }
  }


  @Override
  public Page<BaseJournalTempBatch> getAllBatchData(Page<BaseJournalTempBatch> page,
      BaseJournalTempBatch baseJournalTempBatch) {
    StringBuilder hql = new StringBuilder("from BaseJournalTempBatch where 1=1");
    List<Object> params = new ArrayList<>();
    if (StringUtils.isNotBlank(baseJournalTempBatch.getTitleXx())) {
      hql.append(" and (upper(titleXx) like ? or upper(titleEn) like ?)");
      params.add("%" + StringUtils.trimToEmpty(baseJournalTempBatch.getTitleXx()).toUpperCase() + "%");
      params.add("%" + StringUtils.trimToEmpty(baseJournalTempBatch.getTitleXx()).toUpperCase() + "%");
    }
    if (StringUtils.isNotBlank(baseJournalTempBatch.getPissn())) {
      hql.append(" and pissn = ? ");
      params.add(StringUtils.trimToEmpty(baseJournalTempBatch.getPissn()));
    }
    Long throwsCause = baseJournalTempBatch.getThrowsCause();
    if (null != throwsCause) {
      if (throwsCause == 0) {
        hql.append(" and throwsCause > 1 ");
      } else {
        hql.append(" and throwsCause = ? ");
        params.add(throwsCause);
      }
    } else {
      hql.append(" and throwsCause > 1 ");
    }
    hql.append(" and status in (" + baseJournalTempBatch.getStatus() + ",1,3)");
    hql.append(" order by tempBatchId desc");
    Page<BaseJournalTempBatch> batchPage = baseJournalTempBatchDao.findPage(page, hql.toString(), params.toArray());
    if (CollectionUtils.isNotEmpty(batchPage.getResult())) {
      for (BaseJournalTempBatch batch : batchPage.getResult()) {
        if (batch.getDbId() != null) {
          batch.setDbCode(constRefDbDao.getCodeByDbId(batch.getDbId()));
        }
      }
    }
    return batchPage;
  }


  @Override
  public BaseJournalTempBatch getTempBatchById(Long jnlId) {
    BaseJournalTempBatch batch = baseJournalTempBatchDao.get(jnlId);
    if (batch != null && batch.getDbId() != null) {
      batch.setDbCode(constRefDbDao.getCodeByDbId(batch.getDbId()));
    }
    return batch;
  }


  @Override
  public List<BaseJournalTitle> findBaseJournalTitle(String pissn, String titleXx, String titleEn) {
    List<BaseJournalTitle> titleList = baseJournalTitleDao.findBaseJournalTitle(JnlFormateUtils.getStrAlias(titleXx),
        JnlFormateUtils.getStrAlias(titleEn), pissn);
    if (titleList != null && titleList.size() > 0) {
      for (BaseJournalTitle BaseJournalTitle : titleList) {
        if (BaseJournalTitle.getDbId() != null)
          BaseJournalTitle.setDbCode(constRefDbDao.getCodeByDbId(BaseJournalTitle.getDbId()));
      }
    }
    return titleList;
  }

  @Override
  public Journal findJournal(Long jId) throws ServiceException {
    return this.journalDao.get(jId);
  }


  @Override
  public Long findDBIdLikeDbCode(String dbCode) {
    try {
      return constRefDbDao.getDbIdByCode(dbCode);
    } catch (ServiceException e) {
      logger.error("searchDBId出错，dbCode:{}", dbCode, e);
    }
    return null;
  }

  /**
   * 重构BaseJournalTempBatch，将页面上传来的数据与数据库中的数据补充
   * 
   * @param batch
   * @return
   */
  private BaseJournalTempBatch reBuildBaseJournalTempBatch(BaseJournalTempBatch batch) {
    BaseJournalTempBatch batchInDB = baseJournalTempBatchDao.get(batch.getTempBatchId());
    batchInDB.setPissn(batch.getPissn());
    batchInDB.setEissn(batch.getEissn());
    batchInDB.setTitleXx(batch.getTitleXx());
    batchInDB.setTitleEn(batch.getTitleEn());
    return batchInDB;
  }

  @Override
  public Map<String, Object> updateTempBatch(BaseJournalTempBatch batch, Long jnlIds, int handleMethod) {
    batch = reBuildBaseJournalTempBatch(batch);
    // 1=更新至选中期刊 2=新增期刊 3=保留原样
    return updateBatch(batch, (long) handleMethod, jnlIds);
  }

  @Override
  public String updateTempIsIif(BaseJournalTempIsiIf isiIf, Long jnlIds, Long handleMethod) {
    try {
      // 1=更新至选中期刊 2=新增期刊 3=保留原样
      // 更新至选中期刊
      if (handleMethod == 1) {
        if (jnlIds != null) {
          BaseJournalTitle baseJournalTitle = baseJournalTitleDao.get(jnlIds);
          Long jnlId = baseJournalTitle.getJnlId();
          Long dbid = baseJournalTitle.getDbId();
          BaseJournalTempIsiIf baseJournalTempIf = baseJournalTempIsiIfDao.findById(isiIf.getTempIsiIfId());
          baseJournalTempIf.setJnlId(jnlId);
          // 更新影响因子
          BaseJournalIfTo baseJournalIfTo =
              baseJournalIfDao.getJifIdByJnlId(jnlId, dbid, baseJournalTempIf.getIfYear());
          if (null == baseJournalIfTo) {
            baseJournalIfTo = new BaseJournalIfTo();
          }
          baseJournalIfTo.setPissn(baseJournalTempIf.getPissn());
          baseJournalIfTo.setJnlId(jnlId);
          baseJournalIfTo.setDbId(dbid);
          baseJournalIfTo.setJouIf(baseJournalTempIf.getJouIf());
          baseJournalIfTo.setIfYear(baseJournalTempIf.getIfYear());
          baseJournalIfTo.setJouRif(baseJournalTempIf.getJouRif());
          baseJournalIfTo.setImmediacyIndex(baseJournalTempIf.getImmediacyIndex());
          baseJournalIfTo.setArticles(baseJournalTempIf.getArticles());
          baseJournalIfTo.setCites(baseJournalTempIf.getCites());
          baseJournalIfTo.setCitedHalfLife(baseJournalTempIf.getCitedHalfLife());
          baseJournalIfTo.setCitingHalfLife(baseJournalTempIf.getCitingHalfLife());
          baseJournalIfTo.setArticleInfluenceScore(baseJournalTempIf.getArticleInfluenceScore());
          baseJournalIfTo.setEigenfactorScore(baseJournalTempIf.getEigenfactorScore());
          baseJournalIfDao.saveOrUpdate(baseJournalIfTo);
        }
      }
      // （0：待处理/2：待审核/3：已拒绝/4：已通过）。handleMethod（ 1L：更新至选中期刊，2L：保留原样）
      baseJournalTempIsiIfDao.updateStstus(4L, handleMethod, isiIf.getTempIsiIfId());
      return "true";
    } catch (Exception e) {
      logger.error("审核手工导入临时表通过时出错", e);
    }
    return "false";
  }

  /**
   * 
   * @param batch
   * @param handleMethod 期刊手工处理方式 1=更新至选中期刊 2=新增期刊 3=保留原样
   * @param jnlIds
   * @throws DaoException
   */
  private Map<String, Object> updateBatch(BaseJournalTempBatch batch, Long handleMethod, Long jnlId) {
    Assert.notNull(batch, "更新期刊时,对应的batch不能为空");
    Map<String, Object> resultMap = new HashMap<>();
    // 更新至选中期刊
    if (handleMethod == 1) {
      if (jnlId != null) {
        BaseJournalTitle BaseJournalTitle = baseJournalTitleDao.get(jnlId);// 这边传来jnlID的其实是期刊title的主键
        if (BaseJournalTitle != null) {
          BaseJournal baseJournal = convertToBaseJournal(batch, BaseJournalTitle.getJnlId());
          if (baseJournal == null) {
            resultMap.put("result", "error");
            resultMap.put("mesg", "base_journal表中不存在当前更新到的期刊记录，无法更新");
            return resultMap;
          }
          // 使用条件去base_journal表中查，是否村子啊相同的期刊，如果这几个字段相同，则不进行更新（这几个字段定义了唯一索引）
          List<BaseJournal> findBaseJnl = baseJournalDao.findBaseJnl(baseJournal.getTitleXx(), baseJournal.getTitleEn(),
              baseJournal.getPissn(), null);
          if (CollectionUtils.isNotEmpty(findBaseJnl)) {
            resultMap.put("result", "error");
            resultMap.put("mesg", "base_journal表中已存在相同的期刊，无法更新");
            return resultMap;
          }
          // 根据batch中的jnlId去base_journal_title表中查询 有则更新 没有插入
          this.saveOrUpdateBaseTitle(batch, BaseJournalTitle.getJnlId());
          // 根据batch中的jnlId去base_journal表中查询 更新
          baseJournalDao.saveOrUpdate(baseJournal);
        }
        // 更新journal表匹配jid
        this.setJournalMatchJid(batch, BaseJournalTitle.getJnlId());
      }
    }
    // 新增期刊
    if (handleMethod == 2) {
      // 再次调用期刊导入的逻辑，审核数据，并导入相关的表
      importJournal(batch);
    }
    // 更新batch表操作状态
    batch.setStatus(4L);
    batch.setHandleMethod(handleMethod);
    baseJournalTempBatchDao.saveOrUpdate(batch);
    resultMap.put("result", "success");
    return resultMap;
  }

  /**
   * 将batch转换为baseJournal
   * 
   * @param batch
   * @param jnlId
   * @return
   */
  private BaseJournal convertToBaseJournal(BaseJournalTempBatch batch, Long jnlId) {
    BaseJournal baseJournal = baseJournalDao.get(jnlId);
    if (baseJournal != null) {
      if (StringUtils.isNotBlank(batch.getTitleXx())) {
        baseJournal.setTitleXx(batch.getTitleXx());
      }
      if (StringUtils.isNotBlank(batch.getTitleEn())) {
        baseJournal.setTitleEn(batch.getTitleEn());
      }
      if (StringUtils.isNotBlank(batch.getPissn())) {
        baseJournal.setPissn(batch.getPissn());
      }
      if (StringUtils.isNotBlank(batch.getEissn())) {
        baseJournal.setEissn(batch.getEissn());
      }
      if (StringUtils.isNotBlank(batch.getTitleAbbr())) {
        baseJournal.setTitleAbbr(batch.getTitleAbbr());
      }
      if (StringUtils.isNotBlank(batch.getCn())) {
        baseJournal.setCn(batch.getCn());
      }
      if (StringUtils.isNotBlank(batch.getJournalUrl())) {
        baseJournal.setJournalUrl(batch.getJournalUrl());
      }
      if (StringUtils.isNotEmpty(batch.getRegion())) {
        try {
          baseJournal.setRegionId(constRegionDao.getRegionIdByName(batch.getRegion()));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (StringUtils.isNotBlank(batch.getLanguage())) {
        baseJournal.setLanguage(batch.getLanguage());
      }
      if (StringUtils.isNotBlank(batch.getStartYear())) {
        baseJournal.setStartYear(batch.getStartYear());
      }
    }
    return baseJournal;
  }

  private BaseJournalLog impoutJournalByPass(BaseJournalTempBatch baseJournalTempBatch) {
    logger.debug("开始通过审核的期刊！");
    Long jnlId = null;
    // 转手工处理的条数！
    int manualCount = 0;
    // 导入的总条数！
    int impCount = 1;
    // 成功导入的条数！
    int batchCount = 0;
    // 导入更新的条数！
    int updateCount = 0;
    if (baseJournalTempBatch == null)
      return null;
    Long db_id = baseJournalTempBatch.getDbId() == null ? getDbIdByDbCode(baseJournalTempBatch.getDbCode())
        : baseJournalTempBatch.getDbId();
    if (null == db_id)
      return null;
    if (StringUtils.isBlank(baseJournalTempBatch.getPissn()) && StringUtils.isBlank(baseJournalTempBatch.getEissn()))
      return null;
    baseJournalTempBatch.setDbId(db_id);
    // 判断pissn号是否异常！
    if (issnError(baseJournalTempBatch.getPissn(), 1)) {
      // 只插入临时表。并记录失败状态！
      manualCount++;
      baseJournalTempBatch.setThrowsCause(3L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
    }
    // 如果eissn不为空并且格式出错
    if (issnError(baseJournalTempBatch.getEissn(), 2)) {
      baseJournalTempBatch.setThrowsCause(4L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
    }
    int throws_cause = isExistJournalPass(baseJournalTempBatch.getPissn(), baseJournalTempBatch.getEissn(),
        baseJournalTempBatch.getTitleEn(), baseJournalTempBatch.getTitleXx());
    if (throws_cause == 0) {
      // 无重复数据
      // 填充相关表
      batchCount++;
      savaJournalInfo(baseJournalTempBatch);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
      this.setJournalMatchJid(baseJournalTempBatch, baseJournalTempBatch.getJnlId());
    } else if (throws_cause == 1) {
      // 记录重复。更新数据！
      // 插入临时表
      updateCount++;
      baseJournalTempBatch.setJnlId(jnlId);
      baseJournalTempBatch.setThrowsCause(1L);
      baseJournalTempBatchDao.saveOrUpdate(baseJournalTempBatch);
      // 更新相关表数据！
      updateJournalInfo(baseJournalTempBatch);
      this.setJournalMatchJid(baseJournalTempBatch, jnlId);
    }
    logger.debug(baseJournalTempBatch.toString());
    BaseJournalLog baseJournalLog = new BaseJournalLog();
    baseJournalLog.setImpCount(impCount);
    baseJournalLog.setBatchCount(batchCount);
    baseJournalLog.setManualCount(manualCount);
    baseJournalLog.setUpdateCount(updateCount);
    return baseJournalLog;
  }

  /**
   * 保存或者更新baseJournal表
   * 
   * @param batch
   * @param toJnlId 要更新到的期刊对应的id
   * @throws Exception
   */
  private void saveOrUpdateBaseJouranl(BaseJournalTempBatch batch, Long toJnlId) {
    BaseJournal baseJournal = baseJournalDao.get(toJnlId);
    // 不为空 更新baseJournal
    if (baseJournal != null) {
      if (StringUtils.isNotBlank(batch.getEissn())) {
        baseJournal.setEissn(batch.getEissn());
      }
      if (StringUtils.isNotBlank(batch.getEissn())) {
        baseJournal.setEissn(batch.getEissn());
      }
      if (StringUtils.isNotBlank(batch.getTitleAbbr())) {
        baseJournal.setTitleAbbr(batch.getTitleAbbr());
      }
      if (StringUtils.isNotBlank(batch.getCn())) {
        baseJournal.setCn(batch.getCn());
      }
      if (StringUtils.isNotBlank(batch.getJournalUrl())) {
        baseJournal.setJournalUrl(batch.getJournalUrl());
      }
      if (StringUtils.isNotEmpty(batch.getRegion())) {
        try {
          baseJournal.setRegionId(constRegionDao.getRegionIdByName(batch.getRegion()));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (StringUtils.isNotBlank(batch.getLanguage())) {
        baseJournal.setLanguage(batch.getLanguage());
      }
      if (StringUtils.isNotBlank(batch.getStartYear())) {
        baseJournal.setStartYear(batch.getStartYear());
      }
      baseJournalDao.saveOrUpdate(baseJournal);
    }
  }

  /**
   * 保存或者更新baseTitle表
   * 
   * @param batch
   */
  private void saveOrUpdateBaseTitle(BaseJournalTempBatch batch) throws Exception {
    if (batch.getJnlId() == null)
      return;
    // 根据batch中的jnlId去base_journal_title表中查询
    List<BaseJournalTitle> journalTitles = baseJournalTitleDao.findByJnlId(batch.getJnlId());
    // 没有就新增
    if (CollectionUtils.isNotEmpty(journalTitles)) {
      BaseJournalTitle newJnlTitle = new BaseJournalTitle();
      if (batch.getDbId() != null)
        newJnlTitle.setDbId(batch.getDbId());
      newJnlTitle.setJnlId(batch.getJnlId());
      if (StringUtils.isNotBlank(batch.getTitleEn()))
        newJnlTitle.setTitleEn(batch.getTitleEn());
      if (StringUtils.isNotBlank(batch.getTitleXx()))
        newJnlTitle.setTitleXx(batch.getTitleXx());
      if (StringUtils.isNotBlank(batch.getPissn()))
        newJnlTitle.setPissn(batch.getPissn());
      if (StringUtils.isNotBlank(batch.getEissn()))
        newJnlTitle.setEissn(batch.getEissn());
      newJnlTitle.setTitleStatus(0);
      baseJournalTitleDao.save(newJnlTitle);
    } else {
      // 遍历更新
      journalTitles.forEach(journalTitle -> {
        if (StringUtils.isBlank(journalTitle.getTitleEn()))
          journalTitle.setTitleEn(batch.getTitleEn());
        if (StringUtils.isBlank(journalTitle.getTitleXx()))
          journalTitle.setTitleXx(batch.getTitleXx());
        if (StringUtils.isNotBlank(batch.getEissn()))
          journalTitle.setEissn(batch.getEissn());
        baseJournalTitleDao.save(journalTitle);
      });
    }
  }


  @Override
  public Page<BaseJournalTempIsiIf> getAllIsIData(Page<BaseJournalTempIsiIf> page, BaseJournalTempIsiIf isiIf) {
    StringBuilder hql = new StringBuilder("from BaseJournalTempIsiIf where 1=1 ");
    List<Object> params = new ArrayList<>();
    if (StringUtils.isNotBlank(isiIf.getTitleXx())) {
      hql.append(" and titleXx like ? or titleEn like ?");
      String titleXx = StringUtils.trimToEmpty(isiIf.getTitleXx());
      params.add("%" + titleXx + "%");
      params.add("%" + titleXx + "%");
    }
    if (StringUtils.isNotBlank(isiIf.getPissn())) {
      hql.append(" and pissn=? ");
      params.add(StringUtils.trimToEmpty(isiIf.getPissn()));
    }
    hql.append(" and throwsCause =?");
    params.add(isiIf.getThrowsCause());
    hql.append(" and status in (" + isiIf.getStatus() + ",3)");
    hql.append(" order by tempIsiIfId desc");
    return baseJournalTempIsiIfDao.findPage(page, hql.toString(), params.toArray());
  }

  @Override
  public BaseJournalTempIsiIf getTempIsiIfById(Long tempIsiIfId) {
    BaseJournalTempIsiIf isiIf = baseJournalTempIsiIfDao.get(tempIsiIfId);
    try {
      if (isiIf != null && isiIf.getDbId() != null)
        isiIf.setDbCode(constRefDbDao.getCodeByDbId(isiIf.getDbId()));
    } catch (ServiceException e) {
      logger.error("根据影响因子Id获取实体出错", e);
    }
    return isiIf;
  }

  @Override
  public Page<BaseJournal> getBaseJournal(Page<BaseJournal> page, BaseJournal journal) {
    if (journal.getThrowsType() != null) {
      page = baseJournalDao.findBaseJournalByThrows(page, journal.getThrowsType());
    } else {
      page = baseJournalDao.getBaseJournal(page, journal);
    }
    return page;
  }

  @Override
  public List<BaseJournal> findNewBaseJournalList(BaseJournal journal) {
    List<Long> bjIds = baseJournalTitleDao.findBaseJournalIds(journal.getPissn(), journal.getTitleXx());
    return baseJournalDao.findByIds(bjIds);
  }

  @Override
  public void updateBaseJournal(List<BaseJournal> jnlList) {
    if (CollectionUtils.isNotEmpty(jnlList)) {
      for (BaseJournal jnl : jnlList) {
        boolean flag = false;
        boolean isNewPissn = false;
        List<BaseJournalTitle> titleList = baseJournalTitleDao.findByJnlId(jnl.getJnlId());
        if (CollectionUtils.isNotEmpty(titleList)) {
          for (BaseJournalTitle jnlTitle : titleList) {
            if (!StringUtils.equals(jnl.getPissn(), jnlTitle.getPissn())) {
              isNewPissn = true;
              break;
            }
          }
          if (isNewPissn) {
            jnl.setIsNewPissn(true);
          }
          if (titleList.size() == 1) {
            baseJournalTitleDao.updateTitleStatus(titleList.get(0).getJouTitleId(), 1);
            continue;
          }
          for (BaseJournalTitle jnlTitle : titleList) {
            if (jnlTitle.getTitleStatus() == null || jnlTitle.getTitleStatus() == 0) {
              flag = true;
              break;
            }
          }
          if (flag) {
            jnl.setIsNewTitle(true);
          }
        }
      }
    }
  }

  @Override
  public int mergeJournal(Long jouId, Long toJouId) {
    try {
      // 合并到的期刊
      BaseJournal jnl2 = baseJournalDao.get(toJouId);
      if (jnl2 == null) {
        return 3;
      }
      // 原期刊
      BaseJournal jnl1 = baseJournalDao.get(jouId);
      // 将temp*表中的原jnl1的jnlId更改为要合并到期刊jnlId
      List<BaseJournalTempBatch> batchList = baseJournalTempBatchDao.findByJournalId(jnl1.getJnlId());
      if (CollectionUtils.isNotEmpty(batchList)) {
        for (BaseJournalTempBatch baseJournalTempBatch : batchList) {
          // 跟新batch表
          baseJournalTempBatchDao.updateJnlId(baseJournalTempBatch.getJnlId(), jnl2.getJnlId());
        }
      }
      List<BaseJournalTempIsiIf> isiIfList = baseJournalTempIsiIfDao.findByJournalId(jnl1.getJnlId());
      if (CollectionUtils.isNotEmpty(isiIfList)) {
        for (BaseJournalTempIsiIf baseJournalTempIsiIf : isiIfList) {
          // 跟新 tempIsI
          baseJournalTempIsiIfDao.updateJnlId(baseJournalTempIsiIf.getJnlId(), jnl2.getJnlId());
        }
      }
      // 合并期刊
      // 查重更新base_journal_title。。！
      List<BaseJournalTitle> titleList1 = baseJournalTitleDao.findByJnlId(jnl1.getJnlId());
      if (CollectionUtils.isNotEmpty(titleList1)) {
        for (BaseJournalTitle jnlTitle1 : titleList1) {
          List<BaseJournalTitle> list = baseJournalTitleDao.findBaseJournalTitleToByJnlId(jnl2.getJnlId(),
              jnlTitle1.getPissn(), jnlTitle1.getTitleEn(), jnlTitle1.getTitleXx(), jnlTitle1.getDbId());
          if (CollectionUtils.isEmpty(list)) {
            BaseJournalTitle newTitle = new BaseJournalTitle();
            newTitle.setJnlId(jnl2.getJnlId());
            if (StringUtils.isNotBlank(jnlTitle1.getCn()))
              newTitle.setCn(jnlTitle1.getCn());
            if (StringUtils.isNotBlank(jnlTitle1.getDbCode()))
              newTitle.setDbCode(jnlTitle1.getDbCode());
            if (jnlTitle1.getDbId() != null)
              newTitle.setDbId(jnlTitle1.getDbId());
            if (StringUtils.isNotBlank(jnlTitle1.getPissn()))
              newTitle.setPissn(jnlTitle1.getPissn());
            if (StringUtils.isNotBlank(jnlTitle1.getEissn()))
              newTitle.setEissn(jnlTitle1.getEissn());
            if (StringUtils.isNotBlank(jnlTitle1.getTitleAbbr()))
              newTitle.setTitleAbbr(jnlTitle1.getTitleAbbr());
            if (StringUtils.isNotBlank(jnlTitle1.getTitleEn()))
              newTitle.setTitleEn(jnlTitle1.getTitleEn());
            if (StringUtils.isNotBlank(jnlTitle1.getTitleXx()))
              newTitle.setTitleXx(jnlTitle1.getTitleXx());
            baseJournalTitleDao.save(newTitle);
          }
        }
      }
      // 更新合并到的期刊
      baseJournalDao.saveOrUpdate(jnl2);
      // 删除原来的期刊以及一些相关表的数据
      deletBaseJournal(jnl1.getJnlId());
      return 1;
    } catch (Exception e) {
      logger.error("期刊合并出错", e);
      throw new ServiceException();
    }
  }

  @Override
  public int updateJnlAndJnlTitle(BaseJournal jnl) throws ServiceException {
    try {
      // 更新BaseJournal表
      BaseJournal journal = baseJournalDao.get(jnl.getJnlId());
      if (journal == null) {
        return 0;
      }
      journal.setPissn(jnl.getPissn());
      journal.setEissn(jnl.getEissn());
      journal.setTitleEn(jnl.getTitleEn());
      journal.setTitleXx(jnl.getTitleXx());
      baseJournalDao.update(journal);
      // 更新或新增对应的title
      saveOrUpBaseTitleByJournal(journal);
      return 1;
    } catch (Exception e) {
      throw new ServiceException("修改基础期刊主表及其title表出错", e);
    }
  }

  /**
   * 使用指定的journal查询baseJournalTitle表中的数据，如果存在重复的就更新，否则新增数据
   * 
   * @param baseJournal
   * @throws Exception
   */
  private void saveOrUpBaseTitleByJournal(BaseJournal baseJournal) throws Exception {
    if (baseJournal == null)
      return;
    List<BaseJournalTitle> bjtList =
        baseJournalTitleDao.findBaseJnlTitle(JnlFormateUtils.getStrAlias(baseJournal.getTitleXx()),
            JnlFormateUtils.getStrAlias(baseJournal.getTitleEn()), baseJournal.getPissn(), null);
    if (CollectionUtils.isEmpty(bjtList)) {
      BaseJournalTitle newJnlTitle = new BaseJournalTitle();
      newJnlTitle.setJnlId(baseJournal.getJnlId());
      if (StringUtils.isNotBlank(baseJournal.getTitleEn()))
        newJnlTitle.setTitleEn(baseJournal.getTitleEn());
      if (StringUtils.isNotBlank(baseJournal.getTitleXx()))
        newJnlTitle.setTitleXx(baseJournal.getTitleXx());
      if (StringUtils.isNotBlank(baseJournal.getPissn()))
        newJnlTitle.setPissn(baseJournal.getPissn());
      if (StringUtils.isNotBlank(baseJournal.getEissn()))
        newJnlTitle.setEissn(baseJournal.getEissn());
      newJnlTitle.setTitleStatus(0);
      baseJournalTitleDao.save(newJnlTitle);
    } else {
      for (BaseJournalTitle baseJournalTitle : bjtList) {
        if (StringUtils.isBlank(baseJournalTitle.getTitleEn()))
          baseJournalTitle.setTitleEn(baseJournal.getTitleEn());
        if (StringUtils.isBlank(baseJournalTitle.getTitleXx()))
          baseJournalTitle.setTitleXx(baseJournal.getTitleXx());
        if (StringUtils.isNotBlank(baseJournal.getEissn()))
          baseJournalTitle.setEissn(baseJournal.getEissn());
        baseJournalTitleDao.save(baseJournalTitle);
      }
    }
  }

  @Override
  public void deletBaseJournal(Long jnlId) {
    try {
      if (baseJournalDao.get(jnlId) != null) {
        // add by liuwei..如果删除的是有合并至此期刊的数据的话。我们的先修改temp表中的throws_cause
        baseJournalTempBatchDao.updateStstusForDeleteJou(1l, 0l, jnlId);
        baseJournalTempIsiIfDao.updateStstusForDeleteJou(1l, 0l, jnlId);
        // baseJnlSerchDao.deleteBaseJournalSearch(jouId);// 删除冗余表
        journalDao.updateMatchJnlId(jnlId, null);// 更新期刊表匹配id
        baseJournalDbDao.deleteByJnlId(jnlId);// 删除对应baseJournalDb中的数据
        baseJournalTitleDao.deleteByJnlId(jnlId);// 删除对应baseJournalTitle表中的记录
        baseJournalPublisherDao.deleteByJnlId(jnlId);// 删除 baseJournalPublisher表中对应的数据
        baseJnlCategoryRankDao.deleteBaseJnlCategoryRankByjnlId(jnlId);// 先删除baseJournalCategoryRank中的数据
        baseJournalCategoryDao.deleteByJnlId(jnlId);// 删除baseJournalCategory对应的数据
        baseJournalIfDao.deleteByJnlId(jnlId);// 删除期刊的影响因子
        baseJournalDao.delete(jnlId);
        // 同步新增基础期刊刷新表供期刊统计调用.
        // baseJournalDao.syncBaseJournalFlag(jouId, 1);
      }
    } catch (Exception e) {
      throw new ServiceException("根据主键删除基础期刊数据出错", e);
    }
  }

  @Override
  public void deleteBaseJournalTitleById(Long bjtId) throws ServiceException {
    baseJournalTitleDao.delete(bjtId);
  }


  @Override
  public BaseJournal findBaseJournal(Long jnlId) {
    return baseJournalDao.get(jnlId);
  }
}

