package com.smate.center.task.service.pdwh.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.BaseConstCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.BaseJournalCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubForClassificationDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubForClassificationNsfcDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryNsfcByJournalDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapCnkiNsfcDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapIsiNsfcDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapScmCnkiDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapScmIsiDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapScmNsfcDao;
import com.smate.center.task.dao.sns.quartz.NsfcPrjCategoryDao;
import com.smate.center.task.dao.sns.quartz.NsfcPrjForClassificationDao;
import com.smate.center.task.dao.sns.quartz.PsnDiscNsfcDao;
import com.smate.center.task.dao.sns.quartz.PsnDiscScmDao;
import com.smate.center.task.dao.sns.quartz.PsnNsfcInfoDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.ScmUserForClassificationDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.pub.BaseConstCategory;
import com.smate.center.task.model.pdwh.pub.PubCategory;
import com.smate.center.task.model.pdwh.pub.PubCategoryNsfcByJournal;
import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassification;
import com.smate.center.task.model.pdwh.quartz.PdwhPubForClassificationNsfc;
import com.smate.center.task.model.sns.pub.NsfcPrjCategory;
import com.smate.center.task.model.sns.pub.NsfcPrjForClassification;
import com.smate.center.task.model.sns.pub.PsnDiscScm;
import com.smate.center.task.model.sns.pub.ScmUserForClassification;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.service.sns.quartz.TaskMarkerService;
import com.smate.center.task.single.dao.solr.PdwhPubDupDao;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.dao.solr.PubInfoPdwhDao;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubJournalDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubJournalPO;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.pubHash.PubHashUtils;

@Service("scmClassificationService")
@Transactional(rollbackFor = Exception.class)
public class ScmClassificationServiceImpl implements ScmClassificationService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private TaskMarkerService taskMarkerService;
  @Autowired
  private CacheService cacheService;
  @Autowired
  private PubInfoPdwhDao pubInfoPdwhDao;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private BaseConstCategoryDao baseConstCategoryDao;
  @Autowired
  private CategoryMapScmIsiDao crossRefCategoryIsiScmDao;
  @Autowired
  private CategoryMapScmCnkiDao crossRefCategoryCnkiScmDao;
  @Autowired
  private CategoryMapScmNsfcDao crossRefCategoryNsfcScmDao;
  @Autowired
  private PubCategoryDao pubCategoryDao;
  @Autowired
  private PsnDiscNsfcDao psnDiscNsfcDao;
  @Autowired
  private PsnNsfcInfoDao psnNsfcInfoDao;
  @Autowired
  private PsnDiscScmDao psnDiscScmDao;
  @Autowired
  private PdwhPubForClassificationDao pdwhPubForClassificationDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private NsfcPrjForClassificationDao nsfcPrjForClassificationDao;
  @Autowired
  private NsfcPrjCategoryDao nsfcPrjCategoryDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PdwhPubDupDao pdwhPubDupDao;
  @Autowired
  private ScmUserForClassificationDao scmUserForClassificationDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private CategoryMapIsiNsfcDao categoryMapIsiNsfcDao;
  @Autowired
  private PubCategoryNsfcByJournalDao pubCategoryNsfcByJournalDao;
  @Autowired
  private CategoryMapCnkiNsfcDao categoryMapCnkiNsfcDao;
  @Autowired
  private PdwhPubForClassificationNsfcDao pdwhPubForClassificationNsfcDao;
  @Autowired
  private PdwhPubJournalDAO pdwhPubJournalDAO;

  private String PSN_NSFC_TO_SCM = "psn_nsfc_to_scm";
  private Integer size = 5000;

  @Override
  public Integer IsiCategoryToScm(PdwhPubForClassification pub) {
    if (pub == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long pdwhPubId = pub.getPubId();

    if (pdwhPubId == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    PdwhPubJournalPO pubJnl = this.pdwhPubJournalDAO.get(pdwhPubId);
    if (pubJnl == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long jnlId = pubJnl.getJid();
    if (jnlId == null || jnlId == 0L) {
      // 成果没有jnlid
      return 7;
    }
    List<Long> categoryIds = baseJournalCategoryDao.getIsiCategoryIds(jnlId);
    if (categoryIds == null || categoryIds.size() <= 0) {
      // 通过jnlid没有取到对应期刊数据
      return 8;
    }

    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryEn())) {
          bcc.setCategoryEn(bcc.getCategoryEn().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    List<Long> scmCategoryIdList = new ArrayList<Long>();

    for (BaseConstCategory bcc : list) {
      List<Long> idList = this.crossRefCategoryIsiScmDao.getScmCategoryIds(bcc.getCategoryEn());
      if (CollectionUtils.isNotEmpty(idList)) {
        Long id = idList.get(0);
        if (!scmCategoryIdList.contains(id)) {
          scmCategoryIdList.add(id);
        }
      }
    }

    // 存入结果
    if (CollectionUtils.isNotEmpty(scmCategoryIdList)) {
      if (this.pubCategoryDao.getCountsByPubId(pdwhPubId) > 0L) {
        this.pubCategoryDao.deleteByPubId(pdwhPubId);
      }

      for (Long scId : scmCategoryIdList) {
        PubCategory pc = new PubCategory();
        pc.setPubId(pdwhPubId);
        pc.setScmCategoryId(scId);
        this.pubCategoryDao.save(pc);
      }
    } else {// 用来标记没有被归类的成果
      return 2;
    }
    // 分类成功
    return 1;
  }

  @Override
  public Integer IsiCategoryToNsfc(PdwhPubForClassificationNsfc pub) {
    if (pub == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long pdwhPubId = pub.getPubId();

    if (pdwhPubId == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    PdwhPubJournalPO pubJnl = this.pdwhPubJournalDAO.get(pdwhPubId);
    if (pubJnl == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long jnlId = pubJnl.getJid();
    if (jnlId == null || jnlId == 0L) {
      // 成果没有jnlid
      return 7;
    }
    List<Long> categoryIds = baseJournalCategoryDao.getIsiCategoryIds(jnlId);
    if (categoryIds == null || categoryIds.size() <= 0) {
      // 通过jnlid没有取到对应期刊数据
      return 8;
    }

    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      if (id == null) {
        continue;
      }
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryEn())) {
          bcc.setCategoryEn(bcc.getCategoryEn().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    List<String> nsfcCategoryIdList = new ArrayList<String>();

    for (BaseConstCategory bcc : list) {
      List<String> idList = this.categoryMapIsiNsfcDao.getNsfcCategoryIds(bcc.getCategoryEn());
      if (CollectionUtils.isNotEmpty(idList)) {
        String nsfcId = idList.get(0);
        if (!nsfcCategoryIdList.contains(nsfcId)) {
          nsfcCategoryIdList.add(nsfcId);
        }
      }
    }

    // 存入结果
    if (CollectionUtils.isNotEmpty(nsfcCategoryIdList)) {
      if (this.pubCategoryNsfcByJournalDao.getCountsByPubId(pdwhPubId) > 0L) {
        this.pubCategoryNsfcByJournalDao.deleteByPubId(pdwhPubId);
      }

      for (String scId : nsfcCategoryIdList) {
        PubCategoryNsfcByJournal pcn = new PubCategoryNsfcByJournal();
        pcn.setNsfcCategoryId(scId);
        pcn.setPubId(pdwhPubId);
        this.pubCategoryNsfcByJournalDao.save(pcn);
      }
    } else {// 用来标记没有被归类的成果
      return 2;
    }
    // 分类成功
    return 1;
  }

  @Override
  public Integer CnkiCategoryToScm(PdwhPubForClassification pub) throws Exception {
    if (pub == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long pdwhPubId = pub.getPubId();

    if (pdwhPubId == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    PdwhPubJournalPO pubJnl = this.pdwhPubJournalDAO.get(pdwhPubId);
    if (pubJnl == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long jnlId = pubJnl.getJid();
    if (jnlId == null || jnlId == 0L) {
      // 成果没有jnlid
      return 7;
    }

    List<Long> categoryIds = baseJournalCategoryDao.getCnkiCategoryIds(jnlId);

    if (categoryIds == null || categoryIds.size() <= 0) {
      // 通过jnlid没有取到对应期刊数据
      return 8;
    }

    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      if (id == null) {
        continue;
      }
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryXx())) {
          bcc.setCategoryXx(bcc.getCategoryXx().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    List<Long> scmCategoryIdList = new ArrayList<Long>();
    Long id;
    for (BaseConstCategory bcc : list) {
      List<Long> idList = this.crossRefCategoryCnkiScmDao.getScmCategoryIds(bcc.getCategoryXx());
      if (CollectionUtils.isNotEmpty(idList)) {
        id = idList.get(0);
        if (!scmCategoryIdList.contains(id)) {
          scmCategoryIdList.add(id);
        }
      }
    }

    // 当cnki结果为空时，再查找isi
    List<Long> rsList = this.getIsiToScmCategory(categoryIds, scmCategoryIdList);

    // 存入结果
    if (CollectionUtils.isNotEmpty(rsList)) {
      if (this.pubCategoryDao.getCountsByPubId(pdwhPubId) > 0L) {
        this.pubCategoryDao.deleteByPubId(pdwhPubId);
      }
      for (Long scId : rsList) {
        PubCategory pc = new PubCategory();
        pc.setPubId(pdwhPubId);
        pc.setScmCategoryId(scId);
        this.pubCategoryDao.save(pc);
      }
    } else { // 用来标记没有被归类的成果
      return 2;
    }
    // 分类成功
    return 1;
  }

  @Override
  public Integer CnkiCategoryToNsfc(PdwhPubForClassificationNsfc pub) throws Exception {
    if (pub == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long pdwhPubId = pub.getPubId();

    if (pdwhPubId == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    PdwhPubJournalPO pubJnl = this.pdwhPubJournalDAO.get(pdwhPubId);
    if (pubJnl == null) {
      // 对应基准库成果与成果ID为空
      return 9;
    }

    Long jnlId = pubJnl.getJid();
    if (jnlId == null || jnlId == 0L) {
      // 成果没有jnlid
      return 7;
    }

    List<Long> categoryIds = baseJournalCategoryDao.getCnkiCategoryIds(jnlId);

    if (categoryIds == null || categoryIds.size() <= 0) {
      // 通过jnlid没有取到对应期刊数据
      return 8;
    }

    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      if (id == null) {
        continue;
      }
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryXx())) {
          bcc.setCategoryXx(bcc.getCategoryXx().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    List<String> nsfcCategoryIdList = new ArrayList<String>();
    String id;
    for (BaseConstCategory bcc : list) {
      List<String> idList = this.categoryMapCnkiNsfcDao.getNsfcCategoryIds(bcc.getCategoryXx());
      if (CollectionUtils.isNotEmpty(idList)) {
        id = idList.get(0);
        if (!nsfcCategoryIdList.contains(id)) {
          nsfcCategoryIdList.add(id);
        }
      }
    }

    // 当cnki结果为空时，再查找isi
    List<String> rsList = this.getIsiToNsfcCategory(categoryIds, nsfcCategoryIdList);

    // 存入结果
    if (CollectionUtils.isNotEmpty(rsList)) {
      if (this.pubCategoryNsfcByJournalDao.getCountsByPubId(pdwhPubId) > 0L) {
        this.pubCategoryNsfcByJournalDao.deleteByPubId(pdwhPubId);
      }

      for (String scId : nsfcCategoryIdList) {
        PubCategoryNsfcByJournal pcn = new PubCategoryNsfcByJournal();
        pcn.setNsfcCategoryId(scId);
        pcn.setPubId(pdwhPubId);
        this.pubCategoryNsfcByJournalDao.save(pcn);
      }
    } else { // 用来标记没有被归类的成果
      return 2;
    }
    // 分类成功
    return 1;
  }

  @Override
  public void ScmPsnClassification() {
    if (taskMarkerService.getApplicationQuartzSettingValue("scmClassificationService_removePubAllId") == 1) {
      cacheService.remove(PSN_NSFC_TO_SCM, "last_pub_all_id");
    }
    int size = 1000;
    // Long lastId = 0L;
    Long lastId = (Long) cacheService.get(PSN_NSFC_TO_SCM, "last_pub_all_id");

    if (lastId == null) {
      lastId = psnDiscNsfcDao.getMinimunPsnId();
    }

    List<Long> psnList = psnDiscNsfcDao.getBatchPsnId(lastId, size);

    if (CollectionUtils.isEmpty(psnList)) {
      return;
    }

    // 初始化scm-nsfc对应map,都为小写
    Map<Long, List<String>> catMapLength5 = getScmNsfcCrossRefMapByLength(5);
    Map<Long, List<String>> catMapLength3 = getScmNsfcCrossRefMapByLength(3);

    for (Long psnId : psnList) {
      this.scmPsnClassify(psnId, catMapLength3, catMapLength5);
    }
  }

  @Override
  public Integer classifyNsfcProjectToScm(NsfcPrjForClassification prj) throws Exception {
    if (prj == null) {
      return 9;
    }
    String nsfcCategoryCode = prj.getNsfcCategoryCode();

    if (StringUtils.isEmpty(nsfcCategoryCode)) {
      // 项目没有获取到对应nsfc分类
      return 9;
    }

    List<Long> scmCategoryList = new ArrayList<Long>();
    // 优先对比长码，分类更细
    if (nsfcCategoryCode.length() >= 5) {
      nsfcCategoryCode = nsfcCategoryCode.substring(0, 5);
      scmCategoryList = this.crossRefCategoryNsfcScmDao.getScmCategoryByNsfcCategory(nsfcCategoryCode);
    }

    if (CollectionUtils.isEmpty(scmCategoryList)) {
      nsfcCategoryCode = nsfcCategoryCode.substring(0, 3);
      scmCategoryList = this.crossRefCategoryNsfcScmDao.getScmCategoryByNsfcCategory(nsfcCategoryCode);
    }

    if (CollectionUtils.isEmpty(scmCategoryList)) {
      return 2;
    } else {
      if (this.nsfcPrjCategoryDao.getRsCountsByPrjNo(prj.getPrjNo()) > 0) {
        this.nsfcPrjCategoryDao.deleteRsByPrjNo(prj.getPrjNo());
      }

      for (Long scmCode : scmCategoryList) {
        NsfcPrjCategory nsfcPrjCategory = new NsfcPrjCategory();
        nsfcPrjCategory.setNsfcPrjNo(prj.getPrjNo());
        nsfcPrjCategory.setScmCategoryId(scmCode);
        this.nsfcPrjCategoryDao.save(nsfcPrjCategory);
      }
    }

    return 1;
  }

  public Map<Long, List<String>> getScmNsfcCrossRefMapByLength(Integer size) {

    List<Long> scmCategoryNoList = this.crossRefCategoryNsfcScmDao.getScmCategroyNoList();
    Map<Long, List<String>> rsMap = new HashMap<Long, List<String>>();
    for (Long categoryNo : scmCategoryNoList) {
      List<String> nsfcCategoryList = this.crossRefCategoryNsfcScmDao.getNsfcCategorybyCategoryId(categoryNo, size);
      if (CollectionUtils.isNotEmpty(nsfcCategoryList)) {
        rsMap.put(categoryNo, nsfcCategoryList);
      }
    }

    return rsMap;
  }

  public void scmPsnClassify(Long psnId, Map<Long, List<String>> catMapLength3, Map<Long, List<String>> catMapLength5) {

    try {
      List<String> infoList = psnNsfcInfoDao.getPsnNsfcInfo(psnId);
      if (CollectionUtils.isNotEmpty(infoList)) {
        List<Long> scmCategoryList = new ArrayList<Long>();

        for (String disc : infoList) {

          // 当nsfc所填学科长度大于4时，直接取前5位匹配，如果未匹配上，再匹配前3位;如果是长度<4的，直接截取前3位匹配
          if (StringUtils.isNotBlank(disc)) {
            if (disc.length() >= 5) {
              disc = disc.substring(0, 5);
              for (Entry entry : catMapLength5.entrySet()) {
                List<String> list = (List<String>) entry.getValue();
                if (list.contains(disc)) {
                  scmCategoryList.add((Long) entry.getKey());
                }
              }
            }

            if (CollectionUtils.isEmpty(scmCategoryList)) {
              disc = disc.substring(0, 3);
              for (Entry entry : catMapLength3.entrySet()) {
                List<String> list = (List<String>) entry.getValue();
                if (list.contains(disc)) {
                  scmCategoryList.add((Long) entry.getKey());
                }
              }
            }
          }
        }
        // 查重,然后保存
        if (CollectionUtils.isNotEmpty(scmCategoryList)) {
          scmCategoryList = this.duplicateCheck(scmCategoryList);
          for (Long cate : scmCategoryList) {
            PsnDiscScm psnDiscScm = new PsnDiscScm();
            psnDiscScm.setPsnId(psnId);
            psnDiscScm.setScmDiscNo(cate);
            this.psnDiscScmDao.save(psnDiscScm);
          }
        }
      }
    } catch (Exception e) {
      // 由于数量比较多，某个人出错不应影响整个任务工作
      logger.error("人员分类出错，psnId = " + psnId, e);
    }
  }

  @Override
  public Integer psnClassifyBasedOnNsfcInfo(ScmUserForClassification psn) throws Exception {
    if (psn == null || psn.getPsnId() == null || personDao.existsPerson(psn.getPsnId()) == null || psn.getPsnId() == 0L
        || psn.getInfoSource() == null) {
      return 9;
    }

    List<Long> scmCategoryList = new ArrayList<Long>();
    // 利用nsfc项目信息分类
    if (psn.getInfoSource() == 1) {
      scmCategoryList = this.psnClassificationFromNsfc(psn.getPsnId());
    }

    // 利用scm成果信息分类
    if (psn.getInfoSource() == 2) {
      scmCategoryList = this.psnClassificationByPub(psn.getPsnId());
    }

    if (CollectionUtils.isEmpty(scmCategoryList)) {
      return 2;
    } else {
      scmCategoryList = this.duplicateCheck(scmCategoryList);

      if (this.psnDiscScmDao.getClassificationCountByPsnId(psn.getPsnId()) > 0L) {
        this.psnDiscScmDao.deleteClassificationByPsnId(psn.getPsnId());
      }

      for (Long scmCode : scmCategoryList) {
        PsnDiscScm psnDiscScm = new PsnDiscScm();
        psnDiscScm.setPsnId(psn.getPsnId());
        psnDiscScm.setScmDiscNo(scmCode);
        this.psnDiscScmDao.save(psnDiscScm);
      }
    }

    return 1;
  }

  public List<Long> duplicateCheck(List<Long> scmCategoryList) {
    List<Long> rsList = new ArrayList<Long>();
    for (Long str : scmCategoryList) {
      if (!rsList.contains(str)) {
        rsList.add(str);
      }
    }
    return rsList;
  }

  @Override
  public List<PdwhPubForClassification> getIsiPdwhPubs() {
    return this.pdwhPubForClassificationDao.getIsiPubs(size);
  }

  @Override
  public List<PdwhPubForClassification> getCnkiPdwhPubs() {
    return this.pdwhPubForClassificationDao.getCnkiPubs(size);
  }

  @Override
  public void updateClassificationStatus(PdwhPubForClassification pub, Integer status) {
    pub.setStatus(status);
    this.pdwhPubForClassificationDao.save(pub);
  }

  @Override
  public List<PdwhPubForClassificationNsfc> getIsiPdwhPubsNsfc() {
    return this.pdwhPubForClassificationNsfcDao.getIsiPubs(size);
  }

  @Override
  public List<PdwhPubForClassificationNsfc> getCnkiPdwhPubsNsfc() {
    return this.pdwhPubForClassificationNsfcDao.getCnkiPubs(size);
  }

  @Override
  public void updateClassificationStatusNsfc(PdwhPubForClassificationNsfc pub, Integer status) {
    pub.setStatus(status);
    this.pdwhPubForClassificationNsfcDao.save(pub);
  }

  @Override
  public List<NsfcPrjForClassification> getNsfcPrjs() {
    return this.nsfcPrjForClassificationDao.getNsfcPrj(0, size);
  }

  @Override
  public void updateNsfcPrjClassificationStatus(NsfcPrjForClassification prj, Integer status) {
    prj.setStatus(status);
    this.nsfcPrjForClassificationDao.save(prj);
  }

  @Override
  public List<ScmUserForClassification> getScmUserForClassification() {
    return this.scmUserForClassificationDao.getScmUserForClassification(0, size);
  }

  @Override
  public void updateScmUserForClassificationStatus(ScmUserForClassification user, Integer status) {
    user.setStatus(status);
    this.scmUserForClassificationDao.save(user);
  }

  public List<Long> getCnkiToScmCategory(List<Long> categoryIds, List<Long> scmCategoryIdList) {
    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryXx())) {
          bcc.setCategoryXx(bcc.getCategoryXx().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    // List<Long> scmCategoryIdList = new ArrayList<Long>();
    Long id;
    for (BaseConstCategory bcc : list) {
      List<Long> idList = this.crossRefCategoryCnkiScmDao.getScmCategoryIds(bcc.getCategoryXx());
      if (CollectionUtils.isNotEmpty(idList)) {
        id = idList.get(0);
        if (!scmCategoryIdList.contains(id)) {
          scmCategoryIdList.add(id);
        }
      }
    }

    return scmCategoryIdList;
  }

  public List<Long> getIsiToScmCategory(List<Long> categoryIds, List<Long> scmCategoryIdList) {
    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      if (id == null) {
        continue;
      }
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryEn())) {
          bcc.setCategoryEn(bcc.getCategoryEn().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    // List<Long> scmCategoryIdList = new ArrayList<Long>();

    for (BaseConstCategory bcc : list) {
      List<Long> idList = this.crossRefCategoryIsiScmDao.getScmCategoryIds(bcc.getCategoryEn());
      if (CollectionUtils.isNotEmpty(idList)) {
        Long id = idList.get(0);
        if (!scmCategoryIdList.contains(id)) {
          scmCategoryIdList.add(id);
        }
      }
    }
    return scmCategoryIdList;
  }

  public List<String> getIsiToNsfcCategory(List<Long> categoryIds, List<String> nsfcCategoryIdList) {
    List<BaseConstCategory> list = new ArrayList<BaseConstCategory>();
    for (Long id : categoryIds) {
      if (id == null) {
        continue;
      }
      BaseConstCategory bcc = baseConstCategoryDao.get(id);
      if (bcc != null) {
        if (StringUtils.isNotEmpty(bcc.getCategoryEn())) {
          bcc.setCategoryEn(bcc.getCategoryEn().toLowerCase().trim());
          list.add(bcc);
        }
      }
    }

    // List<Long> scmCategoryIdList = new ArrayList<Long>();

    for (BaseConstCategory bcc : list) {
      List<String> idList = this.categoryMapIsiNsfcDao.getNsfcCategoryIds(bcc.getCategoryEn());
      if (CollectionUtils.isNotEmpty(idList)) {
        String nsfcId = idList.get(0);
        if (!nsfcCategoryIdList.contains(nsfcId)) {
          nsfcCategoryIdList.add(nsfcId);
        }
      }
    }
    return nsfcCategoryIdList;
  }

  private List<Long> psnClassificationFromNsfc(Long psnId) {
    List<String> nsfcCategories = this.nsfcPrjForClassificationDao.getNsfcCategoryByPsnId(psnId);
    List<Long> scmCategoryList = new ArrayList<Long>();
    for (String nsfcCategory : nsfcCategories) {
      List<Long> list = new ArrayList<Long>();
      // 优先对比长码，分类更细
      if (nsfcCategory.length() >= 5) {
        nsfcCategory = nsfcCategory.substring(0, 5);
        list = this.crossRefCategoryNsfcScmDao.getScmCategoryByNsfcCategory(nsfcCategory);
      }

      if (CollectionUtils.isEmpty(list)) {
        nsfcCategory = nsfcCategory.substring(0, 3);
        list = this.crossRefCategoryNsfcScmDao.getScmCategoryByNsfcCategory(nsfcCategory);
      }

      scmCategoryList.addAll(list);
    }

    return scmCategoryList;
  }

  private List<Long> psnClassificationByPub(Long psnId) throws DaoException {
    List<Publication> pubList = this.publicationDao.findPubIdsByPsnId(psnId);
    if (pubList == null || pubList.size() == 0) {
      return null;
    }

    ArrayList<Long> scmCategories = new ArrayList<Long>();

    for (Publication pub : pubList) {
      Long pdwhPubId = this.getCorresponddingPdwhPubId(pub);
      List<Long> scmCategoryList = this.pubCategoryDao.getScmCategoryByPubId(pdwhPubId);
      scmCategories.addAll(scmCategoryList);
    }

    return scmCategories;
  }

  private Long getCorresponddingPdwhPubId(Publication pub) {
    String zhTitle = PubHashUtils.cleanTitle(pub.getZhTitle());
    String enTitle = PubHashUtils.cleanTitle(pub.getEnTitle());
    Long doiHash = PubHashUtils.cleanDoiHash(pub.getDoi());
    String[] titleValues = new String[] {zhTitle, enTitle};
    Long enTitleHash = HashUtils.getStrHashCode(enTitle);
    Long zhTitleHash = HashUtils.getStrHashCode(zhTitle);
    Long titleHashValue = PubHashUtils.fingerPrint(titleValues) == null ? 0L : PubHashUtils.fingerPrint(titleValues);

    if (doiHash != null && doiHash != 0L) {
      List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsByDoiHash(doiHash);
      if (dupPubIds != null && dupPubIds.size() > 0) {
        return dupPubIds.get(0);
      }
    }

    List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsByTitle(zhTitleHash, enTitleHash, titleHashValue);
    if (dupPubIds != null && dupPubIds.size() > 0) {
      return dupPubIds.get(0);
    }

    return null;
  }
}
