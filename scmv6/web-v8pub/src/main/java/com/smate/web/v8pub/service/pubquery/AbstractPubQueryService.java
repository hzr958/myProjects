package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubLibraryEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDAO;
import com.smate.web.v8pub.dao.sns.CollectedPubDao;
import com.smate.web.v8pub.dao.sns.FileDownloadRecordDao;
import com.smate.web.v8pub.dao.sns.PubFullTextReqBaseDao;
import com.smate.web.v8pub.dao.sns.PubLikeDAO;
import com.smate.web.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.web.v8pub.dao.sns.group.GrpPubIndexUrlDAO;
import com.smate.web.v8pub.dom.AwardsInfoBean;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.ConferencePaperBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.PubDetailDOM;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.SoftwareCopyrightBean;
import com.smate.web.v8pub.dom.StandardInfoBean;
import com.smate.web.v8pub.dom.ThesisInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.service.pdwh.PdwhPubMemberService;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubMemberService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.sns.indexurl.PubIndexUrlService;
import com.smate.web.v8pub.untils.PubDetailVoUtil;
import com.smate.web.v8pub.vo.PubListResult;

public abstract class AbstractPubQueryService implements PubQueryService {

  @Autowired
  private ConstRegionDao constRegionDao;

  @Value("${domainscm}")
  public String domainscm;
  @Resource
  private PubIndexUrlDao pubIndexUrlDao;
  @Resource
  private PubFullTextService pubFullTextService;
  @Resource
  private PdwhPubFullTextService pdwhpubFullTextService;
  @Resource
  private GrpPubIndexUrlDAO grpPubIndexUrlDAO;
  @Autowired
  private PubFullTextReqBaseDao pubFullTextReqBaseDao;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private CollectedPubDao collectedPubDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PdwhPubStatisticsService pdwhPubStatisticsService;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;
  @Autowired
  private PubIndexUrlService pubIndexUrlService;
  @Autowired
  private FileDownloadRecordDao fileDownloadRecordDao;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PubMemberService pubMemberService;
  @Autowired
  private PdwhPubMemberService pdwhPubMemberService;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;

  public abstract Map<String, Object> checkData(PubQueryDTO pubQueryDTO);

  public abstract void queryPubs(PubQueryDTO pubQueryDTO);

  public abstract PubListResult assembleData(PubQueryDTO pubQueryDTO);

  @Override
  public PubListResult handleData(PubQueryDTO pubQueryDTO) {
    // 1: 检查参数
    Map<String, Object> checkResult = checkData(pubQueryDTO);
    PubListResult pubListResult = new PubListResult();
    if (checkResult == null) {
      // 2:查询成果
      queryPubs(pubQueryDTO);
      // 3:拼装成果数据
      pubListResult = assembleData(pubQueryDTO);
    } else {
      pubListResult.setStatus(V8pubConst.ERROR);
      pubListResult.setMsg(checkResult.get(V8pubConst.ERROR_MSG).toString());
    }
    return pubListResult;
  }

  /**
   * 构建群组成果的短地址
   * 
   * @param list
   * @param grpId
   */
  public void buildGrpPubIndexUrl(List<PubInfo> list, Long grpId) {
    if (list != null) {
      for (PubInfo info : list) {
        GrpPubIndexUrlPO grpPubIndexUrlPO = grpPubIndexUrlDAO.findByGrpIdAndPubId(grpId, info.getPubId());
        if (grpPubIndexUrlPO != null && grpPubIndexUrlPO.getPubId().longValue() == info.getPubId().longValue()) {
          String indexUrl = this.domainscm + "/" + ShortUrlConst.B_TYPE + "/" + grpPubIndexUrlPO.getPubIndexUrl();
          info.setPubIndexUrl(indexUrl);
          continue;
        }
      }
    }
  }

  /**
   * 构建个人库成果的短地址
   * 
   * @param list
   * @param pubIdList
   */
  public void buildSnsPubIndexUrl(List<PubInfo> list, List<Long> pubIdList) {
    if (list != null && list.size() > 0 && pubIdList != null && pubIdList.size() > 0) {
      List<PubIndexUrl> urlList = pubIndexUrlDao.findPubIndexUrl(pubIdList);
      for (PubInfo info : list) {
        for (PubIndexUrl pubIndexUrl : urlList) {
          if (pubIndexUrl.getPubId().longValue() == info.getPubId().longValue()) {
            String indexUrl = this.domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
            info.setPubIndexUrl(indexUrl);
            break;
          }
        }

      }
    }
  }

  /**
   * 构建个人库成果的全文信息
   * 
   * @param list
   * @param pubIdList
   */
  public void buildSnsPubFulltext(List<PubInfo> list, List<Long> pubIdList) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    buildSnsPubFulltext(list, pubIdList, currentUserId);
  }

  /**
   * 重写个人库成果的全文信息
   * 
   * @param list
   * @param pubIdList
   */
  public void buildSnsPubFulltext(List<PubInfo> list, List<Long> pubIdList, Long psnId) {
    List<PubFullTextPO> pubfulltextList = pubFullTextService.findPubfulltextList(pubIdList);
    if (pubfulltextList != null && list != null) {
      for (PubInfo info : list) {
        for (PubFullTextPO fulltext : pubfulltextList) {
          if (fulltext.getPubId().longValue() == info.getPubId().longValue()) {

            boolean flag = pubFTReqBaseDao.isFullTextReqAgree(info.getPubId(), psnId, PubDbEnum.SNS);
            if ((info.getOwnerPsnId() != null && info.getOwnerPsnId().equals(psnId)) || fulltext.getPermission() == 0
                || flag) {
              // 公开的或自己的显示短地址，短地址不用登录
              info.setFullTextDownloadUrl(fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT,
                  fulltext.getFileId(), info.getPubId()));
            } else {
              // SCM-19946 只要有全文的都显示全文图片
              info.setFullTextDownloadUrl(
                  fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, fulltext.getFileId(), info.getPubId()));
            }
            info.setHasFulltext(1);
            info.setFullTextFieId(fulltext.getFileId());
            info.setFullTextPermission(fulltext.getPermission());
            if (flag) {
              info.setFullTextPermission(0);
            }
            // 下载次数
            Long downLoadSum = 0l;
            try {
              /*
               * Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(info.getPubId()); if (pdwhPubId !=
               * null && pdwhPubId > 0) { PdwhPubFullTextPO pdwhfulltext =
               * pdwhpubFullTextService.getPdwhPubfulltext(pdwhPubId); if (pdwhfulltext != null &&
               * pdwhfulltext.getPermission() == 0) { downLoadSum =
               * fileDownloadRecordDao.getFileDonwloadSum(pdwhfulltext.getFileId()); } } else { downLoadSum =
               * downloadCollectStatisticsDao.countDownloadByKey(info.getPubId(), 1); }
               */
              downLoadSum = downloadCollectStatisticsDao.countDownloadByKey(info.getPubId(), 1);
            } catch (Exception e) {
            }
            info.setDownloadCount(downLoadSum == null ? 0L : downLoadSum.intValue());
            info.setFullTextImgUrl(StringUtils.isNotBlank(fulltext.getThumbnailPath()) ? fulltext.getThumbnailPath()
                : "/resmod/images_v5/images2016/file_img1.jpg");
            continue;
          }
        }

      }
    }
  }

  public void buildPdwhPubIndexUrl(PubInfo pubInfo, Long psnId) {
    PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlService.get(pubInfo.getPubId());
    if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
      pubInfo.setPubIndexUrl(domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pdwhPubIndexUrl.getPubIndexUrl());
    }

  }

  public void builPdwhPubStatistics(PubInfo pubInfo, Long psnId) {
    PdwhPubStatisticsPO pdwhPubStatistics = pdwhPubStatisticsService.get(pubInfo.getPubId());
    // Long commentCount = pubPdwhDetailService.getPdwhCommentNumber(pubInfo.getPubId());
    Integer commentCount = pdwhPubStatistics.getCommentCount();// SCM-24683
    if (pdwhPubStatistics != null) {
      pubInfo.setAwardCount(pdwhPubStatistics.getAwardCount());
      pubInfo.setShareCount(pdwhPubStatistics.getShareCount());
      pubInfo.setCommentCount(commentCount);
      // SCM-19763 增加基准库与个人库关联的成果的统计数
      Long readCount = newPubStatisticsService.getSnsPubReadCounts(pubInfo.getPubId());
      readCount =
          (pdwhPubStatistics.getReadCount() == null) ? 0 + readCount : pdwhPubStatistics.getReadCount() + readCount;
      pubInfo.setReadCount(readCount.intValue());
      pubInfo.setCitedTimes(pdwhPubStatistics.getRefCount());
    }
    long count = pdwhPubLikeDAO.getLikeRecord(pubInfo.getPubId(), psnId);
    if (count > 0) {
      pubInfo.setIsAward(1);
    }
    if (collectedPubDao.isCollectedPub(psnId, pubInfo.getPubId(), PubDbEnum.PDWH)) {
      pubInfo.setIsCollected(1);
    }
  }

  public void buildPdwhPubFulltext(PubInfo pubInfo, Long psnId) {
    Long pubId = pubInfo.getPubId();
    PdwhPubFullTextPO fulltext = pdwhpubFullTextService.getPdwhPubfulltext(pubInfo.getPubId());
    // 同意全文请求的 TODO
    // Boolean flag =
    // pubFTReqBaseDao.isFullTextReqAgree(pub.getPubId(),
    // SecurityUtils.getCurrentUserId(), PubDbEnum.SNS);
    // 只有有权限的全文，才显示处理，
    if (fulltext != null) {
      if ((pubInfo.getOwnerPsnId() != null && pubInfo.getOwnerPsnId().longValue() == psnId)
          || fulltext.getPermission() == 0) {
        pubInfo.setHasFulltext(1);
        pubInfo.setFullTextFieId(fulltext.getFileId());
        pubInfo.setFullTextPermission(fulltext.getPermission());
        pubInfo
            .setFullTextDownloadUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, pubInfo.getPubId()));
        Long downLoadSum = fileDownloadRecordDao.getFileDonwloadSum(fulltext.getFileId());
        pubInfo.setDownloadCount(downLoadSum == null ? 0L : downLoadSum.intValue());
      } else {
        pubInfo.setDownloadCount(0L);
      }
      pubInfo.setFullTextImgUrl(StringUtils.isNotBlank(fulltext.getThumbnailPath()) ? fulltext.getThumbnailPath()
          : domainscm + "/resmod/images_v5/images2016/file_img1.jpg");
    } else {
      pubInfo.setDownloadCount(0L);
      pubInfo.setFullTextImgUrl(domainscm + "/resmod/images_v5/images2016/file_img.jpg");
    }

  }

  public void builSnsPubStatistics(PubInfo pubInfo, Long psnId) {
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubInfo.getPubId());
    // 初始化成果的赞操作
    PubStatisticsPO pubStat = pubStatisticsDAO.getPubStatisticsById(pubInfo.getPubId());
    if (pubStat != null) {
      pubInfo.setAwardCount(pubStat.getAwardCount());
      pubInfo.setShareCount(pubStat.getShareCount());
      pubInfo.setCommentCount(pubStat.getCommentCount());
      pubInfo.setReadCount(pubStat.getReadCount());
      pubInfo.setCitedTimes(pubStat.getRefCount());
    }
    // SCM-23704 个人库关联成果：关联的基准库成果加其他关联个人成果的阅读数之和
    if (pdwhPubId != null && pdwhPubId > 0) {
      // SCM-24461
      boolean flag = false;
      PsnConfigPub psnConfigPub = psnConfigPubDao.getPsnConfigPubByPubId(pubInfo.getPubId());
      if (psnConfigPub != null && psnConfigPub.getAnyUser() < 7) {
        flag = true;
      }
      if (!flag) {
        PdwhPubStatisticsPO pdwhPubStatistics = pdwhPubStatisticsService.get(pdwhPubId);
        Long readNum = newPubStatisticsService.getSnsPubReadCounts(pdwhPubId);
        int readCount = Integer.parseInt(readNum.toString());
        if (pdwhPubStatistics != null) {
          pubInfo.setReadCount(
              pdwhPubStatistics.getReadCount() == null ? 0 + readCount : pdwhPubStatistics.getReadCount() + readCount);
        } else {
          pubInfo.setReadCount(readCount);
        }
      }
    }
    if (psnId != null) {
      long count = pubLikeDAO.getLikeRecord(pubInfo.getPubId(), psnId);
      boolean collectedPub = collectedPubDao.isCollectedPub(psnId, pubInfo.getPubId(), PubDbEnum.SNS);
      if (count > 0) {
        pubInfo.setIsAward(1);
      }
      if (collectedPub) {
        pubInfo.setIsCollected(1);
      }
    }
  }

  public void buildSnsPubIndexUrl(PubInfo pubInfo, Long psnId) {
    PubIndexUrl pubIndexUrl = pubIndexUrlService.get(pubInfo.getPubId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubInfo.setPubIndexUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }

  }

  public void buildSnsPubFulltext(PubInfo pubInfo, Long psnId) {
    PubFullTextPO fulltext = pubFullTextService.get(pubInfo.getPubId());
    // 同意全文请求的
    Boolean flag = pubFullTextReqBaseDao.isFullTextReqAgree(pubInfo.getPubId(), psnId, PubDbEnum.SNS);
    if (fulltext != null) {
      pubInfo.setHasFulltext(1);
      pubInfo.setFullTextFieId(fulltext.getFileId());
      pubInfo.setFullTextPermission(fulltext.getPermission());
      if ((pubInfo.getOwnerPsnId() != null && pubInfo.getOwnerPsnId().equals(psnId)) || fulltext.getPermission() == 0
          || flag) {
        // 公开的或自己的显示短地址，短地址不用登录
        pubInfo.setFullTextDownloadUrl(fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT,
            fulltext.getFileId(), pubInfo.getPubId()));
      } else {
        // SCM-19946 只要有全文的都显示全文图片
        pubInfo
            .setFullTextDownloadUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pubInfo.getPubId()));
      }

      pubInfo.setFullTextImgUrl(StringUtils.isNotBlank(fulltext.getThumbnailPath()) ? fulltext.getThumbnailPath()
          : domainscm + "/resmod/images_v5/images2016/file_img1.jpg");
    }
    if (flag) {
      pubInfo.setFullTextPermission(0);
    }
    pubInfo.setFullTextImgUrl(StringUtils.isNotBlank(pubInfo.getFullTextImgUrl()) ? pubInfo.getFullTextImgUrl()
        : domainscm + "/resmod/images_v5/images2016/file_img.jpg");
  }

  /**
   * 构建成果信息
   *
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public PubDetailVO buildPubTypeInfo(PubSnsDetailDOM detailDOM) {
    return PubDetailVoUtil.buildPubTypeInfo(detailDOM);
  }

  /**
   * 构建成果成员信息 因为MongoDB数据不完整，少了owne字段 所以才查询表的数据
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubMembersInfo(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {
    List<PdwhPubMemberPO> members = pdwhPubMemberService.findByPubId(detailDOM.getPubId());
    List<PubMemberBean> memberBeanList = detailDOM.getMembers();
    if (members != null && members.size() > 0) {
      for (PdwhPubMemberPO pdwhPubMemberPO : members) {
        PubMemberDTO pubMember = new PubMemberDTO();
        pubDetailVO.getMembers().add(pubMember);
        pubMember.setSeqNo(pdwhPubMemberPO.getSeqNo());
        pubMember.setPsnId(pdwhPubMemberPO.getPsnId());
        pubMember.setName(pdwhPubMemberPO.getName());
        if (memberBeanList != null && memberBeanList.size() > 0) {
          for (int i = 0; i < memberBeanList.size(); i++) {
            if (memberBeanList.get(i).getSeqNo().equals(pdwhPubMemberPO.getSeqNo())) {
              pubMember.setInsNames(buildInsNames(memberBeanList.get(i)));
            }
          }
        }
        pubMember.setEmail(pdwhPubMemberPO.getEmail());
        pubMember.setOwner(pdwhPubMemberPO.getOwner() == 1);
        pubMember.setCommunicable(pdwhPubMemberPO.getCommunicable() == 1);
        pubMember.setFirstAuthor(pdwhPubMemberPO.getFirstAuthor() == 1);
      }
    }
  }

  /**
   * 构建成果成员信息
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubMembersInfo(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    List<PubMemberPO> members = pubMemberService.findByPubId(detailDOM.getPubId());
    if (members != null && members.size() > 0) {
      for (PubMemberPO pubMemberPO : members) {
        PubMemberDTO pubMember = new PubMemberDTO();
        pubDetailVO.getMembers().add(pubMember);
        pubMember.setSeqNo(pubMemberPO.getSeqNo());
        pubMember.setPsnId(pubMemberPO.getPsnId());
        pubMember.setName(pubMemberPO.getName());
        pubMember.setInsNames(buildInsNames(pubMemberPO));
        pubMember.setEmail(pubMemberPO.getEmail());
        pubMember.setOwner(pubMemberPO.getOwner() == 1);
        pubMember.setCommunicable(pubMemberPO.getCommunicable() == 1);
        pubMember.setFirstAuthor(pubMemberPO.getFirstAuthor() == 1);
      }
    }
  }

  private List<MemberInsDTO> buildInsNames(PubMemberPO pubMemberPO) {
    List<MemberInsDTO> insNames = new ArrayList<MemberInsDTO>();
    MemberInsDTO memberIns = new MemberInsDTO();
    memberIns.setInsId(pubMemberPO.getInsId());
    memberIns.setInsName(pubMemberPO.getInsName());
    insNames.add(memberIns);
    return insNames;
  }

  private List<MemberInsDTO> buildInsNames(PubMemberBean memberBean) {
    List<MemberInsBean> memberInsBeans = memberBean.getInsNames();
    List<MemberInsDTO> insNames = new ArrayList<MemberInsDTO>();
    if (memberInsBeans != null && memberInsBeans.size() > 0) {
      MemberInsDTO memberIns = new MemberInsDTO();
      memberIns.setInsId(memberInsBeans.get(0).getInsId());
      memberIns.setInsName(memberInsBeans.get(0).getInsName());
      insNames.add(memberIns);
    }
    return insNames;
  }

  public void buildSnsPubIndexUrl(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    PubIndexUrl pubIndexUrl = pubIndexUrlService.get(detailDOM.getPubId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      pubDetailVO.setPubIndexUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }

  }

  /**
   * 构建全文, 不需要下载链接
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, PubSnsDetailDOM detailDOM) {
    // 全文逻辑id 主键唯一的
    PubFulltextDTO fullText = new PubFulltextDTO();
    PubFullTextPO fullTextPO = pubFullTextService.get(detailDOM.getPubId());
    if (fullTextPO != null) {
      String des3fileId = Des3Utils.encodeToDes3(Objects.toString(fullTextPO.getFileId()));
      fullText.setDes3fileId(des3fileId);
      fullText.setFileName(fullTextPO.getFileName());
      fullText.setPermission(fullTextPO.getPermission());
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        fullText.setThumbnailPath(this.domainscm + fullTextPO.getThumbnailPath());
      }
      pubDetailVO.setFullText(fullText);

    }
  }

  /**
   * 构建成果收录
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void buildPubSituation(PubDetailVO pubDetailVO, PubDetailDOM detailDOM) {
    Set<PubSituationBean> situations = detailDOM.getSituations();
    List<PubSituationDTO> list = new ArrayList<PubSituationDTO>();
    pubDetailVO.setSituations(list);
    PubSituationDTO sitation = null;
    if (situations != null && situations.size() > 0) {
      for (PubSituationBean pubSituationBean : situations) {
        sitation = new PubSituationDTO();
        String libraryName = PubLibraryEnum.parse(pubSituationBean.getLibraryName()).desc;
        sitation.setLibraryName(libraryName);
        sitation.setSitStatus(pubSituationBean.isSitStatus());
        sitation.setSitOriginStatus(pubSituationBean.isSitOriginStatus());
        list.add(sitation);
      }
    }

  }

  public PubDetailVO buildPubTypeInfo(PubPdwhDetailDOM detailDOM) {
    int pubType = detailDOM.getPubType();
    PubDetailVO pubDetailVO = null;
    PubTypeInfoBean pubTypeInfoBean = detailDOM.getTypeInfo();
    pubDetailVO = new PubDetailVO();
    switch (pubType) {
      case 1:
        AwardsInfoBean awardsInfoBean = (AwardsInfoBean) pubTypeInfoBean;
        if (awardsInfoBean != null) {
          AwardsInfoDTO awardsInfo = new AwardsInfoDTO();
          awardsInfo.setCategory(awardsInfoBean.getCategory());
          awardsInfo.setIssuingAuthority(awardsInfoBean.getIssuingAuthority());
          awardsInfo.setIssueInsId(awardsInfoBean.getIssueInsId());
          awardsInfo.setCertificateNo(awardsInfoBean.getCertificateNo());
          awardsInfo.setAwardDate(awardsInfoBean.getAwardDate());
          awardsInfo.setGrade(awardsInfoBean.getGrade());
          pubDetailVO.setPubTypeInfo(awardsInfo);
        }
        break;
      case 2:
      case 10:
        BookInfoBean bookInfoBean = (BookInfoBean) pubTypeInfoBean;
        if (bookInfoBean != null) {
          BookInfoDTO bookInfo = new BookInfoDTO();
          bookInfo.setName(bookInfoBean.getName());
          bookInfo.setSeriesName(bookInfoBean.getSeriesName());
          bookInfo.setEditors(bookInfoBean.getEditors());
          bookInfo.setISBN(bookInfoBean.getISBN());
          bookInfo.setPublisher(bookInfoBean.getPublisher());
          bookInfo.setTotalWords(bookInfoBean.getTotalWords());
          bookInfo.setType(bookInfoBean.getType());
          bookInfo.setTotalPages(bookInfoBean.getTotalPages());
          bookInfo.setChapterNo(bookInfoBean.getChapterNo());
          bookInfo.setPageNumber(bookInfoBean.getPageNumber());
          bookInfo.setLanguage(bookInfoBean.getLanguage());
          pubDetailVO.setPubTypeInfo(bookInfo);
        }

        break;
      case 3:
        ConferencePaperBean conferencePaperBean = (ConferencePaperBean) pubTypeInfoBean;
        if (conferencePaperBean != null) {
          ConferencePaperDTO conferencePaper = new ConferencePaperDTO();
          conferencePaper.setPaperType(conferencePaperBean.getPaperType());
          conferencePaper.setName(conferencePaperBean.getName());
          conferencePaper.setOrganizer(conferencePaperBean.getOrganizer());
          conferencePaper.setStartDate(conferencePaperBean.getStartDate());
          conferencePaper.setEndDate(conferencePaperBean.getEndDate());
          conferencePaper.setPageNumber(conferencePaperBean.getPageNumber());
          conferencePaper.setPapers(conferencePaperBean.getPapers());
          pubDetailVO.setPubTypeInfo(conferencePaper);
        }

        break;
      case 4:
        JournalInfoBean journalInfoBean = (JournalInfoBean) pubTypeInfoBean;
        if (journalInfoBean != null) {
          JournalInfoDTO journalInfo = new JournalInfoDTO();
          journalInfo.setJid(journalInfoBean.getJid());
          journalInfo.setName(journalInfoBean.getName());
          journalInfo.setVolumeNo(journalInfoBean.getVolumeNo());
          journalInfo.setIssue(journalInfoBean.getIssue());
          journalInfo.setPageNumber(journalInfoBean.getPageNumber());
          journalInfo.setPublishStatus(journalInfoBean.getPublishStatus());
          pubDetailVO.setPubTypeInfo(journalInfo);
        }

        break;
      case 5:
        PatentInfoBean patentInfoBean = (PatentInfoBean) pubTypeInfoBean;
        if (patentInfoBean != null) {
          PatentInfoDTO journalInfo = new PatentInfoDTO();
          journalInfo.setType(patentInfoBean.getType());
          journalInfo.setArea(patentInfoBean.getArea());
          journalInfo.setStatus(patentInfoBean.getStatus());
          journalInfo.setApplier(patentInfoBean.getApplier());
          journalInfo.setApplicationDate(patentInfoBean.getApplicationDate());
          journalInfo.setStartDate(patentInfoBean.getStartDate());
          journalInfo.setEndDate(patentInfoBean.getEndDate());
          journalInfo.setApplicationNo(patentInfoBean.getApplicationNo());
          journalInfo.setPublicationOpenNo(patentInfoBean.getPublicationOpenNo());
          journalInfo.setIPC(patentInfoBean.getIPC());
          journalInfo.setCPC(patentInfoBean.getCPC());
          PubPatentTransitionStatusEnum transitionStatusEnum = patentInfoBean.getTransitionStatus();
          journalInfo.setTransitionStatus(transitionStatusEnum);
          journalInfo.setPrice(patentInfoBean.getPrice());
          journalInfo.setIssuingAuthority(patentInfoBean.getIssuingAuthority());

          pubDetailVO.setPubTypeInfo(journalInfo);
        }

        break;
      case 7:
        break;
      case 8:
        ThesisInfoBean thesisInfoBean = (ThesisInfoBean) pubTypeInfoBean;
        if (thesisInfoBean != null) {
          ThesisInfoDTO thesisInfo = new ThesisInfoDTO();
          PubThesisDegreeEnum degreeEnum = thesisInfoBean.getDegree();
          thesisInfo.setDegree(degreeEnum);
          thesisInfo.setDefenseDate(thesisInfoBean.getDefenseDate());
          thesisInfo.setIssuingAuthority(thesisInfoBean.getIssuingAuthority());
          thesisInfo.setDepartment(thesisInfoBean.getDepartment());

          pubDetailVO.setPubTypeInfo(thesisInfo);
        }
        break;
      case 12:
        // 标准
        StandardInfoBean standardInfoBean = (StandardInfoBean) pubTypeInfoBean;
        if (standardInfoBean != null) {
          StandardInfoDTO standardInfo = new StandardInfoDTO();
          standardInfo.setPublishAgency(standardInfoBean.getPublishAgency());
          standardInfo.setStandardNo(standardInfoBean.getStandardNo());
          standardInfo.setTechnicalCommittees(standardInfoBean.getTechnicalCommittees());
          standardInfo.setType(standardInfoBean.getType());
          standardInfo.setIcsNo(standardInfoBean.getIcsNo());
          standardInfo.setDomainNo(standardInfoBean.getDomainNo());
          standardInfo.setImplementDate(standardInfoBean.getImplementDate());
          standardInfo.setObsoleteDate(standardInfoBean.getObsoleteDate());
          pubDetailVO.setPubTypeInfo(standardInfo);
        }
        break;
      case 13:
        // 软件著作权
        SoftwareCopyrightBean scBean = (SoftwareCopyrightBean) pubTypeInfoBean;
        if (scBean != null) {
          SoftwareCopyrightDTO scDto = new SoftwareCopyrightDTO();
          scDto.setRegisterNo(scBean.getRegisterNo());
          scDto.setAcquisitionType(scBean.getAcquisitionType());
          scDto.setScopeType(scBean.getScopeType());
          scDto.setCategoryNo(scBean.getCategoryNo());
          scDto.setFirstPublishDate(scBean.getFirstPublishDate());
          scDto.setPublicityDate(scBean.getPublicityDate());
          scDto.setRegisterDate(scBean.getRegisterDate());
          pubDetailVO.setPubTypeInfo(scDto);
        }
        break;
      default:
        break;
    }
    return pubDetailVO;
  }

  /**
   * 构建基准库全文
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPubFulltext(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {
    Long pdwhPubId = pubDetailVO.getPubId();
    PdwhPubFullTextPO fullTextPO = pdwhPubFullTextService.getPdwhPubfulltext(pdwhPubId);
    // 全文逻辑id 主键唯一的
    if (fullTextPO != null) {
      PubFulltextDTO fulltext = new PubFulltextDTO();
      fulltext.setFileId(fullTextPO.getFileId());
      fulltext.setFileName(fullTextPO.getFileName());
      pubDetailVO.setFullText(fulltext);
      if (StringUtils.isNotBlank(fullTextPO.getThumbnailPath())) {
        pubDetailVO.setFulltextImageUrl(this.domainscm + fullTextPO.getThumbnailPath());
        fulltext.setThumbnailPath(this.domainscm + fullTextPO.getThumbnailPath());
      }

    }
  }

  /**
   * 构建成果短地址
   *
   * @param pubDetailVO
   * @param detailDOM
   */
  @SuppressWarnings("rawtypes")
  public void buildPdwhPubIndex(PubDetailVO pubDetailVO, PubPdwhDetailDOM detailDOM) {

    String pubIndex = pdwhPubIndexUrlService.getIndexUrlByPubId(detailDOM.getPubId());
    if (StringUtils.isNotBlank(pubIndex)) {
      String pubShortIndex = this.domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pubIndex;
      pubDetailVO.setPubIndexUrl(pubShortIndex);
    }
  }

  /**
   * 获取国家城市 国家 == 一级 城市 == 省份+市
   * 
   * @param pubDetailVO
   */
  @SuppressWarnings("rawtypes")
  public void buildCountryRegionId(PubDetailVO pubDetailVO) {
    if (pubDetailVO.getCountryId() == null) {
      return;
    }
    ConstRegion constRegion = constRegionDao.findRegionNameById(pubDetailVO.getCountryId());
    String firstRegionName = "";
    String secondRegionName = "";
    String thirdRegionName = "";
    if (constRegion != null) {
      firstRegionName = constRegion.getZhName();
      if (constRegion.getSuperRegionId() != null) {
        constRegion = constRegionDao.findRegionNameById(constRegion.getSuperRegionId());
        secondRegionName = constRegion.getZhName();
        if (constRegion.getSuperRegionId() != null) {
          constRegion = constRegionDao.findRegionNameById(constRegion.getSuperRegionId());
          thirdRegionName = constRegion.getZhName();
        }
      }
    }
    if (StringUtils.isNotBlank(firstRegionName) && StringUtils.isNotBlank(secondRegionName)
        && StringUtils.isNotBlank(thirdRegionName)) {
      pubDetailVO.setCityName(secondRegionName + firstRegionName);
      pubDetailVO.setCountryName(thirdRegionName);
      return;
    }
    if (StringUtils.isNotBlank(firstRegionName) && StringUtils.isNotBlank(secondRegionName)) {
      pubDetailVO.setCountryName(secondRegionName);
      pubDetailVO.setCityName(firstRegionName);
      return;
    }
    if (StringUtils.isNotBlank(firstRegionName)) {
      pubDetailVO.setCountryName(firstRegionName);
      return;
    }
  }

  public boolean pubIsDelete(Long pubId) {
    PubPdwhPO pubPdwh = pubPdwhService.get(pubId);
    if (pubPdwh == null || pubPdwhDAO.checkPdwhIsDel(pubId, PubPdwhStatusEnum.DELETED)) {
      return true;
    }
    return false;
  }
}
