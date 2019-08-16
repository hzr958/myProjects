package com.smate.center.task.v8pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhFullTextFileDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubCitedTimesDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.dao.sns.quartz.ConstRefDbDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.PdwhPubCitedTimes;
import com.smate.center.task.model.pdwh.quartz.PdwhFullTextFile;
import com.smate.center.task.model.pdwh.quartz.PdwhPubXml;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.model.sns.quartz.ConstRefDb;
import com.smate.center.task.service.sns.pub.PubAuthorMatchService;
import com.smate.center.task.utils.PubBuildAuthorNamesUtils;
import com.smate.center.task.utils.PubDbUtils;
import com.smate.center.task.utils.XSSUtils;
import com.smate.center.task.utils.data.PubLocaleUtils;
import com.smate.center.task.v8pub.backups.dao.PubDataBackupsDao;
import com.smate.center.task.v8pub.backups.model.PubDataBackups;
import com.smate.center.task.v8pub.dao.pdwh.BaseJournalDAO;
import com.smate.center.task.v8pub.dao.pdwh.BaseJournalTitleDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberEmailDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhMemberInsNameDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubCitationsDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubCommentDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubKeywordsDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubMemberDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubShareDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubViewDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.BaseJournal2;
import com.smate.center.task.v8pub.pdwh.po.PdwhMemberEmailPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhMemberInsNamePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubCitationsPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubDuplicatePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubMemberPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubSituationPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubStatisticsPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.center.task.v8pub.strategy.PubTypeInfoConstructor;
import com.smate.center.task.v8pub.strategy.PubTypeInfoDriver;
import com.smate.core.base.pub.dao.pdwh.PdwhFullTextImageDao;
import com.smate.core.base.pub.model.pdwh.PdwhFullTextImage;
import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.util.CleanCopyrightUtils;
import com.smate.core.base.pub.util.PdwhPubXMLToJsonStrUtils;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.pubxml.ImportPubXmlUtils;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;

@Service("pubPdwhDataInitService")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhDataInitServiceImpl implements PubPdwhDataInitService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private PubTypeInfoConstructor pubTypeInfoConstructor;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PdwhPubKeywordsDAO pdwhPubKeywordsDAO;
  @Autowired
  private PdwhPubCitedTimesDao pdwhPubCitedTimesDao;
  @Autowired
  private PdwhPubCitationsDAO pdwhPubCitationsDAO;
  @Autowired
  private PdwhPubMemberDAO pdwhPubMemberDAO;
  @Autowired
  private PdwhPubSituationDAO pdwhPubSituationDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private BaseJournalTitleDAO baseJournalTitleDAO;
  @Autowired
  private BaseJournalDAO baseJournalDAO;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PdwhPubViewDAO pdwhPubViewDAO;
  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;
  @Autowired
  private PdwhMemberInsNameDAO pdwhMemberInsNameDAO;
  @Autowired
  private PdwhMemberEmailDAO pdwhMemberEmailDAO;
  @Autowired
  private PdwhFullTextFileDao pdwhFullTextFileDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private PdwhFullTextImageDao pdwhFullTextImageDao;
  @Autowired
  private PubDataBackupsDao pubDataBackupsDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;

  @Override
  public void initDuplicate(PubPdwhDetailDOM pubDetail) throws ServiceException {
    try {
      if (pubDetail != null) {
        Long pdwhPubId = pubDetail.getPubId();
        PdwhPubDuplicatePO pdwhDup = pdwhPubDuplicateDAO.get(pdwhPubId);
        if (pdwhDup != null) {
          Integer dbId = pubDetail.getSrcDbId();
          if (StringUtils.isNotBlank(pubDetail.getDoi())) {
            Long hashCleanDoi = PubHashUtils.getDoiHashRemotePun(pubDetail.getDoi());
            pdwhDup.setHashCleanDoi(NumberUtils.isNullOrZero(hashCleanDoi) ? "" : hashCleanDoi + "");
          }
          if (PubDbUtils.isCnkiDb(dbId)) {
            Long hashCleanCnkiDoi = PubHashUtils.getDoiHashRemotePun(pubDetail.getDoi());
            pdwhDup.setHashCleanCnkiDoi(NumberUtils.isNullOrZero(hashCleanCnkiDoi) ? "" : hashCleanCnkiDoi + "");
          }
          pdwhPubDuplicateDAO.saveOrUpdate(pdwhDup);
        }
        // String hashTP = PubHashUtils.getTpHash(pubDetail.getTitle(),
        // String.valueOf(pubDetail.getPubType()));
        // pdwhDup.setHashTP(hashTP);
        // String publishDate = pubDetail.getPublishDate();
        // if (StringUtils.isNotEmpty(publishDate)) {
        // publishDate = StringUtils.substring(publishDate, 0, 4);
        // Integer publishYear = NumberUtils.toInt(publishDate);
        // if (publishYear != null && publishYear != 0) {
        // String hashTPP = PubHashUtils.getTitleInfoHash(pubDetail.getTitle(), pubDetail.getPubType(),
        // publishYear);
        // pdwhDup.setHashTPP(hashTPP);
        // }
        // }
        // Long hashTitle = PubHashUtils.cleanTitleHash(pubDetail.getTitle());
        // pdwhDup.setHashTitle(NumberUtils.isNullOrZero(hashTitle) ? "" : hashTitle + "");
        // pdwhDup = new PdwhPubDuplicatePO();
        // pdwhDup.setPdwhPubId(pdwhPubId);
        // Integer dbId = pubDetail.getSrcDbId();
        // if (StringUtils.isNotBlank(pubDetail.getDoi())) {
        // Long hashDoi = PubHashUtils.cleanDoiHash(pubDetail.getDoi());
        // pdwhDup.setHashDoi(NumberUtils.isNullOrZero(hashDoi) ? "" : hashDoi + "");
        // }
        // if (PubDbUtils.isCnkiDb(dbId)) {
        // Long hashCnkiDoi = PubHashUtils.cleanDoiHash(pubDetail.getDoi());
        // pdwhDup.setHashCnkiDoi(NumberUtils.isNullOrZero(hashCnkiDoi) ? "" : hashCnkiDoi + "");
        // }
        // if (PubDbUtils.isIsiDb(dbId)) {
        // Long hashIsiSourceId = PubHashUtils.cleanSourceIdHash(pubDetail.getSourceId());
        // pdwhDup.setHashIsiSourceId(NumberUtils.isNullOrZero(hashIsiSourceId) ? "" : hashIsiSourceId +
        // "");
        // }
        // if (PubDbUtils.isEiDb(dbId)) {
        // Long hashEiSourceId = PubHashUtils.cleanSourceIdHash(pubDetail.getSourceId());
        // pdwhDup.setHashEiSourceId(NumberUtils.isNullOrZero(hashEiSourceId) ? "" : hashEiSourceId + "");
        // }
        // if (pubDetail.getPubType() == 5) {
        // // 专利
        // PatentInfoBean p = (PatentInfoBean) pubDetail.getTypeInfo();
        // if (StringUtils.isNotBlank(p.getApplicationNo())) {
        // Long hashApplicationNo = PubHashUtils.cleanPatentNoHash(p.getApplicationNo());
        // pdwhDup.setHashApplicationNo(NumberUtils.isNullOrZero(hashApplicationNo) ? "" : hashApplicationNo
        // + "");
        // }
        // if (StringUtils.isNotBlank(p.getPublicationOpenNo())) {
        // Long hashPublicationOpenNo = PubHashUtils.cleanPatentNoHash(p.getPublicationOpenNo());
        // pdwhDup.setHashPublicationOpenNo(
        // NumberUtils.isNullOrZero(hashPublicationOpenNo) ? "" : hashPublicationOpenNo + "");
        // }
        // }
      }
    } catch (Exception e) {
      logger.error("基准库保存查重数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long initFulltext(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      Long fulltextId = 0L;
      PdwhPubFullTextPO fpo = null;
      List<PdwhFullTextFile> textFileList = pdwhFullTextFileDao.findFileList(pdwhPubId);
      if (textFileList != null && textFileList.size() > 0) {
        for (PdwhFullTextFile textFile : textFileList) {
          if (NumberUtils.isNullOrZero(textFile.getFileId())) {
            continue;
          }
          ArchiveFile archiveFile = archiveFileDao.get(textFile.getFileId());
          if (archiveFile == null) {
            continue;
          }
          fpo = pdwhPubFullTextDAO.findByPubIdAndFileId(pdwhPubId, textFile.getFileId());
          if (fpo == null) {
            fpo = new PdwhPubFullTextPO();
            fpo.setFileId(textFile.getFileId());
            fpo.setPdwhPubId(pdwhPubId);
          }
          fpo.setGmtCreate(textFile.getCreateDate());
          fpo.setFileName(archiveFile.getFileName());
          fpo.setFileDesc(archiveFile.getFileDesc());
          // 基准库成果全文没有权限，默认为0
          fpo.setPermission(0);
          String sourceFulltextUrl = String.valueOf(dataMap.get("srcFulltextUrl"));
          sourceFulltextUrl = sourceFulltextUrl.equalsIgnoreCase("null") ? "" : sourceFulltextUrl;
          fpo.setSourceFulltextUrl(sourceFulltextUrl);
          PdwhFullTextImage pfti = pdwhFullTextImageDao.get(pdwhPubId);
          if (pfti != null && pfti.getFileId().compareTo(textFile.getFileId()) == 0) {
            fpo.setGmtModified(pfti.getUpdateTime());
            fpo.setThumbnailPath(pfti.getImagePath());
          }
          pdwhPubFullTextDAO.saveOrUpdate(fpo);
          if (fulltextId < fpo.getId()) {
            // 最后一个就是最大的fileId
            fulltextId = fpo.getId();
          }
        }
      }
      return fulltextId == 0L ? null : fulltextId;
    } catch (Exception e) {
      logger.error("基准库成果全文初始化出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void initKeywords(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      String keywords = String.valueOf(dataMap.get("keywords"));
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
      pdwhPubKeywordsDAO.savePubKeywords(pdwhPubId, kwList);
    } catch (Exception e) {
      logger.error("初始化关键词数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void initCitations(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      // 引用次数的初始化直接从历史表数据进行导入
      PdwhPubCitationsPO pdwhPubCitationsPO = null;
      pdwhPubCitationsDAO.deleteByPubId(pdwhPubId);
      List<PdwhPubCitedTimes> pcts = pdwhPubCitedTimesDao.listByPubId(pdwhPubId);
      if (pcts != null && pcts.size() > 0) {
        for (PdwhPubCitedTimes pct : pcts) {
          pdwhPubCitationsPO = new PdwhPubCitationsPO();
          pdwhPubCitationsPO.setPdwhPubId(pdwhPubId);
          pdwhPubCitationsPO.setDbId(pct.getDbId());
          pdwhPubCitationsPO.setCitations(pct.getCitedTimes());
          pdwhPubCitationsPO.setType(pct.getType());
          pdwhPubCitationsPO.setGmtModified(pct.getUpdateDate());
          pdwhPubCitationsDAO.saveOrUpdate(pdwhPubCitationsPO);
        }
      } else {
        if (dataMap.get("citations") != null && !"".equals(dataMap.get("citations"))) {
          Integer citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));
          if (dataMap.get("srcDbId") != null) {
            Integer srcDbId = Integer.valueOf(String.valueOf(dataMap.get("srcDbId")));
            pdwhPubCitationsPO = new PdwhPubCitationsPO();
            pdwhPubCitationsPO.setPdwhPubId(pdwhPubId);
            pdwhPubCitationsPO.setDbId(srcDbId);
            citations = (citations == null) ? 0 : citations;
            pdwhPubCitationsPO.setCitations(citations);
            pdwhPubCitationsPO.setType(0);
            pdwhPubCitationsPO.setGmtModified(new Date());
            pdwhPubCitationsDAO.saveOrUpdate(pdwhPubCitationsPO);
          }
        }
      }
    } catch (Exception e) {
      logger.error("初始化基准库引用次数数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void initMembers(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PdwhPubMemberPO pdwhPubMemberPO = null;
      String memberJson = JacksonUtils.listToJsonStr((List) dataMap.get("members"));
      pdwhPubMemberDAO.deleteAllMember(pdwhPubId);
      if (StringUtils.isNotBlank(memberJson) && !"null".equalsIgnoreCase(memberJson)
          && !"[]".equalsIgnoreCase(memberJson)) {
        List<PubMemberDTO> members = JacksonUtils.jsonToCollection(memberJson, List.class, PubMemberDTO.class);
        if (members != null && members.size() > 0) {
          for (PubMemberDTO pubMemberDTO : members) {
            if (StringUtils.isBlank(pubMemberDTO.getName())) {
              continue;
            }
            pdwhPubMemberPO = new PdwhPubMemberPO();
            pdwhPubMemberPO.setPdwhPubId(pdwhPubId);
            pdwhPubMemberPO.setPsnId(pubMemberDTO.getPsnId());
            pdwhPubMemberPO.setSeqNo(pubMemberDTO.getSeqNo());
            pdwhPubMemberPO.setName(pubMemberDTO.getName());
            List<MemberInsDTO> insNames = pubMemberDTO.getInsNames();
            if (insNames != null) {
              pdwhPubMemberPO.setInsCount(pubMemberDTO.getInsNames().size());
            } else {
              pdwhPubMemberPO.setInsCount(0);
            }
            pdwhPubMemberPO.setCommunicable(pubMemberDTO.isCommunicable() ? 1 : 0);
            pdwhPubMemberPO.setEmail(pubMemberDTO.getEmail());
            pdwhPubMemberPO.setFirstAuthor(pubMemberDTO.isFirstAuthor() ? 1 : 0);
            pdwhPubMemberPO.setOwner(0);
            pdwhPubMemberDAO.save(pdwhPubMemberPO);
          }
        }
      }
    } catch (Exception e) {
      logger.error("初始化成果作者数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void initSitation(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PdwhPubSituationPO pdwhPubSituationPO = null;
      String sitationJson = JacksonUtils.listToJsonStr((List) dataMap.get("situations"));
      if (StringUtils.isNotBlank(sitationJson) && !"null".equalsIgnoreCase(sitationJson)
          && !"[]".equalsIgnoreCase(sitationJson)) {
        pdwhPubSituationDAO.deleteAll(pdwhPubId);
        List<PubSituationDTO> pubInfoList =
            JacksonUtils.jsonToCollection(sitationJson, List.class, PubSituationDTO.class);
        if (pubInfoList != null && pubInfoList.size() > 0) {
          for (PubSituationDTO pubSituationDTO : pubInfoList) {
            pdwhPubSituationPO = new PdwhPubSituationPO();
            pdwhPubSituationPO.setPdwhPubId(pdwhPubId);
            pdwhPubSituationPO.setLibraryName(pubSituationDTO.getLibraryName());
            pdwhPubSituationPO.setSitStatus(pubSituationDTO.isSitStatus() ? 1 : 0);
            pdwhPubSituationPO.setSitOriginStatus(pubSituationDTO.isSitOriginStatus() ? 1 : 0);
            pdwhPubSituationPO.setSrcDbId(pubSituationDTO.getSrcDbId());
            pdwhPubSituationPO.setSrcId(pubSituationDTO.getSrcId());
            pdwhPubSituationPO.setSrcUrl(pubSituationDTO.getSrcUrl());
            pdwhPubSituationPO.setGmtCreate(new Date());
            pdwhPubSituationPO.setGmtModified(new Date());
            pdwhPubSituationDAO.saveOrUpdate(pdwhPubSituationPO);
          }
        }
      }
    } catch (Exception e) {
      logger.error("初始化基准库成果引用情况数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initPubPdwh(Map<String, Object> dataMap, PdwhPublication pdwhPublication) throws ServiceException {
    try {
      Long pdwhPubId = pdwhPublication.getPubId();
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pdwhPubId);
      if (pubPdwhPO != null) {
        String title = String.valueOf(dataMap.get("title"));
        pubPdwhPO.setTitle(title);
        pubPdwhDAO.saveOrUpdate(pubPdwhPO);
      }
      // if (pubPdwhPO == null) {
      // pubPdwhPO = new PubPdwhPO();
      // pubPdwhPO.setGmtCreate(new Date());
      // pubPdwhPO.setStatus(PubPdwhStatusEnum.DEFAULT);
      // pubPdwhPO.setPubId(pdwhPubId);
      // }
      // Integer pubType = Integer.valueOf(String.valueOf(dataMap.get("pubType")));
      // pubPdwhPO.setPubType(pubType);
      // String publishDate = String.valueOf(dataMap.get("publishDate"));
      // if (StringUtils.isNotBlank(publishDate)) {
      // publishDate = StringUtils.trim(publishDate);
      // Integer publishYear = Integer.valueOf(StringUtils.substring(publishDate, 0, 4));
      // pubPdwhPO.setPublishYear(publishYear);
      // }
      // String authorNames = String.valueOf(dataMap.get("authorNames"));
      // pubPdwhPO.setAuthorNames(authorNames);
      // // 引用次数
      // Integer citations = 0;
      // if (dataMap.get("citations") != null && !"".equals(dataMap.get("citations"))) {
      // citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));
      // if (dataMap.get("srcDbId") != null) {
      // Integer srcDbId = Integer.valueOf(String.valueOf(dataMap.get("srcDbId")));
      // citations = PubParamUtils.resetCitation(srcDbId, citations);
      // }
      // }
      // citations = (citations == null) ? 0 : citations;
      // pubPdwhPO.setCitations(citations);
      // // 国家或地区id
      // Long countryId = null;
      // if (dataMap.get("countryId") != null) {
      // countryId = Long.valueOf(String.valueOf(dataMap.get("countryId")));
      // }
      // pubPdwhPO.setCountryId(countryId);
      // // 更新标记
      // pubPdwhPO.setUpdateMark(1);
      // pubPdwhPO.setCreatePsnId(pdwhPublication.getCreatePsnId());
      // pubPdwhPO.setPublishYear(pdwhPublication.getPubYear());
      // pubPdwhPO.setGmtModified(pdwhPublication.getUpdateDate());
      // pubPdwhPO.setGmtCreate(pdwhPublication.getCreateDate());

    } catch (Exception e) {
      logger.error("初始化基准库主表数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public PubPdwhDetailDOM initPubDetail(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pdwhPubId);
      // if (pubPdwhDetailDOM == null) {
      // pubPdwhDetailDOM = new PubPdwhDetailDOM();
      // pubPdwhDetailDOM.setPubId(pdwhPubId);
      // }
      // buildPdwhPubMain(dataMap, pubPdwhDetailDOM);
      // buildPdwhPubMembers(dataMap, pubPdwhDetailDOM);
      // buildPdwhPubSituations(dataMap, pubPdwhDetailDOM);
      // if (pubPdwhDetailDOM != null) {
      // // 标题
      // String title = String.valueOf(dataMap.get("title"));
      // pubPdwhDetailDOM.setTitle(title);
      // pubPdwhDetailDAO.save(pubPdwhDetailDOM);
      // }
      return pubPdwhDetailDOM;
    } catch (Exception e) {
      logger.error("初始化基准库成果详情数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void buildPdwhPubSituations(Map<String, Object> dataMap, PubPdwhDetailDOM pubPdwhDetailDOM) {
    try {
      PubSituationBean pubSituationBean = null;
      Set<PubSituationBean> situationSet = new HashSet<>();
      String sitationJson = JacksonUtils.listToJsonStr((List) dataMap.get("situations"));
      if (StringUtils.isNotBlank(sitationJson) && !"null".equalsIgnoreCase(sitationJson)
          && !"[]".equalsIgnoreCase(sitationJson)) {
        List<PubSituationDTO> pubInfoList =
            JacksonUtils.jsonToCollection(sitationJson, List.class, PubSituationDTO.class);
        if (pubInfoList != null && pubInfoList.size() > 0) {
          for (PubSituationDTO pubSituationDTO : pubInfoList) {
            pubSituationBean = new PubSituationBean();
            pubSituationBean.setLibraryName(pubSituationDTO.getLibraryName());
            pubSituationBean.setSitStatus(pubSituationDTO.isSitStatus());
            pubSituationBean.setSitOriginStatus(pubSituationDTO.isSitOriginStatus());
            pubSituationBean.setSrcDbId(pubSituationDTO.getSrcDbId());
            pubSituationBean.setSrcId(pubSituationDTO.getSrcId());
            pubSituationBean.setSrcUrl(pubSituationDTO.getSrcUrl());
            situationSet.add(pubSituationBean);
          }
          pubPdwhDetailDOM.setSituations(situationSet);
        }
      }
    } catch (Exception e) {
      logger.error("初始化详情构造成果收录情况对象出错！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void buildPdwhPubMembers(Map<String, Object> dataMap, PubPdwhDetailDOM pubPdwhDetailDOM) {
    try {
      PubMemberBean pubMemberBean = null;
      List<PubMemberBean> members = new ArrayList<>();
      String memberJson = JacksonUtils.listToJsonStr((List) dataMap.get("members"));
      if (StringUtils.isNotBlank(memberJson) && !"null".equalsIgnoreCase(memberJson)
          && !"[]".equalsIgnoreCase(memberJson)) {
        List<PubMemberDTO> memberList = JacksonUtils.jsonToCollection(memberJson, List.class, PubMemberDTO.class);
        if (memberList != null && memberList.size() > 0) {
          for (PubMemberDTO pubMemberDTO : memberList) {
            if (StringUtils.isBlank(pubMemberDTO.getName())) {
              continue;
            }
            pubMemberBean = new PubMemberBean();
            pubMemberBean.setPsnId(pubMemberDTO.getPsnId());
            pubMemberBean.setSeqNo(pubMemberDTO.getSeqNo());
            pubMemberBean.setName(pubMemberDTO.getName());
            pubMemberBean.setInsNames(new ArrayList<MemberInsBean>());
            pubMemberBean.setCommunicable(pubMemberDTO.isCommunicable());
            pubMemberBean.setEmail(pubMemberDTO.getEmail());
            pubMemberBean.setDept(pubMemberDTO.getDept());
            pubMemberBean.setFirstAuthor(pubMemberDTO.isFirstAuthor());
            members.add(pubMemberBean);
          }
          pubPdwhDetailDOM.setMembers(members);
        }
      }
    } catch (Exception e) {
      logger.error("初始化详情构造成果作者对象出错！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings("unchecked")
  private void buildPdwhPubMain(Map<String, Object> dataMap, PubPdwhDetailDOM pubPdwhDetailDOM) {
    try {
      // 标题
      String title = String.valueOf(dataMap.get("title"));
      pubPdwhDetailDOM.setTitle(title);
      // 摘要
      String summary = String.valueOf(dataMap.get("summary"));
      pubPdwhDetailDOM.setSummary(summary);
      // 作者名
      String authorNames = String.valueOf(dataMap.get("authorNames"));
      pubPdwhDetailDOM.setAuthorNames(authorNames);
      // 引用数
      Integer citations = 0;
      if (dataMap.get("citations") != null && !"".equals(dataMap.get("citations"))) {
        citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));
        if (dataMap.get("srcDbId") != null) {
          Integer srcDbId = Integer.valueOf(String.valueOf(dataMap.get("srcDbId")));
          citations = PubParamUtils.resetCitation(srcDbId, citations);
        }
      }
      citations = (citations == null) ? 0 : citations;
      pubPdwhDetailDOM.setCitations(citations);
      // citedUrl
      String citedUrl = String.valueOf(dataMap.get("citedUrl"));
      pubPdwhDetailDOM.setCitedUrl(citedUrl);
      // sourceUrl
      String sourceUrl = String.valueOf(dataMap.get("sourceUrl"));
      pubPdwhDetailDOM.setSourceUrl(sourceUrl);
      // srcDbId
      if (dataMap.get("srcDbId") != null) {
        Integer srcDbId = Integer.valueOf(String.valueOf(dataMap.get("srcDbId")));
        pubPdwhDetailDOM.setSrcDbId(srcDbId);
      }
      // countryId
      Long countryId = null;
      if (dataMap.get("countryId") != null) {
        countryId = Long.valueOf(String.valueOf(dataMap.get("countryId")));
      }
      pubPdwhDetailDOM.setCountryId(countryId);
      // DOI
      String doi = String.valueOf(dataMap.get("doi"));
      pubPdwhDetailDOM.setDoi(doi);
      // 全文fulltextId
      Long fulltextId = null;
      if (dataMap.get("fulltextId") != null) {
        fulltextId = Long.valueOf(String.valueOf(dataMap.get("fulltextId")));
      }
      pubPdwhDetailDOM.setFulltextId(fulltextId);
      // 基金信息
      String fundInfo = String.valueOf(dataMap.get("fundInfo"));
      pubPdwhDetailDOM.setFundInfo(fundInfo);
      // 关键词
      String keywords = String.valueOf(dataMap.get("keywords"));
      pubPdwhDetailDOM.setKeywords(keywords);
      // organization
      String organization = String.valueOf(dataMap.get("organization"));
      pubPdwhDetailDOM.setOrganization(organization);
      // 成果发布日期
      String publishDate = String.valueOf(dataMap.get("publishDate"));
      pubPdwhDetailDOM.setPublishDate(publishDate);
      // 原始全文地址
      String srcFulltextUrl = String.valueOf(dataMap.get("srcFulltextUrl"));
      srcFulltextUrl = srcFulltextUrl.equalsIgnoreCase("null") ? "" : srcFulltextUrl;
      pubPdwhDetailDOM.setSrcFulltextUrl(srcFulltextUrl);
      // sourceId
      String sourceId = String.valueOf(dataMap.get("sourceId"));
      pubPdwhDetailDOM.setSourceId(sourceId);
      // 成果类别
      Integer pubType = Integer.valueOf(String.valueOf(dataMap.get("pubType")));
      pubPdwhDetailDOM.setPubType(pubType);
      // 简要描述
      String briefDesc = String.valueOf(dataMap.get("briefDesc"));
      pubPdwhDetailDOM.setBriefDesc(briefDesc);
      // 成果对象
      PubTypeInfoBean typeInfo = buildTypeInfo(dataMap.get("pubTypeInfo"), pubType);
      pubPdwhDetailDOM.setTypeInfo(typeInfo);
      // HP
      pubPdwhDetailDOM.setHP(false);
      // HCP
      pubPdwhDetailDOM.setHCP(false);
      // OA
      pubPdwhDetailDOM.setOA("");
    } catch (Exception e) {
      logger.error("成果详情主字段构造出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initPubStatistics(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PdwhPubStatisticsPO pdwhPubStatisticsPO = null;
      String title = String.valueOf(dataMap.get("title"));
      boolean isChina = PubLocaleUtils.getLocale(title).equals(Locale.CHINA);
      Integer awardCount = initPubLike(pdwhPubId);
      Integer commentCount = initPubComment(pdwhPubId);
      Integer readCount = initPubView(pdwhPubId);
      Integer shareCount = initPubShare(pdwhPubId, isChina);
      Integer citedCount = 0;
      if (dataMap.get("citations") != null && !"".equals(dataMap.get("citations"))) {
        citedCount = Integer.valueOf(String.valueOf(dataMap.get("citations")));
        if (dataMap.get("srcDbId") != null) {
          Integer srcDbId = Integer.valueOf(String.valueOf(dataMap.get("srcDbId")));
          citedCount = PubParamUtils.resetCitation(srcDbId, citedCount);
        }
      }
      citedCount = (citedCount == null) ? 0 : citedCount;
      pdwhPubStatisticsPO = pdwhPubStatisticsDAO.get(pdwhPubId);
      if (pdwhPubStatisticsPO == null) {
        pdwhPubStatisticsPO =
            new PdwhPubStatisticsPO(pdwhPubId, awardCount, shareCount, commentCount, readCount, citedCount);
      } else {
        pdwhPubStatisticsPO.setAwardCount(awardCount);
        pdwhPubStatisticsPO.setCommentCount(commentCount);
        pdwhPubStatisticsPO.setReadCount(readCount);
        pdwhPubStatisticsPO.setShareCount(shareCount);
        pdwhPubStatisticsPO.setRefCount(citedCount);
      }
      pdwhPubStatisticsDAO.saveOrUpdate(pdwhPubStatisticsPO);
    } catch (Exception e) {
      logger.error("初始化基准库成果统计数数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Integer initPubLike(Long pdwhPubId) throws ServiceException {
    try {
      Long awardCount = pdwhPubLikeDAO.getAwardCount(pdwhPubId);
      awardCount = (awardCount == null) ? 0 : awardCount;
      return awardCount.intValue();
    } catch (Exception e) {
      logger.error("初始化基准库成果赞记录数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer initPubShare(Long pdwhPubId, boolean isChina) throws ServiceException {
    try {
      Long shareCount = pdwhPubShareDAO.getShareCount(pdwhPubId);
      shareCount = (shareCount == null) ? 0 : shareCount;
      return shareCount.intValue();
    } catch (Exception e) {
      logger.error("初始化基准库成果分享记录数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer initPubComment(Long pdwhPubId) throws ServiceException {
    try {
      Long commentCount = pdwhPubCommentDAO.getCommentsCount(pdwhPubId);
      commentCount = (commentCount == null) ? 0L : commentCount;
      return commentCount.intValue();
    } catch (Exception e) {
      logger.error("初始化基准库成果评论记录数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer initPubView(Long pdwhPubId) throws ServiceException {
    try {
      Long readCount = pdwhPubViewDAO.getViewCount(pdwhPubId);
      readCount = (readCount == null) ? 0 : readCount;
      return readCount.intValue();
    } catch (Exception e) {
      logger.error("初始化基准库成果阅读记录数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void constructData(Map<String, Object> dataMap) {
    // 重新构造srcDbId
    buildSrcDbId(dataMap);
    // 重新构造countryId
    buildCountryId(dataMap);
    // 构造字段authorNames字段
    buildAuthorNames(dataMap);
    // 构造字段keywords字段
    buildKeywords(dataMap);
    // 摘要去除版权信息
    cleanSummary(dataMap);
    // 构造字段briefDesc
    try {
      buildBriefDesc(dataMap);
    } catch (Exception e) {
      logger.error("构造briefDesc参数出错", e);
    }
  }

  private void buildKeywords(Map<String, Object> dataMap) {
    String keywords = (String) dataMap.get("keywords");
    if (StringUtils.isNotBlank(keywords)) {
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
      if (CollectionUtils.isNotEmpty(kwList)) {
        keywords = kwList.stream().map(keyword -> keyword).collect(Collectors.joining("; "));
      }
    }
    dataMap.remove("keywords");
    dataMap.put("keywords", keywords);
  }

  private void cleanSummary(Map<String, Object> dataMap) {
    String summary = "";
    if (dataMap.get("summary") != null && !"".equals(dataMap.get("summary"))) {
      summary = (String) dataMap.get("summary");
      summary = CleanCopyrightUtils.cleanSummary(summary);
    }
    dataMap.remove("summary");
    dataMap.put("summary", summary);
  }

  private void buildAuthorNames(Map<String, Object> dataMap) {
    String authorNames = "";
    if (dataMap.get("authorNames") != null && !"".equals(dataMap.get("authorNames"))) {
      authorNames = (String) dataMap.get("authorNames");
      List<String> authorList = AuthorNamesUtils.parsePubAuthorNames(authorNames);
      if (authorList != null && authorList.size() > 0) {
        authorNames = PubBuildAuthorNamesUtils.buildPdwhPubAuthorNames(authorList);
      }
      authorNames = XmlUtil.subStr500char(authorNames);
      authorNames = XmlUtil.cleanAuthorsAddr(authorNames);
      if (authorNames.length() >= 2) {
        authorNames = XmlUtil.formatPubAuthorKws(authorNames);
      } else {
        authorNames = "";
      }
    }
    dataMap.remove("authorNames");
    dataMap.put("authorNames", authorNames);
  }

  private void buildBriefDesc(Map<String, Object> dataMap) throws Exception {
    Integer pubType = Integer.valueOf(dataMap.get("pubType").toString());
    PubTypeInfoDriver pubTypeInfoDriver = pubTypeInfoConstructor.getPubTypeInfoDriver(pubType);
    PubTypeInfoBean typeInfo = buildTypeInfo(dataMap.get("pubTypeInfo"), pubType);
    String title = "";
    if (dataMap.get("title") != null && !"".equals(dataMap.get("title"))) {
      title = (String) dataMap.get("title");
    }
    Locale locale = PubLocaleUtils.getLocale(title);
    Long countryId = null;
    if (dataMap.get("countryId") != null) {
      countryId = Long.valueOf(String.valueOf(dataMap.get("countryId")));
    }
    String publishDate = String.valueOf(dataMap.get("publishDate"));
    String briefDesc = pubTypeInfoDriver.constructBriefDesc(typeInfo, locale, countryId, publishDate);
    dataMap.put("briefDesc", briefDesc);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildSrcDbId(Map<String, Object> map) {
    Long srcDbId = null;
    if (map.get("srcDbId") != null) {
      ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(map.get("srcDbId").toString());
      if (constRefDb != null) {
        srcDbId = constRefDb.getId();
        map.remove("srcDbId");
        map.put("srcDbId", srcDbId);
      }
    }
    // 填充引用情况记录对象中的srcDbId
    if (map.get("situations") != null) {
      String sitJson = JacksonUtils.listToJsonStr((List) map.get("situations"));
      if (StringUtils.isNotBlank(sitJson) && !"null".equalsIgnoreCase(sitJson) && !"[]".equalsIgnoreCase(sitJson)) {
        List<PubSituationDTO> situations = JacksonUtils.jsonToCollection(sitJson, List.class, PubSituationDTO.class);
        if (situations != null && situations.size() > 0) {
          for (PubSituationDTO pubSituationDTO : situations) {
            String dbCode = pubSituationDTO.getLibraryName();
            if (StringUtils.isNotBlank(dbCode)) {
              ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(dbCode);
              if (constRefDb != null) {
                pubSituationDTO.setSrcDbId(constRefDb.getId() + "");
              }
            }
          }
          map.remove("situations");
          map.put("situations", situations);
        }
      }
    }
  }

  public void buildCountryId(Map<String, Object> map) {
    Long countryId = null;
    Object country = map.get("country");
    Object city = map.get("city");
    if (country != null && !"".equals(country)) {
      countryId = constRegionDao.getRegionIdByCityName(String.valueOf(country));
    } else if (city != null && !"".equals(city)) {
      countryId = constRegionDao.getRegionIdByCityName(String.valueOf(city));
    }
    map.remove("city");
    map.remove("country");
    map.put("countryId", countryId);
  }

  @SuppressWarnings("rawtypes")
  public PubTypeInfoBean buildTypeInfo(Object typeInfo, Integer pubType) {
    PubTypeInfoBean typeInfoBean = null;
    String json = JacksonUtils.mapToJsonStr((Map) typeInfo);
    switch (pubType) {
      case 1:
        typeInfoBean = JacksonUtils.jsonObject(json, AwardsInfoBean.class);
        break;
      case 2:
      case 10:
        typeInfoBean = JacksonUtils.jsonObject(json, BookInfoBean.class);
        break;
      case 3:
        typeInfoBean = JacksonUtils.jsonObject(json, ConferencePaperBean.class);
        break;
      case 4:
        JournalInfoBean journalInfoBean = JacksonUtils.jsonObject(json, JournalInfoBean.class);
        // 增加期刊匹配的逻辑
        typeInfoBean = journalMatch(journalInfoBean);
        break;
      case 5:
        typeInfoBean = JacksonUtils.jsonObject(json, PatentInfoBean.class);
        break;
      case 8:
        typeInfoBean = JacksonUtils.jsonObject(json, ThesisInfoBean.class);
        break;
      case 7:
        typeInfoBean = null;
        break;
      default:
        typeInfoBean = null;
        break;
    }
    return typeInfoBean;
  }

  private JournalInfoBean journalMatch(JournalInfoBean journal) {
    Long baseJnlId = null;
    // 参数的处理
    String jname = XmlUtil.changeSBCChar(journal.getName());
    String jissn = XmlUtil.buildStandardIssn(XmlUtil.changeSBCChar(journal.getISSN()));
    // jname和jissn均不为空时才进行基准库期刊匹配
    if (StringUtils.isBlank(jname) || StringUtils.isBlank(jissn)) {
      return journal;
    }
    baseJnlId = baseJournalTitleDAO.searchJournalMatchBaseJnlId(jname, jissn);
    if (NumberUtils.isNullOrZero(baseJnlId)) {
      return journal;
    }
    BaseJournal2 b2 = baseJournalDAO.getBaseJournal2Title(baseJnlId);
    if (b2 == null) {
      return journal;
    }
    boolean isChina = PubLocaleUtils.getLocale(journal.getName()).equals(Locale.CHINA);
    String name = isChina ? StringUtils.isNotBlank(b2.getTitleXx()) ? b2.getTitleXx() : b2.getTitleEn()
        : StringUtils.isNotBlank(b2.getTitleEn()) ? b2.getTitleEn() : b2.getTitleXx();
    journal.setName(name);
    journal.setJid(baseJnlId);
    return journal;
  }

  @Override
  public void initPdwhPubDetail(Long pubId, PubPdwhDetailDOM pdwhDetail) throws ServiceException {
    try {
      if (pdwhDetail != null) {
        PubDataBackups pubDataBackups = pubDataBackupsDao.get(pubId);
        if (pubDataBackups == null) {
          pubDataBackups = new PubDataBackups();
          pubDataBackups.setPubId(pubId);
          pubDataBackups.setDataType(1);
        }
        pubDataBackups.setStatus(0);
        pubDataBackupsDao.saveOrUpdate(pubDataBackups);
      }
    } catch (Exception e) {
      logger.error("基准库备份表记录出错！", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public void dealWithMember(Long pdwhPubId) {
    try {
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pdwhPubId);
      if (pubPdwhDetailDOM != null) {
        // xml中pub_author转换出来的authors数据
        List<PubMemberDTO> authors = getByXmlAuthors(pubPdwhDetailDOM.getPubId());
        // json里面的members数据
        List<PubMemberDTO> memberList =
            pubAuthorMatchService.perfectMembers(pubPdwhDetailDOM.getAuthorNames(), authors);
        /**
         * 此表保存必须第一位
         */
        updatePdwhMember(memberList, pdwhPubId);// 1.更新表数据V_PUB_PDWH_MEMBER作者信息表
        updateDetailMembers(memberList, pubPdwhDetailDOM);// 2.更新json中的members字段
        saveMemberInsName(memberList, pdwhPubId);// 3.保存表V_PUB_PDWH_MEMBER_INSNAME
        saveMemberEmail(authors, pdwhPubId);// 4.保存表V_PUB_PDWH_MEMBER_EMAIL
      }
    } catch (Exception e) {
      logger.error("基准库作者名数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private List<PubMemberDTO> getByXmlAuthors(Long pdwhPubId) {
    List<PubMemberDTO> authors = new ArrayList<>();
    PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(pdwhPubId);
    if (pdwhPubXml != null) {
      String jsonData = PdwhPubXMLToJsonStrUtils.dealWithXML(pdwhPubXml.getXml());
      // jsonData数据的处理
      jsonData = XSSUtils.xssReplace(jsonData);
      Map<String, Object> dataMap = JacksonUtils.json2HashMap(jsonData);
      if (dataMap != null) {
        String memberJson = JacksonUtils.listToJsonStr((List) dataMap.get("members"));
        if (StringUtils.isNotBlank(memberJson) && !"null".equalsIgnoreCase(memberJson)
            && !"[]".equalsIgnoreCase(memberJson)) {
          authors = JacksonUtils.jsonToCollection(memberJson, List.class, PubMemberDTO.class);
        }
      }
    }
    return authors;
  }

  @SuppressWarnings("unchecked")
  private List<PubMemberDTO> getByMongodbMembers(PubPdwhDetailDOM pubPdwhDetailDOM) {
    List<PubMemberDTO> authors = new ArrayList<>();
    List<PubMemberBean> members = pubPdwhDetailDOM.getMembers();
    if (CollectionUtils.isNotEmpty(members)) {
      for (PubMemberBean pubMemberBean : members) {
        PubMemberDTO pubMemberDTO = new PubMemberDTO();
        pubMemberDTO.setName(pubMemberBean.getName());
        pubMemberDTO.setEmail(pubMemberBean.getEmail());
        pubMemberDTO.setDept(pubMemberBean.getDept());
        pubMemberDTO.setSeqNo(pubMemberBean.getSeqNo());
        pubMemberDTO.setPsnId(pubMemberBean.getPsnId());
        pubMemberDTO.setFirstAuthor(pubMemberBean.isFirstAuthor());
        pubMemberDTO.setCommunicable(pubMemberBean.isCommunicable());
        authors.add(pubMemberDTO);
      }
    }

    return authors;
  }

  private void saveMemberEmail(List<PubMemberDTO> authors, Long pdwhPubId) {
    pdwhMemberEmailDAO.deleteAll(pdwhPubId);
    // 构造成果的邮件地址
    Set<String> emails = new HashSet<>();
    if (CollectionUtils.isNotEmpty(authors)) {
      for (PubMemberDTO bean : authors) {
        List<String> eList = parseEmail(bean.getEmail());
        if (CollectionUtils.isNotEmpty(eList)) {
          for (String email : eList) {
            if (StringUtils.isNotBlank(email)) {
              emails.add(email);
            }
          }
        }
      }
    }
    // 保存成果邮件
    if (CollectionUtils.isNotEmpty(emails)) {
      for (String email : emails) {
        if (StringUtils.isNotBlank(email)) {
          PdwhMemberEmailPO pdwhMemberEmailPO = new PdwhMemberEmailPO();
          pdwhMemberEmailPO.setPdwhPubId(pdwhPubId);
          pdwhMemberEmailPO.setEmail(email);
          pdwhMemberEmailDAO.save(pdwhMemberEmailPO);
        }
      }
    }

  }

  public static List<String> parseEmail(String emails) {
    if (StringUtils.isBlank(emails)) {
      return new ArrayList<>();
    }
    String[] eList = emails.split("[; ]");
    List<String> list = new ArrayList<String>();
    if (eList != null && eList.length > 0) {
      for (String email : eList) {
        email = email.trim();
        email = StringUtils.trimToEmpty(email);
        if (StringUtils.isNotBlank(email)) {
          list.add(email);
        }
      }
    }
    return list;
  }

  private void saveMemberInsName(List<PubMemberDTO> memberList, Long pdwhPubId) {
    if (CollectionUtils.isNotEmpty(memberList)) {
      PdwhMemberInsNamePO insNamePO = null;
      for (PubMemberDTO pubMemberDTO : memberList) {
        Set<String> depts = pubMemberDTO.getDepts();
        if (CollectionUtils.isNotEmpty(depts)) {
          for (String dept : depts) {
            insNamePO = new PdwhMemberInsNamePO();
            insNamePO.setMemberId(pubMemberDTO.getMemberId());
            dept = StringUtils.trimToEmpty(dept);
            if (StringUtils.isNotBlank(dept)) {
              dept = StringUtils.substring(dept, 0, 3500);
              insNamePO.setDept(dept);
              pdwhMemberInsNameDAO.saveOrUpdate(insNamePO);
            }
          }
        }
      }
    }
  }

  private void updatePdwhMember(List<PubMemberDTO> memberList, Long pdwhPubId) {
    // 先删除insName表
    pdwhMemberInsNameDAO.deleteAll(pdwhPubId);
    pdwhPubMemberDAO.deleteAllMember(pdwhPubId);
    if (CollectionUtils.isNotEmpty(memberList)) {
      PdwhPubMemberPO pdwhPubMemberPO = null;
      for (PubMemberDTO pubMemberDTO : memberList) {
        if (StringUtils.isBlank(pubMemberDTO.getName())) {
          continue;
        }
        pdwhPubMemberPO = new PdwhPubMemberPO();
        pdwhPubMemberPO.setPdwhPubId(pdwhPubId);
        pdwhPubMemberPO.setPsnId(pubMemberDTO.getPsnId());
        pdwhPubMemberPO.setName(pubMemberDTO.getName());
        pdwhPubMemberPO.setEmail(pubMemberDTO.getEmail());
        pdwhPubMemberPO.setSeqNo(pubMemberDTO.getSeqNo());
        pdwhPubMemberPO.setOwner(pubMemberDTO.isOwner() ? 1 : 0);
        pdwhPubMemberPO.setCommunicable(pubMemberDTO.isCommunicable() ? 1 : 0);
        pdwhPubMemberPO.setFirstAuthor(pubMemberDTO.isFirstAuthor() ? 1 : 0);
        List<MemberInsDTO> insList = pubMemberDTO.getInsNames();
        if (CollectionUtils.isNotEmpty(insList) && insList != null) {
          pdwhPubMemberPO.setInsCount(insList.size());
        } else {
          pdwhPubMemberPO.setInsCount(0);
        }
        pdwhPubMemberDAO.saveOrUpdate(pdwhPubMemberPO);
        pubMemberDTO.setMemberId(pdwhPubMemberPO.getId());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void updateDetailMembers(List<PubMemberDTO> memberList, PubPdwhDetailDOM pubPdwhDetailDOM) {
    List<PubMemberBean> members = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(memberList)) {
      PubMemberBean bean = null;
      for (PubMemberDTO pubMemberDTO : memberList) {
        bean = new PubMemberBean();
        bean.setPsnId(pubMemberDTO.getPsnId());
        bean.setSeqNo(pubMemberDTO.getSeqNo());
        bean.setName(pubMemberDTO.getName());
        bean.setEmail(pubMemberDTO.getEmail());
        bean.setCommunicable(pubMemberDTO.isCommunicable());
        bean.setFirstAuthor(pubMemberDTO.isFirstAuthor());
        bean.setInsNames(new ArrayList<>());
        bean.setMemberId(pubMemberDTO.getMemberId());
        members.add(bean);
      }
      pubPdwhDetailDOM.setMembers(members);
    }
    pubPdwhDetailDAO.save(pubPdwhDetailDOM);
  }

}
