package com.smate.center.task.service.bdspimp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.bdsp.BdspDataLogRecordDao;
import com.smate.center.task.dao.bdsp.BdspPaperAuthorDao;
import com.smate.center.task.dao.bdsp.BdspPaperBaseDao;
import com.smate.center.task.dao.bdsp.BdspPaperCategoryDao;
import com.smate.center.task.dao.bdsp.BdspPaperCollectionDao;
import com.smate.center.task.dao.bdsp.BdspPaperGamDao;
import com.smate.center.task.dao.bdsp.BdspPaperUnitDao;
import com.smate.center.task.dao.bdsp.BdspPatentBaseDao;
import com.smate.center.task.dao.bdsp.BdspPatentCategoryDao;
import com.smate.center.task.dao.bdsp.BdspPatentGamDao;
import com.smate.center.task.dao.bdsp.BdspPatentInventorDao;
import com.smate.center.task.dao.bdsp.BdspPatentUnitDao;
import com.smate.center.task.dao.bdsp.BdspPrjBaseDao;
import com.smate.center.task.dao.bdsp.BdspPrjCategoryDao;
import com.smate.center.task.dao.bdsp.BdspPrjMemberDao;
import com.smate.center.task.dao.bdsp.BdspPrjUnitDao;
import com.smate.center.task.dao.bdsp.BdspResearchPsnBaseDao;
import com.smate.center.task.dao.bdsp.BdspResearchPsnCategoryDao;
import com.smate.center.task.dao.bdsp.BdspResearchPsnUnitDao;
import com.smate.center.task.dao.journal.BaseJournalDao;
import com.smate.center.task.dao.pdwh.quartz.PatentCategpruNsfcDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAddrInsRecordDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubAuthorSnsPsnRecordDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubCitedTimesDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryNsfcByJournalDao;
import com.smate.center.task.dao.sns.pub.FileDownloadRecordDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.dao.sns.quartz.PsnDiscScmDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectDao;
import com.smate.center.task.dao.snsbak.bdsp.BdspProjectPersonDao;
import com.smate.center.task.model.bdsp.BdspDataConstant;
import com.smate.center.task.model.bdsp.BdspDataForm;
import com.smate.center.task.model.bdsp.BdspPaperAuthor;
import com.smate.center.task.model.bdsp.BdspPaperBase;
import com.smate.center.task.model.bdsp.BdspPaperCategory;
import com.smate.center.task.model.bdsp.BdspPaperCollection;
import com.smate.center.task.model.bdsp.BdspPaperGam;
import com.smate.center.task.model.bdsp.BdspPaperUnit;
import com.smate.center.task.model.bdsp.BdspPatentBase;
import com.smate.center.task.model.bdsp.BdspPatentCategory;
import com.smate.center.task.model.bdsp.BdspPatentGam;
import com.smate.center.task.model.bdsp.BdspPatentInventor;
import com.smate.center.task.model.bdsp.BdspPatentUnit;
import com.smate.center.task.model.bdsp.BdspPrjBase;
import com.smate.center.task.model.bdsp.BdspPrjCategory;
import com.smate.center.task.model.bdsp.BdspPrjMember;
import com.smate.center.task.model.bdsp.BdspPrjUnit;
import com.smate.center.task.model.bdsp.BdspResearchPsnBase;
import com.smate.center.task.model.bdsp.BdspResearchPsnCategory;
import com.smate.center.task.model.bdsp.BdspResearchPsnUnit;
import com.smate.center.task.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.snsbak.bdsp.BdspProject;
import com.smate.center.task.model.snsbak.bdsp.BdspProjectPerson;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubCitationsDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubCitationsPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubStatisticsPO;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * bdsp 任务处理服务
 * 
 * @author zzx
 *
 */
@Service("bdspBuildDataService")
@Transactional(rollbackFor = Exception.class)
public class BdspBuildDataServiceImpl implements BdspBuildDataService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BdspDataLogRecordDao bdspDataLogRecordDao;
  @Autowired
  private BdspResearchPsnBaseDao bdspResearchPsnBaseDao;
  @Autowired
  private BdspResearchPsnUnitDao bdspResearchPsnUnitDao;
  @Autowired
  private BdspResearchPsnCategoryDao bdspResearchPsnCategoryDao;
  @Autowired
  private BdspPrjBaseDao BdspPrjBaseDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private BdspProjectDao BdspProjectDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private BdspPrjCategoryDao bdspPrjCategoryDao;
  @Autowired
  private BdspPrjUnitDao bdspPrjUnitDao;
  @Autowired
  private BdspPrjMemberDao bdspPrjMemberDao;
  @Autowired
  private BdspPaperBaseDao bdspPaperBaseDao;
  @Autowired
  private BdspPaperCollectionDao bdspPaperCollectionDao;
  @Autowired
  private BdspPaperGamDao bdspPaperGamDao;
  @Autowired
  private BdspPaperAuthorDao bdspPaperAuthorDao;
  @Autowired
  private BdspPaperUnitDao bdspPaperUnitDao;
  @Autowired
  private BdspPaperCategoryDao bdspPaperCategoryDao;
  @Autowired
  private BdspPatentBaseDao bdspPatentBaseDao;
  @Autowired
  private BdspPatentInventorDao bdspPatentInventorDao;
  @Autowired
  private BdspPatentUnitDao bdspPatentUnitDao;
  @Autowired
  private BdspPatentCategoryDao bdspPatentCategoryDao;
  @Autowired
  private BdspPatentGamDao bdspPatentGamDao;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDao;
  @Autowired
  private BdspProjectPersonDao bdspProjectPersonDao;
  @Autowired
  private FileDownloadRecordDao fileDownloadRecordDao;
  @Autowired
  private PdwhPubCitedTimesDao pdwhPubCitedTimesDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private PsnDiscScmDao psnDiscScmDao;
  @Autowired
  private PatentCategpruNsfcDao patentCategpruNsfcDao;
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Autowired
  private PdwhPubAuthorSnsPsnRecordDao pdwhPubAuthorSnsPsnRecordDao;
  @Autowired
  private PubCategoryNsfcByJournalDao pubCategoryNsfcByJournalDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDao;
  @Autowired
  private PdwhPubCitationsDAO pdwhPubCitationsDao;
  @Autowired
  private PdwhPubFullTextDAO PdwhPubFullTextDao;

  @Override
  public List<Person> psnList(Integer size) {
    List<Person> list = null;
    List<Long> dataIdList = bdspDataLogRecordDao.findListByType(BdspDataConstant.dataType_psn, size);
    if (dataIdList != null && dataIdList.size() > 0) {
      list = personDao.findListByIds(dataIdList);
    }
    return list;
  }

  @Override
  public void handlePsnInfo(Person one) throws Exception {
    // 科研人员信息保存
    savePsnBaseInfo(one);
    // 科研人员单位信息保存
    savePsnUnitInfo(one);
    // 科研人员分类信息保存
    savePsnCategoryInfo(one);
  }

  @Override
  public List<BdspProject> prjList(Integer size) {
    List<BdspProject> list = null;
    List<Long> dataIdList = bdspDataLogRecordDao.findListByType(BdspDataConstant.dataType_prj, size);
    if (dataIdList != null && dataIdList.size() > 0) {
      list = BdspProjectDao.findByIds(dataIdList);
    }
    return list;
  }

  @Override
  public void handlePrjInfo(BdspProject one) throws Exception {
    BdspDataForm f = new BdspDataForm();
    f.setBdspProject(one);
    // 保存项目基本信息
    savePrjBaseInfo(f);
    // 保存项目分类信息
    savePrjCategoryInfo(one);
    // 保存项目合作单位信息
    savePrjUnitInfo(f);
    // 保存项目成员信息
    savePrjMemberInfo(one);
  }

  @Override
  public List<PubPdwhDetailDOM> paperList(Integer size) {
    List<PubPdwhDetailDOM> list = null;
    List<Long> dataIdList = bdspDataLogRecordDao.findListByType(BdspDataConstant.dataType_paper, size);
    if (dataIdList != null && dataIdList.size() > 0) {
      // list = pdwhPublicationDao.findListByIds(dataIdList);
      list = new ArrayList<PubPdwhDetailDOM>();
      for (Long id : dataIdList) {
        PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDao.findByPubId(id);
        if (pubPdwhDetailDOM != null) {
          list.add(pubPdwhDetailDOM);
        }
      }
    }
    if (list != null && list.size() == 0) {
      return null;
    }
    return list;
  }

  @Override
  public void handlePaperInfo(PubPdwhDetailDOM one) throws Exception {
    // 保存论文基本信息
    savePaperBaseInfo(one);
    // 保存论文收录信息
    savePaperCollectionInfo(one);
    // 保存论文分类信息
    savePaperCategoryInfo(one);
    // 保存论文单位信息
    savePaperUnitInfo(one);
    // 保存论文作者信息
    savePaperAuthorInfo(one);
    // 保存论文社交信息
    savePaperGamInfo(one);
  }

  @Override
  public List<PubPdwhDetailDOM> patentList(Integer size) {
    List<PubPdwhDetailDOM> list = null;
    List<Long> dataIdList = bdspDataLogRecordDao.findListByType(BdspDataConstant.dataType_patent, size);
    if (dataIdList != null && dataIdList.size() > 0) {
      list = new ArrayList<PubPdwhDetailDOM>();
      for (Long id : dataIdList) {
        PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDao.findByPubId(id);
        if (pubPdwhDetailDOM != null && pubPdwhDetailDOM.getPubType() != null) {
          if (pubPdwhDetailDOM.getPubType() == 5) {
            list.add(pubPdwhDetailDOM);
          }
        }
      }
    }
    if (list != null && list.size() == 0) {
      return null;
    }
    return list;
  }

  @Override
  public void handlePatentInfo(PubPdwhDetailDOM one) throws Exception {
    // 保存专利基本信息
    savePatentBaseInfo(one);
    // 保存专利分类信息
    savePatentCategoryInfo(one);
    // 保存专利产权人（单位）信息
    savePatentUnitInfo(one);
    // 保存专利发明人信息
    savePatentInventorInfo(one);
    // 保存专利社交信息
    savePatentGamInfo(one);
  }

  @Override
  public void updateLogRecord(Integer status, String msg, Long dataId, int typeId) {
    if (dataId != null) {
      try {
        if (StringUtils.isNotBlank(msg) && msg.length() > 1000) {
          msg = msg.substring(0, 1000);
        }
        bdspDataLogRecordDao.updateInfo(status, msg, dataId, typeId);
      } catch (Exception e) {
        logger.error("保存Bdsp日志记录失败", e);
      }
    }
  }

  private void savePsnCategoryInfo(Person one) {
    List<Long> ids = psnDiscScmDao.findPsnDiscIds(one.getPersonId());
    if (ids != null && ids.size() > 0) {
      for (Long id : ids) {
        BdspResearchPsnCategory p = new BdspResearchPsnCategory();
        p.setPsnId(one.getPersonId());
        p.setTypeId(1);// 1=smate
        p.setFromId(id);
        p.setSmateId(id);
        // p.setMoeId(moeId);
        // p.setMostId(mostId);
        // p.setNsfcId(nsfcId);
        // p.setSeiId(seiId);
        // TODO
        bdspResearchPsnCategoryDao.save(p);
      }
    }
  }

  private void savePsnUnitInfo(Person one) {
    if (one.getInsId() != null) {
      BdspResearchPsnUnit psnUnit = new BdspResearchPsnUnit();
      psnUnit.setPsnId(one.getPersonId());
      psnUnit.setInsId(one.getInsId());
      bdspResearchPsnUnitDao.save(psnUnit);

    }
  }

  private void savePsnBaseInfo(Person one) {
    BdspResearchPsnBase psnbase = new BdspResearchPsnBase();
    psnbase.setPsnId(one.getPersonId());
    psnbase.setBirthday(one.getBirthday());
    psnbase.setGenderId(one.getSex());
    psnbase.setPosId(one.getPosId());
    // 优先取首要经历 - 其次取id大的
    EducationHistory e = educationHistoryDao.findAccurate(one.getPersonId());
    if (e != null) {
      psnbase.setEduId(e.getEduId());
      psnbase.setDegreeId(getDegreeId(e.getDegree()));
    }
    psnbase.setCreateDate(new Date());
    bdspResearchPsnBaseDao.save(psnbase);
  }

  private Long getDegreeId(String degree) {
    if (StringUtils.isNotBlank(degree)) {
      String idstr = degree.trim().substring(0, 1);
      if (NumberUtils.isNumber(idstr)) {
        return Long.parseLong(idstr);
      }
    }
    return null;
  }

  private void savePrjMemberInfo(BdspProject one) {
    List<BdspProjectPerson> list = bdspProjectPersonDao.findListByPrjCode(one.getPrjCode());
    if (list != null && list.size() > 0) {
      for (BdspProjectPerson psn : list) {
        if (StringUtils.isNotBlank(psn.getOrgName())) {
          Long insId = institutionDao.getInsIdByName(psn.getOrgName(), psn.getOrgName());
          if (insId != null) {
            BdspPrjMember b = new BdspPrjMember();
            b.setPrjId(one.getPrjCode());
            b.setInsId(insId);
            b.setPsnName(psn.getZhName());
            b.setInsName(psn.getOrgName());
            // b.setPsnId(psnId);
            bdspPrjMemberDao.save(b);
          }
        }
      }
    }
  }

  private void savePrjUnitInfo(BdspDataForm f) {
    if (f.getInsId() != null) {
      BdspProject one = f.getBdspProject();
      BdspPrjUnit b = new BdspPrjUnit();
      b.setPrjId(one.getPrjCode());
      b.setInsId(f.getInsId());
      b.setInsName(one.getOrgName());
      bdspPrjUnitDao.save(b);
    }
  }

  private void savePrjCategoryInfo(BdspProject one) {
    BdspPrjCategory b = new BdspPrjCategory();
    b.setPrjId(one.getPrjCode());
    b.setTechId(one.getSubjectId());
    b.setTechTypeId(1L);
    bdspPrjCategoryDao.save(b);
  }

  private void savePrjBaseInfo(BdspDataForm f) {
    BdspProject one = f.getBdspProject();
    BdspPrjBase b = new BdspPrjBase();
    b.setPrjId(one.getPrjCode());
    b.setApplyId(one.getPrpCode());
    b.setSupportId(one.getPrpNo());
    if (StringUtils.isNotBlank(one.getOrgName())) {
      Long insId = institutionDao.getInsIdByName(one.getOrgName(), one.getOrgName());
      f.setInsId(insId);
      b.setOrgId(insId);
    }
    b.setStatYear(one.getStatYear());
    b.setTotalAmt(one.getReqAmt());
    b.setGrantId(one.getGrantCode());
    b.setCreateDate(new Date());
    // b.setPsnId(psnId);
    // TODO
    BdspPrjBaseDao.save(b);
  }

  private void savePaperGamInfo(PubPdwhDetailDOM one) {
    BdspPaperGam b = new BdspPaperGam();
    b.setPubId(one.getPubId());
    Integer dbId = transformDbId(one.getSrcDbId());
    PdwhPubCitationsPO pdwhPubCitationsPO = pdwhPubCitationsDao.getcitesByPubDBId(one.getPubId(), dbId);
    if (pdwhPubCitationsPO != null) {
      b.setCitation(pdwhPubCitationsPO.getCitations());
    }
    PdwhPubStatisticsPO s = pdwhPubStatisticsDao.get(one.getPubId());
    if (s != null && s.getReadCount() != null) {
      b.setView(s.getReadCount());
    }
    // 获取与成果相关的所有全文文件
    List<Long> list = PdwhPubFullTextDao.findFileIdList(one.getPubId());
    if (list != null && list.size() > 0) {
      Integer downloadCount = 0;
      for (Long fileId : list) {
        downloadCount += fileDownloadRecordDao.findCount(fileId);
      }
      b.setDownload(downloadCount);
    }
    bdspPaperGamDao.save(b);
  }


  /**
   * 将基准库成果中的SrcDBid转换为对应的成果引用表中的dbid
   * 
   * @param pwdhDbid
   * @return
   */
  private Integer transformDbId(Integer pwdhDbid) {
    if (pwdhDbid == 11 || pwdhDbid == 21 || pwdhDbid == 31) {
      return 98;
    } else if (pwdhDbid == 15 || pwdhDbid == 16 || pwdhDbid == 17) {
      return 99;
    }
    return null;
  }

  private void savePaperAuthorInfo(PubPdwhDetailDOM one) {
    List<Long> list = pdwhPubAuthorSnsPsnRecordDao.findPsnIdList(one.getPubId());
    if (list != null && list.size() > 0) {
      for (Long psnId : list) {
        Person psn = personDao.findPersonInfoIncludeIns(psnId);
        if (psn != null) {
          BdspPaperAuthor b = new BdspPaperAuthor();
          b.setPubId(one.getPubId());
          b.setPsnId(psnId);
          b.setPsnName(psn.getName());
          b.setInsId(psn.getInsId());
          b.setInsName(psn.getInsName());
          bdspPaperAuthorDao.save(b);
        }

      }
    }
  }

  private void savePaperUnitInfo(PubPdwhDetailDOM one) {
    List<PdwhPubAddrInsRecord> list = pdwhPubAddrInsRecordDao.findListByPubId(one.getPubId());
    if (list != null && list.size() > 0) {
      for (PdwhPubAddrInsRecord p : list) {
        BdspPaperUnit b = new BdspPaperUnit();
        b.setPubId(one.getPubId());
        b.setInsId(p.getInsId());
        b.setInsName(p.getInsName());
        bdspPaperUnitDao.save(b);
      }
    }
  }

  private void savePaperCategoryInfo(PubPdwhDetailDOM one) {
    List<String> categoryList = pubCategoryNsfcByJournalDao.findCategoryByPubId(one.getPubId());
    if (categoryList != null && categoryList.size() > 0) {
      for (String category : categoryList) {
        BdspPaperCategory b = new BdspPaperCategory();
        b.setPubId(one.getPubId());
        b.setTechId(category);
        b.setTechTypeId(1L);// smate
        bdspPaperCategoryDao.save(b);
      }
    }
  }


  private void savePaperBaseInfo(PubPdwhDetailDOM one) {
    BdspPaperBase b = new BdspPaperBase();
    b.setPubId(one.getPubId());
    // 为期刊类型，才存在jid
    if (one.getPubType() == 4) {
      Long jid = ((JournalInfoBean) one.getTypeInfo()).getJid();
      b.setJid(jid);
    }
    // 设置年份
    String publishDate = one.getPublishDate();
    Integer year = (publishDate == null ? null : Integer.parseInt((publishDate.substring(0, 4))));
    b.setPublishYear(year);
    b.setPubType(one.getPubType());
    // 之前从数据库中取得，现在可以直接从mongoDB中取得数据
    if (one.getFundInfo() != null) {
      b.setFundinfo(one.getFundInfo());
    }
    b.setCreateDate(new Date());
    // b.setSupportId(one.get);
    bdspPaperBaseDao.save(b);
  }

  private String getFundinfo(String xml) {
    String result = "";
    try {
      Document document = DocumentHelper.parseText(xml);
      Element root = document.getRootElement();
      Element publication = root.element("publication");
      if (publication != null) {
        String fundinfo = publication.attributeValue("fundinfo");
        if (StringUtils.isNotBlank(fundinfo)) {
          if (fundinfo.length() > 1500) {
            fundinfo = fundinfo.substring(0, 1500);
          }
          result = fundinfo;
        }
      }
    } catch (Exception e) {
      logger.error("解析pubxml fundinfo出错", e);
      return result;
    }
    return result;
  }

  private void savePaperCollectionInfo(PubPdwhDetailDOM one) {
    if (one.getPubType() == 4) {// 为期刊论文
      Long jid = ((JournalInfoBean) one.getTypeInfo()).getJid();
      if (jid != null) {
        Long regionId = baseJournalDao.findRegionId(jid);
        if (regionId != null && regionId != 0L) {
          BdspPaperCollection b = new BdspPaperCollection();
          b.setJid(jid);
          b.setRegionId(regionId);
          bdspPaperCollectionDao.save(b);
        }
      }
    }
  }

  private void savePatentGamInfo(PubPdwhDetailDOM one) {
    BdspPatentGam b = new BdspPatentGam();
    b.setPubId(one.getPubId());
    PdwhPubStatisticsPO s = pdwhPubStatisticsDao.get(one.getPubId());
    if (s != null) {
      b.setView(s.getReadCount());
    }

    List<Long> list = PdwhPubFullTextDao.findFileIdList(one.getPubId());
    if (list != null && list.size() > 0) {
      Integer downloadCount = 0;
      for (Long fileId : list) {
        downloadCount += fileDownloadRecordDao.findCount(fileId);
      }
      b.setDownload(downloadCount);
    }
    // b.setAmt(amt);
    // b.setConvert(convert);
    // TODO
    bdspPatentGamDao.save(b);
  }

  private void savePatentInventorInfo(PubPdwhDetailDOM one) {
    List<Long> list = pdwhPubAuthorSnsPsnRecordDao.findPsnIdList(one.getPubId());
    if (list != null && list.size() > 0) {
      for (Long psnId : list) {
        Person psn = personDao.findPersonInfoIncludeIns(psnId);
        if (psn != null) {
          BdspPatentInventor b = new BdspPatentInventor();
          b.setPubId(one.getPubId());
          b.setPsnId(psnId);
          b.setPsnName(psn.getName());
          b.setInsId(psn.getInsId());
          b.setInsName(psn.getInsName());
          bdspPatentInventorDao.save(b);
        }
      }
    }
  }

  private void savePatentUnitInfo(PubPdwhDetailDOM one) {
    List<PdwhPubAddrInsRecord> list = pdwhPubAddrInsRecordDao.findListByPubId(one.getPubId());
    if (list != null && list.size() > 0) {
      for (PdwhPubAddrInsRecord p : list) {
        BdspPatentUnit b = new BdspPatentUnit();
        b.setPubId(one.getPubId());
        b.setInsId(p.getInsId());
        b.setInsName(p.getInsName());
        bdspPatentUnitDao.save(b);
      }
    }
  }


  private void savePatentCategoryInfo(PubPdwhDetailDOM one) {
    List<String> categoryList = patentCategpruNsfcDao.findCategoryByPubId(one.getPubId());
    if (categoryList != null && categoryList.size() > 0) {
      for (String category : categoryList) {
        BdspPatentCategory b = new BdspPatentCategory();
        b.setPubId(one.getPubId());
        b.setTechId(category);
        b.setTechTypeId(1L);// smate
        bdspPatentCategoryDao.save(b);
      }
    }
  }

  private void savePatentBaseInfo(PubPdwhDetailDOM one) {
    PatentInfoBean patentInfo = null;
    try {
      patentInfo = (PatentInfoBean) (one.getTypeInfo());
    } catch (Exception e) {
      logger.info("当前处理的成果不是专利成果，无法完成专利成果数据修复，成果id为：" + one.getPubId(), e);
    }
    BdspPatentBase b = new BdspPatentBase();
    b.setPubId(one.getPubId());
    // b.setPrpYear(one.getPubYear());
    // PdwhPubXml pub = pdwhPubXmlDao.get(one.getPubId());
    // if(pub!=null) {
    // handleXmlForPatent(pub.getXml(),b);
    // }
    b.setPubType(one.getPubType());
    b.setFundinfo(one.getFundInfo());
    b.setPatentNo(patentInfo.getApplicationNo());
    // 获取申请年份，并截取前面4位字符作为年份
    String applicationDate = (patentInfo.getApplicationDate());
    Integer year = (StringUtils.isBlank(applicationDate) || applicationDate.length() < 4 ? null
        : Integer.parseInt(applicationDate.substring(0, 4)));
    b.setPrpYear(year);
    b.setCreateDate(new Date());
    bdspPatentBaseDao.save(b);
  }

  private void handleXmlForPatent(String xml, BdspPatentBase b) {
    try {
      Document document = DocumentHelper.parseText(xml);
      Element root = document.getRootElement();
      Element publication = root.element("publication");
      if (publication != null) {
        String patent_category = publication.attributeValue("patent_category");
        if (NumberUtils.isNumber(patent_category)) {
          b.setPubType(Integer.parseInt(patent_category));
        }
        String fundinfo = publication.attributeValue("fundinfo");
        if (StringUtils.isNotBlank(fundinfo)) {
          if (fundinfo.length() > 1500) {
            fundinfo = fundinfo.substring(0, 1500);
          }
          b.setFundinfo(fundinfo);
        }
        // 优先读取patent_no，没有则读取patent_reg_no
        String patent_no = publication.attributeValue("patent_no");
        if (StringUtils.isNotBlank(patent_no)) {
          b.setPatentNo(patent_no);
        } else {
          String patent_reg_no = publication.attributeValue("patent_reg_no");
          if (StringUtils.isNotBlank(patent_reg_no)) {
            b.setPatentNo(patent_reg_no);
          }
        }
      }
    } catch (Exception e) {
      logger.error("解析pubxml patent_no出错", e);
    }
  }

  private String objToStr(Object obj) {
    if (obj != null) {
      return obj.toString().trim();
    }
    return "";
  }

  private Long objToLong(Object obj) {
    String str = objToStr(obj);
    if (NumberUtils.isNumber(str)) {
      return Long.parseLong(str);
    }
    return 0L;
  }

  private Integer objToInt(Object obj) {
    String str = objToStr(obj);
    if (NumberUtils.isNumber(str)) {
      return Integer.parseInt(str);
    }
    return 0;
  }

  private Double objToDouble(Object obj) {
    String str = objToStr(obj);
    if (NumberUtils.isNumber(str)) {
      return Double.parseDouble(str);
    }
    return 0.0;
  }

  @Override
  public List<BdspPatentBase> UpdatePatentList(Integer size) {
    return bdspPatentBaseDao.findUpdateList(size);
  }

  @Override
  public void UpdatePatentType(BdspPatentBase one) throws Exception {
    int type = 0;
    try {
      PdwhPubXml pub = pdwhPubXmlDao.get(one.getPubId());
      if (pub != null && StringUtils.isNotBlank(pub.getXml())) {
        Document document = DocumentHelper.parseText(pub.getXml());
        Element root = document.getRootElement();
        Element publication = root.element("publication");
        if (publication != null) {
          String patent_category = publication.attributeValue("patent_category");
          if (NumberUtils.isNumber(patent_category)) {
            type = Integer.parseInt(patent_category);
          }
        }
      }
    } catch (Exception e) {
      logger.error("更新专利类型出错", e);
    }
    one.setPubType(type);
    bdspPatentBaseDao.save(one);
  }

}
