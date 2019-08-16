package com.smate.center.task.v8pub.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.group.GrpPubsDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.dao.sns.quartz.DynamicAwardPsnDao;
import com.smate.center.task.dao.sns.quartz.DynamicAwardResDao;
import com.smate.center.task.dao.sns.quartz.DynamicSharePsnDao;
import com.smate.center.task.dao.sns.quartz.DynamicShareResDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.grp.GrpPubs;
import com.smate.center.task.model.sns.quartz.DynamicAwardPsn;
import com.smate.center.task.model.sns.quartz.DynamicAwardRes;
import com.smate.center.task.model.sns.quartz.DynamicSharePsn;
import com.smate.center.task.model.sns.quartz.DynamicShareRes;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.utils.PubBuildAuthorNamesUtils;
import com.smate.center.task.utils.PubDbUtils;
import com.smate.center.task.utils.data.PubLocaleUtils;
import com.smate.center.task.v8pub.dao.sns.PubAccessoryDAO;
import com.smate.center.task.v8pub.dao.sns.PubCitationsDAO;
import com.smate.center.task.v8pub.dao.sns.PubCommentDAO;
import com.smate.center.task.v8pub.dao.sns.PubDuplicateDAO;
import com.smate.center.task.v8pub.dao.sns.PubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubKeywordsDAO;
import com.smate.center.task.v8pub.dao.sns.PubLikeDAO;
import com.smate.center.task.v8pub.dao.sns.PubMemberDAO;
import com.smate.center.task.v8pub.dao.sns.PubShareDAO;
import com.smate.center.task.v8pub.dao.sns.PubSituationDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.center.task.v8pub.dao.sns.PubViewDAO;
import com.smate.center.task.v8pub.dao.sns.SnsPubDetailDAO;
import com.smate.center.task.v8pub.enums.PubSnsStatusEnum;
import com.smate.center.task.v8pub.sns.po.PubAccessoryPO;
import com.smate.center.task.v8pub.sns.po.PubCitationsPO;
import com.smate.center.task.v8pub.sns.po.PubDuplicatePO;
import com.smate.center.task.v8pub.sns.po.PubLikePO;
import com.smate.center.task.v8pub.sns.po.PubMemberPO;
import com.smate.center.task.v8pub.sns.po.PubSharePO;
import com.smate.center.task.v8pub.sns.po.PubSituationPO;
import com.smate.center.task.v8pub.sns.po.PubSnsDetailPO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.center.task.v8pub.sns.po.PubStatisticsPO;
import com.smate.center.task.v8pub.strategy.PubTypeInfoConstructor;
import com.smate.center.task.v8pub.strategy.PubTypeInfoDriver;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.model.PsnPubPO;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.util.CleanCopyrightUtils;
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
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubAccessoryDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;

@Service("pubSnsDataInitService")
@Transactional(rollbackFor = Exception.class)
public class PubSnsDataInitServiceImpl implements PubSnsDataInitService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAccessoryDAO pubAccessoryDAO;
  @Autowired
  private PubDuplicateDAO pubDuplicateDAO;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private DynamicAwardPsnDao dynamicAwardPsnDao;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private DynamicShareResDao dynamicShareResDao;
  @Autowired
  private DynamicSharePsnDao dynamicSharePsnDao;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private PubMemberDAO pubMemberDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private PubKeywordsDAO pubKeywordsDAO;
  @Autowired
  private PubTypeInfoConstructor pubTypeInfoConstructor;
  @Autowired
  private PubCitationsDAO pubCitationsDAO;
  @Autowired
  private PsnPubDAO psnPubsDAO;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private PubSituationDAO pubSituationDAO;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private SnsPubDetailDAO snsPubDetailDAO;
  @Autowired
  private PubCommentDAO pubCommentDAO;
  @Autowired
  private PubViewDAO pubViewDAO;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void initAccessory(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    PubAccessoryPO pubAccessoryPO = null;
    List<Long> lock = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      // 获取导入的成果的附件列表
      String attachJson = JacksonUtils.listToJsonStr((List) dataMap.get("accessorys"));
      if (StringUtils.isNotBlank(attachJson)) {
        List<PubAccessoryDTO> attachs = JacksonUtils.jsonToCollection(attachJson, List.class, PubAccessoryDTO.class);
        if (attachs != null && attachs.size() > 0) {
          // 遍历附件列表，根据附件的查看权限判断能否替换到重复的成果中去
          for (PubAccessoryDTO accessDTO : attachs) {
            if (NumberUtils.isNullOrZero(accessDTO.getFileId())) {
              continue;
            }
            if (lock.contains(accessDTO.getFileId())) {
              continue;
            }
            lock.add(accessDTO.getFileId());
            pubAccessoryPO = pubAccessoryDAO.findByPubIdAndFileId(pubId, accessDTO.getFileId());
            if (pubAccessoryPO == null) {
              pubAccessoryPO = new PubAccessoryPO();
              pubAccessoryPO.setFileId(accessDTO.getFileId());
              pubAccessoryPO.setPubId(pubId);
            }
            pubAccessoryPO.setFileName(accessDTO.getFileName());
            if (StringUtils.isNotBlank(accessDTO.getGmtCreate())) {
              pubAccessoryPO.setGmtCreate(sdf.parse(accessDTO.getGmtCreate()));
            }
            if (StringUtils.isNotBlank(accessDTO.getGmtModified())) {
              pubAccessoryPO.setGmtModified(sdf.parse(accessDTO.getGmtModified()));
            }
            pubAccessoryPO.setPermission(accessDTO.getPermission());
            pubAccessoryDAO.saveOrUpdate(pubAccessoryPO);
          }
        }
      }
    } catch (Exception e) {
      logger.error("sns库成果附件初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public void initDuplicate(Long pubId, PubSnsDetailDOM pubDetail) throws ServiceException {
    try {
      if (pubDetail == null) {
        return;
      }
      PubDuplicatePO pubDuplicatePO = pubDuplicateDAO.get(pubId);
      if (pubDuplicatePO != null) {
        Integer dbId = pubDetail.getSrcDbId();
        if (StringUtils.isNotBlank(pubDetail.getDoi())) {
          Long hashCleanDoi = PubHashUtils.getDoiHashRemotePun(pubDetail.getDoi());
          pubDuplicatePO.setHashCleanDoi(NumberUtils.isNullOrZero(hashCleanDoi) ? "" : hashCleanDoi + "");
        }
        if (dbId != null && PubDbUtils.isCnkiDb(dbId)) {
          Long hashCleanCnkiDoi = PubHashUtils.getDoiHashRemotePun(pubDetail.getDoi());
          pubDuplicatePO.setHashCleanCnkiDoi(NumberUtils.isNullOrZero(hashCleanCnkiDoi) ? "" : hashCleanCnkiDoi + "");
        }
        pubDuplicateDAO.saveOrUpdate(pubDuplicatePO);
      }
      // Integer dbId = pubDetail.getSrcDbId();
      // if (StringUtils.isNotBlank(pubDetail.getDoi())) {
      // Long hashDoi = PubHashUtils.cleanDoiHash(pubDetail.getDoi());
      // pubDuplicatePO.setHashDoi(NumberUtils.isNullOrZero(hashDoi) ? "" : hashDoi + "");
      // }
      // if (dbId != null && PubDbUtils.isCnkiDb(dbId)) {
      // Long hashCnkiDoi = PubHashUtils.cleanDoiHash(pubDetail.getDoi());
      // pubDuplicatePO.setHashCnkiDoi(NumberUtils.isNullOrZero(hashCnkiDoi) ? "" : hashCnkiDoi + "");
      // }
      // if (dbId != null && PubDbUtils.isIsiDb(dbId)) {
      // Long hashIsiSourceId = PubHashUtils.cleanSourceIdHash(pubDetail.getSourceId());
      // pubDuplicatePO.setHashIsiSourceId(NumberUtils.isNullOrZero(hashIsiSourceId) ? "" :
      // hashIsiSourceId + "");
      // }
      // if (dbId != null && PubDbUtils.isEiDb(dbId)) {
      // Long hashEiSourceId = PubHashUtils.cleanSourceIdHash(pubDetail.getSourceId());
      // pubDuplicatePO.setHashEiSourceId(NumberUtils.isNullOrZero(hashEiSourceId) ? "" : hashEiSourceId +
      // "");
      // }
      // if (pubDetail.getPubType() == 5) {
      // // 专利
      // PatentInfoBean p = (PatentInfoBean) pubDetail.getTypeInfo();
      // if (p != null) {
      // if (StringUtils.isNotBlank(p.getApplicationNo())) {
      // Long hashApplicationNo = PubHashUtils.cleanPatentNoHash(p.getApplicationNo());
      // pubDuplicatePO
      // .setHashApplicationNo(NumberUtils.isNullOrZero(hashApplicationNo) ? "" : hashApplicationNo + "");
      // }
      // if (StringUtils.isNotBlank(p.getPublicationOpenNo())) {
      // Long hashPublicationOpenNo = PubHashUtils.cleanPatentNoHash(p.getPublicationOpenNo());
      // pubDuplicatePO.setHashPublicationOpenNo(
      // NumberUtils.isNullOrZero(hashPublicationOpenNo) ? "" : hashPublicationOpenNo + "");
      // }
      // }
      // // 新加的参数，需要重新计算detailsHash
      // String detailsJson = JacksonUtils.jsonObjectSerializer(pubDetail);
      // String detailsHash = String.valueOf(HashUtils.getStrHashCode(detailsJson));
      // pubDuplicatePO.setDetailsHash(detailsHash);
      // pubDuplicatePO.setGmtModified(new Date());
      // }

    } catch (Exception e) {
      logger.error("sns库成果查重表数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Long initFulltext(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      // String fullTextJson = JacksonUtils.mapToJsonStr((Map)
      // dataMap.get("fullText"));
      // if (StringUtils.isNotBlank(fullTextJson) &&
      // !"null".equalsIgnoreCase(fullTextJson) &&
      // !"{}".equalsIgnoreCase(fullTextJson)) {
      // PubFulltextDTO pubFullTextDTO =
      // JacksonUtils.jsonObject(fullTextJson, PubFulltextDTO.class);
      // PubFulltext pubFulltext = pubFulltextDao.get(pubId);
      // if (pubFullTextDTO != null) {
      // PubFullTextPO pubFullTextPO = new PubFullTextPO();
      // pubFullTextDAO.deletByPubId(pubId);
      // pubFullTextPO.setPubId(pubId);
      // pubFullTextPO.setFileId(pubFullTextDTO.getFileId());
      // pubFullTextPO.setFileName(pubFullTextDTO.getFileName());
      // pubFullTextPO.setPermission(pubFullTextDTO.getPermission());
      // if (StringUtils.isNotBlank(pubFullTextDTO.getGmtCreate())) {
      // pubFullTextPO.setGmtCreate(sdf.parse(pubFullTextDTO.getGmtCreate()));
      // }
      // if (StringUtils.isNotBlank(pubFullTextDTO.getGmtModified())) {
      // pubFullTextPO.setGmtModified(sdf.parse(pubFullTextDTO.getGmtModified()));
      // }
      // if (pubFulltext != null) {
      // pubFullTextPO.setThumbnailPath(pubFulltext.getFulltextImagePath());
      // }
      // pubFullTextDAO.save(pubFullTextPO);
      // return pubFullTextPO.getId();
      // }
      // }
      return pubFullTextDAO.getFullTextIdByPubId(pubId);
    } catch (Exception e) {
      logger.error("sns库成果全文数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initKeywords(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      String keywords = (String) dataMap.get("keywords");
      if (StringUtils.isNotEmpty(keywords)) {
        List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
        pubKeywordsDAO.savePubKeywords(pubId, kwList);
      }
    } catch (Exception e) {
      logger.error("sns库成果关键词初始化出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void initMembers(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    PubMemberPO pubMemberPO = null;
    try {
      String membersJson = JacksonUtils.listToJsonStr((List) dataMap.get("members"));
      pubMemberDAO.deleteAll(pubId);
      if (StringUtils.isNotBlank(membersJson) && !"null".equalsIgnoreCase(membersJson)
          && !"[]".equalsIgnoreCase(membersJson)) {
        List<PubMemberDTO> members = JacksonUtils.jsonToCollection(membersJson, List.class, PubMemberDTO.class);
        if (members != null && members.size() > 0) {
          for (PubMemberDTO pubMemberDTO : members) {
            if (StringUtils.isEmpty(pubMemberDTO.getName())) {
              continue;
            }
            pubMemberPO = new PubMemberPO();
            pubMemberPO.setPsnId(pubMemberDTO.getPsnId());
            pubMemberPO.setPubId(pubId);
            pubMemberPO.setSeqNo(pubMemberDTO.getSeqNo());
            pubMemberPO.setName(pubMemberDTO.getName());
            pubMemberPO.setEmail(pubMemberDTO.getEmail());
            List<MemberInsDTO> mIns = pubMemberDTO.getInsNames();
            if (mIns != null && mIns.size() > 0) {
              MemberInsDTO m = mIns.get(0);
              pubMemberPO.setInsId(m.getInsId());
              pubMemberPO.setInsName(m.getInsName());
            }
            pubMemberPO.setOwner(pubMemberDTO.isOwner() ? 1 : 0);
            pubMemberPO.setFirstAuthor(pubMemberDTO.isFirstAuthor() ? 1 : 0);
            pubMemberPO.setCommunicable(pubMemberDTO.isCommunicable() ? 1 : 0);
            pubMemberDAO.save(pubMemberPO);
          }
        }
      }

    } catch (Exception e) {
      logger.error("sns库成果作者数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void initSitation(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PubSituationPO pubSiuationPO = null;
      String sitJson = JacksonUtils.listToJsonStr((List) dataMap.get("situations"));
      if (StringUtils.isNotBlank(sitJson) && !"null".equalsIgnoreCase(sitJson) && !"[]".equalsIgnoreCase(sitJson)) {
        List<PubSituationDTO> situations = JacksonUtils.jsonToCollection(sitJson, List.class, PubSituationDTO.class);
        pubSituationDAO.deleteAll(pubId);
        if (situations != null && situations.size() > 0) {
          for (PubSituationDTO psDto : situations) {
            String libraryName = psDto.getLibraryName();
            if (StringUtils.isBlank(libraryName)) {
              continue;
            }
            libraryName = PubLibraryEnum.parse(libraryName).desc;
            if (libraryName.equalsIgnoreCase("OTHER") && StringUtils.isBlank(psDto.getSrcId())
                && StringUtils.isBlank(psDto.getSrcUrl())) {
              continue;
            }
            pubSiuationPO = new PubSituationPO();
            pubSiuationPO.setPubId(pubId);
            pubSiuationPO.setLibraryName(libraryName);
            pubSiuationPO.setSitStatus(psDto.isSitStatus() ? 1 : 0);
            pubSiuationPO.setSitOriginStatus(psDto.isSitOriginStatus() ? 1 : 0);
            pubSiuationPO.setSrcDbId(psDto.getSrcDbId());
            pubSiuationPO.setSrcId(psDto.getSrcId());
            pubSiuationPO.setSrcUrl(psDto.getSrcUrl());
            pubSiuationPO.setGmtModified(new Date());
            pubSiuationPO.setGmtCreate(new Date());
            pubSituationDAO.saveOrUpdate(pubSiuationPO);
          }
        }
      }
    } catch (Exception e) {
      logger.error("sns库成果收录情况数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initPubSns(PubSimple pubSimple, Map<String, Object> dataMap) throws ServiceException {
    try {
      PubSnsPO pubSnsPO = pubSnsDAO.get(pubSimple.getPubId());
      if (pubSnsPO != null) {
        // pubSnsPO = new PubSnsPO();
        // pubSnsPO.setPubId(pubSimple.getPubId());
        // 标题
        String title = (String) dataMap.get("title");
        pubSnsPO.setTitle(resetTitle(title));
        pubSnsDAO.saveOrUpdate(pubSnsPO);
      }
      // // 创建者psnId
      // pubSnsPO.setCreatePsnId(pubSimple.getOwnerPsnId());
      // // 国家或地区id
      // Long countryId = null;
      // if (dataMap.get("countryId") != null && !"".equals(dataMap.get("countryId"))) {
      // countryId = Long.valueOf(String.valueOf(dataMap.get("countryId")));
      // pubSnsPO.setCountryId(countryId);
      // }
      // // 作者名
      // String authorNames = (String) dataMap.get("authorNames");
      // pubSnsPO.setAuthorNames(resetAuthorNames(authorNames));
      // // 简短描述
      // String briefDesc = (String) dataMap.get("briefDesc");
      // pubSnsPO.setBriefDesc(briefDesc);
      // // 引用数
      // Integer citations = 0;
      // if (dataMap.get("citations") != null) {
      // citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));;
      // }
      // pubSnsPO.setCitations(citations);
      // // 成果类型
      // Integer pubType = 7;
      // if (dataMap.get("pubType") != null) {
      // pubType = Integer.valueOf(String.valueOf(dataMap.get("pubType")));
      // }
      // pubSnsPO.setPubType(pubType);
      // // 录入类型，默认手工导入
      // PubSnsRecordFromEnum recordFrom = PubSnsRecordFromEnum.MANUAL_INPUT;
      // if (dataMap.get("recordFrom") != null) {
      // Integer val = Integer.valueOf(String.valueOf(dataMap.get("recordFrom")));
      // recordFrom = PubSnsRecordFromEnum.valueOf(val);
      // }
      // pubSnsPO.setRecordFrom(recordFrom);
      // // 成果状态
      // pubSnsPO.setStatus(buildStatus(pubSimple.getStatus()));
      // // 成果年限
      // pubSnsPO.setPublishYear(pubSimple.getPublishYear());
      // // 更新标记
      // pubSnsPO.setUpdateMark(pubSimple.getUpdateMark());
      // // 修改时间
      // pubSnsPO.setGmtModified(pubSimple.getUpdateDate());
      // pubSnsPO.setGmtCreate(pubSimple.getCreateDate());
    } catch (Exception e) {
      logger.error("sns库成果主表数据初始化出错！pubId={}", pubSimple.getPubId());
      throw new ServiceException(e);
    }

  }

  private PubSnsStatusEnum buildStatus(Integer status) {
    if (status == 1) {
      return PubSnsStatusEnum.DELETED;
    } else {
      return PubSnsStatusEnum.DEFAULT;
    }
  }

  private String resetAuthorNames(String authorNames) {
    authorNames = XmlUtil.subStr500char(authorNames);
    authorNames = XmlUtil.cleanAuthorsAddr(authorNames);
    authorNames = XmlUtil.formatPubAuthorKws(authorNames);
    return authorNames;
  }

  private String resetTitle(String title) {
    title = StringUtils.substring(title, 0, 250);
    title = XmlUtil.trimAllHtml(title);
    return title;
  }

  private PubSnsStatusEnum buildStatus(Long pubId) {
    PubSnsPO pubSnsPO = pubSnsDAO.get(pubId);
    if (pubSnsPO != null) {
      Integer psnPubStatus = psnPubsDAO.getStatus(pubId);
      Integer grpPubStatus = grpPubsDao.getStatus(pubId);
      if (psnPubStatus == null && grpPubStatus == null) {
        return PubSnsStatusEnum.DELETED;
      } else {
        return PubSnsStatusEnum.DEFAULT;
      }
    }
    return PubSnsStatusEnum.DEFAULT;
  }

  @Override
  public PubSnsDetailDOM initPubDetail(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PubSnsDetailDOM pub = pubSnsDetailDAO.findByPubId(pubId);
      // if (pub == null) {
      // pub = new PubSnsDetailDOM();
      // pub.setPubId(pubId);
      // }
      // buildMain(pub, dataMap);
      // buildMembers(pub, dataMap);
      // buildSituations(pub, dataMap);
      if (pub != null) {
        // 标题
        String title = (String) dataMap.get("title");
        pub.setTitle(title);
        pubSnsDetailDAO.save(pub);
      }
      return pub;
    } catch (Exception e) {
      logger.error("sns库成果详情数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void buildSituations(PubSnsDetailDOM pub, Map<String, Object> dataMap) {
    Set<PubSituationBean> situationSet = new HashSet<>();
    String sitJson = JacksonUtils.listToJsonStr((List) dataMap.get("situations"));
    if (StringUtils.isNotBlank(sitJson) && !"null".equalsIgnoreCase(sitJson) && !"[]".equalsIgnoreCase(sitJson)) {
      List<PubSituationDTO> situations = JacksonUtils.jsonToCollection(sitJson, List.class, PubSituationDTO.class);
      if (situations != null && situations.size() > 0) {
        PubSituationBean pubSituationBean = null;
        for (PubSituationDTO psDto : situations) {
          if (StringUtils.isEmpty(psDto.getLibraryName())) {
            continue;
          }
          pubSituationBean = new PubSituationBean();
          pubSituationBean.setLibraryName(psDto.getLibraryName());
          pubSituationBean.setSitStatus(psDto.isSitStatus());
          pubSituationBean.setSitOriginStatus(psDto.isSitOriginStatus());
          pubSituationBean.setSrcDbId(psDto.getSrcDbId());
          pubSituationBean.setSrcId(psDto.getSrcId());
          pubSituationBean.setSrcUrl(psDto.getSrcUrl());
          situationSet.add(pubSituationBean);
        }
      }
    }
    pub.setSituations(situationSet);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void buildMembers(PubSnsDetailDOM pub, Map<String, Object> dataMap) {
    List<PubMemberBean> list = new ArrayList<>();
    String membersJson = JacksonUtils.listToJsonStr((List) dataMap.get("members"));
    if (StringUtils.isNotBlank(membersJson) && !"null".equalsIgnoreCase(membersJson)
        && !"[]".equalsIgnoreCase(membersJson)) {
      List<PubMemberDTO> members = JacksonUtils.jsonToCollection(membersJson, List.class, PubMemberDTO.class);
      if (members != null && members.size() > 0) {
        PubMemberBean pubMemberBean = null;
        for (PubMemberDTO pubMemberDTO : members) {
          if (StringUtils.isEmpty(pubMemberDTO.getName())) {
            continue;
          }
          pubMemberBean = new PubMemberBean();
          pubMemberBean.setPsnId(pubMemberDTO.getPsnId());
          pubMemberBean.setSeqNo(pubMemberDTO.getSeqNo());
          pubMemberBean.setName(pubMemberDTO.getName());
          pubMemberBean.setEmail(pubMemberDTO.getEmail());
          pubMemberBean.setInsNames(buildInsNames(pubMemberDTO.getInsNames()));
          pubMemberBean.setFirstAuthor(pubMemberDTO.isFirstAuthor());
          pubMemberBean.setCommunicable(pubMemberDTO.isCommunicable());
          list.add(pubMemberBean);
        }
      }
    }
    pub.setMembers(list);
  }

  @SuppressWarnings("unchecked")
  private List<MemberInsBean> buildInsNames(List<MemberInsDTO> insNames) {
    if (CollectionUtils.isEmpty(insNames)) {
      return null;
    }
    String insJson = JacksonUtils.jsonObjectSerializer(insNames);
    return JacksonUtils.jsonToCollection(insJson, List.class, MemberInsBean.class);
  }

  @SuppressWarnings("unchecked")
  private void buildMain(PubSnsDetailDOM pub, Map<String, Object> dataMap) {
    // 当前库引用次数
    Integer citations = 0;
    if (dataMap.get("citations") != null) {
      citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));
    }
    pub.setCitations(citations);
    // 国家或地区id
    Long countryId = null;
    if (dataMap.get("countryId") != null) {
      countryId = Long.valueOf(String.valueOf(dataMap.get("countryId")));
    }
    pub.setCountryId(countryId);
    // doi
    String doi = (String) dataMap.get("doi");
    pub.setDoi(doi);
    // 全文id
    pub.setFulltextId(pub.getFulltextId());
    // 基金信息
    String fundInfo = (String) dataMap.get("fundInfo");
    pub.setFundInfo(fundInfo);
    // 组织机构
    String organization = (String) dataMap.get("organization");
    pub.setOrganization(organization);
    // 来源的dbId
    String srcDbId = (String) dataMap.get("srcDbId");
    if (StringUtils.isNotEmpty(srcDbId)) {
      pub.setSrcDbId(Integer.valueOf(srcDbId));
    }
    // 原始来源全文url
    String srcFulltextUrl = (String) dataMap.get("srcFulltextUrl");
    pub.setSrcFulltextUrl(srcFulltextUrl);
    // 来源url
    String sourceUrl = (String) dataMap.get("sourceUrl");
    pub.setSourceUrl(sourceUrl);
    // 来源id
    String sourceId = (String) dataMap.get("sourceId");
    pub.setSourceId(sourceId);
    // 引用url
    String citedUrl = (String) dataMap.get("citedUrl");
    pub.setCitedUrl(citedUrl);
    // 作者名的集合
    String authorNames = (String) dataMap.get("authorNames");
    pub.setAuthorNames(authorNames);
    // 简短描述
    String briefDesc = (String) dataMap.get("briefDesc");
    pub.setBriefDesc(briefDesc);
    // 关键词
    String keywords = (String) dataMap.get("keywords");
    pub.setKeywords(keywords);
    // 摘要
    String summary = (String) dataMap.get("summary");
    pub.setSummary(summary);
    // 标题
    String title = (String) dataMap.get("title");
    pub.setTitle(title);
    // 全文id
    Long fulltextId = null;
    if (dataMap.get("fulltextId") != null) {
      fulltextId = Long.valueOf(String.valueOf(dataMap.get("fulltextId")));
    }
    pub.setFulltextId(fulltextId);
    // 成果类型
    Integer pubType = 7;
    if (dataMap.get("pubType") != null) {
      pubType = Integer.valueOf(String.valueOf(dataMap.get("pubType")));
    }
    pub.setPubType(pubType);
    // 发布日期
    String publishDate = (String) dataMap.get("publishDate");
    pub.setPublishDate(publishDate);
    // 成果对象
    PubTypeInfoBean typeInfo = buildTypeInfo(dataMap.get("pubTypeInfo"), pubType);
    pub.setTypeInfo(typeInfo);

    pub.setHCP(false);
    pub.setHP(false);
    pub.setOA("");
  }

  @Override
  public void initCitations(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    // 先删除原引用次数数据
    pubCitationsDAO.deleteByPubId(pubId);
    // 来源的dbId
    String srcDbId = (String) dataMap.get("srcDbId");
    if (StringUtils.isNotEmpty(srcDbId)) {
      Integer dbId = Integer.valueOf(srcDbId);
      dbId = PubParamUtils.combineDbid(dbId);
      // 只有15，16，17的数据才会存进引用次数表里面
      if (dbId == 99) {
        PubCitationsPO pubCitationsPO = pubCitationsDAO.getByPubId(pubId);
        if (pubCitationsPO == null) {
          pubCitationsPO = new PubCitationsPO();
          pubCitationsPO.setPubId(pubId);
        }
        Integer citations = 0;
        if (dataMap.get("citations") != null) {
          citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));
        }
        pubCitationsPO.setCitations(citations);
        pubCitationsPO.setCitedType(0);
        pubCitationsPO.setGmtModified(new Date());
        pubCitationsDAO.saveOrUpdate(pubCitationsPO);
      }
    }

  }

  @Override
  public void initPubStatistics(Long pubId, Map<String, Object> dataMap) throws ServiceException {
    try {
      PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(pubId);
      if (pubStatisticsPO == null) {
        pubStatisticsPO = new PubStatisticsPO(pubId, 0, 0, 0, 0, 0);
      }
      Integer likeCount = initPubLike(pubId);
      String title = (String) dataMap.get("title");
      boolean isChina = PubLocaleUtils.getLocale(title).equals(Locale.CHINA);
      Integer shareCount = initPubShare(pubId, isChina);
      Integer commentCount = initPubComment(pubId);
      Integer viewCount = initPubView(pubId);
      Integer citations = 0;
      if (dataMap.get("citations") != null) {
        citations = Integer.valueOf(String.valueOf(dataMap.get("citations")));
      }
      pubStatisticsPO.setAwardCount(Optional.ofNullable(likeCount).orElse(0));
      pubStatisticsPO.setShareCount(Optional.ofNullable(shareCount).orElse(0));
      pubStatisticsPO.setCommentCount(commentCount);
      pubStatisticsPO.setReadCount(viewCount);
      pubStatisticsPO.setRefCount(citations);
      pubStatisticsDAO.saveOrUpdate(pubStatisticsPO);
    } catch (Exception e) {
      logger.error("sns库成果统计数数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  // 成果resType 1 resNode 1
  @Override
  public Integer initPubLike(Long pubId) throws ServiceException {
    PubLikePO pubLikePO = null;
    try {
      List<Long> psnIds = new ArrayList<>();
      DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getPubAwardResByResId(pubId);
      if (dynamicAwardRes == null) {
        return 0;
      }
      List<DynamicAwardPsn> listdaPsn = dynamicAwardPsnDao.listDynamicAwardPsn(dynamicAwardRes.getAwardId());
      pubLikeDAO.deleteAll(pubId);
      if (listdaPsn != null && listdaPsn.size() > 0) {
        for (DynamicAwardPsn dynamicAwardPsn : listdaPsn) {
          if (dynamicAwardPsn == null) {
            continue;
          }
          Long psnId = dynamicAwardPsn.getAwarderPsnId();
          if (NumberUtils.isNullOrZero(psnId)) {
            continue;
          }
          if (psnIds.contains(psnId)) {
            continue;
          }
          pubLikePO = new PubLikePO();
          pubLikePO.setPsnId(psnId);
          psnIds.add(psnId);
          pubLikePO.setPubId(pubId);
          pubLikePO.setStatus(dynamicAwardPsn.getStatus().getValue());
          pubLikePO.setGmtCreate(dynamicAwardPsn.getAwardDate());
          pubLikeDAO.saveOrUpdate(pubLikePO);
        }
        return dynamicAwardPsnDao.getAwardCount(dynamicAwardRes.getAwardId()).intValue();
      }
      return null;
    } catch (Exception e) {
      logger.error("sns库成果赞数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer initPubShare(Long pubId, boolean isChina) throws ServiceException {
    PubSharePO pubSharePO = null;
    try {
      DynamicShareRes dynamicShareRes = dynamicShareResDao.getPubShareResByResId(pubId);
      if (dynamicShareRes == null) {
        return 0;
      }
      List<DynamicSharePsn> listdaPsn = dynamicSharePsnDao.listDynamicSharePsn(dynamicShareRes.getShareId());
      pubShareDAO.deleteAll(pubId);
      if (listdaPsn != null && listdaPsn.size() > 0) {
        for (DynamicSharePsn d : listdaPsn) {
          if (d == null) {
            continue;
          }
          pubSharePO = new PubSharePO();
          pubSharePO.setPubId(pubId);
          pubSharePO.setPsnId(d.getSharerPsnId());
          // 原始数据没有分享平台之分
          pubSharePO.setPlatform(null);
          // 原始数据没有分享状态，默认状态都为0已分享，已分享 9为取消分享
          pubSharePO.setStatus(0);
          pubSharePO
              .setComment(isChina ? StringUtils.isNotBlank(d.getShareTitle()) ? d.getShareTitle() : d.getShareEnTitle()
                  : StringUtils.isNotBlank(d.getShareEnTitle()) ? d.getShareEnTitle() : d.getShareTitle());
          pubSharePO.setGmtCreate(d.getShareDate());
          pubShareDAO.save(pubSharePO);
        }
        return listdaPsn.size();
      }
      return null;
    } catch (Exception e) {
      logger.error("sns库成果分享数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer initPubComment(Long pubId) throws ServiceException {
    try {
      Long commentCount = pubCommentDAO.countPubComment(pubId);
      commentCount = (commentCount == null) ? 0L : commentCount;
      return commentCount.intValue();
    } catch (Exception e) {
      logger.error("sns库成果评论数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public Integer initPubView(Long pubId) throws ServiceException {
    try {
      Long viewCount = pubViewDAO.countPubView(pubId);
      viewCount = (viewCount == null) ? 0L : viewCount;
      return viewCount.intValue();
    } catch (Exception e) {
      logger.error("sns库成果查看数据初始化出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void initPsnPub(PubSimple pubSimple) throws ServiceException {
    Long ownerPsnId = pubSimple.getOwnerPsnId();
    if (pubSimple.getStatus() != null && pubSimple.getStatus() == 0) {
      if (!NumberUtils.isNullOrZero(ownerPsnId)) {
        PsnPubPO psnPubPO = psnPubsDAO.getPsnPub(pubSimple.getPubId(), ownerPsnId);
        if (psnPubPO == null) {
          psnPubPO = new PsnPubPO();
          psnPubPO.setPubId(pubSimple.getPubId());
          psnPubPO.setOwnerPsnId(ownerPsnId);

        }
        psnPubPO.setStatus(buildStatus(pubSimple.getStatus()).getValue());
        psnPubPO.setGmtCreate(pubSimple.getCreateDate());
        psnPubPO.setGmtModified(pubSimple.getUpdateDate());
        psnPubsDAO.saveOrUpdate(psnPubPO);
      }
    }
  }

  @Override
  public void initGrpPubs(PubSimple pubSimple) throws ServiceException {
    Long ownerPsnId = pubSimple.getOwnerPsnId();
    if (!NumberUtils.isNullOrZero(ownerPsnId)) {
      List<GrpPubs> grpPubList = grpPubsDao.findGrpPubs(pubSimple.getPubId());
      if (grpPubList != null && grpPubList.size() > 0) {
        for (GrpPubs grpPubs : grpPubList) {
          grpPubs.setOwnerPsnId(ownerPsnId);
          grpPubsDao.saveOrUpdate(grpPubs);
        }
      }
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void buildAuthorNames(Map<String, Object> dataMap) {
    List<String> authorList = new ArrayList<>();
    String authorNames = "";
    // 通过members节点进行构造
    String members = JacksonUtils.listToJsonStr((List) dataMap.get("members"));
    if (StringUtils.isNotBlank(members) && !"null".equalsIgnoreCase(members) && !"[]".equalsIgnoreCase(members)) {
      List<PubMemberDTO> memberList = JacksonUtils.jsonToCollection(members, List.class, PubMemberDTO.class);
      if (!CollectionUtils.isEmpty(memberList)) {
        authorNames = PubBuildAuthorNamesUtils.buildSnsPubAuthorNames(memberList);
        // 格式化中英标点符号
        authorNames = XmlUtil.formateSymbolAuthors(authorNames, authorNames);
      }
    } else if (dataMap.get("authorNames") != null && !"".equals(dataMap.get("authorNames"))) {
      authorNames = (String) dataMap.get("authorNames");
      authorList = AuthorNamesUtils.parsePubAuthorNames(authorNames);
      authorNames = PubBuildAuthorNamesUtils.buildPdwhPubAuthorNames(authorList);
    }
    dataMap.remove("authorNames");
    dataMap.put("authorNames", authorNames);
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

  @Override
  public void constructData(Map<String, Object> dataMap) throws ServiceException {
    // 重新构造countryId
    buildCountryId(dataMap);
    // 构造字段authorNames字段
    buildAuthorNames(dataMap);
    // 重构keywords
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
    briefDesc = StringUtils.substring(briefDesc, 0, 500);
    dataMap.remove("briefDesc");
    dataMap.put("briefDesc", briefDesc);

  }

  private void buildCountryId(Map<String, Object> dataMap) {
    Long countryId = null;
    Object city = dataMap.get("city");
    if (city != null && !"".equals(city)) {
      countryId = constRegionDao.getRegionIdByCityName(String.valueOf(city));
    }
    if (NumberUtils.isNullOrZero(countryId)) {
      Object country_Id = dataMap.get("countryId");
      if (country_Id != null && !"".equals(country_Id)) {
        countryId = Long.valueOf(String.valueOf(country_Id));
      }
    }

    dataMap.remove("city");
    dataMap.put("countryId", countryId);
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
        typeInfoBean = JacksonUtils.jsonObject(json, JournalInfoBean.class);
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

  @Override
  public void initSNSPubDetail(Long pubId, PubSnsDetailDOM pubSnsDetail) throws ServiceException {
    try {
      if (pubSnsDetail != null) {
        PubSnsDetailPO pubSnsDetailPO = snsPubDetailDAO.get(pubId);
        if (pubSnsDetailPO == null) {
          pubSnsDetailPO = new PubSnsDetailPO();
          pubSnsDetailPO.setPubId(pubId);
        }
        String pubJson = JacksonUtils.jsonObjectSerializer(pubSnsDetail);
        pubSnsDetailPO.setPubJson(pubJson);
        snsPubDetailDAO.saveOrUpdate(pubSnsDetailPO);
      }
    } catch (Exception e) {
      logger.error("保存sns库成果备份表出错！", pubId);
      throw new ServiceException(e);
    }
  }

}
