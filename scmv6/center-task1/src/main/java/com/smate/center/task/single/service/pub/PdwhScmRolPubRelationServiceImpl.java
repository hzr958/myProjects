package com.smate.center.task.single.service.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.dao.publicpub.PubFulltextUploadLogDao;
import com.smate.center.task.dao.rol.quartz.PublicationRolDao;
import com.smate.center.task.dao.rol.quartz.RolPubXmlDao;
import com.smate.center.task.dao.sns.pub.PubPdwhScmRolDao;
import com.smate.center.task.dao.sns.pub.PublicationPdwhDao;
import com.smate.center.task.dao.sns.quartz.ArchiveFileDao;
import com.smate.center.task.dao.sns.quartz.PubFulltextPsnRcmdDao;
import com.smate.center.task.model.publicpub.PubFulltextUploadLog;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.model.rol.quartz.RolPubXml;
import com.smate.center.task.model.sns.pub.PubPdwhScmRol;
import com.smate.center.task.model.sns.pub.PublicationPdwh;
import com.smate.center.task.model.sns.quartz.ArchiveFile;
import com.smate.center.task.model.sns.quartz.PubFulltextPsnRcmd;
import com.smate.center.task.single.dao.rol.pub.PubPdwhRolRelationDao;
import com.smate.center.task.single.dao.rol.pub.PublicationRolPdwhDao;
import com.smate.center.task.single.dao.solr.PdwhPubDupDao;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.model.rol.pub.PubPdwhRolRelation;
import com.smate.center.task.single.model.rol.pub.PublicationRolPdwh;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.util.pub.PubXmlDbUtils;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubCommentDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubShareDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.center.task.v8pub.dao.sns.PubCommentDAO;
import com.smate.center.task.v8pub.dao.sns.PubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubLikeDAO;
import com.smate.center.task.v8pub.dao.sns.PubShareDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubCommentPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubLikePO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubSharePO;
import com.smate.center.task.v8pub.sns.po.PubCommentPO;
import com.smate.center.task.v8pub.sns.po.PubFullTextPO;
import com.smate.center.task.v8pub.sns.po.PubLikePO;
import com.smate.center.task.v8pub.sns.po.PubSharePO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.center.task.v8pub.sns.po.PubStatisticsPO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

@Service("pdwhScmRolPubRelationService")
@Transactional(rollbackFor = Exception.class)
public class PdwhScmRolPubRelationServiceImpl implements PdwhScmRolPubRelationService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String scmDomain;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Autowired
  private PubPdwhScmRolDao pubPdwhScmRolDao;
  @Autowired
  private PublicationPdwhDao publicationPdwhDao;

  @Autowired
  private PublicationRolPdwhDao publicationRolPdwhDao;

  @Autowired
  private PubPdwhRolRelationDao pubPdwhRolRelationDao;

  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private PdwhPubDupDao pdwhPubDupDao;

  @Autowired
  private RolPubXmlDao rolPubXmlDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;

  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDao;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubFulltextUploadLogDao pubFulltextUploadLogDao;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PubCommentDAO pubCommentDAO;
  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private PubShareDAO pubShareDAO;
  @Autowired
  private ArchiveFileDao archiveFileDao;

  @Override
  public List<PubPdwhScmRol> getPdwhToHandlePub(Integer size) {
    return this.pubPdwhScmRolDao.getToHandleList(size);
  }

  @Override
  public Integer handleRolPub(PubPdwhScmRol pubPdwhScmRol) {
    Long rolPubId = pubPdwhScmRol.getPubId();
    PublicationRolPdwh pubRolPdwh = this.publicationRolPdwhDao.get(rolPubId);
    // 由于历史数据错误，部分rol的成果被记录到了sns，所以需要作出判断。
    if (pubRolPdwh == null) {
      PublicationPdwh pubPdwh = this.publicationPdwhDao.get(rolPubId);
      if (pubPdwh == null) {
        return 4;
      }
      Long pdwhPubId = this.getPdwhId(pubPdwh);
      if (pdwhPubId > 25918829L) {
        if (this.pubPdwhRolRelationDao.get(rolPubId) == null) {
          PubPdwhRolRelation pub = new PubPdwhRolRelation();
          pub.setRolPubId(rolPubId);
          pub.setPdwhPubId(pdwhPubId);
          this.pubPdwhRolRelationDao.save(pub);
        }
      } else {// 如果没有，重新匹配
        if (this.pubPdwhRolRelationDao.get(rolPubId) == null) {
          Long newPdwhPubId = reMatchPdwhPubForRol(rolPubId);
          if (newPdwhPubId == 6L) {
            return 6;// publication表中没有获取到对应数据
          } else if (newPdwhPubId == 0L) {
            return 5;// 没有匹配上
          } else if (newPdwhPubId != null) {
            PubPdwhRolRelation pub = new PubPdwhRolRelation();
            pub.setRolPubId(rolPubId);
            pub.setPdwhPubId(newPdwhPubId);
            this.pubPdwhRolRelationDao.save(pub);
          }
        }
      }
    } else {
      Long pdwhPubId = this.getRolPdwhId(pubRolPdwh);
      if (pdwhPubId > 25918829L) {
        if (this.pubPdwhRolRelationDao.get(rolPubId) == null) {
          PubPdwhRolRelation pub = new PubPdwhRolRelation();
          pub.setRolPubId(rolPubId);
          pub.setPdwhPubId(pdwhPubId);
          this.pubPdwhRolRelationDao.save(pub);
        }
      } else {// 如果没有，重新匹配
        if (this.pubPdwhRolRelationDao.get(rolPubId) == null) {
          Long newPdwhPubId = reMatchPdwhPubForRol(rolPubId);
          if (newPdwhPubId == 6L) {
            return 6;// publication表中没有获取到对应数据
          } else if (newPdwhPubId == 0L) {
            return 5;// 没有匹配上
          } else if (newPdwhPubId != null) {
            PubPdwhRolRelation pub = new PubPdwhRolRelation();
            pub.setRolPubId(rolPubId);
            pub.setPdwhPubId(newPdwhPubId);
            this.pubPdwhRolRelationDao.save(pub);
          }
        }
      }
    }

    return 1;
  }

  /**
   * 处理SNS的成果
   * 
   * @throws Exception
   */
  @Override
  public Integer handleScmPub(PubPdwhScmRol pubPdwhScmRol) throws Exception {
    Long scmPubId = pubPdwhScmRol.getPubId();
    if (pubExists(scmPubId)) {
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findById(pubPdwhScmRol.getPubId());
      if (pubDetail == null) {
        return 6;// 这条成果已删除
      }
      // 匹配对应的PdwhPubId
      List<String> dupPubIds = getMatchedPubId(pubDetail);
      if (CollectionUtils.isEmpty(dupPubIds)) {
        pubPdwhSnsRelationDao.delPubPdwhSnsRelation(scmPubId);
        return 5;// 没有获取到基准库对应的pdwhPubId
      } else {
        for (String dupPubId : dupPubIds) {
          Long matchedPubId = Long.valueOf(dupPubId);
          PubPdwhSnsRelationPO pubPdwhSnsRelation = pubPdwhSnsRelationDao.getByPubIdAndPdwhId(scmPubId, matchedPubId);
          if (pubPdwhSnsRelation != null) {
            pubPdwhSnsRelation.setPdwhPubId(matchedPubId);
            pubPdwhSnsRelation.setUpdateDate(new Date());
          } else {
            pubPdwhSnsRelation = new PubPdwhSnsRelationPO(scmPubId, matchedPubId, new Date());
          }
          this.pubPdwhSnsRelationDao.saveOrUpdate(pubPdwhSnsRelation);
          // 关联成果将基准库的赞/评论/分享数据同步到个人库
          Long commentCount = synPdwhCommentToSns(scmPubId, matchedPubId);
          Long awardCount = synPdwhLikeToSns(scmPubId, matchedPubId);
          Long shareCount = synPdwhShareToSns(scmPubId, matchedPubId);
          updatePubStatistics(scmPubId, commentCount, awardCount, shareCount);
          // 匹配到基准库成果后给这条成果匹配全文
          this.matchPubFulltext(scmPubId, matchedPubId);
        }

      }
    } else {
      return 6;// 这条成果已删除
    }
    return 1;
  }

  private boolean pubExists(Long scmPubId) {
    PubSnsPO pub = pubSnsDAO.getPubsnsById(scmPubId);
    if (pub != null) {
      return true;
    }
    return false;
  }

  public void updatePubStatistics(Long scmPubId, Long commentCount, Long awardCount, Long shareCount) {
    PubStatisticsPO pubStatistics = pubStatisticsDAO.get(scmPubId);
    if (pubStatistics == null) {
      pubStatistics =
          new PubStatisticsPO(scmPubId, awardCount.intValue(), shareCount.intValue(), commentCount.intValue(), 0, 0);
    } else {
      pubStatistics.setAwardCount(awardCount.intValue());
      pubStatistics.setCommentCount(commentCount.intValue());
      pubStatistics.setShareCount(shareCount.intValue());
    }
    pubStatisticsDAO.saveOrUpdate(pubStatistics);
  }

  public Long synPdwhShareToSns(Long scmPubId, Long newPdwhPubId) {
    List<PdwhPubSharePO> shareList = pdwhPubShareDAO.getShareRecords(newPdwhPubId);
    if (shareList != null) {
      for (PdwhPubSharePO pdwhPubSharePO : shareList) {
        PubSharePO pubSharePO = pubShareDAO.findShare(scmPubId, pdwhPubSharePO.getPsnId(), pdwhPubSharePO.getPlatform(),
            pdwhPubSharePO.getGmtCreate());
        if (pubSharePO == null) {
          pubSharePO = new PubSharePO();
          pubSharePO.setPubId(scmPubId);
          pubSharePO.setPsnId(pdwhPubSharePO.getPsnId());
          pubSharePO.setComment(pdwhPubSharePO.getComment());
          pubSharePO.setPlatform(pdwhPubSharePO.getPlatform());
          pubSharePO.setStatus(pdwhPubSharePO.getStatus());
          pubSharePO.setSharePsnGroupId(pdwhPubSharePO.getSharePsnGroupId());
          pubSharePO.setGmtCreate(pdwhPubSharePO.getGmtCreate());
        }
        pubSharePO.setGmtModified(pdwhPubSharePO.getGmtModified());
        pubShareDAO.saveOrUpdate(pubSharePO);
      }
    }
    Long shareCount = pdwhPubShareDAO.getShareCount(newPdwhPubId);
    return shareCount;
  }

  public Long synPdwhLikeToSns(Long scmPubId, Long newPdwhPubId) {
    List<PdwhPubLikePO> likeList = pdwhPubLikeDAO.findByPubId(newPdwhPubId);
    if (likeList != null) {
      for (PdwhPubLikePO pdwhPubLike : likeList) {
        PubLikePO pubLikePO = pubLikeDAO.findByPubIdAndPsnId(scmPubId, pdwhPubLike.getPsnId());
        if (pubLikePO == null) {
          pubLikePO = new PubLikePO();
          pubLikePO.setPubId(scmPubId);
          pubLikePO.setPsnId(pdwhPubLike.getPsnId());
        }
        pubLikePO.setStatus(pdwhPubLike.getStatus());
        pubLikePO.setGmtCreate(pdwhPubLike.getGmtCreate());
        pubLikePO.setGmtModified(pdwhPubLike.getGmtModified());
        pubLikeDAO.saveOrUpdate(pubLikePO);
      }
    }
    Long awardCount = pdwhPubLikeDAO.getAwardCount(newPdwhPubId);
    return awardCount;
  }

  public Long synPdwhCommentToSns(Long scmPubId, Long newPdwhPubId) {
    List<PdwhPubCommentPO> commentList = pdwhPubCommentDAO.findByPubId(newPdwhPubId);
    if (commentList != null) {
      for (PdwhPubCommentPO pdwhPubCommentPO : commentList) {
        PubCommentPO pubCommentPO = pubCommentDAO.findComment(scmPubId, pdwhPubCommentPO.getPsnId(),
            pdwhPubCommentPO.getContent(), pdwhPubCommentPO.getGmtCreate());
        if (pubCommentPO == null) {
          pubCommentPO = new PubCommentPO();
          pubCommentPO.setPubId(scmPubId);
          pubCommentPO.setPsnId(pdwhPubCommentPO.getPsnId());
          pubCommentPO.setContent(pdwhPubCommentPO.getContent());
          pubCommentPO.setStatus(pdwhPubCommentPO.getStatus());
          pubCommentPO.setGmtCreate(pdwhPubCommentPO.getGmtCreate());
        }
        pubCommentPO.setGmtModified(pdwhPubCommentPO.getGmtModified());
        pubCommentDAO.saveOrUpdate(pubCommentPO);
      }
    }
    Long commentCount = pdwhPubCommentDAO.getCommentsCount(newPdwhPubId);
    return commentCount;
  }

  private void matchPubFulltext(Long scmPubId, Long newPdwhPubId) {
    // 保存推荐记录前需要判断有没有全文。有全文就不推荐全文，群组成果不推荐
    PubFullTextPO fulltext = pubFullTextDAO.getPubFullTextByPubId(scmPubId);
    PubSnsPO snsPub = pubSnsDAO.getSnsPubById(scmPubId);
    try {
      if (fulltext == null && snsPub != null) {
        PubSnsPO pubSnsPO = pubSnsDAO.get(scmPubId);
        // 成果拥有者
        Long pubOwner = pubSnsPO.getCreatePsnId();
        // 这条成果没有全文的情况下匹配全文
        // 1.先根据关联到的基准库id找全文
        PubFulltextUploadLog uploadLog = pubFulltextUploadLogDao.getFulltextByPdwhPubId(newPdwhPubId);
        if (uploadLog != null) {
          PubFulltextPsnRcmd psnRcmd = pubFulltextPsnRcmdDao.getPsnRcmd(scmPubId, 1, uploadLog.getPdwhPubId());
          // 获取文件大小
          ArchiveFile archiveFile = archiveFileDao.get(uploadLog.getFulltextFileId());
          if (archiveFile != null) {
            // 过滤掉相同文件 (同一个文件Id或者相同文件大小) 不更新 不插入
            Long fileSize = archiveFile.getFileSize();
            List<PubFulltextPsnRcmd> repeatPsnRcmd =
                pubFulltextPsnRcmdDao.getRepeatPsnRcmd(scmPubId, pubOwner, uploadLog.getFulltextFileId(), fileSize);
            // 有重复记录的删除 另起一个事务删除
            if (repeatPsnRcmd != null && repeatPsnRcmd.size() > 0) {
              pubFulltextPsnRcmdDao.deleteAllRepeatPsnRcmd(scmPubId, pubOwner, uploadLog.getFulltextFileId(), fileSize);
            }
            if (psnRcmd != null) {
              if (psnRcmd.getStatus() != 2) {
                psnRcmd.setFulltextFileId(uploadLog.getFulltextFileId());
                psnRcmd.setStatus(0);
                psnRcmd.setFileSize(fileSize);
              }
            } else {
              psnRcmd = new PubFulltextPsnRcmd(scmPubId, pubOwner, uploadLog.getFulltextFileId(), 1, 3, 0, new Date(),
                  newPdwhPubId, uploadLog.getUploadPsnId(), fileSize);
            }
            pubFulltextPsnRcmdDao.saveOrUpdateWithNewTs(psnRcmd);
          }
        }
        // 2.根据基准库找到其他关联到的个人成果
        List<Long> snsPubIds = pubPdwhSnsRelationDao.getSnsPubIdsByPdwhId(newPdwhPubId, scmPubId);
        if (CollectionUtils.isNotEmpty(snsPubIds)) {
          List<PubFulltextUploadLog> uploadLogList = pubFulltextUploadLogDao.getFulltextBySnsPubId(snsPubIds);
          for (PubFulltextUploadLog pubFulltextUploadLog : uploadLogList) {
            PubSnsPO srcSnsPub = pubSnsDAO.getSnsPubById(pubFulltextUploadLog.getSnsPubId());
            if (pubFulltextUploadLog.getIsPrivacy() != 2 && pubFulltextUploadLog.getIsDelete() == 0
                && srcSnsPub != null) {
              PubFulltextPsnRcmd psnRcmd =
                  pubFulltextPsnRcmdDao.getPsnRcmd(scmPubId, 0, pubFulltextUploadLog.getSnsPubId());
              // 获取文件大小
              ArchiveFile archiveFile = archiveFileDao.get(pubFulltextUploadLog.getFulltextFileId());
              if (archiveFile == null)
                continue;
              Long fileSize = archiveFile.getFileSize();
              // 过滤掉相同文件 (同一个文件Id或者相同文件大小) 不更新 不插入
              List<PubFulltextPsnRcmd> repeatPsnRcmds = pubFulltextPsnRcmdDao.getRepeatPsnRcmd(scmPubId, pubOwner,
                  pubFulltextUploadLog.getFulltextFileId(), fileSize);
              // 有重复记录的删除 另起一个事务删除
              if (CollectionUtils.isNotEmpty(repeatPsnRcmds)) {
                repeatPsnRcmds.forEach(repeatPsnRcmd -> {
                  // 基准库的不删除
                  if (repeatPsnRcmd.getDbId() == 0)
                    pubFulltextPsnRcmdDao.deleteWithNewTs(repeatPsnRcmd.getId());
                });
              }
              if (psnRcmd != null) {
                if (psnRcmd.getStatus() != 2) {
                  psnRcmd.setFulltextFileId(pubFulltextUploadLog.getFulltextFileId());
                  psnRcmd.setStatus(0);
                  psnRcmd.setFileSize(fileSize);
                }
              } else {
                psnRcmd = new PubFulltextPsnRcmd(scmPubId, pubOwner, pubFulltextUploadLog.getFulltextFileId(), 0, 3, 0,
                    new Date(), pubFulltextUploadLog.getSnsPubId(), pubFulltextUploadLog.getUploadPsnId(), fileSize);
              }
              pubFulltextPsnRcmdDao.saveOrUpdateWithNewTs(psnRcmd);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("新增成果匹配全文出错==========", e);
    }
    try {
      // 全文不为空 就推荐给其他人
      if (fulltext != null && snsPub != null) {
        // 根据基准库找到其他关联到的个人成果
        Long srcPsnId = snsPub.getCreatePsnId();
        List<Long> snsPubIds = pubPdwhSnsRelationDao.getSnsPubIdsByPdwhId(newPdwhPubId, scmPubId);
        if (CollectionUtils.isNotEmpty(snsPubIds)) {
          // 排除群组成果
          List<PubSnsPO> pubList = pubSnsDAO.getSnsPubList(snsPubIds);
          for (PubSnsPO pubSnsPO : pubList) {
            // 保存推荐记录前需要判断有没有全文。有全文就不推荐全文
            Long pubId = pubSnsPO.getPubId();
            PubFullTextPO othersFulltext = pubFullTextDAO.getPubFullTextByPubId(pubId);
            if (othersFulltext == null) {
              // 新上传的成果肯定是没有推荐过的
              // 获取文件大小
              Long fileId = fulltext.getFileId();
              ArchiveFile archiveFile = archiveFileDao.get(fileId);
              if (archiveFile == null) {
                continue;
              }
              Long fileSize = archiveFile.getFileSize();
              // 直接保存
              PubFulltextPsnRcmd psnRcmd = new PubFulltextPsnRcmd(pubId, pubSnsPO.getCreatePsnId(), fileId, 0, 3, 0,
                  new Date(), scmPubId, srcPsnId, fileSize);
              pubFulltextPsnRcmdDao.saveOrUpdate(psnRcmd);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("新增成果推荐全文给他人出错============", e);
    }
  }

  /**
   * 保存到PubPdwh表
   * 
   * @param scmPubId
   * @param pdwhPubId
   * @throws Exception
   */
  public void saveToPubPdwh(Long scmPubId, Long pdwhPubId) throws Exception {
    PublicationPdwh pubPdwh = new PublicationPdwh();
    pubPdwh.setPubId(scmPubId);
    Integer dbId = pdwhPublicationDao.getDbIdById(pdwhPubId);
    // isi文献
    if (PubXmlDbUtils.isIsiDb(dbId)) {
      pubPdwh.setIsiId(pdwhPubId);
      // scopus文献
    } else if (PubXmlDbUtils.isScopusDb(dbId)) {
      pubPdwh.setSpsId(pdwhPubId);
      // cnki文献
    } else if (PubXmlDbUtils.isCnkiDb(dbId)) {
      pubPdwh.setCnkiId(pdwhPubId);
      // ei
    } else if (PubXmlDbUtils.isEiDb(dbId)) {
      pubPdwh.setEiId(pdwhPubId);
      // wanfang
    } else if (PubXmlDbUtils.isWanFangDb(dbId)) {
      pubPdwh.setWfId(pdwhPubId);
      // CNIPRDb
    } else if (PubXmlDbUtils.isCNIPRDb(dbId)) {
      pubPdwh.setCniprId(pdwhPubId);
      // pubmed
    } else if (PubXmlDbUtils.isPubMedDb(dbId)) {
      pubPdwh.setPubmedId(pdwhPubId);
    } // ieeexp
    else if (PubXmlDbUtils.isIEEEXploreDb(dbId)) {
      pubPdwh.setIeeeXpId(pdwhPubId);
    } // ScienceDirect
    else if (PubXmlDbUtils.isScienceDirectDb(dbId)) {
      pubPdwh.setScdId(pdwhPubId);
    }
    // cnkipat
    else if (PubXmlDbUtils.isCnkipatDb(dbId)) {
      pubPdwh.setCnkiPatId(pdwhPubId);
    }
    publicationPdwhDao.save(pubPdwh);
  }

  private Long getPdwhId(PublicationPdwh pubPdwh) {
    Long pdwhPubId = 0L;
    if (pubPdwh.getIsiId() != null) {
      pdwhPubId = pubPdwh.getIsiId();
      return pdwhPubId;
    }

    if (pubPdwh.getEiId() != null) {
      pdwhPubId = pubPdwh.getEiId();
      return pdwhPubId;
    }

    if (pubPdwh.getPubmedId() != null) {
      pdwhPubId = pubPdwh.getPubmedId();
      return pdwhPubId;
    }

    if (pubPdwh.getCnkiId() != null) {
      pdwhPubId = pubPdwh.getCnkiId();
      return pdwhPubId;
    }

    if (pubPdwh.getCnkiPatId() != null) {
      pdwhPubId = pubPdwh.getCnkiPatId();
      return pdwhPubId;
    }

    return pdwhPubId;
  }

  private Long getRolPdwhId(PublicationRolPdwh pubPdwh) {
    Long pdwhPubId = 0L;
    if (pubPdwh.getIsiId() != null) {
      pdwhPubId = pubPdwh.getIsiId();
      return pdwhPubId;
    }

    if (pubPdwh.getEiId() != null) {
      pdwhPubId = pubPdwh.getEiId();
      return pdwhPubId;
    }

    if (pubPdwh.getPubmedId() != null) {
      pdwhPubId = pubPdwh.getPubmedId();
      return pdwhPubId;
    }

    if (pubPdwh.getCnkiId() != null) {
      pdwhPubId = pubPdwh.getCnkiId();
      return pdwhPubId;
    }

    if (pubPdwh.getCnkiPatId() != null) {
      pdwhPubId = pubPdwh.getCnkiPatId();
      return pdwhPubId;
    }

    return pdwhPubId;
  }

  /**
   * 为rol匹配重新匹配成果
   * 
   * 
   * 
   */
  private Long reMatchPdwhPubForRol(Long rolPubId) {
    PublicationRol pubRol = this.publicationRolDao.get(rolPubId);

    if (pubRol == null) {
      return 6L;
    }

    Long patentNoHash = 0L;
    Long patentOpenNoHash = 0L;

    if (pubRol.getTypeId() == 5) {
      // 获取专利号与公开号
      RolPubXml pubXml = this.rolPubXmlDao.get(rolPubId);
      if (pubXml != null && StringUtils.isNotBlank(pubXml.getPubXml())) {
        try {
          PubXmlDocument doc = new PubXmlDocument(pubXml.getPubXml());
          String patentOpenNo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_open_no");
          String patentNo = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "patent_no");
          if (StringUtils.isNotEmpty(patentOpenNo)) {
            patentNoHash = PubHashUtils.cleanPatentNoHash(patentOpenNo);
          }

          if (StringUtils.isNotEmpty(patentNo)) {
            patentOpenNoHash = PubHashUtils.cleanPatentNoHash(patentNo);
          }

        } catch (DocumentException e) {
          logger.error("PdwhScmRolPubRelationService获取专利号与公开号出错", e);
        }
      }
    }

    String zhTitle = PubHashUtils.cleanTitle(pubRol.getZhTitle());
    String enTitle = PubHashUtils.cleanTitle(pubRol.getEnTitle());
    Integer pubYear = pubRol.getPublishYear();
    Integer pubType = pubRol.getTypeId();
    String[] titleValues = new String[] {zhTitle, enTitle};
    Long titleHashValue = PubHashUtils.fingerPrint(titleValues) == null ? 0L : PubHashUtils.fingerPrint(titleValues);
    Long enTitleHash = HashUtils.getStrHashCode(enTitle);
    Long zhTitleHash = HashUtils.getStrHashCode(zhTitle);
    Long doiHash = PubHashUtils.cleanDoiHash(pubRol.getDoi());
    Long matchedPubId = getMatchedPubId(doiHash, zhTitleHash, enTitleHash, titleHashValue, patentNoHash,
        patentOpenNoHash, pubYear, pubType);
    return matchedPubId;
  }

  private List<String> getMatchedPubId(PubSnsDetailDOM pubDetail) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("pubGener", 3);
    if (pubDetail.getPubType() == 5) {
      PatentInfoBean infoBean = (PatentInfoBean) pubDetail.getTypeInfo();
      if (StringUtils.isBlank(infoBean.getApplicationNo()) && StringUtils.isBlank(infoBean.getPublicationOpenNo())) {
        return null;
      }
      dataMap.put("applicationNo", infoBean.getApplicationNo());
      dataMap.put("publicationOpenNo", infoBean.getPublicationOpenNo());
    }
    dataMap.put("title", pubDetail.getTitle());
    dataMap.put("pubType", pubDetail.getPubType());
    dataMap.put("pubYear", PubDetailVoUtil.parseDateForYear(pubDetail.getPublishDate()));
    dataMap.put("doi", pubDetail.getDoi());
    dataMap.put("sourceId", pubDetail.getSourceId());
    dataMap.put("srcDbId", pubDetail.getSrcDbId());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dataMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    Map dupResultMap = JacksonUtils.jsonToMap(result);
    List<String> dupPubIds = new ArrayList<>();
    if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
      if (dupResultMap.get("msgList") != null) {
        String[] dupPubIdString = dupResultMap.get("msgList").toString().split(",");
        dupPubIds = Arrays.asList(dupPubIdString);
      } else if (dupResultMap.get("msg") != null) {
        dupPubIds.add(dupResultMap.get("msg").toString());
      }
      return dupPubIds;
    }
    return null;

  }

  /**
   * 查询匹配到的pubId
   * 
   * @param doiHash
   * @param zhTitleHash
   * @param enTitleHash
   * @param titleHashValue
   * @param patentNoHash
   * @param patentOpenNoHash
   * @param pubYear
   * @param pubType
   * @return
   */

  private Long getMatchedPubId(Long doiHash, Long zhTitleHash, Long enTitleHash, Long titleHashValue, Long patentNoHash,
      Long patentOpenNoHash, Integer pubYear, Integer pubType) {
    List<Long> dupPubIds = new ArrayList<Long>();
    dupPubIds = this.getDupPubIdsByDoi(doiHash);
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }

    dupPubIds = this.getDupPubIdsByPatentInfo(patentNoHash, patentOpenNoHash);
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }

    if (pubYear == null || pubType == null || pubYear == 0) {
      return 0L;
    }

    dupPubIds = this.getDupPubIdsByTitle(zhTitleHash, enTitleHash, titleHashValue, pubYear, pubType);
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      return dupPubIds.get(0);
    }

    return 0L;
  }

  private List<Long> getDupPubIdsByDoi(Long doiHash) {
    if (doiHash == null) {
      return null;
    }

    List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsByDoiHash(doiHash);
    return dupPubIds;
  }

  private List<Long> getDupPubIdsByTitle(Long zhTitleHash, Long enTitleHash, Long titleHashValue, Integer pubYear,
      Integer pubType) {
    if (zhTitleHash == null && enTitleHash == null && titleHashValue == null) {
      return null;
    }
    List<Long> dupPubIds =
        this.pdwhPubDupDao.getDupPubIdsByTitle(zhTitleHash, enTitleHash, titleHashValue, pubType, pubYear);

    return dupPubIds;
  }

  private List<Long> getDupPubIdsByPatentInfo(Long patentNoHash, Long patentOpenNoHash) {
    if (patentNoHash == null && patentOpenNoHash == null) {
      return null;
    }
    List<Long> dupPubIds = this.pdwhPubDupDao.getDupPubIdsByPatentInfo(patentNoHash, patentOpenNoHash);
    return dupPubIds;
  }

  @Override
  public void updatePubPdwhScmRol(PubPdwhScmRol pubPdwhScmRol, Integer status) {
    pubPdwhScmRol.setStatus(status);
    this.pubPdwhScmRolDao.saveOrUpdate(pubPdwhScmRol);
  }

  @Override
  public void savePubPdwhScmRol(PubPdwhScmRol pubPdwhScmRol) {
    pubPdwhScmRolDao.save(pubPdwhScmRol);
  }

  @Override
  public List<Long> getScmPubIds() {
    return pubSnsDAO.getScmPubIds();
  }

  @Override
  public List<PubPdwhScmRol> getPdwhToHandlePub(Integer size, Long startPubId, Long endPubId) {
    return pubPdwhScmRolDao.getToHandleList(size, startPubId, endPubId);
  }
}
